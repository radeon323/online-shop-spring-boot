package com.olshevchenko.onlineshop.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Oleksandr Shevchenko
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "products" )
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_id_gen")
    @SequenceGenerator(name = "products_id_gen", sequenceName = "products_id_seq")
    private int id;

    private String name;

    private String description;

    private double price;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
