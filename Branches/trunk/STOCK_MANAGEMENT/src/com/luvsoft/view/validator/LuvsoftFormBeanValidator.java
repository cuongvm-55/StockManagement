package com.luvsoft.view.validator;

import java.lang.reflect.Method;

import javax.persistence.Column;

import org.vaadin.viritin.MBeanFieldGroup.MValidator;

import com.luvsoft.DAO.ValidationModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.view.component.anotations.AvoidDuplication;
import com.vaadin.data.Validator.InvalidValueException;

/**
 * This validator is used when we want to validate a form
 * @author datnq.55
 *
 * @param <T>
 */
@SuppressWarnings({ "rawtypes" })
public class LuvsoftFormBeanValidator<T extends AbstractEntity> implements MValidator {
    private static final long serialVersionUID = 3901977890246454227L;
    private Class<?> beanClass;
    private String propertyName;
    private ValidationModel<T> validationModel;

    @SuppressWarnings({ "unchecked" })
    public LuvsoftFormBeanValidator(Class<?> beanClass, String propertyName) {
        this.beanClass = beanClass;
        this.propertyName = propertyName;
        validationModel = new ValidationModel(beanClass);
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        Object valueOfObject = ((AbstractEntity) value).getValueByPropertyName(propertyName);
        Method[] methods = beanClass.getMethods();
        for (Method m : methods) {
            // In fact, we just want to valid AvoidDuplication when the user
            // press on save button
            // so we just do it when isValid function is called by preCommit
            // function
            if (m.isAnnotationPresent(AvoidDuplication.class)) {
                if (m.isAnnotationPresent(Column.class)) {
                    Column cl = m.getAnnotation(Column.class);
                    if (!cl.name().equals(propertyName)) {
                        continue;
                    }
                }
                AvoidDuplication ad = m.getAnnotation(AvoidDuplication.class);
                if (validationModel.checkDuplication((AbstractEntity) value, propertyName, valueOfObject )) {
                    System.out.println("The " + propertyName + " of " + value + " is duplicated");
                    throw new InvalidValueException(ad.message());
                }
            }
        }
    }
}
