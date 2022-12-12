package com.olshevchenko.onlineshop.web;

import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.security.entity.Role;
import com.olshevchenko.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersRestController {

    private final UserService userService;

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByEmail(@RequestParam() String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.save(user, Role.USER);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<User> updateCurrentUser(@AuthenticationPrincipal User currentUser, @Valid @RequestBody User user) {
        user.setId(currentUser.getId());
        Role role = currentUser.getRole();
        userService.save(currentUser, role);
        return ResponseEntity.ok(currentUser);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<Integer> deleteCurrentUser(@AuthenticationPrincipal User currentUser) {
        int id = currentUser.getId();
        userService.delete(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteUserById(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.ok(id);
    }

}
