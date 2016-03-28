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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.luvsoft.utils.NemErrorList;
import com.luvsoft.view.component.anotations.AvoidDuplication;

/**
 * Coupontype generated by hbm2java
 */
@Entity
@Table(name = "coupontype", catalog = "stockmanagement")
public class Coupontype extends AbstractEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 619189815486000057L;
    private Integer id;
    private String name;
    private String description;
    private Set<Coupon> coupons = new HashSet<Coupon>(0);

    public Coupontype() {
        id = -1;
        name = "";
        description = "";
    }

    public Coupontype(String name) {
        this.name = name;
    }

    public Coupontype(String name, String description, Set<Coupon> coupons) {
        this.name = name;
        this.description = description;
        this.coupons = coupons;
    }

    public Coupontype(Integer id, String name, String description,
            Set<Coupon> coupons) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.coupons = coupons;
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

    @Column(name = "name", unique = true, nullable = false, length = 45)
    @Length(max = 45, message = NemErrorList.Error_NameTooLong)
    @NotBlank(message = NemErrorList.Error_NameNotEmpty)
    @AvoidDuplication(message = NemErrorList.Error_NameIsDuplicated)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(max = 65535, message = NemErrorList.Error_DescriptionTooLong)
    @Column(name = "description", length = 65535)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupontype")
    public Set<Coupon> getCoupons() {
        return this.coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "Coupontype [id=" + id + ", name=" + name + ", description="
                + description + ", coupons=" + coupons + "]";
    }

    public static String getEntityname() {
        return "Coupontype";
    }

    @Override
    public Object getValueByPropertyName(String propertyName) {
        switch (propertyName) {
            case "name":
                return this.name;
            case "description":
                return this.description;
        }
        return new String("Not found");
    }
    
    @Override
    public Coupontype cloneObject() {
        return new Coupontype(getId(), getName(), getDescription(), getCoupons());
    }

    @Override
    public void verifyObject() {
        if(id == null) {
            id = -1;
        }
        if(name == null) {
            name = "";
        }
        if(description == null) {
            description = "";
        }
    }
}
