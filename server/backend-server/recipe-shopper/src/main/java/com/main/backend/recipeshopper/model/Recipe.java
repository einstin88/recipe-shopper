package com.main.backend.recipeshopper.model;

import java.util.List;

public record Recipe(
        String recipeId,
        String name,
        List<Product> ingredients) {

}
