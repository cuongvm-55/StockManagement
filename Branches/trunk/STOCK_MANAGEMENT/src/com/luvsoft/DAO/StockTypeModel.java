package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.Stocktype;

public class StockTypeModel {

    public List<Stocktype> getData() {
        List<Stocktype> data = new ArrayList<Stocktype>();
        for(int i = 0; i<20; i++) {
            Stocktype entity = new Stocktype();
            entity.setId(i);
            entity.setName("Name " + i);
            entity.setDescription("Description " + i);

            data.add(entity);
        }
        return data;
    }
}
