package com.luvsoft.presenter;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.utils.ACTION;

public interface EntityListener {

    public void generateTable();
    public void updateEntity(AbstractEntity entity);
    public void deleteEntity(AbstractEntity entity);
    public void setAction(ACTION action);
    // public boolean validateForm(AbstractEntity entity);
}
