package com.luvsoft.report.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.luvsoft.report.producer.CustomerDebtProducer;
import com.luvsoft.report.producer.CustomerDebtProducer.DebtRecord;

public class CustomerDebtReportView extends AbstractReportView<DebtRecord>{
    /**
     * 
     */
    private static final long serialVersionUID = 5924811558832649615L;

    public CustomerDebtReportView(){
        producer = new CustomerDebtProducer(this);
        super.init("Công Nợ Khách Hàng", DebtRecord.class);
    }

    @Override
    public String[] getProperties() {
        return new String[] {
                "sequence",
                "customerCode",
                "customerName",
                "orgAmount",
                "outAmount",
                "inAmount",
                "ivtAmount"};
    }

    @SuppressWarnings("serial")
    @Override
    public Map<String, String> getHeaderNames() {
        return new LinkedHashMap<String, String>(){{
            put("sequence", "<b>STT</b>");
            put("customerCode", "<b>Mã KH</b>");
            put("customerName", "<b>Tên KH</b>");
            put("orgAmount", "<b>Tồn Đầu Kì</b>");
            put("outAmount", "<b>Xuất Trong Kì</b>");
            put("inAmount", "<b>Thu Trong Kì</b>");
            put("ivtAmount", "<b>Tồn cuối</b>");
        }};
    }

    @SuppressWarnings("serial")
    @Override
    public Map<String, String> getFilterColumns() {
        return new LinkedHashMap<String, String>(){{
            put("frk_customertype1_name", "Loại KH 1");
            put("frk_customertype2_name", "Loại KH 2");
            put("name", "Tên KH");
        }};
    }

}
