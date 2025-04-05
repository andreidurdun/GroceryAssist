package com.grocery.assist.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany
    private Set<Subcategory> subcategories;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products; // e.g., ["milk", "cheese", "yogurt"]

    public Category() {}
    public Category(String name, List<Product> products) {
        this.name = name;
        this.products = new ArrayList<Product>(products);
    }

    public String getName(){
        return this.name;
    }


    public List<Product> getProducts(){
        return (new ArrayList<Product>(this.products));
    }
}
