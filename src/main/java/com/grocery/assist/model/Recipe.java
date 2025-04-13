package com.grocery.assist.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private Long id;
    private String name;
    private List<Ingredient> ingredients;

    public Recipe() {
        ingredients = new ArrayList<Ingredient>();
    }
    public Recipe(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = new ArrayList<Ingredient>(ingredients);
    }

    public String getName() {
        return this.name;
    }

    public List<Ingredient> getIngredients() {
        return (new ArrayList<>(this.ingredients));
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }
    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Recepie: ").append(name).append("\n");
//        for (Ingredient ingredient : ingredients) {
//            sb.append(ingredient.toString()).append("\n");
//        }
//        return sb.toString();

        return this.name;
    }
}
