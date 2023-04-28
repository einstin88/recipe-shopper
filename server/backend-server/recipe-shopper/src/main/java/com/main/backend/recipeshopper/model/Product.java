package com.main.backend.recipeshopper.model;

import java.time.LocalDateTime;

/**
 * POJO to represent details of a product
 */
public class Product {
    private String productId;
    private String name;
    private String url;
    private String pack_size;
    private Double price;
    private String img;
    private LocalDateTime timeStamp;
    
    public Product(String productId, String name, String url, String pack_size, Double price, String img,
            LocalDateTime timeStamp) {
        this.productId = productId;
        this.name = name;
        this.url = url;
        this.pack_size = pack_size;
        this.price = price;
        this.img = img;
        this.timeStamp = timeStamp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPack_size() {
        return pack_size;
    }

    public void setPack_size(String pack_size) {
        this.pack_size = pack_size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", name=" + name + ", url=" + url + ", pack_size=" + pack_size
                + ", price=" + price + ", img=" + img + ", timeStamp=" + timeStamp + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((pack_size == null) ? 0 : pack_size.hashCode());
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
        Product other = (Product) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (pack_size == null) {
            if (other.pack_size != null)
                return false;
        } else if (!pack_size.equals(other.pack_size))
            return false;
        return true;
    }

    

}
