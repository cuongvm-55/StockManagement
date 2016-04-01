package com.luvsoft.view.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.viritin.grid.MGrid;

import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.OrderPresenter;
import com.luvsoft.presenter.OrderPresenter.CustomerConverter;
import com.luvsoft.view.Order.OrderFormContent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
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

public class GenericTabFunction implements ClickListener {

    // MMI part
    HorizontalLayout wrapper;
    VerticalLayout leftPanel;

    // Left panel part
    private CssLayout sellPart;
    private Button order;
    private Button returnToSupplier;

    private CssLayout categoryPart;
    private Button material;
    private Button customer;
    private Button stock;

    // Presenter
    OrderPresenter presenter;

    public void init() {
        presenter = new OrderPresenter();

        wrapper = new HorizontalLayout();
        wrapper.setSizeFull();

        buildLeftPanel();

        Order order = new Order();
        order.verifyObject();
        OrderFormContent centerPart = new OrderFormContent(presenter, order);
        wrapper.addComponent(centerPart);

        wrapper.setExpandRatio(centerPart, 1.0f);
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

    @Override
    public void buttonClick(ClickEvent event) {
        // TODO Auto-generated method stub
        
    }

    public HorizontalLayout getWapper() {
        return wrapper;
    }
}
