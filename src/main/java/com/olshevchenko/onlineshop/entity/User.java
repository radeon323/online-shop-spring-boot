package com.olshevchenko.onlineshop.entity;

import com.olshevchenko.onlineshop.security.entity.Role;
import com.olshevchenko.onlineshop.utils.PGSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

/**
 * @author Oleksandr Shevchenko
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "pgsql_enum", typeClass = PGSQLEnumType.class)
@Table( name = "users" )
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq")
    private int id;

    private String email;

    private String password;

    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    private String about;
    private int age;

    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    private Role role;

}

