package com.olshevchenko.onlineshop.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;
}
