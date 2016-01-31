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
 * Coupon generated by hbm2java
 */
@Entity
@Table(name = "coupon", catalog = "stockmanagement")
public class Coupon extends AbstractEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8045163466793625157L;
    private Integer id;
    private Coupontype coupontype;
    private Customer customer;
    private String code;
    private String buyer;
    private String content;
    private Date date;
    private String note;
    private Set<Coupondetail> coupondetails = new HashSet<Coupondetail>(0);
    private Set<Receivingbill> receivingbills = new HashSet<Receivingbill>(0);
    private Set<Spendingbill> spendingbills = new HashSet<Spendingbill>(0);

    public Coupon() {
    }

    public Coupon(Coupontype coupontype, String code, String content, Date date) {
        this.coupontype = coupontype;
        this.code = code;
        this.content = content;
        this.date = date;
    }

    public Coupon(Coupontype coupontype, Customer customer, String code, String buyer, String content, Date date, String note,
            Set<Coupondetail> coupondetails, Set<Receivingbill> receivingbills, Set<Spendingbill> spendingbills) {
        this.coupontype = coupontype;
        this.customer = customer;
        this.code = code;
        this.buyer = buyer;
        this.content = content;
        this.date = date;
        this.note = note;
        this.coupondetails = coupondetails;
        this.receivingbills = receivingbills;
        this.spendingbills = spendingbills;
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
    @JoinColumn(name = "idCouponType", nullable = false)
    public Coupontype getCoupontype() {
        return this.coupontype;
    }

    public void setCoupontype(Coupontype coupontype) {
        this.coupontype = coupontype;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCustomer")
    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Column(name = "code", nullable = false, length = 45)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon")
    public Set<Coupondetail> getCoupondetails() {
        return this.coupondetails;
    }

    public void setCoupondetails(Set<Coupondetail> coupondetails) {
        this.coupondetails = coupondetails;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon")
    public Set<Receivingbill> getReceivingbills() {
        return this.receivingbills;
    }

    public void setReceivingbills(Set<Receivingbill> receivingbills) {
        this.receivingbills = receivingbills;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon")
    public Set<Spendingbill> getSpendingbills() {
        return this.spendingbills;
    }

    public void setSpendingbills(Set<Spendingbill> spendingbills) {
        this.spendingbills = spendingbills;
    }

    @Override
    public String toString() {
        return "Coupon [id=" + id + ", coupontype=" + coupontype
                + ", customer=" + customer + ", code=" + code + ", buyer="
                + buyer + ", content=" + content + ", date=" + date + ", note="
                + note + ", coupondetails=" + coupondetails
                + ", receivingbills=" + receivingbills + ", spendingbills="
                + spendingbills + "]";
    }

    public static String getEntityname() {
        return "Coupon";
    }

}
