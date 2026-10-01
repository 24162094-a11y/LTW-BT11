package com.bookstore.store.service;

import com.bookstore.store.dao.IBookDAO_24162094;
import com.bookstore.store.entity.Book_24162094;

import java.util.List;

public class BookServiceImpl_24162094 implements IBookService_24162094 {
    private final IBookDAO_24162094 bookDAO;

    public BookServiceImpl_24162094(IBookDAO_24162094 bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public List<Book_24162094> getBooksWithPagination(int page, int pageSize) {
        return bookDAO.getAllBooks(page, pageSize);
    }

    @Override
    public long countAllBooks() {
        return bookDAO.countAllBooks();
    }

    @Override
    public Book_24162094 getBookById(Integer id) {
        return bookDAO.getBookById(id);
    }

    @Override
    public Book_24162094 save(Book_24162094 book) {
        return bookDAO.save(book);
    }

    @Override
    public Book_24162094 update(Book_24162094 book) {
        return bookDAO.update(book);
    }

    @Override
    public void delete(Integer id) {
        deleteById(id);
    }

    @Override
    public void deleteById(Integer id) {
        bookDAO.deleteById(id);
    }
}
