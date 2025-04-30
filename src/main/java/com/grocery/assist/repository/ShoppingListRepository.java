package com.grocery.assist.repository;

import com.grocery.assist.model.Ingredient;
import com.grocery.assist.model.ShoppingList;
import com.grocery.assist.util.DBConnectionManager;
import com.grocery.assist.model.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListRepository implements  RepositoryInterface<ShoppingList> {
    private static ShoppingListRepository instance;
    private final Connection con;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository = IngredientRepository.getInstance();

    private ShoppingListRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
        this.productRepository = ProductRepository.getInstance();
    }

    public static synchronized ShoppingListRepository getInstance() throws SQLException {
        if (instance == null) {
            instance = new ShoppingListRepository();
        }
        return instance;
    }


    public Long save(ShoppingList shoppingList) throws SQLException {
        String query = "INSERT INTO shoppinglist (date) VALUES (?)";
        PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys(); //obtinem cheia generata
        long shoppingListId = 0;
        if (rs.next()) {
            shoppingListId = rs.getLong(1);
        }


        //inseram produsele daca nu exista deja, daca exista inseram in tabela de legatura
        String insertProductQuery = "INSERT INTO product (price, category_id, productname, quantity, unit, recipe_id) VALUES (?, ?, ?, ?, ?, ?)";
        if(shoppingList.getProducts() == null){ //daca nu exista produse in shoppingList ne oprim
            return shoppingListId;
        }
        for(Product product: shoppingList.getProducts()){
            if(productRepository.find(product.getId()) == null) {
                PreparedStatement prstmt = con.prepareStatement(insertProductQuery, Statement.RETURN_GENERATED_KEYS);
                prstmt.setDouble(1, product.getPrice());
                if (product.getCategoryId() != null) {
                    prstmt.setLong(2, product.getCategoryId());
                } else {
                    System.out.println("Category id is null");
                    prstmt.setNull(2, Types.BIGINT); // setăm categoryId ca NULL în baza de date
                }
                prstmt.setString(3, product.getProductName());
                if (product instanceof Ingredient) {
                    prstmt.setFloat(4, ((Ingredient) product).getQuantity());
                    prstmt.setString(5, ((Ingredient) product).getUnit());
                    if (((Ingredient) product).getRecipeId() != null) {
                        prstmt.setLong(6, ((Ingredient) product).getRecipeId());
                    } else {
                        prstmt.setNull(6, Types.BIGINT);
                    }

                }
                else{
                    prstmt.setNull(4, Types.FLOAT);
                    prstmt.setNull(5, Types.VARCHAR);
                    prstmt.setNull(6, Types.BIGINT);
                }
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
            }

        }
        return shoppingListId;
    }

        @Override
        public ShoppingList find(Long id) throws SQLException {
            return null;
        }

        @Override
        public void delete(Long id) throws SQLException {
            String query3 = "SELECT product_id FROM shoppinglist_product WHERE shoppinglist_id = ?";
            PreparedStatement ps3 = con.prepareStatement(query3);
            ps3.setLong(1, id);
            ResultSet rs3 = ps3.executeQuery();
            long productId = 0;
            if(rs3.next()) {
                productId = rs3.getLong("product_id");
            }

            String query1 = "DELETE FROM shoppinglist_product WHERE shoppinglist_id = ?";
            PreparedStatement ps1 = con.prepareStatement(query1);
            ps1.setLong(1, id);
            ps1.executeUpdate();

            String query2 = "DELETE FROM product WHERE recipe_id is null and id = ?";
            PreparedStatement ps2 = con.prepareStatement(query2);
            ps2.setLong(1, productId);
            ps2.executeUpdate();

            String query = "DELETE FROM shoppinglist WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, id);
            ps.executeUpdate();

        }

        @Override
        public void update(ShoppingList shoppingList) throws SQLException {
            String query = "UPDATE shoppinglist SET date = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ps.setLong(2, shoppingList.getId());
            ps.executeUpdate();
            //stergem produsele din tabela de legatura si le inseram pe cele editate
            String query2 = "DELETE FROM shoppinglist_product WHERE shoppinglist_id = ?";
            PreparedStatement ps2 = con.prepareStatement(query2);
            ps2.setLong(1, shoppingList.getId());
            ps2.executeUpdate();

            for(Product product: shoppingList.getProducts()){
                long id_produs = 0;
                if(product instanceof Ingredient){
                    System.out.println("Ingredient: " + product.getProductName());
                    id_produs = ingredientRepository.save((Ingredient) product);
                }
                else{
                    System.out.println("Product: " + product.getProductName());
                    id_produs = productRepository.save(product);
                }

                String query3 = "INSERT INTO shoppinglist_product (shoppinglist_id, product_id) VALUES (?, ?)";
                PreparedStatement ps3 = con.prepareStatement(query3);
                ps3.setLong(1, shoppingList.getId());
                ps3.setLong(2, id_produs);
                ps3.executeUpdate();
            }

        }

    @Override
    public List<ShoppingList> findAll() throws SQLException {
        List<ShoppingList> shoppingLists = new ArrayList<>();
        String query = "SELECT * FROM shoppinglist";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setId(rs.getLong("id"));
            shoppingList.setDate(rs.getDate("date"));

            String query2 = "SELECT * FROM shoppinglist_product WHERE shoppinglist_id = ?";
            PreparedStatement stmt2 = con.prepareStatement(query2);
            stmt2.setLong(1, shoppingList.getId());
            ResultSet rs2 = stmt2.executeQuery();

            List<Product> products = new ArrayList<>();
            while (rs2.next()) {
                if(ingredientRepository.find(rs2.getLong("product_id")) != null) {
                    Ingredient ingredient = ingredientRepository.find(rs2.getLong("product_id"));
                    products.add(ingredient);
                }
                else{
                    Product product = productRepository.find(rs2.getLong("product_id"));
                    products.add(product);
                }
            }

            shoppingList.setProducts(products);
            shoppingLists.add(shoppingList);
        }

        return shoppingLists;
    }
}
