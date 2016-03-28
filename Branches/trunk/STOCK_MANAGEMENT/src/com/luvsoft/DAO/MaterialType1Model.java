package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype1;

public class MaterialType1Model extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> entityList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Materialtype1 entity = (Materialtype1) object;
            entity.verifyObject();
            entityList.add(entity);
        }

        return entityList;
    }

    @Override
    public String getEntityname(){
        return Materialtype1.getEntityname();
    }
    
    @Override
    public List<Material> getMaterialListByMaterialType1Name(String couponTypeName){
        List<Material> entityList = new ArrayList<Material>();
        List<String> params = new ArrayList<String>();
        params.add(couponTypeName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Material.getEntityname() + " e WHERE materialtype1.name LIKE :var0", params);

        for (Object object : objectlist) {
            Material entity = (Material) object;
            entity.verifyObject();
            entityList.add(entity);
        }
        return entityList;
    }
}
