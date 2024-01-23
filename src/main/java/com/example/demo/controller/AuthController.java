package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/logowanie")
    public String loginForm(@RequestParam(required = false) String error,
                            Model model) {
        boolean showErrorMessage = error != null;
        model.addAttribute("showErrorMessage", showErrorMessage);
        return "login";
    }

    @PostMapping("/logowanie")
    public String loginSubmit() {
        return "home";
    }
}