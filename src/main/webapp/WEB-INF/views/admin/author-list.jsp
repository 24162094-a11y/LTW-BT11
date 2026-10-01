<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Quản lý Tác giả</title>

<section>
    <h1>Quản lý Tác giả</h1>
    <p>
        <a href="${pageContext.request.contextPath}/admin/author-add">Thêm tác giả mới</a>
    </p>

    <table>
        <thead>
        <tr>
            <th>STT</th>
            <th>AuthorID</th>
            <th>Author Name</th>
            <th>Date of Birth</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty authors}">
                <tr>
                    <td colspan="5">Chưa có tác giả.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="author" items="${authors}" varStatus="status">
                    <tr>
                        <td>${(currentPage - 1) * 6 + status.index + 1}</td>
                        <td><c:out value="${author.authorId}" /></td>
                        <td><c:out value="${author.name}" /></td>
                        <td><c:out value="${author.dateOfBirth}" /></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/author-edit?id=${author.authorId}">Sửa</a>
                            <a href="${pageContext.request.contextPath}/admin/author-delete?id=${author.authorId}"
                               onclick="return confirm('Bạn chắc chắn muốn xóa?')">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</section>

<nav aria-label="Phân trang tác giả">
    <c:if test="${currentPage > 1}">
        <a href="${pageContext.request.contextPath}/admin/authors?page=${currentPage - 1}">Previous</a>
    </c:if>
    <c:forEach var="pageNumber" begin="1" end="${totalPages}">
        <c:choose>
            <c:when test="${pageNumber == currentPage}">
                <strong>${pageNumber}</strong>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/admin/authors?page=${pageNumber}">${pageNumber}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${currentPage < totalPages}">
        <a href="${pageContext.request.contextPath}/admin/authors?page=${currentPage + 1}">Next</a>
    </c:if>
</nav>