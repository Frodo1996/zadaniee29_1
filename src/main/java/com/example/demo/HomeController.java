package com.example.demo;

import com.example.demo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("")
    public String homepage(Model model) {
        model.addAttribute("user", new User());
        return "home";
    }

    @GetMapping("/secure")
    public String secure() {
        return "secure";
    }
}