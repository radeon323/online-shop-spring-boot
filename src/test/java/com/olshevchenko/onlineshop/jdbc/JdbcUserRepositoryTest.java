package com.olshevchenko.onlineshop.jdbc;

import com.olshevchenko.onlineshop.entity.Gender;
import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.repository.jdbc.JdbcUserRepository;
import com.olshevchenko.onlineshop.security.entity.Role;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Oleksandr Shevchenko
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcUserRepositoryTest {
    private final BasicDataSource dataSource = new BasicDataSource();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    private final JdbcUserRepository jdbcUserRepository = new JdbcUserRepository(jdbcTemplate, namedParameterJdbcTemplate);
    private final User darthVader;
    private final User lukeSkywalker;

    JdbcUserRepositoryTest() throws SQLException {
        darthVader = User.builder()
                .id(1)
                .email("darthvader@gmail.com")
                .password(" �vX���i�G��\u0006\u0005JKJ:�\u0012Y��k]�GB��1�Y�G\u001A�\u0001\u0011*���Y��t��\u0011��\u0006�Y�����s���")
                .gender(Gender.MALE)
                .firstName("Darth")
                .lastName("Vader")
                .about("I'm your father")
                .age(41)
                .role(Role.USER)
                .build();

        lukeSkywalker = User.builder()
                .id(2)
                .email("lukeskywalker@gmail.com")
                .password(" �vX���i�G��\u0006\u0005JKJ:�\u0012Y��k]�GB��1�Y�G\u001A�\u0001\u0011*���Y��t��\u0011��\u0006�Y�����s���")
                .gender(Gender.MALE)
                .firstName("Luke")
                .lastName("Skywalker")
                .about("May the force be with you!!!")
                .age(19)
                .role(Role.ADMIN)
                .build();

        dataSource.setUrl("jdbc:h2:mem:test");
        Connection connection = dataSource.getConnection();

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        JdbcUserRepositoryTest.class.getClassLoader().getResourceAsStream("users.sql"))));
        RunScript.execute(connection, bufferedReader);
    }

    @Test
    void testFindByIdReturnUser() {
        Optional<User> actualUser = jdbcUserRepository.findById(1);
        assertTrue(actualUser.isPresent());
        assertEquals(darthVader, actualUser.get());
    }

    @Test
    void testFindByEmailReturnUser() {
        Optional<User> actualUser = jdbcUserRepository.findByEmail("darthvader@gmail.com");
        assertTrue(actualUser.isPresent());
        assertEquals(darthVader, actualUser.get());
    }

    @Test
    void testAddAndUpdateAndRemove() {
        jdbcUserRepository.add(lukeSkywalker);
        Optional<User> actualUser2 = jdbcUserRepository.findById(2);
        assertTrue(actualUser2.isPresent());
        assertEquals(lukeSkywalker, actualUser2.get());

        lukeSkywalker.setEmail("skywalker@gmail.com");
        jdbcUserRepository.update(lukeSkywalker);
        Optional<User> actualUser = jdbcUserRepository.findByEmail("skywalker@gmail.com");
        assertTrue(actualUser.isPresent());
        assertEquals(lukeSkywalker, actualUser.get());

        jdbcUserRepository.remove(2);
    }

}