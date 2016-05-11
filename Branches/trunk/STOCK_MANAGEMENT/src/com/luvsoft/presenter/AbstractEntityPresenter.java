package com.luvsoft.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luvsoft.DAO.AbstractEntityModel;
import com.luvsoft.DAO.FilterObject;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Area;
import com.luvsoft.entities.Customertype1;
import com.luvsoft.entities.Customertype2;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.entities.Unit;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.GenericTabCategory;
import com.vaadin.ui.TextField;

public abstract class AbstractEntityPresenter implements UpdateEntityListener{
    @SuppressWarnings("rawtypes")
    protected GenericTabCategory view;
    protected AbstractEntityModel model;
    
    protected final int DEFAULT_NUMBER_OF_RECORDS_PER_PAGE = 8;
    protected ACTION action;
    protected int currentPage = 0;

    protected Map<String, String> criteriaMap;

    AbstractEntityPresenter(){
        criteriaMap = new HashMap<String, String>();
        action = ACTION.UNKNOWN;
    }

    /**
     * Do the filter when value of one of TextField in textFields list changed
     * We expect that the index of TextField is always corresponding to the index of table property
     * @param filterFieldIdx
     * @param value
     */
    public void doFilter(int filterFieldIdx, String value){
        // check if the current field is foreign key (contains "frk_")
        // re-formatted it before add to query. e.g: "frk_stocktype_name" -->"stocktype_name" --> "stocktype.name"
        String key = (String)view.getTableProperties().get(filterFieldIdx);
        if( key.startsWith("frk_") ){
            key = key.replace("frk_", "");
            key = key.replace("_", ".");
        }

        // remove old <K, V> if exist
        criteriaMap.remove(key);

        // add new <K, V>
        if( value != null && !value.equals("") ){
            criteriaMap.put(key, value);
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
        refreshView();
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

    public void updateEntity(AbstractEntity entity, ACTION action) {
        this.setAction(action);
        updateEntity(entity);
    }

    public void deleteEntity(AbstractEntity entity) {
        doPreDeleteAction(entity);
        model.deleteEntity(entity);
        //generateTable();
        refreshView();
    }

    public void setAction(ACTION action) {
        this.action = action;
    }

    public AbstractEntity getEntityByName(String entityName, String name){
        return model.getEntityByName(entityName, name);
    }

    public String generateEntityCode(String entityName){
        Object obj = model.findLastItem(entityName);
        AbstractEntity lastEntity = (obj != null) ? (AbstractEntity)obj : null;
        String nextIdStr = "" + ( ((lastEntity != null ) ? lastEntity.getId() : 0) + 1 );// + (int)(Math.random()*50000);
        switch(entityName){
        case "Customer":
            return "KH" + nextIdStr;
        case "Material":
            return "VT" + nextIdStr;
        case "Order":
            return "HD" + nextIdStr;
        case "Coupon":
            return "PH" + nextIdStr;
        case "Receivingbill":
            return "PT" + nextIdStr;
        case "Spendingbill":
            return "PC" + nextIdStr;
        }
        return "";
    }

    // abstract functions
    public abstract void updateEntity(AbstractEntity entity);

    // override functions
    public void doPreDeleteAction(AbstractEntity entity){}; //<! Do the stuff before delete entity
    public List<Stocktype> getStockTypeList(){return null;}
    public List<Area> getAreaList(){return null;}
    public List<Customertype1> getCustomerType1List(){return null;}
    public List<Customertype2> getCustomerType2List(){return null;}
    public List<Unit> getUnitList(){return null;}
    public List<Stock> getStockList(){return null;}
    public List<Materialtype1> getMaterialType1List(){return null;}
    public List<Materialtype2> getMaterialType2List(){return null;}

}
