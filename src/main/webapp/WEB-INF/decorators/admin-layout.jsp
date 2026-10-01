<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property="title">BookStore Admin</sitemesh:write></title>
    <sitemesh:write property="head" />
</head>
<body>
    <header>
        <nav>
            <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
            <a href="${pageContext.request.contextPath}/admin/dashboard">Trang quản trị</a>
            <a href="${pageContext.request.contextPath}/admin/books">Quản lý Sách</a>
            <a href="${pageContext.request.contextPath}/admin/authors">Quản lý Tác giả</a>
            <a href="${pageContext.request.contextPath}/auth/logout">Đăng xuất</a>
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
