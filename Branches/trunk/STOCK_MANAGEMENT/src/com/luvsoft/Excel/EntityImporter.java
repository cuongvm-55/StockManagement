package com.luvsoft.Excel;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import com.luvsoft.DAO.EntityManagerDAO;

public abstract class EntityImporter extends ExcelImporter{
    private EntityAnalyzer entityAnalyzer;
    private EntityManagerDAO entityDao;

    public EntityImporter(){
        entityAnalyzer = new EntityAnalyzer(getEntityFullPathName());
        entityDao = new EntityManagerDAO();
    }

    /**
     * Full path entity class name
     * @return
     */
    public abstract String getEntityFullPathName();

    public boolean process(InputStream inputStream){
        System.out.println("Process input stream.");
        if( inputStream == null ){
            return false;
        }

        // parse file to get headers and records
        if( !super.parse(inputStream) ){
            System.out.println("Fail to parse the input stream.");
            return false;
        }

        if( headers != null ){
            System.out.println(headers.toString());
        }
        if( records != null ){
            System.out.println(records.toString());
        }
        
        // Check if we do not have so many headers than the entity
        if( headers.size() > entityAnalyzer.getFieldList().size() )
        {
            System.out.println("Invalid number of headers in excel file!");
            return false;
        }

        // save the records to db
        for( List<Object> record : records ){
            System.out.println(record);
            // Instantiate object
            Object entityInstance = EntityAnalyzer.instantiateObjectFromClassName(getEntityFullPathName());

            if( entityInstance == null ){
                System.out.println("Fail to instantiate a new object of class "+getEntityFullPathName());
                return false;
            }

            // map the data to entity
            for( String header : headers ){
                for( Field field : entityAnalyzer.getFieldList() ){
                    // if the header name are matched
                    if( field.getName().equals(header)){
                        Object data = record.get(headers.indexOf(header)); // header and value are always in the same order
                        if( field.getType().equals(data.getClass()) ){
                            // If the type are matched
                            // Set the value
                            EntityAnalyzer.setFieldValue(entityInstance, field, data);
                        }
                        else if( field.getType().equals(Integer.class) && data.getClass().equals(Double.class) ){
                            // number data from excel is in double format
                            // so we need to convert it
                            EntityAnalyzer.setFieldValue(entityInstance, field, (Integer)data);
                        }
                        else if( field.getType().equals(BigDecimal.class) && data.getClass().equals(Double.class) ){
                            EntityAnalyzer.setFieldValue(entityInstance, field, (BigDecimal)data);
                        }else if( field.getType().getPackage().getName()
                                .equals("com.luvsoft.entities") && field.getType().equals(String.class)){
                            // One-to-many relation
                            // The data received from excel file should be in string type (it's a name of referenced object)
                            // query database to get the desired object
                            Object refObj= entityDao.findByName(field.getType().getSimpleName(), (String)data);
                            if( refObj != null ){
                                EntityAnalyzer.setFieldValue(entityInstance, field, refObj);
                            }

                            // if getting the object fail:
                            // in case the key is not null, we ignore the record
                            // otherwise, just left it blank
                        }
                        else{
                            // invalid record
                            // in case the key is not null, we ignore the record
                            // otherwise, just left it blank
                            System.out.println("Invalid data: " + data.toString());
                        }
                    }
                }
            }
            System.out.println(entityInstance.toString());
            entityDao.addNew(entityInstance);
        }
        return true;
    }
}
