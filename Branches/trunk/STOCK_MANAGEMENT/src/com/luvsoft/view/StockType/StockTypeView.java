package com.luvsoft.view.StockType;

import java.util.List;

import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.StockTypePresenter;
import com.luvsoft.view.component.GenericTabCategory;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class StockTypeView extends GenericTabCategory<Stocktype> implements StockTypeViewInterface, ClickListener{
    private static final long serialVersionUID = -7975276654447059817L;
    private StockTypePresenter presenter;
    private StockTypeModel model;

    public StockTypeView() {
        model = new StockTypeModel();
        presenter = new StockTypePresenter(this, model);

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
    }

    @Override
    public void setTable(List<Stocktype> listData) {
        this.withContentData(listData);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton().equals(btnAdd)) {
            // TODO implement handle event at there
        } else if(event.getButton().equals(btnEdit)) {
            // TODO implement handle event at there
        } else if(event.getButton().equals(btnDelete)) {
            // TODO implement handle event at there
        } else if(event.getButton().equals(btnExportExcel)) {
            // TODO implement handle event at there
        } else if(event.getButton().equals(btnImportExcel)) {
            // TODO implement handle event at there
        } else if(event.getButton().equals(btnRefresh)) {
            // TODO implement handle event at there
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
