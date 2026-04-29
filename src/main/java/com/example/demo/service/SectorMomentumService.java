package com.example.demo.service;

import com.example.demo.model.SectorMomentumAnalysis;
import com.example.demo.repository.SectorMomentumAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SectorMomentumService {

    @Autowired
    private SectorMomentumAnalysisRepository repository;

    /**
     * Fetch all sector momentum analysis records
     */
    public List<SectorMomentumAnalysis> getAllAnalysis() {
        return repository.findAll();
    }

    /**
     * Fetch analysis by specific sector
     */
    public List<SectorMomentumAnalysis> getAnalysisBySector(String sector) {
        return repository.findBySector(sector);
    }

    /**
     * Fetch analysis by phase
     */
    public List<SectorMomentumAnalysis> getAnalysisByPhase(String phase) {
        return repository.findByPhase(phase);
    }

    /**
     * Fetch analysis by trend
     */
    public List<SectorMomentumAnalysis> getAnalysisByTrend(String trend) {
        return repository.findByTrend(trend);
    }

    /**
     * Get analysis record by sector ID
     */
    public SectorMomentumAnalysis getAnalysisById(String sector) {
        return repository.findById(sector).orElse(null);
    }

    /**
     * Get count of all records
     */
    public long getTotalCount() {
        return repository.count();
    }
}
