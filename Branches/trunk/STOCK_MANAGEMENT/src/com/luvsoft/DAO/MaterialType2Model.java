package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype2;

public class MaterialType2Model extends AbstractEntityModel{
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> entityList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Materialtype2 entity = (Materialtype2) object;
            entity.verifyObject();
            entityList.add(entity);
        }

        return entityList;
    }

    @Override
    public String getEntityname(){
        return Materialtype2.getEntityname();
    }
    
    @Override
    public List<Material> getMaterialListByMaterialType2Name(String typeName){
        List<Material> entityList = new ArrayList<Material>();
        List<Object> params = new ArrayList<Object>();
        params.add(typeName);
        List<Object> objectlist = entityManager.findByQuery("SELECT e FROM " + Material.getEntityname() + " e WHERE materialtype2.name LIKE :var0", params);

        for (Object object : objectlist) {
            Material entity = (Material) object;
            entity.verifyObject();
            entityList.add(entity);
        }
        return entityList;
    }
}
