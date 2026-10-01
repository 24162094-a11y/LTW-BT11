package com.bookstore.store.entity;

import java.math.BigDecimal;

public class CartItem_24162094 {
    private final Book_24162094 book;
    private final int quantity;

    public CartItem_24162094(Book_24162094 book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book_24162094 getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return book.getPrice() == null ? BigDecimal.ZERO
                : book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}