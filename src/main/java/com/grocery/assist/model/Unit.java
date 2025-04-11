package com.grocery.assist.model;

public enum Unit {
    G("g"),
    BUC("buc");

    private final String label;

    Unit(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
