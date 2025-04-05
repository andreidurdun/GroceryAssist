package com.grocery.assist.ui;

import com.grocery.assist.service.SubcategoryService;

import javax.swing.*;

public class MainWindow extends JFrame {

    private final SubcategoryService service;

    public MainWindow(SubcategoryService service) {
        this.service = service;

        setTitle("Scraper App");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton button = new JButton("Scrape Subcategory");
        button.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter Subcategory ID:");
            Long id = Long.parseLong(input);
            service.scrapeById(id);
        });

        add(button);
    }
}

