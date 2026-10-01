package com.bookstore.store.service;

import com.bookstore.store.entity.Book_24162094;
import com.bookstore.store.entity.CustomerOrder_24162094;
import com.bookstore.store.entity.OrderStatus_24162094;
import com.bookstore.store.entity.User_24162094;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutService_24162094Test {
    private static EntityManagerFactory entityManagerFactory;
    private static CheckoutService_24162094 checkoutService;
    private Integer userId;
    private Integer secondBookId;
    private Integer firstBookId;

    @BeforeAll
    static void createPersistenceUnit() {
        entityManagerFactory = Persistence.createEntityManagerFactory("BookStoreTestPU");
        checkoutService = new CheckoutService_24162094(entityManagerFactory::createEntityManager);
    }

    @AfterAll
    static void closePersistenceUnit() {
        entityManagerFactory.close();
    }

    @BeforeEach
    void seedDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("delete from OrderItem_24162094").executeUpdate();
            entityManager.createQuery("delete from CustomerOrder_24162094").executeUpdate();
            entityManager.createQuery("delete from Rating_24162094").executeUpdate();
            entityManager.createQuery("delete from Book_24162094").executeUpdate();
            entityManager.createQuery("delete from User_24162094").executeUpdate();

            User_24162094 user = new User_24162094();
            user.setUsername("buyer@example.com");
            user.setFullName("Buyer");
            user.setEmail("buyer@example.com");
            user.setPassword("test");
            entityManager.persist(user);
            userId = user.getUserId();

            firstBookId = persistBook(entityManager, "First", "12.50", 5);
            secondBookId = persistBook(entityManager, "Second", "20.00", 1);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Test
    void createsCodOrderAndDecrementsStock() {
        Long orderId = checkoutService.createCodOrder(userId, "Buyer Name", "0900000000",
                "1 Book Street", "Leave at door", Map.of(firstBookId, 2));

        CustomerOrder_24162094 order = checkoutService.findOrderForUser(orderId, userId);
        assertNotNull(order);
        assertEquals("COD", order.getPaymentMethod());
        assertEquals(OrderStatus_24162094.NEW.getCode(), order.getStatus());
        assertEquals(new BigDecimal("25.00"), order.getTotalAmount());
        assertEquals(1, order.getItems().size());
        assertEquals("First", order.getItems().get(0).getBookTitle());
        assertEquals(3, bookQuantity(firstBookId));
    }

    @Test
    void rejectsOverLimitWithoutCreatingOrderOrChangingStock() {
        CheckoutService_24162094.CheckoutException exception = assertThrows(
                CheckoutService_24162094.CheckoutException.class,
                () -> checkoutService.createCodOrder(userId, "Buyer", "0900000000",
                        "1 Book Street", null, Map.of(secondBookId, 2)));

        assertEquals(CheckoutService_24162094.CheckoutError.INSUFFICIENT_STOCK,
                exception.getError());
        assertEquals(1, bookQuantity(secondBookId));
        assertEquals(0, orderCount());
    }

    @Test
    void rollsBackEarlierStockUpdatesWhenLaterItemIsUnavailable() {
        Map<Integer, Integer> cart = new LinkedHashMap<>();
        cart.put(firstBookId, 2);
        cart.put(secondBookId, 2);

        assertThrows(CheckoutService_24162094.CheckoutException.class,
                () -> checkoutService.createCodOrder(userId, "Buyer", "0900000000",
                        "1 Book Street", null, cart));

        assertEquals(5, bookQuantity(firstBookId));
        assertEquals(1, bookQuantity(secondBookId));
        assertEquals(0, orderCount());
    }

    @Test
    void onlyOrderOwnerCanLoadOrderConfirmation() {
        Long orderId = checkoutService.createCodOrder(userId, "Buyer", "0900000000",
                "1 Book Street", null, Map.of(firstBookId, 1));
        Integer otherUserId = createAnotherUser();

        assertNull(checkoutService.findOrderForUser(orderId, otherUserId));
        assertNotNull(checkoutService.findOrderForUser(orderId, userId));
    }

    @Test
    void filtersEveryStatusForCurrentUserAndIncludesLegacyPendingAsNew() {
        for (OrderStatus_24162094 status : OrderStatus_24162094.values()) {
            persistOrder(userId, status.getCode());
        }
        persistOrder(userId, "PENDING");
        persistOrder(createAnotherUser(), OrderStatus_24162094.CONFIRMED.getCode());

        for (OrderStatus_24162094 status : OrderStatus_24162094.values()) {
            var orders = checkoutService.findOrdersForUser(userId, status);
            assertEquals(status == OrderStatus_24162094.NEW ? 2 : 1, orders.size());
            assertEquals(userId, orders.get(0).getUserId());
            assertEquals(status.getLabel(), orders.get(0).getStatusLabel());
        }

        assertEquals(9, checkoutService.findOrdersForUser(userId, null).size());
        assertEquals(OrderStatus_24162094.NEW.getLabel(),
                orderWithStatus("PENDING").getStatusLabel());
    }

    private Integer persistBook(EntityManager entityManager, String title,
                                String price, int quantity) {
        Book_24162094 book = new Book_24162094();
        book.setTitle(title);
        book.setPrice(new BigDecimal(price));
        book.setQuantity(quantity);
        entityManager.persist(book);
        return book.getBookId();
    }

    private int bookQuantity(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(Book_24162094.class, id).getQuantity();
        } finally {
            entityManager.close();
        }
    }

    private long orderCount() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("select count(o) from CustomerOrder_24162094 o",
                    Long.class).getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    private Integer createAnotherUser() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            User_24162094 user = new User_24162094();
            user.setUsername("other@example.com");
            user.setFullName("Other");
            user.setEmail("other@example.com");
            user.setPassword("test");
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return user.getUserId();
        } finally {
            entityManager.close();
        }
    }

    private void persistOrder(Integer ownerId, String status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            CustomerOrder_24162094 order = new CustomerOrder_24162094();
            order.setUserId(ownerId);
            order.setRecipientName("Buyer");
            order.setPhone("0900000000");
            order.setShippingAddress("1 Book Street");
            order.setTotalAmount(BigDecimal.TEN);
            order.setPaymentMethod("COD");
            order.setStatus(status);
            order.setCreatedAt(LocalDateTime.now());
            entityManager.persist(order);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    private CustomerOrder_24162094 orderWithStatus(String status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery(
                            "select o from CustomerOrder_24162094 o "
                                    + "where o.userId = :userId and o.status = :status",
                            CustomerOrder_24162094.class)
                    .setParameter("userId", userId)
                    .setParameter("status", status)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }
}