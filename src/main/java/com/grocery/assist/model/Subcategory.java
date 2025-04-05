package com.grocery.assist.model;

import jakarta.persistence.*;

@Entity
public class Subcategory extends Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getLink(){
        return this.link;
    }
}
