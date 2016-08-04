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

    @SuppressWarnings("serial")
    @Override
    public Map<String, String> getHeaderNames() {
        return new LinkedHashMap<String, String>(){{
            put("sequence", "<b>STT</b>");
            put("customerCode", "<b>Mã KH</b>");
            put("customerName", "<b>Tên KH</b>");
            put("orgAmount", "<b>Tồn Đầu Kì</b>");
            put("expectedOutAmount","<b>Chi trong kì</b>");
            put("outAmount", "<b>Thực chi trong kì</b>");
            put("expectedInAmount","<b>Thu trong kì</b>");
            put("inAmount", "<b>Thực thu Trong Kì</b>");
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
