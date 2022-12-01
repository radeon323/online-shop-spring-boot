package com.olshevchenko.onlineshop.repository;

import com.olshevchenko.onlineshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query(nativeQuery = true,
    value = "INSERT INTO users (email, password, gender, firstName, lastName, about, age, role) VALUES (:email, :password, (:gender::gender), :firstname, :lastname, :about, :age, (:role::user_role))")
    void saveUser(User user);
}
