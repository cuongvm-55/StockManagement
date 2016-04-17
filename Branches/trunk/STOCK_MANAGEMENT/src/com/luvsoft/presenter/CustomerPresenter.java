package com.luvsoft.presenter;

import java.util.List;

import com.luvsoft.DAO.CustomerModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Area;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype1;
import com.luvsoft.entities.Customertype2;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Customer.CustomerView;

public class CustomerPresenter extends AbstractEntityPresenter {
    public CustomerPresenter(CustomerView view) {
        this.view = view;
        model = new CustomerModel();
    }

    public void updateEntity(AbstractEntity entity) {
        Customer customer = (Customer)entity;
        customer.setArea((Area)model.getEntityByName(Area.getEntityname(), customer.getFrk_area_name()));
        System.out.println(customer.getArea());
        customer.setCustomertype1((Customertype1)model.getEntityByName(Customertype1.getEntityname(), customer.getFrk_customertype1_name()));
        customer.setCustomertype2((Customertype2)model.getEntityByName(Customertype2.getEntityname(), customer.getFrk_customertype2_name()));
        if(action.equals(ACTION.CREATE)) {
            model.addNew(customer);
            goToLastPage();
        }
        else if(action.equals(ACTION.UPDATE)) {
            model.update(customer);
            generateTable();
        } else if(action.equals(ACTION.UPDATE_BY_TABLE_EDITOR)) {
            model.update(customer);
            generateTable();
        }
        this.action = ACTION.UNKNOWN; // Reset action state after modification to avoid duplicate action in the future
    }

    @Override
    public List<Area> getAreaList(){
        return model.getAreaList();
    }

    @Override
    public List<Customertype1> getCustomerType1List(){
        return model.getCustomerType1List();
    }

    @Override
    public List<Customertype2> getCustomerType2List(){
        return model.getCustomerType2List();
    }

}