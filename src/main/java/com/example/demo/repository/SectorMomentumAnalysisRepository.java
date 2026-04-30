package com.example.demo.repository;

import com.example.demo.model.SectorMomentumAnalysis;
import com.example.demo.model.SectorMomentumDashboardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SectorMomentumAnalysisRepository extends JpaRepository<SectorMomentumAnalysis, String> {
    List<SectorMomentumAnalysis> findBySector(String sector);
    List<SectorMomentumAnalysis> findByPhase(String phase);
    List<SectorMomentumAnalysis> findByTrend(String trend);

    @Query(value = "SELECT sma.sector AS sector, sma.calculated_date AS calculatedDate, sma.phase AS phase, sma.rs_12m AS rs12m, sma.rsi_14 AS rsi14, sma.adx_14 AS adx14, sma.return_12m AS return12m, sma.trend AS trend, sgl.golden_length AS goldenLength FROM sector_momentum_analysis sma LEFT JOIN sector_golden_lengths sgl ON sma.sector = sgl.sector", nativeQuery = true)
    List<SectorMomentumDashboardProjection> findAllDashboardRecords();

    @Query(value = "SELECT DISTINCT phase FROM sector_momentum_analysis ORDER BY phase", nativeQuery = true)
    List<String> findDistinctPhases();

    @Query(value = "SELECT DISTINCT trend FROM sector_momentum_analysis ORDER BY trend", nativeQuery = true)
    List<String> findDistinctTrends();

    @Query(value = "SELECT sma.sector AS sector, sma.calculated_date AS calculatedDate, sma.phase AS phase, sma.rs_12m AS rs12m, sma.rsi_14 AS rsi14, sma.adx_14 AS adx14, sma.return_12m AS return12m, sma.trend AS trend, sgl.golden_length AS goldenLength FROM sector_momentum_analysis sma LEFT JOIN sector_golden_lengths sgl ON sma.sector = sgl.sector ORDER BY sgl.golden_length ASC NULLS LAST", nativeQuery = true)
    List<SectorMomentumDashboardProjection> findAllDashboardRecordsOrderByGoldenLengthAsc();

    @Query(value = "SELECT sma.sector AS sector, sma.calculated_date AS calculatedDate, sma.phase AS phase, sma.rs_12m AS rs12m, sma.rsi_14 AS rsi14, sma.adx_14 AS adx14, sma.return_12m AS return12m, sma.trend AS trend, sgl.golden_length AS goldenLength FROM sector_momentum_analysis sma LEFT JOIN sector_golden_lengths sgl ON sma.sector = sgl.sector ORDER BY sgl.golden_length DESC NULLS LAST", nativeQuery = true)
    List<SectorMomentumDashboardProjection> findAllDashboardRecordsOrderByGoldenLengthDesc();
}
