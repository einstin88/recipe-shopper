package com.main.backend.recipeshopper.model;

public record Product(
    String id,
    String name,
    String url,
    String pack_size,
    Double price,
    String img
) {
    
}
