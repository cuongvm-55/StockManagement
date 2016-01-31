package com.luvsoft.Excel;

public class StockTypeExporter extends EntityExporter{
    public StockTypeExporter(){
        entityAnalyzer = new EntityAnalyzer("com.luvsoft.entities.Stocktype");
        currentColumn = 0;
        currentRow = 0;
    }
}
