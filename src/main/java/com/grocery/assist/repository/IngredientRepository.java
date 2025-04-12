package com.grocery.assist.repository;


import com.grocery.assist.model.Ingredient;
import com.grocery.assist.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientRepository implements RepositoryInterface<Ingredient> {
    private final Connection con;

    public IngredientRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
    }
    @Override
    public Long save(Ingredient ob) throws SQLException {
        String query = "INSERT INTO ingredients (name, quantity, unit, recipe_id) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, ob.getName());
        ps.setFloat(2, ob.getQuantity());
        ps.setString(3, ob.getUnit());
        ps.setLong(4, ob.getRecipeId());
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
        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {

    }

    @Override
    public void update(Long id) throws SQLException {

    }
}
