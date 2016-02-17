package com.luvsoft.DAO;

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

    public List<Object> getFilterData(FilterObject filterObject){
        return entityManager.searchWithCriteriaWithPagination(Stocktype.getEntityname(), filterObject);
    }
}
