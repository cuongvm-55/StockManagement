package com.luvsoft.view.validator;

import java.lang.reflect.Method;

import javax.persistence.Column;

import com.luvsoft.entities.Orderdetail;
import com.luvsoft.view.component.anotations.DoNotGreaterThanQuantityInStock;
import com.vaadin.data.validator.BeanValidator;

/**
 * This validator is specific for editor in Grid
 * @author datnq.55
 *
 * @param <T>
 */
public class LuvsoftOrderDetailValidator extends BeanValidator {
    private static final long serialVersionUID = 7875072287835766201L;
    private String propertyName;
    private Orderdetail orderDetail;
    private boolean isCalledByPreCommit = false;

    public LuvsoftOrderDetailValidator(String propertyName) {
        super(Orderdetail.class, propertyName);
        this.propertyName = propertyName;
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        super.validate(value);

        Method[] methods = Orderdetail.class.getMethods();
        for(Method m : methods) {

            // In fact, we just want to valid AvoidDuplication when the user press on save button
            // so we just do it when isValid function is called by preCommit function
            if(m.isAnnotationPresent(DoNotGreaterThanQuantityInStock.class) && isCalledByPreCommit) {
                if(m.isAnnotationPresent(Column.class)) {
                    Column cl = m.getAnnotation(Column.class);
                    if(!cl.name().equals(propertyName)) {
                        continue;
                    }
                }
                DoNotGreaterThanQuantityInStock ad = m.getAnnotation(DoNotGreaterThanQuantityInStock.class);
                if(value != null && orderDetail != null && Integer.parseInt(value+"") > orderDetail.getFrk_material_quantity()) {
                    throw new InvalidValueException(ad.message());
                }
                isCalledByPreCommit = false;
            }
        }
    }

    public void setEntity(Orderdetail entity) {
        this.orderDetail = entity;
    }

    public boolean isCalledByPreCommit() {
        return isCalledByPreCommit;
    }

    public void setCalledByPreCommit(boolean isCalledByPreCommit) {
        this.isCalledByPreCommit = isCalledByPreCommit;
    }
}
