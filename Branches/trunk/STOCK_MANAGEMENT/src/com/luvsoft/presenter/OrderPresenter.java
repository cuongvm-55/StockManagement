package com.luvsoft.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.luvsoft.DAO.CustomerModel;
import com.luvsoft.entities.Customer;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class OrderPresenter implements OrderListener {

    CustomerModel model = new CustomerModel();

    @Override
    public CollectionSuggestionProvider getSuggestionProvider() {
        List<Object> listCustomers = model.getCustomers();

        Collection<String> providerCollection = new ArrayList<String>();
        for (Object customer : listCustomers) {
            providerCollection.add(((Customer)customer).getPhoneNumber());
        }

        CollectionSuggestionProvider provider = new CollectionSuggestionProvider(providerCollection, MatchMode.CONTAINS, true) {
            @Override
            public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
                Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);

                for (AutocompleteSuggestion suggestion : suggestions) {
                    String previousString = "";
                    for (Object object : listCustomers) {
                        String phoneNumber = ((Customer) object).getPhoneNumber();
                        if(suggestion.getValue().equals(phoneNumber)) {
                            if(!phoneNumber.equals(previousString)) {
                                suggestion.setDescription(((Customer) object).getName());
                            }
                            previousString = suggestion.getValue();
                        }
                    }
                }
                return suggestions;
            }
        };
        return provider;
    }

}
