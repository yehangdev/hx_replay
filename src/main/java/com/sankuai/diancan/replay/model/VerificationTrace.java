package com.sankuai.diancan.replay.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VerificationTrace {

    private String verificationCode;
    private String dealGroupId;
    private Long poiId;
    private String channel;
    private String pickupCode;
    private String cabinetNo;
    private LocalDateTime merchantReadyAt;
    private LocalDateTime pickedAt;
    private Integer prepCompletedOffset;
    private Integer verificationStatus;
    private String sourceSystem;
    private LocalDateTime createTime;
    private LocalDate dt;
}
