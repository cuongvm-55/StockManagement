package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Unit;

public class MaterialModel extends AbstractEntityModel {

    @Override
    public String getEntityname() {
        return Material.getEntityname();
    }

    @Override
    public List<AbstractEntity> getFilterData(FilterObject filterObject){
        List<AbstractEntity> entityList = new ArrayList<AbstractEntity>();
        List<Object> objectlist = entityManager.searchWithCriteriaWithPagination(getEntityname(), filterObject);

        for (Object object : objectlist) {
            Material entity = (Material) object;
            entity.verifyObject();
            entityList.add(entity);
        }
        return entityList;
    }

    @Override
    public List<Unit> getUnitList(){
        List<Unit> list = new ArrayList<Unit>();
        List<Object> objectlist = entityManager.findAll(Unit.getEntityname());

        for (Object object : objectlist) {
            Unit entity = (Unit) object;
            entity.verifyObject();
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<Stock> getStockList(){
        List<Stock> list = new ArrayList<Stock>();
        List<Object> objectlist = entityManager.findAll(Stock.getEntityname());

        for (Object object : objectlist) {
            Stock entity = (Stock) object;
            entity.verifyObject();
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<Materialtype1> getMaterialType1List(){
        List<Materialtype1> list = new ArrayList<Materialtype1>();
        List<Object> objectlist = entityManager.findAll(Materialtype1.getEntityname());

        for (Object object : objectlist) {
            Materialtype1 entity = (Materialtype1) object;
            entity.verifyObject();
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<Materialtype2> getMaterialType2List(){
        List<Materialtype2> list = new ArrayList<Materialtype2>();
        List<Object> objectlist = entityManager.findAll(Materialtype2.getEntityname());

        for (Object object : objectlist) {
            Materialtype2 entity = (Materialtype2) object;
            entity.verifyObject();
            list.add(entity);
        }
        return list;
    }

    /**
     * 
     * @param newQuantity
     * @param material
     */
    public void updateQuantityInStock(int newQuantity, Material material) {
        if(newQuantity >= 0) {
            material.setQuantity(newQuantity);
        }
        entityManager.update(material);
    }
}
