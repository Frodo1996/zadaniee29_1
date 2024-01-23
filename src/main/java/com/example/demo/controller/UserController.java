package com.example.demo.controller;

import com.example.demo.user.User;
import com.example.demo.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String userDashboard(Model model) {
        User currentUser = userService.findCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "user";
    }

    @GetMapping("/edytuj")
    public String showEditProfileForm(Model model) {
        User currentUser = userService.findCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "edit";
    }

    @PostMapping("/edytuj")
    public String editProfile(@ModelAttribute User updatedUser, @RequestParam String newPassword) {
        userService.editUserProfile(updatedUser, newPassword);
        return "redirect:/user";
    }
}