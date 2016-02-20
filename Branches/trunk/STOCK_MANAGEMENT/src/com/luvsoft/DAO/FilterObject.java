package com.luvsoft.DAO;

import java.util.HashMap;
import java.util.Map;

public class FilterObject {
    private Map<String, String> criteria; //<! <FieldName, FieldValue>
    private int pageIndex;
    private int numberOfRecordsPerPage;

    public FilterObject(){
        criteria = new HashMap<String, String>();
        pageIndex = 0;
        numberOfRecordsPerPage = 0;
    }

    public FilterObject(Map<String, String> criteria, int pageIndex,
            int numberOfRecordsPerPage) {
        super();
        this.criteria = criteria;
        this.pageIndex = pageIndex;
        this.numberOfRecordsPerPage = numberOfRecordsPerPage;
    }

    public int getPageIndex() {
        return pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    public int getNumberOfRecordsPerPage() {
        return numberOfRecordsPerPage;
    }
    public void setNumberOfRecordsPerPage(int numberOfRecordsPerPage) {
        this.numberOfRecordsPerPage = numberOfRecordsPerPage;
    }

    public Map<String, String> getCriteria() {
        return criteria;
    }

    public void setCriteria(Map<String, String> criteria) {
        this.criteria = criteria;
    }
}
