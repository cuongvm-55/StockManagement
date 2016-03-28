package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype2;

public class CustomerType2Model extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> entityList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Customertype2 entity = (Customertype2) object;
            entity.verifyObject();
            entityList.add(entity);
        }

        return entityList;
    }

    @Override
    public String getEntityname(){
        return Customertype2.getEntityname();
    }
    
    @Override
    public List<Customer> getCustomerListByCustomertype2Name(String couponTypeName){
        List<Customer> entityList = new ArrayList<Customer>();
        List<String> params = new ArrayList<String>();
        params.add(couponTypeName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Customer.getEntityname() + " e WHERE customertype2.name LIKE :var0", params);

        for (Object object : objectlist) {
            Customer entity = (Customer) object;
            entity.verifyObject();
            entityList.add(entity);
        }
        return entityList;
    }
}
