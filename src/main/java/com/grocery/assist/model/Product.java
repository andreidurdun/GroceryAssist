package com.grocery.assist.model;

import jakarta.persistence.*;



public class Product {
    private Long id;
    private String productName;
    private double price;
    private Long category_id;

    public Product() {
        this.productName = "";
        this.price = 0.0;
        this.category_id = null;
    }
    public Product(String productName) {
        this.price = 0.0;
        this.category_id = null;
        this.productName = productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(Long category_id) {
        this.category_id = category_id;
    }

    public Long getId() {
        return  this.id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return category_id;
    }

    @Override
    public String toString() {
        return this.productName;
    }
}
