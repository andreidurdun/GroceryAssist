package com.grocery.assist.model;


public class Ingredient implements Comparable<Ingredient> {
    private Long id;
    private String name;
    private Float quantity;
    private String unit;  //JComboBox<Unit>
    private Long recipeId;

    public Ingredient(){

    }
    public Ingredient(String name, Float quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Long getId() {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUnit()
    {
        return this.unit;
    }

    public Float getQuantity()
    {
        return this.quantity;
    }

    public void setRecipeId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return this.recipeId;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return name.equalsIgnoreCase(that.name); // compara dupa nume
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

}
