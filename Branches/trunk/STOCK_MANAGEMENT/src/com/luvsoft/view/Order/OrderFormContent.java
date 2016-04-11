package com.luvsoft.view.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vaadin.suggestfield.SuggestField;
import org.vaadin.viritin.grid.MGrid;

import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.entities.Ordertype;
import com.luvsoft.presenter.OrderPresenter;
import com.luvsoft.presenter.OrderPresenter.CustomerConverter;
import com.luvsoft.presenter.OrderPresenter.MaterialConverter;
import com.luvsoft.printing.OrderPrintingView;
import com.luvsoft.stockmanagement.StockManagementUI;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OrderFormContent extends VerticalLayout implements ClickListener {
    private static final long serialVersionUID = -6147363220520400910L;
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
    private List<Ordertype> ordertypeList;

    // Footer
    private Button printer;
    private Button save;
    private Button discard;

    // Presenter
    private OrderPresenter presenter;
    private Order order;
    private Customer customer;

    public OrderFormContent(OrderPresenter presenter, Order order) {
        super();
        this.presenter = presenter;
        this.order = order;
        create();
    }

    @SuppressWarnings("serial")
    public void create() {
        addStyleName("formlayout-spacing max-textfield-width background-blue");
        setSizeFull();
        removeAllComponents();

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
        orderContent.setValue("Xuất Bán Hàng Cho Khách");
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

        // Event register
        save.addClickListener(this);
        discard.addClickListener(this);
        printer.addClickListener(this);

        // Fill data
        // If order is not null and has an id (it is already created) 
        // we will fill data for components by data of this order
        // If order is not null and doesn't have any id (it is never created)
        // we will generate an unique value for order number
        if(order != null) {
            ordertypeList = new ArrayList<Ordertype>();
            presenter.createOrderTypes(orderType, order.getOrdertype(), ordertypeList);
            if(order.getId() != -1) {
                orderNumber.setValue(order.getOrderCode());
                orderContent.setValue(order.getContent());
                orderDate.setValue(order.getDate());
                orderNote.setValue(order.getNote());
            } else {
                presenter.generateOrderCode(orderNumber);
                orderDate.setValue(new Date());
            }
        }

        // Handle printing button
        BrowserWindowOpener opener = new BrowserWindowOpener(StockManagementUI.class);// "http://google.com"
        opener.setFeatures("height=50000,width=80000,fullScreen=yes,menubar=no,location=no,resizable=no,scrollbars=no,status=no");
        opener.setWindowName("_new");// _new, _blank, _top, etc.
        opener.setParameter("OPEN_REASON", "PRINT");
        opener.extend(printer);
        printer.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if(order == null) {
                    System.out.println("Has no order to save");
                    return;
                }

                // Set data for order
                order.setOrderCode(orderNumber.getValue());
                if(customer != null && !customer.getCode().equals("") && customerCode.getValue() != null) {
                    order.setCustomer(customer);
                }
                order.setBuyer(buyer.getValue());
                order.setContent(orderContent.getValue());
                order.setDate(orderDate.getValue());
                order.setNote(orderNote.getValue());

                if(ordertypeList == null || ordertypeList.isEmpty()) {
                    order.setOrdertype(null);
                } else {
                    for (Ordertype ordertype : ordertypeList) {
                        if(ordertype.getName().equals(orderType.getValue())) {
                            order.setOrdertype(ordertype);
                        }
                    }
                }

                // Update orderDetails List
                if(orderDetails != null && !orderDetails.isEmpty()) {
                    Set<Orderdetail> setOrderdetails = new HashSet<Orderdetail>(orderDetails);
                    order.setOrderdetails(setOrderdetails);
                }
                OrderPrintingView.getInstance().setOrder(order);
            }
        });
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
                filter.setMinimumQueryCharacters(2);

                if(property.equals("frk_material_code")) {
                    presenter.setUpSuggestFieldForMaterial(filter, MaterialConverter.BY_CODE);
                } else if(property.equals("frk_material_name")) {
                    presenter.setUpSuggestFieldForMaterial(filter, MaterialConverter.BY_NAME);
                }

                filter.addValueChangeListener(new ValueChangeListener() {
                    private static final long serialVersionUID = 6452140842646163170L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Orderdetail orderDetail = new Orderdetail();
                        orderDetail.setMaterial((Material)filter.getValue());
                        orderDetail.setFrk_material_code(orderDetail.getMaterial().getCode());
                        orderDetail.setFrk_material_name(orderDetail.getMaterial().getName());
                        orderDetail.setOrder(order);

                        // Set some default value
                        orderDetail.setQuantityNeeded(10);
                        orderDetail.setQuantityDelivered(10);
                        orderDetail.setPrice(new BigDecimal("250000"));

                        // Add orderdetail to table
                        if(presenter.addToOrderDetailList(orderDetail, orderDetails)) {
                            tableOrderDetails.setRows(orderDetails);
                        }
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

    private void fillTextFieldByCustomer(Customer customer) {
        if(customer == null) {
            System.out.println("Customer is null");
            return;
        }

        customerCode.setValue(customer);
        customerName.setValue(customer);
        customerAddress.setValue(customer.getAddress());
        customerPhoneNumber.setValue(customer);
    }

    private SuggestField createSuggestFieldForCustomer(CustomerConverter converter, String caption, String inputPrompt) {
        SuggestField search = new SuggestField();
        search.setCaption(caption);
        search.setInputPrompt(inputPrompt);
        search.setMinimumQueryCharacters(2);
        presenter.setUpSuggestFieldForCustomer(search, converter);

        search.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -7248141255975105212L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                customer = (Customer) search.getValue();
                fillTextFieldByCustomer(customer);
            }
        });
        return search;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton().equals(save)) {
            saveOrder();
        }
    }

    private void saveOrder() {
        if(order == null) {
            System.out.println("Has no order to save");
            return;
        }

        // Set data for order
        order.setOrderCode(orderNumber.getValue());
        if(order.getOrderCode().trim().equals("")) {
            System.out.println("Order code is invalid");
            return;
        }

        if(customer != null && !customer.getCode().equals("") && customerCode.getValue() != null) {
            order.setCustomer(customer);
        }

        if(orderContent.getValue().trim().equals("")) {
            System.out.println("Content cannot be empty");
            return;
        }

        if(orderDate.getValue() == null) {
            System.out.println("Date cannot be empty");
            return;
        }

        order.setBuyer(buyer.getValue());
        order.setContent(orderContent.getValue());
        order.setDate(orderDate.getValue());
        order.setNote(orderNote.getValue());

        if(ordertypeList == null || ordertypeList.isEmpty()) {
            order.setOrdertype(null);
        } else {
            for (Ordertype ordertype : ordertypeList) {
                if(ordertype.getName().equals(orderType.getValue())) {
                    order.setOrdertype(ordertype);
                }
            }
        }

        // Update orderDetails List
        if(orderDetails != null && !orderDetails.isEmpty()) {
            Set<Orderdetail> setOrderdetails = new HashSet<Orderdetail>(orderDetails);
            order.setOrderdetails(setOrderdetails);
        }

        // Save it to database
        presenter.saveOrder(order);
    }
}
