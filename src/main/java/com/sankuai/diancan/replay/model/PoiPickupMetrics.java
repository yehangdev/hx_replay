package com.sankuai.diancan.replay.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PoiPickupMetrics {

    private Long poiId;
    private LocalDate statDate;
    private Integer etlVersion;
    private Integer totalOrders;
    private Integer completedPickup;
    private Integer abandonedPickup;
    private BigDecimal avgReadyOffsetMinutes;
    private BigDecimal avgPickupGapMinutes;
    private Integer noShowCount;
}
