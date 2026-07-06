package com.sankuai.diancan.replay.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VerificationIdMaskUtilTest {

    @Test
    void shouldMaskLongVerificationCode() {
        assertEquals("ABC****ZYZ", VerificationIdMaskUtil.maskVerificationCode("ABCDEFGHIJZYZ"));
    }

    @Test
    void shouldMaskShortCodeCompletely() {
        assertEquals("****", VerificationIdMaskUtil.maskVerificationCode("AB12"));
    }

    @Test
    void shouldMaskMediumCodeMiddlePart() {
        assertEquals("A****F", VerificationIdMaskUtil.maskVerificationCode("ABCDEF"));
    }
}
