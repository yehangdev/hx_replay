package com.sankuai.diancan.replay.controller;

import com.sankuai.diancan.replay.common.ApiResponse;
import com.sankuai.diancan.replay.model.TraceEvent;
import com.sankuai.diancan.replay.service.VerificationTimelineService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trace/timeline")
public class VerificationTimelineController {

    private final VerificationTimelineService verificationTimelineService;

    public VerificationTimelineController(VerificationTimelineService verificationTimelineService) {
        this.verificationTimelineService = verificationTimelineService;
    }

    @GetMapping("/{verificationCode}")
    public ApiResponse<List<TraceEvent>> timeline(
            @PathVariable String verificationCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.ok(
                verificationTimelineService.buildTimeline(verificationCode, startDate, endDate));
    }
}
