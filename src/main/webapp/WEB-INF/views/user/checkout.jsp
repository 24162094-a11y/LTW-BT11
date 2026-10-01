<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Thanh toán COD</title>

<h1>Thông tin nhận hàng</h1>
<c:if test="${param.error == 'details'}"><p role="alert">Vui lòng kiểm tra họ tên, số điện thoại và địa chỉ nhận hàng.</p></c:if>
<c:if test="${param.error == 'stock'}"><p role="alert">Tồn kho vừa thay đổi. Hãy kiểm tra lại giỏ hàng trước khi đặt.</p></c:if>
<c:if test="${param.error == 'price'}"><p role="alert">Giá của một sản phẩm không hợp lệ; đơn hàng chưa được tạo.</p></c:if>
<c:if test="${param.error == 'cart'}"><p role="alert">Giỏ hàng không hợp lệ hoặc đang trống.</p></c:if>

<form method="post" action="${pageContext.request.contextPath}/checkout">
    <p>
        <label for="recipientName">Họ tên người nhận</label><br>
        <input id="recipientName" name="recipientName" type="text" maxlength="150"
               value="<c:out value='${recipientName}' />" required>
    </p>
    <p>
        <label for="phone">Số điện thoại</label><br>
        <input id="phone" name="phone" type="tel" maxlength="20"
               value="<c:out value='${phone}' />" required>
    </p>
    <p>
        <label for="shippingAddress">Địa chỉ nhận hàng</label><br>
        <textarea id="shippingAddress" name="shippingAddress" maxlength="500" rows="4" required></textarea>
    </p>
    <p>
        <label for="note">Ghi chú</label><br>
        <textarea id="note" name="note" maxlength="1000" rows="3"></textarea>
    </p>
    <p>Phương thức thanh toán: <strong>Thanh toán khi nhận hàng (COD)</strong></p>
    <button type="submit">Đặt hàng</button>
    <a href="${pageContext.request.contextPath}/cart">Quay lại giỏ hàng</a>
</form>