package com.luvsoft.presenter;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.utils.ACTION;

public interface UpdateEntityListener {
    public void updateEntity(AbstractEntity entity);
    public void deleteEntity(AbstractEntity entity);
    public void setAction(ACTION action);
}
