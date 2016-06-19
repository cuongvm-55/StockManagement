package com.luvsoft.statistic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.luvsoft.DAO.EntityManagerDAO;
import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Coupondetail;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customerhistory;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.entities.Receivingbill;
import com.luvsoft.entities.Receivingbilldetail;
import com.luvsoft.entities.Spendingbill;
import com.luvsoft.entities.Spendingbilldetail;
import com.luvsoft.statistic.Types.CouponType;
import com.luvsoft.utils.Utilities;


public class CustomerStatisticManager {
    private static CustomerStatisticManager instance = null;
    
    public static CustomerStatisticManager getInstance(){
        if( instance == null ){
            instance = new CustomerStatisticManager();
        }
        
        return instance;
    }

    /**
     * Gets total debt amount of customer
     * @param datePoint
     * @param customer
     * @return
     */
    public Customerhistory getTotalDebtAmountAtDatePoint(Date datePoint, Customer customer){
        // First, get the nearest Customerhistory according to datePoint
        // We have the inventory consumed of previous month
        Customerhistory ch = getNearestCustomerHistory(datePoint, customer.getId());

        // Get total spend amount in interval [ch.date, datePoint-1]
        double spentAmount = getTotalSpendingAmount(
                ch.getDate(),
                Utilities.addDate(datePoint, -1),
                customer.getId());

        // Get expected spending amount for this customer in [ch.date, datePoint-1]
        double expectedSpendingAmount = getExpectedTotalSpendingAmount(
                ch.getDate(),
                Utilities.addDate(datePoint, -1),
                customer.getId());

        // Get total received amount in interval [ch.date, datePoint-1]
        double receivedAmount = getTotalReceivingAmount(
                ch.getDate(),
                Utilities.addDate(datePoint, -1),
                customer.getId());

        // Get expected receiving amount for this customer in [ch.date, datePoint-1]
        double expectedReceivingAmount = getExpectedTotalReceivingAmount(
                ch.getDate(),
                Utilities.addDate(datePoint, -1),
                customer.getId());
        
        // debt is negative means we       owe  customer
        // debt is positive means customer owes us
        double totalDebt = ch.getDebt().doubleValue();
        totalDebt += (spentAmount - expectedSpendingAmount);     // debt = debt + spendingAmount
        totalDebt -= (receivedAmount - expectedReceivingAmount); // debt = debt - receivingAmount

        Customerhistory ret = new Customerhistory();
        ret.setCustomer(customer);
        ret.setDate(datePoint);
        ret.setDebt(new BigDecimal(totalDebt));

        return ret;
    }

    /**
     * Gets nearest Customerhistory in (..., datePoint]
     * It should be the consumed debt of customer of previous month
     * @param datePoint
     * @param customerId
     * @return
     */
    public Customerhistory getNearestCustomerHistory(Date datePoint, Integer customerId){
        // First, get the nearest MaterialHistory according to datePoint
        Customerhistory h = new Customerhistory();
        h.verifyObject();
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Customerhistory.getEntityname()+" e "
                + "WHERE e.date <= :var0 "
                + "AND customer.id = :var1 "
                + "ORDER BY date DESC"; // get only the last record
        List<Object> params = new ArrayList<Object>();
        params.add(datePoint);
        params.add(customerId);

        List<Object> retsults = EntityManagerDAO.getInstance().findByQuery(QUERY, params);
        if( !retsults.isEmpty() ){
            // get the first history record
            h = (Customerhistory)retsults.get(0);
            h.verifyObject();
        }

        return h;
    }
    
    /**
     * Retrieves all the receiving bills of customerId fall in interval [from, to]
     * @param from
     * @param to
     * @param customerId
     * @return
     */
    private List<Object> retrieveReceivingBills(Date from, Date to, Integer customerId){
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Receivingbill.getEntityname()+" e "
                + "WHERE date >= :var0 AND date <= :var1 "
                + "AND customer.id = :var2";
        List<Object> params = new ArrayList<Object>();
        params.add(from);
        params.add(to);
        params.add(customerId);
        return EntityManagerDAO.getInstance().findByQuery(QUERY, params);
    }

    /**
     * Retrieves all the spending bills of customerId fall in interval [from, to]
     * @param from
     * @param to
     * @param customerId
     * @return
     */
    private List<Object> retrieveSpendingBills(Date from, Date to, Integer customerId){
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Spendingbill.getEntityname()+" e "
                + "WHERE date >= :var0 AND date <= :var1 "
                + "AND customer.id = :var2";
        List<Object> params = new ArrayList<Object>();
        params.add(from);
        params.add(to);
        params.add(customerId);
        return EntityManagerDAO.getInstance().findByQuery(QUERY, params);
    }
    
    /**
     * Gets total amount that we spent for the customerId
     * @param from
     * @param to
     * @param customerId
     * @return
     */
    public double getTotalSpendingAmount(Date from, Date to, Integer customerId){
        List<Object> spendingBillList = retrieveSpendingBills(from, to, customerId);
        // No bills
        if( spendingBillList == null || spendingBillList.isEmpty() ){
            return 0.00d;
        }
        
        double totalAmount = 0.00d;
        for( int i = 0; i < spendingBillList.size(); i++ ){
            Spendingbill bill = (Spendingbill)spendingBillList.get(i);
            Object[] details = bill.getSpendingbilldetails().toArray();
             // No details
             if( details == null || details.length == 0 ){
                 continue; // check next bill
             }
            for( int j = 0; j < details.length; j++ ){
                Spendingbilldetail d = (Spendingbilldetail)details[j];
                totalAmount+= d.getAmount().doubleValue();
            }
        }
        
        return totalAmount;
    }
    
    /**
     * Gets total amount that we received from the customerId
     * @param from
     * @param to
     * @param customerId
     * @return
     */
    public double getTotalReceivingAmount(Date from, Date to, Integer customerId){
        List<Object> receiveBillList = retrieveReceivingBills(from, to, customerId);
        // No bills
        if( receiveBillList == null || receiveBillList.isEmpty() ){
            return 0.00d;
        }
        
        double totalAmount = 0.00d;
        for( int i = 0; i < receiveBillList.size(); i++ ){
            Receivingbill bill = (Receivingbill)receiveBillList.get(i);
            Object[] details = bill.getReceivingbilldetails().toArray();
             // No details
             if( details == null || details.length == 0 ){
                 continue; // check next bill
             }
            for( int j = 0; j < details.length; j++ ){
                Receivingbilldetail d = (Receivingbilldetail)details[j];
                totalAmount+= d.getAmount().doubleValue();
            }
        }

        return totalAmount;
    }
    
    /**
     * We will consume and save the Customer history when we reach new month
     * The Customerhistory record at datePoint is the inventory information
     * of a customer in the interval (..., datePoint)
     * 
     * That means the statistic at datePoint will not be calculated in that record
     * 
     * @param datepoint
     * @param material
     */
    public void consumeCustomerHistory(Date datePoint, Customer customer){
        System.out.println("Consume customer history...");
        // Check if the consuming has already done for the datePoint
        if( !isConsumeCustomerHistoryNeeded(datePoint, customer.getId()) ){
            return;
        }

        // Consuming
        Customerhistory newCH = getTotalDebtAmountAtDatePoint(datePoint, customer);

        // Save the new record for datePoint
        EntityManagerDAO enMgr = EntityManagerDAO.getInstance();
        enMgr.addNew(newCH);
    }

    /**
     * Check if the consuming is needed
     * @param datePoint
     * @param materialId
     * @return
     */
    public boolean isConsumeCustomerHistoryNeeded(Date datePoint, Integer customerId){
        // We will consume the history only one time for each month
        // So check if the consuming has already done for this month (i.e: datePoint.month)
        Customerhistory nearestCH = getNearestCustomerHistory(datePoint, customerId);
        Calendar cal = Calendar.getInstance();
        cal.setTime(nearestCH.getDate());

        int nearestMHMonth = cal.get(Calendar.MONTH);
        int nearestMHYear  = cal.get(Calendar.YEAR);

        cal.setTime(datePoint);
        if( nearestMHYear >= cal.get(Calendar.YEAR) &&
                nearestMHMonth >= cal.get(Calendar.MONTH) ){
            return false; // the record for this month is already consumed
        }

        return true;
    }

    /**
     * Gets all Coupon of CouponType type of customerId in interval [from, to]
     * @param from
     * @param to
     * @param customerId
     * @param type
     * @return
     */
    public List<Object> retrieveCoupons(Date from, Date to, Integer customerId, CouponType type){
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Coupon.getEntityname()+" e "
                + "WHERE date >= :var0 AND date <= :var1 "
                + "AND customer.id = :var2 "
                + "AND coupontype.id = :var3";
        List<Object> params = new ArrayList<Object>();
        params.add(from);
        params.add(to);
        params.add(customerId);
        params.add(type.getValue());
        return EntityManagerDAO.getInstance().findByQuery(QUERY, params);
    }

    /**
     * Gets all Order of customerId in interval [from, to]
     * @param from
     * @param to
     * @param customerId
     * @return
     */
    public List<Object> retrieveOrders(Date from, Date to, Integer customerId){
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Order.getEntityname()+" e "
                + "WHERE date >= :var0 AND date <= :var1 "
                + "AND customer.id = :var2";
        List<Object> params = new ArrayList<Object>();
        params.add(from);
        params.add(to);
        params.add(customerId);
        return EntityManagerDAO.getInstance().findByQuery(QUERY, params);
    }

    /**
     * Gets the expected total amount that we spend to customer
     * Extracts total amount from buy_coupon and customer_return_coupon
     * @param from
     * @param to
     * @param customerId
     * @return
     */
    public double getExpectedTotalSpendingAmount(Date from, Date to, Integer customerId){
        List<Object> buyCouponList = retrieveCoupons(from, to, customerId, CouponType.Coupon_Buy);
        List<Object> customerReturnCouponList = retrieveCoupons(from, to, customerId, CouponType.Coupon_Customer_Return);
        List<Object> couponList = new ArrayList<Object>();
        couponList.addAll(buyCouponList);
        couponList.addAll(customerReturnCouponList);

        if( couponList.isEmpty() ){
            return 0.00d;
        }

        double totalAmount = 0.00d;
        for( int i = 0; i < couponList.size(); i++ ){
            Coupon cp = (Coupon)couponList.get(i);
            cp.verifyObject();
            Object[] details = cp.getCoupondetails().toArray();
            // No details
            if( details == null || details.length == 0 ){
                continue; // check next coupon
            }

            for( int j = 0; j < details.length; j++ ){
                Coupondetail d = (Coupondetail)details[i];
                totalAmount+= d.getPrice().doubleValue()*d.getQuantity();
            }
        }

        return totalAmount;
    }
    
    /**
     * Gets the expected total amount that we receive from customer
     * Extracts total amount from order and return_provider_coupon
     * @param from
     * @param to
     * @param customerId
     * @return
     */
    public double getExpectedTotalReceivingAmount(Date from, Date to, Integer customerId){
        // Return provider coupon
        List<Object> retProviderCouponList = retrieveCoupons(from, to, customerId, CouponType.Coupon_Return_Provider);
        double totalAmount = 0.00d;
        if( retProviderCouponList != null){
            for( int i = 0; i < retProviderCouponList.size(); i++ ){
                Coupon cp = (Coupon)retProviderCouponList.get(i);
                cp.verifyObject();
                Object[] details = cp.getCoupondetails().toArray();
                // No details
                if( details == null || details.length == 0 ){
                    continue; // check next coupon
                }
    
                for( int j = 0; j < details.length; j++ ){
                    Coupondetail d = (Coupondetail)details[i];
                    totalAmount+= d.getPrice().doubleValue()*d.getQuantity();
                }
            }
        }

        // Order
        List<Object> orderList = retrieveOrders(from, to, customerId);
        if( orderList != null){
            for( int i = 0; i < orderList.size(); i++ ){
                Order od = (Order)orderList.get(i);
                od.verifyObject();
                Object[] details = od.getOrderdetails().toArray();
                // No details
                if( details == null || details.length == 0 ){
                    continue; // check next order
                }
    
                for( int j = 0; j < details.length; j++ ){
                    Orderdetail d = (Orderdetail)details[i];
                    totalAmount+= d.getQuantityDelivered()*d.getPrice().doubleValue();
                }
            }
        }
        return totalAmount;
    }
}
