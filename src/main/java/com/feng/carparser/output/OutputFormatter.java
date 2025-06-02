package com.feng.carparser.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feng.carparser.model.Car;
import com.feng.carparser.model.Currency;

import java.util.List;
import java.util.Map;

public class OutputFormatter {
    public static void print(List<Car> cars, String format) throws Exception {
        switch (format.toLowerCase()) {
            case "json" -> {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cars));
            }
            case "xml" -> {
                XmlMapper xmlMapper = new XmlMapper();
                xmlMapper.registerModule(new JavaTimeModule());
                xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                CarListWrapper wrapper = new CarListWrapper(cars); // 假设你已有 List<Car>
                System.out.println(xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper));
            }
            default -> printTable(cars);
        }
    }

    public static void printTable(List<Car> cars) {
        System.out.printf("%-10s %-10s %-12s %-12s %-12s %-12s %-15s%n",
                "Type", "Model", "Price(USD)", "Price(EUR)", "Price(GBP)", "Price(JPY)", "Release Date");

        for (Car car : cars) {
            String type = car.getType();
            String model = car.getModel();
            Map<String, Double> prices = car.getPrices();

            String usd = prices.containsKey(Currency.USD.getFieldName()) ? String.format("%.2f", prices.get(Currency.USD.getFieldName())) : "-";
            String eur = prices.containsKey(Currency.EUR.getFieldName()) ? String.format("%.2f", prices.get(Currency.EUR.getFieldName())) : "-";
            String gbp = prices.containsKey(Currency.GBP.getFieldName()) ? String.format("%.2f", prices.get(Currency.GBP.getFieldName())) : "-";
            String jpy = prices.containsKey(Currency.JPY.getFieldName()) ? String.format("%.0f", prices.get(Currency.JPY.getFieldName())) : "-";

            String releaseDate = car.getReleaseDate() != null ? car.getReleaseDate().toString() : "-";

            System.out.printf("%-10s %-10s %-12s %-12s %-12s %-12s %-15s%n",
                    type, model, usd, eur, gbp, jpy, releaseDate);
        }
    }
}
