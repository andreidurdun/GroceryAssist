package com.grocery.assist.repository;

import java.sql.SQLException;

public interface RepositoryInterface<T> {
    Long save(T ob) throws SQLException;
    T find(Long id) throws SQLException;
    void delete(Long id) throws SQLException;
    void update(Long id) throws SQLException;
}
