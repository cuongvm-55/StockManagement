package com.luvsoft.DAO;

import java.util.List;

import com.luvsoft.entities.Stocktype;

public class StockTypeModel {
    EntityManagerDAO entityManager = new EntityManagerDAO();
    public List<Object> getData(int pageIndex, int numberOfRecordPerPage) {
        return entityManager.findAllWithPagination(Stocktype.getEntityname(), pageIndex, numberOfRecordPerPage);
    }

    public long getCountData() {
        return entityManager.countData(Stocktype.getEntityname());
    }
}
