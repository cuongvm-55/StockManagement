package com.luvsoft.presenter;

import java.util.HashMap;
import java.util.List;

import com.luvsoft.DAO.OrderTypeModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Ordertype;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Order.OrderTypeView;

public class OrderTypePresenter extends AbstractEntityPresenter {
    public OrderTypePresenter(OrderTypeView view) {
        this.view = view;
        model = new OrderTypeModel();
        criteriaMap = new HashMap<String, String>();
        action = ACTION.UNKNOWN;
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
        Ordertype type = (Ordertype)entity;
        if( type.getName() == null){
            return; // nothing to do
        }
        List<Order> list = model.getOrderListByOrderTypeName(type.getName());
        if( list != null ){
            for( int idx = 0; idx < list.size(); idx++ ){
                Order stk = list.get(idx);
                stk.setOrdertype(null);
                model.update(stk); // update it in db
            }
        }
    }
}
