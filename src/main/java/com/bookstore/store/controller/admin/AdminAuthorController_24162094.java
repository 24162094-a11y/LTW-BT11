package com.bookstore.store.controller.admin;

import com.bookstore.store.dao.AuthorDAOImpl_24162094;
import com.bookstore.store.service.AuthorServiceImpl_24162094;
import com.bookstore.store.service.IAuthorService_24162094;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/authors")
public class AdminAuthorController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private static final int PAGE_SIZE = 6;
    private final IAuthorService_24162094 authorService =
            new AuthorServiceImpl_24162094(new AuthorDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = parsePage(request.getParameter("page"));
        long totalAuthors = authorService.countAllAuthors();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalAuthors / PAGE_SIZE));
        page = Math.min(page, totalPages);

        request.setAttribute("authors", authorService.getAuthorsWithPagination(page, PAGE_SIZE));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/admin/author-list.jsp").forward(request, response);
    }

    private int parsePage(String value) {
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return 1;
        }
    }
}