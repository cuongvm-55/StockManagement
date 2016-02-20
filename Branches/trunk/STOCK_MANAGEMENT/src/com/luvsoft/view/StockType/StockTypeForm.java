package com.luvsoft.view.StockType;

import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.StockTypeListener;
import com.luvsoft.utils.ACTION;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class StockTypeForm extends MWindow {
    private static final long serialVersionUID = 2032052653696784763L;
    private PropertysetItem stocktypeItem;
    private TextField txtName;
    private TextArea txtDescription;

    @SuppressWarnings("serial")
    public StockTypeForm(Stocktype stockType, StockTypeListener presenter, ACTION action) {
        withClosable(true).withDraggable(false).withModal(true).withResizable(false);
        presenter.setAction(action);

        stocktypeItem = new PropertysetItem();
        stocktypeItem.addItemProperty("name", new ObjectProperty<String>(stockType.getName()));
        stocktypeItem.addItemProperty("description", new ObjectProperty<String>(stockType.getDescription()));

        txtName = new TextField("Tên");
        txtName.setRequired(true);
        txtName.focus();
        txtName.addValidator(new BeanValidator(Stocktype.class, "name"));

        txtDescription = new TextArea("Mô Tả");
        txtDescription.setRequired(false);
        txtDescription.addValidator(new BeanValidator(Stocktype.class, "description"));

        // Set default value for text fields if action is update
        if(stockType != null && stockType.getId() != -1 && action.equals(ACTION.UPDATE)) {
            txtName.setValue(stockType.getName());
            txtDescription.setValue(stockType.getDescription());
        }

        FieldGroup fieldGroup = new FieldGroup(stocktypeItem);
        fieldGroup.bind(txtName, "name");
        fieldGroup.bind(txtDescription, "description");
        fieldGroup.setBuffered(true);
        fieldGroup.addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                fieldGroup.isValid();
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
                presenter.updateEntity(stockType);
                close();
            }
        });

        Button save = new Button("Lưu");
        save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        save.setIcon(FontAwesome.CHECK);
        save.setClickShortcut(KeyCode.ENTER);
        save.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    stockType.setName(txtName.getValue());
                    stockType.setDescription(txtDescription.getValue());

                    // Validate form by presenter: isEmpty, isDuplicated
                    if (!presenter.validateForm(stockType)) {
                        return;
                    }

                    fieldGroup.commit();
                } catch (CommitException e) {
                    e.printStackTrace();
                }
            }
        });

        VerticalLayout wrapper = new MVerticalLayout()
                                    .withSpacing(true)
                                    .withCaption("Thêm Mới")
                                    .with(txtName, txtDescription, save);
        wrapper.setComponentAlignment(save, Alignment.MIDDLE_RIGHT);

        this.setContent(wrapper);
    }
}
