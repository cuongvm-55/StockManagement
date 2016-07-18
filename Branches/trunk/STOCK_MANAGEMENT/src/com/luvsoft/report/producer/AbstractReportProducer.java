package com.luvsoft.report.producer;

import java.util.Date;
import java.util.List;

import com.luvsoft.DAO.FilterObject;
import com.luvsoft.report.view.AbstractReportView;
import com.vaadin.ui.TextField;

public abstract class AbstractReportProducer {
    @SuppressWarnings("rawtypes")
    protected AbstractReportView view;

    //protected Map<String, String> criteriaMap;
    protected FilterObject filterObject;

    protected List<Object> dataList;

    protected Date fromDate;
    protected Date toDate;

    AbstractReportProducer(){
        filterObject = new FilterObject();
    }

    /**
     * Do the filter when value of one of TextField in textFields list changed
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void doFilter(String key, String value){
        // check if the current field is foreign key (contains "frk_")
        // re-formatted it before add to query. e.g: "frk_stocktype_name" -->"stocktype_name" --> "stocktype.name"
        if( key.startsWith("frk_") ){
            key = key.replace("frk_", "");
            key = key.replace("_", ".");
        }
        // remove old <K, V> if exist
        filterObject.criteria.remove(key);
        // add new <K, V>
        if( value != null && !value.equals("") ){
            filterObject.criteria.put(key, value);
        }

        // Generate report
        if( fromDate != null && toDate!= null && !toDate.before(fromDate) ){
            view.getSumDataList().clear();
            view.setTable(getFilterStatistic(fromDate, toDate, filterObject));
        }
    }

    private void clearFilterCriteria(){
        for( Object filter : view.getFilterFields() ){
            TextField tf = (TextField)filter;
            tf.clear();
        }
        filterObject.criteria.clear();
    }

    /**
     * Generate statistic report in date range [from, to]
     * @param from
     * @param to
     */
    @SuppressWarnings("unchecked")
    public void generateReport(Date from, Date to){
        System.out.println("AbstractReportProducer::generateReport()");
        // Save those dates for later use
        fromDate = from;
        toDate   = to;

        clearFilterCriteria();
        view.getSumDataList().clear();

        // Update view
        view.setTable(getFilterStatistic(fromDate, toDate, filterObject));
    }

    public abstract List<Object> getFilterStatistic(Date from, Date to, FilterObject fo);

}
