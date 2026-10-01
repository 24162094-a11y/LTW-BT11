package com.bookstore.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.MapsId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "rating")
public class Rating_24162094 {
    @EmbeddedId
    private RatingId_24162094 id;

    @Column(name = "rating", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private Integer rating;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(@JoinColumn(name = "userid", nullable = false))
    private User_24162094 user;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(@JoinColumn(name = "bookid", nullable = false))
    private Book_24162094 book;

    public RatingId_24162094 getId() {
        return id;
    }

    public void setId(RatingId_24162094 id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public User_24162094 getUser() {
        return user;
    }

    public void setUser(User_24162094 user) {
        this.user = user;
    }

    public Book_24162094 getBook() {
        return book;
    }

    public void setBook(Book_24162094 book) {
        this.book = book;
    }
}