package com.luvsoft.presenter;

import java.util.List;

import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Stock.StockTypeView;

public class StockTypePresenter extends AbstractEntityPresenter {
    public StockTypePresenter(StockTypeView view) {
        this.view = view;
        model = new StockTypeModel();
    }
    
    public void updateEntity(AbstractEntity entity) {
        if(action.equals(ACTION.CREATE)) {
            model.addNew(entity);
            goToLastPage();
        }
        else if(action.equals(ACTION.UPDATE)) {
            model.update(entity);
            generateTable();
        } else if(action.equals(ACTION.UPDATE_BY_TABLE_EDITOR)) {
            model.update(entity);
            generateTable();
        }
        this.action = ACTION.UNKNOWN; // Reset action state after modification to avoid duplicate action in the future
    }

    /**
     * Set stocktype of all stock that belong to the to-be-deleted stocktype to null
     */
    @Override
    public void doPreDeleteAction(AbstractEntity entity){
        // Find all stock belong to the stock type of entity
        Stocktype stocktype = (Stocktype)entity;
        if( stocktype.getName() == null){
            return; // nothing to do
        }
        List<Stock> stockList = model.getStockListByStockTypeName(stocktype.getName());
        if( stockList != null ){
            for( int idx = 0; idx < stockList.size(); idx++ ){
                Stock stk = stockList.get(idx);
                stk.setStocktype(null); // set stocktype of these stock to be null
                model.update(stk); // update it in db
            }
        }
    }
}
