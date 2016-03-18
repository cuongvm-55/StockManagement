package com.luvsoft.view.component;

import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.luvsoft.entities.AbstractEntity;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

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

    public void init() {
        wrapper = new HorizontalLayout();
        wrapper.addStyleName("caption-center");
        wrapper.setSizeFull();

        leftPanel = new VerticalLayout();
        leftPanel.setSizeFull();
        leftPanel.addStyleName("left-panel");
        wrapper.addComponent(leftPanel);

        // Order part
        order = new Button("Hóa Đơn", new ThemeResource("image/bill.png"));
        order.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        order.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        order.setWidth("100%");
        returnToSupplier = new Button("Phiếu Xuất Trả Nhà Cung Cấp", new ThemeResource("image/bill.png"));
        returnToSupplier.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " multiline " + ValoTheme.BUTTON_BORDERLESS_COLORED);
        returnToSupplier.setWidth("100%");

        sellPart = new CssLayout();
        sellPart.setSizeFull();
        sellPart.setCaptionAsHtml(true);
        sellPart.setCaption("<b>Bán Hàng</b>");
        sellPart.addComponents(order, returnToSupplier);
        leftPanel.addComponent(sellPart);

        // Category part
        material = new Button("Hàng Hóa", new ThemeResource("image/bill.png"));
        material.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        material.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        material.setWidth("100%");

        customer = new Button("Khách Hàng", new ThemeResource("image/bill.png"));
        customer.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        customer.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        customer.setWidth("100%");

        stock = new Button("Kho", new ThemeResource("image/bill.png"));
        stock.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        stock.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        stock.setWidth("100%");

        categoryPart = new CssLayout();
        categoryPart.setSizeFull();
        categoryPart.setCaptionAsHtml(true);
        categoryPart.setCaption("<b>Danh Mục</b>");
        categoryPart.addComponents(material, material, customer, stock);
        leftPanel.addComponent(categoryPart);

        // Center part
        center = new VerticalLayout();
        center.setSizeFull();;
        center.setCaptionAsHtml(true);
        center.setCaption("<b>Hóa đơn</b>");
        wrapper.addComponent(center);

        wrapper.setExpandRatio(center, 1.0f);
        wrapper.setExpandRatio(leftPanel, 0.15f);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        // TODO Auto-generated method stub
        
    }

    public HorizontalLayout getWapper() {
        return wrapper;
    }
}
