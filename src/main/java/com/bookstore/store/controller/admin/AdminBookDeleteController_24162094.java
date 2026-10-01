package com.bookstore.store.controller.admin;

import com.bookstore.store.dao.BookDAOImpl_24162094;
import com.bookstore.store.service.BookServiceImpl_24162094;
import com.bookstore.store.service.IBookService_24162094;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/book-delete")
public class AdminBookDeleteController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IBookService_24162094 bookService =
            new BookServiceImpl_24162094(new BookDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/books?error=invalid-id");
            return;
        }

        try {
            bookService.deleteById(id);
            response.sendRedirect(request.getContextPath() + "/admin/books");
        } catch (RuntimeException exception) {
            response.sendRedirect(request.getContextPath() + "/admin/books?error=delete-failed");
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