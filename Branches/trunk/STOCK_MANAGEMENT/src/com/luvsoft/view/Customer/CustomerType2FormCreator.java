package com.luvsoft.view.Customer;

import com.luvsoft.entities.Customertype2;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;

public class CustomerType2FormCreator {
    public void createForm(Customertype2 entity, UpdateEntityListener presenter, ACTION action) {
        LuvsoftAbstractForm<Customertype2> form = new LuvsoftAbstractForm<Customertype2>(presenter, action, entity);
        form.createTextField("Tên", Customertype2.class, "name");
        form.createTextField("Mô tả", Customertype2.class, "description");
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(entity.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
