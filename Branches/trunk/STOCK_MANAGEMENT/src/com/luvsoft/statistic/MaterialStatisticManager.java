package com.luvsoft.statistic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.luvsoft.DAO.EntityManagerDAO;
import com.luvsoft.entities.Coupondetail;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialhistory;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.statistic.Types.CouponType;
import com.luvsoft.statistic.Types.StatRecord;
import com.luvsoft.utils.Utilities;

/**
 * 
 * @author cuongvm
 *
 */
public class MaterialStatisticManager {
    private static MaterialStatisticManager instance = null;

    public static MaterialStatisticManager getInstance(){
        if( instance == null ){
            instance = new MaterialStatisticManager();
        }

        return instance;
    }

    /**
     * Get input stats (Coupon_Buy, Coupon_Customer_Return) in date range [fromDate, toDate]
     * @param from
     * @param to
     * @param materialId
     * @param quantity The accumulated input quantity
     * @param amount   The accumulated input amount  (spent amount)
     */
    public void extractInputStatisticInDateRange(Date from, Date to, int materialId, StatRecord record){
        List<Object> couponDetailList = retrieveCouponDetailsInDateRange( // [from, date]
                from,
                to,
                materialId);

        for( int i = 0; i < couponDetailList.size(); i++ ){
            Coupondetail couponDetail = (Coupondetail)couponDetailList.get(i);
            Integer cpType = couponDetail.getCoupon().getCoupontype().getId();
            // input values
            if( cpType == CouponType.Coupon_Buy.getValue()
                    || cpType == CouponType.Coupon_Customer_Return.getValue()){
                record.quantity += couponDetail.getQuantity();
                record.amount   += couponDetail.getQuantity() * couponDetail.getPrice().doubleValue();
            }
        }
    }

    /**
     * Get output stats (Coupon_Return_Provider, Order) in date range [fromDate, toDate]
     * @param from
     * @param to
     * @param materialId
     * @param quantity The accumulated output quantity
     * @param amount   The accumulated out amount  (received amount)
     */
    public void extractOutputStatisticInDateRange(Date from, Date to, int materialId, StatRecord record){
        // Coupons
        List<Object> couponDetailList = retrieveCouponDetailsInDateRange( // [from, date]
                from,
                to,
                materialId);

        for( int i = 0; i < couponDetailList.size(); i++ ){
            Coupondetail couponDetail = (Coupondetail)couponDetailList.get(i);
            Integer cpType = couponDetail.getCoupon().getCoupontype().getId();
            // output values
            if( cpType == CouponType.Coupon_Return_Provider.getValue() ){
                record.quantity += couponDetail.getQuantity();
                record.amount += couponDetail.getQuantity() * couponDetail.getPrice().doubleValue();
            }
        }
        
        // Orders
        List<Object> orderDetailList = retrieveOrderDetailsInDateRange(
                from,
                to,
                materialId);
        for( int i = 0; i < orderDetailList.size(); i++ ){
            Orderdetail orderDetail = (Orderdetail)orderDetailList.get(i);
            // output values
            record.quantity += orderDetail.getQuantityDelivered();
            record.amount += orderDetail.getQuantityDelivered() * orderDetail.getPrice().doubleValue();
        }

    }
    
    /**
     * get inventory material consumed till the datePoint [..., datePoint)
     * The statistic at datePoint will not be calculated
     * @param datePoint the next date that the MaterialHistory was consumed
     * @return
     */
    public Materialhistory getInventoryMaterialAtDatePoint(Date datePoint, Material material){
        // First, get the nearest MaterialHistory according to datePoint
        // We have the inventory consumed of previous month
        Materialhistory nearestMH = getNearestMaterialHistory(datePoint, material.getId());

        // Now look at all the Coupondetail and Orderdetail in the interval [nearestMH.date, datePoint)
        StatRecord inputRecord = new StatRecord();
        StatRecord outputRecord = new StatRecord();
        
        // Input stats in [nearestMH.date, datePoint-1]
        extractInputStatisticInDateRange(
                nearestMH.getDate(),
                Utilities.addDate(datePoint, -1),
                material.getId(),
                inputRecord);
        
        // Output stats in [nearestMH.date, datePoint-1]
        extractOutputStatisticInDateRange(
                nearestMH.getDate(),
                Utilities.addDate(datePoint, -1),
                material.getId(),
                outputRecord);
        
        // Now calculate the average price till datePoint
        // The nearest average price and inventory quantity must be taken into account
        Double totalAmount = nearestMH.getAverageInputPrice() != null ? 
                                 (nearestMH.getAverageInputPrice().doubleValue() * nearestMH.getQuantity() + inputRecord.amount)
                                 : inputRecord.amount;
        int totalQuantity = nearestMH.getQuantity() + inputRecord.quantity;
        Double avgPrice = totalQuantity != 0 ? totalAmount/totalQuantity : 0.00d;

        Materialhistory mh = new Materialhistory();
        mh.setDate(datePoint);
        mh.setAverageInputPrice(new BigDecimal(avgPrice));
        mh.setQuantity(nearestMH.getQuantity() + inputRecord.quantity - outputRecord.quantity);
        mh.setMaterial(material);

        return mh;
    }
    
    /**
     * Gets nearest Materialhistory in (..., datePoint]
     * It should be the consumed inventory material record of previous month
     * @param datePoint
     * @param materialId
     * @return
     */
    private Materialhistory getNearestMaterialHistory(Date datePoint, Integer materialId){
        // First, get the nearest MaterialHistory according to datePoint
        Materialhistory h = new Materialhistory();
        h.verifyObject();
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Materialhistory.getEntityname()+" e "
                + "WHERE e.date <= :var0 "
                + "AND material.id = :var1 "
                + "ORDER BY date DESC"; // get only the last record
        List<Object> params = new ArrayList<Object>();
        params.add(datePoint);
        params.add(materialId);

        List<Object> retsults = EntityManagerDAO.getInstance().findByQueryWithLimit(QUERY, params, 1); // only 1 record
        if( !retsults.isEmpty() ){
            // get the first history record
            h = (Materialhistory)retsults.get(0);
            h.verifyObject();
        }

        return h;
    }
    
    /**
     * get all Coupondetail in [from, to] period of material id
     * @param from
     * @param to
     * @return
     */
    private List<Object> retrieveCouponDetailsInDateRange(Date from, Date to, Integer materialId){
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Coupondetail.getEntityname()+" e "
                + "WHERE coupon.date >= :var0 AND coupon.date <= :var1 "
                + "AND material.id = :var2";
        List<Object> params = new ArrayList<Object>();
        params.add(from);
        params.add(to);
        params.add(materialId);
        return EntityManagerDAO.getInstance().findByQuery(QUERY, params);
    }

    /**
     * get all Orderdetail in [from, to] period of material id
     * @param from
     * @param to
     * @return
     */
    private List<Object> retrieveOrderDetailsInDateRange(Date from, Date to, Integer materialId){
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Orderdetail.getEntityname()+" e "
                + "WHERE order.date >= :var0 AND order.date <= :var1 "
                + "AND material.id = :var2";
        List<Object> params = new ArrayList<Object>();
        params.add(from);
        params.add(to);
        params.add(materialId);
        return EntityManagerDAO.getInstance().findByQuery(QUERY, params);
    }

    /**
     * We will consume and save the Material history when we reach new month
     * The Materialhistory record at datePoint is the inventory information
     * of a material in the interval (..., datePoint)
     * 
     * That means the statistic at daetPoint will not be calculated in that record
     * 
     * @param datepoint
     * @param material
     */
    public void consumeMaterialHistory(Date datePoint, Material material){
        System.out.println("Consume material history...");
        // Check if the consuming has already done for the datePoint
        if( !isConsumMaterialHistoryNeeded(datePoint, material.getId()) ){
            return;
        }

        // Consuming
        Materialhistory newMH = getInventoryMaterialAtDatePoint(datePoint, material);

        // Save the new record for datePoint
        EntityManagerDAO enMgr = EntityManagerDAO.getInstance();
        enMgr.addNew(newMH);
    }

    /**
     * Check if the consuming is needed
     * @param datePoint
     * @param materialId
     * @return
     */
    public boolean isConsumMaterialHistoryNeeded(Date datePoint, Integer materialId){
        // We will consume the history only one time for each month
        // So check if the consuming has already done for this month (i.e: datePoint.month)
        Materialhistory nearestMH = getNearestMaterialHistory(datePoint, materialId);
        Calendar cal = Calendar.getInstance();
        cal.setTime(nearestMH.getDate());
        
        int nearestMHMonth = cal.get(Calendar.MONTH);
        int nearestMHYear  = cal.get(Calendar.YEAR);

        cal.setTime(datePoint);
        if( nearestMHYear >= cal.get(Calendar.YEAR) &&
                nearestMHMonth >= cal.get(Calendar.MONTH) ){
            return false; // the record for this month is already consumed
        }

        return true;
    }
}
