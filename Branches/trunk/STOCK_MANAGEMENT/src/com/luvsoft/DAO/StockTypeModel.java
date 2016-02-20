package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Stocktype;

public class StockTypeModel {
    EntityManagerDAO entityManager = new EntityManagerDAO();
    public List<Stocktype> getData(int pageIndex, int numberOfRecordPerPage) {
        List<Object> objectlist =  entityManager.findAllWithPagination(Stocktype.getEntityname(), pageIndex, numberOfRecordPerPage);
        List<Stocktype> stockTypeList = new ArrayList<Stocktype>();
        for (Object object : objectlist) {
            Stocktype stocktype = (Stocktype) object;
            stocktype.verifyObject();
            stockTypeList.add(stocktype);
        }
        return stockTypeList;
    }

    public long getCountData() {
        return entityManager.countData(Stocktype.getEntityname());
    }

    public void addNew(Stocktype stocktype) {
        entityManager.addNew(stocktype);
    }

    public void update(Stocktype stocktype) {
        entityManager.update(stocktype);
    }

    public boolean isDuplicatedName(Stocktype stocktype) {
        List<Object> list = entityManager.findEntityByName(Stocktype.getEntityname(), stocktype.getName());
        if(!list.isEmpty()) {
            for (Object object : list) {
                if( ((Stocktype) object).getId() == stocktype.getId() ) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void deleteEntity(Stocktype stocktype) {
        entityManager.remove(Stocktype.getEntityname(), stocktype.getId());
    }
}
