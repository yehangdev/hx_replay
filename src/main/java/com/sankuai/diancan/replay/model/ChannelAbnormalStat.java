package com.sankuai.diancan.replay.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ChannelAbnormalStat {

    private LocalDate statDate;
    private String channel;
    private Long totalVerification;
    private Long abnormalCount;
    private Long missingPickupCount;
}
