package com.example.demo.controller;

import com.example.demo.model.SectorMomentumAnalysis;
import com.example.demo.service.SectorMomentumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sector")
public class SectorDataController {

    @Autowired
    private SectorMomentumService sectorMomentumService;

    @GetMapping("/all")
    public Map<String, Object> getAllSectorData() {
        try {
            List<SectorMomentumAnalysis> data = sectorMomentumService.getAllAnalysis();
            return Map.of(
                "success", true,
                "count", data.size(),
                "data", data
            );
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "error", e.getMessage()
            );
        }
    }

    @GetMapping("/by-sector/{sector}")
    public Map<String, Object> getSectorByName(@PathVariable String sector) {
        try {
            List<SectorMomentumAnalysis> data = sectorMomentumService.getAnalysisBySector(sector);
            return Map.of(
                "success", true,
                "sector", sector,
                "count", data.size(),
                "data", data
            );
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "error", e.getMessage()
            );
        }
    }

    @GetMapping("/by-status/{status}")
    public Map<String, Object> getSectorByStatus(@PathVariable String status) {
        try {
            List<SectorMomentumAnalysis> data = sectorMomentumService.getAnalysisByStatus(status);
            return Map.of(
                "success", true,
                "status", status,
                "count", data.size(),
                "data", data
            );
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "error", e.getMessage()
            );
        }
    }

    @GetMapping("/total-count")
    public Map<String, Object> getTotalCount() {
        try {
            long count = sectorMomentumService.getTotalCount();
            return Map.of(
                "success", true,
                "totalRecords", count
            );
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "error", e.getMessage()
            );
        }
    }
}
