package com.luvsoft.utils;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class NemErrorList {
    public static final String Error_NameTooLong = "Tên không được vượt quá 45 ký tự";
    public static final String Error_NameNotEmpty = "Tên không được để trống";

    public static void raiseErrorDataTooLong(String field, int maximum) {
        Notification notification = new Notification("<b>Lỗi</b>", "<i><b>" + field + " không được vượt quá "
                                                    + maximum + " kí tự</i></b>",
                Notification.Type.TRAY_NOTIFICATION, true);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }

    public static void raiseErrorFieldEmpty(String field) {
        Notification notification = new Notification("<b>Lỗi</b>", "<i><b>" + field + " không được để trống</i></b>",
                Notification.Type.TRAY_NOTIFICATION, true);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }

    public static void raiseErrorFieldExisted(String field) {
        Notification notification = new Notification("<b>Lỗi</b>", "<i><b>" + field + " đã tồn tại</i></b>",
                Notification.Type.TRAY_NOTIFICATION, true);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }
}
