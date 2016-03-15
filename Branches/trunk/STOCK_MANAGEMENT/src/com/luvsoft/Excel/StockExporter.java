package com.luvsoft.Excel;

import java.util.HashMap;
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
        return new HashMap<String, String>(){{
            put("code","Mã");
            put("name","Tên");
            put("description","Mô tả");
            put("stocktype","Loại kho");
        }};
    }
}
