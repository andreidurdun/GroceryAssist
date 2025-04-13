package com.grocery.assist.repository;

import com.grocery.assist.model.Ingredient;
import com.grocery.assist.model.Recipe;
import com.grocery.assist.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeRepository implements RepositoryInterface<Recipe> {
    private final Connection con;
    private final IngredientRepository ingredientRepository;

    public RecipeRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
        this.ingredientRepository = new IngredientRepository();
    }

    @Override
    public Long save(Recipe ob) throws SQLException {
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
            ingredient.setRecepieId(id);
            ingredientRepository.save(ingredient);
        }

        return id;
    }

    @Override
    public Recipe find(Long id) throws SQLException {
        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {

    }

    @Override
    public void update(Long id) throws SQLException {

    }

    @Override
    public List<Recipe> findAll() throws SQLException {
        List<Recipe> recepies = new ArrayList<>();
        String query = "SELECT * FROM recipe";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            long id = rs.getLong(1);
            String name = rs.getString(2);
            Recipe recipe = new Recipe();
            recipe.setId(id);
            recipe.setName(name);
            List<Ingredient> ingredients = ingredientRepository.findByRecipeId(id);
            recipe.setIngredients(ingredients);
            recepies.add(recipe);
        }
        return recepies;
    }
}
