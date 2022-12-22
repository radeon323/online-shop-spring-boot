package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.entity.CartItem;
import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.exception.ProductNotFoundException;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@RequiredArgsConstructor
@Service
public class CartService {

    private final ProductService productService;

    public void addToCart(List<CartItem> cart, CartItem cartItem) {
        int productId = cartItem.getProduct().getId();
        int quantity = cartItem.getQuantity();
        Product product = productService.findById(productId);
        cart.stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> cart.add(new CartItem(product, quantity)));
    }

    public void removeFromCart(List<CartItem> cart, CartItem cartItemToDelete) {
        int productId = cartItemToDelete.getProduct().getId();
        CartItem cartItem = cart.stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Could not find product by id: " + productId));
        cart.remove(cartItem);
    }

    public void updateQuantity(List<CartItem> cart, CartItem cartItem) {
        int productId = cartItem.getProduct().getId();
        int quantity = cartItem.getQuantity();
        Product product = productService.findById(productId);
        cart.stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(quantity),
                    () -> cart.add(new CartItem(product, quantity)));
    }

}
