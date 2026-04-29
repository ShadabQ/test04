package com.example.demo.controller;

import com.example.demo.model.SectorMomentumAnalysis;
import com.example.demo.service.SectorMomentumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private SectorMomentumService sectorMomentumService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "Guest";
        model.addAttribute("username", username);
        
        // Fetch sector momentum data
        List<SectorMomentumAnalysis> analysis = sectorMomentumService.getAllAnalysis();
        model.addAttribute("sectorAnalysis", analysis);
        model.addAttribute("analysisCount", analysis.size());
        
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
        
        // Fetch sector momentum data for dashboard
        List<SectorMomentumAnalysis> analysis = sectorMomentumService.getAllAnalysis();
        model.addAttribute("sectorAnalysis", analysis);
        model.addAttribute("totalRecords", sectorMomentumService.getTotalCount());
        
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
