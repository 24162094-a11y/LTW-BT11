<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Đặt hàng thành công</title>

<h1>Đặt hàng thành công</h1>
<p>Mã đơn hàng: <strong><c:out value="${order.orderId}" /></strong></p>
<p>Trạng thái: <c:out value="${order.statusLabel}" /></p>
<p>Thanh toán: Thanh toán khi nhận hàng (COD)</p>
<p>Người nhận: <c:out value="${order.recipientName}" /> | <c:out value="${order.phone}" /></p>
<p>Địa chỉ: <c:out value="${order.shippingAddress}" /></p>

<div style="overflow-x:auto">
    <table style="width:100%; border-collapse:collapse">
        <thead><tr><th scope="col">Sách</th><th scope="col">Đơn giá</th>
            <th scope="col">Số lượng</th><th scope="col">Tạm tính</th></tr></thead>
        <tbody>
            <c:forEach var="item" items="${order.items}">
                <tr>
                    <td><c:out value="${item.bookTitle}" /></td>
                    <td><c:out value="${item.unitPrice}" /></td>
                    <td><c:out value="${item.quantity}" /></td>
                    <td><c:out value="${item.unitPrice * item.quantity}" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<p><strong>Tổng thanh toán: <c:out value="${order.totalAmount}" /></strong></p>
<a href="${pageContext.request.contextPath}/home">Tiếp tục mua sách</a>