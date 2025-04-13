package com.grocery.assist.repository;

import com.grocery.assist.model.Product;
import com.grocery.assist.util.DBConnectionManager;
import jakarta.persistence.EntityManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductRepository implements RepositoryInterface<Product> {
    private final Connection con;


    public ProductRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
    }

    @Override
    public Product find(Long id) throws SQLException {
        if (id == null) {
            return null;
        }
        String query = "SELECT * FROM Product WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        product.setPrice(rs.getDouble("price"));
        product.setProductName(rs.getString("productname"));
        product.setCategory(rs.getLong("category_id"));
        return product;
    }

    @Override
    public Long save(Product ob) throws SQLException {
            return null;
    }

    @Override
    public void delete(Long id) throws SQLException {

    }

    @Override
    public void update(Long id) throws SQLException {

    }

    @Override
    public List<Product> findAll() throws SQLException {
        return List.of();
    }
}
