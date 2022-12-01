package com.olshevchenko.onlineshop.web;

import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.exception.UserNotFoundException;
import com.olshevchenko.onlineshop.security.entity.Role;
import com.olshevchenko.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/")
public class UsersRestController {

    private final UserService userService;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user;
        try {
            user = userService.findById(id);
        } catch (UserNotFoundException e) {
            log.error("Could not find user by id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUserByEmail(@RequestParam(value="email", required = false) String email, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<User> users;
        if (email != null) {
            try {
                User user = userService.findByEmail(email);
                users = List.of(user);
            } catch (UserNotFoundException e) {
                log.error("Could not find user by email: {}", email);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            users = userService.findAll();
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //TODO: validation??
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.save(user, Role.USER);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //TODO: validation??
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateCurrentUser(@AuthenticationPrincipal User currentUser, @RequestBody User user) {
        user.setId(currentUser.getId());
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            userService.save(currentUser, Role.ADMIN);
        } else {
            userService.save(currentUser, Role.USER);
        }
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteCurrentUser(@AuthenticationPrincipal User currentUser) {
        int id = currentUser.getId();
        userService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
