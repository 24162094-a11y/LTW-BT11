<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Giỏ hàng</title>

<h1>Giỏ hàng</h1>
<c:if test="${param.added != null}"><p role="status">Đã thêm sách vào giỏ.</p></c:if>
<c:if test="${param.updated != null}"><p role="status">Đã cập nhật số lượng.</p></c:if>
<c:if test="${param.removed != null}"><p role="status">Đã xóa sách khỏi giỏ.</p></c:if>
<c:if test="${param.limit != null}"><p role="alert">Số lượng vượt quá tồn kho hiện tại.</p></c:if>
<c:if test="${param.unavailable != null}"><p role="alert">Sách đã hết hàng.</p></c:if>
<c:if test="${param.invalid != null}"><p role="alert">Thông tin thao tác không hợp lệ.</p></c:if>
<c:if test="${param['empty'] != null}"><p role="status">Giỏ hàng đang trống.</p></c:if>

<c:choose>
    <c:when test="${empty cartItems}">
        <p>Chưa có sách trong giỏ.</p>
        <a href="${pageContext.request.contextPath}/home">Tiếp tục xem sách</a>
    </c:when>
    <c:otherwise>
        <div style="overflow-x:auto">
            <table style="width:100%; border-collapse:collapse">
                <thead>
                    <tr><th scope="col">Sách</th><th scope="col">Đơn giá</th>
                        <th scope="col">Số lượng</th><th scope="col">Tạm tính</th>
                        <th scope="col">Thao tác</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="line" items="${cartItems}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/book-detail?id=${line.book.bookId}">
                                <c:out value="${line.book.title}" /></a></td>
                            <td><c:out value="${line.book.price}" /></td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/cart/update">
                                    <input type="hidden" name="bookId" value="${line.book.bookId}">
                                    <label for="quantity-${line.book.bookId}">Số lượng</label>
                                    <input id="quantity-${line.book.bookId}" type="number" name="quantity"
                                           value="${line.quantity}" min="1" max="${line.book.quantity}" required>
                                    <button type="submit">Cập nhật</button>
                                </form>
                            </td>
                            <td><c:out value="${line.subtotal}" /></td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/cart/remove">
                                    <input type="hidden" name="bookId" value="${line.book.bookId}">
                                    <button type="submit">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <p><strong>Tổng cộng: <c:out value="${cartTotal}" /></strong></p>
        <c:choose>
            <c:when test="${sessionScope.user == null}">
                <a href="${pageContext.request.contextPath}/login">Đăng nhập để thanh toán</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/checkout">Tiến hành thanh toán</a>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>