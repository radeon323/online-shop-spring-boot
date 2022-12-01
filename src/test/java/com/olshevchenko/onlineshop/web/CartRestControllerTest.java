package com.olshevchenko.onlineshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olshevchenko.onlineshop.entity.CartItem;
import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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
class CartRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;


    //TODO: how to inject session in test?
    @Test
    @WithUserDetails()
    void testFetchListOfCartItems() throws Exception {
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

        List<CartItem> cartItems = List.of(new CartItem(productSamsung, 1),
                                           new CartItem(productXiaomi, 1),
                                           new CartItem(productApple, 1));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute( "cartItems", cartItems);

//        when(session.getAttribute("cartItems")).thenReturn(cartItems);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/cart/")
                        .session(session)
                        .sessionAttr("cartItems", cartItems)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].product").value(productSamsung))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[1].product").value(productXiaomi))
                .andExpect(jsonPath("$[1].quantity").value(1))
                .andExpect(jsonPath("$[2].product").value(productApple))
                .andExpect(jsonPath("$[2].quantity").value(1));
    }

    @Test
    @WithUserDetails()
    void testAddToCart() throws Exception {
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

        List<CartItem> cartItems = List.of(new CartItem(productSamsung, 1),
                                           new CartItem(productXiaomi, 1),
                                           new CartItem(productApple, 1));

        cartService.addToCart(cartItems,1);
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/cart/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product").value(productSamsung))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

}