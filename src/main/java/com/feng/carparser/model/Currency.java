package com.feng.carparser.model;

public enum Currency {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    JPY("JPY");

    private final String fieldName;

    Currency(String fieldName) {
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
