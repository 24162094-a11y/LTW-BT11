package com.bookstore.store.controller;

import com.bookstore.store.dao.BookDAOImpl_24162094;
import com.bookstore.store.service.IBookService_24162094;
import com.bookstore.store.service.BookServiceImpl_24162094;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/home", "/"})
public class HomeController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private static final int PAGE_SIZE = 6;
    private final IBookService_24162094 bookService =
            new BookServiceImpl_24162094(new BookDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = parsePage(request.getParameter("page"));
        long totalBooks = bookService.countAllBooks();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalBooks / PAGE_SIZE));
        page = Math.min(page, totalPages);

        request.setAttribute("books", bookService.getBooksWithPagination(page, PAGE_SIZE));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/user/home.jsp").forward(request, response);
    }

    private int parsePage(String value) {
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return 1;
        }
    }
}
