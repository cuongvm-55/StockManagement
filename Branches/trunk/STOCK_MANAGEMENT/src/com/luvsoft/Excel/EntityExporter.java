package com.luvsoft.Excel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import jxl.write.Label;
import jxl.write.WritableCell;

import com.luvsoft.DAO.EntityManagerDAO;

public abstract class EntityExporter extends ExcelExporter{

    protected boolean buildHeader(List<WritableCell> headers){
        List<Field> fields = entityAnalyzer.getFieldList();
        currentColumn = 0; // from the first column
        for( int i=0;i<fields.size();i++ ){
            Label lbl = createLabelCell(currentColumn, currentRow, fields.get(i).getName());
            headers.add(lbl);
            currentColumn++;
        }
        currentRow++;
        return true;
    }

    @Override
    protected boolean buildFooter(List<WritableCell> footers) {
        return true;
    }

    @Override
    protected boolean buildContent(List<WritableCell> contents) {
        EntityManagerDAO entityDAO = new EntityManagerDAO();
        List<Object> entities = entityDAO.findAll(entityAnalyzer.getEntityName());
        for( Object entity : entities){
            currentColumn = 0; // from the first column
            for( Field field : entityAnalyzer.getFieldList() ){
                if( field.getType().equals(String.class) ){
                    Label lbl = createLabelCell(currentColumn,
                            currentRow,
                            EntityAnalyzer.getFieldValue(entity, field).toString());
                    contents.add(lbl);
                }
                else if( field.getType().equals(Integer.class) ||
                            field.getType().equals(BigDecimal.class)){
                    jxl.write.Number nbr = createNumberCell(currentColumn,
                            currentRow,
                            Float.parseFloat(EntityAnalyzer.getFieldValue(entity, field).toString()));
                    contents.add(nbr);
                }
                else if( field.getType().equals(Date.class) ){
                    jxl.write.DateTime date = createDateCell(
                            currentColumn,
                            currentRow,
                            (Date)(EntityAnalyzer.getFieldValue(entity, field)),
                            false);
                    contents.add(date);
                }
                // Object generated from foreign key
                // Export only its id
                else if( field.getType().getPackage().getName()
                        .equals("com.luvsoft.entities") ){
                    Object object = EntityAnalyzer.getFieldValue(entity, field);
                    Method method = null;

                    // Get method getId()
                    try {
                        method = object.getClass().getDeclaredMethod ("getId");
                    } catch (NoSuchMethodException e) {
                        System.out.println("No method getId() in class " + object.getClass().getSimpleName());
                    } catch (SecurityException e) {
                        System.out.println("Access denied for method getId() in class " + object.getClass().getSimpleName());
                    }

                    // Get the desired id
                    try {
                        // Id's always an interger value
                        jxl.write.Number nbr = createNumberCell(
                                currentColumn,
                                currentRow,
                                Float.parseFloat(method.invoke(object).toString()));
                        contents.add(nbr);
                    } catch (IllegalAccessException e) {
                        System.out.println("IllegalAccessException method " + method.getName());
                    } catch (IllegalArgumentException e) {
                        System.out.println("IllegalArgumentException method " + method.getName());
                    } catch (InvocationTargetException e) {
                        System.out.println("InvocationTargetException method " + method.getName());
                    }
                }
                currentColumn++;
            }
            currentRow++;
        }

        return true;
    }

    @Override
    protected String getSheetName(){
        return entityAnalyzer.getEntityName();
    }

    @Override
    protected String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getReportName() {
        // TODO Auto-generated method stub
        return null;
    }
}
