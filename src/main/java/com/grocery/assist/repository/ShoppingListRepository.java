package com.grocery.assist.repository;

import com.grocery.assist.model.ShoppingList;
import com.grocery.assist.util.DBConnectionManager;
import com.grocery.assist.model.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingListRepository implements  RepositoryInterface<ShoppingList> {


    private final Connection con;
    private final ProductRepository productRepository;

    public ShoppingListRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
        this.productRepository = new ProductRepository(con);
    }

    public List<ShoppingList> findByDate(Date fromDate, Date toDate) throws SQLException {

        String query = "SELECT id, date FROM ShoppingList WHERE date >= ? AND date <= ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setDate(1, (java.sql.Date) fromDate);
        stmt.setDate(2, (java.sql.Date) toDate);
        ResultSet rs = stmt.executeQuery(query);

        List<ShoppingList> shLists = new ArrayList<>();
        while(rs.next()){
            ShoppingList shoppingList = new ShoppingList();
            Long id = rs.getLong("id");
            Date date = rs.getDate("date");
            shoppingList.setDate(date);
            shoppingList.setId(id);

            String query2 = "SELECT * FROM Product WHERE shoppinglist_id = id";
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);

            List<Product> products = new ArrayList<>();
            while(rs2.next()){
                Product product = new Product();
                product.setId(rs2.getLong("id"));
                product.setProductName(rs2.getString("name"));
                product.setPrice(rs2.getDouble("price"));
                product.setCategory(rs2.getLong("category_id"));
                products.add(product);
            }

            shoppingList.setProducts(products);
            shLists.add(shoppingList);
        }

        return shLists;
    }

    public void save(ShoppingList shoppingList) throws SQLException {
        String query = "INSERT INTO shoppinglist (date) VALUES (?)";
        PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys(); //obtinem cheia generata
        long shoppingListId = 0;
        if (rs.next()) {
            shoppingListId = rs.getLong(1);
        }
        stmt.close();

        //inseram produsele daca nu exista deja, daca exista inseram in tabela de legatura
        String insertProductQuery = "INSERT INTO product (price, category_id, productname) VALUES (?, ?, ?)";
        if(shoppingList.getProducts() == null){ //daca nu exista produse in shoppingList ne oprim
            return;
        }
        for(Product product: shoppingList.getProducts()){
            if(productRepository.find(product.getId()) == null) {
                PreparedStatement prstmt = con.prepareStatement(insertProductQuery, Statement.RETURN_GENERATED_KEYS);
                prstmt.setDouble(1, product.getPrice());
                if (product.getCategoryId() != null) {
                    prstmt.setLong(2, product.getCategoryId());
                } else {
                    prstmt.setNull(2, Types.BIGINT); // setăm categoryId ca NULL în baza de date
                }
                prstmt.setString(3, product.getProductName());
                prstmt.executeUpdate();

                ResultSet rs2 = prstmt.getGeneratedKeys();
                long productId = 0;
                if (rs2.next()) {
                    productId = rs2.getLong(1);
                }

                String ShListProductQuery = "INSERT INTO shoppinglist_product (shoppinglist_id, product_id) VALUES (?, ?)";
                PreparedStatement prstmt3 = con.prepareStatement(ShListProductQuery);
                prstmt3.setLong(1, shoppingListId);
                prstmt3.setLong(2, productId);
                prstmt3.executeUpdate();
                prstmt3.close();
            }
            else{
                String ShListProductQuery = "INSERT INTO shoppinglist_product (shoppinglist_id, product_id) VALUES (?, ?)";
                PreparedStatement prstmt3 = con.prepareStatement(ShListProductQuery);
                prstmt3.setLong(1, shoppingListId);
                prstmt3.setLong(2, product.getId());
                prstmt3.executeUpdate();
                prstmt3.close();
            }

        }

        }

        @Override
        public ShoppingList find(Long id) throws SQLException {
            return null;
        }

        @Override
        public void delete(Long id) throws SQLException {

        }

        @Override
        public void update(Long id) throws SQLException {

        }
}
