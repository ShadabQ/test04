package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "Guest";
        model.addAttribute("username", username);
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority().replace("ROLE_", ""))
            .collect(Collectors.joining(", "));

        model.addAttribute("username", username);
        model.addAttribute("roles", roles);
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
