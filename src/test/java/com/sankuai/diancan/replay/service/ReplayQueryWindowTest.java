package com.sankuai.diancan.replay.service;

import com.sankuai.diancan.replay.repository.PickupMetricRepository;
import com.sankuai.diancan.replay.repository.VerificationTraceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReplayQueryWindowTest {

    @Mock
    private VerificationTraceRepository verificationTraceRepository;

    @Mock
    private PickupMetricRepository pickupMetricRepository;

    @InjectMocks
    private VerificationTraceService verificationTraceService;

    @InjectMocks
    private PickupMetricService pickupMetricService;

    @InjectMocks
    private ReplaySummaryService replaySummaryService;

    @Test
    void shouldRejectReversedTraceWindow() {
        assertThrows(IllegalArgumentException.class, () -> verificationTraceService.queryByRange(
                110100L, LocalDate.of(2026, 9, 2), LocalDate.of(2026, 9, 1), null, 50));
    }

    @Test
    void shouldRejectThirtyOneDayDifferenceForAllQueryTypes() {
        LocalDate startDate = LocalDate.of(2026, 8, 1);
        LocalDate endDate = LocalDate.of(2026, 9, 1);
        assertThrows(IllegalArgumentException.class,
                () -> verificationTraceService.queryByRange(110100L, startDate, endDate, null, 50));
        assertThrows(IllegalArgumentException.class,
                () -> pickupMetricService.queryPickupOrders(110100L, startDate, endDate, 200));
        assertThrows(IllegalArgumentException.class,
                () -> replaySummaryService.dailySummaryList(110100L, startDate, endDate, null));
    }

    @Test
    void shouldAllowThirtyDayDifferenceForAllQueryTypes() {
        LocalDate startDate = LocalDate.of(2026, 8, 1);
        LocalDate endDate = LocalDate.of(2026, 8, 31);
        when(verificationTraceRepository.findByQuery(
                eq(110100L), eq(startDate), eq(endDate), any(), anyInt()))
                .thenReturn(Collections.emptyList());
        when(verificationTraceRepository.findPickupOrders(
                eq(110100L), eq(startDate), eq(endDate), eq(200)))
                .thenReturn(Collections.emptyList());
        when(verificationTraceRepository.findPoiDailySummaries(
                eq(110100L), eq(startDate), eq(endDate), any()))
                .thenReturn(Collections.emptyList());

        verificationTraceService.queryByRange(110100L, startDate, endDate, null, 50);
        pickupMetricService.queryPickupOrders(110100L, startDate, endDate, 200);
        replaySummaryService.dailySummaryList(110100L, startDate, endDate, null);
    }

    @Test
    void shouldDefaultStartRelativeToProvidedEndDate() {
        LocalDate endDate = LocalDate.of(2026, 8, 31);
        LocalDate expectedStart = endDate.minusDays(7);

        verificationTraceService.queryByRange(110100L, null, endDate, null, 50);
        pickupMetricService.queryPickupOrders(110100L, null, endDate, 200);

        verify(verificationTraceRepository).findByQuery(
                eq(110100L), eq(expectedStart), eq(endDate), any(), eq(50));
        verify(verificationTraceRepository).findPickupOrders(
                eq(110100L), eq(expectedStart), eq(endDate), eq(200));
    }
}
