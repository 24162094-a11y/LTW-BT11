<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Lịch sử đặt hàng</title>

<h1>Lịch sử đặt hàng</h1>
<form method="get" action="${pageContext.request.contextPath}/orders">
    <label for="status">Lọc theo trạng thái</label>
    <select id="status" name="status">
        <option value="" ${empty selectedStatus ? 'selected' : ''}>Tất cả trạng thái</option>
        <c:forEach var="statusOption" items="${orderStatuses}">
            <option value="${statusOption.code}"
                    ${selectedStatus == statusOption.code ? 'selected' : ''}>
                <c:out value="${statusOption.label}" />
            </option>
        </c:forEach>
    </select>
    <button type="submit">Lọc</button>
</form>

<c:choose>
    <c:when test="${empty orders}">
        <p>Không có đơn hàng phù hợp.</p>
    </c:when>
    <c:otherwise>
        <div style="overflow-x:auto">
            <table style="width:100%; border-collapse:collapse">
                <thead>
                    <tr><th scope="col">Mã đơn</th><th scope="col">Ngày đặt</th>
                        <th scope="col">Trạng thái</th><th scope="col">Thanh toán</th>
                        <th scope="col">Tổng tiền</th><th scope="col">Chi tiết</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td><c:out value="${order.orderId}" /></td>
                            <td><c:out value="${order.createdAt}" /></td>
                            <td><c:out value="${order.statusLabel}" /></td>
                            <td><c:out value="${order.paymentMethod}" /></td>
                            <td><c:out value="${order.totalAmount}" /></td>
                            <td><a href="${pageContext.request.contextPath}/order-success?id=${order.orderId}">Xem đơn</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>