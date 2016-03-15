package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;

public class StockModel extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> stockList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Stock stock = (Stock) object;
            stock.verifyObject();
            stockList.add(stock);
        }
        return stockList;
    }

    @Override
    public String getEntityname(){
        return Stock.getEntityname();
    }

    public List<Stocktype> getStockTypeList(){
        List<Stocktype> stockTypeList = new ArrayList<Stocktype>();
        List<Object> objectlist = entityManager.findAll(Stocktype.getEntityname());

        for (Object object : objectlist) {
            Stocktype stocktype = (Stocktype) object;
            stocktype.verifyObject();
            stockTypeList.add(stocktype);
        }

        return stockTypeList;
    }
}
