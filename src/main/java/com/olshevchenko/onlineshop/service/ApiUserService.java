package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.security.entity.ApiUser;
import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Oleksandr Shevchenko
 */
@Service
@RequiredArgsConstructor
public class ApiUserService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", email)));
        return ApiUser.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}
