package com.luvsoft.presenter;

import org.vaadin.suggestfield.SuggestField;

import com.luvsoft.presenter.OrderPresenter.CustomerConverter;
import com.luvsoft.presenter.OrderPresenter.MaterialConverter;


public interface OrderListener {
    public void setUpSuggestFieldForCustomer(final SuggestField search, CustomerConverter type);
    public void setUpSuggestFieldForMaterial(final SuggestField search, MaterialConverter type);
}
