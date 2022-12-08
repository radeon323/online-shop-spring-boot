package com.olshevchenko.onlineshop.web;

import com.olshevchenko.onlineshop.entity.CartItem;
import com.olshevchenko.onlineshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public class CartRestController {

    private final CartService cartService;

    @GetMapping()
    protected List<CartItem> getCart(HttpSession session,
                                     @SessionAttribute(required = false) List<CartItem> cartItems) {
        session.setAttribute("cartItems", cartItems);
        return cartItems;
    }

    @PostMapping("{id}")
    protected ResponseEntity<List<CartItem>> addProductToCart(@SessionAttribute(required = false) List<CartItem> cartItems, HttpSession session,
                                                              @PathVariable("id") int id, @RequestParam(required = false) Integer quantity) {
        cartItems = getItemList(cartItems);
        if (quantity != null && quantity != 0) {
            cartService.updateQuantity(cartItems,id,quantity);
        } else {
            cartService.addToCart(cartItems,id);
        }
        session.setAttribute("cartItems", cartItems);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("{id}")
    protected ResponseEntity<List<CartItem>> updateCart(@SessionAttribute(required = false) List<CartItem> cartItems, HttpSession session,
                                                        @PathVariable("id") int id, @RequestParam(required = false) Integer quantity) {
        cartItems = getItemList(cartItems);
        if (quantity != null && quantity != 0) {
            cartService.updateQuantity(cartItems,id,quantity);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        session.setAttribute("cartItems", cartItems);
        return ResponseEntity.ok(cartItems);
    }


    @DeleteMapping("{id}")
    protected ResponseEntity<List<CartItem>> removeProductFromCart(@PathVariable("id") int id, HttpSession session,
                                                                   @SessionAttribute(required = false) List<CartItem> cartItems) {
        cartItems = getItemList(cartItems);
        cartService.removeFromCart(cartItems,id);
        session.setAttribute("cartItems", cartItems);
        return ResponseEntity.ok(cartItems);
    }

    private List<CartItem> getItemList(List<CartItem> cartItems) {
        if (cartItems == null) {
            cartItems = Collections.synchronizedList(new ArrayList<>());
        }
        return cartItems;
    }

}
