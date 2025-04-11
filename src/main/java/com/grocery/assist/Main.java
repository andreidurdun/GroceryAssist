package com.grocery.assist;

import java.sql.*;

import com.grocery.assist.ui.ShoppingListUI;
import com.grocery.assist.ui.StoreUI;


import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        // Set up EntityManager
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
//        EntityManager em = emf.createEntityManager();

        // Creează obiectele
//        SubcategoryRepository repository = new SubcategoryRepository(em);
//        Scraper scraper = new Scraper();
//        SubcategoryService service = new SubcategoryService(repository, scraper);

//        // Pornește UI-ul
//        SwingUtilities.invokeLater(() -> {
//            new MainWindow(service).setVisible(true);
//        });

        //Setare magazin
//        List<Category> categories = new ArrayList<Category>();
//        categories.add(new Subcategory("milk", "www.abcd.com"));
//        categories.add(new Subcategory("meat", "www.abcd.com"));
//        System.out.println(categories.toString());

//        Connection con = DBConnectionManager.getConnection();
//        ProductRepository pr = new ProductRepository(con);
//        //System.out.println(pr.find(1L).getProductName());
//
//        ShoppingListRepository sl = new ShoppingListRepository(con);
//        ShoppingList shoppingList = new ShoppingList();
//        sl.save(shoppingList);

        //SwingUtilities.invokeLater(ShoppingListUI::new);
        SwingUtilities.invokeLater(StoreUI::new);
    }
}
