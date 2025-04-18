package com.grocery.assist.repository;

import java.sql.SQLException;
import java.util.List;

public interface RepositoryInterface<T> {
    Long save(T ob) throws SQLException;
    T find(Long id) throws SQLException;
    void delete(Long id) throws SQLException;
    void update(T id) throws SQLException;
    List<T> findAll() throws SQLException;
}
