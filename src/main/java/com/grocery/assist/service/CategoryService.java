package com.grocery.assist.service;

import com.grocery.assist.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private List<Category> categories;

    public CategoryService() {}
    public CategoryService(List<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }
    public void add(Category category) {
        this.categories.add(category);
    }
    public void remove(Category category) {
        this.categories.remove(category);
    }
    public List<Category> getCategories() {
        return (new ArrayList<Category>(this.categories));
    }
}
