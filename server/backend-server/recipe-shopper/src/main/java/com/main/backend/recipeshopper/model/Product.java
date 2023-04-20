package com.main.backend.recipeshopper.model;

import java.time.LocalDateTime;

public record Product(
    String productId,
    String name,
    String url,
    String pack_size,
    Double price,
    String img,
    LocalDateTime timeStamp
) {
    
}
