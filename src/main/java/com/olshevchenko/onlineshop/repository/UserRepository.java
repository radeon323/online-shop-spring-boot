package com.olshevchenko.onlineshop.repository;

import com.olshevchenko.onlineshop.entity.User;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public interface UserRepository {
    Optional<User> findById(int id);
    Optional<User> findByEmail (String email);
    void add(User user);
    void remove(int id);
    void update(User user);
}
