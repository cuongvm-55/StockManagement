package com.luvsoft.Excel;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;

import jxl.write.Label;
import jxl.write.WritableCell;

import com.luvsoft.DAO.EntityManagerDAO;

public abstract class EntityExporter extends ExcelExporter{

    /**
     * Build excel file header
     * @param headers
     * @return
     */
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
        for( Object stockType : entities){
            currentColumn = 0; // from the first column
            for( Field field : entityAnalyzer.getFieldList() ){
                System.out.println(field.getName()+": " + EntityAnalyzer.getFieldValue(field, stockType.getClass(), stockType).toString());
                if( field.getType().equals(String.class) ){
                    Label lbl = createLabelCell(currentColumn,
                            currentRow,
                            EntityAnalyzer.getFieldValue(field, stockType.getClass(), stockType).toString());
                    contents.add(lbl);
                }
                else if( field.getType().equals(Integer.class) ){
                    jxl.write.Number nbr = createNumberCell(currentColumn,
                            currentRow,
                            Float.parseFloat(EntityAnalyzer.getFieldValue(field,
                                    stockType.getClass(),
                                    stockType).toString()));
                    contents.add(nbr);
                }
                else if( field.getType().equals(Date.class) ){
                    jxl.write.DateTime date = createDateCell(
                            currentColumn,
                            currentRow,
                            (Date)(EntityAnalyzer.getFieldValue(field,
                                    stockType.getClass(),
                                    stockType)), false);
                    contents.add(date);
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
