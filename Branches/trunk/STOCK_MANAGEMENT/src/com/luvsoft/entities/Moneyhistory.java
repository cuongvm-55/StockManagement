package com.luvsoft.entities;

// Generated May 12, 2016 9:26:32 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Moneyhistory generated by hbm2java
 */
@Entity
@Table(name = "moneyhistory", catalog = "stockmanagement")
public class Moneyhistory extends AbstractEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private BigDecimal amount;
    private Date date;

    public Moneyhistory() {
    }

    public Moneyhistory(BigDecimal amount, Date date) {
        this.amount = amount;
        this.date = date;
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

    @Column(name = "amount", nullable = false, scale = 4)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false, length = 19)
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
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
