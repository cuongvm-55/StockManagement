package com.luvsoft.view.Material;

import com.luvsoft.entities.Materialtype2;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;

public class MaterialType2FormCreator {
    public void createForm(Materialtype2 entity, UpdateEntityListener presenter, ACTION action) {
        LuvsoftAbstractForm<Materialtype2> form = new LuvsoftAbstractForm<Materialtype2>(presenter, action, entity);
        form.createTextField("Tên", Materialtype2.class, "name");
        form.createTextField("Mô tả", Materialtype2.class, "description");
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(entity.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
