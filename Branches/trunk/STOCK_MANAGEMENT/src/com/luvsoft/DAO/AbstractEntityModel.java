package com.luvsoft.DAO;

import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Area;
import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype1;
import com.luvsoft.entities.Customertype2;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.entities.Unit;

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

    public void refreshEntity(AbstractEntity entity, Class<?> classtype, int id) {
        entityManager.refreshEntity(entity, classtype, id);
    }

    public AbstractEntity getEntityByName(String entityName, String name){
        Object obj = entityManager.findByName(entityName, name);
        return (AbstractEntity)obj;
    }

    public List<Object> findAll(String entityName) {
        return entityManager.findAll(entityName);
    }

    public Object findById(String entityName, int id) {
        return entityManager.findById(entityName, id);
    }

    public Object findByName(String entityName, String name) {
        return entityManager.findByName(entityName, name);
    }

    public Object findLastItem(String entityName) {
        return entityManager.findLastItem(entityName);
    }

    // override functions
    public List<Stock> getStockListByStockTypeName(String stockTypeName){return null;}
    public List<Stocktype> getStockTypeList(){return null;}

    public List<Customer> getCustomerListByAreaName(String areaName){return null;}

    public List<Coupon> getCouponListByCouponTypeName(String typeName){return null;}

    public List<Customer> getCustomerListByCustomerType1Name(String typeName){return null;}
    public List<Customer> getCustomerListByCustomertype2Name(String typeName){return null;}

    public List<Material> getMaterialListByMaterialType1Name(String typeName){return null;}
    public List<Material> getMaterialListByMaterialType2Name(String typeName){return null;}
    public List<Material> getMaterialListByUnitName(String unitName){return null;}

    public List<Order> getOrderListByOrderTypeName(String typeName){return null;}

    public List<Area> getAreaList(){return null;}
    public List<Customertype1> getCustomerType1List(){return null;}
    public List<Customertype2> getCustomerType2List(){return null;}

    public List<Stock> getStockList(){return null;}
    public List<Unit> getUnitList(){return null;}
    public List<Materialtype1> getMaterialType1List(){return null;}
    public List<Materialtype2> getMaterialType2List(){return null;}

    // abstract functions
    public abstract String getEntityname();
    public abstract List<AbstractEntity> getFilterData(FilterObject filterObject);
}
