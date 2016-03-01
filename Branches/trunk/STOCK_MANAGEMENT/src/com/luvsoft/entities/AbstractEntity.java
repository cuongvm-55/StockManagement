package com.luvsoft.entities;

public abstract class AbstractEntity {
    public abstract Integer getId();
    public abstract Object getValueByPropertyName(String propertyName);
    public abstract void verifyObject();
}