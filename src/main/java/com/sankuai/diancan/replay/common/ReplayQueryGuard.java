package com.sankuai.diancan.replay.common;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class ReplayQueryGuard {

    public static final int MAX_WINDOW_DAYS = 30;

    private ReplayQueryGuard() {
    }

    public static void checkWindow(LocalDate startDate, LocalDate endDate, String label) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(label + " start and end dates are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(label + " startDate must not be after endDate");
        }
        if (ChronoUnit.DAYS.between(startDate, endDate) > MAX_WINDOW_DAYS) {
            throw new IllegalArgumentException(label + " range must not exceed 31 calendar days");
        }
    }
}
