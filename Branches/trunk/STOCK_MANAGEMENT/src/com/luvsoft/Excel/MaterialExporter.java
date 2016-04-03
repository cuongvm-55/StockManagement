package com.luvsoft.Excel;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaterialExporter extends EntityExporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Material";
    }
    public MaterialExporter(String destFolder){
        this.destFolder = destFolder;
    }

    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new LinkedHashMap<String, String>(){{
            put("code","Mã");
            put("name","Tên");
            put("quantity","Số Lượng");
            put("unit","Đv Tính");
            put("price","Giá");
            put("materialtype1","Loại Vật Tư 1");
            put("materialtype2","Loại Vật Tư 2");
            put("stock","Kho");
            put("description","Mô Tả");
        }};
    }
}
