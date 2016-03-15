package com.luvsoft.view.StockType;

import java.util.List;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.AbstractEntityPresenter;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;
import com.luvsoft.view.validator.LuvsoftFormBeanValidator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;

public class StockFromCreator {
    public void createForm(Stock stock, AbstractEntityPresenter presenter, ACTION action) {
        LuvsoftAbstractForm<Stock> form = new LuvsoftAbstractForm<Stock>(presenter, action, stock);
        form.createTextField("Mã", Stock.class, "code");
        form.createTextField("Tên", Stock.class, "name");
        form.createTextField("Mô tả", Stock.class, "description");

        ComboBox stockTypeCombx = new ComboBox();
        List<Stocktype> stockTypeList = presenter.getStockTypeList();
        if( stockTypeList != null ){
            for(Stocktype type : stockTypeList){
                stockTypeCombx.addItem(type.getName());
            }
        }
        if(stock != null){
            stockTypeCombx.select(stock.getFrk_stocktype_name());
        }
        stockTypeCombx.setCaption("Loại kho");
        form.getListComponents().add(stockTypeCombx);
        form.getListValidators().add(new LuvsoftFormBeanValidator<AbstractEntity>(Stock.class, "frk_stocktype_name"));
        form.addValidators();

        stockTypeCombx.addValueChangeListener(new ValueChangeListener() {
            /**
             * 
             */
            private static final long serialVersionUID = 3864228290609300383L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                form.getEntity().setStocktype((Stocktype)presenter.getEntityByName(Stocktype.getEntityname(),
                        event.getProperty().getValue().toString()));
            }
        });
        
        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(stock.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();
    }
}
