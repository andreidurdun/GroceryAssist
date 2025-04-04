package com.grocery.assist.model;

import jakarta.persistence.*;

@Entity
public class Subcategory extends Category {

    private String link;

    @ManyToOne
    private Category category;

    public Subcategory() {
        super();
    }
    public Subcategory(String link) {
        super();
        this.link = link;
    }
}
