package com.luvsoft.view.Stock;

import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;

public class StockTypeFormCreator {
    public void createForm(Stocktype stockType, UpdateEntityListener presenter, ACTION action) {
        LuvsoftAbstractForm<Stocktype> form = new LuvsoftAbstractForm<Stocktype>(presenter, action, stockType);
        form.createTextField("Tên", Stocktype.class, "name");
        form.createTextField("Mô tả", Stocktype.class, "description");
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(stockType.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
