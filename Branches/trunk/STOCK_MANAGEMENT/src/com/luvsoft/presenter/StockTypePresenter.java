package com.luvsoft.presenter;

import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.view.StockType.StockTypeView;

public class StockTypePresenter implements StockTypeListener {
    private StockTypeView view;
    private StockTypeModel model;
    private final int DEFAULT_NUMBER_OF_RECORDS_PER_PAGE = 5;


    public StockTypePresenter(StockTypeView view) {
        this.view = view;
        model = new StockTypeModel();
    }

    @Override
    public void generateTable() {
        view.setTable(model.getData(0, DEFAULT_NUMBER_OF_RECORDS_PER_PAGE));
        view.setNumberOfPages(0, (int) Math.ceil((double) model.getCountData()/DEFAULT_NUMBER_OF_RECORDS_PER_PAGE));
    }

    @Override
    public void goToPage(int number) {
        int pageTotal = (int) Math.ceil((double) model.getCountData()/DEFAULT_NUMBER_OF_RECORDS_PER_PAGE);
        if(number > pageTotal-1) {
            number = pageTotal - 1;
        }
        if(number < 0) {
            number = 0;
        }

        view.setTable(model.getData(number, DEFAULT_NUMBER_OF_RECORDS_PER_PAGE));
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
