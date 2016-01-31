package com.luvsoft.view.component;

import java.util.List;

import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class GenericTabCategory<T> {
    // MMI part
    protected MVerticalLayout wrapper;
    protected Label title;

    // List of functions
    protected HorizontalLayout functionsWrapper;
    protected Button btnAdd;
    protected Button btnEdit;
    protected Button btnDelete;
    protected Button btnRefresh;
    protected Button btnImportExcel;
    protected Button btnExportExcel;

    protected MGrid<T> content;

    protected GenericTabCategory<T> init(String strTitle) {
        wrapper = new MVerticalLayout();
        wrapper.setSpacing(true);
        wrapper.setSizeFull();

        functionsWrapper = new HorizontalLayout();
        functionsWrapper.setSpacing(true);

        // Build list of Button who is child of functionsWrapper
        btnAdd = new Button("Thêm Mới", FontAwesome.PLUS);
        btnAdd.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnEdit = new Button("Sửa", FontAwesome.PENCIL);
        btnDelete = new Button("Xóa", FontAwesome.BAN);
        btnRefresh = new Button("Làm Mới", FontAwesome.REFRESH);
        btnImportExcel = new Button("Nhập Vào Từ Excel", FontAwesome.ARROW_DOWN);
        btnExportExcel = new Button("Xuất Ra Excel", FontAwesome.ARROW_UP);

        content = new MGrid<T>();
        content.setSizeFull();
        content.setEditorEnabled(true);
        content.setSelectionMode(SelectionMode.MULTI);

        title = new Label(strTitle);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_LARGE);

        wrapper.addComponents(title, functionsWrapper, content);
        return this;
    }

    /**
     * Function is used to add functional buttons to this view
     * @return
     */
    public GenericTabCategory<T> withGeneralFuntionsList() {
        functionsWrapper.addComponents(btnAdd, btnDelete, btnEdit, btnImportExcel, btnExportExcel, btnRefresh);
        return this;
    }

    /**
     * Function is used to set properties for table content
     * @param properties
     * @return
     */
    public GenericTabCategory<T> withTableProperties(String... properties) {
        content.withProperties(properties);
        return this;
    }

    /**
     * Function is used to set name for each column
     * @param propertyId
     * @param text
     * @return
     */
    public GenericTabCategory<T> withHeaderNames(String propertyId, String text) {
        content.getDefaultHeaderRow().getCell(propertyId).setHtml(text);
        return this;
    }

    /**
     * Function is used to set data for table content
     * @param listData
     * @return
     */
    public GenericTabCategory<T> withContentData(List<T> listData) {
        content.setRows(listData);
        return this;
    }

    public VerticalLayout getWrapper() {
        return wrapper;
    }

    public void setWrapper(MVerticalLayout wrapper) {
        this.wrapper = wrapper;
    }

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public HorizontalLayout getFunctionsWrapper() {
        return functionsWrapper;
    }

    public void setFunctionsWrapper(HorizontalLayout functionsWrapper) {
        this.functionsWrapper = functionsWrapper;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(Button btnAdd) {
        this.btnAdd = btnAdd;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public void setBtnEdit(Button btnEdit) {
        this.btnEdit = btnEdit;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public void setBtnRefresh(Button btnRefresh) {
        this.btnRefresh = btnRefresh;
    }

    public Button getBtnImportExcel() {
        return btnImportExcel;
    }

    public void setBtnImportExcel(Button btnImportExcel) {
        this.btnImportExcel = btnImportExcel;
    }

    public Button getBtnExportExcel() {
        return btnExportExcel;
    }

    public void setBtnExportExcel(Button btnExportExcel) {
        this.btnExportExcel = btnExportExcel;
    }

    public MGrid<T> getContent() {
        return content;
    }

    public void setContent(MGrid<T> content) {
        this.content = content;
    }
}
