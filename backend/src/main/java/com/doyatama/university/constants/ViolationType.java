package com.doyatama.university.constants;

public class ViolationType {

    // Frontend Detection - Browser events
    public static final String TAB_SWITCH = "TAB_SWITCH";
    public static final String WINDOW_BLUR = "WINDOW_BLUR";
    public static final String COPY_PASTE = "COPY_PASTE";
    public static final String RIGHT_CLICK = "RIGHT_CLICK";
    public static final String KEYBOARD_SHORTCUT = "KEYBOARD_SHORTCUT";
    public static final String FULLSCREEN_EXIT = "FULLSCREEN_EXIT";
    public static final String BROWSER_DEV_TOOLS = "BROWSER_DEV_TOOLS";
    public static final String PRINT_SCREEN = "PRINT_SCREEN";
    public static final String ALT_TAB = "ALT_TAB";
    public static final String CTRL_C_V = "CTRL_C_V";

    // Backend Analysis - Behavioral only (SIMPLIFIED)
    public static final String SUSPICIOUS_TIMING = "SUSPICIOUS_TIMING";
    public static final String SUSPICIOUS_BEHAVIOR = "SUSPICIOUS_BEHAVIOR"; // NEW - general behavioral
    public static final String FAST_COMPLETION = "FAST_COMPLETION";
    public static final String EXCESSIVE_CHANGES = "EXCESSIVE_CHANGES";
    public static final String IDENTICAL_TIMING = "IDENTICAL_TIMING";

    // System Detection
    public static final String MULTIPLE_SESSIONS = "MULTIPLE_SESSIONS";
    public static final String BROWSER_MANIPULATION = "BROWSER_MANIPULATION";
    public static final String SCREEN_RESOLUTION_CHANGE = "SCREEN_RESOLUTION_CHANGE";

    // REMOVED: All pattern-specific violations
    // REMOVED: MULTI_PATTERN_SUSPICIOUS, COCOK_PATTERN_SUSPICIOUS, etc.

    private ViolationType() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    public static boolean isFrontendDetectable(String violationType) {
        switch (violationType) {
            case TAB_SWITCH:
            case WINDOW_BLUR:
            case COPY_PASTE:
            case RIGHT_CLICK:
            case KEYBOARD_SHORTCUT:
            case FULLSCREEN_EXIT:
            case BROWSER_DEV_TOOLS:
            case PRINT_SCREEN:
            case ALT_TAB:
            case CTRL_C_V:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBehavioralAnalysis(String violationType) {
        switch (violationType) {
            case SUSPICIOUS_TIMING:
            case SUSPICIOUS_BEHAVIOR:
            case FAST_COMPLETION:
            case EXCESSIVE_CHANGES:
            case IDENTICAL_TIMING:
                return true;
            default:
                return false;
        }
    }

    public static String getDefaultSeverity(String violationType) {
        switch (violationType) {
            case BROWSER_DEV_TOOLS:
            case ALT_TAB:
            case MULTIPLE_SESSIONS:
                return SeverityLevel.HIGH;
            case COPY_PASTE:
            case CTRL_C_V:
            case FULLSCREEN_EXIT:
            case SUSPICIOUS_BEHAVIOR:
                return SeverityLevel.MEDIUM;
            case RIGHT_CLICK:
            case WINDOW_BLUR:
            case SUSPICIOUS_TIMING:
                return SeverityLevel.LOW;
            default:
                return SeverityLevel.MEDIUM;
        }
    }
}