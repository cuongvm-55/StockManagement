package com.luvsoft.report.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.luvsoft.report.producer.AbstractReportProducer;
import com.luvsoft.utils.Utilities;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public abstract class AbstractReportView<T> implements ClickListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected MVerticalLayout wrapper;

    private Label lblTitle;
    private DateField dfFromDate;
    private DateField dfToDate;
    private MHorizontalLayout hzFilter;

    private Button btnOk;

    protected HeaderRow filteringHeader;
    protected List<TextField> filterFields; // filter fields
    
    private MGrid<T> gridContent;
    private List<String> tableProperties; // headers

    // Backing Beans: Any changes (update, delete, create)
    // should re-charge this list instead of setRows() again
    private List<T> listOfData;

    // sum data list (for footer)
    private Map<String, String> sumDataList; // hold summary of some properties
    private FooterRow groupingFooter;

    protected AbstractReportProducer producer;

    AbstractReportView(){
    }

    @SuppressWarnings("serial")
    protected AbstractReportView<T> init(String strTitle, Class<T> typeOfRows) {
        System.out.println("AbstractReportView::init()");
        wrapper = new MVerticalLayout();
        wrapper.setSpacing(true);

        lblTitle = new Label(strTitle);
        lblTitle.addStyleName("text-center margin-left-20px margin-top-10px");
        lblTitle.addStyleName(ValoTheme.LABEL_BOLD);
        lblTitle.addStyleName(ValoTheme.LABEL_LARGE);

        dfFromDate = new DateField("Từ");
        dfFromDate.addStyleName("date-field-caption");
        dfToDate = new DateField("Đến");
        dfToDate.addStyleName("date-field-caption");
        btnOk = new Button("Xem báo cáo");
        // default date range is the first day of previous month to the current day
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1); // the first day
        int thisMonth = cal.get(Calendar.MONTH);
        if( thisMonth == 1 ){
            // The previous month is the December of the previous year
            cal.set(Calendar.MONTH, 12);
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        }
        else{
            // Others
            cal.set(Calendar.MONTH, thisMonth - 1);
        }
        // default from date
        dfFromDate.setValue(cal.getTime());
        // default to date
        dfToDate.setValue(new Date());

        HorizontalLayout hzLayoutDate = new HorizontalLayout();
        hzLayoutDate.addComponents(dfFromDate, dfToDate, btnOk);
        hzLayoutDate.setSpacing(true);
        hzLayoutDate.addStyleName("margin-left-right-20px");

        // Filter
        hzFilter = new MHorizontalLayout();

        // Grid
        gridContent = new MGrid<>(typeOfRows);
        gridContent.setSizeFull();
        gridContent.setEditorEnabled(false);
        gridContent.setSelectionMode(SelectionMode.NONE);
        gridContent.addStyleName("margin-left-right-20px");
        gridContent.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if( event.isDoubleClick() ){
                    System.out.println("Item double clicked !!! " + event.getItemId().toString());
                }
            }
        });

        groupingFooter = gridContent.prependFooterRow();

        tableProperties = new ArrayList<String>();
        //footerProperties = new ArrayList<String>();
        sumDataList = new LinkedHashMap<String, String>();
        filterFields = new ArrayList<TextField>();
 
        wrapper.addComponents(lblTitle, hzLayoutDate, hzFilter, gridContent);

        // Handle events
        this.btnOk.addClickListener(this);

        withTableProperties( getProperties ()   );
        withHeaderNames    ( getHeaderNames()   );
        withColumnFiltering( getFilterColumns() );

        return this;
    }

    /**
     * Handle click event for components of this view
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Component c = event.getComponent();
        if( c == null ){
            return;
        }

        if( c.equals(btnOk)  ){
            // Build report
            Date frDate = dfFromDate.getValue();
            Date toDate = dfToDate.getValue();
            if( frDate != null && toDate != null && !toDate.before(frDate) ){
                producer.generateReport(Utilities.reachDayBegin(frDate), Utilities.reachDayEnd(toDate));
            }
        }
    }

    @SuppressWarnings("serial")
    public AbstractReportView<T> withColumnFiltering(Map<String, String> properties) {
        if( properties == null || properties.isEmpty() ){
            return this;
        }
        hzFilter.setSizeUndefined();
        Label lblFilter = new Label("Bộ lọc");
        lblFilter.addStyleName("margin-left-20px");
        hzFilter.addComponent(lblFilter);

        MHorizontalLayout hz1 = new MHorizontalLayout();
        hz1.setSizeFull();
        Object[] fields = properties.keySet().toArray();
        //filteringHeader = gridContent.appendHeaderRow();
        for( int i = 0; i < fields.length; i++){
            String property = (String) fields[i];
            // Create the filter TextField
            TextField filter = new TextField();
            filter.setWidth("100%");
            filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
            filter.setData(property);
            filter.setCaption(properties.get(property));
            ResetButtonForTextField.extend(filter);
            filter.setImmediate(true);
            filter.addStyleName("filter-header");
            filter.addStyleName("caption-left");

            // save to handle filter box
            filterFields.add(filter);

            // Add value change handler
            filter.addTextChangeListener(new TextChangeListener() {
                @Override
                public void textChange(TextChangeEvent event) {
                    producer.doFilter(filter.getData().toString(), event.getText());
                }
            });

            hz1.addComponent(filter);
        }

        hzFilter.addComponent(hz1);
        hzFilter.setExpandRatio(lblFilter, 1);
        hzFilter.setExpandRatio(hz1, 9);
        return this;
    }

    /**
     * Function is used to set properties for table content
     * @param properties
     * @return
     */
    public AbstractReportView<T> withTableProperties(String... properties) {
        gridContent.withProperties(properties);
        tableProperties.clear();
        tableProperties.addAll(Arrays.asList(properties));
        return this;
    }

    /**
     * Function is used to set name for each column
     * @param propertyId
     * @param text
     * @return
     */
    public AbstractReportView<T> withHeaderNames(Map<String, String> headers) {
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
    public AbstractReportView<T> withContentData(List<T> listOfData) {
        try{
            this.listOfData = listOfData;
            gridContent.setRows(this.listOfData);

            if( !sumDataList.isEmpty() ){
                //groupingFooter = gridContent.getFooterRow(0).;
                for( int i=0; i < tableProperties.size(); i++ ){
                    if( sumDataList.get(tableProperties.get(i)) != null ){
                        groupingFooter.getCell(tableProperties.get(i)).setText(sumDataList.get(tableProperties.get(i)));
                    }
                }
            }
            else
            {
                gridContent.removeFooterRow(groupingFooter);
                groupingFooter = gridContent.prependFooterRow();
            }

        }catch(Exception e)
        {
            System.out.println("No record found.");
        }
        return this;
    }

    public void setTable(List<T> listData) {
        this.withContentData(listData);
    }

    public MVerticalLayout getWrapper() {
        return wrapper;
    }

    public void setWrapper(MVerticalLayout wrapper) {
        this.wrapper = wrapper;
    }

    public Label getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(Label lblTitle) {
        this.lblTitle = lblTitle;
    }

    public DateField getTfFromDate() {
        return dfFromDate;
    }

    public void setTfFromDate(DateField tfFromDate) {
        this.dfFromDate = tfFromDate;
    }

    public DateField getTfToDate() {
        return dfToDate;
    }

    public void setTfToDate(DateField tfToDate) {
        this.dfToDate = tfToDate;
    }

    public Button getBtnOk() {
        return btnOk;
    }

    public void setBtnOk(Button btnOk) {
        this.btnOk = btnOk;
    }

    public HeaderRow getFilteringHeader() {
        return filteringHeader;
    }

    public void setFilteringHeader(HeaderRow filteringHeader) {
        this.filteringHeader = filteringHeader;
    }

    public List<TextField> getFilterFields() {
        return filterFields;
    }

    public void setFilterFields(List<TextField> filterFields) {
        this.filterFields = filterFields;
    }

    public MGrid<T> getGridContent() {
        return gridContent;
    }

    public void setGridContent(MGrid<T> gridContent) {
        this.gridContent = gridContent;
    }

    public List<String> getTableProperties() {
        return tableProperties;
    }

    public void setTableProperties(List<String> tableProperties) {
        this.tableProperties = tableProperties;
    }

    public List<T> getListOfData() {
        return listOfData;
    }

    public void setListOfData(List<T> listOfData) {
        this.listOfData = listOfData;
    }

    public AbstractReportProducer getProducer() {
        return producer;
    }

    public void setProducer(AbstractReportProducer producer) {
        this.producer = producer;
    }

    public Map<String, String> getSumDataList() {
        return sumDataList;
    }

    public void setSumDataList(Map<String, String> sumDataList) {
        this.sumDataList = sumDataList;
    }

    // Abstract functions
    public abstract String[] getProperties();
    public abstract Map<String, String> getHeaderNames();
    public abstract Map<String, String> getFilterColumns();
}
