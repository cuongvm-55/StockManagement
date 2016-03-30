package com.luvsoft.Excel;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomerExporter extends EntityExporter{
    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Customer";
    }
    public CustomerExporter(String destFolder){
        this.destFolder = destFolder;
    }

    @SuppressWarnings("serial")
    public Map<String, String> getFieldList(){
        return new LinkedHashMap<String, String>(){{
            put("code","Mã");
            put("name","Tên");
            put("address","Địa Chỉ");
            put("phoneNumber","Số ĐT");
            put("email","Email");
            put("bankName","Tên Ngân Hàng");
            put("bankAccount","TK Ngân Hàng");
            put("debt","Nợ");
            put("area","Khu Vực");
            put("customertype1","Loại KH 1");
            put("customertype2","Loại KH 2");
        }};
    }
}
