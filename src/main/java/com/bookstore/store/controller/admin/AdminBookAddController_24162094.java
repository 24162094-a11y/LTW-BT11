package com.bookstore.store.controller.admin;

import com.bookstore.store.dao.AuthorDAOImpl_24162094;
import com.bookstore.store.dao.BookDAOImpl_24162094;
import com.bookstore.store.entity.Author_24162094;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/book-add")
public class AdminBookAddController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IBookService_24162094 bookService =
            new BookServiceImpl_24162094(new BookDAOImpl_24162094());
    private final IAuthorService_24162094 authorService =
            new AuthorServiceImpl_24162094(new AuthorDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("authors", authorService.getAllAuthors());
        request.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            Book_24162094 book = readBook(request, null);
            bookService.save(book);
            response.sendRedirect(request.getContextPath() + "/admin/books");
        } catch (IllegalArgumentException exception) {
            response.sendRedirect(request.getContextPath() + "/admin/book-add?error=invalid");
        }
    }

    static Book_24162094 readBook(HttpServletRequest request, Integer bookId) {
        String title = required(request.getParameter("title"));
        Integer isbn = requiredInteger(request.getParameter("isbn"));
        Book_24162094 book = new Book_24162094();
        book.setBookId(bookId);
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPublisher(request.getParameter("publisher"));
        book.setPrice(optionalDecimal(request.getParameter("price")));
        book.setDescription(request.getParameter("description"));
        book.setPublishDate(optionalDate(request.getParameter("publishDate")));
        book.setCoverImage(request.getParameter("coverImage"));
        book.setQuantity(optionalInteger(request.getParameter("quantity")));
        book.setAuthors(readAuthors(request));
        return book;
    }

    private static List<Author_24162094> readAuthors(HttpServletRequest request) {
        List<Author_24162094> authors = new ArrayList<>();
        String[] authorIds = request.getParameterValues("authorIds");
        if (authorIds != null) {
            for (String authorId : authorIds) {
                Integer id = optionalInteger(authorId);
                if (id != null) {
                    Author_24162094 author = new Author_24162094();
                    author.setAuthorId(id);
                    authors.add(author);
                }
            }
        }
        return authors;
    }

    private static String required(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required value is missing");
        }
        return value.trim();
    }

    private static Integer requiredInteger(String value) {
        Integer result = optionalInteger(value);
        if (result == null) {
            throw new IllegalArgumentException("Required number is invalid");
        }
        return result;
    }

    private static Integer optionalInteger(String value) {
        try {
            return value == null || value.isBlank() ? null : Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Number is invalid", exception);
        }
    }

    private static BigDecimal optionalDecimal(String value) {
        try {
            return value == null || value.isBlank() ? null : new BigDecimal(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Decimal is invalid", exception);
        }
    }

    private static LocalDate optionalDate(String value) {
        try {
            return value == null || value.isBlank() ? null : LocalDate.parse(value);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Date is invalid", exception);
        }
    }
}