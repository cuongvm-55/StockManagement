package com.luvsoft.view.Coupon;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.vaadin.suggestfield.SuggestField;
import org.vaadin.viritin.grid.MGrid;

import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Coupondetail;
import com.luvsoft.entities.Coupontype;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Coupontype.COUPON_TYPES;
import com.luvsoft.presenter.CouponPresenter;
import com.luvsoft.presenter.CouponPresenter.CustomerConverter;
import com.luvsoft.presenter.CouponPresenter.MaterialConverter;
import com.luvsoft.stockmanagement.StockManagementUI;
import com.luvsoft.utils.Utilities;
import com.luvsoft.view.component.LuvsoftConfirmationDialog;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CouponFormContent extends VerticalLayout implements ClickListener {
    private static final long serialVersionUID = -6147363220520400910L;
    // Information part
    private TextField couponNumber; // auto generate
    private TextField couponContent;
    private DateField couponDate;
    private TextArea couponNote;

    private SuggestField customerCode;
    private SuggestField customerName;
    private SuggestField customerPhoneNumber;
    private TextField customerAddress; // fetch form customer but can edit
    private TextField transporter;

    private MGrid<Coupondetail> tableCouponDetails;
    private List<Coupondetail> couponDetails;
    private List<SuggestField> filterFields;
    private List<String> tableProperties;
    private HeaderRow filteringHeader;

    // Footer
    private Label totalAmountOfCoupondetails;
    private Button printer;
    private Button save;
    private Button discard;
    private Button deleteSelectedOrderdetails;
    private Button paid;

    // Presenter
    private CouponPresenter presenter;
    private Coupon coupon;
    private Customer customer;
    private COUPON_TYPES couponType;

    public CouponFormContent(CouponPresenter presenter, Coupon coupon, Coupontype.COUPON_TYPES couponType) {
        super();
        this.presenter = presenter;
        this.coupon = coupon;
        this.couponType = couponType;
        create();
    }

    @SuppressWarnings("serial")
    public void create() {
        addStyleName("formlayout-spacing max-textfield-width background-blue");
        setSizeFull();
        removeAllComponents();
        filteringHeader = null;

        Label centertitle = new Label();
        if(couponType.equals(COUPON_TYPES.PH_NHAPMUA)) {
            centertitle.setValue("Phiếu Nhập Mua");
        } else if(couponType.equals(COUPON_TYPES.PH_NHAPHANGTRALAI)) {
            centertitle.setValue("Phiếu Nhập Hàng Trả Lại");
        } else if(couponType.equals(COUPON_TYPES.PH_XUATTRANHACUNGCAP)) {
            centertitle.setValue("Phiếu Xuất Trả Nhà Cung Cấp");
        }

        centertitle.addStyleName("center font16 " + ValoTheme.LABEL_BOLD);
        addComponent(centertitle);

        couponNumber = new TextField(); // auto generate
        couponNumber.setCaption("Số Phiếu");

        customerCode = createSuggestFieldForCustomer(CustomerConverter.BY_CODE, "Mã Khách Hàng", "Tìm theo mã khách");
        customerName = createSuggestFieldForCustomer(CustomerConverter.BY_NAME, "Tên Khách Hàng", "Tìm theo tên");
        customerPhoneNumber = createSuggestFieldForCustomer(CustomerConverter.BY_PHONE_NUMBER, "Số Điện Thoại", "Tìm theo số điện thoại");

        customerAddress = new TextField(); // fetch form customer but can edit
        customerAddress.setCaption("Địa Chỉ");

        transporter = new TextField();
        transporter.setCaption("Người Vận Chuyển");
        couponContent = new TextField();
        couponContent.setCaption("Diễn Giải");
        if(couponType.equals(COUPON_TYPES.PH_NHAPMUA)) {
            couponContent.setValue("Nhập Mua Hàng");
        } else if(couponType.equals(COUPON_TYPES.PH_NHAPHANGTRALAI)) {
            couponContent.setValue("Nhập Hàng Trả Lại");
        } else if(couponType.equals(COUPON_TYPES.PH_XUATTRANHACUNGCAP)) {
            couponContent.setValue("Xuất Trả Nhà Cung Cấp");
        }

        couponDate = new DateField();
        couponDate.setCaption("Ngày");

        couponNote = new TextArea();
        couponNote.setCaption("Ghi Chú");
        couponNote.setRows(1);
        couponNote.setColumns(61);

        // //////////////////////////////////////////////////////////////
        // | wrapper1 || wrapper2 || wrapper3 |//
        // | || || |//
        // | || || |//
        // //////////////////////////////////////////////////////////////
        // | wrapper4 //
        // //////////////////////////////////////////////////////////////
        FormLayout wrapper1 = new FormLayout();
        wrapper1.setSizeFull();
        wrapper1.addComponents(couponNumber, customerCode, customerAddress);

        FormLayout wrapper2 = new FormLayout();
        wrapper2.setSizeFull();
        wrapper2.addComponents(customerName, transporter);

        FormLayout wrapper3 = new FormLayout();
        wrapper3.setSizeFull();
        wrapper3.addComponents(couponContent, customerPhoneNumber, couponDate);

        FormLayout wrapper4 = new FormLayout();
        wrapper4.addStyleName("addition-space");
        wrapper4.addComponent(couponNote);

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

        tableCouponDetails = new MGrid<Coupondetail>(Coupondetail.class);
        tableCouponDetails.setSelectionMode(SelectionMode.MULTI);
        tableCouponDetails.setEditorEnabled(true);
        tableCouponDetails.setSizeFull();
        tableCouponDetails.setEditorSaveCaption("Lưu");
        tableCouponDetails.setEditorCancelCaption("Hủy");
        tableCouponDetails.withProperties("frk_material_code", "frk_material_name", "frk_material_unit", "frk_material_stock", "quantity",
                "frk_material_quantity", "formattedPrice", "saleOff", "formattedBuyingPrice", "formattedTotalAmount");

        tableCouponDetails.getDefaultHeaderRow().getCell("frk_material_code").setHtml("<b>Mã Hàng</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("frk_material_name").setHtml("<b>Tên Hàng</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("frk_material_unit").setHtml("<b>Đơn Vị</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("frk_material_stock").setHtml("<b>Mã Kho</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("quantity").setHtml("<b>Số Lượng</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("frk_material_quantity").setHtml("<b>Tồn Cuối</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("formattedPrice").setHtml("<b>Giá Chuẩn</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("saleOff").setHtml("<b>Chiết Khấu</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("formattedBuyingPrice").setHtml("<b>Giá Mua</b>");
        tableCouponDetails.getDefaultHeaderRow().getCell("formattedTotalAmount").setHtml("<b>Thành Tiền</b>");
        setColumnFiltering(true);

        couponDetails = new ArrayList<Coupondetail>();
        tableCouponDetails.setRows(couponDetails);

        // Handle value change for some text fields
        tableCouponDetails.getColumn("quantity").setEditorField(new TextField());
        tableCouponDetails.getColumn("frk_material_quantity").setEditorField(new TextField());
        tableCouponDetails.getColumn("formattedPrice").setEditorField(new TextField());
        tableCouponDetails.getColumn("formattedBuyingPrice").setEditorField(new TextField());
        tableCouponDetails.getColumn("formattedTotalAmount").setEditorField(new TextField());

        // Size for some columns
        tableCouponDetails.getColumn("formattedBuyingPrice").setWidth(100);

        // Disable some columns
        tableCouponDetails.getColumn("frk_material_code").setEditable(false);
        tableCouponDetails.getColumn("frk_material_name").setEditable(false);
        tableCouponDetails.getColumn("frk_material_unit").setEditable(false);
        tableCouponDetails.getColumn("frk_material_stock").setEditable(false);

        TextField txtTableSaleOff = new TextField();
        txtTableSaleOff.setConverter(new StringToFloatConverter() {
            private static final long serialVersionUID = 3312602926285681764L;

            @Override
            protected NumberFormat getFormat(Locale locale) {
                return Utilities.getPercentageFormat();
            }
        });

        tableCouponDetails.getColumn("saleOff").setEditorField(txtTableSaleOff);
        tableCouponDetails.getColumn("saleOff").setConverter(new StringToFloatConverter() {
            private static final long serialVersionUID = 3312602926285681764L;

            @Override
            protected NumberFormat getFormat(Locale locale) {
                return Utilities.getPercentageFormat();
            }
        });

        tableCouponDetails.getColumn("quantity").getEditorField().addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 9041180115447481664L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                presenter.calculateTotalAmountWhenChangeQuantity(event);
            }
        });

        tableCouponDetails.getColumn("formattedPrice").getEditorField().addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 5675838268905015025L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                presenter.calculateBuyingPriceWhenChangePrice(event);
            }

        });

        tableCouponDetails.getColumn("saleOff").getEditorField().addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 2922146168876632193L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                presenter.calculateSellingPriceWhenChangeSaleOff(event);
            }

        });

        tableCouponDetails.getEditorFieldGroup().addCommitHandler(new CommitHandler() {
            private static final long serialVersionUID = 5721445674624128951L;

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                // TODO validate form
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                presenter.calculateTotalAmountByCoupondetails();
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

        totalAmountOfCoupondetails = new Label("______ VNĐ");
        totalAmountOfCoupondetails.setCaption("<b>Tổng tiền</b>");
        totalAmountOfCoupondetails.setCaptionAsHtml(true);
        totalAmountOfCoupondetails.addStyleName("text-align-right");

        deleteSelectedOrderdetails = new Button("Xóa Vật Tư", FontAwesome.BAN);
        deleteSelectedOrderdetails.addStyleName(ValoTheme.BUTTON_DANGER + " " + ValoTheme.BUTTON_TINY);
        deleteSelectedOrderdetails.addClickListener(this);

        totalwrapper.addComponent(deleteSelectedOrderdetails);
        totalwrapper.addComponent(totalAmountOfCoupondetails);

        addComponents(centerPart, tableCouponDetails, totalwrapper, footer);
        setExpandRatio(centerPart, 0.3f);
        setExpandRatio(tableCouponDetails, 0.5f);
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
        if (coupon != null) {
            if (coupon.getId() != -1) {
                couponNumber.setValue(coupon.getCode());
                couponContent.setValue(coupon.getContent());
                couponDate.setValue(coupon.getDate());
                couponNote.setValue(coupon.getNote());
            } else {
                presenter.generateCouponCode(couponNumber);
                couponDate.setValue(new Date());
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
                presenter.printCoupon();
            }
        });
    }

    private void setColumnFiltering(boolean filtered) {
        if (filtered && filteringHeader == null) {
            filteringHeader = tableCouponDetails.appendHeaderRow();
            for (String property : tableProperties) {

                // Add new TextFields to each column which filters the data from
                // that column
                String columnId = property;
                SuggestField filter = new SuggestField();
                filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
                filter.setInputPrompt("Tìm Kiếm");
                filter.setImmediate(true);
                filter.setMinimumQueryCharacters(2);

                if (property.equals("frk_material_code")) {
                    presenter.setUpSuggestFieldForMaterial(filter, MaterialConverter.BY_CODE);
                } else if (property.equals("frk_material_name")) {
                    presenter.setUpSuggestFieldForMaterial(filter, MaterialConverter.BY_NAME);
                }

                filter.addValueChangeListener(new ValueChangeListener() {
                    private static final long serialVersionUID = 6452140842646163170L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Coupondetail coupondetail = new Coupondetail();
                        presenter.fillDefaultDataForCouponDetail(coupondetail, filter);
                        // Add orderdetail to table
                        if (presenter.addToCouponDetailList(coupondetail, couponDetails)) {
                            tableCouponDetails.setRows(couponDetails);
                        }
                    }
                });

                filteringHeader.getCell(columnId).setComponent(filter);
                filteringHeader.getCell(columnId).setStyleName("filter-header");

                // save to handle filter box
                filterFields.add(filter);
            }
        } else if (!filtered && filteringHeader != null) {
            tableCouponDetails.removeHeaderRow(filteringHeader);
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

                if(coupon != null) {
                    coupon.setCustomer(customer);
                }

                fillTextFieldByCustomer(customer);
            }
        });
        return search;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(save)) {
            LuvsoftConfirmationDialog dialog = new LuvsoftConfirmationDialog("Xác nhận lưu hóa đơn?");
            dialog.addLuvsoftClickListener(new ClickListener() {
                private static final long serialVersionUID = 351366856643651627L;

                @Override
                public void buttonClick(ClickEvent event) {
                    presenter.saveCoupon();
                    dialog.close();
                }
            });
            UI.getCurrent().addWindow(dialog);
        } else if(event.getButton().equals(deleteSelectedOrderdetails)) {
            presenter.deleteSelectedCoupondetails();
        } else if(event.getButton().equals(paid)) {
            LuvsoftConfirmationDialog dialog = new LuvsoftConfirmationDialog("Xác nhận thanh toán?");
            dialog.addLuvsoftClickListener(new ClickListener() {
                private static final long serialVersionUID = 351366856643651627L;

                @Override
                public void buttonClick(ClickEvent event) {
                    presenter.paidCoupon();
                    dialog.close();
                }
            });
            UI.getCurrent().addWindow(dialog);
        } else if(event.getButton().equals(discard)) {
            create();
        }
    }

    public TextField getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(TextField CouponNumber) {
        this.couponNumber = CouponNumber;
    }

    public TextField getCouponContent() {
        return couponContent;
    }

    public void setCouponContent(TextField CouponContent) {
        this.couponContent = CouponContent;
    }

    public DateField getCouponDate() {
        return couponDate;
    }

    public void setCouponDate(DateField CouponDate) {
        this.couponDate = CouponDate;
    }

    public TextArea getCouponNote() {
        return couponNote;
    }

    public void setCouponNote(TextArea CouponNote) {
        this.couponNote = CouponNote;
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

    public TextField getTransporter() {
        return transporter;
    }

    public void setTransporter(TextField transporter) {
        this.transporter = transporter;
    }

    public MGrid<Coupondetail> getTableCouponDetails() {
        return tableCouponDetails;
    }

    public void setTableOrderDetails(MGrid<Coupondetail> tableCouponDetails) {
        this.tableCouponDetails = tableCouponDetails;
    }

    public List<Coupondetail> getCouponDetails() {
        return couponDetails;
    }

    public void setCouponDetails(List<Coupondetail> couponDetails) {
        this.couponDetails = couponDetails;
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

    public CouponPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(CouponPresenter presenter) {
        this.presenter = presenter;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Label getTotalAmountOfCoupondetails() {
        return totalAmountOfCoupondetails;
    }

    public void setTotalAmountOfCoupondetails(Label totalAmountOfCoupondetails) {
        this.totalAmountOfCoupondetails = totalAmountOfCoupondetails;
    }

    public Button getPaid() {
        return paid;
    }

    public void setPaid(Button paid) {
        this.paid = paid;
    }

    public COUPON_TYPES getCouponType() {
        return couponType;
    }

    public void setCouponType(COUPON_TYPES couponType) {
        this.couponType = couponType;
    }
}
