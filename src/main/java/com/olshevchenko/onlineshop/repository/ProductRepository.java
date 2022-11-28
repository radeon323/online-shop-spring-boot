package com.olshevchenko.onlineshop.repository;

import com.olshevchenko.onlineshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oleksandr Shevchenko
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
