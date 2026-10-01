<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
    <c:when test="${not empty author}">
        <title>Cập nhật tác giả</title>
        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/author-edit" />
        <h1>Cập nhật tác giả</h1>
    </c:when>
    <c:otherwise>
        <title>Thêm tác giả mới</title>
        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/author-add" />
        <h1>Thêm tác giả mới</h1>
    </c:otherwise>
</c:choose>

<form method="post" action="${formAction}">
    <c:if test="${not empty author}">
        <input type="hidden" name="authorId" value="${author.authorId}">
    </c:if>

    <label for="name">Author Name</label>
    <input id="name" name="name" type="text" value="${author.name}" required>

    <label for="dateOfBirth">Date of Birth</label>
    <input id="dateOfBirth" name="dateOfBirth" type="date" value="${author.dateOfBirth}">

    <button type="submit">Submit</button>
    <a href="${pageContext.request.contextPath}/admin/authors">Hủy</a>
</form>