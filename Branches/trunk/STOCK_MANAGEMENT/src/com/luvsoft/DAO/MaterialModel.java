package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;

public class MaterialModel extends AbstractEntityModel {

    public List<Object> getMaterials() {
        return entityManager.findAll(getEntityname());
    }

    @Override
    public String getEntityname() {
        return Material.getEntityname();
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject) {
        List<AbstractEntity> materialList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Material material = (Material) object;
            material.verifyObject();
            materialList.add(material);
        }
        return materialList;
    }

}
