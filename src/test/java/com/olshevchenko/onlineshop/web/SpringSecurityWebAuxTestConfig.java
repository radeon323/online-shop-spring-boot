package com.olshevchenko.onlineshop.web;

import com.olshevchenko.onlineshop.security.entity.ApiUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

import static com.olshevchenko.onlineshop.security.entity.Role.ADMIN;
import static com.olshevchenko.onlineshop.security.entity.Role.USER;

/**
 * @author Oleksandr Shevchenko
 */
@TestConfiguration
public class SpringSecurityWebAuxTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        ApiUser admin = ApiUser.builder()
                .email("admin")
                .password("password")
                .role(ADMIN)
                .grantedAuthorities(ADMIN.getGrantedAuthorities())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
        ApiUser user = ApiUser.builder()
                .email("username")
                .password("password")
                .role(USER)
                .grantedAuthorities(USER.getGrantedAuthorities())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
        return new InMemoryUserDetailsManager(Arrays.asList(
                admin, user
        ));
    }
}
