package com.bookstore.store.dao;

import com.bookstore.store.entity.Rating_24162094;
import com.bookstore.store.entity.User_24162094;
import com.bookstore.store.entity.Book_24162094;
import com.bookstore.store.utils.JPAUtil_24162094;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RatingDAOImpl_24162094 implements IRatingDAO_24162094 {
    @Override
    public Rating_24162094 save(Rating_24162094 rating) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(rating);
            entityManager.getTransaction().commit();
            return rating;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Rating_24162094 saveRating(Rating_24162094 rating) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Rating_24162094 existing = entityManager.find(Rating_24162094.class, rating.getId());
            if (existing == null) {
                existing = rating;
                existing.setUser(entityManager.getReference(User_24162094.class, rating.getId().getUserId()));
                existing.setBook(entityManager.getReference(Book_24162094.class, rating.getId().getBookId()));
                entityManager.persist(existing);
            } else {
                existing.setRating(rating.getRating());
                existing.setReviewText(rating.getReviewText());
            }
            entityManager.getTransaction().commit();
            return existing;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Rating_24162094> getRatingsByBookId(Integer bookId) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            return entityManager.createQuery(
                            "select r from Rating_24162094 r join fetch r.user "
                                    + "where r.book.bookId = :bookId order by r.id.bookId",
                            Rating_24162094.class)
                    .setParameter("bookId", bookId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
}