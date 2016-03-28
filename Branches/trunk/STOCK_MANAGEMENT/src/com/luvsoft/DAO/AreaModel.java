package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Area;
import com.luvsoft.entities.Customer;

public class AreaModel extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> areaList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Area area = (Area) object;
            area.verifyObject();
            areaList.add(area);
        }

        return areaList;
    }

    @Override
    public String getEntityname(){
        return Area.getEntityname();
    }
    
    @Override
    public List<Customer> getCustomerListByAreaName(String areaName){
        List<Customer> customerList = new ArrayList<Customer>();
        List<String> params = new ArrayList<String>();
        params.add(areaName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Customer.getEntityname() + " e WHERE area.name LIKE :var0", params);

        for (Object object : objectlist) {
            Customer customer = (Customer) object;
            customer.verifyObject();
            customerList.add(customer);
        }
        return customerList;
    }
}
