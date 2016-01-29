package com.luvsoft.entities;

// Generated Jan 26, 2016 10:06:12 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Receivingbilldetail generated by hbm2java
 */
@Entity
@Table(name = "receivingbilldetail", catalog = "stockmanagement")
public class Receivingbilldetail extends AbstractEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7569307280337331448L;
    private Integer id;
    private Receivingbill receivingbill;
    private String category;
    private String reason;
    private BigDecimal amount;

    public Receivingbilldetail() {
    }

    public Receivingbilldetail(Receivingbill receivingbill, BigDecimal amount) {
        this.receivingbill = receivingbill;
        this.amount = amount;
    }

    public Receivingbilldetail(Receivingbill receivingbill, String category, String reason, BigDecimal amount) {
        this.receivingbill = receivingbill;
        this.category = category;
        this.reason = reason;
        this.amount = amount;
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
    @JoinColumn(name = "idReceivingBill", nullable = false)
    public Receivingbill getReceivingbill() {
        return this.receivingbill;
    }

    public void setReceivingbill(Receivingbill receivingbill) {
        this.receivingbill = receivingbill;
    }

    @Column(name = "category", length = 256)
    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "reason", length = 65535)
    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "amount", nullable = false, scale = 4)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Receivingbilldetail [id=" + id + ", receivingbill="
                + receivingbill + ", category=" + category + ", reason="
                + reason + ", amount=" + amount + "]";
    }

    public static String getEntityname() {
        return "Receivingbilldetail";
    }
}
