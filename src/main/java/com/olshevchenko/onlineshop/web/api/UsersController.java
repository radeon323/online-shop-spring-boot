package com.olshevchenko.onlineshop.web.api;

import com.olshevchenko.onlineshop.dto.UserDto;
import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.security.entity.Role;
import com.olshevchenko.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class UsersController {

    private final UserService userService;

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") int id) {
        User user = userService.findById(id);
        UserDto userDto = entityToDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam() String email) {
        User user = userService.findByEmail(email);
        UserDto userDto = entityToDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping()
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        User user = dtoToEntity(userDto);
        userService.save(user, Role.USER);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<UserDto> updateCurrentUser(@AuthenticationPrincipal User currentUser, @Valid @RequestBody UserDto userDto) {
        User user = dtoToEntity(userDto);
        user.setId(currentUser.getId());
        Role role = currentUser.getRole();
        userService.save(user, role);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('users:write')")
    public void deleteCurrentUser(@AuthenticationPrincipal User currentUser) {
        int id = currentUser.getId();
        userService.delete(id);
    }

    @GetMapping("all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById(@PathVariable("id") int id) {
        userService.delete(id);
    }


    private User dtoToEntity(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .gender(userDto.getGender())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .about(userDto.getAbout())
                .age(userDto.getAge())
                .role(userDto.getRole())
                .build();
    }

    private UserDto entityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .gender(user.getGender())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .about(user.getAbout())
                .age(user.getAge())
                .role(user.getRole())
                .build();
    }

}
