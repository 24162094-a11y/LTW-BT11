<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Trang chủ</title>

<section>
    <h1>Danh sách sách</h1>
    <c:choose>
        <c:when test="${empty books}">
            <p>Chưa có sách trong cửa hàng.</p>
        </c:when>
        <c:otherwise>
            <div class="book-grid">
                <c:forEach var="book" items="${books}">
                    <article class="book-card">
                        <c:choose>
                            <c:when test="${not empty book.coverImage}">
                                <img src="${pageContext.request.contextPath}/uploads/${book.coverImage}"
                                      alt="Bìa sách ${book.title}"
                                      onerror="this.style.display='none'; this.nextElementSibling.style.display='grid';">
                                  <div class="book-cover-placeholder image-fallback">Không có ảnh bìa</div>
                            </c:when>
                            <c:otherwise>
                                <div class="book-cover-placeholder">Không có ảnh</div>
                            </c:otherwise>
                        </c:choose>
                        <h2>
                            <a href="${pageContext.request.contextPath}/book-detail?id=${book.bookId}">
                                <c:out value="${book.title}" />
                            </a>
                        </h2>
                        <p>Mã ISBN: <c:out value="${book.isbn}" /></p>
                        <p>Tác giả:
                            <c:forEach var="author" items="${book.authors}" varStatus="status">
                                <c:if test="${status.index > 0}">, </c:if>
                                <c:out value="${author.name}" />
                            </c:forEach>
                        </p>
                        <p>Publisher: <c:out value="${book.publisher}" /></p>
                        <p>Publisher date: <c:out value="${book.publishDate}" /></p>
                        <p>Quantity: <c:out value="${book.quantity}" /></p>
                        <p>Review (10): <c:out value="${book.reviewCount}" /></p>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<nav aria-label="Phân trang">
    <c:if test="${currentPage > 1}">
        <a href="${pageContext.request.contextPath}/home?page=${currentPage - 1}">Previous</a>
    </c:if>
    <c:forEach var="pageNumber" begin="1" end="${totalPages}">
        <c:choose>
            <c:when test="${pageNumber == currentPage}">
                <strong>${pageNumber}</strong>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/home?page=${pageNumber}">${pageNumber}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${currentPage < totalPages}">
        <a href="${pageContext.request.contextPath}/home?page=${currentPage + 1}">Next</a>
    </c:if>
</nav>