package com.feng.carparser.parser;

import com.feng.carparser.model.Car;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feng.carparser.model.CarFieldInXml;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class CarTypeXmlParser {
    public static List<Car> parse(String filePath) throws Exception {
        List<Car> cars = new ArrayList<>();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
        NodeList carNodes = doc.getElementsByTagName(CarFieldInXml.CAR.getFieldName());

        for (int i = 0; i < carNodes.getLength(); i++) {
            Element carElement = (Element) carNodes.item(i);
            String type = getText(carElement, CarFieldInXml.TYPE.getFieldName());
            String model = getText(carElement, CarFieldInXml.MODEL.getFieldName());

            Map<String, Double> prices = new HashMap<>();
            Element mainPrice = (Element) carElement.getElementsByTagName(CarFieldInXml.PRICE.getFieldName()).item(0);
            prices.put(mainPrice.getAttribute(CarFieldInXml.CURRENCY.getFieldName()), Double.parseDouble(mainPrice.getTextContent()));

            NodeList priceNodes = carElement.getElementsByTagName(CarFieldInXml.PRICES.getFieldName()).item(0).getChildNodes();
            for (int j = 0; j < priceNodes.getLength(); j++) {
                if (priceNodes.item(j) instanceof Element) {
                    Element p = (Element) priceNodes.item(j);
                    prices.put(p.getAttribute(CarFieldInXml.CURRENCY.getFieldName()), Double.parseDouble(p.getTextContent()));
                }
            }
            cars.add(new Car(type, model, prices));
        }
        return cars;
    }

    private static String getText(Element parent, String tag) {
        return parent.getElementsByTagName(tag).item(0).getTextContent();
    }
}
