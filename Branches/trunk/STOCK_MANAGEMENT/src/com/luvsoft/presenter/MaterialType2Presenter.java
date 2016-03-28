package com.luvsoft.presenter;

import java.util.HashMap;
import java.util.List;

import com.luvsoft.DAO.MaterialType2Model;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Material.MaterialType2View;

public class MaterialType2Presenter extends AbstractEntityPresenter {
    public MaterialType2Presenter(MaterialType2View view) {
        this.view = view;
        model = new MaterialType2Model();
        criteriaMap = new HashMap<String, String>();
        action = ACTION.UNKNOWN;
    }
    
    public void updateEntity(AbstractEntity entity) {
        if(action.equals(ACTION.CREATE)) {
            model.addNew(entity);
            goToLastPage();
        }
        else if(action.equals(ACTION.UPDATE)) {
            model.update(entity);
            generateTable();
        } else if(action.equals(ACTION.UPDATE_BY_TABLE_EDITOR)) {
            model.update(entity);
            generateTable();
        }
        this.action = ACTION.UNKNOWN; // Reset action state after modification to avoid duplicate action in the future
    }

    /**
     * Set type of all entities that belong to the to-be-deleted type to null
     */
    @Override
    public void doPreDeleteAction(AbstractEntity entity){
        Materialtype2 type = (Materialtype2)entity;
        if( type.getName() == null){
            return; // nothing to do
        }
        List<Material> list = model.getMaterialListByMaterialType2Name(type.getName());
        if( list != null ){
            for( int idx = 0; idx < list.size(); idx++ ){
                Material stk = list.get(idx);
                stk.setMaterialtype1(null);
                model.update(stk); // update it in db
            }
        }
    }
}
