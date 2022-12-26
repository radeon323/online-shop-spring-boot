package com.olshevchenko.onlineshop.web.api;

import com.olshevchenko.onlineshop.security.entity.AuthenticationRequest;
import com.olshevchenko.onlineshop.security.jwt.JwtUtils;
import com.olshevchenko.onlineshop.service.ApiUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oleksandr Shevchenko
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final ApiUserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        String email = request.getEmail();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        final UserDetails user = userService.loadUserByUsername(email);

        if (user == null) {
            return ResponseEntity.status(400).body("Error when load user by email " + email);
        }

        return ResponseEntity.ok(jwtUtils.generateToken(user));
    }
}
