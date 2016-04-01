package com.luvsoft.stockmanagement;

import com.luvsoft.view.Coupon.CouponTypeView;
import com.luvsoft.view.Customer.AreaView;
import com.luvsoft.view.Customer.CustomerType1View;
import com.luvsoft.view.Customer.CustomerType2View;
import com.luvsoft.view.Customer.CustomerView;
import com.luvsoft.view.Material.MaterialType1View;
import com.luvsoft.view.Material.MaterialType2View;
import com.luvsoft.view.Material.UnitView;
import com.luvsoft.view.Order.OrderTypeView;
import com.luvsoft.view.Stock.StockTypeView;
import com.luvsoft.view.Stock.StockView;
import com.luvsoft.view.component.GenericTabFunction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MainMenu extends TabSheet {
    private static final long serialVersionUID = 6290323462935186029L;
    // Sub tab category
    // Stock
    private static final String SUB_TAB_CATEGORY_STOCK = "Kho";
    private static final String SUB_TAB_CATEGORY_STOCK_TYPE = "Loại Kho";
    // Customer
    private static final String SUB_TAB_CATEGORY_CUSTOMER = "Khách Hàng";
    private static final String SUB_TAB_CATEGORY_CUSTOMER_TYPE_1 = "Loại Khách Hàng 1";
    private static final String SUB_TAB_CATEGORY_CUSTOMER_TYPE_2 = "Loại Khách Hàng 2";
    private static final String SUB_TAB_CATEGORY_AREA = "Khu Vực";
    // Material
    private static final String SUB_TAB_CATEGORY_MATERIAL_TYPE_1 = "Loại Vật Tư 1";
    private static final String SUB_TAB_CATEGORY_MATERIAL_TYPE_2 = "Loại Vật Tư 2";
    private static final String SUB_TAB_CATEGORY_UNIT = "Đơn Vị Tính";
    // Order
    private static final String SUB_TAB_CATEGORY_ORDER_TYPE = "Loại Hóa Đơn";
    // Coupon
    private static final String SUB_TAB_CATEGORY_COUNPON_TYPE = "Loại Phiếu";

    // List of views
    private StockTypeView stockTypeView;
    private StockView stockView;
    private AreaView areaView;
    private CouponTypeView couponTypeView;
    private CustomerView customerView;
    private CustomerType1View customerType1View;
    private CustomerType2View customerType2View;
    private MaterialType1View materialType1View;
    private MaterialType2View materialType2View;
    private OrderTypeView orderTypeView;
    private UnitView unitView;

    public MainMenu() {
        super();
        init();
    }

    public void initViews() {
        stockTypeView = new StockTypeView();
        stockTypeView.initView(); // the first tabsheet will be chosen automatically, we have to init it
        stockView = new StockView();
        areaView = new AreaView();
        couponTypeView = new CouponTypeView();
        customerView = new CustomerView();
        customerType1View = new CustomerType1View();
        customerType2View = new CustomerType2View();
        materialType1View = new MaterialType1View();
        materialType2View = new MaterialType2View();
        orderTypeView = new OrderTypeView();
        unitView = new UnitView();
    }

    @SuppressWarnings("serial")
    public void init() {
        addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
        setSizeFull();

        initViews();

        // Init submenus
        VerticalLayout subTabSystem = new VerticalLayout();
        addTab(subTabSystem, "Hệ Thống", FontAwesome.COGS);

        GenericTabFunction tab = new GenericTabFunction();
        tab.init();
        TabSheet subTabFunction = new TabSheet();
        subTabFunction.addStyleName(ValoTheme.TABSHEET_FRAMED);
        subTabFunction.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        subTabFunction.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        subTabFunction.setSizeFull();
        subTabFunction.addTab(tab.getWapper(), "Bán Hàng", FontAwesome.DOLLAR);
        addTab(subTabFunction, "Chức Năng", FontAwesome.FOLDER_OPEN);

        VerticalLayout subTabReport = new VerticalLayout();
        addTab(subTabReport, "Báo Cáo", FontAwesome.FILE_EXCEL_O);

        TabSheet subTabCategory = new TabSheet();
        subTabCategory.addStyleName(ValoTheme.TABSHEET_FRAMED);
        subTabCategory.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        subTabCategory.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        subTabCategory.setSizeFull();
        addTab(subTabCategory, "Danh Mục", FontAwesome.BOOK);
        subTabCategory.addTab(stockTypeView.getWrapper(), SUB_TAB_CATEGORY_STOCK_TYPE, FontAwesome.TRUCK);
        subTabCategory.addTab(stockView.getWrapper(), SUB_TAB_CATEGORY_STOCK, FontAwesome.CAR);
        subTabCategory.addTab(customerView.getWrapper(), SUB_TAB_CATEGORY_CUSTOMER, FontAwesome.MALE);
        subTabCategory.addTab(customerType1View.getWrapper(), SUB_TAB_CATEGORY_CUSTOMER_TYPE_1, FontAwesome.MALE);
        subTabCategory.addTab(customerType2View.getWrapper(), SUB_TAB_CATEGORY_CUSTOMER_TYPE_2, FontAwesome.FEMALE);
        subTabCategory.addTab(areaView.getWrapper(), SUB_TAB_CATEGORY_AREA, FontAwesome.LOCATION_ARROW);
        subTabCategory.addTab(materialType1View.getWrapper(), SUB_TAB_CATEGORY_MATERIAL_TYPE_1, FontAwesome.TAG);
        subTabCategory.addTab(materialType2View.getWrapper(), SUB_TAB_CATEGORY_MATERIAL_TYPE_2, FontAwesome.TAG);
        subTabCategory.addTab(unitView.getWrapper(), SUB_TAB_CATEGORY_UNIT, FontAwesome.MAGIC);
        subTabCategory.addTab(orderTypeView.getWrapper(), SUB_TAB_CATEGORY_ORDER_TYPE, FontAwesome.TICKET);
        subTabCategory.addTab(couponTypeView.getWrapper(), SUB_TAB_CATEGORY_COUNPON_TYPE, FontAwesome.FILE_TEXT);

        // add listener to tabsheet
        subTabCategory.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                TabSheet tabsheet = event.getTabSheet();
                Layout tab = (Layout) tabsheet.getSelectedTab();
                String tabCaption = tabsheet.getTab(tab).getCaption();
                System.out.println("Select tab: " + tabCaption);
                switch( tabCaption ){
                case MainMenu.SUB_TAB_CATEGORY_STOCK_TYPE:
                    stockTypeView.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_STOCK:
                    stockView.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_AREA:
                    areaView.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_COUNPON_TYPE:
                    couponTypeView.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_CUSTOMER:
                    customerView.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_CUSTOMER_TYPE_1:
                    customerType1View.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_CUSTOMER_TYPE_2:
                    customerType2View.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_MATERIAL_TYPE_1:
                    materialType1View.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_MATERIAL_TYPE_2:
                    materialType2View.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_UNIT:
                    unitView.initView();
                    break;
                case MainMenu.SUB_TAB_CATEGORY_ORDER_TYPE:
                    orderTypeView.initView();
                    break;
                default:
                    System.out.println("Sub tab sheet is not handled!");
                    break;
                }
            }
        });

        VerticalLayout subTabInformation = new VerticalLayout();
        addTab(subTabInformation, "Trợ Giúp", FontAwesome.INFO);
    }
}
