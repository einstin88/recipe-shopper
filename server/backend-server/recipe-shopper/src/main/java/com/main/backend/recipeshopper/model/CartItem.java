package com.main.backend.recipeshopper.model;

import java.util.List;

public record CartItem(
        String recipeId,
        String recipeName,
        String recipeCreator,
        List<CartIngredient> ingredients) {

}
