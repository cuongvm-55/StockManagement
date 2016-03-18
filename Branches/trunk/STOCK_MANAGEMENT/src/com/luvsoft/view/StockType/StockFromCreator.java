package com.luvsoft.view.StockType;

import java.util.List;

import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.presenter.AbstractEntityPresenter;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class StockFromCreator {
    public void createForm(Stock stock, AbstractEntityPresenter presenter, ACTION action) {
        LuvsoftAbstractForm<Stock> form = new LuvsoftAbstractForm<Stock>(presenter, action, stock);
        form.createTextField("Mã", Stock.class, "code");
        form.createTextField("Tên", Stock.class, "name");
        form.createTextField("Mô tả", Stock.class, "description");
        form.createTextField("Loại Kho", Stock.class, "stocktype");

        List<Stocktype> stockTypeList = presenter.getStockTypeList();
        if( stockTypeList != null ){
            for(Stocktype type : stockTypeList){
                form.getStocktype().addItem(type);
            }
        }
        if(stock != null){
            form.getStocktype().select(stock.getFrk_stocktype_name());
        }

        form.addValidators();

        // Because Java use reference type, so we clone this object and work on copy entity to be sure
        // that there are no stupid modifications on original object
        form.setEntity(stock.cloneObject());
        form.openInModalPopup();
        form.handleSaveAndResetOperations();

        form.getStocktype().addValueChangeListener(new ValueChangeListener() {
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
    }
}
