package com.olshevchenko.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Oleksandr Shevchenko
 */
@Data
@AllArgsConstructor
public class CartItemDto {
    private int productId;
    private int quantity;
}
