package com.bookstore.store.controller.admin;

import com.bookstore.store.dao.AuthorDAOImpl_24162094;
import com.bookstore.store.entity.Author_24162094;
import com.bookstore.store.service.AuthorServiceImpl_24162094;
import com.bookstore.store.service.IAuthorService_24162094;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/author-edit")
public class AdminAuthorEditController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IAuthorService_24162094 authorService =
            new AuthorServiceImpl_24162094(new AuthorDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = parseId(request.getParameter("id"));
        Author_24162094 author = id == null ? null : authorService.getAuthorById(id);
        if (author == null) {
            response.sendRedirect(request.getContextPath() + "/admin/authors?error=not-found");
            return;
        }
        request.setAttribute("author", author);
        request.getRequestDispatcher("/WEB-INF/views/admin/author-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        Integer id = parseId(request.getParameter("authorId"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/authors?error=invalid-id");
            return;
        }
        try {
            authorService.update(AdminAuthorAddController_24162094.readAuthor(request, id));
            response.sendRedirect(request.getContextPath() + "/admin/authors");
        } catch (IllegalArgumentException exception) {
            response.sendRedirect(request.getContextPath() + "/admin/author-edit?id=" + id + "&error=invalid");
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