package com.luvsoft.DAO;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;

public class EntityNameFactory {

    public static <T extends AbstractEntity> String createEntityName(Class<T> classType) {
        if(classType == Stocktype.class) {
            return Stocktype.getEntityname();
        }

        if(classType == Stock.class) {
            return Stock.getEntityname();
        }

        return "";
    }
}
