
# 📦 Car Data Parser CLI

A command-line Java application that parses car information from XML and CSV files, filters and sorts the data based on user input, and outputs the results in JSON, XML, or table format.

---

## ✅ Features

- Parse car data from both XML and CSV
- Filter by brand, price range, release date
- Sort by year (latest to oldest), price (high to low), or optional(sort all by type and currency)
- Output results in **table**, **XML**, or **JSON**
- Type-based sorting in specific currencies (SUV in EUR, Sedan in JPY, Truck in USD)

---

## 🗂 Input Format

### 1. XML file (`--input-xml`)
Contains car types, models, and prices in multiple currencies.

### 2. CSV file (`--input-csv`)
Contains brand and release date per car, matching the order of XML entries.

---

## 🚀 Usage

```
java -jar CarParser-1.0.0-SNAPSHOT-jar-with-dependencies.jar --input-xml=CarTypes.xml --input-csv=BrandDates.csv \
  [--brand=Toyota] [--minPrice=20000] [--maxPrice=50000] \
  [--after=2023,01,01] [--before=2023,31,12] \
  [--sort=year|price|optional] \
  [--type=SUV|Sedan|Truck] [--currency=USD|EUR|GBP|JPY] \
  [--output=table|xml|json]
```

---

## 🧾 Parameters

| Parameter       | Required | Description                                                   |
|----------------|----------|---------------------------------------------------------------|
| `--input-xml`   | ✅ Yes   | Path to the XML file                                          |
| `--input-csv`   | ✅ Yes   | Path to the CSV file                                          |
| `--brand`       | ❌ No    | Filter by brand name                                          |
| `--minPrice`    | ❌ No    | Filter by minimum price (in USD)                              |
| `--maxPrice`    | ❌ No    | Filter by maximum price (in USD)                              |
| `--after`       | ❌ No    | Filter cars released after this date (`yyyy,dd,MM`)           |
| `--before`      | ❌ No    | Filter cars released before this date (`yyyy,dd,MM`)          |
| `--sort`        | ❌ No    | Sort by `year`, `price`, or `optional`                        |
| `--type`        | ❌ *Only when `--sort=type-currency`* | The car type to sort (`SUV`, `Sedan`, `Truck`)                |
| `--currency`    | ❌ *Only when `--sort=type-currency`* | Currency to sort in (`USD`, `EUR`, `GBP`, `JPY`)              |
| `--output`      | ❌ No    | Output format: `table`, `xml`, or `json` (default is `table`) |

---

## 📌 Example

```
java -jar CarParser-1.0.0-SNAPSHOT-jar-with-dependencies.jar \
  --input-xml=CarTypes.xml --input-csv=BrandDates.csv \
  --brand=Toyota --minPrice=20000 --sort=price \
  --output=json
```

---

## 📦 Build Instructions

Use Maven to build a fat jar including all dependencies:

```
mvn clean package
```

Jar file output:

```
target/CarParser-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 🧱 Dependencies

- Java 17+
- OpenCSV
- Jackson (core + datatype-jsr310)

---

## ⚠️ Error Handling

- Unrecognized command-line options will result in a usage message

---

