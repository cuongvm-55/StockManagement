package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Area;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype1;
import com.luvsoft.entities.Customertype2;

public class CustomerModel extends AbstractEntityModel {

    public List<Object> getCustomers() {
        return entityManager.findAll(getEntityname());
    }

    @Override
    public String getEntityname() {
        return Customer.getEntityname();
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> entityList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Customer entity = (Customer) object;
            entity.verifyObject();
            entityList.add(entity);
        }
        return entityList;
    }

    @Override
    public List<Area> getAreaList(){
        List<Area> list = new ArrayList<Area>();
        List<Object> objectlist = entityManager.findAll(Area.getEntityname());

        for (Object object : objectlist) {
            Area entity = (Area) object;
            entity.verifyObject();
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<Customertype1> getCustomerType1List(){
        List<Customertype1> list = new ArrayList<Customertype1>();
        List<Object> objectlist = entityManager.findAll(Customertype1.getEntityname());

        for (Object object : objectlist) {
            Customertype1 entity = (Customertype1) object;
            entity.verifyObject();
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<Customertype2> getCustomerType2List(){
        List<Customertype2> list = new ArrayList<Customertype2>();
        List<Object> objectlist = entityManager.findAll(Customertype2.getEntityname());

        for (Object object : objectlist) {
            Customertype2 entity = (Customertype2) object;
            entity.verifyObject();
            list.add(entity);
        }
        return list;
    }
    
}
