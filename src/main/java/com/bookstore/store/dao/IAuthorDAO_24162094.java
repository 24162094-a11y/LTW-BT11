package com.bookstore.store.dao;

import com.bookstore.store.entity.Author_24162094;

import java.util.List;

public interface IAuthorDAO_24162094 {
    List<Author_24162094> getAllAuthors();

    Author_24162094 getAuthorById(Integer id);

    List<Author_24162094> getAllAuthors(int page, int pageSize);

    long countAllAuthors();

    Author_24162094 save(Author_24162094 author);

    Author_24162094 update(Author_24162094 author);

    void delete(Integer id);

    void deleteById(Integer id);
}
