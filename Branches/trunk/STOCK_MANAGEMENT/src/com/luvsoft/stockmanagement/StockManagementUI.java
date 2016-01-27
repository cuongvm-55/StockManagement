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
import com.vaadin.server.VaadinRequest;
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

        layout.addComponent(view);
    }

}