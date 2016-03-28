package com.luvsoft.Excel;

import java.util.HashMap;
import java.util.Map;


public class MaterialType2Importer extends EntityImporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Materialtype2";
    }

    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new HashMap<String, String>(){{
            put("name","Tên");
            put("description","Mô tả");
        }};
    }

}
