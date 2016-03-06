package com.luvsoft.presenter;

import java.util.Map;

import com.luvsoft.DAO.AbstractEntityModel;
import com.luvsoft.DAO.FilterObject;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.GenericTabCategory;
import com.vaadin.ui.TextField;

public class AbstractEntityPresenter {
    @SuppressWarnings("rawtypes")
    protected GenericTabCategory view;
    protected AbstractEntityModel model;
    
    protected final int DEFAULT_NUMBER_OF_RECORDS_PER_PAGE = 8;
    protected ACTION action;
    protected int currentPage = 0;

    protected Map<String, String> criteriaMap;
    
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
            criteriaMap.put((String)view.getTableProperties().get(filterFieldIdx), value);
        }

        goToPage(0); // always back to the first page
    }

    public void refreshView(){
        for( Object filter : view.getFilterFields() ){
            TextField tf = (TextField)filter;
            tf.clear();
        }
        criteriaMap.clear();
        goToPage(0);
    }

    public void generateTable() {
        criteriaMap.clear(); // we do not filter data when generate table
        goToPage(currentPage);
    }

    @SuppressWarnings("unchecked")
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

        // Due to a possible bug in Vaadin (table cannot setRows by itself when we update table by table editor)
        // we consider to work around it by separating UPDATE_BY_TABLE_EDITOR like a specific case
        if(action.equals(ACTION.UPDATE_BY_TABLE_EDITOR))
        {
            view.setListOfData(model.getFilterData(filterObject));
        } else {
            view.setTable(model.getFilterData(filterObject));
        }
        view.setNumberOfPages(number, pageTotal);
        currentPage = number;
    }

    public void goToFirstPage() {
        goToPage(0);
    }

    public void goToLastPage() {
        goToPage(view.getPaginationNumberWrapper().getComponentCount());
    }

    public void goToNextPage() {
        goToPage(view.getCurrentPage()+1);
    }

    public void goToPreviousPage() {
        goToPage(view.getCurrentPage()-1);
    }

    public void updateEntity(AbstractEntity entity) {
        if(action.equals(ACTION.CREATE)) {
            model.addNew((Stocktype) entity);
            goToLastPage();
        }
        else if(action.equals(ACTION.UPDATE)) {
            model.update((Stocktype) entity);
            generateTable();
        } else if(action.equals(ACTION.UPDATE_BY_TABLE_EDITOR)) {
            model.update((Stocktype) entity);
            generateTable();
        }
        this.action = ACTION.UNKNOWN; // Reset action state after modification to avoid duplicate action in the future
    }

    public void updateEntity(AbstractEntity entity, ACTION action) {
        this.setAction(action);
        updateEntity(entity);
    }

    public void deleteEntity(AbstractEntity entity) {
        model.deleteEntity((Stocktype) entity);
        generateTable();
    }

    public void setAction(ACTION action) {
        this.action = action;
    }
}
