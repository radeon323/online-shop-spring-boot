package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.repository.ProductRepository;
import com.olshevchenko.onlineshop.entity.Product;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository jdbcProductRepository;

    public List<Product> findAll() {
        return jdbcProductRepository.findAll();
    }

    public Optional<Product> findById(int id) {
        return jdbcProductRepository.findById(id);
    }

    public void add(Product product) {
        product.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0));
        jdbcProductRepository.add(product);
    }

    public void remove(int id) {
        jdbcProductRepository.remove(id);
    }

    public void edit(Product product) {
        jdbcProductRepository.update(product);
    }
}
