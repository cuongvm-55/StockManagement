package com.luvsoft.stockmanagement;

import java.util.List;

import com.luvsoft.DAO.AreaDAO;
import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.entities.Area;
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

        AreaDAO areaDAO = new AreaDAO();
        
        /*Area area = new Area();
        area.setName("Ba Đình");
        area.setDescription("Quận Ba Đình");
        
        areaDAO.addNew(area);
        */
        
        List<Area> areas = areaDAO.findAll();
        for( Area area : areas ){
            System.out.println(area.toString());
        }
        
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