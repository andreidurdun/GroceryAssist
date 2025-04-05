package com.grocery.assist.repository;

import com.grocery.assist.model.Subcategory;
import jakarta.persistence.EntityManager;


public class SubcategoryRepository {
    private final EntityManager entityManager;

    public SubcategoryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Subcategory findById(Long id) {
        return entityManager.find(Subcategory.class, id);
    }
}
