package com.luvsoft.report.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.luvsoft.report.producer.InOutInventoryProducer;
import com.luvsoft.report.producer.InOutInventoryProducer.InOutInventoryRecord;

@SuppressWarnings("serial")
public class InOutInventoryReportView extends AbstractReportView<InOutInventoryRecord>{
    public InOutInventoryReportView(){
        System.out.println("InOutInventoryReportView::InOutInventoryReportView()");
        producer = new InOutInventoryProducer(this);
        super.init("Xuất Nhập Tồn Hàng Hóa", InOutInventoryRecord.class);
    }

    @Override
    public Map<String, String> getHeaderNames() {
        return new LinkedHashMap<String, String>(){{
            put("sequence", "<b>STT</b>");
            put("code"    , "<b>Mã VT</b>");
            put("name"    , "<b>Tên VT</b>");
            put("unit"    , "<b>Đơn vị</b>");

            put("orgQuantity", "<b>SL Tồn đầu</b>");
            put("avgInputPrice", "<b>Giá TB</b>");
            put("orgAmount", "<b>TT Tồn Đầu</b>");

            put("inputQuantity", "<b>SL Nhập</b>");
            put("inputAmount", "<b>TT Nhập</b>");

            put("outputQuantity", "<b>SL Xuất</b>");
            put("outputAmount", "<b>TT Xuất</b>");

            put("invtQuantity", "<b>SL Tồn Cuối</b>");
            put("invtAmount", "<b>TT Tồn Cuối</b>");
        }};
    }

    @Override
    public Map<String, String> getFilterColumns() {
        return new LinkedHashMap<String, String>(){{
            put("frk_materialtype1_name", "Loại VT 1");
            put("frk_materialtype2_name", "Loại VT 2");
            put("name", "Tên VT");
        }};
    }

}
