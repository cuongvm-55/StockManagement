package com.luvsoft.view.StockType;

import java.util.List;

import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.StockTypePresenter;
import com.luvsoft.view.component.GenericTabCategory;
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
                                    .withTableProperties("id", "name", "description");
        presenter.generateTable();

        // Handle events
        this.btnAdd.addClickListener(this);
        this.btnEdit.addClickListener(this);
        this.btnDelete.addClickListener(this);
        this.btnImportExcel.addClickListener(this);
        this.btnExportExcel.addClickListener(this);
        this.btnRefresh.addClickListener(this);
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
        }
    }

}
