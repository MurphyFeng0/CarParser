package com.feng.carparser.model;

public enum CarFieldInXml {
    CAR("car"),
    TYPE("type"),
    MODEL("model"),
    PRICE("price"),
    PRICES("prices"),
    CURRENCY("currency");

    private final String fieldName;

    CarFieldInXml(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return fieldName;
    }
}
