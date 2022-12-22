package com.olshevchenko.onlineshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olshevchenko.onlineshop.entity.Gender;
import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.exception.UserNotFoundException;
import com.olshevchenko.onlineshop.security.entity.Role;
import com.olshevchenko.onlineshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.olshevchenko.onlineshop.security.entity.Role.ADMIN;
import static com.olshevchenko.onlineshop.security.entity.Role.USER;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebAuxTestConfig.class
)
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final User darthVader = User.builder()
                                        .id(1)
                                        .email("darthvader@gmail.com")
                                        .password("$2a$10$hHINZ1WVjQltKiMiFYyp9OsxnblocMA7yBoCXEn6yarNG.53RR9DK")
                                        .gender(Gender.MALE)
                                        .firstName("Darth")
                                        .lastName("Vader")
                                        .about("I'm your father")
                                        .age(41)
                                        .role(USER)
                                        .build();

    private final User lukeSkywalker = User.builder()
                                        .id(2)
                                        .email("lukeskywalker@gmail.com")
                                        .password("$2a$10$hHINZ1WVjQltKiMiFYyp9OsxnblocMA7yBoCXEn6yarNG.53RR9DK")
                                        .gender(Gender.MALE)
                                        .firstName("Luke")
                                        .lastName("Skywalker")
                                        .about("May the force be with you!!!")
                                        .age(19)
                                        .role(ADMIN)
                                        .build();

    private final List<User> usersList = List.of(darthVader, lukeSkywalker);

    @Test
    @WithUserDetails("admin")
    void testFetchUsers() throws Exception {
        when(userService.findAll()).thenReturn(usersList);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value("darthvader@gmail.com"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[1].email").value("lukeskywalker@gmail.com"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));
        verify(userService, times(1)).findAll();
    }

    @Test
    @WithUserDetails("admin")
    void testFetchUserByEmail() throws Exception {
        when(userService.findByEmail("darthvader@gmail.com")).thenReturn(darthVader);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/?email={email}", "darthvader@gmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("darthvader@gmail.com"))
                .andExpect(jsonPath("$.role").value("USER"));
        verify(userService, times(1)).findByEmail("darthvader@gmail.com");
    }

    @Test
    void testFetchUserByEmailForbiddenForUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/?email={email}", "darthvader@gmail.com")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("darthvader@gmail.com", "$2a$10$hHINZ1WVjQltKiMiFYyp9OsxnblocMA7yBoCXEn6yarNG.53RR9DK"))
                        .with(user("user"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void testFetchUserByEmailIfUserNotFound() throws Exception {
        when(userService.findByEmail("hansolo@gmail.com")).thenThrow(UserNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/?email={email}", "hansolo@gmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).findByEmail("hansolo@gmail.com");
    }

    @Test
    void testFetchUsersIfForbiddenForUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/all")
                        .with(user("user"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void testFetchUserById() throws Exception {
        when(userService.findById(2)).thenReturn(lukeSkywalker);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/{id}", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("lukeskywalker@gmail.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
        verify(userService, times(1)).findById(2);
    }

    @Test
    void testFetchUserByIdIfForbiddenForUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/{id}", 2)
                        .with(user("user"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void testFetchUserByIdIfUserNotFound() throws Exception {
        when(userService.findById(3)).thenThrow(UserNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/users/{id}", 3)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).findById(3);
    }

    @Test
    @WithUserDetails("admin")
    void testSaveUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lukeSkywalker)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("lukeskywalker@gmail.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
        verify(userService).save(any(User.class), any(Role.class));
    }

    @Test
    @WithUserDetails("admin")
    void testSaveUserIfUserNull() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("admin")
    void testUpdateCurrentUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lukeSkywalker)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("lukeskywalker@gmail.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
        verify(userService).save(any(User.class), any(Role.class));
    }

    //TODO: not injecting principal to mockmvc, 403 forbidden
    @Test
    @WithMockUser("username")
    void testDeleteCurrentUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService).delete(any(Integer.class));
    }
}