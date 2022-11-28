package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.exception.UserNotFoundException;
import com.olshevchenko.onlineshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Could not find user by id: " + id));
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user by email: " + email));
    }

    public void save(User user) {
        repository.save(user);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", email)));
    }
}
