package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SectorMomentumDashboardProjection {

    String getSector();

    LocalDate getCalculatedDate();

    String getPhase();

    BigDecimal getRs12m();

    BigDecimal getRsi14();

    BigDecimal getAdx14();

    String getReturn12m();

    String getTrend();

    Integer getGoldenLength();
}
