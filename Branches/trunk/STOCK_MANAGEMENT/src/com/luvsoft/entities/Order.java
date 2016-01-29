package com.luvsoft.entities;

// Generated Jan 26, 2016 10:06:12 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Order generated by hbm2java
 */
@Entity
@Table(name = "order", catalog = "stockmanagement")
public class Order extends AbstractEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7702163142598168598L;
    private Integer id;
    private Customer customer;
    private Ordertype ordertype;
    private String orderCode;
    private String buyer;
    private String content;
    private Date date;
    private String note;
    private Set<Receivingbill> receivingbills = new HashSet<Receivingbill>(0);
    private Set<Orderdetail> orderdetails = new HashSet<Orderdetail>(0);

    public Order() {
    }

    public Order(String orderCode, String content, Date date) {
        this.orderCode = orderCode;
        this.content = content;
        this.date = date;
    }

    public Order(Customer customer, Ordertype ordertype, String orderCode, String buyer, String content, Date date, String note,
            Set<Receivingbill> receivingbills, Set<Orderdetail> orderdetails) {
        this.customer = customer;
        this.ordertype = ordertype;
        this.orderCode = orderCode;
        this.buyer = buyer;
        this.content = content;
        this.date = date;
        this.note = note;
        this.receivingbills = receivingbills;
        this.orderdetails = orderdetails;
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
    @JoinColumn(name = "idCustomer")
    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOrderType")
    public Ordertype getOrdertype() {
        return this.ordertype;
    }

    public void setOrdertype(Ordertype ordertype) {
        this.ordertype = ordertype;
    }

    @Column(name = "orderCode", nullable = false, length = 45)
    public String getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Column(name = "buyer", length = 128)
    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    @Column(name = "content", nullable = false, length = 65535)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false, length = 19)
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "note", length = 65535)
    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    public Set<Receivingbill> getReceivingbills() {
        return this.receivingbills;
    }

    public void setReceivingbills(Set<Receivingbill> receivingbills) {
        this.receivingbills = receivingbills;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    public Set<Orderdetail> getOrderdetails() {
        return this.orderdetails;
    }

    public void setOrderdetails(Set<Orderdetail> orderdetails) {
        this.orderdetails = orderdetails;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", customer=" + customer + ", ordertype="
                + ordertype + ", orderCode=" + orderCode + ", buyer=" + buyer
                + ", content=" + content + ", date=" + date + ", note=" + note
                + ", receivingbills=" + receivingbills + ", orderdetails="
                + orderdetails + "]";
    }

    public static String getEntityname() {
        return "Order";
    }
}
