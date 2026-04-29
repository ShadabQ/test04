package com.example.demo.repository;

import com.example.demo.model.SectorMomentumAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SectorMomentumAnalysisRepository extends JpaRepository<SectorMomentumAnalysis, Long> {
    List<SectorMomentumAnalysis> findBySector(String sector);
    List<SectorMomentumAnalysis> findByStatusOrderByAnalysisDateDesc(String status);
}
