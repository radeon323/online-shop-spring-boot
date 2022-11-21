package com.olshevchenko.onlineshop.repository.mapper;

import com.olshevchenko.onlineshop.entity.Gender;
import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.security.entity.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Oleksandr Shevchenko
 */
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        Gender gender = Gender.valueOf(resultSet.getString("gender"));
        String firstName = resultSet.getString("firstname");
        String lastName = resultSet.getString("lastname");
        String about = resultSet.getString("about");
        int age = resultSet.getInt("age");
        Role role = Role.valueOf(resultSet.getString("role"));
        return User.builder().
                id(id)
                .email(email)
                .password(password)
                .gender(gender)
                .firstName(firstName)
                .lastName(lastName)
                .about(about)
                .age(age)
                .role(role)
                .build();
    }
}
