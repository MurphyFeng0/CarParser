package com.feng.carparser.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Map;

public class Car {
    private String type;
    private String model;
    private Map<String, Double> prices;
    private String brand;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    public Car(String type, String model, Map<String, Double> prices) {
        this.type = type;
        this.model = model;
        this.prices = prices;
    }

    public String getType() { return type; }
    public String getModel() { return model; }
    public Map<String, Double> getPrices() { return prices; }
    public String getBrand() { return brand; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public double getPriceInCurrency(String currency) {
        return prices.getOrDefault(currency, 0.0);
    }

    @Override
    public String toString() {
        return String.format("Brand: %s | Model: %s | Type: %s | USD: %.2f | Released: %s",
                brand, model, type, getPriceInCurrency("USD"), releaseDate);
    }
}
