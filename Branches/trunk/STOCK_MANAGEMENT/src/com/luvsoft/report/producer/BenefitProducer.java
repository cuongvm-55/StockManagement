package com.luvsoft.report.producer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.luvsoft.DAO.FilterObject;
import com.luvsoft.DAO.MaterialModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialhistory;
import com.luvsoft.report.view.AbstractReportView;
import com.luvsoft.statistic.MaterialStatisticManager;
import com.luvsoft.utils.EntityAnalyzer;
import com.luvsoft.utils.Utilities;

public class BenefitProducer extends AbstractReportProducer{

    public BenefitProducer(AbstractReportView<BenefitRecord> v){
        view = v;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> getFilterStatistic(Date from, Date to, FilterObject fo) {
        MaterialModel mtModel = new MaterialModel();
        List<Object> dataList = new ArrayList<Object>();

        List<AbstractEntity> materialList = mtModel.getFilterData(fo);
        if( materialList == null || materialList.isEmpty() ){
            System.out.println("no material found!");
            return dataList; // no material found
        }

        MaterialStatisticManager mtStatMgr = MaterialStatisticManager.getInstance();

        // Check the date interval [from, to] in the same year
        // If so, data will be grouped by month
        Calendar cal = Calendar.getInstance();

        // Extract month & year of from date
        cal.setTime(from);
        int fromYear = cal.get(Calendar.YEAR);
        int fromMonth = cal.get(Calendar.MONTH) + 1; // Note: MONTH return by calendar starts from 0
        // Extract month & year of to date
        cal.setTime(to);
        int toYear = cal.get(Calendar.YEAR);
        int toMonth = cal.get(Calendar.MONTH) + 1;

        boolean isSameYear = false;
        Map<String, String> headers =  new LinkedHashMap<String, String>();
        headers.put("sequence", "<b>STT</b>");
        headers.put("mtName", "<b>Tên Vật Tư</b>");
        if( fromYear == toYear ){
            isSameYear = true;
            // Set the list of month to table
            for( int k = fromMonth; k <= toMonth; k++){
                System.out.println("k = " + k);
                headers.put("field"+(k-fromMonth+1), "<b>Tháng "+k+"</b>");
            }
        }
        else{
            // Set the list of year to table
            for( int k = fromYear; k <= toYear; k++){
                headers.put("field"+(k-fromYear+1), "<b>Năm "+k+"</b>");
            }
        }

        // Set the headers for view
        view.withHeaderNames(headers);

        int idx = 1;
        // Extract stat data
        Map<String, Double> sumList = new HashMap<String, Double>();
        for( int index = 0; index < materialList.size(); index++ ){
            Material material = (Material)materialList.get(index);

            cal.setTime(to);
            BenefitRecord record = new BenefitRecord();
            record.setSequence(idx+"");
            record.setMtName(material.getName());
            if( isSameYear ){
                for( int i = fromMonth; i <= toMonth; i++ ){
                    // consume the statistic during December, datepoint must be the first day of next year
                    if( (i+1) > 12 ){
                        cal.set( Calendar.YEAR, (cal.get(Calendar.YEAR) +1) );
                    }
                    else{
                        cal.set(Calendar.MONTH, (i+1));
                    }
                    // Reach the first day of month
                    Date datePoint = Utilities.reachMonthBegin(cal.getTime());
                    Materialhistory mh = mtStatMgr.getInventoryMaterialAtDatePoint(datePoint, material);

                    // Calculate benefit = outAmount - inAmount
                    Double benefit = mh.getOutAmount().doubleValue() - mh.getAverageInputPrice().doubleValue()* mh.getOutQuantity();

                    String fieldName = "field"+(i-fromMonth+1);
                    EntityAnalyzer.setFieldValue(record,
                            fieldName,
                            Utilities.getNumberFormat().format(benefit));

                    // If sumfield is exist, append data
                    if( sumList.containsKey(fieldName) )
                    {
                        double oldVal = sumList.get(fieldName);
                        sumList.replace(fieldName, oldVal+benefit);
                    }
                    else{
                        sumList.put(fieldName, benefit);
                    }
                }
            }
            else{
                // Different year, data must be grouped by year
                for(int year = fromYear; year <= toYear; year++){
                    double benefit = 0.0d;
                    for( int i = 1; i <= 12; i++ ){
                        // consume the statistic during December, datepoint must be the first day of next year
                        if( (i+1) > 12 ){
                            cal.set( Calendar.YEAR, (cal.get(Calendar.YEAR) +1) );
                        }
                        else{
                            // date point must be the first day of next month
                            // because the record in that day is consumed for this month
                            cal.set(Calendar.MONTH, (i+1));
                        }
                        // Reach the first day of month
                        Date datePoint = Utilities.reachMonthBegin(cal.getTime());
                        Materialhistory mh = mtStatMgr.getInventoryMaterialAtDatePoint(datePoint, material);

                        // Calculate benefit = outAmount - inAmount
                        benefit+= mh.getOutAmount().doubleValue() - mh.getAverageInputPrice().doubleValue()* mh.getOutQuantity();
                    }
                    
                    String fieldName = "field"+(year-fromYear+1);
                    EntityAnalyzer.setFieldValue(record,
                            "field"+(year-fromYear+1),
                            Utilities.getNumberFormat().format(benefit));

                    // If sumfield is exist, append data
                    if( sumList.containsKey(fieldName) )
                    {
                        double oldVal = sumList.get(fieldName);
                        sumList.replace(fieldName, oldVal+benefit);
                    }
                    else{
                        sumList.put(fieldName, benefit);
                    }
                }
            }
            dataList.add(record);
            idx++;
        }
        // set sum data
        Object[] fields = sumList.keySet().toArray();
        for( int j = 0; j < fields.length; j++ ){
            view.getSumDataList().put(fields[j], Utilities.getNumberFormat().format(sumList.get(fields[j])));
        }

        return dataList;
    }

    public class BenefitRecord{
        private String sequence;
        private String mtName; // material Name
        private String field1;
        private String field2;
        private String field3;
        private String field4;
        private String field5;
        private String field6;
        private String field7;
        private String field8;
        private String field9;
        private String field10;
        private String field11;
        private String field12;

        public BenefitRecord(){
        }

        public String getMtName() {
            return mtName;
        }

        public void setMtName(String mtName) {
            this.mtName = mtName;
        }

        public BenefitRecord(String sequence, String mtName, String field1,
                String field2, String field3, String field4, String field5,
                String field6, String field7, String field8, String field9,
                String field10, String field11, String field12) {
            super();
            this.sequence = sequence;
            this.mtName = mtName;
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
            this.field4 = field4;
            this.field5 = field5;
            this.field6 = field6;
            this.field7 = field7;
            this.field8 = field8;
            this.field9 = field9;
            this.field10 = field10;
            this.field11 = field11;
            this.field12 = field12;
        }


        public String getSequence() {
            return sequence;
        }
        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        public String getField3() {
            return field3;
        }

        public void setField3(String field3) {
            this.field3 = field3;
        }

        public String getField4() {
            return field4;
        }

        public void setField4(String field4) {
            this.field4 = field4;
        }

        public String getField5() {
            return field5;
        }

        public void setField5(String field5) {
            this.field5 = field5;
        }

        public String getField6() {
            return field6;
        }

        public void setField6(String field6) {
            this.field6 = field6;
        }

        public String getField7() {
            return field7;
        }

        public void setField7(String field7) {
            this.field7 = field7;
        }

        public String getField8() {
            return field8;
        }

        public void setField8(String field8) {
            this.field8 = field8;
        }

        public String getField9() {
            return field9;
        }

        public void setField9(String field9) {
            this.field9 = field9;
        }

        public String getField10() {
            return field10;
        }

        public void setField10(String field10) {
            this.field10 = field10;
        }

        public String getField11() {
            return field11;
        }

        public void setField11(String field11) {
            this.field11 = field11;
        }

        public String getField12() {
            return field12;
        }

        public void setField12(String field12) {
            this.field12 = field12;
        }

    }
}
