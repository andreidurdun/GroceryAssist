package com.grocery.assist.ui;

import com.grocery.assist.model.Category;
import com.grocery.assist.model.Store;
import com.grocery.assist.repository.StoreRepository;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

public class StoreUI extends JFrame {
    private JTextField storeNameField;
    private DefaultListModel<Category> categoryListModel;
    private JList<Category> categoryJList;

    public StoreUI() {
        setTitle("Adaugă Magazin");
        setSize(500, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        storeNameField = new JTextField(20);
        categoryListModel = new DefaultListModel<>();
        categoryJList = new JList<>(categoryListModel);

        JButton addCategoryBtn = new JButton("Adaugă Categorie");
        JButton saveBtn = new JButton("Salvează Magazin");

        addCategoryBtn.addActionListener(e -> addCategory());
        saveBtn.addActionListener(e -> saveStore());
        categoryJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = categoryJList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        editCategory(index);
                    }
                }
            }
        });
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Nume magazin:"));
        topPanel.add(storeNameField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addCategoryBtn);
        buttonPanel.add(saveBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(categoryJList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addCategory() {
        JTextField nameField = new JTextField(20);
        JTextField linkField = new JTextField();

        Object[] fields = {
                "Nume categorie:", nameField,
                "Link:", linkField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Categorie nouă", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Category c = new Category();
            c.setName(nameField.getText());
            categoryListModel.addElement(c);
        }
    }

    private void saveStore() {
        Store store = new Store();
        store.setName(storeNameField.getText());
        store.setCategories(Collections.list(categoryListModel.elements()));

        try{
            StoreRepository repo = new StoreRepository();
            repo.save(store);
            JOptionPane.showMessageDialog(this, "Magazin salvat!");
            storeNameField.setText("");
            categoryListModel.clear();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la salvare: " + e.getMessage());
        }

    }

    private void editCategory(int index) {
        Category category = categoryListModel.get(index);

        JTextField nameField = new JTextField(category.getName(), 20);
        JTextField linkField = new JTextField(category.getLink());

        Object[] fields = {
                "Nume categorie:", nameField,
                "Link:", linkField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Editează Categorie", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            category.setName(nameField.getText());
            category.setLink(linkField.getText());
            categoryListModel.set(index, category); // actualizează elementul în listă
        }
    }
}
