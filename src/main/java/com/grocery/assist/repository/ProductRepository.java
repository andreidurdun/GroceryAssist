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
    private static ProductRepository instance;
    private final Connection con;

    private ProductRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
    }

    public static synchronized ProductRepository getInstance() throws SQLException {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
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
        String query = "INSERT INTO product (price, category_id, productname) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setDouble(1, ob.getPrice());
        if (ob.getCategoryId() != null) {
            ps.setLong(2, ob.getCategoryId());
        } else {
            ps.setNull(2, java.sql.Types.BIGINT);
        }
        ps.setString(3, ob.getProductName());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        long id = 0;
        if (rs.next()) {
            id = rs.getLong(1);
        }
        return id;
    }

    @Override
    public void delete(Long id) throws SQLException {

    }

    @Override
    public void update(Product id) throws SQLException {

    }

    @Override
    public List<Product> findAll() throws SQLException {
        return List.of();
    }

    public void updateCategory(Long id, Long categoryId) throws SQLException {
        String query = "UPDATE product SET category_id = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setLong(1, categoryId);
        ps.setLong(2, id);
        ps.executeUpdate();
    }
}
