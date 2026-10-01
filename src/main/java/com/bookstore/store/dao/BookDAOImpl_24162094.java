package com.bookstore.store.dao;

import com.bookstore.store.entity.Book_24162094;
import com.bookstore.store.entity.Author_24162094;
import com.bookstore.store.utils.JPAUtil_24162094;
import jakarta.persistence.EntityManager;

import java.util.List;

public class BookDAOImpl_24162094 implements IBookDAO_24162094 {
    @Override
    public List<Book_24162094> getAllBooks(int page, int pageSize) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            List<Book_24162094> books = entityManager.createQuery("select distinct b from Book_24162094 b left join fetch b.authors order by b.bookId", Book_24162094.class)
                    .setFirstResult(Math.max(0, page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
            if (books.isEmpty()) {
                return books;
            }
            List<Object[]> reviewCounts = entityManager.createQuery(
                            "select r.book.bookId, count(r) from Rating_24162094 r "
                                    + "where r.book.bookId in :bookIds group by r.book.bookId", Object[].class)
                    .setParameter("bookIds", books.stream().map(Book_24162094::getBookId).toList())
                    .getResultList();
            for (Object[] reviewCount : reviewCounts) {
                Integer bookId = (Integer) reviewCount[0];
                books.stream().filter(book -> book.getBookId().equals(bookId)).findFirst()
                        .ifPresent(book -> book.setReviewCount((Long) reviewCount[1]));
            }
            return books;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countAllBooks() {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            return entityManager.createQuery("select count(b) from Book_24162094 b", Long.class)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Book_24162094 getBookById(Integer id) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            List<Book_24162094> books = entityManager.createQuery(
                            "select distinct b from Book_24162094 b "
                                    + "left join fetch b.authors where b.bookId = :id",
                            Book_24162094.class)
                    .setParameter("id", id)
                    .getResultList();
            return books.isEmpty() ? null : books.get(0);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Book_24162094 save(Book_24162094 book) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            book.setAuthors(resolveAuthors(entityManager, book.getAuthors()));
            entityManager.persist(book);
            entityManager.getTransaction().commit();
            return book;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Book_24162094 update(Book_24162094 book) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            book.setAuthors(resolveAuthors(entityManager, book.getAuthors()));
            Book_24162094 updatedBook = entityManager.merge(book);
            entityManager.getTransaction().commit();
            return updatedBook;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager entityManager = JPAUtil_24162094.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            entityManager.createQuery("delete from Rating_24162094 r where r.book.bookId = :bookId")
                    .setParameter("bookId", id)
                    .executeUpdate();
            entityManager.createNativeQuery("delete from book_author where bookid = :bookId")
                    .setParameter("bookId", id)
                    .executeUpdate();

            Book_24162094 book = entityManager.find(Book_24162094.class, id);
            if (book != null) {
                entityManager.remove(book);
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

    @Override
    public void delete(Integer id) {
        deleteById(id);
    }

    private List<Author_24162094> resolveAuthors(EntityManager entityManager,
                                                  List<Author_24162094> authors) {
        return authors == null ? List.of() : authors.stream()
                .map(author -> entityManager.getReference(Author_24162094.class, author.getAuthorId()))
                .toList();
    }
}
