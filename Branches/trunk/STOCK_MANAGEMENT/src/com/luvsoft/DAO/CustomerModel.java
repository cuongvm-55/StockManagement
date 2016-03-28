package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;

public class CustomerModel extends AbstractEntityModel {

    public List<Object> getCustomers() {
        return entityManager.findAll(getEntityname());
    }

    @Override
    public String getEntityname() {
        return Customer.getEntityname();
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject) {
        // TODO Auto-generated method stub
        return null;
    }

}
