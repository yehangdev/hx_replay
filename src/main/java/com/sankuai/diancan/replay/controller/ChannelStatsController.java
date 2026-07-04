package com.sankuai.diancan.replay.controller;

import com.sankuai.diancan.replay.common.ApiResponse;
import com.sankuai.diancan.replay.model.ChannelAbnormalStat;
import com.sankuai.diancan.replay.service.ChannelStatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/replay/channel-stats")
public class ChannelStatsController {

    private final ChannelStatsService channelStatsService;

    public ChannelStatsController(ChannelStatsService channelStatsService) {
        this.channelStatsService = channelStatsService;
    }

    @GetMapping
    public ApiResponse<List<ChannelAbnormalStat>> stats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String channel) {
        LocalDate statDate = date == null ? LocalDate.now().minusDays(1) : date;
        return ApiResponse.ok(channelStatsService.stats(statDate, channel));
    }
}
