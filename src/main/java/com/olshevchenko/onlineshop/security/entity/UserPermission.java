package com.olshevchenko.onlineshop.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@AllArgsConstructor
public enum UserPermission {
    PRODUCT_READ("products:read"),
    PRODUCT_WRITE("products:write"),
    PRODUCT_DELETE("products:delete"),
    USER_READ("users:read"),
    USER_WRITE("users:write");

    private final String permission;
}
