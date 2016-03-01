package com.luvsoft.view.validator;

import java.lang.reflect.Method;

import javax.persistence.Column;

import com.luvsoft.DAO.ValidationModel;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.view.component.anotations.AvoidDuplication;
import com.vaadin.data.validator.BeanValidator;

/**
 * This validator is specific for editor in Grid
 * @author datnq.55
 *
 * @param <T>
 */
public class LuvsoftTableBeanValidator<T extends AbstractEntity> extends BeanValidator {
    private static final long serialVersionUID = 3901977890246454227L;
    private Class<?> beanClass;
    private String propertyName;
    private ValidationModel<T> validationModel;
    private AbstractEntity entity;
    private boolean isCalledByPreCommit = false;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public LuvsoftTableBeanValidator(Class<?> beanClass, String propertyName) {
        super(beanClass, propertyName);
        this.beanClass = beanClass;
        this.propertyName = propertyName;
        validationModel = new ValidationModel(beanClass);
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        super.validate(value);

        Method[] methods = beanClass.getMethods();
        for(Method m : methods) {

            // In fact, we just want to valid AvoidDuplication when the user press on save button
            // so we just do it when isValid function is called by preCommit function
            if(m.isAnnotationPresent(AvoidDuplication.class) && isCalledByPreCommit) {
                if(m.isAnnotationPresent(Column.class)) {
                    Column cl = m.getAnnotation(Column.class);
                    if(!cl.name().equals(propertyName)) {
                        continue;
                    }
                }
                AvoidDuplication ad = m.getAnnotation(AvoidDuplication.class);
                if(validationModel.checkDuplication(entity, propertyName, value)) {
                    System.out.println("The " + propertyName + " with value " + value + " is duplicated");
                    throw new InvalidValueException(ad.message());
                }
                isCalledByPreCommit = false;
            }
        }
    }

    public void setEntity(AbstractEntity entity) {
        this.entity = entity;
    }

    public boolean isCalledByPreCommit() {
        return isCalledByPreCommit;
    }

    public void setCalledByPreCommit(boolean isCalledByPreCommit) {
        this.isCalledByPreCommit = isCalledByPreCommit;
    }
}
