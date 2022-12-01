package com.olshevchenko.onlineshop.security;

import com.olshevchenko.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.olshevchenko.onlineshop.security.entity.UserPermission.*;
import static com.olshevchenko.onlineshop.security.entity.UserPermission.USER_WRITE;

/**
 * @author Oleksandr Shevchenko
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()

                .antMatchers(HttpMethod.GET,"/api/v1/products/**").hasAuthority(PRODUCT_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/api/v1/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"/api/v1/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE,"/api/v1/products/**").hasAuthority(PRODUCT_DELETE.getPermission())

                .antMatchers("/api/v1/cart/**").hasAuthority(PRODUCT_READ.getPermission())

                .antMatchers(HttpMethod.GET,"/api/v1/users/**").hasAuthority(USER_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/api/v1/users/**").hasAuthority(USER_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"/api/v1/users/**").hasAuthority(USER_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE,"/api/v1/users/**").hasAuthority(USER_WRITE.getPermission())

                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        http.authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
