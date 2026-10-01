package com.bookstore.store.entity;

public enum OrderStatus_24162094 {
    NEW("NEW", "Đơn hàng mới"),
    CONFIRMED("CONFIRMED", "Đã xác nhận"),
    PREPARING("PREPARING", "Chuẩn bị hàng"),
    SHIPPING("SHIPPING", "Vận chuyển"),
    OUT_FOR_DELIVERY("OUT_FOR_DELIVERY", "Đang giao hàng"),
    DELIVERED("DELIVERED", "Đã giao"),
    CANCELLED("CANCELLED", "Đơn hàng hủy"),
    RETURNED("RETURNED", "Đơn hàng hoàn");

    private final String code;
    private final String label;

    OrderStatus_24162094(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static OrderStatus_24162094 fromCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        for (OrderStatus_24162094 status : values()) {
            if (status.code.equalsIgnoreCase(code.trim())) {
                return status;
            }
        }
        return null;
    }
}