package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.repository.UserRepository;
import com.olshevchenko.onlineshop.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository jdbcUserRepository;

    public Optional<User> findById(int id) {
        return jdbcUserRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return jdbcUserRepository.findByEmail(email);
    }

    public void add(User user) {
        jdbcUserRepository.add(user);
    }

    public void remove(int id) {
        jdbcUserRepository.remove(id);
    }

    public void edit(User user) {
        jdbcUserRepository.update(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", email)));
    }
}
