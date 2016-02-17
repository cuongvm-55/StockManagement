package com.luvsoft.presenter;

import com.luvsoft.DAO.FilterObject;
import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.view.StockType.StockTypeView;

public class StockTypePresenter implements StockTypeListener {
    private StockTypeView view;
    private StockTypeModel model;
    private final int DEFAULT_NUMBER_OF_RECORDS_PER_PAGE = 5;
    private String criteria; // filtering value

    public StockTypePresenter(StockTypeView view) {
        this.view = view;
        model = new StockTypeModel();
        criteria = "";
    }

    public void doFilter(String criteria){
        this.criteria = criteria;
        goToPage(0); // always return page 1
    }

    public void refreshView(){
        this.criteria = "";
        view.getFilterField().clear();
        goToPage(0);
    }

    @Override
    public void generateTable() {
        criteria = "";
        FilterObject filterObject = new FilterObject(
                null,
                "", // we want to get all records when generate table
                0,
                DEFAULT_NUMBER_OF_RECORDS_PER_PAGE);

        view.setTable(model.getFilterData(filterObject));
        view.setNumberOfPages(0, (int) Math.ceil((double) model.getCountData(filterObject))/DEFAULT_NUMBER_OF_RECORDS_PER_PAGE);
    }

    @Override
    public void goToPage(int number) {
        FilterObject filterObject = new FilterObject(
                view.getTableProperties(),
                criteria,
                0,
                DEFAULT_NUMBER_OF_RECORDS_PER_PAGE);

        int pageTotal = (int) Math.ceil((double) model.getCountData(filterObject)/DEFAULT_NUMBER_OF_RECORDS_PER_PAGE);
        if(number > pageTotal-1) {
            number = pageTotal - 1;
        }
        if(number < 0) {
            number = 0;
        }

        filterObject.setPageIndex(number);
        filterObject.setNumberOfRecordsPerPage(DEFAULT_NUMBER_OF_RECORDS_PER_PAGE);
        view.setTable(model.getFilterData(filterObject));
        view.setNumberOfPages(number, pageTotal);
    }

    @Override
    public void goToFirstPage() {
        goToPage(0);
    }

    @Override
    public void goToLastPage() {
        goToPage(view.getPaginationNumberWrapper().getComponentCount()-1);
    }

    @Override
    public void goToNextPage() {
        goToPage(view.getCurrentPage()+1);
    }

    @Override
    public void goToPreviousPage() {
        goToPage(view.getCurrentPage()-1);
    }
}
