package com.luvsoft.presenter;

import com.vaadin.client.ui.VFilterSelect.SuggestionPopup;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;


public interface OrderListener {
    public AutocompleteSuggestionProvider getSuggestionProvider();
}
