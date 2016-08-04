package com.luvsoft.report.view;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.luvsoft.report.producer.InOutInventoryDetailsProducer;
import com.luvsoft.report.producer.InOutInventoryDetailsProducer.InOutInventoryDetailsRecord;

@SuppressWarnings("serial")
public class InOutInventoryReportDetailsView extends AbstractReportDetailsView<InOutInventoryDetailsRecord>{

    public InOutInventoryReportDetailsView(String objectCode){
        System.out.println("InOutInventoryReportDetailsView::InOutInventoryReportDetailsView()");
        producer = new InOutInventoryDetailsProducer(this);
        super.init("Báo Cáo Chi Tiết Vật Tư " + objectCode, InOutInventoryDetailsRecord.class, objectCode);
        super.setSummaryHeaderNames("Tồn Đầu Kỳ", "Xuất Trong Kỳ", "Nhập Trong Kỳ", "Tồn Cuối Kỳ");
    }

    @Override
    public String[] getProperties() {
        return new String[] {"sequence", "code", "date", "customer",
                             "content", "inputQuantity", "outputQuantity"};
    }

    @Override
    public Map<String, String> getHeaderNames() {
        return new LinkedHashMap<String, String>(){{
            put("sequence",       "<b>STT</b>");
            put("code"    ,       "<b>Mã HD</b>");
            put("date"    ,       "<b>Ngày</b>");
            put("customer",       "<b>Khách Hàng</b>");
            put("content" ,       "<b>Diễn Giải (Xuất|Nhập)</b>");
            put("inputQuantity",  "<b>SL Nhập</b>");
            put("outputQuantity", "<b>SL Xuất</b>");
        }};
    }

    @Override
    public void generateReportDetails(Date from, Date to)
    {
        producer.generateReport(from, to, objectCode);
    }

    @Override
    public void generateSummaryReportDetails(Date from, Date to) {
        producer.generateSummaryReportDetails(from, to, objectCode);
    }
}
