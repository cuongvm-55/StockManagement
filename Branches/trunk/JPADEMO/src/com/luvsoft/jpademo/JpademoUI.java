package com.luvsoft.jpademo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.vaadin.viritin.fields.MTable;

import com.luvsoft.entities.Stocktype;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("jpademo")
public class JpademoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = JpademoUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		List<Stocktype> data = new ArrayList<Stocktype>();
        for(int i = 0; i<20; i++) {
            Stocktype entity = new Stocktype();
            entity.setId(i);
            entity.setName("Name " + i);
            entity.setDescription("Description " + i);

            data.add(entity);
        }
        
        MTable<Stocktype> table = new MTable<Stocktype>(data)
                .withProperties("id", "name", "description")
                .withColumnHeaders("Id", "Name", "Description")
                .withFullHeight().withFullWidth();
        table.setEditable(true);

		layout.addComponent(table);
	}

}