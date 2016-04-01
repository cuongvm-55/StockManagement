package com.luvsoft.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.SuggestField.SuggestionHandler;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import com.luvsoft.DAO.AbstractEntityModel;
import com.luvsoft.DAO.CustomerModel;
import com.luvsoft.DAO.FilterObject;
import com.luvsoft.DAO.MaterialModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Material;
import com.luvsoft.utils.ACTION;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class OrderPresenter extends AbstractEntityPresenter implements OrderListener {

    public enum CustomerConverter {
        BY_CODE, BY_NAME, BY_PHONE_NUMBER,
    }

    public enum MaterialConverter {
        BY_CODE, BY_NAME
    }

    CustomerModel customerModel = new CustomerModel();
    MaterialModel materialModel = new MaterialModel();

    private List<Object> listCustomers = new ArrayList<Object>();
    private List<Object> listMaterials = new ArrayList<Object>();

    public OrderPresenter() {
        // listCustomers = model.getCustomers();
        // listMaterials = materialModel.getMaterials();
        criteriaMap = new HashMap<String, String>();
    }

    public List<Object> doFilter(String foreignKey, String value, AbstractEntityModel model){
        // check if the current field is foreign key (contains "frk_")
        // re-formatted it before add to query. e.g: "frk_stocktype_name" -->"stocktype_name" --> "stocktype.name"
        String key = foreignKey;
        if( key.startsWith("frk_") ){
            key = key.replace("frk_", "");
            key = key.replace("_", ".");
        }

        // remove old <K, V> if exist
        criteriaMap.remove(key);

        // add new <K, V>
        if( value != null && !value.equals("") ){
            criteriaMap.put(key, value);
        }

        FilterObject filterObject = new FilterObject(
                criteriaMap,
                0,
                100000000);

        // Due to a possible bug in Vaadin (table cannot setRows by itself when we update table by table editor)
        // we consider to work around it by separating UPDATE_BY_TABLE_EDITOR like a specific case
        List<AbstractEntity> listEntity = model.getFilterData(filterObject);
        List<Object> listObject = new ArrayList<Object>();
        for (AbstractEntity abstractEntity : listEntity) {
            listObject.add(abstractEntity);
        }
        return listObject;
    }

    @Override
    public void setUpSuggestFieldForOrderDetail(SuggestField search) {
        search.setSuggestionConverter(new MaterialSuggestionConverterByCode());
        search.setSuggestionHandler(new SuggestionHandler() {
            @Override
            public List<Object> searchItems(String query) {
                listMaterials = doFilter("code", query, materialModel);
                return listMaterials;
            }
        });
    }

    private class MaterialSuggestionConverterByCode extends BeanSuggestionConverter {

        public MaterialSuggestionConverterByCode() {
            super(Customer.class, "id", "code", "code");
        }

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item == null) {
                return new SuggestFieldSuggestion("-1", "", "");
            } else {
                Material material = (Material) item;
                return new SuggestFieldSuggestion(material.getId().toString(), material.getCode() + " " + material.getName(),
                        "");
            }
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            Material result = new Material();
            for (Object object : listMaterials) {
                Material bean = (Material) object;
                if (bean.getId().toString().equals(suggestion.getId())) {
                    result = bean;
                    break;
                }
            }
            return result;
        }

    }

    @Override
    public void setUpSuggestFieldForCustomer(SuggestField search, CustomerConverter type) {
        search.setSuggestionConverter(createConverter(type));
        search.setSuggestionHandler(new SuggestionHandler() {
            @Override
            public List<Object> searchItems(String query) {
                switch (type) {
                    case BY_CODE:
                        listCustomers = doFilter("code", query, customerModel);
                        break;
                    case BY_PHONE_NUMBER:
                        listCustomers = doFilter("phoneNumber", query, customerModel);
                        break;
                    case BY_NAME:
                        listCustomers = doFilter("name", query, customerModel);
                        break;
                    default:
                        break;
                }
                
                return listCustomers;
            }
        });
    }

    public BeanSuggestionConverter createConverter(CustomerConverter type) {
        switch (type) {
            case BY_CODE:
                return new CustomerSuggestionConverterByCode();
            case BY_PHONE_NUMBER:
                return new CustomerSuggestionConverterByPhoneNumber();
            case BY_NAME:
                return new CustomerSuggestionConverterByName();
            default:
                break;
        }
        return null;
    }

    private class CustomerSuggestionConverterByPhoneNumber extends BeanSuggestionConverter {

        public CustomerSuggestionConverterByPhoneNumber() {
            super(Customer.class, "id", "phoneNumber", "phoneNumber");
        }

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item == null) {
                return new SuggestFieldSuggestion("-1", "", "");
            } else {
                Customer customer = (Customer) item;
                return new SuggestFieldSuggestion(customer.getId().toString(), "<b>" + customer.getPhoneNumber() + "</b><br>" + customer.getName(),
                        customer.getPhoneNumber());
            }
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            Customer result = new Customer();
            for (Object object : listCustomers) {
                Customer bean = (Customer) object;
                if (bean.getId().toString().equals(suggestion.getId())) {
                    result = bean;
                    break;
                }
            }
            return result;
        }

    }

    private class CustomerSuggestionConverterByCode extends BeanSuggestionConverter {

        public CustomerSuggestionConverterByCode() {
            super(Customer.class, "id", "code", "code");
        }

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item == null) {
                return new SuggestFieldSuggestion("-1", "", "");
            } else {
                Customer customer = (Customer) item;
                return new SuggestFieldSuggestion(customer.getId().toString(), "<b>" + customer.getCode() + "</b><br>" + customer.getName() + " "
                        + customer.getPhoneNumber(), customer.getCode());
            }
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            Customer result = new Customer();
            for (Object object : listCustomers) {
                Customer bean = (Customer) object;
                if (bean.getId().toString().equals(suggestion.getId())) {
                    result = bean;
                    break;
                }
            }
            return result;
        }
    }

    private class CustomerSuggestionConverterByName extends BeanSuggestionConverter {
        public CustomerSuggestionConverterByName() {
            super(Customer.class, "id", "name", "name");
        }

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item == null) {
                return new SuggestFieldSuggestion("-1", "", "");
            } else {
                Customer customer = (Customer) item;
                return new SuggestFieldSuggestion(customer.getId().toString(), "<b>" + customer.getName() + "</b><br>" + customer.getPhoneNumber(),
                        customer.getName());
            }
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            Customer result = new Customer();
            for (Object object : listCustomers) {
                Customer bean = (Customer) object;
                if (bean.getId().toString().equals(suggestion.getId())) {
                    result = bean;
                    break;
                }
            }
            return result;
        }
    }

    @Override
    public void updateEntity(AbstractEntity entity) {
        // TODO Auto-generated method stub
        
    }
}
