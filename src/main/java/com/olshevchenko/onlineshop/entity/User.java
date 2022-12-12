package com.olshevchenko.onlineshop.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.olshevchenko.onlineshop.security.CustomAuthorityDeserializer;
import com.olshevchenko.onlineshop.security.entity.Role;
import com.olshevchenko.onlineshop.utils.PGSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
@TypeDef(name = "pgsql_enum", typeClass = PGSQLEnumType.class)
@Table( name = "users" )
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq")
    private int id;

    @NotEmpty(message = "The email is required.")
    private String email;

    @NotEmpty(message = "The password is required.")
    private String password;

    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    private String about;
    private int age;

    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Transient
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
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
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getGrantedAuthorities();
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

