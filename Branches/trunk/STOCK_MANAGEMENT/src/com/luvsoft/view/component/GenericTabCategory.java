package com.luvsoft.view.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.luvsoft.presenter.AbstractEntityPresenter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class GenericTabCategory<T> implements ClickListener{
    private static final long serialVersionUID = 5952786059392464758L;
    // MMI part
    protected MVerticalLayout wrapper;
    protected Label title;

    // Pagination part
    protected HorizontalLayout paginationWrapper;
    protected HorizontalLayout paginationNumberWrapper;
    protected Button btnFirstPage;
    protected Button btnLastPage;
    protected Button btnNextPage;
    protected Button btnPreviousPage;

    // List of functions
    protected HorizontalLayout functionsWrapper;
    protected Button btnAdd;
    protected Button btnEdit;
    protected Button btnDelete;
    protected Button btnRefresh;
    protected Button btnImportExcel;
    protected Button btnExportExcel;

    List<TextField> filterFields;
    protected HeaderRow filteringHeader;
    protected List<String> tableProperties;
    
    protected MGrid<T> content;
    protected int currentPage;

    // Backing Beans: Any changes (update, delete, create)
    // should re-charge this list instead of setRows() again
    private List<T> listOfData;

    protected AbstractEntityPresenter presenter;

    protected GenericTabCategory<T> init(String strTitle, Class<T> typeOfRows) {
        wrapper = new MVerticalLayout();
        wrapper.setSpacing(true);

        title = new Label(strTitle);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_LARGE);

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

        content = new MGrid<>(typeOfRows);
        content.setSizeFull();
        content.setEditorEnabled(true);
        content.setSelectionMode(SelectionMode.MULTI);
        content.setEditorSaveCaption("Lưu");
        content.setEditorCancelCaption("Hủy");

        tableProperties = new ArrayList<String>();

        filterFields = new ArrayList<TextField>();

        paginationWrapper = new HorizontalLayout();
        paginationWrapper.setSpacing(true);
        paginationNumberWrapper = new HorizontalLayout();
        paginationNumberWrapper.setSpacing(true);
        btnFirstPage = new Button("<<");
        btnFirstPage.addStyleName(ValoTheme.BUTTON_TINY);
        btnLastPage = new Button(">>");
        btnLastPage.addStyleName(ValoTheme.BUTTON_TINY);
        btnNextPage = new Button(">");
        btnNextPage.addStyleName(ValoTheme.BUTTON_TINY);
        btnPreviousPage = new Button("<");
        btnPreviousPage.addStyleName(ValoTheme.BUTTON_TINY);
        paginationWrapper.addComponents(btnFirstPage, btnPreviousPage, paginationNumberWrapper, btnNextPage, btnLastPage);

        wrapper.addComponents(title, functionsWrapper, content, paginationWrapper);
        wrapper.setComponentAlignment(paginationWrapper, Alignment.MIDDLE_CENTER);

        // Handle events
        this.btnAdd.addClickListener(this);
        this.btnEdit.addClickListener(this);
        this.btnDelete.addClickListener(this);
        this.btnImportExcel.addClickListener(this);
        this.btnExportExcel.addClickListener(this);
        this.btnRefresh.addClickListener(this);
        this.btnFirstPage.addClickListener(this);
        this.btnLastPage.addClickListener(this);
        this.btnPreviousPage.addClickListener(this);
        this.btnNextPage.addClickListener(this);
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

    private void setColumnFiltering(boolean filtered) {
        if (filtered && filteringHeader == null) {
            filteringHeader = content.appendHeaderRow();
            for( String property : tableProperties ){
                //filteringHeader = content.addHeaderRowAt(0);
     
                // Add new TextFields to each column which filters the data from
                // that column
                String columnId = property;
                TextField filter = getColumnFilter(columnId);
                ResetButtonForTextField.extend(filter);
                filter.setImmediate(true);
                filteringHeader.getCell(columnId).setComponent(filter);
                filteringHeader.getCell(columnId).setStyleName("filter-header");
    
                // save to handle filter box
                filterFields.add(filter);
            }
        } else if (!filtered && filteringHeader != null) {
            content.removeHeaderRow(filteringHeader);
            filteringHeader = null;
        }
    }

    private TextField getColumnFilter(final Object columnId) {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setInputPrompt("Tìm kiếm...");
        return filter;
    }

    /**
     * Function is used to set properties for table content
     * @param properties
     * @return
     */
    public GenericTabCategory<T> withTableProperties(String... properties) {
        content.withProperties(properties);
        tableProperties.clear();
        tableProperties.addAll(Arrays.asList(properties));
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
    public GenericTabCategory<T> withContentData(List<T> listOfData) {
        try{
            this.listOfData = listOfData;
            content.setRows(this.listOfData);
        }catch(Exception e)
        {
            System.out.println("No record found.");
        }
        setColumnFiltering(true);
        return this;
    }

    /**
     * This function is used to display page number
     * @param currentPage
     * @param pageTotal
     * @return
     */
    public GenericTabCategory<T> setNumberOfPages(int currentPage, long pageTotal) {
        paginationNumberWrapper.removeAllComponents();

        for(int i=0; i<pageTotal; i++) {
            Button btnPage = new Button((i+1)+"");
            btnPage.addStyleName(ValoTheme.BUTTON_TINY);
            btnPage.setData(i);
            btnPage.addClickListener(this);

            paginationNumberWrapper.addComponent(btnPage);
            if(currentPage == i) {
                btnPage.addStyleName(ValoTheme.BUTTON_FRIENDLY);
                this.currentPage = currentPage;
            }
        }

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

    public HorizontalLayout getPaginationWrapper() {
        return paginationWrapper;
    }

    public void setPaginationWrapper(HorizontalLayout paginationWrapper) {
        this.paginationWrapper = paginationWrapper;
    }

    public HorizontalLayout getPaginationNumberWrapper() {
        return paginationNumberWrapper;
    }

    public void setPaginationNumberWrapper(HorizontalLayout paginationNumberWrapper) {
        this.paginationNumberWrapper = paginationNumberWrapper;
    }

    public Button getBtnFirstPage() {
        return btnFirstPage;
    }

    public void setBtnFirstPage(Button btnFirstPage) {
        this.btnFirstPage = btnFirstPage;
    }

    public Button getBtnLastPage() {
        return btnLastPage;
    }

    public void setBtnLastPage(Button btnLastPage) {
        this.btnLastPage = btnLastPage;
    }

    public Button getBtnNextPage() {
        return btnNextPage;
    }

    public void setBtnNextPage(Button btnNextPage) {
        this.btnNextPage = btnNextPage;
    }

    public Button getBtnPreviousPage() {
        return btnPreviousPage;
    }

    public void setBtnPreviousPage(Button btnPreviousPage) {
        this.btnPreviousPage = btnPreviousPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<String> getTableProperties(){
        return tableProperties;
    }

    public List<TextField> getFilterFields() {
        return filterFields;
    }

    public void setFilterFields(List<TextField> filterFields) {
        this.filterFields = filterFields;
    }

    public List<T> getListOfData() {
        return listOfData;
    }

    public void setListOfData(List<T> listOfData) {
        this.listOfData = listOfData;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if(event.getButton().equals(btnAdd)) {
            onAddButtonClicked();
        } else if(event.getButton().equals(btnEdit)) {
            onEditButtonClicked();
        } else if(event.getButton().equals(btnDelete)) {
            onDeleteButtonClicked();
        } else if(event.getButton().equals(btnExportExcel)) {
            onExcelExportButtonClicked();
        } else if(event.getButton().equals(btnImportExcel)) {
            onExcelImportButtonClicked();
        } else if(event.getButton().equals(btnRefresh)) {
            presenter.refreshView();
        } else if(event.getButton().equals(btnFirstPage)) {
            presenter.goToFirstPage();
        } else if(event.getButton().equals(btnLastPage)) {
            presenter.goToLastPage();
        } else if(event.getButton().equals(btnNextPage)) {
            presenter.goToNextPage();
        } else if(event.getButton().equals(btnPreviousPage)) {
            presenter.goToPreviousPage();
        } else {
            for(int i=0; i<this.paginationNumberWrapper.getComponentCount(); i++) {
                Button btnNumber = (Button) this.paginationNumberWrapper.getComponent(i);
                if(btnNumber.getData().equals(event.getButton().getData())) {
                    presenter.goToPage(i);
                }
            }
        }
    }

    public void setTable(List<T> listData) {
        this.withContentData(listData);
    }

    // abstract functions
    protected abstract void onAddButtonClicked();
    protected abstract void onEditButtonClicked();
    protected abstract void onDeleteButtonClicked();
    protected abstract void onExcelImportButtonClicked();
    protected abstract void onExcelExportButtonClicked();
}
