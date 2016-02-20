package com.luvsoft.presenter;

import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.utils.ACTION;
import com.luvsoft.utils.NemErrorList;
import com.luvsoft.view.StockType.StockTypeView;

public class StockTypePresenter implements StockTypeListener {
    private StockTypeView view;
    private StockTypeModel model;
    private final int DEFAULT_NUMBER_OF_RECORDS_PER_PAGE = 8;
    private ACTION action;
    private int currentPage = 0;

    public StockTypePresenter(StockTypeView view) {
        this.view = view;
        model = new StockTypeModel();
    }

    @Override
    public void generateTable() {
        goToPage(currentPage);
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
        currentPage = number;
    }

    @Override
    public void goToFirstPage() {
        goToPage(0);
    }

    @Override
    public void goToLastPage() {
        goToPage(view.getPaginationNumberWrapper().getComponentCount());
    }

    @Override
    public void goToNextPage() {
        goToPage(view.getCurrentPage()+1);
    }

    @Override
    public void goToPreviousPage() {
        goToPage(view.getCurrentPage()-1);
    }

    /**
     * Refresh view
     */
    public void refreshView(){
        generateTable();
    }

    @Override
    public void updateEntity(AbstractEntity entity) {
        if(action.equals(ACTION.CREATE)) {
            model.addNew((Stocktype) entity);
            goToLastPage();
        }
        else if(action.equals(ACTION.UPDATE)) {
            model.update((Stocktype) entity);
            // refresh table
            generateTable();
        }
    }

    @Override
    public boolean validateForm(AbstractEntity entity) {
        Stocktype stocktype = (Stocktype) entity;
        if(model.isDuplicatedName(stocktype)) {
            NemErrorList.raiseErrorFieldExisted("Tên");
            return false;
        }

        if(stocktype.getName().trim().isEmpty()) {
            System.out.println("EMPTY " + stocktype.getName());
            NemErrorList.raiseErrorFieldEmpty("Tên");
            return false;
        }
        return true;
    }

    @Override
    public void deleteEntity(AbstractEntity entity) {
        model.deleteEntity((Stocktype) entity);
        generateTable();
    }

    @Override
    public void setAction(ACTION action) {
        this.action = action;
    }

}
