package com.grocery.assist.service;

import com.grocery.assist.model.Ingredient;
import com.grocery.assist.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeService {

    public RecipeService() {}

    //metoda care aduna cantitatile ingredientelor dintr o lista de retete
    public List<Ingredient> mergeIngredients(List<Recipe> recepies) {
        HashMap<Ingredient, Float> mergedIngredients = new HashMap<>();
        for(Recipe recepie : recepies)
            for(Ingredient ingredient : recepie.getIngredients())
                if(mergedIngredients.containsKey(ingredient))
                    mergedIngredients.put(ingredient, mergedIngredients.get(ingredient) + ingredient.getQuantity());
                else
                    mergedIngredients.put(ingredient, ingredient.getQuantity());

        List<Ingredient> ingredients = new ArrayList<>();
        for(Ingredient ingredient : mergedIngredients.keySet()) {

            Ingredient newIngredient = new Ingredient();
            newIngredient.setProductName(ingredient.getProductName());
            newIngredient.setQuantity(mergedIngredients.get(ingredient));
            newIngredient.setUnit(ingredient.getUnit());
            ingredients.add(newIngredient);
        }
        return ingredients;
    }

    public static void main(String[] args) {
        // Test the mergeIngredients method
        List<Recipe> recepies = new ArrayList<>();
        // Add some test recepies to the list
        List<Ingredient> ingredients1 = new ArrayList<>();
        ingredients1.add(new Ingredient("Tomato", 2.0f, "g"));
        ingredients1.add(new Ingredient("Onion", 1.0f, "g"));
        recepies.add(new Recipe("Salad", ingredients1));
        List<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(new Ingredient("Tomato", 3.0f, "g"));
        ingredients2.add(new Ingredient("Cucumber", 1.0f, "g"));
        recepies.add(new Recipe("Greek Salad", ingredients2));

        RecipeService recipeService = new RecipeService();
        List<Ingredient> mergedIngredients = recipeService.mergeIngredients(recepies);

        // Print the merged ingredients
        for (Ingredient ingredient : mergedIngredients) {
            System.out.println(ingredient);
        }
    }

}
