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
     * Fetch analysis by status
     */
    public List<SectorMomentumAnalysis> getAnalysisByStatus(String status) {
        return repository.findByStatusOrderByAnalysisDateDesc(status);
    }

    /**
     * Get analysis record by ID
     */
    public SectorMomentumAnalysis getAnalysisById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Get count of all records
     */
    public long getTotalCount() {
        return repository.count();
    }
}
