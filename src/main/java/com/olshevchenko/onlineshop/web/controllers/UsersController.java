package com.olshevchenko.onlineshop.web.controllers;

import com.olshevchenko.onlineshop.entity.Gender;
import com.olshevchenko.onlineshop.entity.User;
import com.olshevchenko.onlineshop.security.entity.Role;
import com.olshevchenko.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping()
public class UsersController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    protected String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    protected String getRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    protected String register(@RequestParam String email, @RequestParam String password,
                              @RequestParam(defaultValue = "MALE") String gender,
                              @RequestParam(defaultValue = "") String firstName,
                              @RequestParam(defaultValue = "") String lastName,
                              @RequestParam(defaultValue = "") String about,
                              @RequestParam(defaultValue = "0") int age,
                              ModelMap model) {

        if (email.isEmpty() || password.isEmpty()) {
            String errorMsg = "Please fill up all necessary fields!";
            model.addAttribute("errorMsg", errorMsg);
            return "register";
        }

        if (userService.findByEmail(email) != null ) {
            String errorMsg = "This user is already exist! <a href='/login'> Login page</a>";
            model.addAttribute("errorMsg", errorMsg);
            return "register";
        } else {
            Optional<User> optionalUser = Optional.ofNullable(User.builder().
                    email(email)
                    .password(passwordEncoder.encode(password))
                    .gender(Gender.valueOf(gender.toUpperCase()))
                    .firstName(firstName)
                    .lastName(lastName)
                    .about(about)
                    .age(age)
                    .role(Role.USER)
                    .build());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                userService.save(user);
                String msgSuccess = String.format("User <i>%s</i> was successfully registered!", email);
                model.addAttribute("msgSuccess", msgSuccess);
            }
        }
        return "register";
    }

    @GetMapping("/users/edit")
    protected String getEditUserPage() {
        return "edit_user";
    }

    @PostMapping("/users/edit")
    protected String editUser(@RequestParam String email, @RequestParam String password,
                    @RequestParam(defaultValue = "") String firstName,
                    @RequestParam(defaultValue = "") String lastName,
                    @RequestParam(defaultValue = "") String about,
                    @RequestParam(defaultValue = "0") int age,
                    HttpSession session,
                    ModelMap model) {

        SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User oldUser = (User) context.getAuthentication().getPrincipal();

        if (email.isEmpty() || password.isEmpty()) {
            String errorMsg = "Please fill up all necessary fields!";
            model.addAttribute("errorMsg", errorMsg);
            return "edit_user";
        }

        int id = oldUser.getId();
        Role role = oldUser.getRole();
        Gender gender = oldUser.getGender();

        if (userService.findByEmail(email) != null ) {
            User user = User.builder()
                    .id(id)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .gender(gender)
                    .firstName(firstName)
                    .lastName(lastName)
                    .about(about)
                    .age(age)
                    .role(role)
                    .build();

            if (Optional.ofNullable(user).isPresent()) {
                user.setId(id);
                userService.save(user);
                String msgSuccess = String.format("User <i>%s</i> was successfully changed!", user.getEmail());
                model.addAttribute("msgSuccess", msgSuccess);
            }
        }
        return "edit_user";
    }


}
