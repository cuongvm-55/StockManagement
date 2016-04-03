package com.luvsoft.presenter;

import java.util.HashMap;
import java.util.List;

import com.luvsoft.DAO.UnitModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Unit;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.Material.UnitView;

public class UnitPresenter extends AbstractEntityPresenter {
    public UnitPresenter(UnitView view) {
        this.view = view;
        model = new UnitModel();
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

    @Override
    public void doPreDeleteAction(AbstractEntity entity){
        Unit unit = (Unit)entity;
        if( unit.getName() == null){
            return; // nothing to do
        }
        List<Material> uList = model.getMaterialListByUnitName(unit.getName());
        if( uList != null ){
            for( int idx = 0; idx < uList.size(); idx++ ){
                Material stk = uList.get(idx);
                stk.setUnit(null);
                model.update(stk); // update it in db
            }
        }
    }
}
