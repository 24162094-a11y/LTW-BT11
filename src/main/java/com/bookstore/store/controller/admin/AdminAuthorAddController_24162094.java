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
import java.time.LocalDate;

@WebServlet("/admin/author-add")
public class AdminAuthorAddController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IAuthorService_24162094 authorService =
            new AuthorServiceImpl_24162094(new AuthorDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/author-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            authorService.save(readAuthor(request, null));
            response.sendRedirect(request.getContextPath() + "/admin/authors");
        } catch (IllegalArgumentException exception) {
            response.sendRedirect(request.getContextPath() + "/admin/author-add?error=invalid");
        }
    }

    static Author_24162094 readAuthor(HttpServletRequest request, Integer id) {
        String name = request.getParameter("name");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name is required");
        }
        Author_24162094 author = new Author_24162094();
        author.setAuthorId(id);
        author.setName(name.trim());
        author.setDateOfBirth(parseDate(request.getParameter("dateOfBirth")));
        return author;
    }

    private static LocalDate parseDate(String value) {
        try {
            return value == null || value.isBlank() ? null : LocalDate.parse(value);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Date is invalid", exception);
        }
    }
}