package com.grocery.assist.ui;

import com.grocery.assist.model.Ingredient;
import com.grocery.assist.model.Recipe;
import com.grocery.assist.model.ShoppingList;
import com.grocery.assist.repository.RecipeRepository;
import com.grocery.assist.repository.ShoppingListRepository;
import com.grocery.assist.service.IngredientService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecipeUI extends JPanel implements Refreshable {
    private final ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
    private final IngredientService ingredientService = new IngredientService();
    private final RecipeRepository recipeRepository = new RecipeRepository();
    private List<Recipe> recipes;
    private DefaultListModel<String> recipeNameModel = new DefaultListModel<>();
    private JTextArea detailsArea = new JTextArea();

    public RecipeUI(AppUI app) throws SQLException {
        this.recipes = recipeRepository.findAll();

        setLayout(new BorderLayout());

        for (Recipe recipe : recipes) {
            String name = recipe.getName();
            recipeNameModel.addElement(name);
        }

        JList<String> recipeName = new JList<>(recipeNameModel);
        //JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);

        JButton deleteButton = new JButton("Sterge Reteta");
        JButton editButton = new JButton("Editeaza Reteta");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        recipeName.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedRecipe = recipeName.getSelectedValue();
                for (Recipe recipe : recipes) {
                    if (recipe.getName().equals(selectedRecipe)) {
                        detailsArea.setText(recipe.toString2());
                        break;
                    }
                }
            }
        });

        deleteButton.addActionListener(e -> {
            String selectedName = recipeName.getSelectedValue();
            if (selectedName == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Sigur vrei sa stergi reteta " + selectedName + "?", "Confirma stergerea", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Recipe toDelete = recipes.stream()
                            .filter(recipe -> recipe.getName().equals(selectedName))
                            .findFirst()
                            .orElse(null);
                    if (toDelete != null) {
                        recipeRepository.delete(toDelete.getId());
                        recipes.remove(toDelete);
                        recipeNameModel.removeElement(selectedName);
                        detailsArea.setText("");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        editButton.addActionListener(e -> {
            String selectedName = recipeName.getSelectedValue();
            if (selectedName == null) return;
            Recipe selectedRecipe = recipes.stream()
                    .filter(recipe -> recipe.getName().equals(selectedName))
                    .findFirst()
                    .orElse(null);
            if (selectedRecipe == null) return;

            StringBuilder currentContent = new StringBuilder();
            for (Ingredient ingredient : selectedRecipe.getIngredients()) {
                currentContent.append(ingredient.toString()).append("\n");
            }

            JTextArea editArea = new JTextArea(currentContent.toString());
            int result = JOptionPane.showConfirmDialog(this, new JScrollPane(editArea), "Editeaza Reteta", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String[] lines = editArea.getText().split("\n");
                List<Ingredient> newIngredients = new ArrayList<>();
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        try {
                            Ingredient ingredient = ingredientService.ingredientParser(line);
                            newIngredients.add(ingredient);


                        }
                        catch(Exception ex) {
                            JOptionPane.showMessageDialog(this, "Trebuie sa adaugati numele si gramajul: " + line);
                        }

                    }
                }
                selectedRecipe.setIngredients(newIngredients);

                try {
                    recipeRepository.update(selectedRecipe);
                    detailsArea.setText(selectedRecipe.toString2());
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(recipeName),
                new JScrollPane(detailsArea)
        );
        splitPane.setDividerLocation(250);

        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new RecipeUI(new AppUI()).setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void refresh() {
        recipeNameModel.clear();
        try {
            recipes = recipeRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Recipe recipe : recipes) {
            String name = recipe.getName();
            recipeNameModel.addElement(name);
        }

        //detailsArea.setText("");
    }
}
