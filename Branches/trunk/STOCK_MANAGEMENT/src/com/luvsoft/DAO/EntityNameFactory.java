package com.luvsoft.DAO;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Area;
import com.luvsoft.entities.Coupontype;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype1;
import com.luvsoft.entities.Customertype2;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.entities.Ordertype;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.entities.Unit;

public class EntityNameFactory {

    public static <T extends AbstractEntity> String createEntityName(Class<T> classType) {

        if(classType == Stocktype.class) {
            return Stocktype.getEntityname();
        }

        if(classType == Stock.class) {
            return Stock.getEntityname();
        }

        if(classType == Area.class) {
            return Area.getEntityname();
        }

        if(classType == Coupontype.class) {
            return Coupontype.getEntityname();
        }

        if(classType == Customertype1.class) {
            return Customertype1.getEntityname();
        }

        if(classType == Customertype2.class) {
            return Customertype2.getEntityname();
        }

        if(classType == Unit.class) {
            return Unit.getEntityname();
        }

        if(classType == Materialtype1.class) {
            return Materialtype1.getEntityname();
        }

        if(classType == Materialtype2.class) {
            return Materialtype2.getEntityname();
        }

        if(classType == Ordertype.class) {
            return Ordertype.getEntityname();
        }

        if(classType == Customer.class) {
            return Customer.getEntityname();
        }

        return "";
    }
}
