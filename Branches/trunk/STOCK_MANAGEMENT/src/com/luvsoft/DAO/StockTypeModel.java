package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Stocktype;

public class StockTypeModel {
    EntityManagerDAO entityManager = new EntityManagerDAO();

    public long getCountData(FilterObject filterObject) {
        // we do not paging when count number of records
        filterObject.setPageIndex(0);
        filterObject.setNumberOfRecordsPerPage(Integer.MAX_VALUE);
        return entityManager.countData(Stocktype.getEntityname(), filterObject);
    }

    public List<Stocktype> getFilterData(FilterObject filterObject){
        List<Stocktype> stockTypeList = new ArrayList<Stocktype>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(Stocktype.getEntityname(), filterObject);

        for (Object object : objectlist) {
            Stocktype stocktype = (Stocktype) object;
            stocktype.verifyObject();
            stockTypeList.add(stocktype);
        }

        return stockTypeList;
    }

    public void addNew(Stocktype stocktype) {
        entityManager.addNew(stocktype);
    }

    public void update(Stocktype stocktype) {
        entityManager.update(stocktype);
    }

    public void deleteEntity(Stocktype stocktype) {
        entityManager.remove(Stocktype.getEntityname(), stocktype.getId());
    }
}
