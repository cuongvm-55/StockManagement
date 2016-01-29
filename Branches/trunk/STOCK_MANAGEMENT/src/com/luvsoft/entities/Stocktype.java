package com.luvsoft.entities;

// Generated Jan 26, 2016 10:06:12 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Stocktype generated by hbm2java
 */
@Entity
@Table(name = "stocktype", catalog = "stockmanagement")
public class Stocktype extends AbstractEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2767774978183348478L;
    private Integer id;
    private String name;
    private String description;
    private Set<Stock> stocks = new HashSet<Stock>(0);

    public Stocktype() {
    }

    public Stocktype(String name) {
        this.name = name;
    }

    public Stocktype(String name, String description, Set<Stock> stocks) {
        this.name = name;
        this.description = description;
        this.stocks = stocks;
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

    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 65535)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stocktype")
    public Set<Stock> getStocks() {
        return this.stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "Stocktype [id=" + id + ", name=" + name + ", description="
                + description + ", stocks=" + stocks + "]";
    }

    public static String getEntityname() {
        return "Stocktype";
    }
}
