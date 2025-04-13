package com.grocery.assist.ui;

import com.grocery.assist.model.Product;
import com.grocery.assist.model.ShoppingList;
import com.grocery.assist.repository.ShoppingListRepository;
import com.grocery.assist.util.DBConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingListUI extends JFrame {
    private JTextField productField;             // Caseta de text unde scrii numele produsului
    private DefaultListModel<String> listModel;  // Modelul listei (structura internă a datelor din JList)
    private JList<String> productList;           // Lista vizuală cu produsele adăugate
    private JButton addButton;                   // Butonul "Adaugă produs"
    private JButton saveButton;                  // Butonul "Salvează lista"
    private List<Product> products = new ArrayList<>();         // Lista de produse efective care vor fi salvate

    public ShoppingListUI()
    {
        //Setari fereastra
        setTitle("Creeaza lista de cumparaturi");   // Titlul ferestrei
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Închide aplicația când închizi fereastra
        setSize(400, 300);                           // Dimensiuni
        setLocationRelativeTo(null);                // Centrează fereastra pe ecran

        // Crearea componentelor
        productField = new JTextField(20);          // TextField cu 20 caractere lungime
        listModel = new DefaultListModel<>();       // Modelul datelor pentru JList
        productList = new JList<>(listModel);       // JList vizual care folosește modelul
        addButton = new JButton("Adaugă produs");   // Buton
        saveButton = new JButton("Salvează lista"); // Buton

        // Adăugarea componentelor în fereastră
        JPanel panel = new JPanel();                // Panou principal (un container)
        panel.setLayout(new BorderLayout());        // Aranjare în stil nord/centru/sud

        JPanel inputPanel = new JPanel();           // Sub-panou pentru input
        inputPanel.add(new JLabel("Produs:"));      // Text „Produs:”
        inputPanel.add(productField);               // Caseta de text
        inputPanel.add(addButton);                  // Buton „Adaugă”

        // Adăugarea sub-panoului de input în panoul principal
        panel.add(inputPanel, BorderLayout.NORTH);        // Sus
        panel.add(new JScrollPane(productList), BorderLayout.CENTER); // Centru (lista)
        panel.add(saveButton, BorderLayout.SOUTH);        // Jos

        add(panel); // Adăugăm totul în fereastra principală
        setVisible(true);        // facem fereastra vizibilă

        // Adăugarea acțiunilor pentru butoane
        addButton.addActionListener(e -> {
            String productName = productField.getText().trim(); // Citim textul introdus
            if (!productName.isEmpty()) {
                listModel.addElement(productName);              // Adăugăm vizual în listă
                products.add(new Product(productName));    // Adăugăm în lista reală de obiecte
                productField.setText("");                       // Golește caseta de text
            }
        });

        saveButton.addActionListener(e -> {
            ShoppingList list = new ShoppingList();             // Creăm o nouă listă
            list.setDate(new Date());                           // Setăm data curentă
            list.setProducts(products);               // Adăugăm produsele

            try {
                ShoppingListRepository repo = new ShoppingListRepository();
                repo.save(list);                                // Salvăm în DB
                JOptionPane.showMessageDialog(this, "Lista a fost salvată cu succes!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la salvare: " + ex.getMessage());
            }
        });


    }


}
