package com.luvsoft.view.component;

import com.luvsoft.entities.Order;
import com.luvsoft.presenter.OrderPresenter;
import com.luvsoft.view.Order.OrderFormContent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class GenericTabFunction implements ClickListener {
    private static final long serialVersionUID = -8619515264131687495L;
    // MMI part
    private HorizontalLayout wrapper;
    private VerticalLayout leftPanel;
    private OrderFormContent centerPart;

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
        centerPart = new OrderFormContent(presenter, order);
        presenter.setView(centerPart);

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

        // Event register
        order.addClickListener(this);
        returnToSupplier.addClickListener(this);

        material.addClickListener(this);
        customer.addClickListener(this);
        stock.addClickListener(this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton().equals(order)) {
            centerPart.setOrder(new Order());
            centerPart.create();
        }
    }

    public HorizontalLayout getWapper() {
        return wrapper;
    }
}
