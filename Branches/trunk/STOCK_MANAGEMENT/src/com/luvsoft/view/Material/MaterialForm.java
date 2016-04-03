package com.luvsoft.view.Material;

import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.entities.Material;
import com.luvsoft.presenter.UpdateEntityListener;
import com.luvsoft.utils.ACTION;
import com.luvsoft.view.component.LuvsoftAbstractForm;
import com.luvsoft.view.validator.LuvsoftFormBeanValidator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class MaterialForm<T extends AbstractEntity> extends LuvsoftAbstractForm<T>{
    private TextField price;
    private TextField quantity;
    private ComboBox unit;
    private ComboBox stock;
    private ComboBox materialtype1;
    private ComboBox materialtype2;

    public MaterialForm(UpdateEntityListener presenter, ACTION action, T cloneEntity){
        super(presenter, action, cloneEntity);

        // code
        code = new TextField();
        code.setCaption("Mã Vật Tư");
        listComponents.add(code);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "code"));

        // name
        name = new TextField();
        name.setCaption("Tên Vật Tư");
        listComponents.add(name);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "name"));

        // description
        description = new TextArea();
        description.setCaption("Mô tả");
        listComponents.add(description);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "description"));

        // price
        price = new TextField();
        price.setCaption("Giá");
        listComponents.add(price);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "price"));

        // quantity
        quantity = new TextField();
        quantity.setCaption("Số Lượng");
        listComponents.add(quantity);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "quantity"));

        // unit
        unit = new ComboBox();
        unit.setCaption("Đ.vị Tính");
        unit.setNullSelectionAllowed(false);
        unit.setNewItemsAllowed(false);
        listComponents.add(unit);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "unit"));

        // stock
        stock = new ComboBox();
        stock.setCaption("Tên Kho");
        stock.setNullSelectionAllowed(false);
        stock.setNewItemsAllowed(false);
        listComponents.add(stock);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "stock"));
        
        // materialtype1
        materialtype1 = new ComboBox();
        materialtype1.setCaption("Loại Vật Tư 1");
        materialtype1.setNullSelectionAllowed(false);
        materialtype1.setNewItemsAllowed(false);
        listComponents.add(materialtype1);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "materialtype1"));

        // materialtype2
        materialtype2 = new ComboBox();
        materialtype2.setCaption("Loại Vật Tư 2");
        materialtype2.setNullSelectionAllowed(false);
        materialtype2.setNewItemsAllowed(false);
        listComponents.add(materialtype2);
        listValidators.add(new LuvsoftFormBeanValidator<AbstractEntity>(Material.class, "materialtype2"));
    }

    @Override
    public MVerticalLayout buildFieldLayouts(){
        MVerticalLayout layout = new MVerticalLayout();

        layout.addComponents(code, name);

        MHorizontalLayout hz1 = new MHorizontalLayout();
        hz1.addComponents(quantity, unit);
        layout.addComponent(hz1);

        layout.addComponent(price);

        MHorizontalLayout hz2 = new MHorizontalLayout();
        hz2.addComponents(materialtype1, materialtype2);
        layout.addComponent(hz2);

        layout.addComponent(stock);
        layout.addComponent(description);

        return layout;
    }

    public ComboBox getUnit() {
        return unit;
    }

    public void setUnit(ComboBox unit) {
        this.unit = unit;
    }

    public ComboBox getStock() {
        return stock;
    }

    public void setStock(ComboBox stock) {
        this.stock = stock;
    }

    public ComboBox getMaterialtype1() {
        return materialtype1;
    }

    public void setMaterialtype1(ComboBox materialtype1) {
        this.materialtype1 = materialtype1;
    }

    public ComboBox getMaterialtype2() {
        return materialtype2;
    }

    public void setMaterialtype2(ComboBox materialtype2) {
        this.materialtype2 = materialtype2;
    }
}
