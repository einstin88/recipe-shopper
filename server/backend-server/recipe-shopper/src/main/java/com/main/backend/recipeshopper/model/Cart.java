package com.main.backend.recipeshopper.model;

import java.util.List;

public record Cart(
    String username,
    List<CartItem> cartItems,
    Double total
) {
    
}
