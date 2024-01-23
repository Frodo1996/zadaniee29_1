package com.example.demo.controller;

import com.example.demo.user.User;
import com.example.demo.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/rejestracja")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "rejestracja";
    }

    @Transactional
    @PostMapping("/rejestracja")
    public String registerUser(User user, Model model) {
        String username = user.getEmail();
        String rawPassword = user.getPassword();
        boolean registrationSuccess = userService.registerUser(username, rawPassword);
        userService.registerUser(username, rawPassword);
        if (registrationSuccess) {
            model.addAttribute("registrationSuccess", true);
        } else {
            model.addAttribute("registrationError", true);
        }
        return "rejestracja";
    }
}