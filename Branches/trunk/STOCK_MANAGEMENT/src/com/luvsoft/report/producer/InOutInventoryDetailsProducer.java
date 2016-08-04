package com.luvsoft.report.producer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Coupondetail;
import com.luvsoft.entities.Coupontype;
import com.luvsoft.entities.Coupontype.COUPON_TYPES;
import com.luvsoft.entities.Materialhistory;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.report.view.InOutInventoryReportDetailsView;
import com.luvsoft.statistic.MaterialStatisticManager;
import com.luvsoft.statistic.Types.StatRecord;
import com.luvsoft.utils.Utilities;

public class InOutInventoryDetailsProducer extends AbstractReportDetailsProducer{

    public InOutInventoryDetailsProducer(InOutInventoryReportDetailsView view){
        this.view = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void generateReport(Date from, Date to, String objectCode) {
        MaterialStatisticManager mtStatMgr = MaterialStatisticManager.getInstance();
        List<Object> lsObjects = mtStatMgr.retrieveCouponDetailsInDateRange(from, to, objectCode);

        List<InOutInventoryDetailsRecord> lsRecords = new ArrayList<InOutInventoryDetailsRecord>();

        // Logical of below code like that:
        // - Get the list of CouponDetail has the same objectCode (e.g materialCode) - lsObjects
        // - Create the list of InOutInventoryDetailsRecord records - lsRecords
        // - For each CouponDetail, we will find it in lsRecords by Coupon Code
        // - If not found, we will a new record and add it to list
        // - If found, we will add more in/out put quantity
        // - Absolutely, there is no duplicate Coupon Code for any couple records
        // - Do the same thing with OrderDetail
        int count = 0;
        for (Object object : lsObjects) {
            Coupondetail cpDetail = (Coupondetail) object;

            boolean isFound = false;
            for (InOutInventoryDetailsRecord record : lsRecords) {
                if (record.getCode().equals(cpDetail.getCoupon().getCode())) {

                    Coupontype type = cpDetail.getCoupon().getCoupontype();

                    if (type.getName().equals(Coupontype.GetStringFromTypes(COUPON_TYPES.PH_NHAPMUA)) ||
                       type.getName().equals(Coupontype.GetStringFromTypes(COUPON_TYPES.PH_NHAPHANGTRALAI))) {
                        record.setInputQuantity(cpDetail.getQuantity() + record.getInputQuantity());
                    }
                    else if (type.getName().equals(Coupontype.GetStringFromTypes(COUPON_TYPES.PH_XUATTRANHACUNGCAP))) {
                        record.setOutputQuantity(cpDetail.getQuantity() + record.getOutputQuantity());
                    }
                    isFound = true;
                    break;
                }
            }

            if (isFound == false) {
                Coupon coupon = cpDetail.getCoupon();
                InOutInventoryDetailsRecord record = new InOutInventoryDetailsRecord();
                record.setSequence(++count);
                record.setCode(coupon.getCode());
                record.setContent(coupon.getContent());
                record.setCustomer(coupon.getCustomer().getCode());
                record.setDate(coupon.getDate());

                Coupontype type = cpDetail.getCoupon().getCoupontype();
                if (type.getName().equals(Coupontype.GetStringFromTypes(COUPON_TYPES.PH_NHAPMUA)) ||
                   type.getName().equals(Coupontype.GetStringFromTypes(COUPON_TYPES.PH_NHAPHANGTRALAI))) {
                    record.setInputQuantity(cpDetail.getQuantity() + record.getInputQuantity());
                 }
                 else if (type.getName().equals(Coupontype.GetStringFromTypes(COUPON_TYPES.PH_XUATTRANHACUNGCAP))) {
                    record.setOutputQuantity(cpDetail.getQuantity() + record.getOutputQuantity());
                 }

                lsRecords.add(record);
            }
        }

        List<InOutInventoryDetailsRecord> lsOrderRecords = new ArrayList<InOutInventoryDetailsRecord>();
        lsObjects = mtStatMgr.retrieveOrderDetailsInDateRange(from, to, objectCode);
        for (Object object : lsObjects) {
            Orderdetail odDetail = (Orderdetail) object;

            boolean isFound = false;
            for (InOutInventoryDetailsRecord record : lsOrderRecords) {
                if (record.getCode().equals(odDetail.getOrder().getOrderCode())) {
                    record.setOutputQuantity(odDetail.getQuantityDelivered() + record.getOutputQuantity());
                    isFound = true;
                    break;
                }
            }

            if (isFound == false) {
                Order order = odDetail.getOrder();
                InOutInventoryDetailsRecord record = new InOutInventoryDetailsRecord();
                record.setSequence(++count);
                record.setCode(order.getOrderCode());
                record.setContent(order.getContent());
                record.setCustomer(order.getCustomer().getCode());
                record.setDate(order.getDate());

                record.setOutputQuantity(odDetail.getQuantityDelivered() + record.getOutputQuantity());

                lsOrderRecords.add(record);
            }
        }

        lsRecords.addAll(lsOrderRecords);
        view.setTable(lsRecords);
    }

    @Override
    public void generateSummaryReportDetails(Date fromDate, Date toDate, String objectCode) {
        int remainingBefore = 0;
        int inputIn = 0;
        int outputIn = 0;
        int remainingEnd = 0;

        MaterialStatisticManager mtStatMgr = MaterialStatisticManager.getInstance();
        Materialhistory mtHistory = mtStatMgr.getInventoryMaterialAtDatePoint(fromDate, objectCode);
        remainingBefore = mtHistory.getQuantity();

        StatRecord inputStRecord = new StatRecord();
        mtStatMgr.extractInputStatisticInDateRange(fromDate, toDate, objectCode, inputStRecord);
        inputIn = inputStRecord.quantity;

        StatRecord outputStRecord = new StatRecord();
        mtStatMgr.extractOutputStatisticInDateRange(fromDate, toDate, objectCode, outputStRecord);
        outputIn = outputStRecord.quantity;

        remainingEnd = remainingBefore + inputIn - outputIn;
        view.setSummaryValues(remainingBefore, inputIn, outputIn, remainingEnd);
    }

    public class InOutInventoryDetailsRecord {
        private int sequence;
        private String code; // order code
        private Date date; // order date
        private String customer;
        private String content;

        private int inputQuantity;
        private int outputQuantity;
        public InOutInventoryDetailsRecord(){
            sequence = 0;
            code = "";
            date = Utilities.StringToDate("1/1/2000 01:00:00");
            inputQuantity = 0;
            outputQuantity = 0;
        }
        public InOutInventoryDetailsRecord(int sequence, String code, Date date, int inputQuantity, int outputQuantity) {
            super();
            this.sequence = sequence;
            this.code = code;
            this.date = date;
            this.inputQuantity = inputQuantity;
            this.outputQuantity = outputQuantity;
        }

        public int getSequence() {
            return sequence;
        }
        public void setSequence(int sequence) {
            this.sequence = sequence;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public String getCustomer() {
            return customer;
        }
        public void setCustomer(String customer) {
            this.customer = customer;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public int getInputQuantity() {
            return inputQuantity;
        }
        public void setInputQuantity(int inputQuantity) {
            this.inputQuantity = inputQuantity;
        }
        public int getOutputQuantity() {
            return outputQuantity;
        }
        public void setOutputQuantity(int outputQuantity) {
            this.outputQuantity = outputQuantity;
        }
        @Override
        public String toString() {
            return "InOutInventoryRecord [sequence=" + sequence + ", code="
                    + code + ", name=" + date.toString()
                    + ", inputQuantity=" + inputQuantity
                    + ",outputQuantity=" + outputQuantity + "]";
        }
    }
}
