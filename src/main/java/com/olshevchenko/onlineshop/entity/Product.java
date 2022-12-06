package com.olshevchenko.onlineshop.entity;

import lombok.*;

import javax.persistence.*;
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
    @SequenceGenerator(name = "products_id_gen", sequenceName = "products_id_seq", allocationSize = 1)
    private int id;

    private String name;
    private String description;
    private double price;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
