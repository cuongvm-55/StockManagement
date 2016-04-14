package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Unit;

public class UnitModel extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> unitList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Unit stocktype = (Unit) object;
            stocktype.verifyObject();
            unitList.add(stocktype);
        }

        return unitList;
    }

    @Override
    public String getEntityname(){
        return Unit.getEntityname();
    }
    
    @Override
    public List<Material> getMaterialListByUnitName(String unitName){
        List<Material> mtList = new ArrayList<Material>();
        List<Object> params = new ArrayList<Object>();
        params.add(unitName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Material.getEntityname() + " e WHERE unit.name LIKE :var0", params);

        for (Object object : objectlist) {
            Material mt = (Material) object;
            mt.verifyObject();
            mtList.add(mt);
        }
        return mtList;
    }
}
