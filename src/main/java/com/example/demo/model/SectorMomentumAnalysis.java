package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sector_momentum_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorMomentumAnalysis {

    @Id
    @Column(name = "sector")
    private String sector;

    @Column(name = "calculated_date")
    private LocalDate calculatedDate;

    @Column(name = "phase")
    private String phase;

    @Column(name = "rs_12m")
    private BigDecimal rs12m;

    @Column(name = "rsi_14")
    private BigDecimal rsi14;

    @Column(name = "adx_14")
    private BigDecimal adx14;

    @Column(name = "return_12m")
    private String return12m;

    @Column(name = "trend")
    private String trend;
}


