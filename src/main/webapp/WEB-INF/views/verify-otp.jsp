<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Xác thực OTP</title>
<h1>Xác thực OTP</h1>
<c:if test="${not empty param.error}">
    <p>Mã OTP không đúng hoặc đã hết hạn.</p>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/auth/verify-otp">
    <label for="otp">Mã OTP</label>
    <input id="otp" name="otp" type="text" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" required>
    <button type="submit">Xác nhận</button>
</form>