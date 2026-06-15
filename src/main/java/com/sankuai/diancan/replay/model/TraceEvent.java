package com.sankuai.diancan.replay.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TraceEvent {

    private String eventId;
    private String eventType;
    private String eventName;
    private LocalDateTime eventTime;
    private Integer offsetSeconds;
    private String detail;
}
