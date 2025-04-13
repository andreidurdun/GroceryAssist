package com.grocery.assist.repository;

import com.grocery.assist.model.Category;
import com.grocery.assist.model.Store;
import com.grocery.assist.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StoreRepository implements RepositoryInterface<Store> {
    Connection con;

    public StoreRepository() throws SQLException {
        this.con = DBConnectionManager.getConnection();
    }

    @Override
    public Long save(Store ob) throws SQLException {
        String query = "INSERT INTO Store (name) VALUES (?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, ob.getName());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        long id = 0;
        if (rs.next()) {
            id = rs.getLong(1);
        }
        //inseram categoriile si le legam de store
        for(Category category : ob.getCategories()) {
            String query2 = "INSERT INTO category (name) VALUES (?)";
            PreparedStatement ps2 = con.prepareStatement(query2, PreparedStatement.RETURN_GENERATED_KEYS);
            ps2.setString(1, category.getName());
            ps2.executeUpdate();

            ResultSet rs2 = ps2.getGeneratedKeys();
            long categoryId = 0;
            if (rs2.next()) {
                categoryId = rs2.getLong(1);
            }

            String query3 = "INSERT INTO store_category (store_id, category_id, link) VALUES (?, ?, ?)";
            PreparedStatement ps3 = con.prepareStatement(query3);
            ps3.setLong(1, id);
            ps3.setLong(2, categoryId);
            ps3.setString(3, category.getLink());
            ps3.executeUpdate();
        }
        return id;
    }



    @Override
    public Store find(Long id) throws SQLException {
        // Implementation for finding a Store object by its ID
        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {
        // Implementation for deleting a Store object by its ID
    }

    @Override
    public void update(Long id) throws SQLException {
        // Implementation for updating a Store object by its ID
    }

    @Override
    public List<Store> findAll() throws SQLException {
        return List.of();
    }
}
