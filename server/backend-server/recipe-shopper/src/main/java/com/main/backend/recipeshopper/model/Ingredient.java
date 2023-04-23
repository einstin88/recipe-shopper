package com.main.backend.recipeshopper.model;

import java.time.LocalDateTime;

public class Ingredient extends Product {
    private Integer quantity;

    public Ingredient(String productId, String name, String url, String pack_size, Double price, String img,
            LocalDateTime timeStamp, Integer quantity) {
        super(productId, name, url, pack_size, price, img, timeStamp);
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Ingredients [productId=" + getProductId() + ", name=" + getName() + ", url=" + getUrl() + ", pack_size="
                + getPack_size() + ", price=" + getPrice() + ", img=" + getImg() + ", timeStamp=" + getTimeStamp()
                + "quantity=" + quantity + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ingredient other = (Ingredient) obj;
        if (!getProductId().equals(other.getProductId()))
            return false;
        return true;
    }
}
