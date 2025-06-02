package com.feng.carparser.model;

import java.time.LocalDate;

public class BrandInfo {
    private String brand;
    private LocalDate releaseDate;

    public BrandInfo(String brand, LocalDate releaseDate) {
        this.brand = brand;
        this.releaseDate = releaseDate;
    }

    public String getBrand() { return brand; }
    public LocalDate getReleaseDate() { return releaseDate; }
}
