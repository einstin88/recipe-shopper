package com.main.backend.recipeshopper.exceptions;

public class ProductUpsertException extends RuntimeException {
    public ProductUpsertException(String msg) {
        super(msg);
    }
}
