package com.example.payment.utils;

public class IAppendableReferenceUtils {
    private static String[] getParts(String reference) {
        if (reference == null || !reference.contains("_")) {
            return null;
        }
        String[] parts = reference.split("_");
        if (parts.length != 2) return null;

        return parts;
    }

    public static Long getIdFrom(String reference) {
        try {
            return Long.parseLong(getParts(reference)[0]);
        } catch (Exception ignored) {
        }
        return 0L;
    }

    public static String getReferenceFrom(String reference) {
        return getParts(reference)[1];
    }
}
