package com.luvsoft.presenter;

public interface StockTypeListener {

    public void generateTable();
    public void goToPage(int number);
    public void goToFirstPage();
    public void goToLastPage();
    public void goToNextPage();
    public void goToPreviousPage();
}
