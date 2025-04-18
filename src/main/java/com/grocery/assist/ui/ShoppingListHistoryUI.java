package com.grocery.assist.ui;

import com.grocery.assist.model.Product;
import com.grocery.assist.model.Recipe;
import com.grocery.assist.model.ShoppingList;
import com.grocery.assist.repository.ShoppingListRepository;
import com.grocery.assist.service.IngredientService;
import com.grocery.assist.service.ShoppingListService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class ShoppingListHistoryUI extends JPanel implements Refreshable{
    private final ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
    private final IngredientService ingredientService = new IngredientService();
    private final ShoppingListService shoppingListService = new ShoppingListService();

    private List<ShoppingList> shoppingLists;
    private DefaultListModel<ShoppingList> dateListModel = new DefaultListModel<>();
    private JTextArea detailsArea = new JTextArea();

    public ShoppingListHistoryUI(AppUI app) throws SQLException {
        this.shoppingLists = shoppingListRepository.findAll();
        shoppingLists.sort(Comparator.comparing(ShoppingList::getDate).reversed());
        setLayout(new BorderLayout());

        for (ShoppingList shoppingList : shoppingLists) {
            dateListModel.addElement(shoppingList);
        }

        JList<ShoppingList> dateList = new JList<>(dateListModel);

        // Render doar cu data
        dateList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ShoppingList) {
                    LocalDate date = ((java.sql.Date) ((ShoppingList) value).getDate()).toLocalDate();
                    setText(date.toString());
                }
                return this;
            }
        });

        detailsArea.setEditable(false);

        JButton deleteButton = new JButton("Sterge Lista");
        JButton editButton = new JButton("Editeaza Lista");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        deleteButton.addActionListener(e -> {
            ShoppingList selectedList = dateList.getSelectedValue();
            if (selectedList == null) return;

            LocalDate selectedDate = ((java.sql.Date) selectedList.getDate()).toLocalDate();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Sigur vrei sa stergi lista din " + selectedDate + "?",
                    "Confirmare stergere", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    shoppingListRepository.delete(selectedList.getId());
                    shoppingLists.remove(selectedList);
                    dateListModel.removeElement(selectedList);
                    detailsArea.setText("");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Eroare la ștergere: " + ex.getMessage());
                }
            }
        });

        editButton.addActionListener(e -> {
            ShoppingList selectedList = dateList.getSelectedValue();
            if (selectedList == null) return;

            StringBuilder currentContent = new StringBuilder();
            for (Product product : selectedList.getProducts()) {
                currentContent.append(product.toString()).append("\n");
            }

            JTextArea editorArea = new JTextArea(currentContent.toString());
            int result = JOptionPane.showConfirmDialog(this, new JScrollPane(editorArea),
                    "Editează produsele", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String[] newLines = editorArea.getText().split("\\n");
                List<Product> newProducts = new ArrayList<>();
                for (String line : newLines) {
                    if (!line.trim().isEmpty()) {
                        try {
                            String[] pr = line.trim().split(" ");
                            String lastWord = pr[pr.length - 1];

                            if (lastWord.equals("g") || lastWord.equals("buc")) {
                                newProducts.add(ingredientService.ingredientParser(line));
                            } else {
                                newProducts.add(new Product(line.trim()));
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this,
                                    "Eroare la parsarea liniei: " + line + "\n" + ex.getMessage());
                        }
                    }
                }

                selectedList.setProducts(newProducts);

                try {
                    shoppingListRepository.update(selectedList);
                    detailsArea.setText(selectedList.toString());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Eroare la actualizare: " + ex.getMessage());
                }
            }
        });

        dateList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ShoppingList selectedList = dateList.getSelectedValue();
                if (selectedList != null) {
                    detailsArea.setText(selectedList.toString());
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(dateList),
                new JScrollPane(detailsArea)
        );
        splitPane.setDividerLocation(250);

        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ShoppingListHistoryUI(new AppUI()).setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void refresh() {
        dateListModel.clear();
        try {
            shoppingLists = shoppingListRepository.findAll();
            shoppingLists.sort(Comparator.comparing(ShoppingList::getDate).reversed());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (ShoppingList shoppingList : shoppingLists) {
            dateListModel.addElement(shoppingList);
        }

        detailsArea.setText("");
    }
}
