package com.luvsoft.Excel;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import com.luvsoft.DAO.EntityManagerDAO;
import com.luvsoft.Excel.ErrorManager.ErrorId;

public abstract class EntityImporter extends ExcelImporter{
    private EntityAnalyzer entityAnalyzer;
    private EntityManagerDAO entityDao;
    
    private int nbrOfImportedRecords;

    public int getNbrOfImportedRecords() {
        return nbrOfImportedRecords;
    }

    public void setNbrOfImportedRecords(int nbrOfImportedRecords) {
        this.nbrOfImportedRecords = nbrOfImportedRecords;
    }

    public EntityImporter(){
        entityAnalyzer = new EntityAnalyzer(getEntityFullPathName());
        entityDao = new EntityManagerDAO();
    }

    /**
     * Full path entity class name
     * @return
     */
    public abstract String getEntityFullPathName();

    public ErrorId process(InputStream inputStream){
        System.out.println("Process input stream.");
        if( inputStream == null ){
            return ErrorId.EXCEL_IMPORT_NOERROR; // just return when no data to process
        }

        // parse file to get headers and records
        ErrorId error = super.parse(inputStream);
        if( error != ErrorId.EXCEL_IMPORT_NOERROR ){
            System.out.println("Fail to parse the input stream.");
            return error;
        }

        if( headers != null ){
            System.out.println(headers.toString());
        }
        if( records != null ){
            System.out.println(records.toString());
        }

        // Check if we have so many headers than the entity
        if( headers.size() > entityAnalyzer.getFieldList().size() )
        {
            System.out.println("Invalid number of headers in excel file!");
            return ErrorId.EXCEL_IMPORT_INVALID_NUMBER_OF_HEADERS;
        }

        nbrOfImportedRecords = 0;
        // save the records to db
        for( List<Object> record : records ){
            System.out.println(record);
            // Instantiate object
            Object entityInstance = EntityAnalyzer.instantiateObjectFromClassName(getEntityFullPathName());

            if( entityInstance == null ){
                System.out.println("Fail to instantiate a new object of class "+getEntityFullPathName());
                return ErrorId.EXCEL_IMPORT_FAIL_TO_PROCESS_ENTITY_RECORD;
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
                            if( !EntityAnalyzer.setFieldValue(entityInstance, field, data) ){
                                return ErrorId.EXCEL_IMPORT_FAIL_TO_PROCESS_ENTITY_RECORD;
                            }
                        }
                        else if( field.getType().equals(Integer.class) && data.getClass().equals(Double.class) ){
                            // number data from excel is in double format
                            // so we need to convert it
                            if( !EntityAnalyzer.setFieldValue(entityInstance, field, (Integer)data) ){
                                return ErrorId.EXCEL_IMPORT_FAIL_TO_PROCESS_ENTITY_RECORD;
                            }
                        }
                        else if( field.getType().equals(BigDecimal.class) && data.getClass().equals(Double.class) ){
                            if( !EntityAnalyzer.setFieldValue(entityInstance, field, (BigDecimal)data) ){
                                return ErrorId.EXCEL_IMPORT_FAIL_TO_PROCESS_ENTITY_RECORD;
                            }
                        }else if( field.getType().getPackage().getName()
                                .equals("com.luvsoft.entities") && field.getType().equals(String.class)){
                            // One-to-many relation
                            // The data received from excel file should be in string type (it's a name of referenced object)
                            // query database to get the desired object
                            Object refObj= entityDao.findByName(field.getType().getSimpleName(), (String)data);
                            if( refObj != null ){
                                if( !EntityAnalyzer.setFieldValue(entityInstance, field, refObj) ){
                                    return ErrorId.EXCEL_IMPORT_FAIL_TO_PROCESS_ENTITY_RECORD;
                                }
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
            if( !entityDao.addNew(entityInstance) ){
                return ErrorId.EXCEL_IMPORT_FAIL_TO_PROCESS_ENTITY_RECORD;
            }

            nbrOfImportedRecords++; // increase imported record
        }

        nbrOfImportedRecords++;
        return ErrorId.EXCEL_IMPORT_NOERROR;
    }
}
