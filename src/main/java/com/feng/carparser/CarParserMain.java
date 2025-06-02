package com.feng.carparser;

import com.feng.carparser.model.BrandInfo;
import com.feng.carparser.model.Car;
import com.feng.carparser.model.Currency;
import com.feng.carparser.model.SortType;
import com.feng.carparser.output.OutputFormatter;
import com.feng.carparser.parser.CarBrandCsvParser;
import com.feng.carparser.parser.CarTypeXmlParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class CarParserMain {

    private static final Set<String> VALID_ARGS = Set.of(
            "--input-xml", "--input-csv", "--brand",
            "--minPrice", "--maxPrice", "--after",
            "--before", "--sort", "--type", "--currency",
            "--output"
    );

    private static Set<String> validSortValues = Set.of("price", "date", "optional");
    private static Set<String> validTypeValues = Set.of("SUV", "Sedan", "Truck");
    private static Set<String> validCurrencyValues = Set.of("USD", "EUR", "GBP", "JPY");
    private static Set<String> validOutputValues = Set.of("table", "xml", "json");



    public static void main(String[] args) throws Exception {
        String xmlPath = null;
        String csvPath = null;

        String filterBrand = null;
        Double minPrice = null;
        Double maxPrice = null;
        LocalDate afterDate = null;
        LocalDate beforeDate = null;
        String sortBy = "price";
        String type = "SUV";
        String currency = "USD";
        String outputFormat = "table";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,dd,MM");

        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=", 2);
                String key = parts[0];
                if (!VALID_ARGS.contains(key)) {
                    System.err.println("Invalid argument: " + key);
                    printUsage();
                    return;
                }
                if (arg.startsWith("--input-xml=")) {
                    xmlPath = arg.split("=", 2)[1];
                }
                if (arg.startsWith("--input-csv=")) {
                    csvPath = arg.split("=", 2)[1];
                }
                if (arg.startsWith("--brand=")) {
                    filterBrand = arg.split("=", 2)[1];
                }
                if (arg.startsWith("--minPrice=")) {
                    minPrice = Double.parseDouble(arg.split("=", 2)[1]);
                }
                if (arg.startsWith("--maxPrice=")) {
                    maxPrice = Double.parseDouble(arg.split("=", 2)[1]);
                }

                try {
                    if (arg.startsWith("--after=")) {
                        afterDate = LocalDate.parse(arg.split("=", 2)[1], formatter);
                    }
                    if (arg.startsWith("--before=")) {
                        beforeDate = LocalDate.parse(arg.split("=", 2)[1], formatter);
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Error: Invalid date format in argument '" + arg + "'. Expected format: yyyy,dd,MM");
                    printUsage();
                    return;
                }

                if (arg.startsWith("--sort=")) {
                    sortBy = arg.split("=", 2)[1];
                    if (!validSortValues.contains(sortBy)) {
                        System.err.println("Error: Invalid value for --sort: '" + sortBy + "'");
                        System.err.println("Valid values are: price, date, optional");
                        printUsage();
                        return;
                    }
                }
                if (arg.startsWith("--type=")) {
                    type = arg.split("=", 2)[1];
                    if (!validTypeValues.contains(type)) {
                        System.err.println("Error: Invalid value for --type: '" + type + "'");
                        System.err.println("Valid values are: SUV, Sedan, Truck");
                        printUsage();
                        return;
                    }
                }
                if (arg.startsWith("--currency=")) {
                    currency = arg.split("=", 2)[1];
                    if (!validCurrencyValues.contains(currency)) {
                        System.err.println("Error: Invalid value for --currency: '" + currency + "'");
                        System.err.println("Valid values are: SUV, Sedan, Truck");
                        printUsage();
                        return;
                    }
                }
                if (arg.startsWith("--output=")) {
                    outputFormat = arg.split("=", 2)[1];
                    if (!validOutputValues.contains(outputFormat)) {
                        System.err.println("Error: Invalid value for --output: '" + outputFormat + "'");
                        System.err.println("Valid values are: table, xml, json");
                        printUsage();
                        return;
                    }
                }
            } else {
                System.err.println("Error: Unrecognized argument format: " + arg);
                printUsage();
                return;
            }
        }

        final String finalFilterBrand = filterBrand;
        final Double finalMinPrice = minPrice;
        final Double finalMaxPrice = maxPrice;
        final LocalDate finalAfterDate = afterDate;
        final LocalDate finalBeforeDate = beforeDate;
        final String finalType = type;
        final String finalCurrency = currency;

        List<Car> fullCars = CarTypeXmlParser.parse(xmlPath);
        List<Car> cars = fullCars;
        List<BrandInfo> brandInfos = CarBrandCsvParser.parse(csvPath);

        for (int i = 0; i < cars.size(); i++) {
            cars.get(i).setBrand(brandInfos.get(i).getBrand());
            cars.get(i).setReleaseDate(brandInfos.get(i).getReleaseDate());
        }

        if (finalFilterBrand != null) {
            cars = cars.stream()
                    .filter(c -> c.getBrand().equalsIgnoreCase(finalFilterBrand))
                    .collect(Collectors.toList());
        }

        if (finalMinPrice != null) {
            cars = cars.stream()
                    .filter(c -> c.getPriceInCurrency(Currency.USD.getFieldName()) >= finalMinPrice)
                    .collect(Collectors.toList());
        }

        if (finalMaxPrice != null) {
            cars = cars.stream()
                    .filter(c -> c.getPriceInCurrency(Currency.USD.getFieldName()) <= finalMaxPrice)
                    .collect(Collectors.toList());
        }

        if (finalAfterDate != null) {
            cars = cars.stream()
                    .filter(c -> c.getReleaseDate().isAfter(finalAfterDate))
                    .collect(Collectors.toList());
        }

        if (finalBeforeDate != null) {
            cars = cars.stream()
                    .filter(c -> c.getReleaseDate().isBefore(finalBeforeDate))
                    .collect(Collectors.toList());
        }

        if (sortBy != null) {
            if (sortBy.equalsIgnoreCase(SortType.PRICE.getFieldName())) {
                cars.sort(Comparator.comparingDouble(c -> -c.getPriceInCurrency(Currency.USD.getFieldName())));
            } else if (sortBy.equalsIgnoreCase(SortType.DATE.getFieldName())) {
                cars.sort(Comparator.comparing(Car::getReleaseDate).reversed());
            } else if (sortBy.equalsIgnoreCase(SortType.OPTIONAL.getFieldName())) {
                fullCars = fullCars.stream().filter(c -> c.getType().equalsIgnoreCase(finalType)).collect(Collectors.toList());
                fullCars.sort(Comparator.comparingDouble(c -> -c.getPriceInCurrency(finalCurrency)));
                cars = fullCars;
            }
        }

        OutputFormatter.print(cars, outputFormat);
    }

    private static void printUsage() {
        System.out.println("""
Usage:
  java -jar CarParser-1.0.0-SNAPSHOT-jar-with-dependencies.jar [options]

Required:
  --input-xml=FILE         [REQUIRED] Specify the input car XML file
  --input-csv=FILE         [REQUIRED] Specify the brand and release date CSV file
  
Optional Filters:
  --brand=BRAND            Filter by car brand
  --minPrice=VALUE         Minimum price (USD)
  --maxPrice=VALUE         Maximum price (USD)
  --after=YYYY-MM-DD       Filter by release date after this date
  --before=YYYY-MM-DD      Filter by release date before this date
  --type=TYPE              Filter all cars by type (SUV, Sedan, Truck) — only used when sort=optional
  --currency=CURRENCY      Filter all cars by currency (USD, EUR, GBP, JPY) — only used when sort=optional

Sorting:
  --sort=FIELD             Sort by field (price, date, optional)
  
Output:
  --output=FORMAT          Output format: table | json | xml

Examples:
  java -jar CarParser-1.0.0-SNAPSHOT-jar-with-dependencies.jar --input-xml=CarTypes.xml --input-csv=BrandDate.csv --brand=Toyota --after=2023-01-01 --sort=price --output=table

  java -jar CarParser-1.0.0-SNAPSHOT-jar-with-dependencies.jar --input-xml=CarTypes.xml --input-csv=BrandDate.csv --sort=optional --type=SUV --currency=USD --output=xml
""");
    }
}
