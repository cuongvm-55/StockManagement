package com.luvsoft.view.Customer;

import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Customer;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;
import com.luvsoft.view.validator.LuvsoftFormBeanValidator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class CustomerForm<T extends AbstractEntity> extends LuvsoftAbstractForm<T>{
    private TextField address;
    private TextField phoneNumber;
    private TextField email;
    private TextField bankName;
    private TextField bankAccount;
    private TextField debt;
    private ComboBox area;
    private ComboBox customertype1;
    private ComboBox customertype2;

    public CustomerForm(UpdateEntityListener presenter, ACTION action, T cloneEntity){
        super(presenter, action, cloneEntity);

        // code
        code = new TextField();
        code.setCaption("Mã KH");
        listComponents.add(code);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "code"));

        // name
        name = new TextField();
        name.setCaption("Tên KH");
        listComponents.add(name);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "name"));

        // address
        address = new TextField();
        address.setCaption("Địa chỉ");
        listComponents.add(address);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "address"));

        // phone nbr
        phoneNumber = new TextField();
        phoneNumber.setCaption("Số ĐT");
        listComponents.add(phoneNumber);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "phoneNumber"));

        // email
        email = new TextField();
        email.setCaption("Email");
        listComponents.add(email);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "email"));

        // bank name
        bankName = new TextField();
        bankName.setCaption("Tên Ngân Hàng");
        listComponents.add(bankName);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "bankName"));

        // bank account
        bankAccount = new TextField();
        bankAccount.setCaption("TK Ngân Hàng");
        listComponents.add(bankAccount);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "bankAccount"));

        // debt
        debt = new TextField();
        debt.setCaption("Dư Nợ");
        listComponents.add(debt);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "debt"));

        // area
        area = new ComboBox();
        area.setCaption("Khu Vực");
        area.setNullSelectionAllowed(false);
        area.setNewItemsAllowed(false);
        listComponents.add(area);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "area"));

        // customertype1
        customertype1 = new ComboBox();
        customertype1.setCaption("Loại KH 1");
        customertype1.setNullSelectionAllowed(false);
        customertype1.setNewItemsAllowed(false);
        listComponents.add(customertype1);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "customertype1"));

        // customertype2
        customertype2 = new ComboBox();
        customertype2.setCaption("Loại KH 2");
        customertype2.setNullSelectionAllowed(false);
        customertype2.setNewItemsAllowed(false);
        listComponents.add(customertype2);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Customer.class, "customertype2"));
    }

    @Override
    public MVerticalLayout buildFieldLayouts(){
        MVerticalLayout layout = new MVerticalLayout();

        layout.addComponents(code, name);

        MHorizontalLayout hz = new MHorizontalLayout();
        hz.addComponents(email, area);
        layout.addComponent(hz);

        MHorizontalLayout hz1 = new MHorizontalLayout();
        hz1.addComponents(address, phoneNumber);
        layout.addComponent(hz1);

        MHorizontalLayout hz2 = new MHorizontalLayout();
        hz2.addComponents(bankName, bankAccount);
        layout.addComponent(hz2);

        MHorizontalLayout hz3 = new MHorizontalLayout();
        hz3.addComponents(customertype1, customertype2);
        layout.addComponent(hz3);

        layout.addComponent(debt);

        return layout;
    }
    
    public ComboBox getArea() {
        return area;
    }

    public void setArea(ComboBox area) {
        this.area = area;
    }

    public ComboBox getCustomertype1() {
        return customertype1;
    }

    public void setCustomertype1(ComboBox customertype1) {
        this.customertype1 = customertype1;
    }

    public ComboBox getCustomertype2() {
        return customertype2;
    }

    public void setCustomertype2(ComboBox customertype2) {
        this.customertype2 = customertype2;
    }
}
