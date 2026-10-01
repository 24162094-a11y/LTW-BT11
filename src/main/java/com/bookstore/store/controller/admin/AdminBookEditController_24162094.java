package com.bookstore.store.controller.admin;

import com.bookstore.store.dao.AuthorDAOImpl_24162094;
import com.bookstore.store.dao.BookDAOImpl_24162094;
import com.bookstore.store.entity.Book_24162094;
import com.bookstore.store.service.AuthorServiceImpl_24162094;
import com.bookstore.store.service.BookServiceImpl_24162094;
import com.bookstore.store.service.IAuthorService_24162094;
import com.bookstore.store.service.IBookService_24162094;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/book-edit")
public class AdminBookEditController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IBookService_24162094 bookService =
            new BookServiceImpl_24162094(new BookDAOImpl_24162094());
    private final IAuthorService_24162094 authorService =
            new AuthorServiceImpl_24162094(new AuthorDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = parseId(request.getParameter("id"));
        Book_24162094 book = id == null ? null : bookService.getBookById(id);
        if (book == null) {
            response.sendRedirect(request.getContextPath() + "/admin/books?error=not-found");
            return;
        }
        request.setAttribute("book", book);
        request.setAttribute("authors", authorService.getAllAuthors());
        request.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        Integer id = parseId(request.getParameter("bookId"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/books?error=invalid");
            return;
        }
        try {
            bookService.update(AdminBookAddController_24162094.readBook(request, id));
            response.sendRedirect(request.getContextPath() + "/admin/books");
        } catch (IllegalArgumentException exception) {
            response.sendRedirect(request.getContextPath() + "/admin/book-edit?id=" + id + "&error=invalid");
        }
    }

    private Integer parseId(String value) {
        try {
            return value == null ? null : Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}