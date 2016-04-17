package com.luvsoft.presenter;

import java.util.List;

import com.luvsoft.DAO.StockModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Stock.StockView;

public class StockPresenter extends AbstractEntityPresenter {
    public StockPresenter(StockView view) {
        this.view = view;
        model = new StockModel();
    }

    public void updateEntity(AbstractEntity entity) {
        Stock stock = (Stock)entity;
        stock.setStocktype((Stocktype)model.getEntityByName(Stocktype.getEntityname(), stock.getFrk_stocktype_name()));
        if(action.equals(ACTION.CREATE)) {
            model.addNew(stock);
            goToLastPage();
        }
        else if(action.equals(ACTION.UPDATE)) {
            model.update(stock);
            generateTable();
        } else if(action.equals(ACTION.UPDATE_BY_TABLE_EDITOR)) {
            model.update(stock);
            generateTable();
        }
        this.action = ACTION.UNKNOWN; // Reset action state after modification to avoid duplicate action in the future
    }

    public List<Stocktype> getStockTypeList(){
        return model.getStockTypeList();
    }
}