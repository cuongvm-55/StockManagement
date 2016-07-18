package com.luvsoft.stockmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.luvsoft.printing.OrderPrintingView;
import com.luvsoft.statistic.StatisticManagerThread;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.JavaScript;
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
        // Local VietNam
        setLocale(new Locale("vi", "VN"));

        boolean isRequestPrinting = false;
        try{
            if( request != null && request.getParameter("OPEN_REASON").equals("PRINT") ){
                isRequestPrinting = true;
            }
        }
        catch(Exception e){
        }
        
        if( isRequestPrinting ){
            OrderPrintingView layout = OrderPrintingView.getInstance();
            layout.setSpacing(true);
            layout.buildContent();
            setContent(layout);
            setHeight(null);
            JavaScript.getCurrent().execute("setTimeout(function() {" + " print(); self.close();}, 20);");
        }
        else{
            layout = new VerticalLayout();
            layout.setSpacing(true);
            layout.setSizeFull();
            setContent(layout);

            MainMenu menu = new MainMenu();
            layout.addComponent(menu);
            layout.setExpandRatio(menu, 2.0f);

            buildFooter();

            // Start statistic manager thread
            if( !StatisticManagerThread.getInstance().isAlive() ){
                StatisticManagerThread.getInstance().start();
            }
        }
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