package com.bookstore.store.dao;

import com.bookstore.store.entity.User_24162094;
import com.bookstore.store.utils.JPAUtil_24162094;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UserDAOImpl_24162094 implements IUserDAO_24162094 {
    @Override
    public User_24162094 findByEmail(String email) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            return entityManager.createQuery("select u from User_24162094 u where u.email = :email", User_24162094.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException exception) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User_24162094 save(User_24162094 user) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return user;
        } finally {
            entityManager.close();
        }
    }
}
