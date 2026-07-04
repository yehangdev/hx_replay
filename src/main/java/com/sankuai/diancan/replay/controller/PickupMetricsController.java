package com.sankuai.diancan.replay.controller;

import com.sankuai.diancan.replay.common.ApiResponse;
import com.sankuai.diancan.replay.model.PickupOrder;
import com.sankuai.diancan.replay.model.PoiPickupMetrics;
import com.sankuai.diancan.replay.service.PickupMetricService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/poi")
public class PickupMetricsController {

    private final PickupMetricService pickupMetricService;

    public PickupMetricsController(PickupMetricService pickupMetricService) {
        this.pickupMetricService = pickupMetricService;
    }

    @GetMapping("/{poiId}/pickup-metrics")
    public ResponseEntity<ApiResponse<PoiPickupMetrics>> pickupMetrics(
            @PathVariable Long poiId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Optional<PoiPickupMetrics> metrics = pickupMetricService.queryPoiMetrics(poiId, date);
        if (!metrics.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("poi pickup metrics not found"));
        }
        return ResponseEntity.ok(ApiResponse.ok(metrics.get()));
    }

    @GetMapping("/{poiId}/pickup-orders")
    public ApiResponse<List<PickupOrder>> pickupOrders(
            @PathVariable Long poiId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "200") Integer limit) {
        return ApiResponse.ok(pickupMetricService.queryPickupOrders(poiId, startDate, endDate, limit));
    }
}
