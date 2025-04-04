package com.grocery.assist.model;

import jakarta.persistence.*;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private double price;

    @ManyToOne(cascade = CascadeType.ALL)//@JoinColumn(name = "category_id") implicit frkey = [nume_camp] + "_" + [nume_cheie_primara_a_entitatii_tinta]
    private Category category;

    @ManyToOne
    private ShoppingList shoppingList;

    public Product() {}
    public Product(String productName) {
        this.productName = productName;
    }
}
