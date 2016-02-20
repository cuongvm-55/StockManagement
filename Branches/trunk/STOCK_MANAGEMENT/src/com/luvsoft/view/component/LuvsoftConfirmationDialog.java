package com.luvsoft.view.component;

import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class LuvsoftConfirmationDialog extends MWindow {
    private static final long serialVersionUID = -3861778072610238436L;
    private Button save;

    @SuppressWarnings("serial")
    public LuvsoftConfirmationDialog(String title) {
        withClosable(true).withDraggable(false).withModal(true).withResizable(false).setWidth("20%");

        MVerticalLayout wrapper = new MVerticalLayout();

        Label lblTitle = new Label(title);
        lblTitle.addStyleName(ValoTheme.LABEL_BOLD);
        wrapper.add(lblTitle);

        MHorizontalLayout footer = new MHorizontalLayout();
        footer.setSizeFull();
        wrapper.add(footer);

        Button esc = new Button("Hủy");
        esc.setIcon(FontAwesome.SIGN_OUT);
        esc.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });

        save = new Button("Xác Nhận");
        save.setIcon(FontAwesome.CHECK);
        save.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        save.setClickShortcut(KeyCode.ENTER);

        footer.add(esc);
        footer.add(save, Alignment.MIDDLE_RIGHT);
        footer.setExpandRatio(save, 1.0f);

        this.setContent(wrapper);
    }

    public void addLuvsoftClickListener(ClickListener listener) {
        save.addClickListener(listener);
    }
}
