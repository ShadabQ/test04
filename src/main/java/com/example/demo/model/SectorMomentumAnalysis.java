package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sector_momentum_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorMomentumAnalysis {

    @Id
    @Column(name = "sector")
    private String sector;

    @Column(name = "momentum_value")
    private Double momentumValue;

    @Column(name = "description")
    private String description;

    @Column(name = "change_percentage")
    private Double changePercentage;

    @Column(name = "status")
    private String status;
}


