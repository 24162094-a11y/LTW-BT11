<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Đăng nhập</title>
<h1>Đăng nhập</h1>
<c:if test="${not empty param.error}">
	<p>Thông tin đăng nhập không hợp lệ hoặc phiên đăng nhập chưa tồn tại.</p>
</c:if>
<c:if test="${not empty param.registered}">
	<p>Đăng ký thành công. Vui lòng đăng nhập.</p>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/auth/login">
	<label for="email">Email</label>
	<input id="email" name="email" type="email" required>
	<label for="password">Mật khẩu</label>
	<input id="password" name="password" type="password" required>
	<button type="submit">Đăng nhập</button>
</form>
<p><a href="${pageContext.request.contextPath}/register">Đăng ký tài khoản</a></p>
