package com.olshevchenko.onlineshop.entity;

import com.olshevchenko.onlineshop.security.entity.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * @author Oleksandr Shevchenko
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table( name = "users" )
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    //TODO: solve org.postgresql.util.PSQLException:
    // ERROR: column "gender" is of type gender but expression is of type character varying
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "about")
    private String about;

    @Column(name = "age")
    private int age;

    //TODO: solve org.postgresql.util.PSQLException:
    // ERROR: column "role" is of type gender but expression is of type character varying
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Transient
    private Set<? extends GrantedAuthority> grantedAuthorities;

    @Transient
    private boolean isAccountNonExpired;

    @Transient
    private boolean isAccountNonLocked;

    @Transient
    private boolean isCredentialsNonExpired;

    @Transient
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getGrantedAuthorities();
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

