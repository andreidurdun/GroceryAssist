package com.grocery.assist.model;


public class Ingredient extends Product implements Comparable<Ingredient> {
    private Float quantity;
    private String unit;  //JComboBox<Unit>
    private Long recepieId;

    public Ingredient(){
        super();
    }

    public Ingredient(String name, Float quantity, String unit) {
        super(name);
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getUnit()
    {
        return this.unit;
    }

    public Float getQuantity()
    {
        return this.quantity;
    }

    public void setRecepieId(Long id) {
        this.recepieId = id;
    }

    public Long getRecepieId() {
        return this.recepieId;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }


    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public int compareTo(Ingredient o) {
        return this.getProductName().compareTo(o.getProductName());
    }

    @Override
    public String toString() {
        return this.getProductName() + " " + this.quantity + " " + this.unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return getProductName().equalsIgnoreCase(that.getProductName()); // compara dupa nume
    }

    @Override
    public int hashCode() {
        return getProductName().toLowerCase().hashCode();
    }

}
