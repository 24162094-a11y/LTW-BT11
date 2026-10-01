package com.bookstore.store.controller;

import com.bookstore.store.dao.BookDAOImpl_24162094;
import com.bookstore.store.entity.Book_24162094;
import com.bookstore.store.entity.CartItem_24162094;
import com.bookstore.store.service.BookServiceImpl_24162094;
import com.bookstore.store.service.CartService_24162094;
import com.bookstore.store.service.CartService_24162094.CartError;
import com.bookstore.store.service.IBookService_24162094;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/cart/*")
public class CartController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final IBookService_24162094 bookService =
            new BookServiceImpl_24162094(new BookDAOImpl_24162094());
    private final CartService_24162094 cartService = new CartService_24162094();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getPathInfo() != null && !"/".equals(request.getPathInfo())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HttpSession session = request.getSession(true);
        List<CartItem_24162094> items;
        synchronized (session) {
            items = loadItems(cart(session));
        }
        BigDecimal total = items.stream().map(CartItem_24162094::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        request.setAttribute("cartItems", items);
        request.setAttribute("cartTotal", total);
        request.getRequestDispatcher("/WEB-INF/views/user/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();
        HttpSession session = request.getSession(true);
        String result;
        synchronized (session) {
            Map<Integer, Integer> cart = cart(session);
            Integer bookId = parsePositive(request.getParameter("bookId"));
            result = switch (path == null ? "" : path) {
                case "/remove" -> {
                    if (bookId == null) {
                        yield "invalid";
                    }
                    cartService.remove(cart, bookId);
                    yield "removed";
                }
                case "/add", "/update" -> {
                    Integer quantity = parsePositive(request.getParameter("quantity"));
                    Book_24162094 book = bookId == null ? null : bookService.getBookById(bookId);
                    CartError outcome = quantity == null ? CartError.INVALID
                            : ("/add".equals(path) ? cartService.add(cart, book, quantity)
                            : cartService.update(cart, book, quantity));
                    yield switch (outcome) {
                        case SUCCESS -> "/add".equals(path) ? "added" : "updated";
                        case UNAVAILABLE -> "unavailable";
                        case LIMIT -> "limit";
                        case INVALID -> "invalid";
                    };
                }
                default -> null;
            };
            if (result == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/cart?" + result + "=1");
    }

    private List<CartItem_24162094> loadItems(Map<Integer, Integer> cart) {
        List<CartItem_24162094> items = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : new HashMap<>(cart).entrySet()) {
            Book_24162094 book = bookService.getBookById(entry.getKey());
            if (book == null) {
                cart.remove(entry.getKey());
                continue;
            }
            int available = Optional.ofNullable(book.getQuantity()).orElse(0);
            if (available < 1) {
                cart.remove(entry.getKey());
                continue;
            }
            int quantity = Math.min(entry.getValue(), available);
            if (quantity != entry.getValue()) {
                cart.put(entry.getKey(), quantity);
            }
            items.add(new CartItem_24162094(book, quantity));
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> cart(HttpSession session) {
        Object value = session.getAttribute("cart");
        if (value instanceof Map<?, ?>) {
            return (Map<Integer, Integer>) value;
        }
        Map<Integer, Integer> cart = new HashMap<>();
        session.setAttribute("cart", cart);
        return cart;
    }

    private Integer parsePositive(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}