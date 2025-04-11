package com.grocery.assist.repository;

import com.grocery.assist.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryRepository implements  RepositoryInterface<Category> {
    private final Connection con;
    public CategoryRepository(Connection con) throws SQLException {
        this.con = con;
    }

    @Override
    public void save(Category ob) throws SQLException {
        String query = "INSERT INTO Category (name) VALUES (?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
    }

    @Override
    public Category find(Long id) throws SQLException {
        // Implementation for finding a Category object by its ID
        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {
        // Implementation for deleting a Category object by its ID
    }

    @Override
    public void update(Long id) throws SQLException {
        // Implementation for updating a Category object by its ID
    }
}
