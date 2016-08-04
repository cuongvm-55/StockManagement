package com.luvsoft.stockmanagement;

import com.luvsoft.report.producer.GrossRevenueProducer.GrossMode;
import com.luvsoft.report.view.CustomerDebtReportView;
import com.luvsoft.report.view.GrossRevenueView;
import com.luvsoft.report.view.InOutInventoryReportView;
import com.luvsoft.view.Coupon.CouponTypeView;
import com.luvsoft.view.Customer.AreaView;
import com.luvsoft.view.Customer.CustomerType1View;
import com.luvsoft.view.Customer.CustomerType2View;
import com.luvsoft.view.Customer.CustomerView;
import com.luvsoft.view.Material.MaterialType1View;
import com.luvsoft.view.Material.MaterialType2View;
import com.luvsoft.view.Material.MaterialView;
import com.luvsoft.view.Material.UnitView;
import com.luvsoft.view.Order.OrderTypeView;
import com.luvsoft.view.Stock.StockTypeView;
import com.luvsoft.view.Stock.StockView;
import com.luvsoft.view.component.GenericTabFunction_Buy;
import com.luvsoft.view.component.GenericTabFunction_Shell;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MainMenu extends TabSheet implements SelectedTabChangeListener{
    private static final long serialVersionUID = 6290323462935186029L;
    // Reports
    private static final String SUB_TAB_REPORT_IN_OUT_INVENTORY = "Xuất-Nhập-Tồn";
    private static final String SUB_TAB_REPORT_CUSTOMER_DEBT    = "Công Nợ Khách Hàng";
    private static final String SUB_TAB_REPORT_GROSS_REVENEU    = "Doanh Thu";
    private static final String SUB_TAB_REPORT_GROSS_INPUT      = "Hàng Nhập";

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
    private static final String SUB_TAB_CATEGORY_MATERIAL = "Vật Tư";
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
    private MaterialView materialView;
    private MaterialType1View materialType1View;
    private MaterialType2View materialType2View;
    private OrderTypeView orderTypeView;
    private UnitView unitView;

    // reports
    private InOutInventoryReportView inputOutputInventoryReport;
    private CustomerDebtReportView   customerDebtReport;
    private GrossRevenueView         grossRevenueReport;
    private GrossRevenueView         grossInputReport;

    public MainMenu() {
        super();
        init();
    }

    public void initViews() {
        stockTypeView = new StockTypeView();
        stockView = new StockView();
        areaView = new AreaView();
        couponTypeView = new CouponTypeView();
        customerView = new CustomerView();
        customerType1View = new CustomerType1View();
        customerType2View = new CustomerType2View();
        materialView = new MaterialView();
        materialType1View = new MaterialType1View();
        materialType2View = new MaterialType2View();
        orderTypeView = new OrderTypeView();
        unitView = new UnitView();

        inputOutputInventoryReport = new InOutInventoryReportView();
        customerDebtReport = new CustomerDebtReportView();
        grossRevenueReport = new GrossRevenueView(GrossMode.GrossRevenue);
        grossInputReport = new GrossRevenueView(GrossMode.GrossInput);
    }

    public void init() {
        addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
        setSizeFull();

        initViews();

        // Init submenus
        VerticalLayout subTabSystem = new VerticalLayout();
        addTab(subTabSystem, "Hệ Thống", FontAwesome.COGS);

        //////////////////////////////////////////////////////////////////////////////////
        // Functions: Shell, Buy
        //////////////////////////////////////////////////////////////////////////////////
        GenericTabFunction_Shell subTabFunction_Shell = new GenericTabFunction_Shell();
        subTabFunction_Shell.init();
        TabSheet subTabFunction = new TabSheet();
        subTabFunction.addStyleName(ValoTheme.TABSHEET_FRAMED);
        subTabFunction.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        subTabFunction.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        subTabFunction.setSizeFull();
        subTabFunction.addTab(subTabFunction_Shell.getWapper(), "Bán Hàng", FontAwesome.DOLLAR);

        GenericTabFunction_Buy subTabFunction_Buy = new GenericTabFunction_Buy();
        subTabFunction_Buy.init();

        subTabFunction.addTab(subTabFunction_Buy.getWapper(), "Mua Hàng", FontAwesome.CART_PLUS);

        addTab(subTabFunction, "Chức Năng", FontAwesome.FOLDER_OPEN);
        //////////////////////////////////////////////////////////////////////////////////

        TabSheet subTabReport = new TabSheet();
        subTabReport.addStyleName(ValoTheme.TABSHEET_FRAMED);
        subTabReport.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        subTabReport.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        subTabReport.setSizeFull();
        addTab(subTabReport, "Báo Cáo", FontAwesome.FILE_EXCEL_O);
        subTabReport.addTab(inputOutputInventoryReport.getWrapper(), SUB_TAB_REPORT_IN_OUT_INVENTORY, FontAwesome.PRODUCT_HUNT);
        subTabReport.addTab(customerDebtReport.getWrapper(), SUB_TAB_REPORT_CUSTOMER_DEBT, FontAwesome.MONEY);
        subTabReport.addTab(grossRevenueReport.getWrapper(), SUB_TAB_REPORT_GROSS_REVENEU, FontAwesome.MONEY);
        subTabReport.addTab(grossInputReport.getWrapper(), SUB_TAB_REPORT_GROSS_INPUT, FontAwesome.MONEY);

        TabSheet subTabCategory = new TabSheet();
        subTabCategory.addStyleName(ValoTheme.TABSHEET_FRAMED);
        subTabCategory.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        subTabCategory.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        subTabCategory.setSizeFull();
        addTab(subTabCategory, "Danh Mục", FontAwesome.BOOK);

        // sub tabs for order
        TabSheet subTabCategoryGroup_Order = new TabSheet();
        subTabCategory.addTab(subTabCategoryGroup_Order, "Hóa Đơn", FontAwesome.NEWSPAPER_O);
        subTabCategoryGroup_Order.addTab(orderTypeView.getWrapper(), SUB_TAB_CATEGORY_ORDER_TYPE, FontAwesome.TICKET);
        // First tab should be init because the framework select the first tab automatically
        orderTypeView.initView();

        // sub tabs for coupon
        TabSheet subTabCategoryGroup_Coupon = new TabSheet();
        subTabCategory.addTab(subTabCategoryGroup_Coupon, "Phiếu", FontAwesome.NEWSPAPER_O);
        subTabCategoryGroup_Coupon.addTab(couponTypeView.getWrapper(), SUB_TAB_CATEGORY_COUNPON_TYPE, FontAwesome.FILE_TEXT);
        couponTypeView.initView();

        // sub tabs for material
        TabSheet subTabCategoryGroup_Material = new TabSheet();
        subTabCategory.addTab(subTabCategoryGroup_Material, "Vật Tư", FontAwesome.TAGS);
        subTabCategoryGroup_Material.addTab(materialView.getWrapper(), SUB_TAB_CATEGORY_MATERIAL, FontAwesome.TAG);
        materialView.initView();
        subTabCategoryGroup_Material.addTab(materialType1View.getWrapper(), SUB_TAB_CATEGORY_MATERIAL_TYPE_1, FontAwesome.TAG);
        subTabCategoryGroup_Material.addTab(materialType2View.getWrapper(), SUB_TAB_CATEGORY_MATERIAL_TYPE_2, FontAwesome.TAG);
        subTabCategoryGroup_Material.addTab(unitView.getWrapper(), SUB_TAB_CATEGORY_UNIT, FontAwesome.MAGIC);
        subTabCategoryGroup_Material.addTab(stockTypeView.getWrapper(), SUB_TAB_CATEGORY_STOCK_TYPE, FontAwesome.TRUCK);
        subTabCategoryGroup_Material.addTab(stockView.getWrapper(), SUB_TAB_CATEGORY_STOCK, FontAwesome.CAR);

        // sub tabs for customer
        TabSheet subTabCategoryGroup_Customer = new TabSheet();
        subTabCategory.addTab(subTabCategoryGroup_Customer, "Khách Hàng", FontAwesome.GROUP);
        subTabCategoryGroup_Customer.addTab(customerView.getWrapper(), SUB_TAB_CATEGORY_CUSTOMER, FontAwesome.MALE);
        customerView.initView();
        subTabCategoryGroup_Customer.addTab(customerType1View.getWrapper(), SUB_TAB_CATEGORY_CUSTOMER_TYPE_1, FontAwesome.MALE);
        subTabCategoryGroup_Customer.addTab(customerType2View.getWrapper(), SUB_TAB_CATEGORY_CUSTOMER_TYPE_2, FontAwesome.FEMALE);
        subTabCategoryGroup_Customer.addTab(areaView.getWrapper(), SUB_TAB_CATEGORY_AREA, FontAwesome.LOCATION_ARROW);

        // add listener to tabsheet
        subTabReport.addSelectedTabChangeListener(this);
        subTabCategoryGroup_Order.addSelectedTabChangeListener(this);
        subTabCategoryGroup_Coupon.addSelectedTabChangeListener(this);
        subTabCategoryGroup_Material.addSelectedTabChangeListener(this);
        subTabCategoryGroup_Customer.addSelectedTabChangeListener(this);
        subTabCategory.addSelectedTabChangeListener(this);

        VerticalLayout subTabInformation = new VerticalLayout();
        addTab(subTabInformation, "Trợ Giúp", FontAwesome.INFO);
    }

    @Override
    public void selectedTabChange(SelectedTabChangeEvent event) {
        // We add the try/catch because we just want to handle the child tab
        // we ignore all the parent tabs
        try{
            // Get the tabsheet that emitted the selected event
            TabSheet tabsheet = event.getTabSheet();
            Layout tab = (Layout) tabsheet.getSelectedTab();
            String tabCaption = tabsheet.getTab(tab).getCaption();
            System.out.println("tabCaption: " + tabCaption);
            handleTabChangeEvent(tabCaption);
        }catch(Exception e){
        }
    }

    private void handleTabChangeEvent(String tabCaption){
        switch( tabCaption ){
        case MainMenu.SUB_TAB_REPORT_IN_OUT_INVENTORY:
        case MainMenu.SUB_TAB_REPORT_CUSTOMER_DEBT:
        case MainMenu.SUB_TAB_REPORT_GROSS_REVENEU:
            System.out.println("Report!");
            break;
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
        case MainMenu.SUB_TAB_CATEGORY_MATERIAL:
            materialView.initView();
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
}
