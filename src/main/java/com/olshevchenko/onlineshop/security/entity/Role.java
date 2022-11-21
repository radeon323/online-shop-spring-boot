package com.olshevchenko.onlineshop.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.olshevchenko.onlineshop.security.entity.UserPermission.*;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(Set.of(PRODUCT_READ, PRODUCT_WRITE, PRODUCT_DELETE, USER_READ, USER_WRITE)),
    USER(Set.of(PRODUCT_READ, USER_READ, USER_WRITE)),
    GUEST(Set.of(PRODUCT_READ));
    private final Set<UserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
