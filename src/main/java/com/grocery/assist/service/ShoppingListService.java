package com.grocery.assist.service;

import com.grocery.assist.model.*;
import com.grocery.assist.repository.CategoryRepository;
import com.grocery.assist.repository.ShoppingListRepository;


import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class ShoppingListService {
    private final ShoppingListRepository repository;
    private final IngredientService ingredientService;
    private final RecipeService recepieService;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ShoppingListService() throws SQLException {
        this.repository = ShoppingListRepository.getInstance();
        this.ingredientService = new IngredientService();
        this.recepieService = new RecipeService();
        this.productService = new ProductService();
        this.categoryRepository = CategoryRepository.getInstance();
    }

    //creeaza o lista de cumparaturi
    public ShoppingList createShoppingListFromProducts(List<Product> products) throws SQLException, IOException {
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
        //asignam categoriile produselor
        try {
            finalProducts = productService.assignCategories(finalProducts);
        }
        catch(Exception e){
            System.out.println("Porneste serverul de clasificare!!!!!!!!");
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

    public String printShoppingList(ShoppingList shoppingList) throws SQLException {
        AuditService.getInstance().log("Action: Print shopping list");

        StringBuilder sb = new StringBuilder();
        sb.append("Shopping List: ").append(shoppingList.getDate()).append("\n\n");
        List<Product> products = shoppingList.getProducts();
        Map<Long, List<Product>> categories_products = new HashMap<>();
        for (Product product : products) {
            categories_products
                    .computeIfAbsent(product.getCategoryId(), k -> new ArrayList<>())
                    .add(product);
        }

        for (Map.Entry<Long, List<Product>> entry : categories_products.entrySet()) {
            Long categoryId = entry.getKey();
            List<Product> productNames = entry.getValue();
            Category category = categoryRepository.find(categoryId);
            String categoryName = null;
            if(category == null) {
                categoryName = "Unknown";
            }
            else {
                categoryName = category.getName();
            }
            sb.append("Categorie: ").append(categoryName).append("\n");
            for (Product productName : productNames) {
                sb.append(" - ").append(productName.toString()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
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
