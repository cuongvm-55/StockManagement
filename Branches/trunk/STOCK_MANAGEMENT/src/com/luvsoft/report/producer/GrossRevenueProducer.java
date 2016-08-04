package com.luvsoft.report.producer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.luvsoft.DAO.CustomerModel;
import com.luvsoft.DAO.FilterObject;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Coupondetail;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.report.view.AbstractReportView;
import com.luvsoft.statistic.CustomerStatisticManager;
import com.luvsoft.statistic.Types.CouponType;
import com.luvsoft.utils.Utilities;

/**
 * 
 * @author cuongvm
 *
 */
//STT Mã hóa đơn  Ngày    Tên Khách   Diễn giải (xuất)    Tổng tiền
// order & return_customer_coupon
public class GrossRevenueProducer extends AbstractReportProducer{
    public static enum GrossMode{
        GrossRevenue, // Bao cao doanh thu
        GrossInput    // Bao cao hang nhap
    };
    
    private GrossMode grossMode;
    public GrossRevenueProducer(AbstractReportView<GrossRecord> v, GrossMode mode){
        view = v;
        grossMode = mode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> getFilterStatistic(Date from, Date to, FilterObject fo) {
        List<Object> retDatas = new ArrayList<Object>();
        // Get customer list
        CustomerModel csModel = new CustomerModel();
        List<AbstractEntity> csList = csModel.getFilterData(fo);
        // Sanity check
        if(csList == null || csList.isEmpty()){
            System.out.println("No customer found!");
            return retDatas;
        }

        CustomerStatisticManager csStatMgr = CustomerStatisticManager.getInstance();
        int sequence = 0;
        double sumAmount = 0.00d;
        for(int i = 0; i < csList.size(); i++){
            Customer cs = (Customer)csList.get(i);
            switch(grossMode){
            case GrossRevenue:
                {
                    // get all expected receiving amount from customer
                    // extracts amount from Order and Return to customer coupon
                    // Return provider coupon
                    List<CouponType> couponTypes = new ArrayList<CouponType>();
                    couponTypes.add(CouponType.Coupon_Return_Provider);
                    List<Object> retProviderCouponList = csStatMgr.retrieveCoupons(from, to, cs.getId(), couponTypes);
        
                    if( retProviderCouponList != null){
                        for( int idx = 0; idx < retProviderCouponList.size(); idx++ ){
                            Coupon cp = (Coupon)retProviderCouponList.get(idx);
                            cp.verifyObject();
                            Object[] details = cp.getCoupondetails().toArray();
                            // No details
                            if( details == null || details.length == 0 ){
                                continue; // check next coupon
                            }
                            double totalAmount = 0.0;
                            for( int j = 0; j < details.length; j++ ){
                                Coupondetail d = (Coupondetail)details[j];
                                totalAmount+= d.getPrice().doubleValue()*d.getQuantity();
                            }
        
                            retDatas.add(new GrossRecord((sequence+1),
                                    cp.getCode(),
                                    Utilities.DateToString(cp.getDate()),
                                    cp.getCustomer().getName(),
                                    Utilities.getNumberFormat().format(totalAmount),
                                    cp.getContent(),
                                    cp.getNote()));
                            sequence++;
                            sumAmount += totalAmount;
                        }
                    }
        
                    // Order
                    List<Object> orderList = csStatMgr.retrieveOrders(from, to, cs.getId());
                    if( orderList != null){
                        for( int idx = 0; idx < orderList.size(); idx++ ){
                            Order od = (Order)orderList.get(idx);
                            od.verifyObject();
                            Object[] details = od.getOrderdetails().toArray();
                            // No details
                            if( details == null || details.length == 0 ){
                                continue; // check next order
                            }
        
                            double totalAmount = 0.0;
                            for( int j = 0; j < details.length; j++ ){
                                Orderdetail d = (Orderdetail)details[j];
                                totalAmount+= d.getQuantityDelivered()*d.getPrice().doubleValue();
                            }
        
                            retDatas.add(new GrossRecord((sequence+1),
                                    od.getOrderCode(),
                                    Utilities.DateToString(od.getDate()),
                                    od.getCustomer().getName(),
                                    Utilities.getNumberFormat().format(totalAmount),
                                    od.getContent(),
                                    od.getNote()));
                            sequence++;
                            sumAmount += totalAmount;
                        }
                    }
                }
                break;
            case GrossInput:
                {
                    // extract all amount of buy_coupon and customer_return_coupon
                    List<CouponType> couponTypes = new ArrayList<CouponType>();
                    couponTypes.add(CouponType.Coupon_Buy);
                    couponTypes.add(CouponType.Coupon_Customer_Return);
                    List<Object> couponList = csStatMgr.retrieveCoupons(from, to, cs.getId(), couponTypes);
                    if( couponList != null ){

                        for( int idx = 0; idx < couponList.size(); idx++ ){
                            Coupon cp = (Coupon)couponList.get(idx);
                            cp.verifyObject();
                            Object[] details = cp.getCoupondetails().toArray();
                            // No details
                            if( details == null || details.length == 0 ){
                                continue; // check next coupon
                            }

                            double totalAmount = 0.00d;
                            for( int j = 0; j < details.length; j++ ){
                                Coupondetail d = (Coupondetail)details[j];
                                totalAmount+= d.getPrice().doubleValue()*d.getQuantity();
                            }

                            retDatas.add(new GrossRecord((sequence+1),
                                    cp.getCode(),
                                    Utilities.DateToString(cp.getDate()),
                                    cp.getCustomer().getName(),
                                    Utilities.getNumberFormat().format(totalAmount),
                                    cp.getContent(),
                                    cp.getNote()));
                            sequence++;
                            sumAmount += totalAmount;
                        }
                    }
                }
                break;
            default:
                break;
            }
        }
        view.getSumDataList().put("totalAmount", Utilities.getNumberFormat().format(sumAmount));
        return retDatas;
    }

    public class GrossRecord{
        int sequence;
        String orderCode; // for both orderId and Return_customer_couponID
        String date;
        String customerName;
        String totalAmount;
        String content;
        String note;

        public GrossRecord(int sequence, String orderCode, String date,
                String customerName, String totalAmount, String content,
                String note) {
            super();
            this.sequence = sequence;
            this.orderCode = orderCode;
            this.date = date;
            this.customerName = customerName;
            this.totalAmount = totalAmount;
            this.content = content;
            this.note = note;
        }

        public int getSequence() {
            return sequence;
        }
        public void setSequence(int sequence) {
            this.sequence = sequence;
        }
        public String getOrderCode() {
            return orderCode;
        }
        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public String getCustomerName() {
            return customerName;
        }
        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }
        public String getTotalAmount() {
            return totalAmount;
        }
        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public String getNote() {
            return note;
        }
        public void setNote(String note) {
            this.note = note;
        }
        @Override
        public String toString() {
            return "GrossRevenueRecord [sequence=" + sequence + ", orderCode="
                    + orderCode + ", date=" + date + ", customerName="
                    + customerName + ", totalAmount=" + totalAmount
                    + ", content=" + content + ", note=" + note + "]";
        }

    }
}
