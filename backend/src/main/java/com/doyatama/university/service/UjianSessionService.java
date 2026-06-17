package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.UjianSession;
import com.doyatama.university.model.HasilUjian;
import com.doyatama.university.model.Ujian;
import com.doyatama.university.model.User;
import com.doyatama.university.model.School;
import com.doyatama.university.model.BankSoalUjian;
import com.doyatama.university.payload.UjianSessionRequest;
import com.doyatama.university.repository.UjianSessionRepository;
import com.doyatama.university.repository.HasilUjianRepository;
import com.doyatama.university.repository.UjianRepository;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;

import org.springframework.beans.factory.annotation.Autowired; // PERBAIKAN: Import @Autowired
import org.springframework.stereotype.Service; // PERBAIKAN: Import @Service

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // PERBAIKAN: Tambah @Service annotation
public class UjianSessionService {

    private static final Logger logger = LoggerFactory.getLogger(UjianSessionService.class);

    // PERBAIKAN: Gunakan @Autowired untuk dependency injection
    @Autowired
    private UjianSessionRepository ujianSessionRepository;

    @Autowired
    private HasilUjianRepository hasilUjianRepository;

    @Autowired
    private UjianRepository ujianRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private HasilUjianService hasilUjianService; // TAMBAHAN

    // ==================== SESSION MANAGEMENT ====================

    /**
     * Start a new ujian session for participant
     */
    public UjianSession startSession(UjianSessionRequest.StartSessionRequest request, String schoolId)
            throws IOException {
        logger.debug("Starting session for ujian: {} participant: {}", request.getIdUjian(), request.getIdPeserta());

        // Validate request
        validateStartSessionRequest(request);

        // Get ujian data
        Ujian ujian = ujianRepository.findById(request.getIdUjian());
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", request.getIdUjian());
        }

        // Validate ujian status and timing
        validateUjianForStart(ujian);

        // Check if participant already has active session
        UjianSession existingSession = ujianSessionRepository.findActiveSessionByUjianAndPeserta(
                request.getIdUjian(), request.getIdPeserta());
        if (existingSession != null) {
            // Allow resume if session is not submitted and time is still available
            if (!existingSession.getIsSubmitted() && isSessionStillValid(existingSession)) {
                logger.info("Resuming existing session: {} for participant: {}",
                        existingSession.getSessionId(), request.getIdPeserta());

                // Update session metadata and keep alive time
                Map<String, Object> metadata = existingSession.getSessionMetadata();
                if (metadata == null)
                    metadata = new HashMap<>();
                metadata.put("resumedAt", Instant.now().toString());
                int currentResumeCount = metadata.get("resumeCount") != null
                        ? Integer.parseInt(metadata.get("resumeCount").toString())
                        : 0;
                metadata.put("resumeCount", currentResumeCount + 1);
                existingSession.setSessionMetadata(metadata);
                // Update keep alive time in metadata since lastKeepAlive might not exist
                metadata.put("lastKeepAlive", Instant.now().toString());
                existingSession.setUpdatedAt(Instant.now());

                // Recalculate time remaining
                Integer actualTimeRemaining = calculateActualTimeRemaining(existingSession);
                existingSession.setTimeRemaining(actualTimeRemaining);

                // Save updated session
                UjianSession updatedSession = ujianSessionRepository.save(existingSession);
                logger.info("Session resumed successfully: {}", updatedSession.getSessionId());
                return updatedSession;
            } else {
                // Session expired or already submitted
                String reason = existingSession.getIsSubmitted() ? "Session sudah di-submit"
                        : "Waktu ujian telah habis";
                throw new BadRequestException("Tidak dapat melanjutkan ujian: " + reason);
            }
        }

        // Check attempt limits
        validateAttemptLimits(ujian, request.getIdPeserta());

        // Get participant and school info
        User peserta = userRepository.findById(request.getIdPeserta());
        if (peserta == null) {
            throw new ResourceNotFoundException("User", "id", request.getIdPeserta());
        }

        School school = schoolRepository.findById(schoolId);
        if (school == null) {
            throw new ResourceNotFoundException("School", "id", schoolId);
        }

        // Calculate time remaining
        Integer timeRemaining = calculateTimeRemaining(ujian);

        // Create new session
        UjianSession session = new UjianSession(
                request.getIdUjian(),
                request.getIdPeserta(),
                generateSessionId(),
                request.getAttemptNumber() != null ? request.getAttemptNumber() : 1,
                schoolId,
                timeRemaining);

        // Set additional data
        session.setIdSession(UUID.randomUUID().toString());
        session.setTotalQuestions(ujian.getJumlahSoal());
        session.setUjian(ujian);
        session.setPeserta(peserta);
        session.setSchool(school);

        // Set session metadata
        Map<String, Object> metadata = new HashMap<>();
        if (request.getSessionMetadata() != null) {
            metadata.putAll(request.getSessionMetadata());
        }
        metadata.put("userAgent", "CAT-System");
        metadata.put("startedAt", Instant.now().toString());
        session.setSessionMetadata(metadata);

        // Save session
        UjianSession savedSession = ujianSessionRepository.save(session);

        logger.info("Session started successfully: {}", savedSession.getSessionId());
        return savedSession;
    }

    /**
     * Save individual answer
     */
    public UjianSession saveJawaban(UjianSessionRequest.SaveJawabanRequest request) throws IOException {
        // Validasi request
        validateSaveJawabanRequest(request); // Ambil session aktif berdasarkan sessionId (bukan hanya idUjian &
                                             // idPeserta)
        UjianSession session = ujianSessionRepository.findBySessionId(request.getSessionId());
        if (session == null || !session.isActive()) {
            throw new BadRequestException("Session tidak ditemukan atau tidak aktif");
        }

        System.out.println("Session found: " + session.getIdSession());
        System.out.println("Session ujian: " + (session.getUjian() != null ? session.getUjian().getIdUjian() : "null"));

        // Validasi idBankSoal
        validateSoalInUjian(session.getUjian(), request.getIdBankSoal());

        // Simpan jawaban ke map answers (key = idBankSoal)
        session.addAnswer(request.getIdBankSoal(), request.getJawaban());

        // Update currentSoalIndex jika ada
        if (request.getCurrentSoalIndex() != null) {
            session.setCurrentSoalIndex(request.getCurrentSoalIndex());
        }

        // Update waktu update
        session.setUpdatedAt(java.time.Instant.now());

        // Simpan session
        return ujianSessionRepository.save(session);
    }

    /**
     * Auto save progress (bulk save)
     */
    public UjianSession autoSaveProgress(UjianSessionRequest.AutoSaveProgressRequest request) throws IOException {
        // Validasi request
        validateAutoSaveRequest(request);

        System.out.println("AutoSave - Looking for session: " + request.getSessionId());
        UjianSession session = ujianSessionRepository.findBySessionId(request.getSessionId());
        System.out.println("AutoSave - Found session: " + (session != null ? session.getIdSession() : "null"));
        System.out.println("AutoSave - Session active: " + (session != null ? session.isActive() : "N/A"));

        if (session == null || !session.isActive()) {
            throw new BadRequestException("Session tidak ditemukan atau tidak aktif");
        }

        // Update answers (bulk)
        if (request.getAnswers() != null) {
            session.setAnswers(request.getAnswers());
        }

        // Update currentSoalIndex
        if (request.getCurrentSoalIndex() != null) {
            session.setCurrentSoalIndex(request.getCurrentSoalIndex());
        }

        // Update timeRemaining
        if (request.getTimeRemaining() != null) {
            session.setTimeRemaining(request.getTimeRemaining());
        }

        session.autoSave();

        return ujianSessionRepository.save(session);
    }

    /**
     * Submit ujian - delegasi ke HasilUjianService untuk create hasil
     */
    public HasilUjian submitUjian(UjianSessionRequest.SubmitUjianRequest request, String schoolId) throws IOException {
        logger.debug("Submitting ujian for session: {}, ujian: {}, peserta: {}",
                request.getSessionId(), request.getIdUjian(), request.getIdPeserta());

        try {
            validateSubmitRequest(request);

            UjianSession session = ujianSessionRepository.findBySessionId(request.getSessionId());
            if (session == null) {
                logger.warn("Session not found: {}", request.getSessionId());
                throw new BadRequestException("Session tidak ditemukan");
            }

            if (!session.isActive()) {
                logger.warn("Session not active: {}, status: {}", request.getSessionId(), session.getStatus());
                throw new BadRequestException("Session tidak aktif");
            }

            logger.debug("Session found and active, proceeding with submit");

            // Update answers terakhir
            if (request.getAnswers() != null) {
                session.setAnswers(request.getAnswers());
                logger.debug("Updated session answers: {} questions answered", request.getAnswers().size());
            }

            // Update waktu tersisa terakhir
            if (request.getFinalTimeRemaining() != null) {
                session.setTimeRemaining(request.getFinalTimeRemaining());
            }

            // Finalisasi session
            session.finalizeSession(Boolean.TRUE.equals(request.getIsAutoSubmit()));

            ujianSessionRepository.save(session);
            logger.debug("Session finalized and saved");

            // Buat hasil ujian
            HasilUjian hasilUjian = hasilUjianService.createHasilUjianFromSession(
                    session.getSessionId(),
                    session.getAnswers(),
                    Boolean.TRUE.equals(request.getIsAutoSubmit()),
                    null);

            logger.debug("HasilUjian created successfully with ID: {}", hasilUjian.getIdHasilUjian());
            return hasilUjian;

        } catch (BadRequestException e) {
            logger.error("Bad request in submitUjian: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error in submitUjian: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to submit ujian: " + e.getMessage());
        }
    }

    // ==================== SESSION MONITORING ====================

    /**
     * Get active session for participant
     */
    public UjianSession getActiveSession(String idUjian, String idPeserta) throws IOException {
        return ujianSessionRepository.findActiveSessionByUjianAndPeserta(idUjian, idPeserta);
    }

    /**
     * Get ujian progress for resume
     */
    public Map<String, Object> getUjianProgress(String idUjian, String idPeserta) throws IOException {
        Map<String, Object> progress = new HashMap<>();

        UjianSession activeSession = getActiveSession(idUjian, idPeserta);

        if (activeSession != null) {
            progress.put("hasActiveSession", true);
            progress.put("sessionId", activeSession.getSessionId());
            progress.put("currentSoalIndex", activeSession.getCurrentSoalIndex());
            progress.put("answeredQuestions", activeSession.getAnsweredQuestions());
            progress.put("totalQuestions", activeSession.getTotalQuestions());
            progress.put("timeRemaining", activeSession.getTimeRemaining());
            progress.put("answers", activeSession.getAnswers());
            progress.put("attemptNumber", activeSession.getAttemptNumber());
            progress.put("startTime", activeSession.getStartTime());
            progress.put("progressPercentage", activeSession.getProgressPercentage());
        } else {
            progress.put("hasActiveSession", false);

            // Check previous attempts
            List<HasilUjian> previousResults = hasilUjianRepository.findByUjianAndPeserta(idUjian, idPeserta);
            progress.put("previousAttempts", previousResults.size());
            progress.put("canStartNewAttempt", canStartNewAttempt(idUjian, idPeserta));
        }

        return progress;
    }

    /**
     * Keep session alive (ping)
     */
    public Map<String, Object> keepSessionAlive(UjianSessionRequest.KeepAliveRequest request) throws IOException {
        UjianSession session = getValidActiveSession(request.getIdUjian(), request.getIdPeserta());

        // Update keep alive time
        session.ping();

        // Update time remaining if provided
        if (request.getCurrentTimeRemaining() != null) {
            session.setTimeRemaining(request.getCurrentTimeRemaining());
        }

        // Check if session should timeout
        boolean shouldTimeout = checkSessionTimeout(session);
        if (shouldTimeout) {
            // Auto submit due to timeout
            session.setStatus("TIMEOUT");
            session.finalizeSession(true);
        }

        ujianSessionRepository.save(session);

        Map<String, Object> keepAliveData = new HashMap<>();
        keepAliveData.put("sessionId", session.getSessionId());
        keepAliveData.put("status", session.getStatus());
        keepAliveData.put("timeRemaining", session.getTimeRemaining());
        keepAliveData.put("lastKeepAlive", session.getLastKeepAliveTime());
        keepAliveData.put("shouldTimeout", shouldTimeout);

        return keepAliveData;
    }

    /**
     * Get time remaining for active session
     */
    public Map<String, Object> getTimeRemaining(String idUjian, String idPeserta) throws IOException {
        UjianSession session = getValidActiveSession(idUjian, idPeserta);

        // Calculate actual remaining time
        Integer actualTimeRemaining = calculateActualTimeRemaining(session);

        Map<String, Object> timeData = new HashMap<>();
        timeData.put("remainingSeconds", actualTimeRemaining);
        timeData.put("sessionTimeRemaining", session.getTimeRemaining());
        timeData.put("startTime", session.getStartTime());
        timeData.put("estimatedEndTime", calculateEstimatedEndTime(session));
        timeData.put("hasTimedOut", actualTimeRemaining <= 0);

        return timeData;
    }

    /**
     * Update current soal index
     */
    public UjianSession updateCurrentSoal(UjianSessionRequest.UpdateCurrentSoalRequest request) throws IOException {
        UjianSession session = getValidActiveSession(request.getIdUjian(), request.getIdPeserta());

        // Validate soal index
        if (request.getCurrentSoalIndex() < 0 ||
                request.getCurrentSoalIndex() >= session.getTotalQuestions()) {
            throw new BadRequestException("Invalid soal index");
        }

        // Update navigation history
        Map<String, Object> navHistory = session.getNavigationHistory();
        if (navHistory == null) {
            navHistory = new HashMap<>();
        }

        String timestamp = Instant.now().toString();
        Map<String, Object> navigationEntry = new HashMap<>();
        navigationEntry.put("previousIndex", session.getCurrentSoalIndex());
        navigationEntry.put("newIndex", request.getCurrentSoalIndex());
        navigationEntry.put("action", request.getNavigationAction() != null ? request.getNavigationAction() : "JUMP");
        navHistory.put(timestamp, navigationEntry);

        session.setNavigationHistory(navHistory);
        session.setCurrentSoalIndex(request.getCurrentSoalIndex());

        return ujianSessionRepository.save(session);
    }

    // ==================== VALIDATION ====================

    /**
     * Validate if participant can start ujian
     */
    public Map<String, Object> validateCanStart(String idUjian, String idPeserta, String schoolId) throws IOException {
        Map<String, Object> validation = new HashMap<>();

        try {
            // Get ujian
            Ujian ujian = ujianRepository.findById(idUjian);
            if (ujian == null) {
                validation.put("canStart", false);
                validation.put("reason", "Ujian tidak ditemukan");
                return validation;
            }

            // Check ujian status
            if (!ujian.isAktif() && !ujian.getIsLive()) {
                validation.put("canStart", false);
                validation.put("reason", "Ujian tidak dalam status aktif");
                return validation;
            }

            // Check timing
            Instant now = Instant.now();

            // Check if ujian has started
            if (ujian.getWaktuMulaiDijadwalkan() != null && now.isBefore(ujian.getWaktuMulaiDijadwalkan())) {
                if (!ujian.getAllowLateStart()) {
                    validation.put("canStart", false);
                    validation.put("reason", "Ujian belum dimulai");
                    validation.put("startTime", ujian.getWaktuMulaiDijadwalkan());
                    return validation;
                }
            }

            // Check if ujian has ended
            if (ujian.getWaktuSelesaiOtomatis() != null && now.isAfter(ujian.getWaktuSelesaiOtomatis())) {
                validation.put("canStart", false);
                validation.put("reason", "Waktu ujian telah berakhir");
                validation.put("endTime", ujian.getWaktuSelesaiOtomatis());
                return validation;
            }

            // Check late start limits
            if (ujian.getAllowLateStart() && ujian.getMaxLateStartMinutes() > 0) {
                Instant maxLateStart = ujian.getWaktuMulaiDijadwalkan()
                        .plus(ujian.getMaxLateStartMinutes(), ChronoUnit.MINUTES);
                if (now.isAfter(maxLateStart)) {
                    validation.put("canStart", false);
                    validation.put("reason", "Waktu untuk memulai ujian telah habis");
                    validation.put("maxLateStart", maxLateStart);
                    return validation;
                }
            }

            // Check existing active session
            UjianSession existingSession = getActiveSession(idUjian, idPeserta);
            if (existingSession != null) {
                // If session exists but is still valid, allow resume
                if (!existingSession.getIsSubmitted() && isSessionStillValid(existingSession)) {
                    validation.put("canStart", true);
                    validation.put("reason", "Dapat melanjutkan ujian yang sudah dimulai");
                    validation.put("hasExistingSession", true);
                    validation.put("existingSessionId", existingSession.getSessionId());
                    validation.put("timeRemaining", calculateActualTimeRemaining(existingSession));
                    validation.put("currentSoalIndex", existingSession.getCurrentSoalIndex());
                    validation.put("answeredQuestions", existingSession.getAnsweredQuestions());
                    return validation;
                } else {
                    validation.put("canStart", false);
                    validation.put("reason",
                            existingSession.getIsSubmitted() ? "Ujian sudah diselesaikan" : "Waktu ujian telah habis");
                    validation.put("existingSessionId", existingSession.getSessionId());
                    return validation;
                }
            }

            // Check attempt limits
            List<HasilUjian> previousAttempts = hasilUjianRepository.findByUjianAndPeserta(idUjian, idPeserta);
            if (previousAttempts.size() >= ujian.getMaxAttempts()) {
                validation.put("canStart", false);
                validation.put("reason", "Sudah mencapai batas maksimal percobaan");
                validation.put("maxAttempts", ujian.getMaxAttempts());
                validation.put("currentAttempts", previousAttempts.size());
                return validation;
            }

            // All validations passed
            validation.put("canStart", true);
            validation.put("reason", "Dapat memulai ujian");

            Map<String, Object> ujianInfo = new HashMap<>();
            ujianInfo.put("namaUjian", ujian.getNamaUjian());
            ujianInfo.put("durasiMenit", ujian.getDurasiMenit());
            ujianInfo.put("jumlahSoal", ujian.getJumlahSoal());
            ujianInfo.put("attemptNumber", previousAttempts.size() + 1);
            validation.put("ujianInfo", ujianInfo);

        } catch (Exception e) {
            logger.error("Error validating can start", e);
            validation.put("canStart", false);
            validation.put("reason", "Terjadi kesalahan sistem");
        }

        return validation;
    }

    // ==================== ADMIN FUNCTIONS ====================

    /**
     * Get all active sessions for monitoring
     */
    public Map<String, Object> getActiveSessions(int page, int size, String schoolId) throws IOException {
        validatePageNumberAndSize(page, size);

        List<UjianSession> activeSessions;

        if ("*".equals(schoolId)) {
            activeSessions = ujianSessionRepository.findAllActiveSessions(size);
        } else {
            activeSessions = ujianSessionRepository.findActiveSessionsBySchool(schoolId, size);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("sessions", activeSessions);
        result.put("totalElements", activeSessions.size());
        result.put("page", page);
        result.put("size", size);

        return result;
    }

    /**
     * Force end session dengan integrasi HasilUjianService
     */
    public UjianSession forceEndSession(String sessionId, String adminUserId) throws IOException {
        UjianSession session = ujianSessionRepository.findBySessionId(sessionId);
        if (session == null) {
            throw new ResourceNotFoundException("Session", "sessionId", sessionId);
        }

        if (!session.isActive()) {
            throw new BadRequestException("Session tidak aktif");
        }

        // Force end dengan proper reason
        session.forceSubmit("ADMIN_FORCE_END", adminUserId);

        UjianSession savedSession = ujianSessionRepository.save(session);

        // Delegate ke HasilUjianService untuk create hasil ujian
        try {
            HasilUjian hasilUjian = hasilUjianService.createHasilUjianFromSession(
                    sessionId,
                    session.getAnswers(),
                    true,
                    "Force ended by admin: " + adminUserId);
            logger.info("Hasil ujian created for force-ended session: {}", hasilUjian.getIdHasilUjian());
        } catch (Exception e) {
            logger.error("Error creating hasil ujian for force-ended session: {}", e.getMessage());
        }

        return savedSession;
    }

    // ==================== SESSION STATISTICS ====================

    /**
     * Get session statistics for ujian
     */
    public Map<String, Object> getSessionStatistics(String idUjian, String schoolId) throws IOException {
        logger.debug("Getting session statistics for ujian: {} school: {}", idUjian, schoolId);

        Map<String, Object> statistics = new HashMap<>();

        try {
            // Get all sessions for this ujian
            List<UjianSession> allSessions = ujianSessionRepository.findSessionsByUjian(idUjian);

            // Filter by school if not wildcard
            if (!"*".equals(schoolId)) {
                allSessions = allSessions.stream()
                        .filter(session -> schoolId.equals(session.getIdSchool()))
                        .collect(Collectors.toList());
            }

            // Calculate basic statistics
            long totalSessions = allSessions.size();
            long activeSessions = allSessions.stream().filter(UjianSession::isActive).count();
            long completedSessions = allSessions.stream().filter(UjianSession::isCompleted).count();
            long timeoutSessions = allSessions.stream().filter(s -> "TIMEOUT".equals(s.getStatus())).count();

            // Calculate average statistics
            double avgProgress = allSessions.stream()
                    .mapToDouble(UjianSession::getProgressPercentage)
                    .average().orElse(0.0);

            long totalAnswered = allSessions.stream()
                    .mapToLong(s -> s.getAnsweredQuestions() != null ? s.getAnsweredQuestions() : 0)
                    .sum();

            // Get unique participants
            long uniqueParticipants = allSessions.stream()
                    .map(UjianSession::getIdPeserta)
                    .distinct()
                    .count();

            // Calculate attempt statistics
            Map<Integer, Long> attemptDistribution = allSessions.stream()
                    .collect(Collectors.groupingBy(
                            s -> s.getAttemptNumber() != null ? s.getAttemptNumber() : 1,
                            Collectors.counting()));

            // Set basic statistics
            statistics.put("totalSessions", totalSessions);
            statistics.put("activeSessions", activeSessions);
            statistics.put("completedSessions", completedSessions);
            statistics.put("timeoutSessions", timeoutSessions);
            statistics.put("uniqueParticipants", uniqueParticipants);
            statistics.put("averageProgress", Math.round(avgProgress * 100.0) / 100.0);
            statistics.put("totalAnswersGiven", totalAnswered);
            statistics.put("attemptDistribution", attemptDistribution);

            // Calculate completion rate
            double completionRate = totalSessions > 0 ? (double) completedSessions / totalSessions * 100 : 0;
            statistics.put("completionRate", Math.round(completionRate * 100.0) / 100.0);

            // Calculate time-based statistics
            calculateTimeStatistics(allSessions, statistics);

            // Calculate violation statistics if available
            calculateViolationStatistics(allSessions, statistics);

            // Get current status summary
            Map<String, Long> statusSummary = allSessions.stream()
                    .collect(Collectors.groupingBy(UjianSession::getStatus, Collectors.counting()));
            statistics.put("statusSummary", statusSummary);

            logger.debug("Session statistics calculated successfully for ujian: {}", idUjian);

        } catch (Exception e) {
            logger.error("Error calculating session statistics for ujian: {}", idUjian, e);
            statistics.put("error", "Failed to calculate statistics: " + e.getMessage());
        }

        return statistics;
    }

    /**
     * Get detailed session analytics for admin dashboard
     */
    public Map<String, Object> getDetailedSessionAnalytics(String schoolId) throws IOException {
        Map<String, Object> analytics = new HashMap<>();

        try {
            List<UjianSession> allSessions;
            if ("*".equals(schoolId)) {
                allSessions = ujianSessionRepository.findAllActiveSessions(1000);
            } else {
                allSessions = ujianSessionRepository.findActiveSessionsBySchool(schoolId, 1000);
            }

            // Current active sessions
            List<UjianSession> currentActive = allSessions.stream()
                    .filter(UjianSession::isActive)
                    .collect(Collectors.toList());

            analytics.put("currentActiveSessions", currentActive.size());

            // Sessions by hour (last 24 hours)
            Map<String, Long> sessionsByHour = calculateSessionsByHour(allSessions);
            analytics.put("sessionsByHour", sessionsByHour);

            // Top active ujian
            Map<String, Long> topActiveUjian = currentActive.stream()
                    .collect(Collectors.groupingBy(UjianSession::getIdUjian, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            java.util.LinkedHashMap::new));

            analytics.put("topActiveUjian", topActiveUjian);

            // Performance metrics
            double avgKeepAliveGap = calculateAverageKeepAliveGap(currentActive);
            analytics.put("averageKeepAliveGapMinutes", avgKeepAliveGap);

        } catch (Exception e) {
            logger.error("Error getting detailed session analytics", e);
            analytics.put("error", "Failed to get analytics: " + e.getMessage());
        }

        return analytics;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Calculate time-based statistics
     */
    private void calculateTimeStatistics(List<UjianSession> sessions, Map<String, Object> statistics) {
        List<UjianSession> completedSessions = sessions.stream()
                .filter(UjianSession::isCompleted)
                .collect(Collectors.toList());

        if (!completedSessions.isEmpty()) {
            // Calculate average duration
            double avgDurationMinutes = completedSessions.stream()
                    .filter(s -> s.getStartTime() != null && s.getEndTime() != null)
                    .mapToLong(s -> java.time.temporal.ChronoUnit.MINUTES.between(s.getStartTime(), s.getEndTime()))
                    .average()
                    .orElse(0.0);

            statistics.put("averageDurationMinutes", Math.round(avgDurationMinutes * 100.0) / 100.0);

            // Calculate time remaining distribution
            Map<String, Long> timeRemainingDistribution = completedSessions.stream()
                    .collect(Collectors.groupingBy(
                            s -> categorizeTimeRemaining(s.getTimeRemaining()),
                            Collectors.counting()));

            statistics.put("timeRemainingDistribution", timeRemainingDistribution);
        }
    }

    /**
     * Calculate violation statistics if available
     */
    private void calculateViolationStatistics(List<UjianSession> sessions, Map<String, Object> statistics) {
        long sessionsWithViolations = sessions.stream()
                .filter(s -> s.getViolationIds() != null && !s.getViolationIds().isEmpty())
                .count();

        long totalViolations = sessions.stream()
                .filter(s -> s.getViolationIds() != null)
                .mapToLong(s -> s.getViolationIds().size())
                .sum();

        long criticalViolations = sessions.stream()
                .filter(s -> s.getSecurityMetadata() != null)
                .filter(s -> Boolean.TRUE.equals(s.getSecurityMetadata().get("hasCriticalViolation")))
                .count();

        statistics.put("sessionsWithViolations", sessionsWithViolations);
        statistics.put("totalViolations", totalViolations);
        statistics.put("criticalViolations", criticalViolations);

        if (sessions.size() > 0) {
            double violationRate = (double) sessionsWithViolations / sessions.size() * 100;
            statistics.put("violationRate", Math.round(violationRate * 100.0) / 100.0);
        }
    }

    /**
     * Calculate sessions by hour for trending analysis
     */
    private Map<String, Long> calculateSessionsByHour(List<UjianSession> sessions) {
        Instant now = Instant.now();
        Instant past24Hours = now.minus(24, java.time.temporal.ChronoUnit.HOURS);

        return sessions.stream()
                .filter(s -> s.getStartTime() != null && s.getStartTime().isAfter(past24Hours))
                .collect(Collectors.groupingBy(
                        s -> s.getStartTime().truncatedTo(java.time.temporal.ChronoUnit.HOURS).toString(),
                        Collectors.counting()));
    }

    /**
     * Calculate average keep alive gap for performance monitoring
     */
    private double calculateAverageKeepAliveGap(List<UjianSession> activeSessions) {
        Instant now = Instant.now();

        return activeSessions.stream()
                .filter(s -> s.getLastKeepAliveTime() != null)
                .mapToLong(s -> java.time.temporal.ChronoUnit.MINUTES.between(s.getLastKeepAliveTime(), now))
                .average()
                .orElse(0.0);
    }

    /**
     * Categorize time remaining for distribution analysis
     */
    private String categorizeTimeRemaining(Integer timeRemaining) {
        if (timeRemaining == null)
            return "Unknown";
        if (timeRemaining <= 0)
            return "Expired";
        if (timeRemaining <= 300)
            return "0-5 minutes"; // 5 minutes
        if (timeRemaining <= 900)
            return "5-15 minutes"; // 15 minutes
        if (timeRemaining <= 1800)
            return "15-30 minutes"; // 30 minutes
        return "30+ minutes";
    }

    // ==================== VALIDATION METHODS ====================

    private void validateStartSessionRequest(UjianSessionRequest.StartSessionRequest request) {
        if (request.getIdUjian() == null || request.getIdUjian().trim().isEmpty()) {
            throw new BadRequestException("ID ujian wajib diisi");
        }
        if (request.getIdPeserta() == null || request.getIdPeserta().trim().isEmpty()) {
            throw new BadRequestException("ID peserta wajib diisi");
        }
    }

    private void validateSaveJawabanRequest(UjianSessionRequest.SaveJawabanRequest request) {
        if (request.getIdUjian() == null || request.getIdUjian().trim().isEmpty()) {
            throw new BadRequestException("ID ujian wajib diisi");
        }
        if (request.getIdPeserta() == null || request.getIdPeserta().trim().isEmpty()) {
            throw new BadRequestException("ID peserta wajib diisi");
        }
        if (request.getIdBankSoal() == null || request.getIdBankSoal().trim().isEmpty()) {
            throw new BadRequestException("ID bank soal wajib diisi");
        }
        if (request.getJawaban() == null) {
            throw new BadRequestException("Jawaban wajib diisi");
        }
    }

    private void validateAutoSaveRequest(UjianSessionRequest.AutoSaveProgressRequest request) {
        if (request.getIdUjian() == null || request.getIdUjian().trim().isEmpty()) {
            throw new BadRequestException("ID ujian wajib diisi");
        }
        if (request.getIdPeserta() == null || request.getIdPeserta().trim().isEmpty()) {
            throw new BadRequestException("ID peserta wajib diisi");
        }
    }

    private void validateSubmitRequest(UjianSessionRequest.SubmitUjianRequest request) {
        if (request.getSessionId() == null || request.getSessionId().trim().isEmpty()) {
            throw new BadRequestException("Session ID wajib diisi");
        }
        if (request.getIdUjian() == null || request.getIdUjian().trim().isEmpty()) {
            throw new BadRequestException("ID ujian wajib diisi");
        }
        if (request.getIdPeserta() == null || request.getIdPeserta().trim().isEmpty()) {
            throw new BadRequestException("ID peserta wajib diisi");
        }
    }

    private void validateUjianForStart(Ujian ujian) {
        if (!ujian.isAktif() && !ujian.getIsLive()) {
            throw new BadRequestException("Ujian tidak dalam status aktif atau live");
        }

        if (ujian.getBankSoalList() == null || ujian.getBankSoalList().isEmpty()) {
            throw new BadRequestException("Ujian tidak memiliki soal");
        }

        Instant now = Instant.now();

        // Check if ujian has ended
        if (ujian.getWaktuSelesaiOtomatis() != null && now.isAfter(ujian.getWaktuSelesaiOtomatis())) {
            throw new BadRequestException("Waktu ujian telah berakhir");
        }
    }

    private void validateAttemptLimits(Ujian ujian, String idPeserta) throws IOException {
        List<HasilUjian> previousAttempts = hasilUjianRepository.findByUjianAndPeserta(ujian.getIdUjian(), idPeserta);

        if (previousAttempts.size() >= ujian.getMaxAttempts()) {
            throw new BadRequestException("Sudah mencapai batas maksimal percobaan (" + ujian.getMaxAttempts() + ")");
        }
    }

    private void validateSoalInUjian(Ujian ujian, String idBankSoal) throws IOException {
        System.out.println("Validating soal: " + idBankSoal);
        System.out.println("Ujian ID: " + (ujian != null ? ujian.getIdUjian() : "null"));
        System.out.println("BankSoalList size: "
                + (ujian != null && ujian.getBankSoalList() != null ? ujian.getBankSoalList().size()
                        : "null or empty"));

        // If bankSoalList is empty, try to reload ujian data from repository
        if (ujian == null || ujian.getBankSoalList() == null || ujian.getBankSoalList().isEmpty()) {
            System.out.println("Ujian or bankSoalList is null/empty, attempting to reload ujian data");

            if (ujian != null && ujian.getIdUjian() != null) {
                try {
                    Ujian reloadedUjian = ujianRepository.findById(ujian.getIdUjian());
                    if (reloadedUjian != null && reloadedUjian.getBankSoalList() != null
                            && !reloadedUjian.getBankSoalList().isEmpty()) {
                        System.out.println("Successfully reloaded ujian data with "
                                + reloadedUjian.getBankSoalList().size() + " soal");
                        // Update the original ujian object
                        ujian.setBankSoalList(reloadedUjian.getBankSoalList());
                    } else {
                        throw new BadRequestException("Data ujian tidak lengkap - bankSoalList kosong setelah reload");
                    }
                } catch (Exception e) {
                    System.out.println("Failed to reload ujian data: " + e.getMessage());
                    throw new BadRequestException("Gagal memuat data ujian");
                }
            } else {
                throw new BadRequestException("Data ujian tidak lengkap - ID ujian null");
            }
        }

        boolean soalExists = ujian.getBankSoalList().stream()
                .anyMatch(soal -> idBankSoal.equals(soal.getIdBankSoal()));

        if (!soalExists) {
            System.out.println("Available soal IDs in ujian:");
            ujian.getBankSoalList().forEach(soal -> System.out.println("- " + soal.getIdBankSoal()));
            throw new BadRequestException("Soal tidak ditemukan dalam ujian");
        }
    }

    private UjianSession getValidActiveSession(String idUjian, String idPeserta) throws IOException {
        UjianSession session = getActiveSession(idUjian, idPeserta);
        if (session == null) {
            throw new ResourceNotFoundException("UjianSession", "ujian and peserta", idUjian + " and " + idPeserta);
        }
        if (!session.isActive()) {
            throw new BadRequestException("Session tidak aktif");
        }
        return session;
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    // ==================== UTILITY METHODS ====================

    private String generateSessionId() {
        return "SES-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private Integer calculateTimeRemaining(Ujian ujian) {
        if (ujian.getDurasiMenit() == null) {
            return null;
        }

        Instant now = Instant.now();
        Integer totalDurationSeconds = ujian.getDurasiMenit() * 60;

        // If ujian has automatic end time, use it
        if (ujian.getWaktuSelesaiOtomatis() != null) {
            long secondsUntilEnd = ChronoUnit.SECONDS.between(now, ujian.getWaktuSelesaiOtomatis());
            return Math.min(totalDurationSeconds, (int) Math.max(0, secondsUntilEnd));
        }

        return totalDurationSeconds;
    }

    private Integer calculateActualTimeRemaining(UjianSession session) {
        if (session.getStartTime() == null || session.getUjian().getDurasiMenit() == null) {
            return session.getTimeRemaining();
        }

        Instant now = Instant.now();
        long elapsedSeconds = ChronoUnit.SECONDS.between(session.getStartTime(), now);
        int totalDurationSeconds = session.getUjian().getDurasiMenit() * 60;

        return Math.max(0, totalDurationSeconds - (int) elapsedSeconds);
    }

    /**
     * Check if session is still valid for resume (time remaining > 0 and not
     * expired)
     */
    private boolean isSessionStillValid(UjianSession session) {
        try {
            // Check if session has time remaining
            Integer timeRemaining = calculateActualTimeRemaining(session);
            if (timeRemaining == null || timeRemaining <= 0) {
                return false;
            }

            // Check ujian end time if set
            if (session.getUjian() != null && session.getUjian().getWaktuSelesaiOtomatis() != null) {
                Instant now = Instant.now();
                if (now.isAfter(session.getUjian().getWaktuSelesaiOtomatis())) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.warn("Error checking session validity: {}", e.getMessage());
            return false;
        }
    }

    private Instant calculateEstimatedEndTime(UjianSession session) {
        if (session.getStartTime() == null || session.getUjian().getDurasiMenit() == null) {
            return null;
        }

        return session.getStartTime().plus(session.getUjian().getDurasiMenit(), ChronoUnit.MINUTES);
    }

    private boolean checkSessionTimeout(UjianSession session) {
        Integer actualTimeRemaining = calculateActualTimeRemaining(session);
        return actualTimeRemaining <= 0;
    }

    private boolean canStartNewAttempt(String idUjian, String idPeserta) throws IOException {
        try {
            Ujian ujian = ujianRepository.findById(idUjian);
            if (ujian == null)
                return false;

            List<HasilUjian> previousAttempts = hasilUjianRepository.findByUjianAndPeserta(idUjian, idPeserta);
            return previousAttempts.size() < ujian.getMaxAttempts();
        } catch (Exception e) {
            return false;
        }
    }
}
