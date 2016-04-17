package com.luvsoft.presenter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.luvsoft.entities.Receivingbill;
import com.luvsoft.entities.Receivingbilldetail;
import com.luvsoft.printing.OrderPrintingView;
import com.luvsoft.utils.Utilities;
import com.luvsoft.view.Order.OrderFormContent;
import com.luvsoft.view.component.LuvsoftConfirmationDialog;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class OrderPresenter extends AbstractEntityPresenter implements OrderListener, Serializable {
    private static final long serialVersionUID = -7212035637448304624L;

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
    private CustomerModel customerModel = new CustomerModel();
    private MaterialModel materialModel = new MaterialModel();
    private OrderTypeModel orderTypeModel = new OrderTypeModel();
    private OrderModel orderModel = new OrderModel();

    private List<Object> listCustomers = new ArrayList<Object>();
    private List<Object> listMaterials = new ArrayList<Object>();

    private OrderFormContent view;

    public OrderPresenter() {
        criteriaMap = new HashMap<String, String>();
    }

    public void saveOrderToDatabase(Order order) {
        if(order == null || order.getOrderCode().equals("")) {
            return;
        }

        orderModel.addNew(order);
        // Update quantity in stock for material
        List<Orderdetail> orderdetails = new ArrayList<Orderdetail>(order.getOrderdetails());
        for (Orderdetail orderdetail : orderdetails) {
            if(orderdetail.getMaterial().getQuantity() != orderdetail.getFrk_material_quantity()) {
                materialModel.updateQuantityInStock(orderdetail.getFrk_material_quantity(), orderdetail.getMaterial());
            }
        }
    }

    public void deleteSelectedOrderdetails() {
        Collection<Object> selectedRows = view.getTableOrderDetails().getSelectedRows();
        LuvsoftConfirmationDialog dialog = new LuvsoftConfirmationDialog("Xác nhận xóa?");
        dialog.addLuvsoftClickListener(new ClickListener() {
            private static final long serialVersionUID = 351366856643651627L;

            @Override
            public void buttonClick(ClickEvent event) {
                for (Object object : selectedRows) {
                    view.getOrderDetails().remove(object);
                }
                view.getTableOrderDetails().setRows(view.getOrderDetails());
                dialog.close();
            }
        });

        // We do not show confirmation dialog if there is no selected item
        if(selectedRows.isEmpty()) {
            return;
        }

        UI.getCurrent().addWindow(dialog);
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
            orderdetail.setQuantityDelivered(0);
            orderdetail.setQuantityLacked(0);
            orderdetail.setQuantityNeeded(0);
            return true;
        }
        return false;
    }

    /**
     * Order are "Xuất Bán" + "Xuất Bán Nội Bộ" and they are queried from database
     * @param options -> component to display order types
     * @param ordertype -> if order already has ordertype, we just fill it to default value of component,
     *                     if not we will get the first item in ordertypelist and set default value for component
     */
    public void createOrderTypes(OptionGroup options, Ordertype ordertype) {
        List<Ordertype> ordertypeList = new ArrayList<Ordertype>();
        ordertypeList = orderTypeModel.findAll();
        for (Object object : ordertypeList) {
            options.addItem(object);
        }

        // Set default value for options
        if(!ordertypeList.isEmpty()) {
            if(ordertype == null || ordertype.getId() == -1) {
                options.setValue(ordertypeList.get(0));
            } else {
                if(ordertypeList.contains(ordertype)) {
                    options.setValue(ordertype);
                }
            }
        }
    }

    /**
     * Find one ordertype by it's id
     * @param name  -> name of ordertype need to search
     * @return ordertype
     */
    public Ordertype findOrderType(int id) {
        System.out.println("FindOrderType ID " + id);
        return orderTypeModel.findById(id);
    }

    /**
     * Order Code has the format like that "HD" + currentId + random number (1->50000)
     * @param Textfield orderCode
     */
    public void generateOrderCode(TextField orderCode) {
        Order lastOrder = orderModel.findLastItem();
        Integer lastItem = (lastOrder != null ) ? lastOrder.getId() : 0; 
        orderCode.setValue("HD" + (lastItem + 1) + (int) (Math.random() * 50000 + 1));
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

    /**
     * This function is used to save an order to database
     */
    public void saveOrder() {
        // Save it to database
        Order order = getOrderFromComponents();
        if(order == null) {
            System.out.println("Exist saveOrder due to order is invalid");
            return;
        }
        saveOrderToDatabase(order);
    }

    /**
     * This function is used to print an order
     */
    public void printOrder() {
        Order order = getOrderFromComponents();
        if(order == null) {
            System.out.println("Exist printOrder due to order is invalid");
            return;
        }
        OrderPrintingView.getInstance().setOrder(order);
    }

    /**
     * This function is used to paid an order
     */
    public void paidOrder() {
        // Save order to database
        Order order = getOrderFromComponents();
        if(order == null) {
            System.out.println("Exist saveOrder due to order is invalid");
            return;
        }

        if(!orderModel.isOrderExisted(order)) {
            saveOrderToDatabase(order);
        }

        // Create a receiving bill to paid money for this order
        Receivingbill bill = new Receivingbill();
        // bill.setCode(code);
        bill.setContent(order.getContent());
        bill.setNote("");
        bill.setIdCustomer(order.getIdCustomer());
        bill.setDate(order.getDate());
        bill.setOrder(order);

        // Receving bill detail
        Receivingbilldetail detail = new Receivingbilldetail();
        detail.setCategory("Thu Tiền");
        detail.setReason(order.getContent());

        double totalAmount = 0.0f;
        for (Orderdetail orderdetail : order.getOrderdetails()) {
            totalAmount += orderdetail.getTotalAmount();
        }
        detail.setAmount(new BigDecimal(totalAmount));
    }

    /**
     * This function is used to fill an order from components
     * @param order
     */
    public Order getOrderFromComponents() {
        Order order = view.getOrder();
        Customer customer = view.getCustomer();

        if(order == null) {
            System.out.println("Has no order to save");
            return null;
        }

        // Set data for order
        order.setOrderCode(view.getOrderNumber().getValue());
        if(order.getOrderCode().trim().equals("")) {
            System.out.println("Order code is invalid");
            return null;
        }

        if(customer != null && !customer.getCode().equals("") && view.getCustomerCode().getValue() != null) {
            order.setIdCustomer(customer.getId());
        } else {
            order.setIdCustomer(0);
        }

        order.setOrdertype((Ordertype) view.getOptionsOrderType().getValue());
        if(order.getOrdertype() == null || order.getOrdertype().getId() == -1) {
            // Ordertype cannot be null
            System.out.println("Ordertype is null");
            return null;
        }

        if(view.getOrderContent().getValue().trim().equals("")) {
            System.out.println("Content cannot be empty");
            return null;
        }

        if(view.getOrderDate().getValue() == null) {
            System.out.println("Date cannot be empty");
            return null;
        }

        order.setBuyer(view.getBuyer().getValue());
        order.setContent(view.getOrderContent().getValue());
        order.setDate(view.getOrderDate().getValue());
        order.setNote(view.getOrderNote().getValue());

        // Update orderDetails List
        if(view.getOrderDetails() != null && !view.getOrderDetails().isEmpty()) {
            Set<Orderdetail> setOrderdetails = new HashSet<Orderdetail>(view.getOrderDetails());
            order.setOrderdetails(setOrderdetails);
        }

        return order;
    }

    /**
     * Fill default values for orderDetail when it just created
     * @param orderDetail
     * @param filter
     */
    public void fillDefaultDataForOrderDetail(Orderdetail orderDetail, SuggestField filter) {
        Material material = (Material)filter.getValue();

        if(material != null) {
            orderDetail.setMaterial(material);
            orderDetail.setFrk_material_code(material.getCode());
            orderDetail.setFrk_material_name(material.getName());
            orderDetail.setFrk_material_unit(material.getUnit().getName());
            orderDetail.setFrk_material_stock(material.getStock().getCode());
            orderDetail.setQuantityNeeded(0);
            orderDetail.setQuantityDelivered(0);
            orderDetail.setQuantityLacked(0);
            orderDetail.setFrk_material_quantity(material.getQuantity());
            orderDetail.setSaleOff(0.0f);
            orderDetail.setPrice(material.getPrice());
            orderDetail.setformattedPrice(orderDetail.getformattedPrice());
    
            double safeOffAmount = material.getPrice().doubleValue() * orderDetail.getSaleOff();
            orderDetail.setSellingPrice(material.getPrice().doubleValue() - safeOffAmount);
            orderDetail.setTotalAmount(orderDetail.getSellingPrice() * orderDetail.getQuantityDelivered());
            orderDetail.setImportPrice(orderDetail.getPrice().doubleValue());
        }
        orderDetail.setOrder(view.getOrder());
    }

    /**
     * This function is used to change and calculate quantity lacked when change quantity delivered
     * @param event  ->  value change event of text field
     */
    public void calculateQuantityLackedWhenChangeQuantityDelivered(ValueChangeEvent event) {
        if(event.getProperty().getValue() != null) {
            int quantityDelivered = Integer.parseInt(event.getProperty().getValue()+"");

            String quantityNeededStr = ((TextField) view.getTableOrderDetails().getColumn("quantityNeeded").getEditorField()).getValue();
            if(quantityNeededStr == null) {
                quantityNeededStr = "";
            }
            int quantityNeeded = Integer.parseInt(quantityNeededStr == "" ? "0" : quantityNeededStr);

            int quantityLacked = quantityNeeded - quantityDelivered;
            if(quantityLacked < 0) {
                quantityLacked = 0;
            }
            ((TextField) view.getTableOrderDetails().getColumn("quantityLacked").getEditorField()).setValue(quantityLacked+"");
        }
    }

    /**
     * This function is used to change and calculate total amount when change quantity delivered
     * @param event  ->  value change event of text field
     */
    public void calculateTotalAmountWhenChangeQuantityDelivered(ValueChangeEvent event) {
        if(event.getProperty().getValue() != null) {
            String sellingPriceStr = ((TextField) view.getTableOrderDetails().getColumn("formattedSellingPrice").getEditorField()).getValue();
            if(sellingPriceStr == null) {
                sellingPriceStr = "";
            }
            double sellingPrice = Utilities.getDoubleValueFromFormattedStr(sellingPriceStr);

            String quantityDeliveredStr = ((TextField) view.getTableOrderDetails().getColumn("quantityDelivered").getEditorField()).getValue();
            int quantityDelivered = Integer.parseInt(quantityDeliveredStr == "" ? "0" : quantityDeliveredStr);

            double totalAmount = sellingPrice * quantityDelivered;
            ((TextField) view.getTableOrderDetails().getColumn("formattedTotalAmount").getEditorField()).setValue(Utilities.getNumberFormat().format(totalAmount));

            String quantityInStockStr = ((TextField) view.getTableOrderDetails().getColumn("frk_material_quantity").getEditorField()).getValue();
            int quantityInStock = Integer.parseInt((quantityInStockStr == null || quantityInStockStr == "") ? "0" : quantityInStockStr);
            quantityInStock = quantityInStock - quantityDelivered;
            ((TextField) view.getTableOrderDetails().getColumn("frk_material_quantity").getEditorField()).setValue(quantityInStock+"");
        }
    }

    /**
     * This function is used to change and calculate quantity lacked when change quantity needed
     * @param event  ->  value change event of text field
     */
    public void calculateQuantityLackedWhenChangeQuantityNeeded(ValueChangeEvent event) {
        if(event.getProperty().getValue() != null) {
            int quantityNeeded = Integer.parseInt(event.getProperty().getValue()+"");

            String quantityDeliveredStr = ((TextField) view.getTableOrderDetails().getColumn("quantityDelivered").getEditorField()).getValue();
            int quantityDelivered = Integer.parseInt(quantityDeliveredStr == "" ? "0" : quantityDeliveredStr);

            int quantityLacked = quantityNeeded - quantityDelivered;
            if(quantityLacked < 0) {
                quantityLacked = 0;
            }
            ((TextField) view.getTableOrderDetails().getColumn("quantityLacked").getEditorField()).setValue(quantityLacked+"");
        }
    }

    /**
     * This function is used to change and calculate selling price when change price
     * @param event  ->  value change event of text field
     */
    public void calculateSellingPriceWhenChangePrice(ValueChangeEvent event) {
        if(event.getProperty().getValue() != null) {
            String priceStr = event.getProperty().getValue() + "";
            double price = Utilities.getDoubleValueFromFormattedStr(priceStr);

            String saleOffStr = ((TextField) view.getTableOrderDetails().getColumn("saleOff").getEditorField()).getValue();
            if(saleOffStr == null) {
                saleOffStr = "";
            }
            float saleOff = Utilities.convertPercentageStringToFloat(saleOffStr == "" ? "0" : saleOffStr);

            double sellingPrice = price - (price * saleOff) / 100.0f;
            ((TextField) view.getTableOrderDetails().getColumn("formattedSellingPrice").getEditorField()).setValue(Utilities.getNumberFormat().format(sellingPrice));

            // Re-caculate the total amount
            String quantityDeliveredStr = ((TextField) view.getTableOrderDetails().getColumn("quantityDelivered").getEditorField()).getValue();
            if(quantityDeliveredStr == null) {
                quantityDeliveredStr = "";
            }
            int quantityDelivered = Integer.parseInt(quantityDeliveredStr == "" ? "0" : quantityDeliveredStr);

            double totalAmount = sellingPrice * quantityDelivered;
            ((TextField) view.getTableOrderDetails().getColumn("formattedTotalAmount").getEditorField()).setValue(Utilities.getNumberFormat().format(totalAmount));
        }
    }

    /**
     * This function is used to calulate selling price and total amount when change sale off value
     * @param event  -> value change event of text field
     */
    public void calculateSellingPriceWhenChangeSaleOff(ValueChangeEvent event) {
        if(event.getProperty().getValue() != null) {
            String saleOffStr = event.getProperty().getValue() + "";
            float saleOff = Utilities.convertPercentageStringToFloat(saleOffStr == "" ? "0" : saleOffStr);

            String priceStr = ((TextField) view.getTableOrderDetails().getColumn("formattedPrice").getEditorField()).getValue();
            if(priceStr == null) {
                priceStr = "";
            }
            double price = Utilities.getDoubleValueFromFormattedStr(priceStr);

            double sellingPrice = price - (price * saleOff) / 100.0f;
            ((TextField) view.getTableOrderDetails().getColumn("formattedSellingPrice").getEditorField()).setValue(Utilities.getNumberFormat().format(sellingPrice));

            // Re-caculate the total amount
            String quantityDeliveredStr = ((TextField) view.getTableOrderDetails().getColumn("quantityDelivered").getEditorField()).getValue();
            if(quantityDeliveredStr == null) {
                quantityDeliveredStr = "";
            }
            int quantityDelivered = Integer.parseInt(quantityDeliveredStr == "" ? "0" : quantityDeliveredStr);

            double totalAmount = sellingPrice * quantityDelivered;
            ((TextField) view.getTableOrderDetails().getColumn("formattedTotalAmount").getEditorField()).setValue(Utilities.getNumberFormat().format(totalAmount));
        }
    }

    /**
     * This function is used to display the total amount for all order details
     */
    public void calculateTotalAmountByOrderdetails() {
        double totalAmount = 0;
        for (Orderdetail orderdetail : view.getOrderDetails()) {
            totalAmount += orderdetail.getTotalAmount();
        }

        // Display it by a label
        view.getTotalAmountOfOrderdetails().setValue(Utilities.getNumberFormat().format(totalAmount) + " VNĐ");
    }

    @Override
    public void updateEntity(AbstractEntity entity) {
        // TODO Auto-generated method stub
        
    }

    public OrderFormContent getView() {
        return view;
    }

    public void setView(OrderFormContent view) {
        this.view = view;
    }
}
