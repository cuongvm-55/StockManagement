package com.luvsoft.presenter;

import org.vaadin.suggestfield.SuggestField;

import com.luvsoft.presenter.OrderPresenter.CustomerConverter;


public interface OrderListener {
    public void setUpSuggestFieldForCustomer(final SuggestField search, CustomerConverter type);
    public void setUpSuggestFieldForOrderDetail(final SuggestField search);
}
