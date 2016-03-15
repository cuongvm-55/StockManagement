package com.luvsoft.view.StockType;

import java.util.Collection;
import java.util.List;

import com.luvsoft.Excel.StockExporter;
import com.luvsoft.Excel.StockImporter;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.StockPresenter;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class StockView extends GenericTabCategory<Stock> {
    private static final long serialVersionUID = -7975276654447059817L;

    @SuppressWarnings("serial")
    public StockView() {
        presenter = new StockPresenter(this);
        super.init("Danh Sách Kho", Stock.class)
            .withGeneralFuntionsList()
            .withTableProperties("code", "name", "description", "frk_stocktype_name")
            .withHeaderNames("code", "<b>Mã</b>")
            .withHeaderNames("name", "<b>Tên</b>")
            .withHeaderNames("description", "<b>Mô Tả</b>")
            .withHeaderNames("frk_stocktype_name", "<b>Loại Kho</b>");

        presenter.generateTable();

        for(TextField filter : this.getFilterFields()){
            filter.addTextChangeListener(new TextChangeListener() {
                @Override
                public void textChange(TextChangeEvent event) {
                    presenter.doFilter(getFilterFields().indexOf(filter), event.getText());
                }
            });
        }

        PropertysetItem stockItem = new PropertysetItem();
        stockItem.addItemProperty("code", new ObjectProperty<String>(""));
        stockItem.addItemProperty("name", new ObjectProperty<String>(""));
        stockItem.addItemProperty("description", new ObjectProperty<String>(""));
        stockItem.addItemProperty("frk_stocktype_name", new ObjectProperty<String>(""));

        ComboBox stockTypeCombx = new ComboBox();
        List<Stocktype> stockTypeList = presenter.getStockTypeList();
        if( stockTypeList != null ){
            for(Stocktype type : stockTypeList){
                stockTypeCombx.addItem(type.getName());
            }
        }

        //if( !stockTypeCombx.isEmpty() ){
        //    stockTypeCombx.select(itemId);
        //}
        this.content.getColumn("frk_stocktype_name").setEditorField(stockTypeCombx);
        FieldGroup fieldGroup = new FieldGroup(stockItem);
        fieldGroup.setBuffered(true);

        LuvsoftTableBeanValidator<Stock> nameValidator = new LuvsoftTableBeanValidator<Stock>(Stock.class, "name");
        this.content.getColumn("name").getEditorField().addValidator(nameValidator);
        LuvsoftTableBeanValidator<Stock> descritionValidator = new LuvsoftTableBeanValidator<Stock>(Stock.class, "description");
        this.content.getColumn("description").getEditorField().addValidator(descritionValidator);
        this.content.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                Stock stock = (Stock) content.getEditedItemId();
                System.out.println(stock.getFrk_stocktype_name());
                nameValidator.setEntity(stock);
                nameValidator.setCalledByPreCommit(true);
                content.getEditorFieldGroup().isValid();
            }

            @Override
            public void postCommit(CommitEvent commitEvent) {
                Stock stock = (Stock) content.getEditedItemId();
                System.out.println(stock.getFrk_stocktype_name());
                presenter.updateEntity(stock, ACTION.UPDATE_BY_TABLE_EDITOR);
            }
        });
    }

    /**
     * Do the stuff when Add button clicked
     */
    protected void onAddButtonClicked(){
        Stock stock = new Stock();
        StockFromCreator form = new StockFromCreator();
        form.createForm(stock, presenter, ACTION.CREATE);
    }

    /**
     * Do the stuff when Edit button clicked
     */
    protected void onEditButtonClicked(){
        Stock stock = null;
        for (Object object : content.getSelectedRows()) {
            stock = (Stock) object;
        }
        if(stock == null) {
            return;
        }

        StockFromCreator form = new StockFromCreator();
        form.createForm(stock, presenter, ACTION.UPDATE);
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
                    Stock stock = (Stock)object;
                    presenter.deleteEntity(stock);
                }
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
        getContent().getUI().addWindow(new FileImportHelper(new StockImporter()));
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
                    StockExporter stockExporter = new StockExporter(fileChooser.getChoosenFile());
                    ErrorId error = stockExporter.export();
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
