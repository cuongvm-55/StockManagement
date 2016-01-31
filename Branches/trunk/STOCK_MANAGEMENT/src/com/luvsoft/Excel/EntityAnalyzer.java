package com.luvsoft.Excel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EntityAnalyzer {
    private String entityName; // name of entity

    List<Field> fieldList; //<! list of field that will be exported

    /**
     * The fully entity class name
     * Ex: com.luvsoft.entities.Stocktype
     * @param entityFullName
     */
    public EntityAnalyzer(String entityFullName){
        fieldList = new ArrayList<Field>();

        getFields(entityFullName);
        System.out.println(fieldList.toString());
/*
        Stocktype type = new Stocktype();
        type.setId(100);
        type.setName("Type 1");
        type.setDescription("Blah blah blah!");

        for( Field field : fieldList ){
            System.out.println(field.getName()+": " + getFieldValue(field, type.getClass(), type).toString());
        }
*/
    }

    /**
     * Get all member of entity
     * @return
     */
    private boolean getFields(String entityName){
        try{
            // Retrieve all fields
            Class<?> c = Class.forName(entityName);
            this.entityName = c.getSimpleName();
            List<Field> privateFields = new ArrayList<>();
            Field[] allFields = c.getDeclaredFields();

            // If there's no field
            if( allFields == null || allFields.length == 0 ){
                System.out.println("Class \""+entityName+"\" has no field!");
                return false;
            }

            // Consider only private members
            for (Field field : allFields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    privateFields.add(field);
                }
            }

            // Filter
            for( int i=0;i<privateFields.size();i++){
                Field field = privateFields.get(i);
                Type type = field.getGenericType();
                System.out.println("field name: " + field.getName());
                if (type instanceof ParameterizedType) {
                    // We ignore parameterized type fields
                    ParameterizedType ptype = (ParameterizedType) type;
                    ptype.getRawType();
                    System.out.println("-raw type:" + ptype.getRawType());
                    System.out.println("-type arg: " + ptype.getActualTypeArguments()[0]);
                } else {
                    System.out.println("-field type: " + field.getType());
                    // Ignore "serialVersionUUID" and "id"
                    if( !field.getName().equals("serialVersionUID") 
                            && !field.getName().equals("id") )
                    fieldList.add(field);
                }
            }
            if( fieldList.isEmpty() ){
                System.out.println("Class \"" +entityName+"\" has no field to export!");
                return false;
            }
            return true;
        }catch(ClassNotFoundException ce){
            System.out.println("Class \"" +entityName+"\" not found!");
            return false;
        }
    }

    /**
     * Get value of a field by invoke getting method of an object
     * @param field
     * @param o
     * @param object
     * @return
     */
    public static Object getFieldValue(Field field, Class<?> o, Object object){
        // MZ: Find the correct method
        for (Method method : o.getMethods()){
            if( (method.getName().startsWith("get"))
                    && (method.getName().length() == (field.getName().length() + 3))){
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())){
                    // MZ: Method found, run it
                    try{
                        return method.invoke(object);
                    }
                    catch (IllegalAccessException e){
                        System.out.println("IllegalAccessException, invoke method: " + method.getName());}
                    catch (InvocationTargetException e){
                        System.out.println("InvocationTargetException, invoke method: " + method.getName());
                    }
                }
            }
        }
        return null;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }
}
