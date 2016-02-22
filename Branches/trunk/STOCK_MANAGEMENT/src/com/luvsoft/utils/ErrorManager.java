package com.luvsoft.utils;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class ErrorManager {
    private static ErrorManager instance;

    public static enum ErrorId{
        // import excel
        EXCEL_IMPORT_NOERROR, // no error
        EXCEL_IMPORT_INVALID_NUMBER_OF_RECORD,
        EXCEL_IMPORT_INVALID_HEADERS,
        EXCEL_IMPORT_FAIL_TO_READ_FILE,
        EXCEL_IMPORT_INVALID_NUMBER_OF_HEADERS,
        EXCEL_IMPORT_FAIL_TO_PROCESS_ENTITY_RECORD,

        // export excel
        EXCEL_EXPORT_NOERROR,
        EXCEL_EXPORT_FAIL,

    };

    @SuppressWarnings("serial")
    private static Map<ErrorId, String> errorTexts = new HashMap<ErrorId, String>(){{
        // import excel
        put(ErrorId.EXCEL_IMPORT_NOERROR, "Nhập excel thành công"); // in fact, it's a success warning
        put(ErrorId.EXCEL_IMPORT_INVALID_NUMBER_OF_RECORD, "Số lượng bản ghi không hợp lệ");
        put(ErrorId.EXCEL_IMPORT_INVALID_NUMBER_OF_HEADERS, "Số lượng thuộc tính không hợp lệ");
        put(ErrorId.EXCEL_IMPORT_INVALID_HEADERS, "Giá trị thuộc tính không hợp lệ");
        put(ErrorId.EXCEL_IMPORT_FAIL_TO_READ_FILE, "Đọc tệp không thành công");
        put(ErrorId.EXCEL_IMPORT_FAIL_TO_PROCESS_ENTITY_RECORD, "Thao tác với bản ghi không thành công");

        // export excel
        put(ErrorId.EXCEL_EXPORT_NOERROR, "Xuất excel thành công");
        put(ErrorId.EXCEL_EXPORT_FAIL, "Xuất excel lỗi");
    }};

    public static ErrorManager getInstance(){
        if( instance == null ){
            instance = new ErrorManager();
        }
        return instance;
    }

    static String getExcelErrorString(ErrorId error){
        return errorTexts.get(error);
    }

    /**
     * Use to raise error
     * @param error
     */
    public void raiseError(ErrorId error, String param){
        showNotification("<b>Lỗi</b>",
                "<i>" + getExcelErrorString(error) +"!</i><br>"+ param+ "</br>",
                Notification.Type.ERROR_MESSAGE);
    }

    /**
     * Use for normal notification
     * @param warning
     */
    public void notifyWarning(ErrorId warning, String param){
        showNotification("<b>Thông báo</b>",
                "<i>" + getExcelErrorString(warning) +"</i><br>"+ param+ "</br>",
                Notification.Type.TRAY_NOTIFICATION);
        
    }
    private void showNotification(String title, String description, Notification.Type type){
        Notification notify = new Notification(title,
                description,
                type , true);
        notify.setPosition(Position.BOTTOM_RIGHT);
        notify.show(Page.getCurrent());
    }
}
