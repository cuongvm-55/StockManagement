package com.luvsoft.view.Material;

import java.util.List;

import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialtype1;
import com.luvsoft.entities.Materialtype2;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Unit;
import com.luvsoft.presenter.AbstractEntityPresenter;
import com.luvsoft.utils.ACTION;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class MaterialFromCreator{
    public void createForm(Material entity, AbstractEntityPresenter presenter, ACTION action) {
        MaterialForm<Material> form = new MaterialForm<Material>(presenter, action, entity);

        // Unit
        List<Unit> unitList = presenter.getUnitList();
        if( unitList != null ){
            for(Unit type : unitList){
                form.getUnit().addItem(type);
            }
        }
        if(entity != null){
            form.getUnit().select(entity.getFrk_unit_name());
        }

        // Stock
        List<Stock> stockList = presenter.getStockList();
        if( stockList != null ){
            for(Stock type : stockList){
                form.getStock().addItem(type);
            }
        }
        if(entity != null){
            form.getStock().select(entity.getFrk_stock_name());
        }

        // Material type 1
        List<Materialtype1> cst1List = presenter.getMaterialType1List();
        if( cst1List != null ){
            for(Materialtype1 type : cst1List){
                form.getMaterialtype1().addItem(type);
            }
        }
        if(entity != null){
            form.getMaterialtype1().select(entity.getFrk_materialtype1_name());
        }

        // Material type 2
        List<Materialtype2> cst2List = presenter.getMaterialType2List();
        if( cst2List != null ){
            for(Materialtype2 type : cst2List){
                form.getMaterialtype2().addItem(type);
            }
        }
        if(entity != null){
            form.getMaterialtype2().select(entity.getFrk_materialtype2_name());
        }
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        // auto generate code field if it's empty
        if( entity.getCode().equals("") ){
            entity.setCode(presenter.generateEntityCode(Material.getEntityname()));
        }
        form.setEntity(entity.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();

        form.getUnit().addValueChangeListener(new ValueChangeListener() {
            /**
             * 
             */
            private static final long serialVersionUID = 3864228290609300383L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                form.getEntity().setUnit((Unit)presenter.getEntityByName(Unit.getEntityname(),
                        event.getProperty().getValue().toString()));
            }
        });

        form.getStock().addValueChangeListener(new ValueChangeListener() {
            /**
             * 
             */
            private static final long serialVersionUID = 3864228290609300383L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                form.getEntity().setStock((Stock)presenter.getEntityByName(Stock.getEntityname(),
                        event.getProperty().getValue().toString()));
            }
        });

        form.getMaterialtype1().addValueChangeListener(new ValueChangeListener() {
            /**
             * 
             */
            private static final long serialVersionUID = 3864228290609300383L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                form.getEntity().setMaterialtype1((Materialtype1)presenter.getEntityByName(Materialtype1.getEntityname(),
                        event.getProperty().getValue().toString()));
            }
        });
        form.getMaterialtype2().addValueChangeListener(new ValueChangeListener() {
            /**
             * 
             */
            private static final long serialVersionUID = 3864228290609300383L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                form.getEntity().setMaterialtype2((Materialtype2)presenter.getEntityByName(Materialtype2.getEntityname(),
                        event.getProperty().getValue().toString()));
            }
        });
    }

}
