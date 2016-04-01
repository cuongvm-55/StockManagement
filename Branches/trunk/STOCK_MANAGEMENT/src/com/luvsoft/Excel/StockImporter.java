package com.luvsoft.Excel;

import java.util.LinkedHashMap;
import java.util.Map;


public class StockImporter extends EntityImporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Stock";
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
