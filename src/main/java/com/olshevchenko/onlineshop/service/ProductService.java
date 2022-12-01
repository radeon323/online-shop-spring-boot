package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.exception.ProductNotFoundException;
import com.olshevchenko.onlineshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Could not find product by id: " + id));
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public void save(Product product) {
        product.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0));
        repository.save(product);
    }

    public List<Product> sortByPrice() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "price"));
    }

    public List<Product> sortByName() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}
