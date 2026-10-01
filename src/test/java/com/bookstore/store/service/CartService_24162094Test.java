package com.bookstore.store.service;

import com.bookstore.store.entity.Book_24162094;
import com.bookstore.store.service.CartService_24162094.CartError;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CartService_24162094Test {
    private final CartService_24162094 cartService = new CartService_24162094();

    @Test
    void addsAndUpdatesOnlyWithinCurrentStock() {
        Book_24162094 book = book(12, 3);
        Map<Integer, Integer> cart = new HashMap<>();

        assertEquals(CartError.SUCCESS, cartService.add(cart, book, 2));
        assertEquals(CartError.LIMIT, cartService.add(cart, book, 2));
        assertEquals(2, cart.get(12));
        assertEquals(CartError.LIMIT, cartService.update(cart, book, 4));
        assertEquals(2, cart.get(12));
        assertEquals(CartError.SUCCESS, cartService.update(cart, book, 3));
        assertEquals(3, cart.get(12));
    }

    @Test
    void rejectsInvalidAndUnavailableItemsAndRemovesLine() {
        Map<Integer, Integer> cart = new HashMap<>();
        Book_24162094 unavailableBook = book(18, 0);

        assertEquals(CartError.INVALID, cartService.add(cart, unavailableBook, 0));
        assertEquals(CartError.UNAVAILABLE, cartService.add(cart, unavailableBook, 1));
        assertFalse(cart.containsKey(18));

        cart.put(18, 1);
        cartService.remove(cart, 18);
        assertFalse(cart.containsKey(18));
    }

    private Book_24162094 book(Integer id, Integer quantity) {
        Book_24162094 book = new Book_24162094();
        book.setBookId(id);
        book.setQuantity(quantity);
        return book;
    }
}