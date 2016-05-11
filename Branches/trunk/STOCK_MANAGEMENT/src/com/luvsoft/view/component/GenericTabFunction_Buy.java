package com.luvsoft.view.component;

import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Coupontype.COUPON_TYPES;
import com.luvsoft.presenter.CouponPresenter;
import com.luvsoft.view.Coupon.CouponFormContent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class GenericTabFunction_Buy implements ClickListener {
    private static final long serialVersionUID = -8619515264131687495L;
    // MMI part
    private HorizontalLayout wrapper;
    private VerticalLayout leftPanel;
    private VerticalLayout centerPart;
    private CouponFormContent couponForm;

    // Left panel part
    private CssLayout buyPart;
    private Button buyCoupon;
    private Button importMaterialReturned;

    private CssLayout categoryPart;
    private Button material;
    private Button customer;
    private Button stock;

    // Presenter
    CouponPresenter presenter;

    public void init() {
        presenter = new CouponPresenter();

        wrapper = new HorizontalLayout();
        wrapper.setSizeFull();

        buildLeftPanel();

        centerPart = new VerticalLayout();
        centerPart.setSizeFull();
        wrapper.addComponent(centerPart);

        couponForm = new CouponFormContent(presenter, new Coupon(), COUPON_TYPES.PH_NHAPMUA);
        couponForm.create();
        presenter.setView(couponForm);
        centerPart.addComponent(couponForm);

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
            buyPart = new CssLayout();
            buyPart.setSizeFull();
            buyPart.setCaptionAsHtml(true);
            buyPart.setCaption("<b>Mua Hàng</b>");
            leftPanel.addComponent(buyPart);
            {
                buyCoupon = new Button("Phiếu Nhập Mua", new ThemeResource("image/bill.png"));
                buyCoupon.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
                buyCoupon.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                buyCoupon.setWidth("100%");
                importMaterialReturned = new Button("Phiếu Nhập Hàng Trả Lại", new ThemeResource("image/bill.png"));
                importMaterialReturned.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP + " multiline " + ValoTheme.BUTTON_BORDERLESS_COLORED);
                importMaterialReturned.setWidth("100%");
    
                buyPart.addComponents(buyCoupon, importMaterialReturned);
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
        buyCoupon.addClickListener(this);
        importMaterialReturned.addClickListener(this);

        material.addClickListener(this);
        customer.addClickListener(this);
        stock.addClickListener(this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton().equals(buyCoupon)) {
            centerPart.removeAllComponents();

            couponForm = new CouponFormContent(presenter, new Coupon(), COUPON_TYPES.PH_NHAPMUA);
            couponForm.create();
            presenter.setView(couponForm);

            centerPart.addComponent(couponForm);
        } else if(event.getButton().equals(importMaterialReturned)) {
            centerPart.removeAllComponents();

            couponForm = new CouponFormContent(presenter, new Coupon(), COUPON_TYPES.PH_NHAPHANGTRALAI);
            couponForm.create();

            centerPart.addComponent(couponForm);
        }
    }

    public HorizontalLayout getWapper() {
        return wrapper;
    }
}
