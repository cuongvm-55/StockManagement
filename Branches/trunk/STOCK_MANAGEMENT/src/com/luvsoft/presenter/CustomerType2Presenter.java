package com.luvsoft.presenter;

import java.util.List;

import com.luvsoft.DAO.CustomerType2Model;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype2;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Customer.CustomerType2View;

public class CustomerType2Presenter extends AbstractEntityPresenter {
    public CustomerType2Presenter(CustomerType2View view) {
        this.view = view;
        model = new CustomerType2Model();
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
     * Set type of all entities that belong to the to-be-deleted type to null
     */
    @Override
    public void doPreDeleteAction(AbstractEntity entity){
        // Find all stock belong to the stock type of entity
        Customertype2 type = (Customertype2)entity;
        if( type.getName() == null){
            return; // nothing to do
        }
        List<Customer> list = model.getCustomerListByCustomertype2Name(type.getName());
        if( list != null ){
            for( int idx = 0; idx < list.size(); idx++ ){
                Customer stk = list.get(idx);
                stk.setCustomertype1(null);
                model.update(stk); // update it in db
            }
        }
    }
}
