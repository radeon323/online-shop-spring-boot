package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.repository.ProductRepository;
import com.olshevchenko.onlineshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        when(productRepositoryMock.findAll()).thenReturn(products);
        List<Product> actualProducts = productService.findAll();
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(productSamsung, actualProducts.get(0));
        assertEquals(productXiaomi, actualProducts.get(1));
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
        Optional<Product> actualProduct = productService.findById(1);
        assertTrue(actualProduct.isPresent());
        assertEquals(productSamsung, actualProduct.get());
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
        doNothing().when(productRepositoryMock).add(isA(Product.class));
        productRepositoryMock.add(productSamsung);
        verify(productRepositoryMock, times(1)).add(productSamsung);
    }

    @Test
    void testRemove() {
        doNothing().when(productRepositoryMock).remove(isA(Integer.class));
        productRepositoryMock.remove(1);
        verify(productRepositoryMock, times(1)).remove(1);
    }

    @Test
    void testUpdate() {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        doNothing().when(productRepositoryMock).update(isA(Product.class));
        productRepositoryMock.update(productSamsung);
        verify(productRepositoryMock, times(1)).update(productSamsung);
    }


}