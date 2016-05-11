package com.luvsoft.DAO;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NoResultException;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Coupon;

public class CouponModel extends AbstractEntityModel implements Serializable {
    private static final long serialVersionUID = -3858266909300389426L;

    public Coupon findLastItem() {
        try {
            Coupon coupon = (Coupon) findLastItem(getEntityname());
            if(coupon == null) {
                coupon = new Coupon();
            } else {
                coupon.verifyObject();
            }
            return coupon;
        } catch (NoResultException e) {
            return new Coupon();
        }
    }

    @Override
    public boolean addNewByPersist(AbstractEntity coupon) {
        return entityManager.addNewByPersist(coupon);
    }

    @Override
    public boolean isCouponExisted(Coupon coupon) {
        if(entityManager.findEntityByProperty(getEntityname(), "code", coupon.getCode()).isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String getEntityname() {
        return Coupon.getEntityname();
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject) {
        // TODO Auto-generated method stub
        return null;
    }

}
