package com.luvsoft.view.Material;

import com.luvsoft.entities.Unit;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;

public class UnitFormCreator {
    public void createForm(Unit unit, UpdateEntityListener presenter, ACTION action) {
        LuvsoftAbstractForm<Unit> form = new LuvsoftAbstractForm<Unit>(presenter, action, unit);
        form.createTextField("Tên", Unit.class, "name");
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(unit.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
