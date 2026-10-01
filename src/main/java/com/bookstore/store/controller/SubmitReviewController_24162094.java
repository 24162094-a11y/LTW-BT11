package com.bookstore.store.controller;

import com.bookstore.store.entity.RatingId_24162094;
import com.bookstore.store.entity.Rating_24162094;
import com.bookstore.store.entity.User_24162094;
import com.bookstore.store.dao.RatingDAOImpl_24162094;
import com.bookstore.store.service.IRatingService_24162094;
import com.bookstore.store.service.RatingServiceImpl_24162094;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/submit-review")
public class SubmitReviewController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IRatingService_24162094 ratingService =
            new RatingServiceImpl_24162094(new RatingDAOImpl_24162094());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Object sessionUser = request.getSession(false) == null
                ? null : request.getSession(false).getAttribute("user");
        if (!(sessionUser instanceof User_24162094 user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer bookId = parseInteger(request.getParameter("bookid"));
        Integer ratingValue = parseInteger(request.getParameter("rating"));
        String reviewText = request.getParameter("review_text");
        if (bookId == null || ratingValue == null || ratingValue < 1 || ratingValue > 5
                || reviewText == null || reviewText.isBlank()) {
            redirectToDetail(request, response, bookId, "invalid-review");
            return;
        }

        Rating_24162094 rating = new Rating_24162094();
        rating.setId(new RatingId_24162094(user.getUserId(), bookId));
        rating.setUser(user);
        rating.setRating(ratingValue);
        rating.setReviewText(reviewText.trim());
        ratingService.saveRating(rating);
        redirectToDetail(request, response, bookId, null);
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private void redirectToDetail(HttpServletRequest request, HttpServletResponse response,
                                  Integer bookId, String error) throws IOException {
        if (bookId == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        String target = request.getContextPath() + "/book-detail?id=" + bookId;
        if (error != null) {
            target += "&error=" + error;
        }
        response.sendRedirect(target);
    }
}