package com.grocery.assist.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShoppingList implements Comparable<ShoppingList>{
    private Long id;
    private Date date;
    private List<Product> products;

    public ShoppingList() {
        this.id = (long) -1;
        this.date = new Date();
        this.products = new ArrayList<Product>();
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public void setProducts(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public Long getId() {
        return id;
    }
    public Date getDate() {
        return date;
    }
    public List<Product> getProducts() {
        if (products == null)
            return null;
        return new ArrayList<>(products);
    }
    @Override
    public int compareTo(ShoppingList o) {
        return this.date.compareTo(o.date);
    }
}
