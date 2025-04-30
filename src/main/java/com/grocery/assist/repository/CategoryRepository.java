package com.grocery.assist.repository;

import com.grocery.assist.model.Category;
import com.grocery.assist.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryRepository implements  RepositoryInterface<Category> {
    private static CategoryRepository instance;
    private final Connection con;

    private CategoryRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
    }

    public static synchronized CategoryRepository getInstance() throws SQLException {
        if (instance == null) {
            instance = new CategoryRepository();
        }
        return instance;
    }

    @Override
    public Long save(Category ob) throws SQLException {
        String query = "INSERT INTO Category (name) VALUES (?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, ob.getName().toLowerCase());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        long id = 0;
        if (rs.next()) {
            id = rs.getLong(1);
        }
        return id;
    }

    @Override
    public Category find(Long id) throws SQLException {
        if (id == null) {
            return null;
        }
        String query = "SELECT * FROM Category WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Category category = new Category();
            category.setId(id);
            category.setName(rs.getString("name"));
            return category;
        }
        return null;
    }

    public Long findByName(String name) throws SQLException {
        String query = "SELECT id FROM Category WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name.toLowerCase());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getLong("id");
        }
        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {
        // Implementation for deleting a Category object by its ID
    }

    @Override
    public void update(Category id) throws SQLException {
        // Implementation for updating a Category object by its ID
    }

    @Override
    public List<Category> findAll() throws SQLException {
        return List.of();
    }
}
