package com.feng.carparser.model;

public enum SortType {
    PRICE("price"),
    DATE("date"),
    OPTIONAL("optional");

    private final String fieldName;

    SortType(String fieldName) {
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
