package com.luvsoft.view.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.luvsoft.stockmanagement.StockManagementUI;
import com.luvsoft.utils.NemErrorList;
import com.luvsoft.utils.Utilities;
import com.luvsoft.view.component.LuvsoftConfirmationDialog;
import com.luvsoft.view.validator.LuvsoftOrderDetailValidator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OrderFormContent extends VerticalLayout implements ClickListener {
    private static final long serialVersionUID = -6147363220520400910L;
    // Information part
    private OptionGroup optionsOrderType; // fetch from OrderType
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
    private Label totalAmountOfOrderdetails;
    private Button printer;
    private Button save;
    private Button discard;
    private Button deleteSelectedOrderdetails;
    private Button paid;

    // Presenter
    private OrderPresenter presenter;
    private Order order;
    private Customer customer;

    // Validators
    private NullValidator customerCodeValidator;
    private StringLengthValidator orderNumberValidator;
    private StringLengthValidator orderContentValidator;
    private NullValidator orderDateValidator;

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
        filteringHeader = null;

        Label centertitle = new Label("Hóa đơn");
        centertitle.addStyleName("center font16 " + ValoTheme.LABEL_BOLD);
        addComponent(centertitle);

        // Information part
        optionsOrderType = new OptionGroup(); // fetch from OrderType
        optionsOrderType.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        optionsOrderType.setCaption("Loại Hóa Đơn");

        orderNumber = new TextField(); // auto generate
        orderNumber.setCaption("Số Hóa Đơn");
        orderNumberValidator = new StringLengthValidator(NemErrorList.Error_CodeOrderNumberNotEmpty);
        orderNumberValidator.setMinLength(1);
        orderNumber.addValidator(orderNumberValidator);

        customerCode = createSuggestFieldForCustomer(CustomerConverter.BY_CODE, "Mã Khách Hàng", "Tìm theo mã khách");
        customerCodeValidator = new NullValidator(NemErrorList.Error_CodeCustomerNotEmpty, false);
        customerCode.addValidator(customerCodeValidator);

        customerName = createSuggestFieldForCustomer(CustomerConverter.BY_NAME, "Tên Khách Hàng", "Tìm theo tên");
        customerPhoneNumber = createSuggestFieldForCustomer(CustomerConverter.BY_PHONE_NUMBER, "Số Điện Thoại", "Tìm theo số điện thoại");

        customerAddress = new TextField(); // fetch form customer but can edit
        customerAddress.setCaption("Địa Chỉ");

        buyer = new TextField();
        buyer.setCaption("Người Mua");

        orderContent = new TextField();
        orderContent.setCaption("Diễn Giải");
        orderContent.setValue("Xuất Bán Hàng Cho Khách");
        orderContentValidator = new StringLengthValidator(NemErrorList.Error_OrderContentNotEmpty);
        orderContentValidator.setMinLength(1);
        orderContent.addValidator(orderContentValidator);

        orderDate = new DateField();
        orderDate.setCaption("Ngày");
        orderDateValidator = new NullValidator(NemErrorList.Error_OrderDateNotEmpty, false);

        orderNote = new TextArea();
        orderNote.setCaption("Ghi Chú");
        orderNote.setRows(1);
        orderNote.setColumns(61);

        // //////////////////////////////////////////////////////////////
        // | wrapper1 || wrapper2 || wrapper3 |//
        // | || || |//
        // | || || |//
        // //////////////////////////////////////////////////////////////
        // | wrapper4 //
        // //////////////////////////////////////////////////////////////
        FormLayout wrapper1 = new FormLayout();
        wrapper1.setSizeFull();
        wrapper1.addComponents(optionsOrderType, customerCode, customerAddress);

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
        // //////////////////////////////////////////////////////////////

        // The table of orderDetails
        VerticalLayout centerPart = new VerticalLayout();
        centerPart.addComponents(contentPart, wrapper4);
        centerPart.setMargin(new MarginInfo(false, true, false, true));

        tableProperties = new ArrayList<String>();
        tableProperties.add("frk_material_code");
        tableProperties.add("frk_material_name");
        filterFields = new ArrayList<SuggestField>();

        tableOrderDetails = new MGrid<Orderdetail>(Orderdetail.class);
        tableOrderDetails.setSelectionMode(SelectionMode.MULTI);
        tableOrderDetails.setEditorEnabled(true);
        tableOrderDetails.setSizeFull();
        tableOrderDetails.setEditorSaveCaption("Lưu");
        tableOrderDetails.setEditorCancelCaption("Hủy");
        tableOrderDetails.withProperties("frk_material_code", "frk_material_name", "frk_material_unit", "frk_material_stock", "quantityNeeded",
                "quantityDelivered", "quantityLacked", "frk_material_quantity", "formattedPrice", "saleOff", "formattedSellingPrice",
                "formattedTotalAmount", "formattedImportPrice");

        tableOrderDetails.getDefaultHeaderRow().getCell("frk_material_code").setHtml("<b>Mã Hàng</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("frk_material_name").setHtml("<b>Tên Hàng</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("frk_material_unit").setHtml("<b>Đơn Vị</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("frk_material_stock").setHtml("<b>Mã Kho</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("quantityNeeded").setHtml("<b>SL Đặt Hàng</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("quantityDelivered").setHtml("<b>SL Xuất</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("quantityLacked").setHtml("<b>SL Thiếu</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("frk_material_quantity").setHtml("<b>Tồn Cuối</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("formattedPrice").setHtml("<b>Giá Chuẩn</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("saleOff").setHtml("<b>Chiết Khấu</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("formattedSellingPrice").setHtml("<b>Giá Bán</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("formattedTotalAmount").setHtml("<b>Thành Tiền</b>");
        tableOrderDetails.getDefaultHeaderRow().getCell("formattedImportPrice").setHtml("<b>Giá Nhập</b>");
        setColumnFiltering(true);

        orderDetails = new ArrayList<Orderdetail>();
        tableOrderDetails.setRows(orderDetails);

        // Handle value change for some text fields
        tableOrderDetails.getColumn("quantityLacked").setEditorField(createTextField(false));
        tableOrderDetails.getColumn("quantityDelivered").setEditorField(createTextField(true));
        tableOrderDetails.getColumn("quantityNeeded").setEditorField(createTextField(true));
        tableOrderDetails.getColumn("frk_material_quantity").setEditorField(createTextField(false));
        tableOrderDetails.getColumn("formattedPrice").setEditorField(createTextField(true));
        tableOrderDetails.getColumn("formattedSellingPrice").setEditorField(createTextField(false));
        tableOrderDetails.getColumn("formattedTotalAmount").setEditorField(createTextField(false));

        // Size for some columns
        tableOrderDetails.getColumn("formattedSellingPrice").setWidth(100);

        // Disable some columns
        tableOrderDetails.getColumn("frk_material_code").setEditable(false);
        tableOrderDetails.getColumn("frk_material_name").setEditable(false);
        tableOrderDetails.getColumn("frk_material_unit").setEditable(false);
        tableOrderDetails.getColumn("frk_material_stock").setEditable(false);
        tableOrderDetails.getColumn("formattedImportPrice").setEditable(false);

        TextField txtTableSaleOff = new TextField();
        txtTableSaleOff.setConverter(new StringToFloatConverter() {
            private static final long serialVersionUID = 3312602926285681764L;

            @Override
            protected NumberFormat getFormat(Locale locale) {
                return Utilities.getPercentageFormat();
            }
        });

        tableOrderDetails.getColumn("saleOff").setEditorField(txtTableSaleOff);
        tableOrderDetails.getColumn("saleOff").setConverter(new StringToFloatConverter() {
            private static final long serialVersionUID = 3312602926285681764L;

            @Override
            protected NumberFormat getFormat(Locale locale) {
                return Utilities.getPercentageFormat();
            }
        });

        tableOrderDetails.getColumn("quantityNeeded").getEditorField().addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 9041180115447481664L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                presenter.updateColumnsWhenChangeQuantityNeeded(event);
            }
        });

        tableOrderDetails.getColumn("quantityDelivered").getEditorField().addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 7100286762666013961L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                presenter.updateColumnsWhenChangeQuantityDelivered(event);
            }
        });

        tableOrderDetails.getColumn("formattedPrice").getEditorField().addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 5675838268905015025L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                presenter.updateColumnsWhenChangePrice(event);
            }

        });

        tableOrderDetails.getColumn("saleOff").getEditorField().addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 2922146168876632193L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                presenter.updateColumnsWhenChangeSaleOff(event);
            }

        });

        LuvsoftOrderDetailValidator orderdetailValidator = new LuvsoftOrderDetailValidator("quantityDelivered");
        tableOrderDetails.getColumn("quantityDelivered").getEditorField().addValidator(orderdetailValidator);

        tableOrderDetails.getEditorFieldGroup().addCommitHandler(new CommitHandler() {
            private static final long serialVersionUID = 5721445674624128951L;

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
//                orderdetailValidator.setEntity((Orderdetail) tableOrderDetails.getEditedItemId());
//                orderdetailValidator.setCalledByPreCommit(true);
//                tableOrderDetails.getEditorFieldGroup().isValid();
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                presenter.calculateTotalAmountByOrderdetails();
            }
        });

        // Footer
        // //////////////////////////////////////////////////////////
        // | totalwrapper                                         |//
        // //////////////////////////////////////////////////////////
        // | footer                                               |//
        // |  _______   _____                      ______    ____ |//
        // | |printer| | paid |                   |discard| |save||//
        // |  -------   ------                     -------   ---- |//
        // //////////////////////////////////////////////////////////
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.setSizeFull();
        footer.setMargin(new MarginInfo(false, true, false, true));
        printer = new Button("In", FontAwesome.PRINT);
        printer.addStyleName(ValoTheme.BUTTON_PRIMARY);

        paid = new Button("Thanh Toán", FontAwesome.DOLLAR);
        paid.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        paid.addClickListener(this);

        save = new Button("Lưu", FontAwesome.SAVE);
        save.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        discard = new Button("Hủy", FontAwesome.BAN);
        footer.addComponents(printer, paid, discard, save);
        footer.setExpandRatio(paid, 1.0f);

        HorizontalLayout totalwrapper = new HorizontalLayout();
        totalwrapper.setSizeFull();
        totalwrapper.setMargin(new MarginInfo(false, true, false, true));

        totalAmountOfOrderdetails = new Label("______ VNĐ");
        totalAmountOfOrderdetails.setCaption("<b>Tổng tiền</b>");
        totalAmountOfOrderdetails.setCaptionAsHtml(true);
        totalAmountOfOrderdetails.addStyleName("text-align-right");

        deleteSelectedOrderdetails = new Button("Xóa Vật Tư", FontAwesome.BAN);
        deleteSelectedOrderdetails.addStyleName(ValoTheme.BUTTON_DANGER + " " + ValoTheme.BUTTON_TINY);
        deleteSelectedOrderdetails.addClickListener(this);

        totalwrapper.addComponent(deleteSelectedOrderdetails);
        totalwrapper.addComponent(totalAmountOfOrderdetails);

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
        if (order != null) {
            presenter.createOrderTypes(optionsOrderType, order.getOrdertype());
            if (order.getId() != -1) {
                orderNumber.setValue(order.getOrderCode());
                orderContent.setValue(order.getContent());
                orderDate.setValue(order.getDate());
                orderNote.setValue(order.getNote());
            } else {
                presenter.generateOrderCode(orderNumber);
                orderDate.setValue(new Date());
            }
        }

        optionsOrderType.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 9041180115447481664L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                order.setOrdertype((Ordertype) event.getProperty().getValue());
            }
        });

        // Handle printing button
        BrowserWindowOpener opener = new BrowserWindowOpener(StockManagementUI.class);// "http://google.com"
        opener.setFeatures("height=50000,width=80000,fullScreen=yes,menubar=no,location=no,resizable=no,scrollbars=no,status=no");
        opener.setWindowName("_new");// _new, _blank, _top, etc.
        opener.setParameter("OPEN_REASON", "PRINT");
        opener.extend(printer);
        printer.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                presenter.printOrder();
            }
        });
    }

    private void setColumnFiltering(boolean filtered) {
        if (filtered && filteringHeader == null) {
            filteringHeader = tableOrderDetails.appendHeaderRow();
            for (String property : tableProperties) {

                // Add new TextFields to each column which filters the data from
                // that column
                String columnId = property;
                SuggestField filter = new SuggestField();
                filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
                filter.setInputPrompt("Tìm Kiếm");
                filter.setImmediate(true);
                filter.setMinimumQueryCharacters(2);
                filter.setBuffered(true);

                if (property.equals("frk_material_code")) {
                    presenter.setUpSuggestFieldForMaterial(filter, MaterialConverter.BY_CODE);
                } else if (property.equals("frk_material_name")) {
                    presenter.setUpSuggestFieldForMaterial(filter, MaterialConverter.BY_NAME);
                }

                filter.addValueChangeListener(new ValueChangeListener() {
                    private static final long serialVersionUID = 6452140842646163170L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        if(((Material) filter.getValue()).getId() == -1) {
                            return;
                        }

                        Orderdetail orderDetail = new Orderdetail();
                        presenter.fillDefaultDataForOrderDetail(orderDetail, filter);
                        // Add orderdetail to table
                        if (presenter.addToOrderDetailList(orderDetail, orderDetails)) {
                            tableOrderDetails.setRows(orderDetails);
                        }

                        // In fact, valueChange event is only fired when change value of suggestionField
                        // it will not be fired if we select the old one
                        // therefore, we will change value of filter to a default one (empty material)
                        // in order to select this value again
                        filter.setValue(new Material());
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
        if (customer == null) {
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

                if(order != null) {
                    order.setCustomer(customer);
                }

                fillTextFieldByCustomer(customer);
            }
        });
        return search;
    }

    private boolean validateAllFields() {
        try {
            customerCodeValidator.validate(customerCode.getValue());
            orderNumberValidator.validate(orderNumber.getValue().trim());
            orderContentValidator.validate(orderContent.getValue().trim());
            orderDateValidator.validate(orderDate.getValue());
        } catch (InvalidValueException e) {
            Notification notify = new Notification("<b>Thông Báo</b>", e.getHtmlMessage(), Type.TRAY_NOTIFICATION, true);
            notify.show(Page.getCurrent());
            return false;
        }
        return true;
    }

    public TextField createTextField(boolean isEnable) {
        return new TextField() {
            private static final long serialVersionUID = 436326517648379862L;

            @Override
            public void setEnabled(boolean enabled) {
                super.setEnabled(isEnable);
            }
        };
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(save)) {
            if(!validateAllFields()) {
                return;
            }

            LuvsoftConfirmationDialog dialog = new LuvsoftConfirmationDialog("Xác nhận lưu hóa đơn?");
            dialog.addLuvsoftClickListener(new ClickListener() {
                private static final long serialVersionUID = 351366856643651627L;

                @Override
                public void buttonClick(ClickEvent event) {
                    presenter.saveOrder();
                    dialog.close();
                }
            });
            UI.getCurrent().addWindow(dialog);
        } else if(event.getButton().equals(deleteSelectedOrderdetails)) {
            presenter.deleteSelectedOrderdetails();
        } else if(event.getButton().equals(paid)) {
            LuvsoftConfirmationDialog dialog = new LuvsoftConfirmationDialog("Xác nhận thanh toán?");
            dialog.addLuvsoftClickListener(new ClickListener() {
                private static final long serialVersionUID = 351366856643651627L;

                @Override
                public void buttonClick(ClickEvent event) {
                    presenter.paidOrder();
                    dialog.close();
                }
            });
            UI.getCurrent().addWindow(dialog);
        } else if(event.getButton().equals(discard)) {
            create();
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OptionGroup getOptionsOrderType() {
        return optionsOrderType;
    }

    public void setOptionsOrderType(OptionGroup optionsOrderType) {
        this.optionsOrderType = optionsOrderType;
    }

    public TextField getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(TextField orderNumber) {
        this.orderNumber = orderNumber;
    }

    public TextField getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(TextField orderContent) {
        this.orderContent = orderContent;
    }

    public DateField getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(DateField orderDate) {
        this.orderDate = orderDate;
    }

    public TextArea getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(TextArea orderNote) {
        this.orderNote = orderNote;
    }

    public SuggestField getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(SuggestField customerCode) {
        this.customerCode = customerCode;
    }

    public SuggestField getCustomerName() {
        return customerName;
    }

    public void setCustomerName(SuggestField customerName) {
        this.customerName = customerName;
    }

    public SuggestField getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(SuggestField customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public TextField getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(TextField customerAddress) {
        this.customerAddress = customerAddress;
    }

    public TextField getBuyer() {
        return buyer;
    }

    public void setBuyer(TextField buyer) {
        this.buyer = buyer;
    }

    public MGrid<Orderdetail> getTableOrderDetails() {
        return tableOrderDetails;
    }

    public void setTableOrderDetails(MGrid<Orderdetail> tableOrderDetails) {
        this.tableOrderDetails = tableOrderDetails;
    }

    public List<Orderdetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<Orderdetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<SuggestField> getFilterFields() {
        return filterFields;
    }

    public void setFilterFields(List<SuggestField> filterFields) {
        this.filterFields = filterFields;
    }

    public List<String> getTableProperties() {
        return tableProperties;
    }

    public void setTableProperties(List<String> tableProperties) {
        this.tableProperties = tableProperties;
    }

    public HeaderRow getFilteringHeader() {
        return filteringHeader;
    }

    public void setFilteringHeader(HeaderRow filteringHeader) {
        this.filteringHeader = filteringHeader;
    }

    public Button getPrinter() {
        return printer;
    }

    public void setPrinter(Button printer) {
        this.printer = printer;
    }

    public Button getSave() {
        return save;
    }

    public void setSave(Button save) {
        this.save = save;
    }

    public Button getDiscard() {
        return discard;
    }

    public void setDiscard(Button discard) {
        this.discard = discard;
    }

    public OrderPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(OrderPresenter presenter) {
        this.presenter = presenter;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Label getTotalAmountOfOrderdetails() {
        return totalAmountOfOrderdetails;
    }

    public void setTotalAmountOfOrderdetails(Label totalAmountOfOrderdetails) {
        this.totalAmountOfOrderdetails = totalAmountOfOrderdetails;
    }

    public Button getPaid() {
        return paid;
    }

    public void setPaid(Button paid) {
        this.paid = paid;
    }
}
