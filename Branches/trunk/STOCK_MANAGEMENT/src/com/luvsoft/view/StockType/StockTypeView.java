package com.luvsoft.view.StockType;

import java.util.List;

import com.luvsoft.Excel.StockTypeExporter;
import com.luvsoft.presenter.StockTypePresenter;
import com.luvsoft.view.component.GenericTabCategory;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class StockTypeView extends GenericTabCategory<Object> implements StockTypeViewInterface, ClickListener{
    private static final long serialVersionUID = -7975276654447059817L;
    private StockTypePresenter presenter;

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
    }

    @Override
    public void setTable(List<Object> listData) {
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
            StockTypeExporter stockTypeExporter = new StockTypeExporter();
            if( stockTypeExporter.export() ){
                Notification notify = new Notification("<b>Thông báo</b>",
                        "Excel đã xuất thành công!",
                        Notification.Type.TRAY_NOTIFICATION  , true);
                notify.setPosition(Position.BOTTOM_RIGHT);
                notify.show(Page.getCurrent());
            }
        } else if(event.getButton().equals(btnImportExcel)) {
            // TODO implement handle event at there
        } else if(event.getButton().equals(btnRefresh)) {
            presenter.refreshView();
        }
    }

}
