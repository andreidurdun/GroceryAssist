package com.grocery.assist.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShoppingList implements Comparable<ShoppingList>{
    private Long id;
    private Date date;
    private List<Product> products;

    public ShoppingList() {

        this.date = new Date();
        this.products = new ArrayList<>();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Shopping List: ").append(date).append("\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        return sb.toString();
    }
}
