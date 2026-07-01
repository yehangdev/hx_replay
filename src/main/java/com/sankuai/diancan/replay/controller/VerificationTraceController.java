package com.sankuai.diancan.replay.controller;

import com.sankuai.diancan.replay.common.ApiResponse;
import com.sankuai.diancan.replay.model.VerificationTrace;
import com.sankuai.diancan.replay.service.VerificationTraceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trace")
public class VerificationTraceController {

    private final VerificationTraceService verificationTraceService;

    public VerificationTraceController(VerificationTraceService verificationTraceService) {
        this.verificationTraceService = verificationTraceService;
    }

    @GetMapping("/verification/{verificationCode}")
    public ApiResponse<List<VerificationTrace>> trace(
            @PathVariable String verificationCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.ok(
                verificationTraceService.queryTrace(verificationCode, startDate, endDate));
    }

    @GetMapping("/list")
    public ApiResponse<List<VerificationTrace>> list(
            @RequestParam Long poiId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String channel,
            @RequestParam(defaultValue = "50") Integer limit) {
        return ApiResponse.ok(verificationTraceService.queryByRange(
                poiId, startDate, endDate, channel, limit));
    }
}
