<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
    <c:when test="${not empty book}">
        <title>Cập nhật sách</title>
        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/book-edit" />
        <h1>Cập nhật sách</h1>
    </c:when>
    <c:otherwise>
        <title>Thêm sách mới</title>
        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/book-add" />
        <h1>Thêm sách mới</h1>
    </c:otherwise>
</c:choose>

<form method="post" action="${formAction}">
    <c:if test="${not empty book}">
        <input type="hidden" name="bookId" value="${book.bookId}">
    </c:if>

    <label for="title">Title</label>
    <input id="title" name="title" type="text" value="${book.title}" required>

    <label for="isbn">ISBN</label>
    <input id="isbn" name="isbn" type="number" value="${book.isbn}" required>

    <label for="publisher">Publisher</label>
    <input id="publisher" name="publisher" type="text" value="${book.publisher}">

    <label for="price">Price</label>
    <input id="price" name="price" type="number" step="0.01" value="${book.price}">

    <label for="description">Description</label>
    <textarea id="description" name="description" rows="5">${book.description}</textarea>

    <label for="publishDate">Publish Date</label>
    <input id="publishDate" name="publishDate" type="date" value="${book.publishDate}">

    <label for="coverImage">Cover Image</label>
    <input id="coverImage" name="coverImage" type="text" value="${book.coverImage}">

    <label for="quantity">Quantity</label>
    <input id="quantity" name="quantity" type="number" value="${book.quantity}">

    <label for="authorIds">Authors</label>
    <select id="authorIds" name="authorIds" multiple size="5">
        <c:forEach var="author" items="${authors}">
            <c:set var="isSelected" value="false" />
            <c:forEach var="selectedAuthor" items="${book.authors}">
                <c:if test="${selectedAuthor.authorId == author.authorId}">
                    <c:set var="isSelected" value="true" />
                </c:if>
            </c:forEach>
            <c:choose>
                <c:when test="${isSelected}">
                    <option value="${author.authorId}" selected>
                        <c:out value="${author.name}" />
                    </option>
                </c:when>
                <c:otherwise>
                    <option value="${author.authorId}">
                        <c:out value="${author.name}" />
                    </option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>

    <button type="submit">Submit</button>
    <a href="${pageContext.request.contextPath}/admin/books">Hủy</a>
</form>