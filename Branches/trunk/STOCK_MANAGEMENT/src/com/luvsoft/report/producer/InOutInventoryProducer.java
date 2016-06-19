package com.luvsoft.report.producer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.luvsoft.DAO.FilterObject;
import com.luvsoft.DAO.MaterialModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialhistory;
import com.luvsoft.report.view.InOutInventoryReportView;
import com.luvsoft.statistic.MaterialStatisticManager;
import com.luvsoft.statistic.Types.StatRecord;
import com.luvsoft.utils.Utilities;

public class InOutInventoryProducer extends AbstractReportProducer{

    public InOutInventoryProducer(InOutInventoryReportView v){
        view = v;
    }

    @Override
    public List<Object> getFilterStatistic(Date from, Date to,
            FilterObject fo) {
        MaterialModel mtModel = new MaterialModel();
        List<Object> dataList = new ArrayList<Object>();

        List<AbstractEntity> materialList = mtModel.getFilterData(fo);
        if( materialList == null || materialList.isEmpty() ){
            System.out.println("no material found!");
            return dataList; // no material found
        }

        MaterialStatisticManager mtStatMgr = MaterialStatisticManager.getInstance();
        for( int index = 0; index < materialList.size(); index++ ){
            Material material = (Material)materialList.get(index);

            // Inventory
            Materialhistory inventoryMH = mtStatMgr.getInventoryMaterialAtDatePoint(from, material);
            // Note: The static on "fromDate" is considered belong to interval
            // Input stats in [from, to]
            StatRecord inputRecord = new StatRecord();
            mtStatMgr.extractInputStatisticInDateRange(
                    from,
                    to,
                    material.getId(),
                    inputRecord);

            // Ouput stats in [from, to]
            StatRecord outputRecord = new StatRecord();
            mtStatMgr.extractOutputStatisticInDateRange(
                    from,
                    to,
                    material.getId(),
                    outputRecord);
            double price = inventoryMH.getAverageInputPrice().doubleValue(); // average price
            price = (price == 0f) ? material.getPrice().doubleValue() : price;
            
            dataList.add(new InOutInventoryRecord(
                    (index + 1),                      // STT
                    material.getCode(),               // Ma VT
                    material.getName(),               // Ten VT
                    material.getUnit() != null ? material.getUnit().getName() : "",// ĐVT
                    inventoryMH.getQuantity(),                   // SL ton  < dau ki >
                    Utilities.getNumberFormat().format(price),   // Gia
                    Utilities.getNumberFormat().format(inventoryMH.getQuantity()*price), // TT ton
                    inputRecord.quantity,                                                // SL nhap < trong ki >
                    Utilities.getNumberFormat().format(inputRecord.amount),       // TT nhap
                    outputRecord.quantity,                                        // SL xuat
                    Utilities.getNumberFormat().format(outputRecord.amount),      // TT xuat
                    inventoryMH.getQuantity() + inputRecord.quantity - outputRecord.quantity, // SL ton  < cuoi ki >
                    Utilities.getNumberFormat().format(
                            (inventoryMH.getQuantity() + inputRecord.quantity - outputRecord.quantity)*price) // TT ton
                    ));
            index++;
        }
        System.out.println(dataList);
        return dataList;
    }

    public class InOutInventoryRecord{
        private int sequence;
        private String code; // material code
        private String name; // material name
        private String unit;
        // origin
        private int    orgQuantity;
        private String avgInputPrice; // average input price till the beginning of the interval
        private String orgAmount;
        
        // belong the interval [from, to]
        // in
        private int    inputQuantity;
        private String inputAmount;
        // out
        private int    outputQuantity;
        private String outputAmount;

        // inventory (the redundant)
        private int    invtQuantity;
        private String invtAmount;
        public InOutInventoryRecord(){
            sequence = 0;
            code = "";
            name = "";
            unit = "";
            orgQuantity = 0;
            orgAmount = "";
            avgInputPrice = "";
            inputQuantity = 0;
            inputAmount = "";
            outputQuantity = 0;
            outputAmount = "";
            invtQuantity = 0;
            invtAmount = "";
        }
        public InOutInventoryRecord(int sequence, String code, String name,
                String unit, int orgQuantity, String avgInputPrice,
                String orgAmount, int inputQuantity, String inputAmount,
                int outputQuantity, String outputAmount, int invtQuantity,
                String invtAmount) {
            super();
            this.sequence = sequence;
            this.code = code;
            this.name = name;
            this.unit = unit;
            this.orgQuantity = orgQuantity;
            this.avgInputPrice = avgInputPrice;
            this.orgAmount = orgAmount;
            this.inputQuantity = inputQuantity;
            this.inputAmount = inputAmount;
            this.outputQuantity = outputQuantity;
            this.outputAmount = outputAmount;
            this.invtQuantity = invtQuantity;
            this.invtAmount = invtAmount;
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
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getUnit() {
            return unit;
        }
        public void setUnit(String unit) {
            this.unit = unit;
        }
        public int getOrgQuantity() {
            return orgQuantity;
        }
        public void setOrgQuantity(int orgQuantity) {
            this.orgQuantity = orgQuantity;
        }
        public String getAvgInputPrice() {
            return avgInputPrice;
        }
        public void setAvgInputPrice(String avgInputPrice) {
            this.avgInputPrice = avgInputPrice;
        }
        public String getOrgAmount() {
            return orgAmount;
        }
        public void setOrgAmount(String orgAmount) {
            this.orgAmount = orgAmount;
        }
        public int getInputQuantity() {
            return inputQuantity;
        }
        public void setInputQuantity(int inputQuantity) {
            this.inputQuantity = inputQuantity;
        }
        public String getInputAmount() {
            return inputAmount;
        }
        public void setInputAmount(String inputAmount) {
            this.inputAmount = inputAmount;
        }
        public int getOutputQuantity() {
            return outputQuantity;
        }
        public void setOutputQuantity(int outputQuantity) {
            this.outputQuantity = outputQuantity;
        }
        public String getOutputAmount() {
            return outputAmount;
        }
        public void setOutputAmount(String outputAmount) {
            this.outputAmount = outputAmount;
        }
        public int getInvtQuantity() {
            return invtQuantity;
        }
        public void setInvtQuantity(int invtQuantity) {
            this.invtQuantity = invtQuantity;
        }
        public String getInvtAmount() {
            return invtAmount;
        }
        public void setInvtAmount(String invtAmount) {
            this.invtAmount = invtAmount;
        }

        @Override
        public String toString() {
            return "InOutInventoryRecord [sequence=" + sequence + ", code="
                    + code + ", name=" + name + ", unit=" + unit
                    + ", orgQuantity=" + orgQuantity + ", avgInputPrice="
                    + avgInputPrice + ", orgAmount=" + orgAmount
                    + ", inputQuantity=" + inputQuantity + ", inputAmount="
                    + inputAmount + ", outputQuantity=" + outputQuantity
                    + ", outputAmount=" + outputAmount + ", invtQuantity="
                    + invtQuantity + ", invtAmount=" + invtAmount + "]";
        }
    }
}
