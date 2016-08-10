package com.luvsoft.report.view;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.luvsoft.report.producer.BenefitProducer;
import com.luvsoft.report.producer.BenefitProducer.BenefitRecord;
import com.luvsoft.utils.ErrorManager.ErrorId;

public class BenefitView extends AbstractReportView<BenefitRecord>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BenefitView(){
        producer = new BenefitProducer(this);
        super.init("Báo Cáo Lỗ Lãi", BenefitRecord.class);
    }

    @SuppressWarnings("serial")
    @Override
    public Map<String, String> getHeaderNames() {
        return new LinkedHashMap<String, String>(){{
            put("sequence", "<b>STT</b>");
            put("field1", "<b>Tháng 1</b>");
            put("field2", "<b>Tháng 2</b>");
            put("field3", "<b>Tháng 3</b>");
            put("field4","<b>Tháng 4</b>");
            put("field5", "<b>Tháng 5</b>");
            put("field6","<b>Tháng 6</b>");
            put("field7", "<b>Tháng 7</b>");
            put("field8", "<b>Tháng 8</b>");
            put("field9", "<b>Tháng 9</b>");
            put("field10", "<b>Tháng 10</b>");
            put("field11", "<b>Tháng 11</b>");
            put("field12", "<b>Tháng 12</b>");
        }};
    }

    @SuppressWarnings("serial")
    @Override
    public Map<String, String> getFilterColumns() {
        return new LinkedHashMap<String, String>(){{
            put("frk_materialtype1_name", "Loại VT 1");
            put("frk_materialtype2_name", "Loại VT 2");
            put("name", "Tên VT");
        }};
    }

    @Override
    protected ErrorId checkReportContraints(Date frDate, Date toDate){
        // With benefit report, need to check time range
        Calendar cal = Calendar.getInstance();
        cal.setTime(frDate);
        int fromYear = cal.get(Calendar.YEAR);
        cal.setTime(toDate);
        if( cal.get(Calendar.YEAR) - fromYear >= 12 ){
            System.out.println("Date range is invalid!");
            return ErrorId.REPORT_INVALID_YEAR_RANGE;
        }
        return ErrorId.ERROR_NONE;
    }
}
