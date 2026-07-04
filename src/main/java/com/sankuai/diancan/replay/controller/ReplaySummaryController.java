package com.sankuai.diancan.replay.controller;

import com.sankuai.diancan.replay.common.ApiResponse;
import com.sankuai.diancan.replay.model.PoiDailySummary;
import com.sankuai.diancan.replay.service.ReplaySummaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/replay/summary")
public class ReplaySummaryController {

    private final ReplaySummaryService replaySummaryService;

    public ReplaySummaryController(ReplaySummaryService replaySummaryService) {
        this.replaySummaryService = replaySummaryService;
    }

    @GetMapping("/poi/{poiId}")
    public ApiResponse<List<PoiDailySummary>> poiSummary(
            @PathVariable Long poiId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String channel) {
        LocalDate statDate = date == null ? LocalDate.now().minusDays(1) : date;
        return ApiResponse.ok(
                replaySummaryService.dailySummaryList(poiId, statDate, statDate, channel));
    }

    @GetMapping("/range")
    public ApiResponse<List<PoiDailySummary>> range(
            @RequestParam Long poiId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String channel) {
        return ApiResponse.ok(
                replaySummaryService.dailySummaryList(poiId, startDate, endDate, channel));
    }
}
