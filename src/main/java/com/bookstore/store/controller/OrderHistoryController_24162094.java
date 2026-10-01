package com.bookstore.store.controller;

import com.bookstore.store.entity.OrderStatus_24162094;
import com.bookstore.store.entity.User_24162094;
import com.bookstore.store.service.CheckoutService_24162094;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;

@WebServlet("/orders")
public class OrderHistoryController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private final CheckoutService_24162094 checkoutService = new CheckoutService_24162094();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object sessionUser = session == null ? null : session.getAttribute("user");
        if (!(sessionUser instanceof User_24162094)) {
            response.sendRedirect(request.getContextPath() + "/login?error=required");
            return;
        }

        String statusCode = request.getParameter("status");
        OrderStatus_24162094 selectedStatus = OrderStatus_24162094.fromCode(statusCode);
        if (statusCode != null && !statusCode.isBlank() && selectedStatus == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order status");
            return;
        }

        User_24162094 user = (User_24162094) sessionUser;
        request.setAttribute("orders", checkoutService.findOrdersForUser(
                user.getUserId(), selectedStatus));
        request.setAttribute("orderStatuses", Arrays.asList(OrderStatus_24162094.values()));
        request.setAttribute("selectedStatus", selectedStatus == null ? "" : selectedStatus.getCode());
        request.getRequestDispatcher("/WEB-INF/views/user/order-history.jsp").forward(request, response);
    }
}