package com.olshevchenko.onlineshop.repository;

import com.olshevchenko.onlineshop.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(int id);
    void add(Product product);
    void remove(int id);
    void update(Product product);
}