package com.grocery.assist.service;

import com.grocery.assist.model.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.jsoup.internal.StringUtil.isNumeric;

public class IngredientService {

    public IngredientService() {}

    public List<Ingredient> mergeIngredients(List<Ingredient> ingredients) {
        HashMap<Ingredient, Float> mergedIngredients = new HashMap<>();

        for(Ingredient ingredient : ingredients)
            if(mergedIngredients.containsKey(ingredient))
                mergedIngredients.put(ingredient, mergedIngredients.get(ingredient) + ingredient.getQuantity());
            else
                mergedIngredients.put(ingredient, ingredient.getQuantity());

        List<Ingredient> ingredientsFinal = new ArrayList<>();
        for(Ingredient ingredient : mergedIngredients.keySet()) {
            Ingredient newIngredient = new Ingredient();
            newIngredient.setProductName(ingredient.getProductName());
            newIngredient.setQuantity(mergedIngredients.get(ingredient));
            newIngredient.setUnit(ingredient.getUnit());
            ingredientsFinal.add(newIngredient);
        }
        return ingredientsFinal;
    }

    public Ingredient ingredientParser (String ing) {
        Ingredient ingredient = new Ingredient();
        String[] split = ing.trim().split(" ");

        StringBuilder ingName = new StringBuilder();
        int i = 0;
        while(i < split.length && !isNumeric(split[i])) {
            ingName.append(split[i]).append(" ");
            i++;
        }
        if(i >= split.length) {
            ingredient.setProductName(ingName.toString());
            return ingredient;
        }
        Float quantity = Float.parseFloat(split[i]);
        String unit = ing.trim().split(" ")[i+1];
        ingredient.setProductName(ingName.toString());
        ingredient.setQuantity(quantity);
        ingredient.setUnit(unit);

        return ingredient;
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Float.parseFloat(str); // sau Double.parseDouble(str)
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        // Test the mergeIngredients method
        List<Ingredient> ingredients = new ArrayList<>();
        // Add some test ingredients to the list
//        ingredients.add(new Ingredient("Tomato", 2.0f, "g"));
//        ingredients.add(new Ingredient("Onion", 1.0f, "g"));
//        ingredients.add(new Ingredient("Tomato", 3.0f, "g"));
//        ingredients.add(new Ingredient("Cucumber", 1.0f, "g"));
//
//        IngredientService ingredientService = new IngredientService();
//        List<Ingredient> mergedIngredients = ingredientService.mergeIngredients(ingredients);
//
//        // Print the merged ingredients
//        for (Ingredient ingredient : mergedIngredients) {
//            System.out.println(ingredient);
//        }

        // Test the ingredientParser method
        String testIngredient = "Tomato 2.0 g";
        IngredientService ingredientService = new IngredientService();
        Ingredient parsedIngredient = ingredientService.ingredientParser(testIngredient);
        System.out.println("Parsed Ingredient: " + parsedIngredient);
    }
}
