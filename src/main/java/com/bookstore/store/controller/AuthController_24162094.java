package com.bookstore.store.controller;

import com.bookstore.store.dao.UserDAOImpl_24162094;
import com.bookstore.store.entity.User_24162094;
import com.bookstore.store.service.IUserService_24162094;
import com.bookstore.store.service.UserServiceImpl_24162094;
import com.bookstore.store.utils.MailUtil_24162094;
import com.bookstore.store.utils.PasswordUtil_24162094;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Random;

@WebServlet(urlPatterns = {"/auth/*", "/login", "/register", "/verify-otp", "/logout"})
public class AuthController_24162094 extends HttpServlet {
    private static final long serialVersionUID = 24162094L;
    private static final long OTP_VALIDITY_MILLIS = 5 * 60 * 1000L;
    private final IUserService_24162094 userService =
            new UserServiceImpl_24162094(new UserDAOImpl_24162094());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = route(request);
        if ("/login".equals(path)) {
            forward(request, response, "/WEB-INF/views/login.jsp");
        } else if ("/register".equals(path)) {
            forward(request, response, "/WEB-INF/views/register.jsp");
        } else if ("/verify-otp".equals(path)) {
            forward(request, response, "/WEB-INF/views/verify-otp.jsp");
        } else if ("/logout".equals(path)) {
            logout(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String path = route(request);
        if ("/register".equals(path)) {
            register(request, response);
        } else if ("/verify-otp".equals(path)) {
            verifyOtp(request, response);
        } else if ("/login".equals(path)) {
            login(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        if (isBlank(email) || isBlank(fullName) || isBlank(password)) {
            redirect(request, response, "/register?error=missing");
            return;
        }
        if (userService.findByEmail(email.trim()) != null) {
            redirect(request, response, "/register?error=exists");
            return;
        }

        User_24162094 user = new User_24162094();
        user.setUsername(email.trim());
        user.setEmail(email.trim());
        user.setFullName(fullName.trim());
        user.setPhone(phone == null ? null : phone.trim());
        user.setPassword(PasswordUtil_24162094.md5(password));
        user.setAdmin(false);

        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        HttpSession session = request.getSession(true);
        session.setAttribute("pendingUser", user);
        session.setAttribute("registrationOtp", otp);
        session.setAttribute("otpExpiresAt", System.currentTimeMillis() + OTP_VALIDITY_MILLIS);
        try {
            MailUtil_24162094.sendOTP(user.getEmail(), otp);
            redirect(request, response, "/verify-otp");
        } catch (IllegalStateException exception) {
            session.removeAttribute("pendingUser");
            session.removeAttribute("registrationOtp");
            session.removeAttribute("otpExpiresAt");
            redirect(request, response, "/register?error=mail");
        }
    }

    private void verifyOtp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String input = request.getParameter("otp");
        if (session == null || session.getAttribute("pendingUser") == null
                || session.getAttribute("registrationOtp") == null
                || System.currentTimeMillis() > (Long) session.getAttribute("otpExpiresAt")) {
            redirect(request, response, "/register?error=expired");
            return;
        }
        if (!session.getAttribute("registrationOtp").equals(input)) {
            redirect(request, response, "/verify-otp?error=invalid");
            return;
        }

        User_24162094 user = (User_24162094) session.getAttribute("pendingUser");
        userService.save(user);
        session.removeAttribute("pendingUser");
        session.removeAttribute("registrationOtp");
        session.removeAttribute("otpExpiresAt");
        redirect(request, response, "/login?registered=1");
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User_24162094 user = isBlank(email) ? null : userService.findByEmail(email.trim());
        if (user == null || !user.getPassword().equals(PasswordUtil_24162094.md5(password == null ? "" : password))) {
            redirect(request, response, "/login?error=invalid");
            return;
        }

        HttpSession oldSession = request.getSession(false);
        Object cart = oldSession == null ? null : oldSession.getAttribute("cart");
        String redirectAfterLogin = oldSession == null ? null
                : (String) oldSession.getAttribute("redirectAfterLogin");
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        if (cart != null) {
            session.setAttribute("cart", cart);
        }
        if (user.isAdmin()) {
            redirect(request, response, "/admin/dashboard");
        } else if ("/checkout".equals(redirectAfterLogin)) {
            redirect(request, response, "/checkout");
        } else {
            redirect(request, response, "/home");
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        redirect(request, response, "/login?logout=1");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String view) throws IOException {
        try {
            request.getRequestDispatcher(view).forward(request, response);
        } catch (Exception exception) {
            throw new IOException(exception);
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String route(HttpServletRequest request) {
        String path = request.getPathInfo();
        return path == null ? request.getServletPath() : path;
    }
}
