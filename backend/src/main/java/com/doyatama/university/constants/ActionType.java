package com.doyatama.university.constants;

public class ActionType {
    public static final String WARNING = "WARNING";
    public static final String LOCK_EXAM = "LOCK_EXAM";
    public static final String TERMINATE_SESSION = "TERMINATE_SESSION";
    public static final String FLAGGED_FOR_REVIEW = "FLAGGED_FOR_REVIEW";
    public static final String AUTO_SUBMIT = "AUTO_SUBMIT";
    public static final String NOTIFY_PROCTOR = "NOTIFY_PROCTOR";
    public static final String REDUCE_TIME = "REDUCE_TIME";

    private ActionType() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    public static boolean isImmediateAction(String actionType) {
        switch (actionType) {
            case LOCK_EXAM:
            case TERMINATE_SESSION:
            case AUTO_SUBMIT:
                return true;
            default:
                return false;
        }
    }
}