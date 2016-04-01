package com.luvsoft.Excel;

import java.util.LinkedHashMap;
import java.util.Map;


public class StockTypeImporter extends EntityImporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Stocktype";
    }

    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new LinkedHashMap<String, String>(){{
            put("name","Tên");
            put("description","Mô tả");
        }};
    }

}
