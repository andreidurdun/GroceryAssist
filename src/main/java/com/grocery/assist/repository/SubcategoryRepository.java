package com.grocery.assist.repository;

import com.grocery.assist.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SubcategoryRepository {
    private final Session session = HibernateUtil.getSessionFactory().openSession();
    private final Transaction tx = session.beginTransaction();
}
