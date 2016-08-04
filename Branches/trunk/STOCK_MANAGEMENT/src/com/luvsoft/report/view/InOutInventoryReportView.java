package com.luvsoft.report.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.luvsoft.report.producer.InOutInventoryProducer;
import com.luvsoft.report.producer.InOutInventoryProducer.InOutInventoryRecord;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class InOutInventoryReportView extends AbstractReportView<InOutInventoryRecord>{
    public InOutInventoryReportView(){
        System.out.println("InOutInventoryReportView::InOutInventoryReportView()");
        producer = new InOutInventoryProducer(this);
        super.init("Xuất Nhập Tồn Hàng Hóa", InOutInventoryRecord.class);

        gridContent.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                String materialCode = (String) event.getItem().getItemProperty("code").getValue();

                InOutInventoryReportDetailsView reportDetailView = new InOutInventoryReportDetailsView(materialCode);
                reportDetailView.setDateValue(dfFromDate.getValue(), dfToDate.getValue());
                reportDetailView.generateReportDetails(dfFromDate.getValue(), dfToDate.getValue());
                reportDetailView.generateSummaryReportDetails(dfFromDate.getValue(), dfToDate.getValue());

                UI.getCurrent().addWindow(reportDetailView);
            }
        });
    }

    @Override
    public String[] getProperties() {
        return new String[] {"sequence", "code", "name", "unit",
                "orgQuantity", "avgInputPrice", "orgAmount",
                "inputQuantity", "inputAmount",
                "outputQuantity", "outputAmount",
                "invtQuantity", "invtAmount"};
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
