package com.luvsoft.DAO;

import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;

public abstract class AbstractEntityModel {
    protected EntityManagerDAO entityManager = new EntityManagerDAO();

    public long getCountData(FilterObject filterObject) {
        // we do not paging when count number of records
        filterObject.setPageIndex(0);
        filterObject.setNumberOfRecordsPerPage(Integer.MAX_VALUE);
        return entityManager.countData(getEntityname(), filterObject);
    }
    
    public void addNew(AbstractEntity entity) {
        entityManager.addNew(entity);
    }

    public void update(AbstractEntity entity) {
        entityManager.update(entity);
    }

    public void deleteEntity(AbstractEntity entity) {
        entityManager.remove(getEntityname(), entity.getId());
    }

    public List<Stock> getStockListByStockTypeName(String stockTypeName){return null;}
    public List<Stocktype> getStockTypeList(){return null;}
    public List<Customer> getCustomerListByAreaName(String areaName){return null;}
    
    public AbstractEntity getEntityByName(String entityName, String name){
        Object obj = entityManager.findByName(entityName, name);
        return (AbstractEntity)obj;
    }

    // abstract functions
    public abstract String getEntityname();
    public abstract List<AbstractEntity> getFilterData(FilterObject filterObject);
}
