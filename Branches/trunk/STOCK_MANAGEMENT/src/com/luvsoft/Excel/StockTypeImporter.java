package com.luvsoft.Excel;

import java.io.InputStream;

public class StockTypeImporter extends EntityImporter{

    public StockTypeImporter(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public String getEntityFullPathName() {
        return "com.luvsoft.entities.Stocktype";
    }

}
