package com.sankuai.diancan.replay.util;

public final class VerificationIdMaskUtil {

    private VerificationIdMaskUtil() {
    }

    public static String maskVerificationCode(String code) {
        if (code == null || code.isBlank()) {
            return code;
        }
        int length = code.length();
        if (length <= 4) {
            return "****";
        }
        if (length <= 7) {
            return code.substring(0, 1) + "****" + code.substring(length - 1);
        }
        return code.substring(0, 3) + "****" + code.substring(length - 3);
    }
}
