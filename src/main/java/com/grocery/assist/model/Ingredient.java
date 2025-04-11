package com.grocery.assist.model;


public class Ingredient implements Comparable<Ingredient> {
    private long id;
    private String name;
    private Float quantity;
    private Unit unit;  //JComboBox<Unit>

    public Ingredient(){}
    public Ingredient(String name, Float quantity, Unit unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    @Override
    public int compareTo(Ingredient o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return this.name + " " + this.quantity + " " + this.unit;
    }
}
