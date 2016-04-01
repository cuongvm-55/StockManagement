package com.luvsoft.view.Customer;

import java.util.Collection;
import java.util.List;

import com.luvsoft.Excel.CustomerExporter;
import com.luvsoft.Excel.CustomerImporter;
import com.luvsoft.entities.Area;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype1;
import com.luvsoft.entities.Customertype2;
import com.luvsoft.presenter.CustomerPresenter;
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

public class CustomerView extends GenericTabCategory<Customer> {
    private static final long serialVersionUID = -7975276654447059817L;

    @SuppressWarnings("serial")
    public CustomerView() {
        presenter = new CustomerPresenter(this);
        super.init("Danh Sách Kho", Customer.class)
        .withGeneralFuntionsList()
        .withTableProperties("code",
                "name",
                "address",
                "phoneNumber",
                "email",
                "bankName",
                "bankAccount",
                "debt",
                "frk_area_name",
                "frk_customertype1_name",
                "frk_customertype2_name")
        .withHeaderNames("code", "<b>Mã</b>")
        .withHeaderNames("name", "<b>Tên</b>")
        .withHeaderNames("address", "<b>Địa Chỉ</b>")
        .withHeaderNames("phoneNumber", "<b>Số ĐT</b>")
        .withHeaderNames("email","<b>Email</b>")
        .withHeaderNames("bankName","<b>Tên Ngân Hàng</b>")
        .withHeaderNames("bankAccount","<b>TK Ngân Hàng</b>")
        .withHeaderNames("debt","<b>Nợ</b>")
        .withHeaderNames("frk_area_name","<b>Khu Vực</b>")
        .withHeaderNames("frk_customertype1_name","<b>Loại KH 1</b>")
        .withHeaderNames("frk_customertype2_name","<b>Loại KH 2</b>");

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
        stockItem.addItemProperty("frk_area_name", new ObjectProperty<String>(""));
        stockItem.addItemProperty("frk_customertype1_name", new ObjectProperty<String>(""));
        stockItem.addItemProperty("frk_customertype2_name", new ObjectProperty<String>(""));

        // Area
        ComboBox areaCombx = new ComboBox();
        List<Area> areaList = presenter.getAreaList();
        if( areaList != null ){
            for(Area type : areaList){
                areaCombx.addItem(type.getName());
            }
        }
        this.content.getColumn("frk_area_name").setEditorField(areaCombx);

        // Customer type 1
        ComboBox cst1Combx = new ComboBox();
        List<Customertype1> cst1List = presenter.getCustomerType1List();
        if( cst1List != null ){
            for(Customertype1 type : cst1List){
                cst1Combx.addItem(type.getName());
            }
        }
        this.content.getColumn("frk_customertype1_name").setEditorField(cst1Combx);

        // Customer type 2
        ComboBox cst2Combx = new ComboBox();
        List<Customertype2> cst2List = presenter.getCustomerType2List();
        if( cst1List != null ){
            for(Customertype2 type : cst2List){
                cst2Combx.addItem(type.getName());
            }
        }
        this.content.getColumn("frk_customertype2_name").setEditorField(cst2Combx);

        FieldGroup fieldGroup = new FieldGroup(stockItem);
        fieldGroup.setBuffered(true);

        LuvsoftTableBeanValidator<Customer> codeValidator = new LuvsoftTableBeanValidator<Customer>(Customer.class, "code");
        this.content.getColumn("code").getEditorField().addValidator(codeValidator);
        LuvsoftTableBeanValidator<Customer> nameValidator = new LuvsoftTableBeanValidator<Customer>(Customer.class, "name");
        this.content.getColumn("name").getEditorField().addValidator(nameValidator);

        LuvsoftTableBeanValidator<Customer> areaValidator = new LuvsoftTableBeanValidator<Customer>(Customer.class, "area");
        this.content.getColumn("frk_area_name").getEditorField().addValidator(areaValidator);

        LuvsoftTableBeanValidator<Customer> cst1Validator = new LuvsoftTableBeanValidator<Customer>(Customer.class, "customertype1");
        this.content.getColumn("frk_customertype1_name").getEditorField().addValidator(cst1Validator);

        LuvsoftTableBeanValidator<Customer> cst2Validator = new LuvsoftTableBeanValidator<Customer>(Customer.class, "customertype2");
        this.content.getColumn("frk_customertype2_name").getEditorField().addValidator(cst2Validator);

        this.content.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
                Customer cs = (Customer) content.getEditedItemId();
                nameValidator.setEntity(cs);
                nameValidator.setCalledByPreCommit(true);
                codeValidator.setEntity(cs);
                codeValidator.setCalledByPreCommit(true);
                // content.getEditorFieldGroup().isValid();
            }

            @Override
            public void postCommit(CommitEvent commitEvent) {
                Customer cs = (Customer) content.getEditedItemId();
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
        Customer cs = new Customer();
        cs.verifyObject();
        CustomerFromCreator form = new CustomerFromCreator();
        form.createForm(cs, presenter, ACTION.CREATE);
    }

    /**
     * Do the stuff when Edit button clicked
     */
    protected void onEditButtonClicked(){
        Customer cs = null;
        for (Object object : content.getSelectedRows()) {
            cs = (Customer) object;
        }
        if(cs == null) {
            return;
        }

        CustomerFromCreator form = new CustomerFromCreator();
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
                    Customer cs = (Customer)object;
                    presenter.deleteEntity(cs);
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
        getContent().getUI().addWindow(new FileImportHelper<Customer>(new CustomerImporter(), this));
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
                    CustomerExporter stockExporter = new CustomerExporter(fileChooser.getChoosenFile());
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
