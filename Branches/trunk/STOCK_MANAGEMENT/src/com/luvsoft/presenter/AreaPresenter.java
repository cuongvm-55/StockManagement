package com.luvsoft.presenter;

import java.util.List;

import com.luvsoft.DAO.AreaModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Area;
import com.luvsoft.entities.Customer;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Customer.AreaView;

public class AreaPresenter extends AbstractEntityPresenter {
    public AreaPresenter(AreaView view) {
        this.view = view;
        model = new AreaModel();
    }
    
    public void updateEntity(AbstractEntity entity) {
        if(action.equals(ACTION.CREATE)) {
            model.addNew(entity);
            goToLastPage();
        }
        else if(action.equals(ACTION.UPDATE)) {
            model.update(entity);
            generateTable();
        } else if(action.equals(ACTION.UPDATE_BY_TABLE_EDITOR)) {
            model.update(entity);
            generateTable();
        }
        this.action = ACTION.UNKNOWN; // Reset action state after modification to avoid duplicate action in the future
    }

    /**
     * Set area of all customer that belong to the to-be-deleted area to null
     */
    @Override
    public void doPreDeleteAction(AbstractEntity entity){
        // Find all customer belong to the area
        Area area = (Area)entity;
        if( area.getName() == null){
            return; // nothing to do
        }
        List<Customer> customerList = model.getCustomerListByAreaName(area.getName());
        if( customerList != null ){
            for( int idx = 0; idx < customerList.size(); idx++ ){
                Customer c = customerList.get(idx);
                c.setArea(null); // set are of these customer to be null
                model.update(c); // update it in db
            }
        }
    }
}
