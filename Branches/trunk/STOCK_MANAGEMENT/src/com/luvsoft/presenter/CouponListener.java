package com.luvsoft.presenter;

import org.vaadin.suggestfield.SuggestField;

import com.luvsoft.presenter.CouponPresenter.CustomerConverter;
import com.luvsoft.presenter.CouponPresenter.MaterialConverter;

public interface CouponListener {
    public void setUpSuggestFieldForCustomer(final SuggestField search, CustomerConverter type);
    public void setUpSuggestFieldForMaterial(final SuggestField search, MaterialConverter type);
}
