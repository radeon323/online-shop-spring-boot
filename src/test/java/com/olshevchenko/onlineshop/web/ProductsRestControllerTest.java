package com.olshevchenko.onlineshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.exception.ProductNotFoundException;
import com.olshevchenko.onlineshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebAuxTestConfig.class
)
@AutoConfigureMockMvc
class ProductsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @WithUserDetails("admin")
    void testFetchListOfProducts() throws Exception {
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

        List<Product> productsList = List.of(productSamsung, productXiaomi, productApple);

        when(productService.findAll()).thenReturn(productsList);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/products/")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Samsung Galaxy M52"))
                .andExpect(jsonPath("$[0].price").value(13499.0))
                .andExpect(jsonPath("$[1].name").value("Xiaomi Redmi Note 9 Pro"))
                .andExpect(jsonPath("$[1].price").value(11699.0))
                .andExpect(jsonPath("$[2].name").value("Apple iPhone 14"))
                .andExpect(jsonPath("$[2].price").value(41499.0));
        verify(productService, times(1)).findAll();
    }

    @Test
    @WithUserDetails("admin")
    void testFetchListOfProductsSortByPrice() throws Exception {
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

        when(productService.findAll()).thenReturn(productsList);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/products/?sort={}", "price")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Xiaomi Redmi Note 9 Pro"))
                .andExpect(jsonPath("$[0].price").value(11699.0))
                .andExpect(jsonPath("$[1].name").value("Samsung Galaxy M52"))
                .andExpect(jsonPath("$[1].price").value(13499.0))
                .andExpect(jsonPath("$[2].name").value("Apple iPhone 14"))
                .andExpect(jsonPath("$[2].price").value(41499.0));
        verify(productService, times(1)).findAll();
    }

    @Test
    @WithUserDetails("admin")
    void testFetchListOfProductsSortByName() throws Exception {
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

        when(productService.findAll()).thenReturn(productsList);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/products/?sort={}", "name")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name").value("Samsung Galaxy M52"))
                .andExpect(jsonPath("$[1].price").value(13499.0))
                .andExpect(jsonPath("$[0].name").value("Apple iPhone 14"))
                .andExpect(jsonPath("$[0].price").value(41499.0))
                .andExpect(jsonPath("$[2].name").value("Xiaomi Redmi Note 9 Pro"))
                .andExpect(jsonPath("$[2].price").value(11699.0));
        verify(productService, times(1)).findAll();
    }

    @Test
    @WithUserDetails("admin")
    void testGetProductById() throws Exception {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        when(productService.findById(1)).thenReturn(productSamsung);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/products/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Samsung Galaxy M52"))
                .andExpect(jsonPath("$.price").value(13499.0));
        verify(productService, times(1)).findById(1);
    }

    @Test
    @WithUserDetails("admin")
    void testGetProductByIdIfProductNotFound() throws Exception {
        when(productService.findById(1)).thenThrow(ProductNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/products/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("admin")
    void testSaveProduct() throws Exception {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/products/")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productSamsung)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Samsung Galaxy M52"))
                .andExpect(jsonPath("$.price").value(13499.0));
        verify(productService).save(any(Product.class));
    }

    @Test
    @WithUserDetails()
    void testSaveProductForbiddenForUsers() throws Exception {
        Product product = Product.builder().build();
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/products/")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void testSaveProductIfProductNull() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/products/")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("admin")
    void testUpdateProduct() throws Exception {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/v1/products/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productSamsung)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Samsung Galaxy M52"))
                .andExpect(jsonPath("$.price").value(13499.0));
        verify(productService).save(any(Product.class));
    }

    @Test
    @WithUserDetails()
    void testUpdateProductForbiddenForUsers() throws Exception {
        Product product = Product.builder().build();
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/v1/products/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void testUpdateProductIfProductNull() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/v1/products/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("admin")
    void testDeleteProduct() throws Exception {
        Product productSamsung = Product.builder()
                .id(1)
                .name("Samsung Galaxy M52")
                .description("6.7 inches, Qualcomm SM7325 Snapdragon 778G 5G")
                .price(13499.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();

        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/api/v1/products/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productSamsung)))
                .andExpect(status().isNoContent());
        verify(productService, times(1)).delete(1);
    }

    @Test
    @WithUserDetails()
    void testDeleteProductForbiddenForUsers() throws Exception {
        Product product = Product.builder().build();
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/api/v1/products/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isForbidden());
    }

}