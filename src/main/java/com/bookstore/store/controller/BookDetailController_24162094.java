package com.bookstore.store.controller;

import com.bookstore.store.dao.BookDAOImpl_24162094;
import com.bookstore.store.dao.RatingDAOImpl_24162094;
import com.bookstore.store.entity.Book_24162094;
import com.bookstore.store.service.BookServiceImpl_24162094;
import com.bookstore.store.service.IBookService_24162094;
import com.bookstore.store.service.IRatingService_24162094;
import com.bookstore.store.service.RatingServiceImpl_24162094;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/book-detail")
public class BookDetailController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IBookService_24162094 bookService =
            new BookServiceImpl_24162094(new BookDAOImpl_24162094());
    private final IRatingService_24162094 ratingService =
            new RatingServiceImpl_24162094(new RatingDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer bookId = parseId(request.getParameter("id"));
        if (bookId == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Book_24162094 book = bookService.getBookById(bookId);
        if (book == null) {
            response.sendRedirect(request.getContextPath() + "/home?error=book-not-found");
            return;
        }

        request.setAttribute("book", book);
        request.setAttribute("ratings", ratingService.getRatingsByBookId(bookId));
        request.getRequestDispatcher("/WEB-INF/views/user/book-detail.jsp").forward(request, response);
    }

    private Integer parseId(String value) {
        try {
            return value == null ? null : Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}