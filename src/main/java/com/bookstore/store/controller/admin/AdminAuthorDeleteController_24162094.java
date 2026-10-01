package com.bookstore.store.controller.admin;

import com.bookstore.store.dao.AuthorDAOImpl_24162094;
import com.bookstore.store.service.AuthorServiceImpl_24162094;
import com.bookstore.store.service.IAuthorService_24162094;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/author-delete")
public class AdminAuthorDeleteController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IAuthorService_24162094 authorService =
            new AuthorServiceImpl_24162094(new AuthorDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/authors?error=invalid-id");
            return;
        }
        try {
            authorService.deleteById(id);
            response.sendRedirect(request.getContextPath() + "/admin/authors");
        } catch (RuntimeException exception) {
            response.sendRedirect(request.getContextPath() + "/admin/authors?error=delete-failed");
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