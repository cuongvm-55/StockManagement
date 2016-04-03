package com.luvsoft.view.Order;

import com.luvsoft.entities.Ordertype;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;

public class OrderTypeFormCreator {
    public void createForm(Ordertype entity, UpdateEntityListener presenter, ACTION action) {
        LuvsoftAbstractForm<Ordertype> form = new LuvsoftAbstractForm<Ordertype>(presenter, action, entity);
        form.createTextField("Tên", Ordertype.class, "name");
        form.createTextField("Mô Tả", Ordertype.class, "description");
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(entity.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
