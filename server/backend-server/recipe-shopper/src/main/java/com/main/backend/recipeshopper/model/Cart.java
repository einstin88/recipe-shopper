package com.main.backend.recipeshopper.model;

import java.util.List;

public record Cart(
    String username,
    String address,
    List<Recipe<Ingredient>> cartItems,
    Double total
) {
    
}
