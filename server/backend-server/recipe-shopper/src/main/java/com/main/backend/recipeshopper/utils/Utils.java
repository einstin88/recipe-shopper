package com.main.backend.recipeshopper.utils;

import java.io.StringReader;
import java.util.UUID;
import java.util.stream.Stream;

import com.main.backend.recipeshopper.model.Product;

import jakarta.json.Json;
import jakarta.json.JsonValue;

public class Utils {
    private static final String PREFIX_PRODUCT = "SS_";
    private static final String PREFIX_RECIPE = "RP_";

    public static String createErrorResponseMsg(String errMsg) {
        return Json.createObjectBuilder()
                .add("error", errMsg)
                .build().toString();
    }

    public static Stream<Product> parseForProducts(String body) {
        return Json.createReader(new StringReader(body))
                .readObject()
                .getJsonArray("results")
                .stream()
                .map(JsonValue::asJsonObject)
                .map(result -> {
                    return new Product(
                            PREFIX_PRODUCT + UUID.randomUUID().toString().substring(0, 8),
                            result.getString("name"),
                            result.getString("url"),
                            result.getString("pack_size"),
                            result.getJsonNumber("price").doubleValue(),
                            result.getString("img"),
                            null);
                });
    }
}
