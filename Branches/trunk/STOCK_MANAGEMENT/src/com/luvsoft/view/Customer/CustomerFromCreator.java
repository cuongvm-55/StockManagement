package com.luvsoft.view.Customer;

import java.util.List;

import com.luvsoft.entities.Area;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Customertype1;
import com.luvsoft.entities.Customertype2;
import com.luvsoft.presenter.AbstractEntityPresenter;
import com.luvsoft.utils.ACTION;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class CustomerFromCreator{
    public void createForm(Customer entity, AbstractEntityPresenter presenter, ACTION action) {
        CustomerForm<Customer> form = new CustomerForm<Customer>(presenter, action, entity);

        // Area
        List<Area> areaList = presenter.getAreaList();
        if( areaList != null ){
            for(Area type : areaList){
                form.getArea().addItem(type);
            }
        }
        if(entity != null){
            form.getArea().select(entity.getFrk_area_name());
        }

        // Customer type 1
        List<Customertype1> cst1List = presenter.getCustomerType1List();
        if( cst1List != null ){
            for(Customertype1 type : cst1List){
                form.getCustomertype1().addItem(type);
            }
        }
        if(entity != null){
            form.getCustomertype1().select(entity.getFrk_customertype1_name());
        }

        // Customer type 2
        List<Customertype2> cst2List = presenter.getCustomerType2List();
        if( cst2List != null ){
            for(Customertype2 type : cst2List){
                form.getCustomertype2().addItem(type);
            }
        }
        if(entity != null){
            form.getCustomertype2().select(entity.getFrk_customertype2_name());
        }
        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(entity.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();

        form.getArea().addValueChangeListener(new ValueChangeListener() {
            /**
             * 
             */
            private static final long serialVersionUID = 3864228290609300383L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                form.getEntity().setArea((Area)presenter.getEntityByName(Area.getEntityname(),
                        event.getProperty().getValue().toString()));
            }
        });

        form.getCustomertype1().addValueChangeListener(new ValueChangeListener() {
            /**
             * 
             */
            private static final long serialVersionUID = 3864228290609300383L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                form.getEntity().setCustomertype1((Customertype1)presenter.getEntityByName(Customertype1.getEntityname(),
                        event.getProperty().getValue().toString()));
            }
        });
        form.getCustomertype2().addValueChangeListener(new ValueChangeListener() {
            /**
             * 
             */
            private static final long serialVersionUID = 3864228290609300383L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                form.getEntity().setCustomertype2((Customertype2)presenter.getEntityByName(Customertype2.getEntityname(),
                        event.getProperty().getValue().toString()));
            }
        });
    }

}
