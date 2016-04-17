package com.luvsoft.presenter;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.SuggestField.SuggestionHandler;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import com.luvsoft.DAO.AbstractEntityModel;
import com.luvsoft.DAO.CustomerModel;
import com.luvsoft.DAO.FilterObject;
import com.luvsoft.DAO.MaterialModel;
import com.luvsoft.DAO.OrderModel;
import com.luvsoft.DAO.OrderTypeModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.entities.Ordertype;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

public class OrderPresenter extends AbstractEntityPresenter implements OrderListener {
    ////////////////////////////////////////////////////////////////////////////////
    // Declare enums and String converter
    ////////////////////////////////////////////////////////////////////////////////
    public enum CustomerConverter {
        BY_CODE, BY_NAME, BY_PHONE_NUMBER,
    }

    private static String CustomerConverterString(CustomerConverter type) {
        switch (type) {
            case BY_PHONE_NUMBER:
                return "phoneNumber";
            case BY_CODE:
                return "code";
            case BY_NAME:
                return "name";
            default:
                return "";
        }
    }

    public enum MaterialConverter {
        BY_CODE, BY_NAME
    }

    private static String MaterialConverterString(MaterialConverter type) {
        switch (type) {
            case BY_CODE:
                return "code";
            case BY_NAME:
                return "name";
            default:
                return "";
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    CustomerModel customerModel = new CustomerModel();
    MaterialModel materialModel = new MaterialModel();
    OrderTypeModel orderTypeModel = new OrderTypeModel();
    OrderModel orderModel = new OrderModel();

    private List<Object> listCustomers = new ArrayList<Object>();
    private List<Object> listMaterials = new ArrayList<Object>();

    public OrderPresenter() {
        model = new OrderModel();
    }

    public void saveOrder(Order order) {
        if(order == null || order.getOrderCode().equals("")) {
            return;
        }
        orderModel.addNew(order);
    }

    /**
     * Add an orderdetail to orderdetailList, if it is existed in list we will not add, if not we will add it
     * @param orderdetail
     * @param orderdetailList
     * 
     * @return return true if add successful, otherwise return false
     */
    public boolean addToOrderDetailList(Orderdetail orderdetail, List<Orderdetail> orderdetailList) {
        if(orderdetail == null) {
            System.out.println("Cannot add a null orderdetail");
            return false;
        }

        if(orderdetailList == null ) {
            orderdetailList = new ArrayList<Orderdetail>();
        }

        boolean isExisted = false;
        for (Orderdetail orderdetail2 : orderdetailList) {
            // We will avoid duplicated value in orderdetail List by comparing the material code of orderdetails
            if(orderdetail2.getFrk_material_code().equals(orderdetail.getFrk_material_code())) {
                isExisted = true;
                break;
            }
        }

        if(!isExisted) {
            orderdetailList.add(orderdetail);
            return true;
        }
        return false;
    }

    /**
     * Order are "Xuất Bán" + "Xuất Bán Nội Bộ" and they are queried from database
     * @param options -> component to display order types
     * @param ordertype -> if order already has ordertype, we just fill it to default value of component,
     *                     if not we will get the first item in ordertypelist and set default value for component
     * @param ordertypeList  -> the list of ordertype
     */
    public void createOrderTypes(OptionGroup options, Ordertype ordertype, List<Ordertype> ordertypeList) {
        ordertypeList = orderTypeModel.findAll();
        if(ordertypeList == null) {
            return;
        }

        for (Ordertype ordertype1 : ordertypeList) {
            options.addItem(ordertype1.getName());
        }

        if(ordertype == null || ordertype.getId() == -1) {
            if(ordertypeList != null && !ordertypeList.isEmpty()) {
                options.setValue(ordertypeList.get(0).getName());
            }
        } else {
            options.setValue(ordertype.getName());
        }
    }

    /**
     * Order Code has the format like that "HD" + currentId + random number (1->50000)
     * @param Textfield orderCode
     */
    public void generateOrderCode(TextField orderCode) {
        orderCode.setValue(generateEntityCode(Order.getEntityname()));
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    // For searching by material we create a suggestion field and setup for it includes:
    // + Suggestion Converter to setup the format for suggestion (by toSuggestion() function) 
    //   and return one item when it selected (by toItem() function)
    // + Suggestion Handler to search items by a query. It will request to database by doFilter() function
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void setUpSuggestFieldForMaterial(SuggestField search, MaterialConverter type) {
        search.setSuggestionConverter(new MaterialSuggestionConverter(type));
        search.setSuggestionHandler(new SuggestionHandler() {
            private static final long serialVersionUID = 1843281143420378657L;

            @Override
            public List<Object> searchItems(String query) {
                listMaterials = doFilter(MaterialConverterString(type), query, materialModel);
                return listMaterials;
            }
        });
    }

    private class MaterialSuggestionConverter extends BeanSuggestionConverter {
        private static final long serialVersionUID = -5171696939084857382L;
        private MaterialConverter type;

        public MaterialSuggestionConverter(MaterialConverter type) {
            super(Customer.class, "id", MaterialConverterString(type), MaterialConverterString(type));
            this.type = type;
        }

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item == null) {
                return new SuggestFieldSuggestion("-1", "", "");
            } else {
                Material material = (Material) item;
                switch (type) {
                    case BY_CODE:
                        return new SuggestFieldSuggestion(material.getId().toString(), material.getCode() + " " + material.getName(),
                                "");
                    case BY_NAME:
                        return new SuggestFieldSuggestion(material.getId().toString(), material.getCode() + " " + material.getName(),
                                "");
                    default:
                        return new SuggestFieldSuggestion("-1", "", "");
                }
            }
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            Material result = new Material();
            if(suggestion == null) {
                return result;
            }

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

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    // For searching by customer we create a suggestion field and setup for it includes:
    // + Suggestion Converter to setup the format for suggestion (by toSuggestion() function) 
    //   and return one item when it selected (by toItem() function)
    // + Suggestion Handler to search items by a query. It will request to database by doFilter() function
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void setUpSuggestFieldForCustomer(SuggestField search, CustomerConverter type) {
        search.setSuggestionConverter(new CustomerSuggestionConverter(type));
        search.setSuggestionHandler(new SuggestionHandler() {
            private static final long serialVersionUID = 2924379050340561852L;

            @Override
            public List<Object> searchItems(String query) {
                listCustomers = doFilter(CustomerConverterString(type), query, customerModel);
                return listCustomers;
            }
        });
    }

    private class CustomerSuggestionConverter extends BeanSuggestionConverter {
        private static final long serialVersionUID = -2019270561085484476L;
        private CustomerConverter type;

        public CustomerSuggestionConverter(CustomerConverter type) {
            super(Customer.class, "id", CustomerConverterString(type), CustomerConverterString(type));
            this.type = type;
        }

        @Override
        public SuggestFieldSuggestion toSuggestion(Object item) {
            if (item == null) {
                return new SuggestFieldSuggestion("-1", "", "");
            } else {
                Customer customer = (Customer) item;
                switch (type) {
                    case BY_CODE:
                        return new SuggestFieldSuggestion(customer.getId().toString(), "<b>" + customer.getCode() + "</b><br>" + customer.getName() + " "
                                + customer.getPhoneNumber(), customer.getCode());
                    case BY_PHONE_NUMBER:
                        return new SuggestFieldSuggestion(customer.getId().toString(), "<b>" + customer.getPhoneNumber() + "</b><br>" + customer.getName(),
                                customer.getPhoneNumber());
                    case BY_NAME:
                        return new SuggestFieldSuggestion(customer.getId().toString(), "<b>" + customer.getName() + "</b><br>" + customer.getPhoneNumber(),
                                customer.getName());
                    default:
                        return new SuggestFieldSuggestion("-1", "", "");
                }
            }
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            Customer result = new Customer();
            if(suggestion == null) {
                return result;
            }

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
