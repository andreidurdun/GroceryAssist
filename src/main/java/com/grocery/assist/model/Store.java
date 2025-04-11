package com.grocery.assist.model;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private Long id;
    private String name;
    private List<Category> categories;

    public Store() {
        this.name = "";
        this.categories = new ArrayList<Category>();
    }

    public Store(String name, List<Category> categories) {
        this.name = name;
        this.categories = new ArrayList<Category>();
        this.categories.addAll(categories);
    }

    public String getName(){
        return this.name;
    }

    public List<Category> getCategories(){
        return (new ArrayList<Category>(this.categories));
    }

    public void setName(String name){
        this.name = name;
    }
    public void setCategories(List<Category> categories){
        this.categories = new ArrayList<Category>(categories);
    }

}
