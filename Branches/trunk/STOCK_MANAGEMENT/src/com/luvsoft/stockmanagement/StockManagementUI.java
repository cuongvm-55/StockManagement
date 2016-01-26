package com.luvsoft.stockmanagement;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.viritin.fields.MTable;

import com.luvsoft.entities.Stocktype;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("stockmanagement")
public class StockManagementUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        List<Stocktype> data = new ArrayList<Stocktype>();
        for (int i = 0; i < 20; i++) {
            Stocktype entity = new Stocktype();
            entity.setId(i);
            entity.setName("Name " + i);
            entity.setDescription("Description " + i);

            data.add(entity);
        }

        MTable<Stocktype> table = new MTable<Stocktype>(data).withProperties("id", "name", "description")
                .withColumnHeaders("Id", "Name", "Description").withFullHeight().withFullWidth();
        table.setEditable(true);

        layout.addComponent(table);
    }

}