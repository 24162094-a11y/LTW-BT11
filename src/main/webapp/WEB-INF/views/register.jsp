<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Đăng ký</title>
<h1>Đăng ký tài khoản</h1>
<c:if test="${not empty param.error}">
    <p>Không thể đăng ký. Vui lòng kiểm tra lại thông tin hoặc cấu hình email.</p>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/auth/register">
    <label for="email">Email</label>
    <input id="email" name="email" type="email" required>
    <label for="fullName">Họ tên</label>
    <input id="fullName" name="fullName" type="text" required>
    <label for="phone">Số điện thoại</label>
    <input id="phone" name="phone" type="tel">
    <label for="password">Mật khẩu</label>
    <input id="password" name="password" type="password" required>
    <button type="submit">Gửi mã OTP</button>
</form>