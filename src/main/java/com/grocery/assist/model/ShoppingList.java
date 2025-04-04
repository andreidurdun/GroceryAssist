package com.grocery.assist.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private final List<String> rawItems;       // User's raw input (e.g., ["milk", "bread"])

    @OneToMany
    private List<Product> resolvedItems; // Auto-matched products

    private List<String> unresolvedItems; // Items not recognized, e.g., typos like "melk"



    public ShoppingList(List<String> rawItems) {
        this.rawItems = new ArrayList<>(rawItems);
    }

    public List<String> getRawItems(){
        return (new ArrayList<String>(this.rawItems));
    }

    public List<Product> getResolvedItems(){
        return (new ArrayList<Product>(this.resolvedItems));
    }

    public List<String> getUnresolvedItems(){
        return (new ArrayList<String>(this.unresolvedItems));
    }
}
