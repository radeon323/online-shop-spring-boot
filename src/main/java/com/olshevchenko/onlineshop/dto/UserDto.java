package com.olshevchenko.onlineshop.dto;

import com.olshevchenko.onlineshop.entity.Gender;
import com.olshevchenko.onlineshop.security.entity.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author Oleksandr Shevchenko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;

    @NotEmpty(message = "The email is required.")
    @Email(message = "Enter a valid email")
    private String email;

    @NotEmpty(message = "The password is required.")
    private String password;

    private Gender gender;
    private String firstName;
    private String lastName;
    private String about;
    private int age;
    private Role role;
}

