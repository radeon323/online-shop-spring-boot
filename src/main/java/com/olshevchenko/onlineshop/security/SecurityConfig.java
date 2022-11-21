package com.olshevchenko.onlineshop.security;

import com.olshevchenko.onlineshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

import static com.olshevchenko.onlineshop.security.entity.UserPermission.*;

/**
 * @author Oleksandr Shevchenko
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_URI = "/login";
    public static final String LOGOUT_URI = "/logout";

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*",
                         "/register", "/register/", "/products", "/products/").permitAll()

                .antMatchers("/cart", "/cart/", "/cart/delete").hasAuthority(PRODUCT_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/cart/update").hasAuthority(PRODUCT_READ.getPermission())

                .antMatchers(HttpMethod.GET,"/products/add", "/products/add/", "/products/edit/*").hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.POST,"/products/add", "/products/add/", "/products/edit/*").hasAuthority(PRODUCT_WRITE.getPermission())

                //TODO: make ability to edin only own user
                .antMatchers(HttpMethod.GET,"/users/edit/*").hasAuthority(USER_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/users/edit/*").hasAuthority(USER_WRITE.getPermission())

                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage(LOGIN_URI).permitAll()
                    .defaultSuccessUrl("/products", true)
                    .passwordParameter("password")
                    .usernameParameter("email")
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(10))
                    .key("0212ce79-369e-477f-b633-c12446c51c75")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl(LOGOUT_URI).permitAll()
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("SESSION", "remember-me")
                    .logoutSuccessUrl(LOGIN_URI);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
