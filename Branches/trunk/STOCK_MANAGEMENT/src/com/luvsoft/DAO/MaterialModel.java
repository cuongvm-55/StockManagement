package com.luvsoft.DAO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialinputhistory;
import com.luvsoft.entities.Materialoutputhistory;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Unit;

public class MaterialModel extends AbstractEntityModel implements Serializable{
    private static final long serialVersionUID = -5055983447976696596L;

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

    /**
     * This function is used to update output history table for material when update quantity of any material
     * @param outputQuantity   ->   quantity needed to increase
     * @param outputPrice      ->   new price of material
     * @param material
     */
    public void updateMaterialOutputHistory(int outputQuantity, BigDecimal outputPrice, Material material) {
        // Check this material is existed in history table today or not?
        Date today = new Date();
        String query = "SELECT e FROM " + Materialoutputhistory.getEntityname() + " e WHERE e.date =:var0 AND material.id =:var1";
        List<Object> listParameters = new ArrayList<Object>();
        listParameters.add(today);
        listParameters.add(material.getId());
        List<Object> listResult = entityManager.findByQuery(query, listParameters);

        if(listResult.isEmpty()) {
            // There is no material existed in history table today
            // Create new field in history table
            Materialoutputhistory outputHistory = new Materialoutputhistory();
            outputHistory.setDate(today);
            outputHistory.setMaterial(material);
            outputHistory.setOutputPrice(outputPrice);
            outputHistory.setQuantity(outputQuantity);
            entityManager.addNew(outputHistory);
        } else {
            // There is at least one material existed in history table today
            Materialoutputhistory outputHistory = (Materialoutputhistory) listResult.get(0);
            outputHistory.setQuantity(outputHistory.getQuantity() + outputQuantity);
            outputHistory.setOutputPrice(outputPrice);
            entityManager.update(outputHistory);
        }
    }

    /**
     * This function is used to update input history table for material when update quantity of any material
     * @param inputQuantity   ->   quantity needed to increase
     * @param inputPrice      ->   new price of material
     * @param material
     */
    public void updateMaterialInputHistory(int inputQuantity, BigDecimal inputPrice, Material material) {
        // Check this material is existed in history table today or not?
        Date today = new Date();
        String query = "SELECT e FROM " + Materialinputhistory.getEntityname() + " e WHERE e.date =:var0 AND material.id =:var1";
        List<Object> listParameters = new ArrayList<Object>();
        listParameters.add(today);
        listParameters.add(material.getId());
        List<Object> listResult = entityManager.findByQuery(query, listParameters);

        if(listResult.isEmpty()) {
            // There is no material existed in history table today
            // Create new field in history table
            Materialinputhistory inputHistory = new Materialinputhistory();
            inputHistory.setDate(today);
            inputHistory.setMaterial(material);
            inputHistory.setInputPrice(inputPrice);
            inputHistory.setQuantity(inputQuantity);
            entityManager.addNew(inputHistory);
        } else {
            // There is at least one material existed in history table today
            Materialinputhistory inputHistory = (Materialinputhistory) listResult.get(0);
            inputHistory.setQuantity(inputHistory.getQuantity() + inputQuantity);
            inputHistory.setInputPrice(inputPrice);
            entityManager.update(inputHistory);
        }
    }
}
