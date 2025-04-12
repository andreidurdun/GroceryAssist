package com.grocery.assist.model;

import java.util.ArrayList;
import java.util.List;

public class Recepie {
    private Long id;
    private String name;
    private List<Ingredient> ingredients;

    public Recepie() {
        ingredients = new ArrayList<Ingredient>();
    }
    public Recepie(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = new ArrayList<Ingredient>(ingredients);
    }

    public String getName() {
        return this.name;
    }

    public List<Ingredient> getIngredients() {
        return (new ArrayList<>(this.ingredients));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recepie: ").append(name).append("\n");
        for (Ingredient ingredient : ingredients) {
            sb.append(ingredient.toString()).append("\n");
        }
        return sb.toString();
    }
}
