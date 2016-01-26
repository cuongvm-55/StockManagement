package com.luvsoft.stockmanagement;

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

        StockTypeView view = new StockTypeView();
        StockTypeModel model = new StockTypeModel();
        StockTypePresenter presenter = new StockTypePresenter(view, model);
        presenter.generateTable();

        layout.addComponent(view);
    }

}