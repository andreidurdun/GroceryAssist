package com.grocery.assist.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AppUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public AppUI() throws SQLException {
        setTitle("Grocery Assist");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // adauga ecranele în layout

        RecipeUI recipeUI = new RecipeUI(this);
        ShoppingListHistoryUI historyUI = new ShoppingListHistoryUI(this);
        ShoppingListUI2 shoppingListUI = new ShoppingListUI2(this);

        mainPanel.add(recipeUI, "RecipeUI");
        mainPanel.add(historyUI, "HistoryUI");
        mainPanel.add(shoppingListUI, "ListUI");

        // Bara de navigare
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton toHistory = new JButton("Istoric Liste de Cumparaturi");
        toHistory.addActionListener(e -> {
            historyUI.refresh();
            cardLayout.show(mainPanel, "HistoryUI");
        });

        JButton toRecipe = new JButton("Rețete Salvate");
        toRecipe.addActionListener(e -> {
            recipeUI.refresh();
            cardLayout.show(mainPanel, "RecipeUI");
        });

        JButton toShoppingList = new JButton("Adauga Lista de Cumparaturi");
        toShoppingList.addActionListener(e -> {
            shoppingListUI.refresh();
            cardLayout.show(mainPanel, "ListUI");
        });

        navBar.add(toShoppingList);
        navBar.add(toHistory);
        navBar.add(toRecipe);
        add(navBar, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "HistoryUI"); // Ecranul de start
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new AppUI().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}

