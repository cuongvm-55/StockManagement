package com.luvsoft.view.Coupon;

import com.luvsoft.entities.Coupontype;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;

public class CouponTypeFormCreator {
    public void createForm(Coupontype type, UpdateEntityListener presenter, ACTION action) {
        LuvsoftAbstractForm<Coupontype> form = new LuvsoftAbstractForm<Coupontype>(presenter, action, type);
        form.createTextField("Tên", Coupontype.class, "name");
        form.createTextField("Mô tả", Coupontype.class, "description");
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(type.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
