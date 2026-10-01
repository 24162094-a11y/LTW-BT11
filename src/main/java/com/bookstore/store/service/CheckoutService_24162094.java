package com.bookstore.store.service;

import com.bookstore.store.entity.Book_24162094;
import com.bookstore.store.entity.CustomerOrder_24162094;
import com.bookstore.store.entity.OrderItem_24162094;
import com.bookstore.store.entity.OrderStatus_24162094;
import com.bookstore.store.entity.User_24162094;
import com.bookstore.store.utils.JPAUtil_24162094;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;

public class CheckoutService_24162094 {
    private final Supplier<EntityManager> entityManagerProvider;

    public CheckoutService_24162094() {
        this(JPAUtil_24162094::getEntityManager);
    }

    public CheckoutService_24162094(Supplier<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    public Long createCodOrder(Integer userId, String recipientName, String phone,
                               String shippingAddress, String note,
                               Map<Integer, Integer> cart) {
        String normalizedName = trimToNull(recipientName);
        String normalizedPhone = trimToNull(phone);
        String normalizedAddress = trimToNull(shippingAddress);
        String normalizedNote = trimToNull(note);
        if (userId == null || normalizedName == null || normalizedPhone == null
                || normalizedAddress == null || normalizedName.length() > 150
                || normalizedPhone.length() > 20 || normalizedAddress.length() > 500
                || (normalizedNote != null && normalizedNote.length() > 1000)) {
            throw new CheckoutException(CheckoutError.INVALID_DETAILS);
        }
        if (cart == null || cart.isEmpty() || cart.size() > 50) {
            throw new CheckoutException(CheckoutError.EMPTY_CART);
        }

        Map<Integer, Integer> requestedItems = new TreeMap<>();
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            if (entry.getKey() == null || entry.getKey() < 1
                    || entry.getValue() == null || entry.getValue() < 1) {
                throw new CheckoutException(CheckoutError.INVALID_CART);
            }
            requestedItems.put(entry.getKey(), entry.getValue());
        }

        try (EntityManager entityManager = entityManagerProvider.get()) {
            entityManager.getTransaction().begin();
            try {
            User_24162094 user = entityManager.find(User_24162094.class, userId);
            if (user == null) {
                throw new CheckoutException(CheckoutError.USER_NOT_FOUND);
            }

            CustomerOrder_24162094 order = new CustomerOrder_24162094();
            order.setUserId(userId);
            order.setRecipientName(normalizedName);
            order.setPhone(normalizedPhone);
            order.setShippingAddress(normalizedAddress);
            order.setNote(normalizedNote);
            order.setPaymentMethod("COD");
            order.setStatus(OrderStatus_24162094.NEW.getCode());
            order.setCreatedAt(LocalDateTime.now());

            BigDecimal total = BigDecimal.ZERO;
            for (Map.Entry<Integer, Integer> entry : requestedItems.entrySet()) {
                Book_24162094 book = entityManager.find(Book_24162094.class, entry.getKey(),
                        LockModeType.PESSIMISTIC_WRITE);
                if (book == null) {
                    throw new CheckoutException(CheckoutError.BOOK_NOT_FOUND);
                }
                int available = Optional.ofNullable(book.getQuantity()).orElse(0);
                int quantity = entry.getValue();
                if (quantity > available) {
                    throw new CheckoutException(CheckoutError.INSUFFICIENT_STOCK);
                }
                if (book.getPrice() == null || book.getPrice().signum() < 0) {
                    throw new CheckoutException(CheckoutError.INVALID_PRICE);
                }

                OrderItem_24162094 item = new OrderItem_24162094();
                item.setCustomerOrder(order);
                item.setBookId(book.getBookId());
                item.setBookTitle(book.getTitle());
                item.setUnitPrice(book.getPrice());
                item.setQuantity(quantity);
                order.getItems().add(item);

                total = total.add(book.getPrice().multiply(BigDecimal.valueOf(quantity)));
                book.setQuantity(available - quantity);
            }
            order.setTotalAmount(total);
            entityManager.persist(order);
            entityManager.flush();
            Long orderId = order.getOrderId();
            entityManager.getTransaction().commit();
            return orderId;
            } catch (RuntimeException exception) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw exception;
            }
        }
    }

    public CustomerOrder_24162094 findOrderForUser(Long orderId, Integer userId) {
        if (orderId == null || userId == null) {
            return null;
        }
        try (EntityManager entityManager = entityManagerProvider.get()) {
            return entityManager.createQuery(
                            "select distinct o from CustomerOrder_24162094 o "
                                    + "left join fetch o.items "
                                    + "where o.orderId = :orderId and o.userId = :userId",
                            CustomerOrder_24162094.class)
                    .setParameter("orderId", orderId)
                    .setParameter("userId", userId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }

    public List<CustomerOrder_24162094> findOrdersForUser(Integer userId,
                                                          OrderStatus_24162094 status) {
        if (userId == null) {
            return List.of();
        }
        try (EntityManager entityManager = entityManagerProvider.get()) {
            String query = "select distinct o from CustomerOrder_24162094 o "
                    + "left join fetch o.items where o.userId = :userId";
            if (status == OrderStatus_24162094.NEW) {
                query += " and o.status in :statuses";
            } else if (status != null) {
                query += " and o.status = :status";
            }
            query += " order by o.createdAt desc, o.orderId desc";

            var typedQuery = entityManager.createQuery(query, CustomerOrder_24162094.class)
                    .setParameter("userId", userId);
            if (status == OrderStatus_24162094.NEW) {
                typedQuery.setParameter("statuses", List.of(
                        OrderStatus_24162094.NEW.getCode(), "PENDING"));
            } else if (status != null) {
                typedQuery.setParameter("status", status.getCode());
            }
            return typedQuery.getResultList();
        }
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    public enum CheckoutError {
        INVALID_DETAILS,
        EMPTY_CART,
        INVALID_CART,
        USER_NOT_FOUND,
        BOOK_NOT_FOUND,
        INSUFFICIENT_STOCK,
        INVALID_PRICE
    }

    public static class CheckoutException extends RuntimeException {
        private final CheckoutError error;

        public CheckoutException(CheckoutError error) {
            super(error.name());
            this.error = error;
        }

        public CheckoutError getError() {
            return error;
        }
    }
}