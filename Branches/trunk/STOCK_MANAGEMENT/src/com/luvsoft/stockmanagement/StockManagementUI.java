package com.luvsoft.stockmanagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.model.StockTypeModel;
import com.luvsoft.presenter.StockTypePresenter;
import com.luvsoft.view.dummy.StockTypeView;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("stockmanagement")
public class StockManagementUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        EntityManagerFactory emfactory = Persistence.
                createEntityManagerFactory( "STOCK_MANAGEMENT" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        
        Stocktype stkType = new Stocktype();
        stkType.setName("Hàng lỗi");
        stkType.setDescription("Loại kho chứa các vật tư lỗi");
        
        entitymanager.persist( stkType );
        entitymanager.getTransaction( ).commit( );
        
        Stock stock = new Stock();
        stock.setName("Kho 1");
        stock.setCode("KHO1");
        stock.setDescription("Kho hàng bán");
        stock.setStocktype(stkType);

        //entitymanager.persist( stock );
        //entitymanager.getTransaction( ).commit( );
        
        entitymanager.close( );
        emfactory.close( );
        
        StockTypeView view = new StockTypeView();
        StockTypeModel model = new StockTypeModel();
        StockTypePresenter presenter = new StockTypePresenter(view, model);
        presenter.generateTable();

        layout.addComponents(buildMenu(), view);
    }

    private MenuBar buildMenu() {
        MenuBar menu = new MenuBar();
        MenuBar.Command command = new MenuBar.Command() {
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                if(selectedItem.getText().equals("Chức Năng")) {
                    
                }
            }
        };

        MenuItem sysItem = menu.addItem("Hệ Thống", FontAwesome.COGS, null);
        sysItem.addItem("Đăng Nhập", FontAwesome.USER, command);
        sysItem.addItem("Đăng Xuất", FontAwesome.KEY, command);

        final MenuItem funcItem = menu.addItem("Chức Năng", FontAwesome.FOLDER_OPEN, command);
        
        menu.addItem("Báo Cáo", FontAwesome.FILE_EXCEL_O, command);
        menu.addItem("Danh Mục", FontAwesome.BOOK, command);
        menu.addItem("Trợ Giúp", FontAwesome.INFO, command);

        // Submenu
        MenuBar subMenu = new MenuBar();
        subMenu.addItem("Bán Hàng", FontAwesome.GLOBE,null);

        return menu;
    }
}