<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property="title">BookStore</sitemesh:write></title>
    <sitemesh:write property="head" />
    <style>
        :root { font-family: Arial, sans-serif; color: #1f2937; background: #f3f4f6; }
        body { max-width: 1100px; margin: 0 auto; background: #fff; min-height: 100vh; }
        header { background: #1e3a5f; padding: 16px 24px; }
        nav { display: flex; gap: 20px; flex-wrap: wrap; }
        nav a { color: #fff; font-weight: 600; text-decoration: none; }
        main { padding: 24px; }
        h1 { color: #1e3a5f; }
        .book-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 20px; }
        .book-card { padding: 18px; border: 1px solid #dbe2ea; border-radius: 8px; background: #fff; box-shadow: 0 2px 8px #1e3a5f18; }
        .book-card img, .book-cover-placeholder { width: 100%; height: 220px; object-fit: contain; background: #eef2f7; }
        .book-cover-placeholder { display: grid; place-items: center; color: #64748b; }
        .image-fallback { display: none; }
        footer { padding: 18px 24px; color: #64748b; border-top: 1px solid #dbe2ea; }
    </style>
</head>
<body>
    <header>
        <nav>
            <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
            <a href="${pageContext.request.contextPath}/books">Sản phẩm</a>
            <a href="${pageContext.request.contextPath}/cart">Giỏ hàng<c:if test="${not empty sessionScope.cart}"> (${fn:length(sessionScope.cart)})</c:if></a>
            <c:choose>
                <c:when test="${sessionScope.user == null}">
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </c:when>
                <c:otherwise>
                <a href="${pageContext.request.contextPath}/auth/logout">Đăng xuất</a>
                </c:otherwise>
            </c:choose>
            <c:if test="${sessionScope.user != null && sessionScope.user.admin}">
                <a href="${pageContext.request.contextPath}/admin/books">Trang quản trị</a>
            </c:if>
        </nav>
    </header>
    <main>
        <sitemesh:write property="body" />
    </main>
    <footer>
        <p>Họ tên: TrinhQuocPhu - MSSV: 24162094 - Mã đề: Đề thi Quá trình – HK1 – 2026-2027 - Môn Lập Trình Web – Đề số 01</p>
    </footer>
</body>
</html>
