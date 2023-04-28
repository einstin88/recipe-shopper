package com.main.backend.recipeshopper.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * POJO to represent details of a recipe
 */
public record Recipe<I extends Product>(
                String recipeId,
                String recipeName,
                String recipeCreator,
                String procedures,
                List<I> ingredients,
                LocalDateTime timeStamp) {

}
