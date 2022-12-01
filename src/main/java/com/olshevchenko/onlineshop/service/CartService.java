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

    public void addToCart(List<CartItem> cart, int id) {
        Product product = productService.findById(id);
        cart.stream()
                .filter(item -> item.getProduct().getId() == id)
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        () -> cart.add(new CartItem(product, 1)));
    }

    public void removeFromCart(List<CartItem> cart, int id) {
        CartItem cartItem = cart.stream()
                .filter(item -> item.getProduct().getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Could not find product by id: " + id));
        cart.remove(cartItem);
    }

    public void updateQuantity(List<CartItem> cart, int id, int quantity) {
        Product product = productService.findById(id);
        cart.stream()
                .filter(item -> item.getProduct().getId() == id)
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(quantity),
                    () -> cart.add(new CartItem(product, quantity)));
    }

    public double calculateTotalPrice(List<CartItem> cart) {
        double total = 0;
        if (cart==null) {
            return 0;
        }
        for (CartItem item : cart) {
            total = total + item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }


}
