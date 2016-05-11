package com.luvsoft.view.Material;

import java.util.Collection;

import com.luvsoft.Excel.UnitExporter;
import com.luvsoft.Excel.UnitImporter;
import com.luvsoft.entities.Unit;
import com.luvsoft.presenter.UnitPresenter;
import com.luvsoft.utils.ACTION;
import com.luvsoft.utils.ErrorManager;
import com.luvsoft.utils.ErrorManager.ErrorId;
import com.luvsoft.view.component.FileChooser;
import com.luvsoft.view.component.FileImportHelper;
import com.luvsoft.view.component.GenericTabCategory;
import com.luvsoft.view.component.LuvsoftConfirmationDialog;
import com.luvsoft.view.validator.LuvsoftTableBeanValidator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;


public class UnitView extends GenericTabCategory<Unit> {
    private static final long serialVersionUID = -7975276654447059817L;

    @SuppressWarnings("serial")
    public UnitView() {
        presenter = new UnitPresenter(this);
        super.init("Danh Sách Đơn Vị Tính", Unit.class)
        .withGeneralFuntionsList()
        .withTableProperties("name")
        .withHeaderNames("name", "<b>Tên</b>");

        //presenter.generateTable();
        for(TextField filter : this.getFilterFields()){
            filter.addTextChangeListener(new TextChangeListener() {
                @Override
                public void textChange(TextChangeEvent event) {
                    presenter.doFilter(getFilterFields().indexOf(filter), event.getText());
                }
            });
        }

        PropertysetItem stocktypeItem = new PropertysetItem();
        stocktypeItem.addItemProperty("name", new ObjectProperty<String>(""));
        FieldGroup fieldGroup = new FieldGroup(stocktypeItem);
        fieldGroup.setBuffered(true);

        LuvsoftTableBeanValidator<Unit> nameValidator = new LuvsoftTableBeanValidator<Unit>(Unit.class, "name");
        this.content.getColumn("name").getEditorField().addValidator(nameValidator);
        this.content.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                Unit unit = (Unit) content.getEditedItemId();
                nameValidator.setEntity(unit);
                nameValidator.setCalledByPreCommit(true);
            }

            @Override
            public void postCommit(CommitEvent commitEvent) {
                Unit unit = (Unit) content.getEditedItemId();
                presenter.updateEntity(unit, ACTION.UPDATE_BY_TABLE_EDITOR);
            }
        });
    }

    @Override
    public void initView() {
        presenter.generateTable();
    }

    /**
     * Do the stuff when Add button clicked
     */
    protected void onAddButtonClicked(){
        Unit unit = new Unit();
        UnitFormCreator form = new UnitFormCreator();
        form.createForm(unit, presenter, ACTION.CREATE);
    }
    
    /**
     * Do the stuff when Edit button clicked
     */
    protected void onEditButtonClicked(){
        Unit unit = null;
        for (Object object : content.getSelectedRows()) {
            unit = (Unit) object;
        }
        if(unit == null) {
            return;
        }

        UnitFormCreator form = new UnitFormCreator();
        form.createForm(unit, presenter, ACTION.UPDATE);
    }

    /**
     * Do the stuff when Delete button clicked
     */
    protected void onDeleteButtonClicked(){
        Collection<Object> selectedRows = content.getSelectedRows();
        LuvsoftConfirmationDialog dialog = new LuvsoftConfirmationDialog("Xác nhận xóa?");
        dialog.addLuvsoftClickListener(new ClickListener() {
            private static final long serialVersionUID = 351366856643651627L;

            @Override
            public void buttonClick(ClickEvent event) {
                for (Object object : selectedRows) {
                    presenter.deleteEntity((Unit) object);
                }
                content.deselectAll();
                dialog.close();
            }
        });

        // We do not show confirmation dialog if there is no selected item
        if(selectedRows.isEmpty()) {
            return;
        }

        UI.getCurrent().addWindow(dialog);
    }

    @Override
    protected void onExcelImportButtonClicked() {
        getContent().getUI().addWindow(new FileImportHelper<Unit>(new UnitImporter(), this));
    }

    @SuppressWarnings("serial")
    @Override
    protected void onExcelExportButtonClicked() {
        FileChooser fileChooser = new FileChooser("/", true);
        this.getContent().getUI().addWindow(fileChooser);
        fileChooser.addCloseListener(new CloseListener() {
            @Override
            public void windowClose(CloseEvent e) {
                if( fileChooser.getChoosenFile() != null ){
                    UnitExporter unitExporter = new UnitExporter(fileChooser.getChoosenFile());
                    ErrorId error = unitExporter.export();
                    if( error == ErrorId.EXCEL_EXPORT_NOERROR){
                        ErrorManager.getInstance().notifyWarning(error, "");
                    }
                    else{
                        ErrorManager.getInstance().raiseError(error, "");
                    }
                }
            }
        });
    }
}
