package com.olshevchenko.onlineshop.web;

import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.exception.ProductNotFoundException;
import com.olshevchenko.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products/")
public class ProductsRestController {

    private final ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    protected List<Product> showAllProducts(@RequestParam(value = "sort", required = false) String sort) {

        List<Product> products;

        if (sort != null && Objects.equals(sort, "price")) {
            products = productService.sortByPrice();
        } else if (sort != null && Objects.equals(sort, "name")) {
            products = productService.sortByName();
        } else {
            products = productService.findAll();
        }
        return products;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        Product product;
        try {
            product = productService.findById(id);
        } catch (ProductNotFoundException e) {
            log.error("Could not find product by id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<Product> editProduct(@PathVariable("id") int id, @RequestBody Product product) {
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        product.setId(id);
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<Product> deleteProduct(@PathVariable int id) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
