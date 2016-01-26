package com.luvsoft.view.dummy;

import java.util.List;

import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.StockTypeListener;

public class StockTypeView extends MVerticalLayout implements StockTypeViewInterface{

    private MTable<Stocktype> table = new MTable<Stocktype>(Stocktype.class)
            .withProperties("id", "name", "description")
            .withColumnHeaders("Id", "Name", "Description")
            .withFullHeight().withFullWidth();

    public StockTypeView() {
        init();
    }

    @Override
    public void init() {
        this.addComponent(table);
    }

    @Override
    public void setTable(List<Stocktype> listData) {
        table.setBeans(listData);
    }

}
