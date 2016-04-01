package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Stock;

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
        List<AbstractEntity> customerList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Customer customer = (Customer) object;
            customer.verifyObject();
            customerList.add(customer);
        }
        return customerList;
    }

}
