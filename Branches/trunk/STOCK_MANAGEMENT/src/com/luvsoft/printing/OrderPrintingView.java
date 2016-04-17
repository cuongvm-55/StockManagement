package com.luvsoft.printing;

import java.math.BigDecimal;
import java.util.Date;

import com.luvsoft.entities.Material;
import com.luvsoft.entities.Order;
import com.luvsoft.entities.Orderdetail;
import com.luvsoft.utils.Utilities;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class OrderPrintingView extends VerticalLayout {
    private final String BLANK_LINE = "__________________________";
    private static OrderPrintingView instance;
    private Order order; // current order data

    public static OrderPrintingView getInstance(){
        if( instance == null ){
            instance = new OrderPrintingView();
        }
        return instance;
    }

    public void buildContent(){
        if( order == null ){
            return;
        }
        this.removeAllComponents(); // refresh the view
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.addStyleName("print-header");
        // Title of the order
        Label lblTitle = new Label("Hóa Đơn Bán Hàng");
        lblTitle.addStyleName("text-align-center print-title");

        Label lblOrderCode = new Label(order.getOrderCode().equals("") ? BLANK_LINE : order.getOrderCode());
        lblOrderCode.setCaption("Số Hóa Đơn:");
        lblOrderCode.addStyleName("caption-left margin-left-right-20px");
        Label lblOrderType = new Label(
                (order.getOrdertype()!=null && !order.getOrdertype().getName().equals("")) ?
                        order.getOrdertype().getName() : BLANK_LINE);
        lblOrderType.setCaption("Loại Hóa Đơn:");
        lblOrderType.addStyleName("caption-left margin-left-right-20px");

        Label lblCustomerCode = new Label(
                (order.getCustomer()!=null && !order.getCustomer().getCode().equals("")) ?
                        order.getCustomer().getCode() : BLANK_LINE);
        lblCustomerCode.setCaption("Mã KH:");
        lblCustomerCode.addStyleName("caption-left margin-left-right-20px");
        Label lblCustomerName = new Label(
                (order.getCustomer()!=null && !order.getCustomer().getName().equals("")) ?
                        order.getCustomer().getName() : BLANK_LINE);
        lblCustomerName.setCaption("Tên KH:");
        lblCustomerName.addStyleName("caption-left margin-left-right-20px");
        HorizontalLayout hzLayout1 = new HorizontalLayout();
        hzLayout1.addComponents(lblCustomerCode, lblCustomerName);

        Label lblCustomerAddress = new Label(
                (order.getCustomer()!=null && !order.getCustomer().getAddress().equals("")) ?
                        order.getCustomer().getAddress() : BLANK_LINE);
        lblCustomerAddress.setCaption(" Địa Chỉ:");
        lblCustomerAddress.addStyleName("caption-left margin-left-right-20px");
        Label lblCustomerPhone = new Label(
                (order.getCustomer()!=null && !order.getCustomer().getPhoneNumber().equals("")) ?
                order.getCustomer().getPhoneNumber() : BLANK_LINE);
        lblCustomerPhone.setCaption(" Số ĐT:");
        lblCustomerPhone.addStyleName("caption-left margin-left-right-20px");
        HorizontalLayout hzLayout2 = new HorizontalLayout();
        hzLayout2.addComponents(lblCustomerPhone, lblCustomerAddress);

        Label lblBuyerName = new Label( (!order.getBuyer().equals("")) ? order.getBuyer() : BLANK_LINE );
        lblBuyerName.setCaption("Người Mua:");
        lblBuyerName.addStyleName("caption-left margin-left-right-20px");
        Label lblOrderContent = new Label( (!order.getContent().equals("")) ? order.getContent() : BLANK_LINE );
        lblOrderContent.setCaption("Diễn Giải:");
        lblOrderContent.addStyleName("caption-left margin-left-right-20px");
        Label lblOrderNote = new Label( (!order.getNote().equals("")) ? order.getNote() : BLANK_LINE );
        lblOrderNote.setCaption("Ghi Chú:");
        lblOrderNote.addStyleName("caption-left margin-left-right-20px");

        Table tblOrderDetails = new Table();
        tblOrderDetails.setSizeFull();
        //tblOrderDetails.setResponsive(true);
        tblOrderDetails.addStyleName("margin-left-right-20px");

        tblOrderDetails.addContainerProperty("STT", Integer.class, null);
        tblOrderDetails.addContainerProperty("Mã Hàng", String.class, null);
        tblOrderDetails.addContainerProperty("Tên Hàng", String.class, null);
        tblOrderDetails.addContainerProperty("Đơn Vị Tính", String.class, null);
        //tblOrderDetails.addContainerProperty("Mã Kho", String.class, null);
        //tblOrderDetails.addContainerProperty("SL Đặt Hàng", Integer.class, null);
        tblOrderDetails.addContainerProperty("SL Xuất", Integer.class, null);
        //tblOrderDetails.addContainerProperty("SL Thiếu", Integer.class, null);
        //tblOrderDetails.addContainerProperty("Tồn Cuối", Integer.class, null);
        tblOrderDetails.addContainerProperty("Giá Chuẩn", BigDecimal.class, null);
        tblOrderDetails.addContainerProperty("Chiết Khấu", Integer.class, null);
        tblOrderDetails.addContainerProperty("Giá Bán", BigDecimal.class, null);
        tblOrderDetails.addContainerProperty("Thành Tiền", Double.class, null);

        //tblOrderDetails.setColumnCollapsingAllowed(true);
        tblOrderDetails.setPageLength(0);

        Label lblTotalAmount = new Label("_________vnd");
        lblTotalAmount.setCaption("Tổng: ");
        lblTotalAmount.addStyleName("caption-left margin-left-right-20px");

        // Set order details content
        Object[] ods = order.getOrderdetails().toArray();
        double totalAmount = 0.0;
        double amount = 0.00;
        for( int i=0;i<ods.length;i++ ){
            Integer itemId = new Integer(i);
            Orderdetail od = (Orderdetail)ods[i];
            Material mt = od.getMaterial();
            // sanity check
            if( mt == null ) continue;
            amount = od.getPrice().doubleValue()*od.getQuantityDelivered();
            totalAmount+= amount;
            tblOrderDetails.addItem(new Object[]{
                    new Integer(i),
                    mt.getCode(),
                    mt.getName(),
                    ( mt.getUnit() != null ) ? mt.getUnit().getName() : "",
                    od.getQuantityDelivered(),
                    od.getMaterial().getPrice(),
                    od.getSaleOff(),
                    od.getPrice(),
                    amount,
                    }, itemId);
        }

        lblTotalAmount.setValue(Utilities.getNumberFormat().format(totalAmount)+"___vnđ");
        
        // align the layout
        headerLayout.addComponents(lblOrderCode, lblOrderType, hzLayout1, hzLayout2, lblOrderContent, lblOrderNote);

        Label lblDate = new Label(Utilities.DateToString(order.getDate() != null ? order.getDate() : new Date()));
        lblDate.setCaption("Thời gian: ");
        lblDate.addStyleName("caption-left margin-left-right-20px");
        
        VerticalLayout layout2 = new VerticalLayout();
        layout2.setSizeUndefined();
        layout2.addComponents(tblOrderDetails, lblTotalAmount, lblDate);

        //VerticalLayout layout = new VerticalLayout();
        this.setSizeUndefined();
        this.addComponents(lblTitle, headerLayout, layout2);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
