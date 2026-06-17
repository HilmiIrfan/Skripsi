package com.doyatama.university.service;

import com.doyatama.university.model.UjianSession;
import com.doyatama.university.model.HasilUjian;
import com.doyatama.university.repository.UjianSessionRepository;
import com.doyatama.university.payload.UjianSessionRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service untuk menangani auto-submit ujian ketika waktu habis
 * Menggunakan scheduled task untuk memantau session yang timeout
 */
@Service
public class UjianAutoSubmitService {

    private static final Logger logger = LoggerFactory.getLogger(UjianAutoSubmitService.class);

    @Autowired
    private UjianSessionRepository ujianSessionRepository;

    @Autowired
    private UjianSessionService ujianSessionService;

    // HasilUjianService will be used via UjianSessionService

    /**
     * Scheduled task yang berjalan setiap 30 detik untuk cek session yang timeout
     * Otomatis submit ujian yang sudah melewati batas waktu
     */
    @Scheduled(fixedDelay = 30000) // Run every 30 seconds
    public void autoSubmitExpiredSessions() {
        try {
            logger.debug("Running auto-submit check for expired sessions");

            // Ambil semua active sessions
            List<UjianSession> activeSessions = ujianSessionRepository.findAllActiveSessions(1000);

            if (activeSessions.isEmpty()) {
                logger.debug("No active sessions found for auto-submit check");
                return;
            }

            int processedCount = 0;
            int autoSubmittedCount = 0;

            for (UjianSession session : activeSessions) {
                try {
                    processedCount++;

                    // Skip jika sudah di-submit
                    if (session.getIsSubmitted()) {
                        continue;
                    }

                    // Cek apakah session sudah timeout
                    if (isSessionExpired(session)) {
                        logger.info("Auto-submitting expired session: {} for ujian: {} participant: {}",
                                session.getSessionId(), session.getIdUjian(), session.getIdPeserta());

                        // Perform auto-submit
                        performAutoSubmit(session);
                        autoSubmittedCount++;

                        logger.info("Successfully auto-submitted session: {}", session.getSessionId());
                    }

                } catch (Exception e) {
                    logger.error("Error auto-submitting session {}: {}", session.getSessionId(), e.getMessage(), e);
                    // Continue dengan session lainnya
                }
            }

            if (autoSubmittedCount > 0) {
                logger.info("Auto-submit check completed: {} sessions processed, {} auto-submitted",
                        processedCount, autoSubmittedCount);
            } else {
                logger.debug("Auto-submit check completed: {} sessions processed, no auto-submit needed",
                        processedCount);
            }

        } catch (Exception e) {
            logger.error("Error in auto-submit scheduler: {}", e.getMessage(), e);
        }
    }

    /**
     * Cek apakah session sudah expired (waktu habis)
     */
    private boolean isSessionExpired(UjianSession session) {
        try {
            // Hitung actual time remaining
            Integer timeRemaining = calculateActualTimeRemaining(session);

            if (timeRemaining != null && timeRemaining <= 0) {
                logger.debug("Session {} expired: time remaining = {}", session.getSessionId(), timeRemaining);
                return true;
            }

            // Cek ujian end time jika di-set
            if (session.getUjian() != null && session.getUjian().getWaktuSelesaiOtomatis() != null) {
                Instant now = Instant.now();
                if (now.isAfter(session.getUjian().getWaktuSelesaiOtomatis())) {
                    logger.debug("Session {} expired: past ujian end time {}",
                            session.getSessionId(), session.getUjian().getWaktuSelesaiOtomatis());
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            logger.warn("Error checking session expiry for {}: {}", session.getSessionId(), e.getMessage());
            return false;
        }
    }

    /**
     * Hitung actual time remaining untuk session
     */
    private Integer calculateActualTimeRemaining(UjianSession session) {
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
     * Perform auto-submit untuk session yang expired
     */
    private void performAutoSubmit(UjianSession session) throws IOException {
        try {
            // Create submit request
            UjianSessionRequest.SubmitUjianRequest submitRequest = new UjianSessionRequest.SubmitUjianRequest();
            submitRequest.setSessionId(session.getSessionId());
            submitRequest.setIdUjian(session.getIdUjian());
            submitRequest.setIdPeserta(session.getIdPeserta());
            submitRequest.setAnswers(session.getAnswers() != null ? session.getAnswers() : new HashMap<>());
            submitRequest.setIsAutoSubmit(true);
            // Auto submit reason will be handled in the service layer
            submitRequest.setFinalTimeRemaining(0);

            // Submit via UjianSessionService
            HasilUjian hasilUjian = ujianSessionService.submitUjian(submitRequest, session.getIdSchool());

            logger.info("Auto-submit completed for session: {}, created result: {}",
                    session.getSessionId(), hasilUjian.getIdHasilUjian());

        } catch (Exception e) {
            logger.error("Failed to perform auto-submit for session {}: {}", session.getSessionId(), e.getMessage(), e);

            // Fallback: mark session as expired without creating result
            try {
                session.setStatus("EXPIRED");
                session.setEndTime(Instant.now());
                session.setIsSubmitted(true);
                session.setIsAutoSubmit(true);
                session.setUpdatedAt(Instant.now());
                ujianSessionRepository.save(session);

                logger.warn("Session {} marked as expired due to auto-submit failure", session.getSessionId());
            } catch (Exception fallbackError) {
                logger.error("Failed to mark session {} as expired: {}", session.getSessionId(),
                        fallbackError.getMessage());
            }
        }
    }

    /**
     * Manual trigger untuk auto-submit session tertentu (untuk testing/admin)
     */
    public Map<String, Object> forceAutoSubmitSession(String sessionId, String adminUserId) throws IOException {
        Map<String, Object> result = new HashMap<>();

        try {
            UjianSession session = ujianSessionRepository.findBySessionId(sessionId);
            if (session == null) {
                throw new IllegalArgumentException("Session tidak ditemukan: " + sessionId);
            }

            if (session.getIsSubmitted()) {
                throw new IllegalArgumentException("Session sudah di-submit: " + sessionId);
            }

            logger.info("Force auto-submitting session {} by admin: {}", sessionId, adminUserId);

            performAutoSubmit(session);

            result.put("success", true);
            result.put("message", "Session berhasil di-auto-submit");
            result.put("sessionId", sessionId);
            result.put("adminUserId", adminUserId);
            result.put("timestamp", Instant.now());

        } catch (Exception e) {
            logger.error("Error in force auto-submit: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * Get statistik auto-submit untuk monitoring
     */
    public Map<String, Object> getAutoSubmitStatistics() throws IOException {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Count total sessions by status
            List<UjianSession> allSessions = ujianSessionRepository.findAll(10000);

            long totalSessions = allSessions.size();
            long activeSessions = allSessions.stream()
                    .filter(s -> "ACTIVE".equals(s.getStatus()) && !s.getIsSubmitted())
                    .count();

            long completedSessions = allSessions.stream()
                    .filter(s -> s.getIsSubmitted())
                    .count();

            long autoSubmittedSessions = allSessions.stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsAutoSubmit()))
                    .count();

            stats.put("totalSessions", totalSessions);
            stats.put("activeSessions", activeSessions);
            stats.put("completedSessions", completedSessions);
            stats.put("autoSubmittedSessions", autoSubmittedSessions);
            stats.put("autoSubmitPercentage",
                    totalSessions > 0 ? (double) autoSubmittedSessions / totalSessions * 100 : 0);

            // Find sessions at risk of timeout (< 5 minutes remaining)
            long sessionsAtRisk = 0;
            for (UjianSession session : allSessions) {
                if ("ACTIVE".equals(session.getStatus()) && !session.getIsSubmitted()) {
                    Integer timeRemaining = calculateActualTimeRemaining(session);
                    if (timeRemaining != null && timeRemaining > 0 && timeRemaining < 300) { // < 5 minutes
                        sessionsAtRisk++;
                    }
                }
            }

            stats.put("sessionsAtRisk", sessionsAtRisk);
            stats.put("lastCheckTime", Instant.now());

        } catch (Exception e) {
            logger.error("Error getting auto-submit statistics: {}", e.getMessage());
            stats.put("error", e.getMessage());
        }

        return stats;
    }
}