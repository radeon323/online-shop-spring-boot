package com.olshevchenko.onlineshop.exception;

/**
 * @author Oleksandr Shevchenko
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
