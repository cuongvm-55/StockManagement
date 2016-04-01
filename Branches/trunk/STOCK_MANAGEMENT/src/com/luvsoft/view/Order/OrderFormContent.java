package com.luvsoft.view.Order;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.suggestfield.SuggestField;
import org.vaadin.viritin.grid.MGrid;

import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.presenter.OrderPresenter;
import com.luvsoft.presenter.OrderPresenter.CustomerConverter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

public class OrderFormContent extends VerticalLayout {
    // Information part
    private OptionGroup orderType; // fetch from OrderType
    private TextField orderNumber; // auto generate
    private TextField orderContent;
    private DateField orderDate;
    private TextArea orderNote;

    private SuggestField customerCode;
    private SuggestField customerName;
    private SuggestField customerPhoneNumber;
    private TextField customerAddress; // fetch form customer but can edit
    private TextField buyer;

    private MGrid<Orderdetail> tableOrderDetails;
    private List<Orderdetail> orderDetails;
    private List<SuggestField> filterFields;
    private List<String> tableProperties;
    private HeaderRow filteringHeader;

    // Footer
    private Button printer;
    private Button save;
    private Button discard;

    // Presenter
    OrderPresenter presenter;
    Order order;

    public OrderFormContent(OrderPresenter presenter, Order order) {
        super();
        this.presenter = presenter;
        this.order = order;
        create();
    }

    public void create() {
        addStyleName("formlayout-spacing max-textfield-width background-blue");
        setSizeFull();

        Label centertitle = new Label("Hóa đơn");
        centertitle.addStyleName("center font16 " + ValoTheme.LABEL_BOLD);
        addComponent(centertitle);

        // Information part
        orderType = new OptionGroup(); // fetch from OrderType
        orderType.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        orderType.setCaption("Loại Hóa Đơn");

        orderNumber = new TextField(); // auto generate
        orderNumber.setCaption("Số Hóa Đơn");

        customerCode = createSuggestFieldForCustomer(CustomerConverter.BY_CODE, "Mã Khách Hàng", "Tìm theo mã khách");
        customerName = createSuggestFieldForCustomer(CustomerConverter.BY_NAME, "Tên Khách Hàng", "Tìm theo tên");
        customerPhoneNumber = createSuggestFieldForCustomer(CustomerConverter.BY_PHONE_NUMBER, "Số Điện Thoại", "Tìm theo số điện thoại");

        customerAddress = new TextField(); // fetch form customer but can edit
        customerAddress.setCaption("Địa Chỉ");

        buyer = new TextField();
        buyer.setCaption("Người Mua");
        orderContent = new TextField();
        orderContent.setCaption("Diễn Giải");
        orderDate = new DateField();
        orderDate.setCaption("Ngày");

        orderNote = new TextArea();
        orderNote.setCaption("Ghi Chú");
        orderNote.setRows(1);
        orderNote.setColumns(61);

        ////////////////////////////////////////////////////////////////
        //| wrapper1         || wrapper2         || wrapper3         |//
        //|                  ||                  ||                  |//
        //|                  ||                  ||                  |//
        ////////////////////////////////////////////////////////////////
        //|                         wrapper4                          //
        ////////////////////////////////////////////////////////////////
        FormLayout wrapper1 = new FormLayout();
        wrapper1.setSizeFull();
        wrapper1.addComponents(orderType, customerCode, customerAddress);

        FormLayout wrapper2 = new FormLayout();
        wrapper2.setSizeFull();
        wrapper2.addComponents(orderNumber, customerName, buyer);

        FormLayout wrapper3 = new FormLayout();
        wrapper3.setSizeFull();
        wrapper3.addComponents(orderContent, customerPhoneNumber, orderDate);

        FormLayout wrapper4 = new FormLayout();
        wrapper4.addStyleName("addition-space");
        wrapper4.addComponent(orderNote);

        HorizontalLayout contentPart = new HorizontalLayout();
        contentPart.setWidth("100%");
        contentPart.setSpacing(true);
        contentPart.addComponents(wrapper1, wrapper2, wrapper3);
        contentPart.setExpandRatio(wrapper1, 0.8f);
        contentPart.setExpandRatio(wrapper2, 0.6f);
        contentPart.setExpandRatio(wrapper3, 0.6f);
        ////////////////////////////////////////////////////////////////

        // The table of orderDetails
        VerticalLayout centerPart = new VerticalLayout();
        centerPart.addComponents(contentPart, wrapper4);
        centerPart.setMargin(new MarginInfo(false, true, false, true));

        tableProperties = new ArrayList<String>();
        tableProperties.add("frk_material_code");
        tableProperties.add("frk_material_name");
        filterFields = new ArrayList<SuggestField>();

        tableOrderDetails = new MGrid<Orderdetail>(Orderdetail.class);
        tableOrderDetails.setEditorEnabled(true);
        tableOrderDetails.setSizeFull();
        tableOrderDetails.withProperties("frk_material_code", "frk_material_name");

        tableOrderDetails.getDefaultHeaderRow().getCell("frk_material_code").setHtml("Mã Hàng");
        tableOrderDetails.getDefaultHeaderRow().getCell("frk_material_name").setHtml("Tên Hàng");
        setColumnFiltering(true);

        orderDetails = new ArrayList<Orderdetail>();
        tableOrderDetails.setRows(orderDetails);

        // Footer
        ////////////////////////////////////////////////////////////
        //|                    totalwrapper                      |//
        ////////////////////////////////////////////////////////////
        //|                     footer                           |//
        //|   _______                          _______      ____ |//
        //|  |printer|                        |discard|    |save||//
        //|   -------                          -------      ---- |//
        ////////////////////////////////////////////////////////////
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.setSizeFull();
        footer.setMargin(new MarginInfo(false, true, false, true));
        printer = new Button("In", FontAwesome.PRINT);
        printer.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save = new Button("Lưu", FontAwesome.SAVE);
        save.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        discard = new Button("Hủy", FontAwesome.BAN);
        footer.addComponents(printer, discard, save);
        footer.setExpandRatio(printer, 1.0f);

        HorizontalLayout totalwrapper = new HorizontalLayout();
        totalwrapper.setSizeFull();
        totalwrapper.setMargin(new MarginInfo(false, true, false, true));
        Label total = new Label("15.000.000 vnd");
        total.setCaption("Tổng tiền");
        total.addStyleName("text-align-right");
        totalwrapper.addComponent(total);

        addComponents(centerPart, tableOrderDetails, totalwrapper, footer);
        setExpandRatio(centerPart, 0.3f);
        setExpandRatio(tableOrderDetails, 0.5f);
        setExpandRatio(totalwrapper, 0.1f);
        setExpandRatio(footer, 0.1f);

        // Fill data
        orderType.addItem("Xuất Bán");
        orderType.addItem("Xuất Bán Nội Bộ");
        if(order != null && order.getId() != -1) {
            orderNumber.setValue(order.getOrderCode());
            orderContent.setValue(order.getContent());
            orderDate.setValue(order.getDate());
            orderNote.setValue(order.getNote());
        }
    }

    private void setColumnFiltering(boolean filtered) {
        if (filtered && filteringHeader == null) {
            filteringHeader = tableOrderDetails.appendHeaderRow();
            for( String property : tableProperties ){
     
                // Add new TextFields to each column which filters the data from
                // that column
                String columnId = property;
                SuggestField filter = new SuggestField();
                filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
                filter.setInputPrompt("Tìm Kiếm");
                filter.setImmediate(true);
                presenter.setUpSuggestFieldForOrderDetail(filter);
                filter.addValueChangeListener(new ValueChangeListener() {
                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Orderdetail orderDetail = new Orderdetail();
                        orderDetail.setMaterial((Material)filter.getValue());
                        orderDetail.setFrk_material_code(orderDetail.getMaterial().getCode());
                        orderDetail.setFrk_material_name(orderDetail.getMaterial().getName());
                        orderDetails.add(orderDetail);
                        tableOrderDetails.setRows(orderDetails);
                    }
                });

                filteringHeader.getCell(columnId).setComponent(filter);
                filteringHeader.getCell(columnId).setStyleName("filter-header");
    
                // save to handle filter box
                filterFields.add(filter);
            }
        } else if (!filtered && filteringHeader != null) {
            tableOrderDetails.removeHeaderRow(filteringHeader);
            filteringHeader = null;
        }
    }

    public void fillTextFieldByCustomer(Customer customer) {
        customerCode.setValue(customer);
        customerName.setValue(customer);
        customerAddress.setValue(customer.getAddress());
        customerPhoneNumber.setValue(customer);
    }

    public SuggestField createSuggestFieldForCustomer(CustomerConverter converter, String caption, String inputPrompt) {
        SuggestField search = new SuggestField();
        search.setCaption(caption);
        search.setInputPrompt(inputPrompt);
        search.setMinimumQueryCharacters(2);
        presenter.setUpSuggestFieldForCustomer(search, converter);

        search.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                fillTextFieldByCustomer((Customer) search.getValue());
            }
        });
        return search;
    }
}
