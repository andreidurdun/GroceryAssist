package com.grocery.assist.ui;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JPanel {
    public MainUI(AppUI app) {
        setLayout(new BorderLayout());

        JButton toHistory = new JButton("Istoric Liste de Cumparaturi");
        toHistory.addActionListener(e -> app.showScreen("HistoryUI"));

        JButton toRecipe = new JButton("Retete Salvate");
        toRecipe.addActionListener(e -> app.showScreen("RecipeUI"));

        JButton toShoppingList = new JButton("Adauga Lista de Cumparaturi");
        toShoppingList.addActionListener(e -> app.showScreen("ListUI"));

        JPanel panel = new JPanel();
        panel.add(toHistory);
        panel.add(toRecipe);
        panel.add(toShoppingList);

        add(panel, BorderLayout.CENTER);
    }
}
