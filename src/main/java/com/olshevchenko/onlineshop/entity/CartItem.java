package com.olshevchenko.onlineshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Oleksandr Shevchenko
 */
@Data
@AllArgsConstructor
public class CartItem implements Serializable {
    private Product product;
    private int quantity;
}
