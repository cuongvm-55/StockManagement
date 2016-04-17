package com.luvsoft.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.luvsoft.DAO.EntityManagerDAO;
import com.luvsoft.DAO.StockModel;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialinputhistory;
import com.luvsoft.entities.Materialoutputhistory;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.utils.Utilities;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Báo cáo Xuất-Nhập-Tồn
 * @author cuongvm
 *
 */
@SuppressWarnings("serial")
public class InputOutputInventoryReport extends VerticalLayout{
    private String TITLE = "BÁO CÁO XUẤT-NHẬP-TỒN KHO HÀNG HÓA";
    
    private Label lblTitle;
    private DateField dfFrom;
    private DateField dfTo;
    private Button btnOk;
    private Grid gridContent;
    
    public InputOutputInventoryReport(){
        lblTitle = new Label(TITLE);
        lblTitle.addStyleName("text-center margin-left-20px margin-top-10px");
        lblTitle.addStyleName(ValoTheme.LABEL_BOLD);
        lblTitle.addStyleName(ValoTheme.LABEL_LARGE);
        dfFrom = new DateField("Từ");
        dfFrom.addStyleName("date-field-caption");
        dfTo = new DateField("Đến");
        dfTo.addStyleName("date-field-caption");
        btnOk = new Button("Xem Báo Cáo");
        btnOk.addClickListener(new ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                Date frDate = dfFrom.getValue();
                Date toDate = dfTo.getValue();
                if( frDate != null && toDate != null && !toDate.before(frDate) ){
                    InputOutputInventoryReport.this.produce();
                    buildStaticData(Utilities.reachDayBegin(frDate), Utilities.reachDayEnd(toDate));
                }
            }
        });
    }
    
    public void produce(){
        HorizontalLayout hzLayoutDate = new HorizontalLayout();
        hzLayoutDate.addComponents(dfFrom, dfTo, btnOk);
        hzLayoutDate.setSpacing(true);
        hzLayoutDate.addStyleName("margin-left-right-20px");
        gridContent = new Grid();
        gridContent.setSizeFull();
        //tblOrderDetails.setResponsive(true);
        gridContent.addStyleName("margin-left-right-20px");

        gridContent.addColumn("STT", Integer.class);
        gridContent.addColumn("Mã VT", String.class);
        gridContent.addColumn("Tên VT", String.class);
        gridContent.addColumn("ĐVT", String.class);

        gridContent.addColumn("SL Đầu", Integer.class);
        gridContent.addColumn("Giá", String.class);
        gridContent.addColumn("TT Đầu", String.class);
        
        gridContent.addColumn("SL Nhập", Integer.class);
        gridContent.addColumn("TT Nhập", String.class);
        
        gridContent.addColumn("SL Xuất", Integer.class);
        gridContent.addColumn("TT Xuất", String.class);

        gridContent.addColumn("SL Tồn", Integer.class);
        gridContent.addColumn("TT Tồn", String.class);

        HeaderRow groupingHeader = gridContent.prependHeaderRow();
        HeaderCell namesCell1 = groupingHeader.join(
                groupingHeader.getCell("SL Đầu"),
                groupingHeader.getCell("TT Đầu"),
                groupingHeader.getCell("Giá"));
        namesCell1.setText("Tồn Đầu Kì");

        HeaderCell namesCell2 = groupingHeader.join(groupingHeader.getCell("SL Nhập"), groupingHeader.getCell("TT Nhập"));
        namesCell2.setText("Nhập Trong Kì");
        
        HeaderCell namesCell3 = groupingHeader.join(groupingHeader.getCell("SL Xuất"), groupingHeader.getCell("TT Xuất"));
        namesCell3.setText("Xuất Trong Kì");

        HeaderCell namesCell4 = groupingHeader.join(groupingHeader.getCell("SL Tồn"), groupingHeader.getCell("TT Tồn"));
        namesCell4.setText("Tồn Cuối Kì");

        // footer
        //gridContent.setFooterVisible(true);
        //FooterRow groupingFooter = gridContent.prependFooterRow();
        /*FooterCell lblSum = groupingFooter.join(
                groupingFooter.getCell("STT"),
                groupingFooter.getCell("Mã VT"),
                groupingFooter.getCell("Tên VT"),
                groupingFooter.getCell("ĐVT"))*/;
        //lblSum.setText("Tổng");
        this.removeAllComponents();
        this.addComponents(lblTitle,hzLayoutDate, gridContent);
        this.setSpacing(true);
    }
    
    /**
     * Retrieves static data fall in the period time [from, to]
     * @param from
     * @param to
     */
    private void buildStaticData(Date from, Date to){
        //MaterialModel mmodel = new MaterialModel();
        //List<Stock> stockList = mmodel.getStockList();
        StockModel mmodel = new StockModel();
        List<Stocktype> stockTypeList = mmodel.getStockTypeList();
        double originQuanitySum = 0;
        double originAmountSum = 0;
        double inputQuanitySum = 0;
        double inputAmountSum = 0;
        double outputQuanitySum = 0;
        double outputAmountSum = 0;
        double inventoryQuanitySum = 0;
        double inventoryAmountSum = 0;
        int index = 0;
        for( Stocktype stockType : stockTypeList ){
            for( Stock stock : stockType.getStocks() ){
                Set<Material> materialList = stock.getMaterials();
                if( materialList == null || materialList.isEmpty() ){
                    continue; // next stock
                }
                Object[] materials = materialList.toArray();
                for( int i=0;i<materials.length;i++ ){
                    Material material = (Material)materials[i];
    
                    // Note: The static before "from" is considered as origin
                    Materialinputhistory inputBegRecord = getNearestInputHistory(Utilities.addDate(from,-1), material.getId());
                    Materialoutputhistory outputBegRecord = getNearestOutputHistory(Utilities.addDate(from,-1), material.getId());
                    Materialinputhistory inputEndRecord = getNearestInputHistory(to, material.getId());
                    Materialoutputhistory outputEndRecord = getNearestOutputHistory(to, material.getId());
    
                    double price = inputBegRecord.getInputPrice().doubleValue();
                    price = (price == 0f) ? material.getPrice().doubleValue() : price;
                    int originQuantity = inputBegRecord.getQuantity() - outputBegRecord.getQuantity();
                    int inputQuantity = inputEndRecord.getQuantity() - inputBegRecord.getQuantity();
                    int outputQuantity = outputEndRecord.getQuantity() - outputBegRecord.getQuantity();
                    int inventoryQuantity = (originQuantity + inputQuantity) - outputQuantity;
                    gridContent.addRow(
                            index,                            // STT
                            material.getCode(),               // Ma VT
                            material.getName(),               // Ten VT
                            material.getUnit() != null ? material.getUnit().getName() : "",// ĐVT
                            originQuantity,                   // SL ton  < dau ki >
                            Utilities.getNumberFormat().format(price), // Gia
                            Utilities.getNumberFormat().format(originQuantity*price), // TT ton
                            inputQuantity,                                                    // SL nhap < trong ki >
                            Utilities.getNumberFormat().format(inputQuantity*price),       // TT nhap
                            outputQuantity,                                                   // SL xuat
                            Utilities.getNumberFormat().format(outputQuantity*price),      // TT xuat
                            inventoryQuantity,                                                // SL ton  < cuoi ki >
                            Utilities.getNumberFormat().format(inventoryQuantity*price) ); // TT ton
                     // update sums
                    originQuanitySum += originQuantity;
                    originAmountSum += originQuantity*price;
    
                    inputQuanitySum += inputQuantity;
                    inputAmountSum += inputQuantity*price;
    
                    outputQuanitySum += outputQuantity;
                    outputAmountSum += outputQuantity*price;
    
                    inventoryQuanitySum += inventoryQuantity;
                    inventoryAmountSum += inventoryQuantity*price;

                    index++;
                }
            }
        }
        FooterRow groupingFooter = gridContent.prependFooterRow();
        groupingFooter.getCell("SL Đầu").setText(""+originQuanitySum);
        groupingFooter.getCell("TT Đầu").setText(Utilities.getNumberFormat().format(originAmountSum));
        groupingFooter.getCell("SL Nhập").setText(""+inputQuanitySum);
        groupingFooter.getCell("TT Nhập").setText(Utilities.getNumberFormat().format(inputAmountSum));
        groupingFooter.getCell("SL Xuất").setText(""+outputQuanitySum);
        groupingFooter.getCell("TT Xuất").setText(Utilities.getNumberFormat().format(outputAmountSum));
        groupingFooter.getCell("SL Tồn").setText(""+inventoryQuanitySum);
        groupingFooter.getCell("TT Tồn").setText(Utilities.getNumberFormat().format(inventoryAmountSum));
    }

    /**
     * get all input of this material  in [from, to] period
     * @param from
     * @param to
     * @return
     */
    private List<Materialinputhistory> extractInputHistoryInDateRange(Date from, Date to, Integer materialId){
        List<Materialinputhistory> ret = new ArrayList<Materialinputhistory>();
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Materialinputhistory.getEntityname()+" e "
                + "WHERE e.date >= :var0 AND e.date <= :var1 "
                + "AND material.id = :var2";
        List<Object> params = new ArrayList<Object>();
        params.add(from);
        params.add(to);
        params.add(materialId);

        List<Object> retsults = EntityManagerDAO.getInstance().findByQuery(QUERY, params);
        for( int i=0;i<retsults.size();i++ ){
            Materialinputhistory mt = (Materialinputhistory)retsults.get(i);
            mt.verifyObject();
            ret.add(mt);
        }

        return ret;
    }

    /**
     * get all output of this material in [from, to] period
     * @param from
     * @param to
     * @return
     */
    private List<Materialoutputhistory> extractOutputHistoryInDateRange(Date from, Date to, Integer materialId){
        List<Materialoutputhistory> ret = new ArrayList<Materialoutputhistory>();
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Materialoutputhistory.getEntityname()+" e "
                + "WHERE e.date >= :var0 AND e.date <= :var1 "
                + "AND material.id = :var2";
        List<Object> params = new ArrayList<Object>();
        params.add(from);
        params.add(to);
        params.add(materialId);

        List<Object> retsults = EntityManagerDAO.getInstance().findByQuery(QUERY, params);
        for( int i=0;i<retsults.size();i++ ){
            Materialoutputhistory mt = (Materialoutputhistory)retsults.get(i);
            mt.verifyObject();
            ret.add(mt);
        }

        return ret;
    }
    
    /**
     * get nearest input history report before the datePoint [..., datePoint]
     * @param datePoint
     * @return
     */
    private Materialinputhistory getNearestInputHistory(Date datePoint, Integer materialId){
        Materialinputhistory h = new Materialinputhistory();
        h.verifyObject();
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Materialinputhistory.getEntityname()+" e "
                + "WHERE e.date <= :var0 "
                + "AND material.id = :var1 "
                + "ORDER BY date DESC";
        List<Object> params = new ArrayList<Object>();
        params.add(datePoint);
        params.add(materialId);

        List<Object> retsults = EntityManagerDAO.getInstance().findByQuery(QUERY, params);
        if( !retsults.isEmpty() ){
            // get the first history record
            h = (Materialinputhistory)retsults.get(0);
            h.verifyObject();
        }

        return h;
    }
    
    /**
     * get nearest output history report before the datePoint [..., datePoint]
     * @param datePoint
     * @return
     */
    private Materialoutputhistory getNearestOutputHistory(Date datePoint, Integer materialId){
        Materialoutputhistory h = new Materialoutputhistory();
        h.verifyObject();
        String QUERY = ""
                + "SELECT e "
                + "FROM "+Materialoutputhistory.getEntityname()+" e "
                + "WHERE e.date <= :var0 "
                + "AND material.id = :var1 "
                + "ORDER BY date DESC";
        List<Object> params = new ArrayList<Object>();
        params.add(datePoint);
        params.add(materialId);

        List<Object> retsults = EntityManagerDAO.getInstance().findByQuery(QUERY, params);
        if( !retsults.isEmpty() ){
            // get the first history record
            h = (Materialoutputhistory)retsults.get(0);
            h.verifyObject();
        }

        return h;
    }
}
