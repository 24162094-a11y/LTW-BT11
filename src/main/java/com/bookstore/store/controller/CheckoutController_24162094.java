package com.bookstore.store.controller;

import com.bookstore.store.entity.CustomerOrder_24162094;
import com.bookstore.store.entity.User_24162094;
import com.bookstore.store.service.CheckoutService_24162094;
import com.bookstore.store.service.CheckoutService_24162094.CheckoutError;
import com.bookstore.store.service.CheckoutService_24162094.CheckoutException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/checkout", "/order-success"})
public class CheckoutController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final CheckoutService_24162094 checkoutService = new CheckoutService_24162094();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User_24162094 user = currentUser(request);
        if (user == null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", "/checkout");
            response.sendRedirect(request.getContextPath() + "/login?error=required");
            return;
        }

        if ("/order-success".equals(request.getServletPath())) {
            showOrder(request, response, user);
            return;
        }

        Map<Integer, Integer> cart = currentCart(request.getSession(false));
        if (cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?empty=1");
            return;
        }
        request.setAttribute("recipientName", user.getFullName());
        request.setAttribute("phone", user.getPhone());
        request.getRequestDispatcher("/WEB-INF/views/user/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        User_24162094 user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=required");
            return;
        }

        HttpSession session = request.getSession(false);
        Long orderId;
        synchronized (session) {
            Map<Integer, Integer> cart = currentCart(session);
            if (cart.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart?empty=1");
                return;
            }
            try {
                orderId = checkoutService.createCodOrder(user.getUserId(),
                        request.getParameter("recipientName"), request.getParameter("phone"),
                        request.getParameter("shippingAddress"), request.getParameter("note"),
                        new HashMap<>(cart));
            } catch (CheckoutException exception) {
                response.sendRedirect(request.getContextPath() + "/checkout?error="
                        + errorKey(exception.getError()));
                return;
            }
            synchronized (session) {
                session.removeAttribute("cart");
            }
        }
        response.sendRedirect(request.getContextPath() + "/order-success?id=" + orderId);
    }

    private void showOrder(HttpServletRequest request, HttpServletResponse response,
                           User_24162094 user) throws ServletException, IOException {
        Long orderId = parseId(request.getParameter("id"));
        CustomerOrder_24162094 order = checkoutService.findOrderForUser(orderId, user.getUserId());
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("order", order);
        request.getRequestDispatcher("/WEB-INF/views/user/order-success.jsp").forward(request, response);
    }

    private User_24162094 currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object value = session == null ? null : session.getAttribute("user");
        return value instanceof User_24162094 ? (User_24162094) value : null;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> currentCart(HttpSession session) {
        if (session == null) {
            return Map.of();
        }
        Object value = session.getAttribute("cart");
        return value instanceof Map<?, ?> ? (Map<Integer, Integer>) value : Map.of();
    }

    private Long parseId(String value) {
        try {
            return value == null ? null : Long.valueOf(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String errorKey(CheckoutError error) {
        return switch (error) {
            case INVALID_DETAILS -> "details";
            case INSUFFICIENT_STOCK, BOOK_NOT_FOUND -> "stock";
            case INVALID_PRICE -> "price";
            default -> "cart";
        };
    }
}