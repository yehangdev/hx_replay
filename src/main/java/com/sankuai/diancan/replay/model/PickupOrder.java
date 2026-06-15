package com.sankuai.diancan.replay.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PickupOrder {

    private String pickupCode;
    private String orderNo;
    private Long poiId;
    private String merchantId;
    private String cabinetNo;
    private String sourceSystem;
    private LocalDateTime orderCreateTime;
    private LocalDateTime merchantReadyAt;
    private LocalDateTime pickedAt;
    private String channel;
    private LocalDate dt;
}
