package com.grocery.assist.service;

import com.grocery.assist.model.Ingredient;
import com.grocery.assist.model.Product;
import com.grocery.assist.model.Recipe;
import com.grocery.assist.model.ShoppingList;
import com.grocery.assist.repository.ShoppingListRepository;


import java.sql.SQLException;
import java.util.*;

public class ShoppingListService {
    private final ShoppingListRepository repository;
    private final IngredientService ingredientService;
    private final RecipeService recepieService;

    public ShoppingListService() throws SQLException {
        this.repository = new ShoppingListRepository();
        this.ingredientService = new IngredientService();
        this.recepieService = new RecipeService();
    }

    //creeaza o lista de cumparaturi
    public ShoppingList createShoppingListFromProducts(List<Product> products) throws SQLException {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setDate(new Date());
        List<Ingredient> mergedProducts = new ArrayList<>();

        for (Product product : products) {
            if(product instanceof Ingredient)
                mergedProducts.add((Ingredient) product);
        }

        mergedProducts = ingredientService.mergeIngredients(mergedProducts);
        //transformam inapoi in lista de produse
        List<Product> finalProducts = new ArrayList<>();
        for(Ingredient ingredient : mergedProducts) {
            finalProducts.add(ingredient);
        }
        shoppingList.setProducts(finalProducts);
        Long id = repository.save(shoppingList);
        shoppingList.setId(id);
        return shoppingList;
    }
    public ShoppingList createShoppingListFromRecepies(List<Recipe> recepies) throws SQLException {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setDate(new Date());
        List<Ingredient> mergedIngredients = recepieService.mergeIngredients(recepies);
        //transformam inapoi in lista de produse
        List<Product> finalProducts = new ArrayList<>();
        for (Ingredient ingredient : mergedIngredients) {
            finalProducts.add(ingredient);
        }
        shoppingList.setProducts(finalProducts);
        Long id = repository.save(shoppingList);
        shoppingList.setId(id);
        return shoppingList;
    }

    public ShoppingList createShoppingList(List<Product> products, List<Recipe> recepies) throws SQLException {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setDate(new Date());
        List<Ingredient> mergedProducts = new ArrayList<>();

        for (Product product : products) {
            if(product instanceof Ingredient)
                mergedProducts.add((Ingredient) product);
        }

        mergedProducts = ingredientService.mergeIngredients(mergedProducts);
        List<Ingredient> mergedRecepies = recepieService.mergeIngredients(recepies);
        mergedRecepies.addAll(mergedProducts);
        List<Ingredient> mergedIngredients = new ArrayList<>();
        mergedIngredients = ingredientService.mergeIngredients(mergedRecepies);

        //transformam inapoi in lista de produse
        List<Product> finalProducts = new ArrayList<>();
        for(Ingredient ingredient : mergedIngredients) {
            finalProducts.add(ingredient);
        }

        shoppingList.setProducts(finalProducts);
        Long id = repository.save(shoppingList);
        shoppingList.setId(id);
        return shoppingList;
    }


    public static void main(String[] args) throws SQLException {
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

        List<Product> ingredients = new ArrayList<>();
        // Add some test ingredients to the list
        ingredients.add(new Ingredient("Tomato", 2.0f, "g"));
        ingredients.add(new Ingredient("Onion", 1.0f, "g"));
        ingredients.add(new Ingredient("Tomato", 3.0f, "g"));
        ingredients.add(new Ingredient("Cucumber", 1.0f, "g"));

        ShoppingListService serv = new ShoppingListService();

        ShoppingList shoppingList = new ShoppingList();
        shoppingList = serv.createShoppingListFromRecepies(recepies);

        System.out.println("Shopping List:");

        for (Product product : shoppingList.getProducts()) {
            System.out.println(product);
        }
    }

}
