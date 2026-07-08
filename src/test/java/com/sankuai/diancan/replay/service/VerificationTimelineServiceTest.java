package com.sankuai.diancan.replay.service;

import com.sankuai.diancan.replay.model.TraceEvent;
import com.sankuai.diancan.replay.repository.VerificationTraceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationTimelineServiceTest {

    @Mock
    private VerificationTraceRepository verificationTraceRepository;

    @InjectMocks
    private VerificationTimelineService verificationTimelineService;

    @Test
    void shouldReturnEventsFromRepositoryOrderedByEventTime() {
        TraceEvent event = TraceEvent.builder()
                .eventId("EVT-20260907-0001")
                .eventType("MERCHANT_READY")
                .eventName("商家出餐完成")
                .eventTime(LocalDateTime.of(2026, 9, 7, 12, 8))
                .offsetSeconds(480)
                .build();
        when(verificationTraceRepository.findTimelineEvents(
                eq("ABC123456789"), any(LocalDate.class), any(LocalDate.class), anyInt()))
                .thenReturn(List.of(event));

        List<TraceEvent> events = verificationTimelineService.buildTimeline("ABC123456789", null, null);

        assertEquals(1, events.size());
        assertEquals("MERCHANT_READY", events.get(0).getEventType());
        assertEquals(480, events.get(0).getOffsetSeconds());
    }
}
