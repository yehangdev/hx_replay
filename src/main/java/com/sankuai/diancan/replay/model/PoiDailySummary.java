package com.sankuai.diancan.replay.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PoiDailySummary {

    private Long poiId;
    private LocalDate statDate;
    private String channel;
    private Integer totalOrders;
    private Integer verifiedOrders;
    private Integer pickupCompleted;
    private Integer abnormalCount;
    private BigDecimal avgReadyOffsetMinutes;
    private BigDecimal avgPickupGapMinutes;
}
