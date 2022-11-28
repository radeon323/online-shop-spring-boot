package com.olshevchenko.onlineshop.exception;

/**
 * @author Oleksandr Shevchenko
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
