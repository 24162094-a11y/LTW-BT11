package com.bookstore.store.controller;

import com.bookstore.store.entity.User_24162094;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter_24162094 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Object sessionUser = httpRequest.getSession(false) == null
                ? null : httpRequest.getSession(false).getAttribute("user");

        if (!(sessionUser instanceof User_24162094)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=required");
        } else if (!((User_24162094) sessionUser).isAdmin()) {
            httpRequest.getSession().setAttribute("errorMessage", "Bạn không có quyền truy cập");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
        } else {
            chain.doFilter(request, response);
        }
    }
}