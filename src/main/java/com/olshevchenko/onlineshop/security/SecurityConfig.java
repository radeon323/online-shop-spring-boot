package com.olshevchenko.onlineshop.security;

import com.olshevchenko.onlineshop.security.jwt.JwtAuthFilter;
import com.olshevchenko.onlineshop.service.ApiUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.olshevchenko.onlineshop.security.entity.UserPermission.*;

/**
 * @author Oleksandr Shevchenko
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final ApiUserService userService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/**/auth/**").permitAll()

                .antMatchers(HttpMethod.GET,"/api/v1/products/**").hasAuthority(PRODUCT_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/api/v1/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"/api/v1/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE,"/api/v1/products/**").hasAuthority(PRODUCT_DELETE.getPermission())

                .antMatchers("/api/v1/cart/**").hasAuthority(PRODUCT_READ.getPermission())
                .antMatchers(HttpMethod.GET,"/api/v1/users/**").hasAuthority(USER_READ.getPermission())

                .anyRequest()
                .authenticated();

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
