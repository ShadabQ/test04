package com.example.demo.controller;

import com.example.demo.model.SectorMomentumAnalysis;
import com.example.demo.service.SectorMomentumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private SectorMomentumService sectorMomentumService;

    @GetMapping("/dashboard123")
    public String showDashboard(Model model, Authentication authentication) {

        // 1. Fetch sector data (reusing your existing service)
        List<SectorMomentumAnalysis> sectorAnalysis = sectorMomentumService.getAllAnalysis();
        long totalRecords = sectorMomentumService.getTotalCount();

        // 2. Security information (Spring Security)
        String username = (authentication != null) ? authentication.getName() : "Guest";
        String roles = (authentication != null)
                ? authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "))
                : "None";

        // 3. Add everything your template expects
        model.addAttribute("sectorAnalysis", sectorAnalysis);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("username", username);
        model.addAttribute("roles", roles);

        return "dashboard";   // loads src/main/resources/templates/dashboard.html
    }
}