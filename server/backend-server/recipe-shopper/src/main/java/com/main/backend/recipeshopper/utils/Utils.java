package com.main.backend.recipeshopper.utils;

import java.io.StringReader;
import java.util.UUID;
import java.util.stream.Stream;

import com.main.backend.recipeshopper.model.Ingredient;
import com.main.backend.recipeshopper.model.Product;
import com.main.backend.recipeshopper.model.Recipe;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
    private static final String PREFIX_PRODUCT = "SS_";
    private static final String PREFIX_RECIPE = "RP_";

    /**
     * 
     * @return
     */
    public static String serverOkResponse() {
        return Json.createObjectBuilder()
                .add("message", "Server is up!")
                .build().toString();
    }

    // TODO: Function is not used, tb be deleted
    /**
     * @deprecated
     * @param errMsg
     * @return
     */
    // private static String createErrorResponseMsg(String errMsg) {
    //     return Json.createObjectBuilder()
    //             .add("error", errMsg)
    //             .build().toString();
    // }

    /**
     * 
     * @param body
     * @return
     */
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

    /**
     * 
     * @param recipe
     * @return
     */
    public static Recipe<Ingredient> generateRecipeId(Recipe<Ingredient> recipe) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        return new Recipe<>(
                PREFIX_RECIPE + id,
                recipe.recipeName(),
                recipe.recipeCreator(),
                recipe.procedures(),
                recipe.ingredients(),
                null);
    }

    public static <T extends RuntimeException> void generateServerError(
            String errMsg, Class<T> exceptionClass, Object... args) {

        errMsg = errMsg.formatted(args);
        log.info("--- {}", errMsg);
        try {
            throw exceptionClass
                    .getConstructor(String.class)
                    .newInstance(errMsg);

        } catch (Exception e) {
            log.error("Internal Error! {}", e.getMessage());
        }
    }
}
