package com.bookstore.store.dao;

import com.bookstore.store.entity.Book_24162094;

import java.util.List;

public interface IBookDAO_24162094 {
    List<Book_24162094> getAllBooks(int page, int pageSize);

    long countAllBooks();

    Book_24162094 getBookById(Integer id);

    Book_24162094 save(Book_24162094 book);

    Book_24162094 update(Book_24162094 book);

    void deleteById(Integer id);

    void delete(Integer id);
}
