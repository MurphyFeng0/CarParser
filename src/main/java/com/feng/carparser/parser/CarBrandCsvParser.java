package com.feng.carparser.parser;


import com.feng.carparser.model.BrandInfo;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CarBrandCsvParser {
    public static List<BrandInfo> parse(String path) throws Exception {
        List<BrandInfo> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            reader.skip(1);

            List<String[]> records = reader.readAll();
            for (String[] line : records) {
                String[] lineArray = line[0].split(",");
                String brand = lineArray[0];
                String date = lineArray[1];
                list.add(new BrandInfo(brand, LocalDate.parse(date, formatter)));
            }
        }
        return list;
    }
}
