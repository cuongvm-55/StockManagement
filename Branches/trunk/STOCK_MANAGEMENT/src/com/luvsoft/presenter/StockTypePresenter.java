package com.luvsoft.presenter;

import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.view.dummy.StockTypeView;

public class StockTypePresenter implements StockTypeListener {
    private StockTypeView view;
    private StockTypeModel model;

    
    public StockTypePresenter(StockTypeView view, StockTypeModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void generateTable() {
        view.setTable(model.getData());
    }

}
