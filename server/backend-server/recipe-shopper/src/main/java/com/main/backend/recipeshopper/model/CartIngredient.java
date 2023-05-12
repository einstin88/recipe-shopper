package com.main.backend.recipeshopper.model;

import java.time.LocalDateTime;

public class CartIngredient extends Ingredient {
    private Double total;

    public CartIngredient(String productId, String name, String url, String pack_size, Double price, String img,
            LocalDateTime timeStamp, Integer quantity, Double total) {
        super(productId, name, url, pack_size, price, img, timeStamp, quantity);
        this.total = total;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CartIngredient [productId=" + getProductId() + ", name=" + getName() + ", url=" + getUrl()
                + ", pack_size=" + getPack_size() + ", price=" + getPrice() + ", img=" + getImg() + ", timeStamp="
                + getTimeStamp() + "quantity=" + getQuantity() + "total=" + total + "]";
    }

}
