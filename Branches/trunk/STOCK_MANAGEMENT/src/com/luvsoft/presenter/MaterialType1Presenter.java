package com.luvsoft.presenter;

import java.util.List;

import com.luvsoft.DAO.MaterialType1Model;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Material.MaterialType1View;

public class MaterialType1Presenter extends AbstractEntityPresenter {
    public MaterialType1Presenter(MaterialType1View view) {
        this.view = view;
        model = new MaterialType1Model();
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
        Materialtype1 type = (Materialtype1)entity;
        if( type.getName() == null){
            return; // nothing to do
        }
        List<Material> list = model.getMaterialListByMaterialType1Name(type.getName());
        if( list != null ){
            for( int idx = 0; idx < list.size(); idx++ ){
                Material stk = list.get(idx);
                stk.setMaterialtype1(null);
                model.update(stk); // update it in db
            }
        }
    }
}
