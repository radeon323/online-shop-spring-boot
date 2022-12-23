package com.olshevchenko.onlineshop.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * @author Oleksandr Shevchenko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private int id;

    @NotEmpty(message = "The product name is required.")
    private String name;

    private String description;

    @Positive(message = "The product price price must be greater than 0")
    @NotNull(message = "The product price is required.")
    private double price;

    private LocalDateTime creationDate;
}
