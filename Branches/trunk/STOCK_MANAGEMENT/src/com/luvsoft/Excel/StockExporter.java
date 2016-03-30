package com.luvsoft.Excel;

import java.util.LinkedHashMap;
import java.util.Map;

public class StockExporter extends EntityExporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Stock";
    }
    public StockExporter(String destFolder){
        this.destFolder = destFolder;
    }

    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new LinkedHashMap<String, String>(){{
            put("code","Mã");
            put("name","Tên");
            put("description","Mô tả");
            put("stocktype","Loại kho");
        }};
    }
}
