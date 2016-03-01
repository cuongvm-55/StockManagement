package com.luvsoft.view.StockType;

import java.util.Collection;
import java.util.List;

import com.luvsoft.Excel.StockTypeExporter;
import com.luvsoft.Excel.StockTypeImporter;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.StockTypePresenter;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;


public class StockTypeView extends GenericTabCategory<Stocktype> implements StockTypeViewInterface, ClickListener{
    private static final long serialVersionUID = -7975276654447059817L;
    private StockTypePresenter presenter;

    @SuppressWarnings("serial")
    public StockTypeView() {
        presenter = new StockTypePresenter(this);

        super.init("Danh Sách Các Loại Kho", Stocktype.class)
                                    .withGeneralFuntionsList()
                                    .withTableProperties("name", "description")
                                    .withHeaderNames("name", "<b>Tên</b>")
                                    .withHeaderNames("description", "<b>Mô Tả</b>");
        presenter.generateTable();

        // Handle events
        this.btnAdd.addClickListener(this);
        this.btnEdit.addClickListener(this);
        this.btnDelete.addClickListener(this);
        this.btnImportExcel.addClickListener(this);
        this.btnExportExcel.addClickListener(this);
        this.btnRefresh.addClickListener(this);
        this.btnFirstPage.addClickListener(this);
        this.btnLastPage.addClickListener(this);
        this.btnPreviousPage.addClickListener(this);
        this.btnNextPage.addClickListener(this);

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
        stocktypeItem.addItemProperty("description", new ObjectProperty<String>(""));
        FieldGroup fieldGroup = new FieldGroup(stocktypeItem);
        fieldGroup.setBuffered(true);

        LuvsoftTableBeanValidator<Stocktype> nameValidator = new LuvsoftTableBeanValidator<Stocktype>(Stocktype.class, "name");
        this.content.getColumn("name").getEditorField().addValidator(nameValidator);
        LuvsoftTableBeanValidator<Stocktype> descritionValidator = new LuvsoftTableBeanValidator<Stocktype>(Stocktype.class, "description");
        this.content.getColumn("description").getEditorField().addValidator(descritionValidator);
        this.content.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                Stocktype stocktype = (Stocktype) content.getEditedItemId();
                nameValidator.setEntity(stocktype);
                nameValidator.setCalledByPreCommit(true);
                content.getEditorFieldGroup().isValid();
            }

            @Override
            public void postCommit(CommitEvent commitEvent) {
                    Stocktype stocktype = (Stocktype) content.getEditedItemId();
                    presenter.updateEntity(stocktype, ACTION.UPDATE_BY_TABLE_EDITOR);
            }
        });
    }

    @Override
    public void setTable(List<Stocktype> listData) {
        this.withContentData(listData);
    }

    @SuppressWarnings("serial")
    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton().equals(btnAdd)) {
            Stocktype stockType = new Stocktype();
            StockTypeFormCreator form = new StockTypeFormCreator();
            form.createForm(stockType, presenter, ACTION.CREATE);

        } else if(event.getButton().equals(btnEdit)) {
            Stocktype stockType = null;
            for (Object object : content.getSelectedRows()) {
                stockType = (Stocktype) object;
            }
            if(stockType == null) {
                return;
            }

            StockTypeFormCreator form = new StockTypeFormCreator();
            form.createForm(stockType, presenter, ACTION.UPDATE);

        } else if(event.getButton().equals(btnDelete)) {
            Collection<Object> selectedRows = content.getSelectedRows();
            LuvsoftConfirmationDialog dialog = new LuvsoftConfirmationDialog("Xác nhận xóa?");
            dialog.addLuvsoftClickListener(new ClickListener() {
                private static final long serialVersionUID = 351366856643651627L;

                @Override
                public void buttonClick(ClickEvent event) {
                    for (Object object : selectedRows) {
                        presenter.deleteEntity((AbstractEntity) object);
                    }
                    dialog.close();
                }
            });

            // We do not show confirmation dialog if there is no selected item
            if(selectedRows.isEmpty()) {
                return;
            }

            UI.getCurrent().addWindow(dialog);

        } else if(event.getButton().equals(btnExportExcel)) {
            FileChooser fileChooser = new FileChooser("/", true);
            this.getContent().getUI().addWindow(fileChooser);
            fileChooser.addCloseListener(new CloseListener() {
                @Override
                public void windowClose(CloseEvent e) {
                    if( fileChooser.getChoosenFile() != null ){
                        StockTypeExporter stockTypeExporter = new StockTypeExporter(fileChooser.getChoosenFile());
                        ErrorId error = stockTypeExporter.export();
                        if( error == ErrorId.EXCEL_EXPORT_NOERROR){
                            ErrorManager.getInstance().notifyWarning(error, "");
                        }
                        else{
                            ErrorManager.getInstance().raiseError(error, "");
                        }
                    }
                }
            });
            
        } else if(event.getButton().equals(btnImportExcel)) {
            getContent().getUI().addWindow(new FileImportHelper(new StockTypeImporter()));
        } else if(event.getButton().equals(btnRefresh)) {
            presenter.refreshView();
        } else if(event.getButton().equals(btnFirstPage)) {
            presenter.goToFirstPage();
        } else if(event.getButton().equals(btnLastPage)) {
            presenter.goToLastPage();
        } else if(event.getButton().equals(btnNextPage)) {
            presenter.goToNextPage();
        } else if(event.getButton().equals(btnPreviousPage)) {
            presenter.goToPreviousPage();
        } else {
            for(int i=0; i<this.paginationNumberWrapper.getComponentCount(); i++) {
                Button btnNumber = (Button) this.paginationNumberWrapper.getComponent(i);
                if(btnNumber.getData().equals(event.getButton().getData())) {
                    presenter.goToPage(i);
                }
            }
        }
    }
}
