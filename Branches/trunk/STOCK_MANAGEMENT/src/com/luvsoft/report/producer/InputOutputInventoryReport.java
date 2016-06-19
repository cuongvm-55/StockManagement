package com.luvsoft.report.producer;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.luvsoft.DAO.StockModel;
import com.luvsoft.entities.Material;
import com.luvsoft.entities.Materialhistory;
import com.luvsoft.entities.Stock;
import com.luvsoft.entities.Stocktype;
import com.luvsoft.statistic.MaterialStatisticManager;
import com.luvsoft.statistic.Types.StatRecord;
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

                    // Inventory
                    Materialhistory inventoryMH = MaterialStatisticManager.getInstance().getInventoryMaterialAtDatePoint(from, material);
                    // Note: The static on "fromDate" is considered belong to interval
                    // Input stats in [from, to]
                    StatRecord inputRecord = new StatRecord();
                    MaterialStatisticManager.getInstance().extractInputStatisticInDateRange(
                            from,
                            to,
                            material.getId(),
                            inputRecord);

                    // Ouput stats in [from, to]
                    StatRecord outputRecord = new StatRecord();
                    MaterialStatisticManager.getInstance().extractOutputStatisticInDateRange(
                            from,
                            to,
                            material.getId(),
                            outputRecord);
                    double price = inventoryMH.getAverageInputPrice().doubleValue(); // average price
                    price = (price == 0f) ? material.getPrice().doubleValue() : price;
                    gridContent.addRow(
                            index,                            // STT
                            material.getCode(),               // Ma VT
                            material.getName(),               // Ten VT
                            material.getUnit() != null ? material.getUnit().getName() : "",// ĐVT
                            inventoryMH.getQuantity(),                   // SL ton  < dau ki >
                            Utilities.getNumberFormat().format(price), // Gia
                            Utilities.getNumberFormat().format(inventoryMH.getQuantity()*price), // TT ton
                            inputRecord.quantity,                                                    // SL nhap < trong ki >
                            Utilities.getNumberFormat().format(inputRecord.amount),       // TT nhap
                            outputRecord.quantity,                                                // SL xuat
                            Utilities.getNumberFormat().format(outputRecord.amount),      // TT xuat
                            inventoryMH.getQuantity() + inputRecord.quantity - outputRecord.quantity, // SL ton  < cuoi ki >
                            Utilities.getNumberFormat().format(
                                    (inventoryMH.getQuantity() + inputRecord.quantity - outputRecord.quantity)*price)); // TT ton
                     // update sums
                    originQuanitySum += inventoryMH.getQuantity();
                    originAmountSum  += inventoryMH.getQuantity()*price;
    
                    inputQuanitySum  += inputRecord.quantity;
                    inputAmountSum   += inputRecord.amount;
    
                    outputQuanitySum += outputRecord.quantity;
                    outputAmountSum  += outputRecord.amount;
    
                    inventoryQuanitySum += (inventoryMH.getQuantity() + inputRecord.quantity - outputRecord.quantity);
                    inventoryAmountSum  += (inventoryMH.getQuantity()*price + inputRecord.amount - outputRecord.amount);

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

}
