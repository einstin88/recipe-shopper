package com.main.backend.recipeshopper.model;

import java.time.LocalDateTime;
import java.util.List;

public record Recipe(
                String recipeId,
                String recipeName,
                String recipeCreator,
                List<String> ingredients,
                LocalDateTime timeStamp) {

}
