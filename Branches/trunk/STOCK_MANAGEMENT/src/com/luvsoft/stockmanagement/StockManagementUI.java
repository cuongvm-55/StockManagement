package com.luvsoft.stockmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.luvsoft.view.component.FileUploader;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("stockmanagement")
public class StockManagementUI extends UI {

    private VerticalLayout layout;
    @Override
    protected void init(VaadinRequest request) {
        layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();
        setContent(layout);

        MainMenu menu = new MainMenu();
        layout.addComponent(menu);
        layout.setExpandRatio(menu, 2.0f);

        FileUploader uploadField = new FileUploader();
        uploadField.setButtonCaption("Nhập vào từ excel");

        layout.addComponents(uploadField);
        buildFooter();
    }

    private void buildFooter() {
        MHorizontalLayout wrapper = new MHorizontalLayout();
        layout.addComponent(wrapper);
        layout.setExpandRatio(wrapper, 0.06f);

        Label version = new Label("Công Ty TNHH Luvsoft - Phần Mềm Quản Lý Kho Phụ Tùng Xe Máy - Phiên Bản 1.0");
        version.addStyleName(ValoTheme.LABEL_BOLD + " " + ValoTheme.LABEL_SMALL);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        Label date = new Label(format.format(currentDate));
        date.addStyleName(ValoTheme.LABEL_BOLD + " " + ValoTheme.LABEL_SMALL);

        wrapper.addComponents(version, date);
    }
}