package com.bookstore.store.service;

import com.bookstore.store.entity.Author_24162094;

import java.util.List;

public interface IAuthorService_24162094 {
    List<Author_24162094> getAllAuthors();

    Author_24162094 getAuthorById(Integer id);

    List<Author_24162094> getAuthorsWithPagination(int page, int pageSize);

    long countAllAuthors();

    Author_24162094 save(Author_24162094 author);

    Author_24162094 update(Author_24162094 author);

    void delete(Integer id);

    void deleteById(Integer id);
}
