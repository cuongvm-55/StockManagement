package com.luvsoft.Excel;

import java.util.HashMap;
import java.util.Map;


public class OrderTypeImporter extends EntityImporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Ordertype";
    }

    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new HashMap<String, String>(){{
            put("name","Tên");
            put("description","Mô tả");
        }};
    }

}
