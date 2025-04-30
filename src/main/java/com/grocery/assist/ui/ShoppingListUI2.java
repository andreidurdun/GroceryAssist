package com.grocery.assist.ui;

import com.grocery.assist.model.Ingredient;
import com.grocery.assist.model.Product;
import com.grocery.assist.model.Recipe;
import com.grocery.assist.model.ShoppingList;
import com.grocery.assist.repository.RecipeRepository;
import com.grocery.assist.service.IngredientService;
import com.grocery.assist.service.RecipeService;
import com.grocery.assist.service.ShoppingListService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


public class ShoppingListUI2 extends JPanel implements Refreshable{
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService = new RecipeService();
    private final IngredientService ingredientService = new IngredientService();
    private final ShoppingListService shoppingListService = new ShoppingListService();

    private DefaultListModel<Product> listModel;
    private JList<Product> shoppingList;

    private JTextField productField;
    private JComboBox<Recipe> recipeComboBox;
    private JTextField newRecipeNameField;
    private JTextArea newRecipeIngredientsArea;

    private List<Recipe> recipes = new ArrayList<>();

    public ShoppingListUI2(AppUI app) throws SQLException {
        setLayout(new BorderLayout());

        try {
            recipeRepository = RecipeRepository.getInstance();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la inițializarea rețetelor: " + e.getMessage());
        }


        // Inițializare rețete
        recipes = recipeRepository.findAll();

        // Panel pentru produse și rețete existente
        JPanel topPanel = new JPanel(new GridLayout(6, 1));

        productField = new JTextField();
        JButton addProductButton = new JButton("Adauga Produs");


        recipeComboBox = new JComboBox<Recipe>(new Vector<>(recipes));
        JButton addRecipeButton = new JButton("Adauga Reteta in lista");

        JButton addShoppingListButton = new JButton("Salveaza lista de cumparaturi");

        listModel = new DefaultListModel<>();
        shoppingList = new JList<>(listModel);

        addProductButton.addActionListener(e -> {
            String product = productField.getText().trim();
            if (!product.isEmpty()) {
                Ingredient ingredient = ingredientService.ingredientParser(product);
                listModel.addElement(ingredient);
                productField.setText("");
            }
        });

        addRecipeButton.addActionListener(e -> {
            Recipe selectedRecipe = (Recipe) recipeComboBox.getSelectedItem();
            if (selectedRecipe != null) {
                List<Ingredient> ingredients = selectedRecipe.getIngredients();
                for (Ingredient ingredient : ingredients) {
                    listModel.addElement(ingredient);
                }
            }
        });

        topPanel.add(new JLabel("Adauga produs individual:"));
        topPanel.add(productField);
        topPanel.add(addProductButton);
        topPanel.add(new JLabel("Selecteaza reteta existenta:"));
        topPanel.add(recipeComboBox);
        topPanel.add(addRecipeButton);


        // Panel pentru adaugare rețeta noua
        JPanel newRecipePanel = new JPanel();
        newRecipePanel.setLayout(new BorderLayout());
        newRecipePanel.setBorder(BorderFactory.createTitledBorder("Adauga reteta noua"));

        JPanel nameAndButtonPanel = new JPanel(new BorderLayout());
        newRecipeNameField = new JTextField();
        JButton createRecipeButton = new JButton("Adauga Reteta");

        nameAndButtonPanel.add(new JLabel("Nume reteta:"), BorderLayout.NORTH);
        nameAndButtonPanel.add(newRecipeNameField, BorderLayout.CENTER);
        nameAndButtonPanel.add(createRecipeButton, BorderLayout.EAST);

        newRecipeIngredientsArea = new JTextArea(5, 20);
        newRecipeIngredientsArea.setLineWrap(true);
        newRecipeIngredientsArea.setBorder(BorderFactory.createTitledBorder("Ingrediente (unul pe linie)"));

        newRecipePanel.add(nameAndButtonPanel, BorderLayout.NORTH);
        newRecipePanel.add(new JScrollPane(newRecipeIngredientsArea), BorderLayout.CENTER);

        createRecipeButton.addActionListener(e -> {
            String name = newRecipeNameField.getText().trim();
            String[] ingredients = newRecipeIngredientsArea.getText().split("\\n");

            if (!name.isEmpty() && ingredients.length > 0) {
                List<Ingredient> ingredientList = new ArrayList<>();
                for (String ing : ingredients) {
                    if (!ing.trim().isEmpty()) {

                        //Parsam lista de ingrediente din retea
                        Ingredient ingredient = ingredientService.ingredientParser(ing);

                        ingredientList.add(ingredient);
                    }
                }
                try {
                    recipeRepository.save(new Recipe(name, ingredientList));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                recipeComboBox.addItem(new Recipe(name, ingredientList));
                JOptionPane.showMessageDialog(this, "Reteta adaugata cu succes!");
                newRecipeNameField.setText("");
                newRecipeIngredientsArea.setText("");
            }
        });

        addShoppingListButton.addActionListener(e -> {
            List<Product> ingredients = Collections.list(listModel.elements());
            ShoppingList shoppingList = null;
            try {
                shoppingList = shoppingListService.createShoppingListFromProducts(ingredients);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(shoppingList.toString());
            try {
                JOptionPane.showMessageDialog(this, shoppingListService.printShoppingList(shoppingList), "Lista de cumparaturi", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(shoppingList), BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(newRecipePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(addShoppingListButton);

        southPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingListUI2 ui = null;
            try {
                ui = new ShoppingListUI2(new AppUI());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ui.setVisible(true);
        });
    }


    @Override
    public void refresh() {
        listModel.clear();
        try {
            recipes = recipeRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        recipeComboBox.removeAllItems();

        for (Recipe recipe : recipes) {
            recipeComboBox.addItem(recipe);
        }

    }
}




