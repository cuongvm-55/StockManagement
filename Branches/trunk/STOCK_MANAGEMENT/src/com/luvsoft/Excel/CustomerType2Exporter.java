package com.luvsoft.Excel;

import java.util.HashMap;
import java.util.Map;

public class CustomerType2Exporter extends EntityExporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Customertype2";
    }
    public CustomerType2Exporter(String destFolder){
        this.destFolder = destFolder;
    }
    
    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new HashMap<String, String>(){{
            put("name","Tên");
            put("description","Mô tả");
        }};
    }
}
