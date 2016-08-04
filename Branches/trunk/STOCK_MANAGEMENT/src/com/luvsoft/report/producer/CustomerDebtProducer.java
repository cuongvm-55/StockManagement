package com.luvsoft.report.producer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.luvsoft.DAO.CustomerModel;
import com.luvsoft.DAO.FilterObject;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customerhistory;
import com.luvsoft.report.view.AbstractReportView;
import com.luvsoft.statistic.CustomerStatisticManager;
import com.luvsoft.utils.Utilities;

public class CustomerDebtProducer extends AbstractReportProducer{
    public CustomerDebtProducer(AbstractReportView<DebtRecord> v){
        view = v;
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
        double sumExpectedOutAmount = 0.00;
        double sumExpectedInAmount = 0.00;
        double sumOrgAmount = 0.0;
        double sumOutAmount = 0.0;
        double sumInAmount = 0.0;
        double sumInvAmount = 0.0;

        for(int i = 0; i < csList.size(); i++){
            Customer customer = (Customer)csList.get(i);
            Customerhistory csHistory = csStatMgr.getTotalDebtAmountAtDatePoint(from, customer);
            // Get total spend amount in interval [from, to]
            double spentAmount = csStatMgr.getTotalSpendingAmount(from, to, customer.getId());
            // Get expected spending amount for this customer in [from, to]
            double expectedSpendingAmount = csStatMgr.getExpectedTotalSpendingAmount(from, to, customer.getId());
            // Get total received amount in interval [from, to]
            double receivedAmount = csStatMgr.getTotalReceivingAmount(from, to, customer.getId());
            // Get expected receiving amount for this customer in [from, to]
            double expectedReceivingAmount = csStatMgr.getExpectedTotalReceivingAmount(from, to, customer.getId());

            // no_cuoi = no_dau + no_trong_ki - tien_minh_no_KH_trong_ki 
            double ivtAmount = csHistory.getDebt().doubleValue() + (expectedReceivingAmount - receivedAmount) - (expectedSpendingAmount - spentAmount)/*spentAmount - receivedAmount*/;
            retDatas.add(new DebtRecord(i+1,
                    customer.getCode(),
                    customer.getName(),
                    Utilities.getNumberFormat().format(csHistory.getDebt()),
                    Utilities.getNumberFormat().format(expectedSpendingAmount),
                    Utilities.getNumberFormat().format(spentAmount),
                    Utilities.getNumberFormat().format(expectedReceivingAmount),
                    Utilities.getNumberFormat().format(receivedAmount),
                    Utilities.getNumberFormat().format(ivtAmount)));

            sumExpectedOutAmount += expectedSpendingAmount;
            sumOrgAmount         += csHistory.getDebt().doubleValue();
            sumOutAmount         += spentAmount;
            sumExpectedInAmount += expectedReceivingAmount;
            sumInAmount          += receivedAmount;
            sumInvAmount         += ivtAmount;
        }

        // set sum data
        view.getSumDataList().put("orgAmount", Utilities.getNumberFormat().format(sumOrgAmount));
        view.getSumDataList().put("expectedOutAmount", Utilities.getNumberFormat().format(sumExpectedOutAmount));
        view.getSumDataList().put("outAmount", Utilities.getNumberFormat().format(sumOutAmount));
        view.getSumDataList().put("expectedInAmount", Utilities.getNumberFormat().format(sumExpectedInAmount));
        view.getSumDataList().put("inAmount", Utilities.getNumberFormat().format(sumInAmount));
        view.getSumDataList().put("ivtAmount", Utilities.getNumberFormat().format(sumInvAmount));

        return retDatas;
    }

    public class DebtRecord{
        private int sequence;
        private String customerCode;
        private String customerName;
        private String orgAmount;         // origin debt
        private String expectedOutAmount; // total expected amount that we should spend on this customer
        private String outAmount;         // it's the amount that we spend on this customer
        private String expectedInAmount;  // total expected amount that we should receive from this customer
        private String inAmount;          // it's the amount that we receive from this customer
        private String ivtAmount;         // inventory debt

        public DebtRecord(){
            sequence = 0;
            customerCode = "";
            customerName = "";
            expectedOutAmount = "";
            orgAmount = "";
            outAmount = "";
            expectedInAmount = "";
            inAmount = "";
            ivtAmount = "";
        }

        public DebtRecord(int sequence, String customerCode,
                String customerName, String orgAmount,
                String expectedOutAmount, String outAmount,
                String expectedInAmount, String inAmount, String ivtAmount) {
            super();
            this.sequence = sequence;
            this.customerCode = customerCode;
            this.customerName = customerName;
            this.orgAmount = orgAmount;
            this.expectedOutAmount = expectedOutAmount;
            this.outAmount = outAmount;
            this.expectedInAmount = expectedInAmount;
            this.inAmount = inAmount;
            this.ivtAmount = ivtAmount;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public String getCustomerCode() {
            return customerCode;
        }

        public void setCustomerCode(String customerCode) {
            this.customerCode = customerCode;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getOrgAmount() {
            return orgAmount;
        }

        public void setOrgAmount(String orgAmount) {
            this.orgAmount = orgAmount;
        }

        public String getExpectedOutAmount() {
            return expectedOutAmount;
        }

        public void setExpectedOutAmount(String expectedOutAmount) {
            this.expectedOutAmount = expectedOutAmount;
        }

        public String getOutAmount() {
            return outAmount;
        }

        public void setOutAmount(String outAmount) {
            this.outAmount = outAmount;
        }

        public String getExpectedInAmount() {
            return expectedInAmount;
        }

        public void setExpectedInAmount(String expectedInAmount) {
            this.expectedInAmount = expectedInAmount;
        }

        public String getInAmount() {
            return inAmount;
        }

        public void setInAmount(String inAmount) {
            this.inAmount = inAmount;
        }

        public String getIvtAmount() {
            return ivtAmount;
        }

        public void setIvtAmount(String ivtAmount) {
            this.ivtAmount = ivtAmount;
        }

        @Override
        public String toString() {
            return "DebtRecord [sequence=" + sequence + ", customerCode="
                    + customerCode + ", customerName=" + customerName
                    + ", orgAmount=" + orgAmount + ", expectedOutAmount="
                    + expectedOutAmount + ", outAmount=" + outAmount
                    + ", expectedInAmount=" + expectedInAmount + ", inAmount="
                    + inAmount + ", ivtAmount=" + ivtAmount + "]";
        }
    }

}
