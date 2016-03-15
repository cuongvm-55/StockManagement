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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.luvsoft.utils.NemErrorList;
import com.luvsoft.view.component.anotations.AvoidDuplication;

/**
 * Stock generated by hbm2java
 */
@Entity
@Table(name = "stock", catalog = "stockmanagement")
public class Stock extends AbstractEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1896934027095966550L;
    private Integer id;
    private Stocktype stocktype;
    private String code;
    private String name;
    private String description;
    private Set<Material> materials = new HashSet<Material>(0);

    // Not-mapped members
    private transient String frk_stocktype_name; // name value of foreign key, formatted as: "frk_type_name"

    public Stock() {
    }

    public Stock(Stocktype stocktype, String code, String name) {
        this.stocktype = stocktype;
        this.code = code;
        this.name = name;
    }

    public Stock(Stocktype stocktype, String code, String name, String description, Set<Material> materials) {
        this.stocktype = stocktype;
        this.code = code;
        this.name = name;
        this.description = description;
        this.materials = materials;
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
    @JoinColumn(name = "idStockType", nullable = false)
    public Stocktype getStocktype() {
        return this.stocktype;
    }

    public void setStocktype(Stocktype stocktype) {
        this.stocktype = stocktype;

        // Set stockTypeName
        if( stocktype != null ){
            this.frk_stocktype_name = stocktype.getName();
        }
    }

    @Column(name = "code", nullable = false, length = 45)
    @Length(max = 45, message = NemErrorList.Error_NameTooLong)
    @NotBlank(message = NemErrorList.Error_NameNotEmpty)
    @AvoidDuplication(message = NemErrorList.Error_NameIsDuplicated)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Column(name = "description", length = 65535)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stock")
    public Set<Material> getMaterials() {
        return this.materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    @Override
    public String toString() {
        return "Stock [id=" + id + ", stocktype=" + stocktype + ", code="
                + code + ", name=" + name + ", description=" + description
                + ", materials=" + materials + ", frk_stocktype_name="
                + frk_stocktype_name + "]";
    }

    @Transient
    public String getFrk_stocktype_name() {
        return frk_stocktype_name;
    }

    public void setFrk_stocktype_name(String frk_stocktype_name) {
        this.frk_stocktype_name = frk_stocktype_name;
    }

    public static String getEntityname() {
        return "Stock";
    }

    @Override
    public Object getValueByPropertyName(String propertyName) {
        switch( propertyName ){
        case "code":
            return code;
        case "name":
            return name;
        case "description":
            return description;
        case "frk_stocktype_name":
            return frk_stocktype_name;
        case "stocktype":
            return stocktype;
        default:
            System.out.println("Property name not managed: " + propertyName);
            break;
        }

        return null;
    }

    @Override
    public void verifyObject() {
        id = (id == null) ? -1 : id;
        code = (code == null) ? "" : code;
        name = (name == null) ? "" : name;
        description = (description == null) ? "" : description;
    }

    @Override
    public Stock cloneObject() {
        Stock obj = new Stock();
        obj.setId(getId());
        obj.setCode(getCode());
        obj.setName(getName());
        obj.setDescription(getDescription());
        //obj.setFrk_stocktype_name(getFrk_stocktype_name());
        obj.setStocktype(getStocktype());
        obj.setMaterials(getMaterials());

        return obj;
    }
}
