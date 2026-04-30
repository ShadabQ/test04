package com.example.demo.service;

import com.example.demo.model.SectorMomentumAnalysis;
import com.example.demo.model.SectorMomentumDashboardProjection;
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
     * Fetch all enriched dashboard records with golden length
     */
    public List<SectorMomentumDashboardProjection> getDashboardAnalysis() {
        return repository.findAllDashboardRecords();
    }

    /**
     * Fetch all enriched dashboard records sorted by golden length
     */
    public List<SectorMomentumDashboardProjection> getDashboardAnalysisSorted(String sortDirection) {
        if ("asc".equalsIgnoreCase(sortDirection)) {
            return repository.findAllDashboardRecordsOrderByGoldenLengthAsc();
        }
        return repository.findAllDashboardRecordsOrderByGoldenLengthDesc();
    }

    /**
     * Fetch all enriched dashboard records sorted and filtered by phase/trend
     */
    public List<SectorMomentumDashboardProjection> getDashboardAnalysisSortedAndFiltered(String sortDirection, String phase, String trend) {
        List<SectorMomentumDashboardProjection> records = getDashboardAnalysisSorted(sortDirection);

        return records.stream()
            .filter(record -> phase == null || "all".equalsIgnoreCase(phase) || phase.equalsIgnoreCase(record.getPhase()))
            .filter(record -> trend == null || "all".equalsIgnoreCase(trend) || trend.equalsIgnoreCase(record.getTrend()))
            .toList();
    }

    public List<String> getAvailablePhases() {
        return repository.findDistinctPhases();
    }

    public List<String> getAvailableTrends() {
        return repository.findDistinctTrends();
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
