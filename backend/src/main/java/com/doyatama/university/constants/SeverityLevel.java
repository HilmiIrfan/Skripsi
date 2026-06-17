package com.doyatama.university.constants;

public class SeverityLevel {
    public static final String LOW = "LOW";
    public static final String MEDIUM = "MEDIUM";
    public static final String HIGH = "HIGH";
    public static final String CRITICAL = "CRITICAL";

    private SeverityLevel() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    public static int getSeverityWeight(String severity) {
        switch (severity) {
            case LOW:
                return 1;
            case MEDIUM:
                return 2;
            case HIGH:
                return 3;
            case CRITICAL:
                return 4;
            default:
                return 2;
        }
    }

    public static boolean shouldAutoSubmit(String severity) {
        return CRITICAL.equals(severity);
    }
}