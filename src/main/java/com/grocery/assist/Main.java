package com.grocery.assist;

import java.sql.*;

import com.grocery.assist.repository.SubcategoryRepository;
import com.grocery.assist.service.Scraper;
import com.grocery.assist.service.SubcategoryService;
import com.grocery.assist.ui.MainWindow;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        // Set up EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        // Creează obiectele
        SubcategoryRepository repository = new SubcategoryRepository(em);
        Scraper scraper = new Scraper();
        SubcategoryService service = new SubcategoryService(repository, scraper);

        // Pornește UI-ul
        SwingUtilities.invokeLater(() -> {
            new MainWindow(service).setVisible(true);
        });


    }
}
