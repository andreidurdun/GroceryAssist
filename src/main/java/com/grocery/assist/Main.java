package com.grocery.assist;

import com.grocery.assist.model.Product;

import java.sql.*;

import com.grocery.assist.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        // Adăugăm un produs în acea categorie
        //Product milk = new Product("Lapte");
        //session.save(milk);

//        tx.commit();
//        session.close();

//        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/GroceryAssistDB", "postgres", "andrei24");
//        Statement stmt = con.createStatement();
//        String sql = "select * from product";
//        ResultSet rs = stmt.executeQuery(sql);



    }
}
