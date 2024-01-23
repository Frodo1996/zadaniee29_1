package com.example.demo.controller;

import com.example.demo.user.Role;
import com.example.demo.user.User;
import com.example.demo.user.UserRole;
import com.example.demo.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String adminDashboard(Model model) {
        List<User> users = userService.findAllWithoutCurrentUser();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/userList")
    public String userList(Model model) {
        List<User> users = userService.findAllUsers();
        String rolesOfCurrentUser = userService.findRolesOfCurrentUser();
        model.addAttribute("rolesOfCurrentUser", rolesOfCurrentUser);
        model.addAttribute("users", users);
        return "userList";
    }

    @PostMapping("/assignRole")
    public String assignRole(@RequestParam Long userId, @RequestParam UserRole role) {
        userService.assignRole(userId, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/revokeRole")
    public String revokeRole(@RequestParam Long userId, @RequestParam UserRole role) {
        userService.revokeRole(userId, role);
        return "redirect:/admin/users";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}

