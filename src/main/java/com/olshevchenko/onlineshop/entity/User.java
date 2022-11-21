package com.olshevchenko.onlineshop.entity;

import com.olshevchenko.onlineshop.security.entity.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class User implements UserDetails {

    private int id;
    private String email;
    private String password;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String about;
    private int age;
    private Role role;

    private Set<? extends GrantedAuthority> grantedAuthorities;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole().getGrantedAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

