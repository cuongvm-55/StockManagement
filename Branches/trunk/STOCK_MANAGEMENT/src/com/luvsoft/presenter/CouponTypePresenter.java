package com.luvsoft.presenter;

import java.util.List;

import com.luvsoft.DAO.CouponTypeModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Coupontype;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Coupon.CouponTypeView;

public class CouponTypePresenter extends AbstractEntityPresenter {
    public CouponTypePresenter(CouponTypeView view) {
        this.view = view;
        model = new CouponTypeModel();
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
     * Set coupontype of all coupon that belong to the to-be-deleted coupontype to null
     */
    @Override
    public void doPreDeleteAction(AbstractEntity entity){
        // Find all stock belong to the stock type of entity
        Coupontype type = (Coupontype)entity;
        if( type.getName() == null){
            return; // nothing to do
        }
        List<Coupon> list = model.getCouponListByCouponTypeName(type.getName());
        if( list != null ){
            for( int idx = 0; idx < list.size(); idx++ ){
                Coupon stk = list.get(idx);
                stk.setCoupontype(null); // set stocktype of these stock to be null
                model.update(stk); // update it in db
            }
        }
    }
}
