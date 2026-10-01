package com.bookstore.store.dao;

import com.bookstore.store.entity.Author_24162094;
import com.bookstore.store.utils.JPAUtil_24162094;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AuthorDAOImpl_24162094 implements IAuthorDAO_24162094 {
    @Override
    public List<Author_24162094> getAllAuthors() {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            return entityManager.createQuery("select a from Author_24162094 a order by a.authorId", Author_24162094.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Author_24162094 getAuthorById(Integer id) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            return entityManager.find(Author_24162094.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Author_24162094> getAllAuthors(int page, int pageSize) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            return entityManager.createQuery("select a from Author_24162094 a order by a.authorId", Author_24162094.class)
                    .setFirstResult(Math.max(0, page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countAllAuthors() {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            return entityManager.createQuery("select count(a) from Author_24162094 a", Long.class)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Author_24162094 save(Author_24162094 author) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(author);
            entityManager.getTransaction().commit();
            return author;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Author_24162094 update(Author_24162094 author) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Author_24162094 updatedAuthor = entityManager.merge(author);
            entityManager.getTransaction().commit();
            return updatedAuthor;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Integer id) {
        deleteById(id);
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("delete from book_author where author_id = :authorId")
                    .setParameter("authorId", id)
                    .executeUpdate();
            Author_24162094 author = entityManager.find(Author_24162094.class, id);
            if (author != null) {
                entityManager.remove(author);
            }
            entityManager.getTransaction().commit();
        } catch (RuntimeException exception) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }
}
