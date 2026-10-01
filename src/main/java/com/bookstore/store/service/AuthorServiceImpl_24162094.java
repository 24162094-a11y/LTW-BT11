package com.bookstore.store.service;

import com.bookstore.store.dao.IAuthorDAO_24162094;
import com.bookstore.store.entity.Author_24162094;

import java.util.List;

public class AuthorServiceImpl_24162094 implements IAuthorService_24162094 {
    private final IAuthorDAO_24162094 authorDAO;

    public AuthorServiceImpl_24162094(IAuthorDAO_24162094 authorDAO) {
        this.authorDAO = authorDAO;
    }

    @Override
    public List<Author_24162094> getAllAuthors() {
        return authorDAO.getAllAuthors();
    }

    @Override
    public Author_24162094 getAuthorById(Integer id) {
        return authorDAO.getAuthorById(id);
    }

    @Override
    public List<Author_24162094> getAuthorsWithPagination(int page, int pageSize) {
        return authorDAO.getAllAuthors(page, pageSize);
    }

    @Override
    public long countAllAuthors() {
        return authorDAO.countAllAuthors();
    }

    @Override
    public Author_24162094 save(Author_24162094 author) {
        return authorDAO.save(author);
    }

    @Override
    public Author_24162094 update(Author_24162094 author) {
        return authorDAO.update(author);
    }

    @Override
    public void delete(Integer id) {
        deleteById(id);
    }

    @Override
    public void deleteById(Integer id) {
        authorDAO.deleteById(id);
    }
}
