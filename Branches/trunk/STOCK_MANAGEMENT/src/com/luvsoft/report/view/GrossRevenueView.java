package com.luvsoft.report.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.luvsoft.report.producer.GrossRevenueProducer;
import com.luvsoft.report.producer.GrossRevenueProducer.GrossMode;
import com.luvsoft.report.producer.GrossRevenueProducer.GrossRecord;

@SuppressWarnings("serial")
public class GrossRevenueView extends AbstractReportView<GrossRecord>{
    public GrossRevenueView(GrossMode mode){
        producer = new GrossRevenueProducer(this, mode);

        String reportTitle = "";
        switch(mode)
        {
        case GrossInput:
            reportTitle = "Báo Cáo Hàng Nhâp";
            break;
        case GrossRevenue:
            reportTitle = "Báo Cáo Doanh Thu";
            break;
        default:
            System.out.println("Gross mode does not supported!");
             break;
        }
        super.init(reportTitle, GrossRecord.class);
    }

    @Override
    public Map<String, String> getHeaderNames() {
        return new LinkedHashMap<String, String>(){{
                put("sequence","<b>STT</b>");
                put("orderCode","<b>Mã Hóa Đơn</b>");
                put("date", "<b>Ngày</b>");
                put("customerName", "<b>Khách Hàng</b>");
                put("totalAmount", "<b>Tổng Tiền</b>");
                put("content", "<b>Nội Dung</b>");
                put("note", "<b>Ghi Chú</b>");
                }};
    }

    @Override
    public Map<String, String> getFilterColumns() {
        return new LinkedHashMap<String, String>(){{
            put("frk_customertype1_name", "Loại KH 1");
            put("frk_customertype2_name", "Loại KH 2");
            put("name", "Tên KH");
        }};
    }

}
