package com.luvsoft.Excel;

public class StockTypeExporter extends EntityExporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Stocktype";
    }
    public StockTypeExporter(String destFolder){
        this.destFolder = destFolder;
    }
}
