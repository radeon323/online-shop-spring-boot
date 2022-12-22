package com.olshevchenko.onlineshop.web.api;

import com.olshevchenko.onlineshop.entity.CartItem;
import com.olshevchenko.onlineshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/cart/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {

    private final CartService cartService;

    @GetMapping()
    protected List<CartItem> getCart(HttpSession session) {
        return createCartInSession(session);
    }

    @PostMapping()
    protected ResponseEntity<List<CartItem>> addProductToCart(HttpSession session, @RequestBody() CartItem cartItem) {
        List<CartItem> cartItems = createCartInSession(session);
        cartService.addToCart(cartItems, cartItem);
        session.setAttribute("cartItems", cartItems);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping()
    protected ResponseEntity<List<CartItem>> updateCart(HttpSession session, @RequestBody() CartItem cartItem) {
        List<CartItem> cartItems = createCartInSession(session);
        cartService.updateQuantity(cartItems,cartItem);
        session.setAttribute("cartItems", cartItems);
        return ResponseEntity.ok(cartItems);
    }


    @DeleteMapping()
    protected ResponseEntity<List<CartItem>> removeProductFromCart(HttpSession session, @RequestBody() CartItem cartItem) {
        List<CartItem> cartItems = createCartInSession(session);
        cartService.removeFromCart(cartItems,cartItem);
        session.setAttribute("cartItems", cartItems);
        return ResponseEntity.ok(cartItems);
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> createCartInSession(HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = Collections.synchronizedList(new ArrayList<>());
            session.setAttribute("cartItems", cartItems);
        }
        return cartItems;
    }

}
