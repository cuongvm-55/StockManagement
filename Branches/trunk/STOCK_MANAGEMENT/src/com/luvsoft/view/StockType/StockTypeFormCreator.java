package com.luvsoft.view.StockType;

import org.vaadin.viritin.form.AbstractForm.ResetHandler;
import org.vaadin.viritin.form.AbstractForm.SavedHandler;

import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.AbstractEntityPresenter;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.validator.LuvsoftFormBeanValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

public class StockTypeFormCreator {
    private AbstractEntityPresenter presenter;

    @SuppressWarnings("unchecked")
    public void createForm(Stocktype stockType, AbstractEntityPresenter presenter, ACTION action) {
        this.presenter = presenter;
        this.presenter.setAction(action);

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        Stocktype cloneEntity = Stocktype.cloneObject(stockType);

        Button save = new Button("Lưu");
        save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        save.setIcon(FontAwesome.CHECK);
        save.setClickShortcut(KeyCode.ENTER, ModifierKey.ALT);

        Button reset = new Button("Làm Mới");
        reset.setIcon(FontAwesome.REFRESH);
        reset.setClickShortcut(KeyCode.R, ModifierKey.ALT);
        
        // Create form validator, each field will contain a validator
        LuvsoftFormBeanValidator<Stocktype> nameValidator = new LuvsoftFormBeanValidator<Stocktype>(Stocktype.class, "name");
        LuvsoftFormBeanValidator<Stocktype> descriptionValidator = new LuvsoftFormBeanValidator<Stocktype>(Stocktype.class, "description");

        StockTypeForm form = new StockTypeForm();
        form.setResetButton(reset);
        form.setSaveButton(save);

        form.addValidator(nameValidator, form.getNameField());
        form.addValidator(descriptionValidator, form.getDescriptionField());
        form.setEntity(cloneEntity);

        // We will open form by a window
        form.openInModalPopup();
        form.getPopup().setDraggable(false);
        form.getPopup().setResizable(false);
        form.getPopup().setCaptionAsHtml(true);
        if(action.equals(ACTION.CREATE)) {
            form.getPopup().setCaption("<b>Tạo mới</b>");
        }
        if(action.equals(ACTION.UPDATE)) {
            form.getPopup().setCaption("<b>Cập nhật thông tin</b>");
        }

        form.setResetHandler(new ResetHandler<Stocktype>() {
            @Override
            public void onReset(Stocktype entity) {
                cloneEntity.setName(stockType.getName());
                cloneEntity.setDescription(stockType.getDescription());
                form.setEntity(cloneEntity);
            }
        });

        form.setSavedHandler(new SavedHandler<Stocktype>() {
            @Override
            public void onSave(Stocktype entity) {
                if(entity == null) {
                    entity = new Stocktype();
                }
                else {
                    entity.verifyObject();
                }

                presenter.updateEntity(entity);

                // Close window when modification is finished
                if(form.getPopup() != null) {
                    form.getPopup().close();
                }
            }
        });
    }
}
