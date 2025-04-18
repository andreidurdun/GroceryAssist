package com.grocery.assist.repository;

import com.grocery.assist.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryRepository implements  RepositoryInterface<Category> {
    private final Connection con;
    public CategoryRepository(Connection con) throws SQLException {
        this.con = con;
    }

    @Override
    public Long save(Category ob) throws SQLException {
        String query = "INSERT INTO Category (name) VALUES (?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, ob.getName());
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
        // Implementation for finding a Category object by its ID
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
