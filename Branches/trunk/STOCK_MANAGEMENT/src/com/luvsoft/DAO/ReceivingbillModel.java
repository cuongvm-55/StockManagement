package com.luvsoft.DAO;

import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Receivingbill;

public class ReceivingbillModel extends AbstractEntityModel {

    @Override
    public String getEntityname() {
        return Receivingbill.getEntityname();
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject) {
        // TODO Auto-generated method stub
        return null;
    }
}
