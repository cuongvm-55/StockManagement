package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

public class FilterObject {
    private List<String> fieldList;
    private String criteria;
    private int pageIndex;
    private int numberOfRecordsPerPage;

    public FilterObject(){
        fieldList = new ArrayList<String>();
        criteria = "";
        pageIndex = 0;
        numberOfRecordsPerPage = 0;
    }

    public FilterObject(List<String> fieldList, String criteria, int pageIndex,
            int numberOfRecordsPerPage) {
        super();
        this.fieldList = fieldList;
        this.criteria = criteria;
        this.pageIndex = pageIndex;
        this.numberOfRecordsPerPage = numberOfRecordsPerPage;
    }
    
    public List<String> getFieldList() {
        return fieldList;
    }
    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }
    public String getCriteria() {
        return criteria;
    }
    public void setCriteria(String criteria) {
        this.criteria = criteria;
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
}
