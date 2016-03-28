package com.luvsoft.view.component;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.OrderPresenter;
import com.luvsoft.presenter.StockTypePresenter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

public class GenericTabFunction implements ClickListener{

    // MMI part
    HorizontalLayout wrapper;
    VerticalLayout leftPanel;
    VerticalLayout center;

    // Left panel part
    private CssLayout sellPart;
    private Button order;
    private Button returnToSupplier;

    private CssLayout categoryPart;
    private Button material;
    private Button customer;
    private Button stock;

    // Center part
    // Information part
    private OptionGroup oderType; // fetch from OrderType
    private TextField orderNumber; // auto generate
    private TextField customerCode;
    private TextField customerName;
    private AutocompleteTextField customerPhoneNumber;
    private TextField customerAddress; // fetch form customer but can edit
    private TextField buyer;
    private TextField content;
    private DateField date;
    private TextArea note;

    private MGrid<Stocktype> table;

    // Footer
    private Button printer;
    private Button save;
    private Button discard;

    // Presenter
    OrderPresenter presenter;

    public void init() {
        presenter = new OrderPresenter();

        wrapper = new HorizontalLayout();
        wrapper.setSizeFull();

        buildLeftPanel();

        buildCenterPart();

        wrapper.setExpandRatio(center, 1.0f);
        wrapper.setExpandRatio(leftPanel, 0.15f);
    }

    public void buildLeftPanel()
    {
        leftPanel = new VerticalLayout();
        leftPanel.setSizeFull();
        leftPanel.addStyleName("left-panel caption-center");
        wrapper.addComponent(leftPanel);
        {
            // Order part
            sellPart = new CssLayout();
            sellPart.setSizeFull();
            sellPart.setCaptionAsHtml(true);
            sellPart.setCaption("<b>Bán Hàng</b>");
            leftPanel.addComponent(sellPart);
            {
                order = new Button("Hóa Đơn", new ThemeResource("image/bill.png"));
                order.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
                order.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                order.setWidth("100%");
                returnToSupplier = new Button("Phiếu Xuất Trả Nhà Cung Cấp", new ThemeResource("image/bill.png"));
                returnToSupplier.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " multiline " + ValoTheme.BUTTON_BORDERLESS_COLORED);
                returnToSupplier.setWidth("100%");
    
                sellPart.addComponents(order, returnToSupplier);
            }
    
            // Category part
            categoryPart = new CssLayout();
            categoryPart.setSizeFull();
            categoryPart.setCaptionAsHtml(true);
            categoryPart.setCaption("<b>Danh Mục</b>");
            leftPanel.addComponent(categoryPart);
            {
                material = new Button("Hàng Hóa", new ThemeResource("image/product.png"));
                material.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
                material.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                material.setWidth("100%");
        
                customer = new Button("Khách Hàng", new ThemeResource("image/customer.png"));
                customer.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
                customer.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                customer.setWidth("100%");
        
                stock = new Button("Kho", new ThemeResource("image/stock.png"));
                stock.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
                stock.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                stock.setWidth("100%");
    
                categoryPart.addComponents(material, customer, stock);
            }
        }
    }

    public void buildCenterPart() {
        // Center part
        center = new VerticalLayout();
        center.addStyleName("formlayout-spacing max-textfield-width background-blue");
        center.setSizeFull();
        wrapper.addComponent(center);
        Label centertitle = new Label("Hóa đơn");
        centertitle.addStyleName("center font16 " + ValoTheme.LABEL_BOLD);
        center.addComponent(centertitle);

        // Information part
        oderType = new OptionGroup(); // fetch from OrderType
        oderType.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        oderType.setCaption("Loại Hóa Đơn");

        orderNumber = new TextField(); // auto generate
        orderNumber.setCaption("Số Hóa Đơn");

        customerCode = new TextField();
        customerCode.setCaption("Mã Khách Hàng");
        customerCode.setWidth("100%");

        
        customerName = new TextField();
        customerName.setCaption("Tên Khách Hàng");

        customerPhoneNumber = new AutocompleteTextField();
        customerPhoneNumber.setCaption("Số Điện Thoại");
        customerPhoneNumber.setCache(true);
        customerPhoneNumber.setDelay(150);
        customerPhoneNumber.setMinChars(3);
        customerPhoneNumber.setScrollBehavior(ScrollBehavior.NONE);
        customerPhoneNumber.setSuggestionLimit(0);
        customerPhoneNumber.setSuggestionProvider(presenter.getSuggestionProvider());
        customerPhoneNumber.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                System.out.println("Value is " + event.getProperty().getValue());
            }
        });

        customerAddress = new TextField(); // fetch form customer but can edit
        customerAddress.setCaption("Địa Chỉ");

        buyer = new TextField();
        buyer.setCaption("Người Mua");
        content = new TextField();
        content.setCaption("Diễn Giải");
        date = new DateField();
        date.setCaption("Ngày");

        note = new TextArea();
        note.setCaption("Ghi Chú");
        note.setRows(1);
        note.setColumns(61);

        FormLayout form1 = new FormLayout();
        form1.setSizeFull();
        form1.addComponents(oderType, customerCode, customerAddress);

        FormLayout form2 = new FormLayout();
        form2.setSizeFull();
        form2.addComponents(orderNumber, customerName, buyer);

        FormLayout form3 = new FormLayout();
        form3.setSizeFull();
        form3.addComponents(content, customerPhoneNumber, date);

        FormLayout form4 = new FormLayout();
        form4.addStyleName("addition-space");
        form4.addComponent(note);

        HorizontalLayout contentPart = new HorizontalLayout();
        contentPart.setWidth("100%");
        contentPart.setSpacing(true);
        contentPart.addComponents(form1, form2, form3);
        contentPart.setExpandRatio(form1, 0.8f);
        contentPart.setExpandRatio(form2, 0.6f);
        contentPart.setExpandRatio(form3, 0.6f);

        VerticalLayout centerPart = new VerticalLayout();
        centerPart.addComponents(contentPart, form4);
        centerPart.setMargin(new MarginInfo(false, true, false, true));

        table = new MGrid<Stocktype>();
        table.setEditorEnabled(true);
        table.setSizeFull();
        // Fake data
        List<Stocktype> orderDetails = new ArrayList<Stocktype>();
        Stocktype detail1 = new Stocktype("Abc", "xyz", null);
        Stocktype detail2 = new Stocktype("Abc", "xyz", null);
        Stocktype detail3 = new Stocktype("Abc", "xyz", null);
        Stocktype detail4 = new Stocktype("Abc", "xyz", null);
        Stocktype detail5 = new Stocktype("Abc", "xyz", null);
        Stocktype detail6 = new Stocktype("Abc", "xyz", null);
        Stocktype detail7 = new Stocktype("Abc", "xyz", null);
        Stocktype detail8 = new Stocktype("Abc", "xyz", null);
        orderDetails.add(detail1);
        orderDetails.add(detail2);
        orderDetails.add(detail3);
        orderDetails.add(detail4);
        orderDetails.add(detail5);
        orderDetails.add(detail6);
        orderDetails.add(detail7);
        orderDetails.add(detail8);
        table.setRows(orderDetails);

        // Footer
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

        center.addComponents(centerPart, table, totalwrapper, footer);
        center.setExpandRatio(centerPart, 0.3f);
        center.setExpandRatio(table, 0.5f);
        center.setExpandRatio(totalwrapper, 0.1f);
        center.setExpandRatio(footer, 0.1f);

        // Fake data
        oderType.addItem("Xuất Bán");
        oderType.addItem("Xuất Bán Nội Bộ");
        orderNumber.setValue("HD12345678");
        customerCode.setValue("KH123456789");
        customerName.setValue("Anh Nguyễn Văn An");
        customerAddress.setValue("19, Ngõ 1, Phạm Văn Đồng, Từ Liêm, Hà Nội");
    }
    @Override
    public void buttonClick(ClickEvent event) {
        // TODO Auto-generated method stub
        
    }

    public HorizontalLayout getWapper() {
        return wrapper;
    }
}
