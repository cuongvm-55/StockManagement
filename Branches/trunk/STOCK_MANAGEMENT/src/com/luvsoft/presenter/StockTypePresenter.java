package com.luvsoft.presenter;

import java.util.HashMap;
import java.util.Map;

import com.luvsoft.DAO.FilterObject;
import com.luvsoft.DAO.StockTypeModel;
import com.luvsoft.view.StockType.StockTypeView;
import com.vaadin.ui.TextField;

public class StockTypePresenter implements StockTypeListener {
    private StockTypeView view;
    private StockTypeModel model;
    private final int DEFAULT_NUMBER_OF_RECORDS_PER_PAGE = 10;

    private Map<String, String> criteriaMap;
    
    public StockTypePresenter(StockTypeView view) {
        this.view = view;
        model = new StockTypeModel();
        criteriaMap = new HashMap<String, String>();
    }

    /**
     * Do the filter when value of one of TextField in textFields list changed
     * We expect that the index of TextField is always corresponding to the index of table property
     * @param filterFieldIdx
     * @param value
     */
    public void doFilter(int filterFieldIdx, String value){
        // remove old <K, V> if exist
        criteriaMap.remove(view.getTableProperties().get(filterFieldIdx));

        // add new <K, V>
        if( value != null && !value.equals("") ){
            criteriaMap.put(view.getTableProperties().get(filterFieldIdx), value);
        }

        goToPage(0); // always back to the first page
    }

    public void refreshView(){
        for( TextField filter : view.getFilterFields() ){
            filter.clear();
        }
        criteriaMap.clear();
        goToPage(0);
    }

    @Override
    public void generateTable() {
        criteriaMap.clear(); // we do not filter data when generate table
        goToPage(0);
    }

    @Override
    public void goToPage(int number) {
        FilterObject filterObject = new FilterObject(
                criteriaMap,
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
