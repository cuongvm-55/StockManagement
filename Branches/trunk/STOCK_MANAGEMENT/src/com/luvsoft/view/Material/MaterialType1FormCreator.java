package com.luvsoft.view.Material;

import com.luvsoft.entities.Materialtype1;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;

public class MaterialType1FormCreator {
    public void createForm(Materialtype1 entity, UpdateEntityListener presenter, ACTION action) {
        LuvsoftAbstractForm<Materialtype1> form = new LuvsoftAbstractForm<Materialtype1>(presenter, action, entity);
        form.createTextField("Tên", Materialtype1.class, "name");
        form.createTextField("Mô tả", Materialtype1.class, "description");
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(entity.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
