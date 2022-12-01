package com.olshevchenko.onlineshop.web;

import com.olshevchenko.onlineshop.entity.CartItem;
import com.olshevchenko.onlineshop.exception.ProductNotFoundException;
import com.olshevchenko.onlineshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart/")
public class CartRestController {

    private final CartService cartService;
    private List<CartItem> cartItems = new CopyOnWriteArrayList<>();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    protected List<CartItem> getCart(HttpSession session) {
        cartItems = getCartFromSession(session);
        return cartItems;
    }

    @PostMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<List<CartItem>> addProductToCart(@PathVariable("id") int id, HttpSession session,
                                              @RequestParam(value = "quantity", required = false) Integer quantity) {
        cartItems = getCartFromSession(session);
        if (quantity != null && quantity != 0) {
            try {
                cartService.updateQuantity(cartItems,id,quantity);
            } catch (ProductNotFoundException e) {
                log.error("There is no product with id {} in the cart", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            cartService.addToCart(cartItems,id);
        }
        session.setAttribute("cartItems", cartItems);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<List<CartItem>> updateCart(@PathVariable("id") int id, HttpSession session,
                                        @RequestParam(value = "quantity", required = false) Integer quantity) {
        cartItems = getCartFromSession(session);
        if (quantity != null && quantity != 0) {
            try {
                cartService.updateQuantity(cartItems,id,quantity);
            } catch (ProductNotFoundException e) {
                log.error("There is no product with id {} in the cart", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        session.setAttribute("cartItems", cartItems);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<List<CartItem>> removeProductFromCart(@PathVariable("id") int id, HttpSession session) {
        cartItems = getCartFromSession(session);
        try {
            cartService.removeFromCart(cartItems,id);
        } catch (ProductNotFoundException e) {
            log.error("There is no product with id {} in the cart", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        session.setAttribute("cartItems", cartItems);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    List<CartItem> getCartFromSession(HttpSession session) {
        cartItems = (List<CartItem>) session.getAttribute("cartItems");
        return cartItems;
    }

}
