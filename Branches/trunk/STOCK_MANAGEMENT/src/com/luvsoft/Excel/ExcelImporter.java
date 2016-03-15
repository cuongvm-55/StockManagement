package com.luvsoft.Excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.luvsoft.utils.ErrorManager.ErrorId;

public abstract class ExcelImporter {
    protected List<String>   headers;  // headers
    protected List<List<Object>> records; // list of records
    public ErrorId parse(InputStream inputStream){
        try {
            Workbook workbook = Workbook.getWorkbook(inputStream);
            List<String> fieldList = new ArrayList<String>();
            fieldList.addAll(getFieldList().keySet());
            for(int index = 0; index < 1/*workbook.getNumberOfSheets()*/; index++){ // accept only the first sheet
                Sheet sheet = workbook.getSheet(index);
                System.out.println("Rows: " + sheet.getRows());
                System.out.println("Columns: " + sheet.getColumns());

                // sanity check
                // there must be at least 1 column and 2 rows (1 header row + 1 record)
                if( sheet.getColumns() < 1 || sheet.getRows() < 2 ){
                    System.out.println("Invalid excel file: Invalid number of records!");
                    return ErrorId.EXCEL_IMPORT_INVALID_NUMBER_OF_RECORD;
                }

                // get headers
                // header's always the first row. Else, invalid excel file
                headers = new ArrayList<String>();
                System.out.println("Get headers:");
                for( int col = 0; col < sheet.getColumns(); col++ ){
                    Cell cell = sheet.getCell(col, 0); // first row
                    if (cell.getType() != CellType.LABEL) 
                    {
                        // invalid header type, return
                        System.out.println("Invalid excel file: Header mus be a string!");
                        return ErrorId.EXCEL_IMPORT_INVALID_HEADERS;
                    }
                    LabelCell lc = (LabelCell) cell;
                    String header = lc.getString();
                    System.out.println(header);
                    if( header.equals("") || headers.contains(header) || !getFieldList().values().contains(header)){
                        // invalid header or duplicate header, return
                        System.out.println("Invalid excel file: Invalid header!");
                        return ErrorId.EXCEL_IMPORT_INVALID_HEADERS;
                    }

                    // Actually the value get from Excel file is the text value (e.g: "Stock type name")
                    // We have to map it to the field name of entity (e.g: stocktype)
                    for( int i=0;i<fieldList.size();i++){
                        if( getFieldList().get(fieldList.get(i)).equals(header) ){
                            headers.add(fieldList.get(i));
                            break;
                        }
                    }
                }

                // get records, get whatever data...
                // from second rows
                System.out.println("Get records:");
                records = new ArrayList<List<Object>>();
                for(int row = 1; row < sheet.getRows(); row++){
                    List<Object> record = new ArrayList<Object>();
                    for( int col = 0; col < headers.size(); col++ ){ // we want all the record has the same elements than headers
                        Cell cell = sheet.getCell(col, row);
                        if (cell.getType() == CellType.LABEL) 
                        {
                            LabelCell lc = (LabelCell) cell; 
                            record.add(lc.getString());
                        } 
                        else if (cell.getType() == CellType.NUMBER) 
                        { 
                            NumberCell nc = (NumberCell) cell; 
                            record.add(nc.getValue());
                        } 
                        else if (cell.getType() == CellType.DATE) 
                        { 
                            DateCell dc = (DateCell) cell; 
                            record.add(dc.getDate());
                        }
                        else{
                            record.add(""); // default is empty value
                        }
                    }
                    // add record to the list
                    records.add(record);
                }
            }
            return ErrorId.EXCEL_IMPORT_NOERROR;
        } catch (BiffException e) {
            return ErrorId.EXCEL_IMPORT_FAIL_TO_READ_FILE;
        } catch (IOException e) {
            return ErrorId.EXCEL_IMPORT_FAIL_TO_READ_FILE;
        }
    }

    public abstract Map<String, String> getFieldList();
}
