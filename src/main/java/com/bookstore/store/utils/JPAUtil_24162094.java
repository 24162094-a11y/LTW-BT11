package com.bookstore.store.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public final class JPAUtil_24162094 {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            createEntityManagerFactory();

    private JPAUtil_24162094() {
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static EntityManager createEntityManager() {
        return getEntityManager();
    }

    public static void close() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        Map<String, Object> properties = new HashMap<>();
        addOverride(properties, "jakarta.persistence.jdbc.url", "BOOKSTORE_DB_URL");
        addOverride(properties, "jakarta.persistence.jdbc.user", "BOOKSTORE_DB_USER");
        addOverride(properties, "jakarta.persistence.jdbc.password", "BOOKSTORE_DB_PASSWORD");
        return Persistence.createEntityManagerFactory("BookStorePU", properties);
    }

    private static void addOverride(Map<String, Object> properties,
                                    String persistenceProperty, String settingName) {
        String value = System.getProperty(settingName);
        if (value == null || value.isBlank()) {
            value = System.getenv(settingName);
        }
        if (value != null) {
            properties.put(persistenceProperty, value);
        }
    }
}
