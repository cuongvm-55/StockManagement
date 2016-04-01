package com.luvsoft.Excel;

import java.util.LinkedHashMap;
import java.util.Map;


public class AreaImporter extends EntityImporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Area";
    }

    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new LinkedHashMap<String, String>(){{
            put("name","Tên");
            put("description","Mô tả");
        }};
    }

}
