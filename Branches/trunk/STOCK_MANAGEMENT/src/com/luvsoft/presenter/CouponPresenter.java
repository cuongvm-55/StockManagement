package com.luvsoft.presenter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.SuggestField.SuggestionHandler;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import com.luvsoft.DAO.AbstractEntityModel;
import com.luvsoft.DAO.CouponModel;
import com.luvsoft.DAO.CouponTypeModel;
import com.luvsoft.DAO.CustomerModel;
import com.luvsoft.DAO.FilterObject;
import com.luvsoft.DAO.MaterialModel;
import com.luvsoft.DAO.SpendingbillModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Coupon;
import com.luvsoft.entities.Coupondetail;
import com.luvsoft.entities.Coupontype;
import com.luvsoft.entities.Customer;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Spendingbill;
import com.luvsoft.entities.Spendingbilldetail;
import com.luvsoft.utils.Utilities;
import com.luvsoft.view.Coupon.CouponFormContent;
import com.luvsoft.view.component.LuvsoftConfirmationDialog;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class CouponPresenter extends AbstractEntityPresenter implements CouponListener, Serializable {
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
    private CouponTypeModel couponTypeModel = new CouponTypeModel();
    private SpendingbillModel spendingbillModel = new SpendingbillModel();

    private List<Object> listCustomers = new ArrayList<Object>();
    private List<Object> listMaterials = new ArrayList<Object>();
    private List<Object> listCouponTypes = new ArrayList<Object>();

    private CouponFormContent view;

    public CouponPresenter() {
        model = new CouponModel();

        // Find all coupon types
        listCouponTypes = couponTypeModel.findAll(Coupontype.getEntityname());
    }

    public void saveCouponToDatabase(Coupon coupon) {
        if(coupon == null || coupon.getCode().equals("")) {
            return;
        }

        model.addNewByPersist(coupon);
        // Update quantity in stock for material
        List<Coupondetail> coupondetails = new ArrayList<Coupondetail>(coupon.getCoupondetails());
        for (Coupondetail coupondetail : coupondetails) {
            if(coupondetail.getMaterial().getQuantity() != coupondetail.getFrk_material_quantity()) {
                int newQuantity = coupondetail.getQuantity();
                int oldQuantity = coupondetail.getMaterial().getQuantity();
                System.out.println("New quantity " + (newQuantity + oldQuantity));
                materialModel.updateQuantityInStock(newQuantity + oldQuantity, coupondetail.getMaterial());

                // Backup history of material to the Materialinputhistory
                //materialModel.updateMaterialInputHistory(newQuantity, new BigDecimal(coupondetail.getBuyingPrice()), coupondetail.getMaterial());
            }
        }
    }

    public void deleteSelectedCoupondetails() {
        Collection<Object> selectedRows = view.getTableCouponDetails().getSelectedRows();
        LuvsoftConfirmationDialog dialog = new LuvsoftConfirmationDialog("Xác nhận xóa?");
        dialog.addLuvsoftClickListener(new ClickListener() {
            private static final long serialVersionUID = 351366856643651627L;

            @Override
            public void buttonClick(ClickEvent event) {
                for (Object object : selectedRows) {
                    view.getCouponDetails().remove(object);
                }
                view.getTableCouponDetails().setRows(view.getCouponDetails());
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
     * Add an coupondetail to coupondetailList, if it is existed in list we will not add, if not we will add it
     * @param coupondetail
     * @param coupondetailList
     * 
     * @return return true if add successful, otherwise return false
     */
    public boolean addToCouponDetailList(Coupondetail coupondetail, List<Coupondetail> coupondetailList) {
        if(coupondetail == null) {
            System.out.println("Cannot add a null coupondetail");
            return false;
        }

        if(coupondetailList == null ) {
            coupondetailList = new ArrayList<Coupondetail>();
        }

        boolean isExisted = false;
        for (Coupondetail coupondetail2 : coupondetailList) {
            // We will avoid duplicated value in coupondetail List by comparing the material code of coupondetails
            if(coupondetail2.getFrk_material_code().equals(coupondetail.getFrk_material_code())) {
                isExisted = true;
                break;
            }
        }

        if(!isExisted) {
            coupondetailList.add(coupondetail);
            coupondetail.setQuantity(0);
            return true;
        }
        return false;
    }

    /**
     * Coupon Code has the format like that "HD" + currentId + random number (1->50000)
     * @param Textfield couponCode
     */
    public void generateCouponCode(TextField couponCode) {
        couponCode.setValue(generateEntityCode(Coupon.getEntityname()));
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
     * This function is used to save an coupon to database
     */
    public void saveCoupon() {
        // Save it to database
        Coupon coupon = getCouponFromComponents();
        if(coupon == null) {
            System.out.println("Exist saveCoupon due to coupon is invalid");
            return;
        }
        saveCouponToDatabase(coupon);

        showNotification("Phiếu Nhập Mua Được Tạo Thành Công");
    }

    /**
     * This function is used to print an coupon
     */
    public void printCoupon() {
        Coupon coupon = getCouponFromComponents();
        if(coupon == null) {
            System.out.println("Exist printCoupon due to coupon is invalid");
            return;
        }
        // CouponPrintingView.getInstance().setCoupon(coupon);
        // TODO print coupon
    }

    /**
     * This function is used to paid an coupon
     */
    public void paidCoupon() {
        // Save coupon to database
        Coupon coupon = getCouponFromComponents();
        if(coupon == null) {
            System.out.println("Exist saveCoupon due to coupon is invalid");
            return;
        }

        if(!model.isCouponExisted(coupon)) {
            saveCouponToDatabase(coupon);
        }

        // Create a receiving bill to paid money for this coupon
        Spendingbill bill = new Spendingbill();
        bill.setCode(generateEntityCode(Spendingbill.getEntityname()));
        bill.setContent(coupon.getContent());
        bill.setNote("");
        bill.setCustomer(coupon.getCustomer());
        bill.setDate(coupon.getDate());
        bill.setIdCoupon(coupon.getId());

        // Receiving bill detail
        Spendingbilldetail detail = new Spendingbilldetail();
        detail.setCategory("Thu Tiền");
        detail.setReason(coupon.getContent());
        detail.setSpendingbill(bill);

        double totalAmount = 0.0f;
        for (Coupondetail coupondetail : coupon.getCoupondetails()) {
            totalAmount += coupondetail.getTotalAmount();
        }
        detail.setAmount(new BigDecimal(totalAmount));

        Set<Spendingbilldetail> details = new HashSet<Spendingbilldetail>();
        details.add(detail);
        bill.setSpendingbilldetails(details);
        // Save receiving bill
        spendingbillModel.addNew(bill);

        showNotification("Phiếu Nhập Mua Được Thanh Toán Thành Công - Một Phiếu Chi Đã Được Tạo");
    }

    /**
     * This function is used to fill an coupon from components
     * @param coupon
     */
    public Coupon getCouponFromComponents() {
        Coupon coupon = view.getCoupon();
        Customer customer = view.getCustomer();

        if(coupon == null) {
            System.out.println("Has no coupon to save");
            return null;
        }

        // Set data for coupon
        coupon.setCode(view.getCouponNumber().getValue());
        if(coupon.getCode().trim().equals("")) {
            System.out.println("Coupon code is invalid");
            return null;
        }

        if(customer != null && !customer.getCode().equals("") && view.getCustomerCode().getValue() != null) {
            coupon.setCustomer(customer);
        } else {
            System.out.println("Customer is required");
            return null;
        }

        if(view.getCouponContent().getValue().trim().equals("")) {
            System.out.println("Content cannot be empty");
            return null;
        }

        if(view.getCouponDate().getValue() == null) {
            System.out.println("Date cannot be empty");
            return null;
        }

        coupon.setBuyer(view.getTransporter().getValue());
        coupon.setContent(view.getCouponContent().getValue());
        coupon.setDate(view.getCouponDate().getValue());
        coupon.setNote(view.getCouponNote().getValue());

        // Update couponDetails List
        if(view.getCouponDetails() != null && !view.getCouponDetails().isEmpty()) {
            Set<Coupondetail> setCoupondetails = new HashSet<Coupondetail>(view.getCouponDetails());
            coupon.setCoupondetails(setCoupondetails);
        }

        for (Object object : listCouponTypes) {
            Coupontype type = (Coupontype) object;
            if(type.getName().equals(Coupontype.GetStringFromTypes(view.getCouponType()))) {
                coupon.setCoupontype(type);
                break;
            }
        }

        return coupon;
    }

    /**
     * Fill default values for Coupondetail when it just created
     * @param Coupondetail
     * @param filter
     */
    public void fillDefaultDataForCouponDetail(Coupondetail couponDetail, SuggestField filter) {
        Material material = (Material)filter.getValue();

        if(material != null) {
            couponDetail.setMaterial(material);
            couponDetail.setFrk_material_code(material.getCode());
            couponDetail.setFrk_material_name(material.getName());
            couponDetail.setFrk_material_unit(material.getUnit().getName());
            couponDetail.setFrk_material_stock(material.getStock().getCode());
            couponDetail.setQuantity(0);
            couponDetail.setFrk_material_quantity(material.getQuantity());
            couponDetail.setSaleOff(0.0f);
            couponDetail.setPrice(material.getPrice());
            couponDetail.setformattedPrice(couponDetail.getformattedPrice());
    
            double safeOffAmount = material.getPrice().doubleValue() * couponDetail.getSaleOff();
            couponDetail.setBuyingPrice(material.getPrice().doubleValue() - safeOffAmount);
            couponDetail.setTotalAmount(couponDetail.getBuyingPrice() * couponDetail.getQuantity());
        }
        couponDetail.setCoupon(view.getCoupon());
    }

    /**
     * This function is used to change and calculate total amount when change quantity
     * @param event  ->  value change event of text field
     */
    public void calculateTotalAmountWhenChangeQuantity(ValueChangeEvent event) {
        if(event.getProperty().getValue() != null) {
            String buyingPriceStr = ((TextField) view.getTableCouponDetails().getColumn("formattedBuyingPrice").getEditorField()).getValue();
            if(buyingPriceStr == null) {
                buyingPriceStr = "";
            }
            double buyingPrice = Utilities.getDoubleValueFromFormattedStr(buyingPriceStr);

            String quantityStr = ((TextField) view.getTableCouponDetails().getColumn("quantity").getEditorField()).getValue();
            int quantity = Integer.parseInt(quantityStr == "" ? "0" : quantityStr);

            double totalAmount = buyingPrice * quantity;
            ((TextField) view.getTableCouponDetails().getColumn("formattedTotalAmount").getEditorField()).setValue(Utilities.getNumberFormat().format(totalAmount));

            String quantityInStockStr = ((TextField) view.getTableCouponDetails().getColumn("frk_material_quantity").getEditorField()).getValue();
            int quantityInStock = Integer.parseInt((quantityInStockStr == null || quantityInStockStr == "") ? "0" : quantityInStockStr);
            quantityInStock = quantityInStock + quantity;
            ((TextField) view.getTableCouponDetails().getColumn("frk_material_quantity").getEditorField()).setValue(quantityInStock+"");
        }
    }

    /**
     * This function is used to change and calculate buying price when change price
     * @param event  ->  value change event of text field
     */
    public void calculateBuyingPriceWhenChangePrice(ValueChangeEvent event) {
        if(event.getProperty().getValue() != null) {
            String priceStr = event.getProperty().getValue() + "";
            double price = Utilities.getDoubleValueFromFormattedStr(priceStr);

            String saleOffStr = ((TextField) view.getTableCouponDetails().getColumn("saleOff").getEditorField()).getValue();
            if(saleOffStr == null) {
                saleOffStr = "";
            }
            float saleOff = Utilities.convertPercentageStringToFloat(saleOffStr == "" ? "0" : saleOffStr);

            double buyingPrice = price - (price * saleOff) / 100.0f;
            ((TextField) view.getTableCouponDetails().getColumn("formattedBuyingPrice").getEditorField()).setValue(Utilities.getNumberFormat().format(buyingPrice));

            // Re-caculate the total amount
            String quantityStr = ((TextField) view.getTableCouponDetails().getColumn("quantity").getEditorField()).getValue();
            if(quantityStr == null) {
                quantityStr = "";
            }
            int quantity = Integer.parseInt(quantityStr == "" ? "0" : quantityStr);

            double totalAmount = buyingPrice * quantity;
            ((TextField) view.getTableCouponDetails().getColumn("formattedTotalAmount").getEditorField()).setValue(Utilities.getNumberFormat().format(totalAmount));
        }
    }

    /**
     * This function is used to calculate buying price and total amount when change sale off value
     * @param event  -> value change event of text field
     */
    public void calculateSellingPriceWhenChangeSaleOff(ValueChangeEvent event) {
        if(event.getProperty().getValue() != null) {
            String saleOffStr = event.getProperty().getValue() + "";
            float saleOff = Utilities.convertPercentageStringToFloat(saleOffStr == "" ? "0" : saleOffStr);

            String priceStr = ((TextField) view.getTableCouponDetails().getColumn("formattedPrice").getEditorField()).getValue();
            if(priceStr == null) {
                priceStr = "";
            }
            double price = Utilities.getDoubleValueFromFormattedStr(priceStr);

            double buyingPrice = price - (price * saleOff) / 100.0f;
            ((TextField) view.getTableCouponDetails().getColumn("formattedBuyingPrice").getEditorField()).setValue(Utilities.getNumberFormat().format(buyingPrice));

            // Re-caculate the total amount
            String quantityStr = ((TextField) view.getTableCouponDetails().getColumn("quantity").getEditorField()).getValue();
            if(quantityStr == null) {
                quantityStr = "";
            }
            int quantity = Integer.parseInt(quantityStr == "" ? "0" : quantityStr);

            double totalAmount = buyingPrice * quantity;
            ((TextField) view.getTableCouponDetails().getColumn("formattedTotalAmount").getEditorField()).setValue(Utilities.getNumberFormat().format(totalAmount));
        }
    }

    /**
     * This function is used to display the total amount for all Coupon details
     */
    public void calculateTotalAmountByCoupondetails() {
        double totalAmount = 0;
        for (Coupondetail Coupondetail : view.getCouponDetails()) {
            totalAmount += Coupondetail.getTotalAmount();
        }

        // Display it by a label
        view.getTotalAmountOfCoupondetails().setValue(Utilities.getNumberFormat().format(totalAmount) + " VNĐ");
    }

    public void showNotification(String message) {
        Notification notification = new Notification("<b>Thông Báo<b>", "<b>" + message + "</b>", Type.TRAY_NOTIFICATION);
        notification.setHtmlContentAllowed(true);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }

    @Override
    public void updateEntity(AbstractEntity entity) {
        // TODO Auto-generated method stub
        
    }

    public CouponFormContent getView() {
        return view;
    }

    public void setView(CouponFormContent view) {
        this.view = view;
    }
}
