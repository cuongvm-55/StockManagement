package com.luvsoft.Excel;

import java.util.LinkedHashMap;
import java.util.Map;

public class StockTypeExporter extends EntityExporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Stocktype";
    }
    public StockTypeExporter(String destFolder){
        this.destFolder = destFolder;
    }
    
    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new LinkedHashMap<String, String>(){{
            put("name","Tên");
            put("description","Mô tả");
        }};
    }
}
