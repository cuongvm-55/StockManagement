package com.luvsoft.DAO;

import java.util.List;

import javax.persistence.NoResultException;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Order;

public class OrderModel extends AbstractEntityModel {

    public Order findLastItem() {
        try {
            Order order = (Order) findLastItem(getEntityname());
            if(order == null) {
                order = new Order();
            } else {
                order.verifyObject();
            }
            return order;
        } catch (NoResultException e) {
            return new Order();
        }
    }

    @Override
    public String getEntityname() {
        return Order.getEntityname();
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject) {
        // TODO Auto-generated method stub
        return null;
    }

}
