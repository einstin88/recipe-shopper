package com.main.backend.recipeshopper.model;

import java.time.LocalDateTime;
import java.util.List;

public record RecipeDetails(
        String recipeId,
        String recipeName,
        String recipeCreator,
        List<Product> ingredients,
        LocalDateTime timeStamp) {

}
