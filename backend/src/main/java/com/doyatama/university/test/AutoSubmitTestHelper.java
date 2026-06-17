package com.doyatama.university.test;

import com.doyatama.university.model.UjianSession;
import com.doyatama.university.model.Ujian;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple test class untuk validate auto-submit logic
 * Dapat dijalankan manual untuk testing
 */
public class AutoSubmitTestHelper {

    /**
     * Test helper untuk simulate expired session
     */
    public static UjianSession createExpiredTestSession() {
        // Create mock ujian
        Ujian ujian = new Ujian();
        ujian.setIdUjian("test-ujian-001");
        ujian.setNamaUjian("Test Ujian Auto Submit");
        ujian.setDurasiMenit(60); // 60 minutes duration

        // Set ujian end time 1 hour ago (expired)
        Instant oneHourAgo = Instant.now().minus(60, ChronoUnit.MINUTES);
        ujian.setWaktuSelesaiOtomatis(oneHourAgo);

        // Create mock session
        UjianSession session = new UjianSession();
        session.setIdSession("test-session-001");
        session.setSessionId("sess-test-001");
        session.setIdUjian("test-ujian-001");
        session.setIdPeserta("test-student-001");
        session.setIdSchool("test-school-001");
        session.setStatus("ACTIVE");
        session.setIsSubmitted(false);

        // Set start time 2 hours ago (so it's definitely expired)
        Instant twoHoursAgo = Instant.now().minus(120, ChronoUnit.MINUTES);
        session.setStartTime(twoHoursAgo);
        session.setTimeRemaining(0); // No time left

        // Add some mock answers
        Map<String, Object> answers = new HashMap<>();
        answers.put("soal-001", "A");
        answers.put("soal-002", "B");
        answers.put("soal-003", "C");
        session.setAnswers(answers);

        session.setUjian(ujian);
        session.setCreatedAt(twoHoursAgo);
        session.setUpdatedAt(Instant.now().minus(30, ChronoUnit.MINUTES));

        return session;
    }

    /**
     * Test helper untuk simulate active session (not expired)
     */
    public static UjianSession createActiveTestSession() {
        // Create mock ujian
        Ujian ujian = new Ujian();
        ujian.setIdUjian("test-ujian-002");
        ujian.setNamaUjian("Test Ujian Aktif");
        ujian.setDurasiMenit(60); // 60 minutes duration

        // Set ujian end time 30 minutes from now (still active)
        Instant thirtyMinutesFromNow = Instant.now().plus(30, ChronoUnit.MINUTES);
        ujian.setWaktuSelesaiOtomatis(thirtyMinutesFromNow);

        // Create mock session
        UjianSession session = new UjianSession();
        session.setIdSession("test-session-002");
        session.setSessionId("sess-test-002");
        session.setIdUjian("test-ujian-002");
        session.setIdPeserta("test-student-002");
        session.setIdSchool("test-school-001");
        session.setStatus("ACTIVE");
        session.setIsSubmitted(false);

        // Set start time 30 minutes ago (30 minutes remaining)
        Instant thirtyMinutesAgo = Instant.now().minus(30, ChronoUnit.MINUTES);
        session.setStartTime(thirtyMinutesAgo);
        session.setTimeRemaining(1800); // 30 minutes = 1800 seconds

        // Add some mock answers
        Map<String, Object> answers = new HashMap<>();
        answers.put("soal-001", "D");
        answers.put("soal-002", "A");
        session.setAnswers(answers);

        session.setUjian(ujian);
        session.setCreatedAt(thirtyMinutesAgo);
        session.setUpdatedAt(Instant.now());

        return session;
    }

    /**
     * Validate auto-submit detection logic
     */
    public static void validateAutoSubmitLogic() {
        System.out.println("=== AUTO-SUBMIT LOGIC VALIDATION ===");

        // Test 1: Expired session should be auto-submitted
        UjianSession expiredSession = createExpiredTestSession();
        boolean shouldAutoSubmit1 = isSessionExpiredMock(expiredSession);
        System.out.println("Test 1 - Expired Session: " + (shouldAutoSubmit1 ? "PASS ✅" : "FAIL ❌"));

        // Test 2: Active session should NOT be auto-submitted
        UjianSession activeSession = createActiveTestSession();
        boolean shouldAutoSubmit2 = isSessionExpiredMock(activeSession);
        System.out.println("Test 2 - Active Session: " + (!shouldAutoSubmit2 ? "PASS ✅" : "FAIL ❌"));

        System.out.println("\n=== TIME CALCULATION VALIDATION ===");

        // Test 3: Time remaining calculation
        Integer timeRemaining1 = calculateActualTimeRemainingMock(expiredSession);
        Integer timeRemaining2 = calculateActualTimeRemainingMock(activeSession);

        System.out.println("Expired Session Time Remaining: " + timeRemaining1 + " seconds");
        System.out.println("Active Session Time Remaining: " + timeRemaining2 + " seconds");

        System.out.println("Time Calculation: " +
                (timeRemaining1 <= 0 && timeRemaining2 > 0 ? "PASS ✅" : "FAIL ❌"));
    }

    /**
     * Mock implementation of isSessionExpired logic
     */
    private static boolean isSessionExpiredMock(UjianSession session) {
        try {
            // Check time remaining
            Integer timeRemaining = calculateActualTimeRemainingMock(session);
            if (timeRemaining != null && timeRemaining <= 0) {
                return true;
            }

            // Check ujian end time
            if (session.getUjian() != null && session.getUjian().getWaktuSelesaiOtomatis() != null) {
                Instant now = Instant.now();
                if (now.isAfter(session.getUjian().getWaktuSelesaiOtomatis())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            System.err.println("Error in isSessionExpiredMock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mock implementation of calculateActualTimeRemaining logic
     */
    private static Integer calculateActualTimeRemainingMock(UjianSession session) {
        if (session.getStartTime() == null || session.getUjian() == null
                || session.getUjian().getDurasiMenit() == null) {
            return session.getTimeRemaining();
        }

        Instant now = Instant.now();
        long elapsedSeconds = ChronoUnit.SECONDS.between(session.getStartTime(), now);
        int totalDurationSeconds = session.getUjian().getDurasiMenit() * 60;

        return Math.max(0, totalDurationSeconds - (int) elapsedSeconds);
    }

    /**
     * Main method untuk run test manual
     */
    public static void main(String[] args) {
        System.out.println("🔧 Testing Auto-Submit Logic...\n");
        validateAutoSubmitLogic();
        System.out.println("\n✅ Auto-Submit Logic Test Complete!");
    }
}