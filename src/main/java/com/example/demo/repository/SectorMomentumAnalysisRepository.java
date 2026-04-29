package com.example.demo.repository;

import com.example.demo.model.SectorMomentumAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SectorMomentumAnalysisRepository extends JpaRepository<SectorMomentumAnalysis, String> {
    List<SectorMomentumAnalysis> findBySector(String sector);
    List<SectorMomentumAnalysis> findByPhase(String phase);
    List<SectorMomentumAnalysis> findByTrend(String trend);
}
