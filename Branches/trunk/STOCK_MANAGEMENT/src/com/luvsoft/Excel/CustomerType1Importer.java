package com.luvsoft.Excel;

import java.util.HashMap;
import java.util.Map;


public class CustomerType1Importer extends EntityImporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Customertype2";
    }

    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new HashMap<String, String>(){{
            put("name","Tên");
            put("description","Mô tả");
        }};
    }

}
