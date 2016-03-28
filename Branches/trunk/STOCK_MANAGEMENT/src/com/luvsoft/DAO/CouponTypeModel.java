package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Coupontype;

public class CouponTypeModel extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> stockTypeList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Coupontype coupontype = (Coupontype) object;
            coupontype.verifyObject();
            stockTypeList.add(coupontype);
        }

        return stockTypeList;
    }

    @Override
    public String getEntityname(){
        return Coupontype.getEntityname();
    }
    
    @Override
    public List<Coupon> getCouponListByCouponTypeName(String couponTypeName){
        List<Coupon> couponList = new ArrayList<Coupon>();
        List<String> params = new ArrayList<String>();
        params.add(couponTypeName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Coupon.getEntityname() + " e WHERE coupontype.name LIKE :var0", params);

        for (Object object : objectlist) {
            Coupon coupon = (Coupon) object;
            coupon.verifyObject();
            couponList.add(coupon);
        }
        return couponList;
    }
}
