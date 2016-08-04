package com.luvsoft.report.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.vaadin.viritin.grid.MGrid;

import com.luvsoft.report.producer.AbstractReportDetailsProducer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public abstract class AbstractReportDetailsView<T> extends Window {
    private static final long serialVersionUID = 8815819214659127635L;

    // First line
    protected DateField dfFrom;
    protected DateField dfTo;
    protected Button btnOk;

    // Second line
    private MGrid<ReportDetailSummary> gridSummaryReportDetail;

    // Third line
    private MGrid<T> gridContent;
    protected List<String> gridContentProperties;

    // Backing Beans: Any changes (update, delete, create)
    // should re-charge this list instead of setRows() again
    private List<T> listOfData;

    protected AbstractReportDetailsProducer producer;
    protected String objectCode;

    public AbstractReportDetailsView() {

    }

    public void init(String title, Class<T> typeOfRows, String objectCode) {
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setHeight("90%");
        setWidth("100%");
        setCaption("<b>" + title + "</b>");
        setCaptionAsHtml(true);

        this.objectCode = objectCode;

        dfFrom = new DateField();
        dfFrom.setCaption("Từ Ngày");
        dfFrom.addStyleName("date-field-caption");
        dfTo = new DateField();
        dfTo.setCaption("Đến Ngày");
        dfTo.addStyleName("date-field-caption");
        btnOk = new Button("Xem Báo Cáo");
        btnOk.addStyleName(ValoTheme.BUTTON_SMALL);
        btnOk.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnOk.addStyleName("margin-top-2px");

        gridSummaryReportDetail = new MGrid<ReportDetailSummary>(ReportDetailSummary.class);
        gridSummaryReportDetail.setWidth("50%");
        gridSummaryReportDetail.setHeight("77px");
        gridSummaryReportDetail.setEditorEnabled(false);
        gridSummaryReportDetail.setSelectionMode(SelectionMode.NONE);
        gridSummaryReportDetail.withProperties("openingStock", "issueInPeriod", "receiptInPeriod", "closingStock");

        gridContent = new MGrid<>(typeOfRows);

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setSpacing(true);

        // Add first line
        HorizontalLayout hrzDateFields = new HorizontalLayout();
        wrapper.addComponent(hrzDateFields);
        wrapper.setComponentAlignment(hrzDateFields, Alignment.TOP_CENTER);
        hrzDateFields.addComponent(dfFrom);
        hrzDateFields.addComponent(dfTo);
        hrzDateFields.addComponent(btnOk);
        hrzDateFields.addStyleName("margin-left-right-20px df-report-detail-style");

        // Add second line
        wrapper.addComponent(gridSummaryReportDetail);
        wrapper.setComponentAlignment(gridSummaryReportDetail, Alignment.TOP_CENTER);

        wrapper.addComponent(gridContent);
        gridContent.setWidth("100%");
        gridContent.setHeight("10%");
        gridContent.setEditorEnabled(false);
        gridContent.setSelectionMode(SelectionMode.NONE);

        gridContentProperties = new ArrayList<String>();

        withTableProperties(getProperties ());
        withHeaderNames    (getHeaderNames());
        setContent(wrapper);

        btnOk.addClickListener(new ClickListener() {
            private static final long serialVersionUID = -2918006939436642296L;

            @Override
            public void buttonClick(ClickEvent event) {
                if(!objectCode.isEmpty()) {
                    generateSummaryReportDetails(dfFrom.getValue(), dfTo.getValue());
                    generateReportDetails(dfFrom.getValue(), dfTo.getValue());
                }
            }
        });
    }

    void setSummaryHeaderNames(String strOpeningStock, String strIssueInPeriod, String strReceiptInPeriod, String strClosingStock) {
        gridSummaryReportDetail.getDefaultHeaderRow().getCell("openingStock").setHtml("<b>" + strOpeningStock + "</b>");
        gridSummaryReportDetail.getDefaultHeaderRow().getCell("issueInPeriod").setHtml("<b>" + strIssueInPeriod + "</b>");
        gridSummaryReportDetail.getDefaultHeaderRow().getCell("receiptInPeriod").setHtml("<b>" + strReceiptInPeriod + "</b>");
        gridSummaryReportDetail.getDefaultHeaderRow().getCell("closingStock").setHtml("<b>" + strClosingStock + "</b>");
    }

    public void setSummaryValues(double remainingBefore, double outputIn, double inputIn, double remainingEnd)
    {
        ReportDetailSummary summary = new ReportDetailSummary(remainingBefore, outputIn, inputIn, remainingEnd);
        gridSummaryReportDetail.setRows(summary);
    }

    /**
     * Function is used to set properties for table content
     * @param properties
     * @return
     */
    public AbstractReportDetailsView<T> withTableProperties(String... properties) {
        gridContent.withProperties(properties);
        gridContentProperties.clear();
        gridContentProperties.addAll(Arrays.asList(properties));
        return this;
    }

    /**
     * Function is used to set name for each column
     * @param propertyId
     * @param text
     * @return
     */
    public AbstractReportDetailsView<T> withHeaderNames(Map<String, String> headers) {
        if( headers == null || headers.isEmpty() ){
            return this;
        }

        Object[] fields = headers.keySet().toArray();
        for( Object obj : fields ){
            String key = (String) obj;
            gridContent.getDefaultHeaderRow().getCell(key).setHtml(headers.get(key));
        }

        return this;
    }

    /**
     * Function is used to set data for table content
     * @param listData
     * @return
     */
    public AbstractReportDetailsView<T> withContentData(List<T> listOfData) {
        try{
            this.listOfData = listOfData;
            gridContent.setRows(this.listOfData);
        }catch(Exception e)
        {
            System.out.println("No record found.");
        }
        return this;
    }

    public void setTable(List<T> listData) {
        this.withContentData(listData);
    }

    public void setDateValue(Date fromDate, Date toDate){
        dfFrom.setValue(fromDate);
        dfTo.setValue(toDate);
    }

    // Abstract functions
    public abstract String[] getProperties();
    public abstract Map<String, String> getHeaderNames();
    public abstract void generateReportDetails(Date from, Date to);
    public abstract void generateSummaryReportDetails(Date from, Date to);
}
