package com.example.demo.controller;

import com.example.demo.model.SectorMomentumAnalysis;
import com.example.demo.model.SectorMomentumDashboardProjection;
import com.example.demo.service.SectorMomentumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String dashboard(
            @RequestParam(name = "sort", defaultValue = "desc") String sort,
            @RequestParam(name = "phase", defaultValue = "all") String phase,
            @RequestParam(name = "trend", defaultValue = "all") String trend,
            Model model,
            Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority().replace("ROLE_", ""))
            .collect(Collectors.joining(", "));

        model.addAttribute("username", username);
        model.addAttribute("roles", roles);
        model.addAttribute("sort", sort);
        model.addAttribute("selectedPhase", phase);
        model.addAttribute("selectedTrend", trend);
        model.addAttribute("phases", sectorMomentumService.getAvailablePhases());
        model.addAttribute("trends", sectorMomentumService.getAvailableTrends());
        
        // Fetch sector momentum data for dashboard
        List<SectorMomentumDashboardProjection> analysis = sectorMomentumService.getDashboardAnalysisSortedAndFiltered(sort, phase, trend);
        model.addAttribute("sectorAnalysis", analysis);
        model.addAttribute("totalRecords", analysis.size());
        
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
