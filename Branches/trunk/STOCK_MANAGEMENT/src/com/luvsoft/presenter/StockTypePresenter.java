package com.luvsoft.presenter;

import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.view.StockType.StockTypeView;

public class StockTypePresenter implements StockTypeListener {
    private StockTypeView view;
    private StockTypeModel model;

    public StockTypePresenter(StockTypeView view) {
        this.view = view;
        model = new StockTypeModel();
    }

    @Override
    public void generateTable() {
        view.setTable(model.getData());
    }

    /**
     * Refresh view
     */
    public void refreshView(){
        generateTable();
    }

}
