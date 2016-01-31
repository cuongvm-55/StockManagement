package com.luvsoft.stockmanagement;

import com.luvsoft.view.StockType.StockTypeView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MainMenu extends TabSheet {
    private static final long serialVersionUID = 6290323462935186029L;
    // List of views
    private StockTypeView stockTypeView;

    public MainMenu() {
        super();
        init();
    }

    public void initViews() {
        stockTypeView = new StockTypeView();
    }


    public void init() {
        addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
        setSizeFull();

        initViews();

        // Init submenus
        VerticalLayout subTabSystem = new VerticalLayout();
        addTab(subTabSystem, "Hệ Thống", FontAwesome.COGS);

        VerticalLayout subTabFunction = new VerticalLayout();
        addTab(subTabFunction, "Chức Năng", FontAwesome.FOLDER_OPEN);

        VerticalLayout subTabReport = new VerticalLayout();
        addTab(subTabReport, "Báo Cáo", FontAwesome.FILE_EXCEL_O);

        TabSheet subTabCategory = new TabSheet();
        subTabCategory.addStyleName(ValoTheme.TABSHEET_FRAMED);
        subTabCategory.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        subTabCategory.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        subTabCategory.addTab(stockTypeView.getWrapper(), "Loại Kho", FontAwesome.TRUCK);
        addTab(subTabCategory, "Danh Mục", FontAwesome.BOOK);

        VerticalLayout subTabInformation = new VerticalLayout();
        addTab(subTabInformation, "Trợ Giúp", FontAwesome.INFO);
    }
}
