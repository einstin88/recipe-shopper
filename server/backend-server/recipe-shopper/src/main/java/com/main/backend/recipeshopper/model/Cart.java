package com.main.backend.recipeshopper.model;

import java.util.List;

public record Cart(
    List<CartItem> cartItems,
    Double total
) {
    
}
