package com.luvsoft.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Ordertype;

public class OrderTypeModel extends AbstractEntityModel implements Serializable {
    private static final long serialVersionUID = -7887472760238754383L;

    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> entityList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Ordertype entity = (Ordertype) object;
            entity.verifyObject();
            entityList.add(entity);
        }

        return entityList;
    }

    public List<Ordertype> findAll() {
        List<Ordertype> ordertypeList = new ArrayList<Ordertype>();

        List<Object> objectlist = findAll(getEntityname());

        for (Object object : objectlist) {
            Ordertype entity = (Ordertype) object;
            entity.verifyObject();
            ordertypeList.add(entity);
        }
        return ordertypeList;
    }

    public Ordertype findById(int id) {
        return (Ordertype) findById(getEntityname(), id);
    }

    @Override
    public String getEntityname(){
        return Ordertype.getEntityname();
    }

    @Override
    public List<Order> getOrderListByOrderTypeName(String typeName){
        List<Order> entityList = new ArrayList<Order>();
        List<Object> params = new ArrayList<Object>();
        params.add(typeName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Order.getEntityname() + " e WHERE ordertype.name LIKE :var0", params);

        for (Object object : objectlist) {
            Order entity = (Order) object;
            entity.verifyObject();
            entityList.add(entity);
        }
        return entityList;
    }
}
