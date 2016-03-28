package com.luvsoft.view.Customer;

import com.luvsoft.entities.Area;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;

public class AreaFormCreator {
    public void createForm(Area area, UpdateEntityListener presenter, ACTION action) {
        LuvsoftAbstractForm<Area> form = new LuvsoftAbstractForm<Area>(presenter, action, area);
        form.createTextField("Tên", Area.class, "name");
        form.createTextField("Mô tả", Area.class, "description");
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(area.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
