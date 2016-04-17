package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype1;

public class CustomerType1Model extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> entityList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Customertype1 entity = (Customertype1) object;
            entity.verifyObject();
            entityList.add(entity);
        }

        return entityList;
    }

    @Override
    public String getEntityname(){
        return Customertype1.getEntityname();
    }
    
    @Override
    public List<Customer> getCustomerListByCustomerType1Name(String couponTypeName){
        List<Customer> entityList = new ArrayList<Customer>();
        List<Object> params = new ArrayList<Object>();
        params.add(couponTypeName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Customer.getEntityname() + " e WHERE customertype1.name LIKE :var0", params);

        for (Object object : objectlist) {
            Customer entity = (Customer) object;
            entity.verifyObject();
            entityList.add(entity);
        }
        return entityList;
    }
}
