package com.grocery.assist.repository;


import com.grocery.assist.model.Ingredient;
import com.grocery.assist.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository implements RepositoryInterface<Ingredient> {
    private static IngredientRepository instance;
    private final Connection con;

    private IngredientRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
    }

    public static synchronized IngredientRepository getInstance() throws SQLException {
        if (instance == null) {
            instance = new IngredientRepository();
        }
        return instance;
    }

    @Override
    public Long save(Ingredient ob) throws SQLException {
        String query = "INSERT INTO product (productname, quantity, unit, recipe_id) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, ob.getProductName());
        ps.setFloat(2, ob.getQuantity());
        ps.setString(3, ob.getUnit());
        if (ob.getRecipeId() != null) {
            ps.setLong(4, ob.getRecipeId());
        } else {
            ps.setNull(4, java.sql.Types.BIGINT);
        }
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        long id = 0;
        if (rs.next()) {
            id = rs.getLong(1);
        }

        return id;
    }

    @Override
    public Ingredient find(Long id) throws SQLException {
        String query = "SELECT * FROM product WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getLong("id"));
            ingredient.setProductName(rs.getString("productname"));
            ingredient.setQuantity(rs.getFloat("quantity"));
            ingredient.setCategory(rs.getLong("category_id"));
            if(rs.wasNull()) { //dc quantity e null inseamna ca e produs
                return null;
            }
            ingredient.setUnit(rs.getString("unit"));
            ingredient.setRecipeId(rs.getLong("recipe_id"));
            return ingredient;
        }
        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {

    }

    @Override
    public void update(Ingredient id) throws SQLException {

    }

    @Override
    public List<Ingredient> findAll() throws SQLException {
        return List.of();
    }

    public List<Ingredient> findByRecipeId(Long id) throws SQLException {
        String query = "SELECT * FROM product WHERE recipe_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        List<Ingredient> ingredients = new ArrayList<>();
        while (rs.next()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getLong("id"));
            ingredient.setProductName(rs.getString("productname"));
            ingredient.setQuantity(rs.getFloat("quantity"));
            ingredient.setUnit(rs.getString("unit"));
            ingredient.setRecipeId(id);
            ingredients.add(ingredient);
        }
        return ingredients;
    }
}
