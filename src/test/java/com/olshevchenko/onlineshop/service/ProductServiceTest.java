package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepositoryMock;

    private ProductService productService;

    @BeforeEach
    void init() {
        productService = new ProductService(productRepositoryMock);
    }

    @Test
    void testFindAll() {
        List<Product> products = new ArrayList<>();
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productSamsung);
        Product productXiaomi = Product.builder()
                .id(2)
                .name("Xiaomi Redmi Note 9 Pro")
                .description("6.67 inches, Qualcomm SM7125 Snapdragon 720G Octa-core")
                .price(11699.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productXiaomi);
        Product productApple = Product.builder()
                .id(3)
                .name("Apple iPhone 14")
                .description("6.1 inches, Apple A15 Bionic")
                .price(41499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productApple);

        when(productRepositoryMock.findAll()).thenReturn(products);
        List<Product> actualProducts = productService.findAll();
        assertNotNull(actualProducts);
        assertEquals(3, actualProducts.size());
        assertEquals(productSamsung, actualProducts.get(0));
        assertEquals(productXiaomi, actualProducts.get(1));
        assertEquals(productApple, actualProducts.get(2));
        verify(productRepositoryMock, times(1)).findAll();
    }

    @Test
    void testFindById() {
        List<Product> products = new ArrayList<>();
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productSamsung);
        Product productXiaomi = Product.builder()
                .id(2)
                .name("Xiaomi Redmi Note 9 Pro")
                .description("6.67 inches, Qualcomm SM7125 Snapdragon 720G Octa-core")
                .price(11699.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        products.add(productXiaomi);

        when(productRepositoryMock.findById(1)).thenReturn(Optional.ofNullable(products.get(0)));
        Product actualProduct = productService.findById(1);
        assertEquals(productSamsung, actualProduct);
        verify(productRepositoryMock, times(1)).findById(1);
    }

    @Test
    void testAdd() {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        when(productRepositoryMock.save(productSamsung)).thenReturn(productSamsung);
        productRepositoryMock.save(productSamsung);
        verify(productRepositoryMock, times(1)).save(productSamsung);
    }

    @Test
    void testRemove() {
        doNothing().when(productRepositoryMock).deleteById(isA(Integer.class));
        productRepositoryMock.deleteById(1);
        verify(productRepositoryMock, times(1)).deleteById(1);
    }

    @Test
    void testSortByPrice() {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        Product productXiaomi = Product.builder()
                .id(2)
                .name("Xiaomi Redmi Note 9 Pro")
                .description("6.67 inches, Qualcomm SM7125 Snapdragon 720G Octa-core")
                .price(11699.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        Product productApple = Product.builder()
                .id(3)
                .name("Apple iPhone 14")
                .description("6.1 inches, Apple A15 Bionic")
                .price(41499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        List<Product> productsList = List.of(productXiaomi, productSamsung, productApple);

        when(productRepositoryMock.findAll(Sort.by(Sort.Direction.ASC, "price"))).thenReturn(productsList);
        List<Product> actualProducts = productService.sortByPrice();
        assertNotNull(actualProducts);
        assertEquals(3, actualProducts.size());
        assertEquals(productSamsung, actualProducts.get(1));
        assertEquals(productXiaomi, actualProducts.get(0));
        assertEquals(productApple, actualProducts.get(2));
        verify(productRepositoryMock, times(1)).findAll(Sort.by(Sort.Direction.ASC, "price"));
    }

    @Test
    void testSortByName() {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        Product productXiaomi = Product.builder()
                .id(2)
                .name("Xiaomi Redmi Note 9 Pro")
                .description("6.67 inches, Qualcomm SM7125 Snapdragon 720G Octa-core")
                .price(11699.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        Product productApple = Product.builder()
                .id(3)
                .name("Apple iPhone 14")
                .description("6.1 inches, Apple A15 Bionic")
                .price(41499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        List<Product> productsList = List.of(productApple, productSamsung, productXiaomi);

        when(productRepositoryMock.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(productsList);
        List<Product> actualProducts = productService.sortByName();
        assertNotNull(actualProducts);
        assertEquals(3, actualProducts.size());
        assertEquals(productSamsung, actualProducts.get(1));
        assertEquals(productXiaomi, actualProducts.get(2));
        assertEquals(productApple, actualProducts.get(0));
        verify(productRepositoryMock, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"));
    }


}