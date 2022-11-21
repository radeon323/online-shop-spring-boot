package com.olshevchenko.onlineshop.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private LocalDateTime creationDate;
}
