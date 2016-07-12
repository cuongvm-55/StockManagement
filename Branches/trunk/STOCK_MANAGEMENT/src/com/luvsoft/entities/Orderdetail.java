package com.luvsoft.entities;

// Generated Apr 14, 2016 10:06:14 PM by Hibernate Tools 4.3.1

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.luvsoft.utils.Utilities;
import com.luvsoft.view.component.anotations.DoNotGreaterThanQuantityInStock;

/**
 * Orderdetail generated by hbm2java
 */
@Entity
@Table(name = "orderdetail", catalog = "stockmanagement")
public class Orderdetail extends AbstractEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2791696496009521630L;
    private Integer id;
    private Material material;
    private Order order;
    private int quantityNeeded;
    private int quantityDelivered;
    private BigDecimal price;
    private Float saleOff;

    // Not-mapped members
    private transient String frk_material_code;
    private transient String frk_material_name;
    private transient String frk_material_unit;
    private transient String frk_material_stock;
    private transient int frk_material_quantity;
    private transient int quantityLacked;
    private transient double sellingPrice;
    private transient double totalAmount;
    private transient double importPrice;

    private transient String formattedPrice;
    private transient String formattedSellingPrice;
    private transient String formattedTotalAmount;
    private transient String formattedImportPrice;

    public Orderdetail() {
    }

    public Orderdetail(Material material, Order order, int quantityNeeded, int quantityDelivered, BigDecimal price) {
        this.material = material;
        this.order = order;
        this.quantityNeeded = quantityNeeded;
        this.quantityDelivered = quantityDelivered;
        this.price = price;
    }

    public Orderdetail(Material material, Order order, int quantityNeeded, int quantityDelivered, BigDecimal price, Float saleOff) {
        this.material = material;
        this.order = order;
        this.quantityNeeded = quantityNeeded;
        this.quantityDelivered = quantityDelivered;
        this.price = price;
        this.saleOff = saleOff;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMaterial", nullable = false)
    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOrder", nullable = false)
    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Column(name = "quantityNeeded", nullable = false)
    public int getQuantityNeeded() {
        return this.quantityNeeded;
    }

    public void setQuantityNeeded(int quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }

    @Column(name = "quantityDelivered", nullable = false)
    @DoNotGreaterThanQuantityInStock(message = "Số lượng đặt hàng không được lớn hơn số lượng tồn kho")
    public int getQuantityDelivered() {
        return this.quantityDelivered;
    }

    public void setQuantityDelivered(int quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    @Column(name = "price", nullable = false, scale = 4)
    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.formattedPrice = Utilities.getNumberFormat().format(price);
    }

    @Column(name = "saleOff", precision = 12, scale = 0)
    public Float getSaleOff() {
        return this.saleOff;
    }

    public void setSaleOff(Float saleOff) {
        this.saleOff = saleOff;
    }

    @Transient
    public String getFrk_material_code() {
        return frk_material_code;
    }

    public void setFrk_material_code(String frk_material_code) {
        this.frk_material_code = frk_material_code;
    }

    @Transient
    public String getFrk_material_name() {
        return frk_material_name;
    }

    public void setFrk_material_name(String frk_material_name) {
        this.frk_material_name = frk_material_name;
    }

    @Transient
    public String getFrk_material_unit() {
        return frk_material_unit;
    }

    public void setFrk_material_unit(String frk_material_unit) {
        this.frk_material_unit = frk_material_unit;
    }

    @Transient
    public String getFrk_material_stock() {
        return frk_material_stock;
    }

    public void setFrk_material_stock(String frk_material_stock) {
        this.frk_material_stock = frk_material_stock;
    }

    @Transient
    public int getFrk_material_quantity() {
        return frk_material_quantity;
    }

    public void setFrk_material_quantity(int frk_material_quantity) {
        this.frk_material_quantity = frk_material_quantity;
    }

    @Transient
    public int getQuantityLacked() {
        return quantityLacked;
    }

    public void setQuantityLacked(int quantityLacked) {
        this.quantityLacked = quantityLacked;
    }

    @Transient
    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.formattedSellingPrice = Utilities.getNumberFormat().format(sellingPrice);
        this.sellingPrice = sellingPrice;
    }

    @Transient
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.formattedTotalAmount = Utilities.getNumberFormat().format(totalAmount);
        this.totalAmount = totalAmount;
    }

    @Transient
    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.formattedImportPrice = Utilities.getNumberFormat().format(importPrice);
        this.importPrice = importPrice;
    }

    @Transient
    public String getformattedPrice() {
        formattedPrice = Utilities.getNumberFormat().format(price);
        return formattedPrice;
    }

    public void setformattedPrice(String formattedPrice) {
        price = new BigDecimal(Utilities.getDoubleValueFromFormattedStr(formattedPrice));
        this.formattedPrice = formattedPrice;
    }

    @Transient
    public String getFormattedSellingPrice() {
        formattedSellingPrice = Utilities.getNumberFormat().format(sellingPrice);
        return formattedSellingPrice;
    }

    public void setFormattedSellingPrice(String formattedSellingPrice) {
        sellingPrice = Utilities.getDoubleValueFromFormattedStr(formattedSellingPrice);
        this.formattedSellingPrice = formattedSellingPrice;
    }

    @Transient
    public String getFormattedTotalAmount() {
        formattedTotalAmount = Utilities.getNumberFormat().format(totalAmount);
        return formattedTotalAmount;
    }

    public void setFormattedTotalAmount(String formattedTotalAmount) {
        totalAmount = Utilities.getDoubleValueFromFormattedStr(formattedTotalAmount);
        this.formattedTotalAmount = formattedTotalAmount;
    }

    @Transient
    public String getFormattedImportPrice() {
        formattedImportPrice = Utilities.getNumberFormat().format(importPrice);
        return formattedImportPrice;
    }

    public void setFormattedImportPrice(String formattedImportPrice) {
        importPrice = Utilities.getDoubleValueFromFormattedStr(formattedImportPrice);
        this.formattedImportPrice = formattedImportPrice;
    }

    @Override
    public String toString() {
        return "Orderdetail [id=" + id + ", material=" + material + ", order="
                + order + ", quantityNeeded=" + quantityNeeded
                + ", quantityDelivered=" + quantityDelivered + ", price="
                + price + ", saleOff=" + saleOff + "]";
    }

    public static String getEntityname() {
        return "Orderdetail";
    }

    @Override
    public Object getValueByPropertyName(String propertyName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void verifyObject() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public AbstractEntity cloneObject() {
        // TODO Auto-generated method stub
        return null;
    }
}
