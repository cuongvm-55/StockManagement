package com.luvsoft.view.StockType;

import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.luvsoft.entities.Stocktype;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class StockTypeForm extends AbstractForm<Stocktype>{
    private static final long serialVersionUID = 5744719222757703565L;
    private TextField name = new TextField("Tên");
    private TextArea description = new TextArea("Mô tả");

    public StockTypeForm() {
        setEagerValidation(true);
    }

    @Override
    protected Component createContent() {
        this.setSizeUndefined();
        name.setWidth("100%");
        description.setWidth("100%");

        return new MVerticalLayout(
                        name,
                        description,
                        getToolbar()
        );
    }

    public TextField getNameField() {
        return name;
    }

    public void setNameField(TextField name) {
        this.name = name;
    }

    public TextArea getDescriptionField() {
        return description;
    }

    public void setDescriptionField(TextArea description) {
        this.description = description;
    }

}
