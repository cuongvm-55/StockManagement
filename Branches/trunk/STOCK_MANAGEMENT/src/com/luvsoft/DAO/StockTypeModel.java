package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;

public class StockTypeModel extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> stockTypeList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Stocktype stocktype = (Stocktype) object;
            stocktype.verifyObject();
            stockTypeList.add(stocktype);
        }

        return stockTypeList;
    }

    @Override
    public String getEntityname(){
        return Stocktype.getEntityname();
    }
    
    @Override
    public List<Stock> getStockListByStockTypeName(String stockTypeName){
        List<Stock> stockList = new ArrayList<Stock>();
        List<Object> params = new ArrayList<Object>();
        params.add(stockTypeName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Stock.getEntityname() + " e WHERE stocktype.name LIKE :var0", params);

        for (Object object : objectlist) {
            Stock stock = (Stock) object;
            stock.verifyObject();
            stockList.add(stock);
        }
        return stockList;
    }
}
