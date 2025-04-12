package com.grocery.assist.repository;

import com.grocery.assist.model.Ingredient;
import com.grocery.assist.model.Recepie;
import com.grocery.assist.util.DBConnectionManager;
import com.grocery.assist.model.Ingredient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeRepository implements RepositoryInterface<Recepie> {
    private final Connection con;
    private final IngredientRepository ingredientRepository;

    public RecipeRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
        this.ingredientRepository = new IngredientRepository();
    }

    @Override
    public Long save(Recepie ob) throws SQLException {
        String query = "INSERT INTO recipe (name) VALUES (?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, ob.getName());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        long id = 0;
        if (rs.next()) {
            id = rs.getLong(1);
        }

        for(Ingredient ingredient : ob.getIngredients()) {
            ingredient.setRecipeId(id);
            ingredientRepository.save(ingredient);
        }

        return id;
    }

    @Override
    public Recepie find(Long id) throws SQLException {
        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {

    }

    @Override
    public void update(Long id) throws SQLException {

    }
}
