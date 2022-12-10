package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.exception.UserAlreadyExistException;
import com.olshevchenko.onlineshop.exception.UserNotFoundException;
import com.olshevchenko.onlineshop.repository.UserRepository;
import com.olshevchenko.onlineshop.security.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Oleksandr Shevchenko
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public void save(User user, Role role) {
        try {
            String email = user.getEmail();
            String emailInDb = findByEmail(email).getEmail();
            if (Objects.equals(email, emailInDb)) {
                throw new UserAlreadyExistException("User with email: " + email + " already exists!");
            }
        } catch (UserNotFoundException e) {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            repository.save(user);
        }
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
