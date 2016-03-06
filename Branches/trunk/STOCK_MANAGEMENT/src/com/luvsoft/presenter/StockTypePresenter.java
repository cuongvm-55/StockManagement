package com.luvsoft.presenter;

import java.util.HashMap;

import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.StockType.StockTypeView;

public class StockTypePresenter extends AbstractEntityPresenter {
    public StockTypePresenter(StockTypeView view) {
        this.view = view;
        model = new StockTypeModel();
        criteriaMap = new HashMap<String, String>();
        action = ACTION.UNKNOWN;
    }
}
