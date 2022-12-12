package com.olshevchenko.onlineshop.security;

import com.olshevchenko.onlineshop.jwt.JwtConfig;
import com.olshevchenko.onlineshop.jwt.JwtTokenVerifier;
import com.olshevchenko.onlineshop.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.olshevchenko.onlineshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

import static com.olshevchenko.onlineshop.security.entity.UserPermission.*;

/**
 * @author Oleksandr Shevchenko
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()

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

    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

}
