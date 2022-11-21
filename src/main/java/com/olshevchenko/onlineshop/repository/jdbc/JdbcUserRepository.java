package com.olshevchenko.onlineshop.repository.jdbc;

import com.olshevchenko.onlineshop.repository.UserRepository;
import com.olshevchenko.onlineshop.repository.mapper.UserRowMapper;
import com.olshevchenko.onlineshop.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final String FIND_BY_ID_SQL = "SELECT id, email, password, gender, firstName, lastName, about, age, role FROM users WHERE id = ?";
    private static final String FIND_BY_EMAIL_SQL = "SELECT id, email, password, gender, firstName, lastName, about, age, role FROM users WHERE email = ?";
    private static final String ADD_SQL = "INSERT INTO users (email, password, gender, firstName, lastName, about, age, role) VALUES (:email, :password, (:gender::gender), :firstName, :lastName, :about, :age, (:role::user_role));";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String UPDATE_BY_ID_SQL = "UPDATE users SET email = :email, password = :password, gender = (:gender::gender), firstName = :firstName, lastName = :lastName, about = :about, age = :age, role = (:role::user_role) WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findById(int id) {
        User user = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, USER_ROW_MAPPER, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = jdbcTemplate.queryForObject(FIND_BY_EMAIL_SQL, USER_ROW_MAPPER, email);
        return Optional.ofNullable(user);
    }

    @Override
    public void add(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("password", user.getPassword());
        parameters.put("gender", user.getGender().name());
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("about", user.getAbout());
        parameters.put("age", user.getAge());
        parameters.put("role", user.getRole().name());
        namedParameterJdbcTemplate.update(ADD_SQL, parameters);
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    @Override
    public void update(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("password", user.getPassword());
        parameters.put("gender", user.getGender().name());
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("about", user.getAbout());
        parameters.put("age", user.getAge());
        parameters.put("role", user.getRole().name());
        parameters.put("id", user.getId());
        namedParameterJdbcTemplate.update(UPDATE_BY_ID_SQL, parameters);
    }


}
