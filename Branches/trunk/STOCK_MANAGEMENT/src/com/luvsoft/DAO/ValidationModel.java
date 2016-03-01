package com.luvsoft.DAO;

import java.util.List;

import com.luvsoft.entities.AbstractEntity;

public class ValidationModel<T extends AbstractEntity> {

    private EntityManagerDAO entityManager;
    private String entityName;

    public ValidationModel(Class<T> classType) {
        entityManager = new EntityManagerDAO();
        entityName = EntityNameFactory.createEntityName(classType);
    }

    /**
     * Use this method to check an field is duplicated or not
     * @param entity
     * @param propertyName
     * @param propertyValue
     * @return true when the field is duplicated, otherwise return false
     */
    public boolean checkDuplication(AbstractEntity entity, String propertyName, Object propertyValue) {
        List<Object> list = entityManager.findEntityByProperty(entityName, propertyName, propertyValue);
        if(!list.isEmpty()) {
            System.out.println("Found");
            if(entity.getId() == -1) {
                System.out.println("duplicated");
                return true;
            }

            for (Object object : list) {
                if( ((AbstractEntity) object).getId() == entity.getId() ) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
