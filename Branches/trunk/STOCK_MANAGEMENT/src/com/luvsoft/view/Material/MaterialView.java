package com.luvsoft.view.Material;

import java.util.Collection;
import java.util.List;

import com.luvsoft.Excel.MaterialExporter;
import com.luvsoft.Excel.MaterialImporter;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Unit;
import com.luvsoft.presenter.MaterialPresenter;
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

public class MaterialView extends GenericTabCategory<Material> {
    private static final long serialVersionUID = -7975276654447059817L;

    @SuppressWarnings("serial")
    public MaterialView() {
        presenter = new MaterialPresenter(this);
        super.init("Danh Sách Vật Tư", Material.class)
        .withGeneralFuntionsList()
        .withTableProperties("code",
                "name",
                "quantity",
                "frk_unit_name",
                "price",
                "frk_stock_name",
                "frk_materialtype1_name",
                "frk_materialtype2_name",
                "description")
        .withHeaderNames("code", "<b>Mã</b>")
        .withHeaderNames("name", "<b>Tên</b>")
        .withHeaderNames("quantity", "<b>Số Lượng</b>")
        .withHeaderNames("frk_unit_name", "<b>Đv Tính</b>")
        .withHeaderNames("price","<b>Giá</b>")
        .withHeaderNames("frk_stock_name","<b>Tên Kho</b>")
        .withHeaderNames("frk_materialtype1_name","<b>Loại Vật Tư 1</b>")
        .withHeaderNames("frk_materialtype2_name","<b>Loại Vật Tư 2</b>")
        .withHeaderNames("description", "<b>Mô Tả</b>");

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
        stockItem.addItemProperty("frk_stock_name", new ObjectProperty<String>(""));
        stockItem.addItemProperty("frk_unit_name", new ObjectProperty<String>(""));
        stockItem.addItemProperty("frk_materialtype1_name", new ObjectProperty<String>(""));
        stockItem.addItemProperty("frk_materialtype2_name", new ObjectProperty<String>(""));

        // Unit
        ComboBox unitCombx = new ComboBox();
        List<Unit> unitList = presenter.getUnitList();
        if( unitList != null ){
            for(Unit type : unitList){
                unitCombx.addItem(type.getName());
            }
        }
        this.content.getColumn("frk_unit_name").setEditorField(unitCombx);

        // Stock
        ComboBox stockCombx = new ComboBox();
        List<Stock> stockList = presenter.getStockList();
        if( stockList != null ){
            for(Stock type : stockList){
                stockCombx.addItem(type.getName());
            }
        }
        this.content.getColumn("frk_stock_name").setEditorField(stockCombx);

        // Material type 1
        ComboBox cst1Combx = new ComboBox();
        List<Materialtype1> cst1List = presenter.getMaterialType1List();
        if( cst1List != null ){
            for(Materialtype1 type : cst1List){
                cst1Combx.addItem(type.getName());
            }
        }
        this.content.getColumn("frk_materialtype1_name").setEditorField(cst1Combx);

        // Material type 2
        ComboBox cst2Combx = new ComboBox();
        List<Materialtype2> cst2List = presenter.getMaterialType2List();
        if( cst1List != null ){
            for(Materialtype2 type : cst2List){
                cst2Combx.addItem(type.getName());
            }
        }
        this.content.getColumn("frk_materialtype2_name").setEditorField(cst2Combx);

        FieldGroup fieldGroup = new FieldGroup(stockItem);
        fieldGroup.setBuffered(true);

        LuvsoftTableBeanValidator<Material> codeValidator = new LuvsoftTableBeanValidator<Material>(Material.class, "code");
        this.content.getColumn("code").getEditorField().addValidator(codeValidator);
        LuvsoftTableBeanValidator<Material> nameValidator = new LuvsoftTableBeanValidator<Material>(Material.class, "name");
        this.content.getColumn("name").getEditorField().addValidator(nameValidator);

        LuvsoftTableBeanValidator<Material> stockValidator = new LuvsoftTableBeanValidator<Material>(Material.class, "stock");
        this.content.getColumn("frk_stock_name").getEditorField().addValidator(stockValidator);

        LuvsoftTableBeanValidator<Material> unitValidator = new LuvsoftTableBeanValidator<Material>(Material.class, "unit");
        this.content.getColumn("frk_unit_name").getEditorField().addValidator(unitValidator);

        LuvsoftTableBeanValidator<Material> cst1Validator = new LuvsoftTableBeanValidator<Material>(Material.class, "materialtype1");
        this.content.getColumn("frk_materialtype1_name").getEditorField().addValidator(cst1Validator);

        LuvsoftTableBeanValidator<Material> cst2Validator = new LuvsoftTableBeanValidator<Material>(Material.class, "materialtype2");
        this.content.getColumn("frk_materialtype2_name").getEditorField().addValidator(cst2Validator);

        this.content.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                Material cs = (Material) content.getEditedItemId();
                nameValidator.setEntity(cs);
                nameValidator.setCalledByPreCommit(true);
                codeValidator.setEntity(cs);
                codeValidator.setCalledByPreCommit(true);
            }

            @Override
            public void postCommit(CommitEvent commitEvent) {
                Material cs = (Material) content.getEditedItemId();
                presenter.updateEntity(cs, ACTION.UPDATE_BY_TABLE_EDITOR);
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
        Material cs = new Material();
        cs.verifyObject();
        MaterialFromCreator form = new MaterialFromCreator();
        form.createForm(cs, presenter, ACTION.CREATE);
    }

    /**
     * Do the stuff when Edit button clicked
     */
    protected void onEditButtonClicked(){
        Material cs = null;
        for (Object object : content.getSelectedRows()) {
            cs = (Material) object;
        }
        if(cs == null) {
            return;
        }

        MaterialFromCreator form = new MaterialFromCreator();
        form.createForm(cs, presenter, ACTION.UPDATE);
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
                    Material cs = (Material)object;
                    presenter.deleteEntity(cs);
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
        getContent().getUI().addWindow(new FileImportHelper<Material>(new MaterialImporter(), this));
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
                    MaterialExporter stockExporter = new MaterialExporter(fileChooser.getChoosenFile());
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
