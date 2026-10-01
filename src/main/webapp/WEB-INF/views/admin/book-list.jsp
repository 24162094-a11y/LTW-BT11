<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Quản lý Sách</title>

<section>
    <h1>Quản lý Sách</h1>
    <p>
        <a href="${pageContext.request.contextPath}/admin/book-add">Thêm sách mới</a>
    </p>

    <table>
        <thead>
        <tr>
            <th>STT</th>
            <th>BookID</th>
            <th>Title</th>
            <th>ISBN</th>
            <th>Publisher</th>
            <th>Publish Date</th>
            <th>Quantity</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty books}">
                <tr>
                    <td colspan="8">Chưa có sách.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="book" items="${books}" varStatus="status">
                    <tr>
                        <td>${(currentPage - 1) * 6 + status.index + 1}</td>
                        <td><c:out value="${book.bookId}" /></td>
                        <td><c:out value="${book.title}" /></td>
                        <td><c:out value="${book.isbn}" /></td>
                        <td><c:out value="${book.publisher}" /></td>
                        <td><c:out value="${book.publishDate}" /></td>
                        <td><c:out value="${book.quantity}" /></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/book-edit?id=${book.bookId}">Sửa</a>
                            <a href="${pageContext.request.contextPath}/admin/book-delete?id=${book.bookId}"
                               onclick="return confirm('Bạn chắc chắn muốn xóa?')">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</section>

<nav aria-label="Phân trang sách">
    <c:if test="${currentPage > 1}">
        <a href="${pageContext.request.contextPath}/admin/books?page=${currentPage - 1}">Previous</a>
    </c:if>
    <c:forEach var="pageNumber" begin="1" end="${totalPages}">
        <c:choose>
            <c:when test="${pageNumber == currentPage}">
                <strong>${pageNumber}</strong>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/admin/books?page=${pageNumber}">${pageNumber}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${currentPage < totalPages}">
        <a href="${pageContext.request.contextPath}/admin/books?page=${currentPage + 1}">Next</a>
    </c:if>
</nav>