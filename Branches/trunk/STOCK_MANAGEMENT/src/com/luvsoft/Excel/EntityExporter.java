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
    protected  String destFolder;
    public EntityExporter(){
        entityAnalyzer = new EntityAnalyzer(getEntityFullPathName());
        currentColumn = 0;
        currentRow = 0;
    }

    public abstract String getEntityFullPathName();

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
                Object val = EntityAnalyzer.getFieldValue(entity, field);
                if( val == null )
                {
                    currentColumn++;
                    continue; // ignore null value
                }
                if( field.getType().equals(String.class) ){
                    Label lbl = createLabelCell(currentColumn,
                            currentRow,
                            val.toString());
                    contents.add(lbl);
                }
                else if( field.getType().equals(Integer.class) ||
                            field.getType().equals(BigDecimal.class)){
                    jxl.write.Number nbr = createNumberCell(currentColumn,
                            currentRow,
                            Float.parseFloat(val.toString()));
                    contents.add(nbr);
                }
                else if( field.getType().equals(Date.class) ){
                    jxl.write.DateTime date = createDateCell(
                            currentColumn,
                            currentRow,
                            (Date)(val),
                            false);
                    contents.add(date);
                }
                // Object generated from foreign key
                // Export only its name
                else if( field.getType().getPackage().getName()
                        .equals("com.luvsoft.entities") ){
                    Method method = null;

                    // Get method getName()
                    try {
                        method = val.getClass().getDeclaredMethod ("getName");
                        if( method == null ){
                            currentColumn++;
                            continue;
                        }
                    } catch (NoSuchMethodException e) {
                        System.out.println("No method getName() in class " + val.getClass().getSimpleName());
                    } catch (SecurityException e) {
                        System.out.println("Access denied for method getName() in class " + val.getClass().getSimpleName());
                    }

                    // Get the desired name
                    try {
                        // Name's always a string value
                        jxl.write.Label nbr = createLabelCell(
                                currentColumn,
                                currentRow,
                                method.invoke(val).toString());
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
    
    @Override
    protected String getDestFolder(){
        return destFolder;
    }
}
