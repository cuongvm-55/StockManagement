package com.luvsoft.view.StockType;

import java.util.List;

import com.luvsoft.Excel.ErrorManager;
import com.luvsoft.Excel.ErrorManager.ErrorId;
import com.luvsoft.Excel.StockTypeExporter;
import com.luvsoft.Excel.StockTypeImporter;
import com.luvsoft.presenter.StockTypePresenter;
import com.luvsoft.view.component.FileChooser;
import com.luvsoft.view.component.FileImportHelper;
import com.luvsoft.view.component.GenericTabCategory;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class StockTypeView extends GenericTabCategory<Object> implements StockTypeViewInterface, ClickListener{
    private static final long serialVersionUID = -7975276654447059817L;
    private StockTypePresenter presenter;

    @SuppressWarnings("serial")
    public StockTypeView() {
        presenter = new StockTypePresenter(this);

        super.init("Danh Sách Các Loại Kho")
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

    }

    @Override
    public void setTable(List<Object> listData) {
        this.withContentData(listData);
    }

    @SuppressWarnings("serial")
    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton().equals(btnAdd)) {
            // TODO implement handle event at there
        } else if(event.getButton().equals(btnEdit)) {
            // TODO implement handle event at there
        } else if(event.getButton().equals(btnDelete)) {
            // TODO implement handle event at there
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
