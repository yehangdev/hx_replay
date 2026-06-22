package com.sankuai.diancan.replay.service;

import com.sankuai.diancan.replay.common.ReplayQueryGuard;
import com.sankuai.diancan.replay.model.TraceEvent;
import com.sankuai.diancan.replay.repository.VerificationTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class VerificationTimelineService {

    @Autowired
    private VerificationTraceRepository verificationTraceRepository;

    public List<TraceEvent> buildTimeline(
            String verificationCode, LocalDate startDate, LocalDate endDate) {
        if (verificationCode == null || verificationCode.isBlank()) {
            return Collections.emptyList();
        }
        LocalDate effectiveEnd = endDate == null ? LocalDate.now() : endDate;
        LocalDate effectiveStart = startDate == null ? effectiveEnd.minusDays(30) : startDate;
        ReplayQueryGuard.checkWindow(effectiveStart, effectiveEnd, "timeline");
        return verificationTraceRepository.findTimelineEvents(
                verificationCode, effectiveStart, effectiveEnd, 200);
    }
}
