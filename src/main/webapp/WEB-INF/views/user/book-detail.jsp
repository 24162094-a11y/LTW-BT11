<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title><c:out value="${book.title}" /></title>

<section class="book-detail">
    <div class="book-detail-cover">
        <c:choose>
            <c:when test="${not empty book.coverImage}">
                <img src="${pageContext.request.contextPath}/uploads/${book.coverImage}"
                     alt="Bìa sách ${book.title}"
                     onerror="this.style.display='none'; this.nextElementSibling.style.display='grid';">
                 <div class="book-cover-placeholder image-fallback">Không có ảnh bìa</div>
            </c:when>
            <c:otherwise>
                <div class="book-cover-placeholder">Không có ảnh bìa</div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="book-detail-information">
        <h1><c:out value="${book.title}" /></h1>
        <p>Mã ISBN: <c:out value="${book.isbn}" /></p>
        <p>Tác giả:
            <c:forEach var="author" items="${book.authors}" varStatus="status">
                <c:if test="${status.index > 0}">, </c:if>
                <c:out value="${author.name}" />
            </c:forEach>
        </p>
        <p>Publisher: <c:out value="${book.publisher}" /></p>
        <p>Publisher date: <c:out value="${book.publishDate}" /></p>
        <p>Giá: <c:out value="${book.price}" /></p>
        <p>Còn lại: <c:out value="${book.quantity}" /></p>
        <c:choose>
            <c:when test="${book.quantity > 0}">
                <form method="post" action="${pageContext.request.contextPath}/cart/add">
                    <input type="hidden" name="bookId" value="${book.bookId}">
                    <label for="cartQuantity">Số lượng</label>
                    <input id="cartQuantity" type="number" name="quantity" value="1"
                           min="1" max="${book.quantity}" required>
                    <button type="submit">Thêm vào giỏ</button>
                </form>
            </c:when>
            <c:otherwise>
                <p>Đã hết hàng.</p>
            </c:otherwise>
        </c:choose>
        <c:if test="${param.limit != null}"><p role="alert">Số lượng yêu cầu vượt quá tồn kho.</p></c:if>
        <c:if test="${param.unavailable != null}"><p role="alert">Sách hiện không còn hàng.</p></c:if>
        <p>Reviews (<c:out value="${ratings.size()}" />)</p>
    </div>
</section>

<section class="book-reviews">
    <h2>Reviews</h2>
    <c:choose>
        <c:when test="${empty ratings}">
            <p>Chưa có đánh giá</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="rating" items="${ratings}">
                <p><strong><c:out value="${rating.user.fullName}" />:</strong>
                    <c:out value="${rating.reviewText}" /></p>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</section>

<section class="add-review">
    <h2>Thêm đánh giá</h2>
    <c:choose>
        <c:when test="${sessionScope.user == null}">
            <p>Vui lòng đăng nhập để đánh giá.
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
            </p>
        </c:when>
        <c:otherwise>
            <form method="post" action="${pageContext.request.contextPath}/submit-review">
                <input type="hidden" name="bookid" value="${book.bookId}">
                <label for="rating">Số sao</label>
                <select id="rating" name="rating" required>
                    <option value="5">5</option>
                    <option value="4">4</option>
                    <option value="3">3</option>
                    <option value="2">2</option>
                    <option value="1">1</option>
                </select>
                <label for="review_text">Nội dung đánh giá</label>
                <textarea id="review_text" name="review_text" rows="5" required></textarea>
                <button type="submit">Submit</button>
            </form>
        </c:otherwise>
    </c:choose>
</section>