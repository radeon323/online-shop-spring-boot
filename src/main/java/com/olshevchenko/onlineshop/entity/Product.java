package com.olshevchenko.onlineshop.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Oleksandr Shevchenko
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table( name = "products" )
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_id_gen")
    @SequenceGenerator(name = "products_id_gen", sequenceName = "products_id_seq")
    private int id;

    @NotEmpty(message = "The product name is required.")
    private String name;

    private String description;

    @Positive(message = "The product price price must be greater than 0")
    @NotNull(message = "The product price is required.")
    private double price;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
