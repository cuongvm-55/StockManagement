package com.luvsoft.presenter;

import java.util.HashMap;
import java.util.List;

import com.luvsoft.DAO.MaterialModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Unit;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Material.MaterialView;

public class MaterialPresenter extends AbstractEntityPresenter {
    public MaterialPresenter(MaterialView view) {
        this.view = view;
        model = new MaterialModel();
        criteriaMap = new HashMap<String, String>();
        action = ACTION.UNKNOWN;
    }

    public void updateEntity(AbstractEntity entity) {
        Material material = (Material)entity;
        material.setUnit((Unit)model.getEntityByName(Unit.getEntityname(), material.getFrk_unit_name()));
        material.setStock((Stock)model.getEntityByName(Stock.getEntityname(), material.getFrk_stock_name()));
        material.setMaterialtype1((Materialtype1)model.getEntityByName(Materialtype1.getEntityname(), material.getFrk_materialtype1_name()));
        material.setMaterialtype2((Materialtype2)model.getEntityByName(Materialtype2.getEntityname(), material.getFrk_materialtype2_name()));
        if(action.equals(ACTION.CREATE)) {
            model.addNew(material);
            goToLastPage();
        }
        else if(action.equals(ACTION.UPDATE)) {
            model.update(material);
            generateTable();
        } else if(action.equals(ACTION.UPDATE_BY_TABLE_EDITOR)) {
            model.update(material);
            generateTable();
        }
        this.action = ACTION.UNKNOWN; // Reset action state after modification to avoid duplicate action in the future
    }

    @Override
    public List<Unit> getUnitList(){
        return model.getUnitList();
    }

    @Override
    public List<Materialtype1> getMaterialType1List(){
        return model.getMaterialType1List();
    }

    @Override
    public List<Materialtype2> getMaterialType2List(){
        return model.getMaterialType2List();
    }

    @Override
    public List<Stock> getStockList(){
        return model.getStockList();
    }
}