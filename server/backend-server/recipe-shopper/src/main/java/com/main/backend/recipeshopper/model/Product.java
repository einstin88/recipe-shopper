package com.main.backend.recipeshopper.model;

public record Product(
    String productId,
    String name,
    String url,
    String pack_size,
    Double price,
    String img
) {
    
}
