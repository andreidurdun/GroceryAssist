package com.grocery.assist.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Category {
    private Long id;
    private String name;
    private String link;


    protected List<Product> products; // e.g., ["milk", "cheese", "yogurt"]

    public Category() {
        this.products = new ArrayList<Product>();
        this.id = (long) -1;
        this.name = "";
    }
    public Category(String name, List<Product> products) {
        this.id = (long) -1;
        this.name = name;
        this.products = new ArrayList<Product>(products);
    }
    public Category(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getLink()
    {
        return this.link;
    }
    public List<Product> getProducts(){
        return (new ArrayList<Product>(this.products));
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLink(String link){
        this.link = link;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

}
