package com.bookstore.store.service;

import com.bookstore.store.entity.Book_24162094;

import java.util.Map;

public class CartService_24162094 {
    public CartError add(Map<Integer, Integer> cart, Book_24162094 book, int quantity) {
        CartError validation = validate(book, quantity);
        if (validation != CartError.SUCCESS) {
            return validation;
        }
        long requested = (long) Math.max(0, cart.getOrDefault(book.getBookId(), 0)) + quantity;
        if (requested > book.getQuantity()) {
            return CartError.LIMIT;
        }
        cart.put(book.getBookId(), (int) requested);
        return CartError.SUCCESS;
    }

    public CartError update(Map<Integer, Integer> cart, Book_24162094 book, int quantity) {
        CartError validation = validate(book, quantity);
        if (validation != CartError.SUCCESS) {
            return validation;
        }
        if (quantity > book.getQuantity()) {
            return CartError.LIMIT;
        }
        cart.put(book.getBookId(), quantity);
        return CartError.SUCCESS;
    }

    public void remove(Map<Integer, Integer> cart, Integer bookId) {
        if (bookId != null) {
            cart.remove(bookId);
        }
    }

    private CartError validate(Book_24162094 book, int quantity) {
        if (book == null || book.getBookId() == null || quantity < 1) {
            return CartError.INVALID;
        }
        if (book.getQuantity() == null || book.getQuantity() < 1) {
            return CartError.UNAVAILABLE;
        }
        return CartError.SUCCESS;
    }

    public enum CartError {
        SUCCESS,
        INVALID,
        UNAVAILABLE,
        LIMIT
    }
}