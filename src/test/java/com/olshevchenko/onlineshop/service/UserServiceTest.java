package com.olshevchenko.onlineshop.service;

import com.olshevchenko.onlineshop.repository.UserRepository;
import com.olshevchenko.onlineshop.entity.Gender;
import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.security.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    private UserService userService;

    private User expectedUser;

    @BeforeEach
    void init() {
        userService = new UserService(userRepositoryMock);

        expectedUser = User.builder()
                .id(1)
                .email("darthvader@gmail.com")
                .password(" �vX���i�G��\u0006\u0005JKJ:�\u0012Y��k]�GB��1�Y�G\u001A�\u0001\u0011*���Y��t��\u0011��\u0006�Y�����s���")
                .gender(Gender.MALE)
                .firstName("Darth")
                .lastName("Vader")
                .about("May the force be with you!")
                .age(41)
                .role(Role.USER)
                .build();
    }

    @Test
    void testFindById() {
        when(userRepositoryMock.findById(1)).thenReturn(Optional.ofNullable(expectedUser));
        Optional<User> optionalUser = userService.findById(1);
        User actualUser = optionalUser.get();
        assertEquals(expectedUser, actualUser);
        verify(userRepositoryMock, times(1)).findById(1);
    }

    @Test
    void testFindByEmail() {
        when(userRepositoryMock.findByEmail("darthvader@gmail.com")).thenReturn(Optional.ofNullable(expectedUser));
        Optional<User> optionalUser = userService.findByEmail("darthvader@gmail.com");
        User actualUser = optionalUser.get();
        assertEquals(expectedUser, actualUser);
        verify(userRepositoryMock, times(1)).findByEmail("darthvader@gmail.com");
    }

    @Test
    void testAdd() {
        doNothing().when(userRepositoryMock).add(isA(User.class));
        userRepositoryMock.add(expectedUser);
        verify(userRepositoryMock, times(1)).add(expectedUser);
    }

    @Test
    void testRemove() {
        doNothing().when(userRepositoryMock).remove(isA(Integer.class));
        userRepositoryMock.remove(1);
        verify(userRepositoryMock, times(1)).remove(1);
    }

    @Test
    void testEdit() {
        doNothing().when(userRepositoryMock).update(isA(User.class));
        userRepositoryMock.update(expectedUser);
        verify(userRepositoryMock, times(1)).update(expectedUser);
    }


}