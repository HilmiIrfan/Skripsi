package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.CheatDetection;
import com.doyatama.university.model.UjianSession;
import com.doyatama.university.model.HasilUjian;
import com.doyatama.university.model.Ujian;
import com.doyatama.university.model.User;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.CheatDetectionRequest;
import com.doyatama.university.repository.CheatDetectionRepository;
import com.doyatama.university.repository.UjianSessionRepository;
import com.doyatama.university.repository.HasilUjianRepository;
import com.doyatama.university.repository.UjianRepository;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.constants.ViolationType;
import com.doyatama.university.constants.SeverityLevel;
import com.doyatama.university.constants.ActionType;
import com.doyatama.university.util.AppConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CheatDetectionService {

    private static final Logger logger = LoggerFactory.getLogger(CheatDetectionService.class);

    @Autowired
    private CheatDetectionRepository cheatDetectionRepository;

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
    private UjianSessionService ujianSessionService;

    @Autowired
    private HasilUjianService hasilUjianService; // TAMBAHAN

    // ==================== ENHANCED VALIDATION METHODS ====================

    /**
     * Validate active session - ENHANCED dengan school, user, ujian validation
     */
    private UjianSession validateActiveSession(String sessionId, String idPeserta, String idUjian) throws IOException {
        UjianSession session = ujianSessionRepository.findBySessionId(sessionId);
        if (session == null) {
            throw new ResourceNotFoundException("UjianSession", "sessionId", sessionId);
        }

        if (!session.isActive()) {
            throw new BadRequestException("Session tidak aktif");
        }

        if (!idPeserta.equals(session.getIdPeserta())) {
            throw new BadRequestException("Session tidak milik peserta ini");
        }

        if (!idUjian.equals(session.getIdUjian())) {
            throw new BadRequestException("Session tidak untuk ujian ini");
        }

        // VALIDASI TAMBAHAN MENGGUNAKAN REPOSITORY
        validateUjian(idUjian);
        validatePeserta(idPeserta);
        validateSchool(session.getIdSchool());

        return session;
    }

    /**
     * VALIDATE UJIAN - MENGGUNAKAN UjianRepository
     */
    private Ujian validateUjian(String idUjian) throws IOException {
        Ujian ujian = ujianRepository.findById(idUjian);
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", idUjian);
        }

        // Check ujian masih aktif
        if (!ujian.isAktif()) {
            throw new BadRequestException("Ujian tidak aktif");
        }

        // Check cheat detection enabled
        if (!ujian.getPreventCheating()) {
            logger.warn("Cheat detection not enabled for ujian: {}", idUjian);
        }

        return ujian;
    }

    /**
     * VALIDATE PESERTA - MENGGUNAKAN UserRepository
     */
    private User validatePeserta(String idPeserta) throws IOException {
        User peserta = userRepository.findById(idPeserta);
        if (peserta == null) {
            throw new ResourceNotFoundException("User", "id", idPeserta);
        }

        // Check user masih aktif
        if (!peserta.isValid()) {
            throw new BadRequestException("User tidak aktif");
        }

        return peserta;
    }

    /**
     * VALIDATE SCHOOL - MENGGUNAKAN SchoolRepository
     */
    private School validateSchool(String idSchool) throws IOException {
        if (idSchool == null || idSchool.trim().isEmpty()) {
            throw new BadRequestException("School ID wajib diisi");
        }

        School school = schoolRepository.findById(idSchool);
        if (school == null) {
            throw new ResourceNotFoundException("School", "id", idSchool);
        }

        // Check school masih aktif
        if (!school.isValid()) {
            throw new BadRequestException("School tidak aktif");
        }

        return school;
    }

    /**
     * FLAG HASIL UJIAN - MENGGUNAKAN HasilUjianRepository
     */
    private void flagHasilUjianIfCheating(CheatDetection detection) throws IOException {
        try {
            // FIX: Create correct method call
            List<HasilUjian> hasilList = hasilUjianRepository.findByUjianAndPeserta(
                    detection.getIdUjian(),
                    detection.getIdPeserta());

            // Find hasil with matching session
            HasilUjian hasil = hasilList.stream()
                    .filter(h -> detection.getSessionId().equals(h.getSessionId()))
                    .findFirst()
                    .orElse(null);

            if (hasil != null) {
                // Add security flag - using available methods
                hasil.setViolationType(detection.getTypeViolation());
                hasil.setViolationSeverity(detection.getSeverity());
                hasil.setViolationCount(detection.getViolationCount());
                hasil.setFlaggedAt(Instant.now());

                // Update security status based on severity
                if (detection.isCritical() || detection.getViolationCount() >= 10) {
                    hasil.setSecurityStatus("UNDER_REVIEW");
                } else {
                    hasil.setSecurityStatus("FLAGGED");
                }

                hasilUjianRepository.save(hasil);

                logger.info("HasilUjian flagged for session: {} due to violation: {}",
                        detection.getSessionId(), detection.getTypeViolation());
            }
        } catch (Exception e) {
            logger.error("Error flagging HasilUjian for session: {}", detection.getSessionId(), e);
            // Don't fail the violation recording process
        }
    }

    /**
     * GET UJIAN SETTINGS untuk violation thresholds
     */
    private Map<String, Object> getUjianViolationSettings(String idUjian) throws IOException {
        Map<String, Object> settings = new HashMap<>();

        try {
            Ujian ujian = ujianRepository.findById(idUjian);
            if (ujian != null) {
                settings.put("preventCheating", ujian.getPreventCheating());
                settings.put("durasiMenit", ujian.getDurasiMenit());
                settings.put("allowReview", ujian.getAllowReview());
                settings.put("allowBacktrack", ujian.getAllowBacktrack());
                settings.put("maxAttempts", ujian.getMaxAttempts());

                // Custom violation thresholds based on ujian type
                if (ujian.getPreventCheating()) {
                    settings.put("warningThreshold", 3);
                    settings.put("flagThreshold", 7);
                    settings.put("autoSubmitThreshold", 15);
                } else {
                    settings.put("warningThreshold", 10);
                    settings.put("flagThreshold", 20);
                    settings.put("autoSubmitThreshold", 50);
                }
            }
        } catch (Exception e) {
            logger.error("Error getting ujian settings: {}", idUjian, e);
            // Use default settings
            settings.put("warningThreshold", 5);
            settings.put("flagThreshold", 10);
            settings.put("autoSubmitThreshold", 20);
        }

        return settings;
    }

    /**
     * Get total violation count for session
     */
    private int getTotalViolationCount(String sessionId) throws IOException {
        List<CheatDetection> violations = cheatDetectionRepository.findBySessionId(sessionId);
        return violations.stream()
                .mapToInt(v -> v.getViolationCount() != null ? v.getViolationCount() : 1)
                .sum();
    }

    /**
     * ENHANCED PROCESS VIOLATION ACTION dengan ujian settings
     */
    private void processViolationAction(CheatDetection detection, UjianSession session) throws IOException {
        // Get ujian-specific violation settings
        Map<String, Object> ujianSettings = getUjianViolationSettings(session.getIdUjian());

        int warningThreshold = (Integer) ujianSettings.getOrDefault("warningThreshold", 5);
        int flagThreshold = (Integer) ujianSettings.getOrDefault("flagThreshold", 10);
        int autoSubmitThreshold = (Integer) ujianSettings.getOrDefault("autoSubmitThreshold", 15);

        int totalViolations = getTotalViolationCount(session.getSessionId());

        if (totalViolations >= autoSubmitThreshold || detection.isCritical()) {
            // Auto-submit ujian
            detection.takeAction(ActionType.AUTO_SUBMIT, "SYSTEM",
                    "Too many violations or critical violation detected");

            try {
                // PERBAIKAN: Force submit session dengan proper integration
                session.forceSubmit("VIOLATION_AUTO_SUBMIT", "CHEAT_DETECTION_SYSTEM");
                ujianSessionRepository.save(session);

                // Create hasil ujian immediately
                HasilUjian hasilUjian = hasilUjianService.createHasilUjianFromSession(
                        session.getSessionId(),
                        session.getAnswers(),
                        true,
                        "Auto-submitted due to " + totalViolations + " violations");

                logger.info("Session {} auto-submitted due to violations", session.getSessionId());

            } catch (Exception e) {
                logger.error("Error auto-submitting session {}: {}", session.getSessionId(), e.getMessage());
            }

        } else if (totalViolations >= flagThreshold) {
            detection.takeAction(ActionType.FLAGGED_FOR_REVIEW, "SYSTEM",
                    "Multiple violations detected (" + totalViolations + ")");
        } else if (totalViolations >= warningThreshold) {
            detection.takeAction(ActionType.WARNING, "SYSTEM",
                    "Warning threshold reached (" + totalViolations + ")");
        }
    }

    /**
     * Check if session should be auto-submitted
     */
    private void checkAutoSubmitConditions(UjianSession session) throws IOException {
        int totalViolations = getTotalViolationCount(session.getSessionId());

        if (totalViolations >= 20) {
            try {
                ujianSessionService.forceEndSession(session.getSessionId(), "CHEAT_DETECTION_SYSTEM");
                logger.warn("Session force-ended due to excessive violations: {}", session.getSessionId());
            } catch (Exception e) {
                logger.error("Error force-ending session: {}", session.getSessionId(), e);
            }
        }
    }

    /**
     * Execute action on violation
     */
    private void executeAction(CheatDetection detection, CheatDetectionRequest.TakeActionRequest request)
            throws IOException {
        String actionType = request.getActionTaken();

        switch (actionType) {
            case ActionType.AUTO_SUBMIT:
            case ActionType.TERMINATE_SESSION:
                try {
                    ujianSessionService.forceEndSession(detection.getSessionId(), request.getActionBy());
                } catch (Exception e) {
                    logger.error("Error executing action {}: {}", actionType, e.getMessage());
                }
                break;

            case ActionType.REDUCE_TIME:
                // Implement time reduction logic if needed
                break;

            case ActionType.WARNING:
            case ActionType.FLAGGED_FOR_REVIEW:
            case ActionType.NOTIFY_PROCTOR:
            default:
                // No immediate action required
                break;
        }
    }

    /**
     * Create behavioral-based violations
     */
    private void createBehavioralViolations(UjianSession session, Map<String, Object> analysisResult)
            throws IOException {
        double suspicionScore = (Double) analysisResult.get("suspicionScore");
        String riskLevel = (String) analysisResult.get("riskLevel");

        CheatDetection behavioralViolation = new CheatDetection(
                session.getSessionId(),
                session.getIdPeserta(),
                session.getIdUjian(),
                session.getIdSchool(),
                ViolationType.SUSPICIOUS_BEHAVIOR,
                suspicionScore >= 0.9 ? SeverityLevel.CRITICAL : SeverityLevel.HIGH);

        behavioralViolation.setIdDetection(UUID.randomUUID().toString());
        behavioralViolation.addEvidence("analysisResult", analysisResult);
        behavioralViolation.addEvidence("suspicionScore", suspicionScore);
        behavioralViolation.addEvidence("riskLevel", riskLevel);
        behavioralViolation.addEvidence("detectionSource", "BEHAVIORAL_ANALYSIS");

        // Set relational data
        behavioralViolation.setUjianSession(session);
        behavioralViolation.setPeserta(session.getPeserta());
        behavioralViolation.setUjian(session.getUjian());
        behavioralViolation.setSchool(session.getSchool());

        cheatDetectionRepository.save(behavioralViolation);

        logger.info("Behavioral-based violation created for session: {} with suspicion score: {}",
                session.getSessionId(), suspicionScore);
    }

    /**
     * Determine risk level based on suspicion score
     */
    private String determineRiskLevel(double suspicionScore) {
        if (suspicionScore >= 0.9) {
            return "CRITICAL";
        } else if (suspicionScore >= 0.7) {
            return "HIGH";
        } else if (suspicionScore >= 0.4) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    // ==================== VIOLATION RECORDING ====================

    /**
     * Record single violation from frontend
     */
    public CheatDetection recordViolation(CheatDetectionRequest.RecordViolationRequest request) throws IOException {
        logger.debug("Recording violation: {} for session: {}", request.getTypeViolation(), request.getSessionId());

        try {
            // Validate request
            validateRecordViolationRequest(request); // ENHANCED validation dengan semua repository
            UjianSession session = validateActiveSession(request.getSessionId(), request.getIdPeserta(),
                    request.getIdUjian());
            // Validate related entities (will throw exception if invalid)
            validateUjian(request.getIdUjian());
            validatePeserta(request.getIdPeserta());
            validateSchool(request.getIdSchool());

            // Check existing violation (spam prevention)
            CheatDetection existingViolation = findRecentViolation(request.getSessionId(), request.getTypeViolation(),
                    30);

            if (existingViolation != null) {
                logger.debug("Found existing violation, incrementing count");
                existingViolation.incrementViolationCount();
                existingViolation.addEvidence("lastDetection", request.getDetectionTimestamp());

                if (request.getEvidence() != null) {
                    existingViolation.getEvidence().putAll(request.getEvidence());
                }

                // TAMBAHAN: Sync dengan session
                existingViolation.syncWithSession(session);

                CheatDetection savedDetection = cheatDetectionRepository.save(existingViolation);

                // TAMBAHAN: Update session dengan violation
                session.addViolation(savedDetection);
                ujianSessionRepository.save(session);

                processViolationAction(savedDetection, session);
                return savedDetection;
            }

            logger.debug("Creating new violation for type: {}", request.getTypeViolation());

            // PERBAIKAN: Create new violation menggunakan factory method
            CheatDetection detection = CheatDetection.createFromSession(session, request.getTypeViolation(),
                    request.getSeverity() != null ? request.getSeverity()
                            : ViolationType.getDefaultSeverity(request.getTypeViolation()),
                    request.getEvidence());

            // Set additional data from request
            detection.setBrowserInfo(request.getBrowserInfo());
            detection.setUserAgent(request.getUserAgent());
            detection.setWindowTitle(request.getWindowTitle());
            detection.setScreenWidth(request.getScreenWidth());
            detection.setScreenHeight(request.getScreenHeight());
            detection.setFullscreenStatus(request.getFullscreenStatus());

            // Set frontend events
            if (request.getFrontendEvents() != null) {
                detection.setFrontendEvents(request.getFrontendEvents());
            }

            // Record answer timing if provided
            if (request.getQuestionId() != null && request.getDetectionTimestamp() != null) {
                detection.recordAnswerTime(request.getQuestionId(), request.getDetectionTimestamp());
            }

            logger.debug("Saving violation to repository");

            // Save detection
            CheatDetection savedDetection = cheatDetectionRepository.save(detection);

            // Process violation action (including session updates)
            processViolationAction(savedDetection, session);

            logger.debug("Successfully recorded violation: {} with ID: {}",
                    savedDetection.getTypeViolation(), savedDetection.getIdDetection());

            return savedDetection;

        } catch (BadRequestException e) {
            logger.error("Bad request in recordViolation: {}", e.getMessage());
            throw e;
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found in recordViolation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error in recordViolation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to record violation: " + e.getMessage());
        }
    }

    /**
     * Record multiple violations in batch
     */
    public Map<String, Object> recordViolationBatch(CheatDetectionRequest.BatchRecordViolationsRequest request)
            throws IOException {
        logger.debug("Recording batch violations for session: {}", request.getSessionId());

        validateBatchRecordRequest(request);

        UjianSession session = validateActiveSession(request.getSessionId(), request.getIdPeserta(),
                request.getIdUjian());

        List<CheatDetection> processedDetections = new ArrayList<>();
        Map<String, Object> batchResult = new HashMap<>();

        for (CheatDetectionRequest.BatchRecordViolationsRequest.ViolationEvent violation : request.getViolations()) {
            try {
                // Create individual request for each violation
                CheatDetectionRequest.RecordViolationRequest individualRequest = new CheatDetectionRequest.RecordViolationRequest();
                individualRequest.setSessionId(request.getSessionId());
                individualRequest.setIdPeserta(request.getIdPeserta());
                individualRequest.setIdUjian(request.getIdUjian());
                individualRequest.setIdSchool(request.getIdSchool());
                individualRequest.setTypeViolation(violation.getTypeViolation());
                individualRequest.setSeverity(violation.getSeverity());
                individualRequest.setDetectionTimestamp(violation.getTimestamp());
                individualRequest.setQuestionId(violation.getQuestionId());
                individualRequest.setEvidence(violation.getEventData());

                CheatDetection detection = recordViolation(individualRequest);
                processedDetections.add(detection);

            } catch (Exception e) {
                logger.error("Error processing violation in batch: {}", violation.getTypeViolation(), e);
                // Continue processing other violations
            }
        }

        batchResult.put("batchId", request.getBatchId());
        batchResult.put("totalRequested", request.getViolations().size());
        batchResult.put("totalProcessed", processedDetections.size());
        batchResult.put("detections", processedDetections);
        batchResult.put("sessionContext", request.getSessionContext());

        // Check if session should be auto-submitted due to multiple violations
        checkAutoSubmitConditions(session);

        logger.info("Batch violations processed: {}/{} for session: {}",
                processedDetections.size(), request.getViolations().size(), request.getSessionId());

        return batchResult;
    }

    // ==================== PATTERN ANALYSIS ====================

    /**
     * Analyze answer patterns for suspicious behavior
     */
    public Map<String, Object> analyzeAnswerPatterns(CheatDetectionRequest.AnalyzeAnswerPatternsRequest request)
            throws IOException {
        logger.debug("Analyzing behavioral patterns for session: {}", request.getSessionId());

        validateAnalyzeRequest(request);

        UjianSession session = ujianSessionRepository.findBySessionId(request.getSessionId());
        if (session == null) {
            throw new ResourceNotFoundException("Session", "sessionId", request.getSessionId());
        }

        Map<String, Object> analysisResult = new HashMap<>();

        // HANYA analyze timing patterns
        Map<String, Object> timingAnalysis = analyzeTimingPatterns(request);
        analysisResult.put("timingAnalysis", timingAnalysis);

        // HANYA analyze answer change patterns
        Map<String, Object> changeAnalysis = analyzeAnswerChanges(request);
        analysisResult.put("changeAnalysis", changeAnalysis);

        // Generate overall suspicion score - SIMPLIFIED
        double suspicionScore = calculateSuspicionScore(timingAnalysis, new HashMap<>(), changeAnalysis);
        analysisResult.put("suspicionScore", suspicionScore);
        analysisResult.put("riskLevel", determineRiskLevel(suspicionScore));

        // Auto-create violations if patterns are highly suspicious
        if (suspicionScore >= 0.8) {
            createBehavioralViolations(session, analysisResult);
        }

        analysisResult.put("sessionId", request.getSessionId());
        analysisResult.put("analysisTimestamp", Instant.now());
        analysisResult.put("analysisType", "BEHAVIORAL_ONLY");

        return analysisResult;
    }

    // ==================== ACTION MANAGEMENT ====================

    /**
     * Take action on a violation
     */
    public CheatDetection takeAction(CheatDetectionRequest.TakeActionRequest request) throws IOException {
        logger.debug("Taking action on violation: {}", request.getIdDetection());

        validateTakeActionRequest(request);

        CheatDetection detection = cheatDetectionRepository.findById(request.getIdDetection());
        if (detection == null) {
            throw new ResourceNotFoundException("CheatDetection", "id", request.getIdDetection());
        }

        // Record action
        detection.takeAction(request.getActionTaken(), request.getActionBy(), request.getActionReason());

        // Execute action
        executeAction(detection, request);

        return cheatDetectionRepository.save(detection);
    }

    /**
     * Resolve violation (mark as reviewed)
     */
    public CheatDetection resolveViolation(CheatDetectionRequest.ResolveViolationRequest request) throws IOException {
        logger.debug("Resolving violation: {}", request.getIdDetection());

        validateResolveRequest(request);

        CheatDetection detection = cheatDetectionRepository.findById(request.getIdDetection());
        if (detection == null) {
            throw new ResourceNotFoundException("CheatDetection", "id", request.getIdDetection());
        }

        // Resolve violation
        detection.resolve(request.getResolvedBy(), request.getResolutionNotes());

        // Add resolution data
        if (request.getResolutionData() != null) {
            for (Map.Entry<String, Object> entry : request.getResolutionData().entrySet()) {
                detection.addEvidence("resolution_" + entry.getKey(), entry.getValue());
            }
        }

        detection.addEvidence("isValidViolation", request.getIsValidViolation());
        detection.addEvidence("reviewerComment", request.getReviewerComment());

        return cheatDetectionRepository.save(detection);
    }

    // ==================== QUERY METHODS ====================

    /**
     * Get violations by criteria
     */
    public Map<String, Object> getViolations(CheatDetectionRequest.GetViolationsRequest request) throws IOException {
        logger.debug("Getting violations with criteria for session: {}", request.getSessionId());

        validateGetViolationsRequest(request);

        List<CheatDetection> allViolations = new ArrayList<>();

        // Get violations based on criteria
        if (request.getSessionId() != null) {
            allViolations = cheatDetectionRepository.findBySessionId(request.getSessionId());
        } else if (request.getIdUjian() != null) {
            allViolations = cheatDetectionRepository.findByUjianId(request.getIdUjian());
        } else if (request.getIdPeserta() != null) {
            allViolations = cheatDetectionRepository.findByPesertaId(request.getIdPeserta());
        } else if (request.getIdSchool() != null) {
            allViolations = cheatDetectionRepository.findBySchoolId(request.getIdSchool());
        } else {
            allViolations = cheatDetectionRepository.findAll(1000);
        }

        // Apply filters
        List<CheatDetection> filteredViolations = applyFilters(allViolations, request);

        // Apply sorting
        List<CheatDetection> sortedViolations = applySorting(filteredViolations, request);

        // Apply pagination
        List<CheatDetection> paginatedViolations = applyPagination(sortedViolations, request);

        // Prepare result
        Map<String, Object> result = new HashMap<>();
        result.put("violations", formatViolations(paginatedViolations, request));
        result.put("totalElements", filteredViolations.size());
        result.put("totalPages", (int) Math.ceil((double) filteredViolations.size() / request.getLimit()));
        result.put("currentPage", 0);
        result.put("pageSize", request.getLimit());

        return result;
    }

    /**
     * Generate violation statistics
     */
    public Map<String, Object> generateStatistics(CheatDetectionRequest.GenerateStatisticsRequest request)
            throws IOException {
        logger.debug("Generating statistics for violations");

        validateStatisticsRequest(request);

        List<CheatDetection> violations = getViolationsForStatistics(request);

        Map<String, Object> statistics = new HashMap<>();

        // Basic counts
        Map<String, Object> basicStats = generateBasicStatistics(violations);
        statistics.put("basicStats", basicStats);

        // Group by analysis
        Map<String, Object> groupedStats = generateGroupedStatistics(violations, request.getGroupBy());
        statistics.put("groupedStats", groupedStats);

        // Trends analysis if requested
        if (request.getIncludeTrends()) {
            Map<String, Object> trends = generateTrendsAnalysis(violations);
            statistics.put("trends", trends);
        }

        // Top violators if requested
        if (request.getIncludeTopViolators()) {
            Map<String, Object> topViolators = generateTopViolatorsAnalysis(violations);
            statistics.put("topViolators", topViolators);
        }

        statistics.put("generatedAt", Instant.now());
        statistics.put("timeRange", request.getTimeRange());
        statistics.put("totalAnalyzed", violations.size());

        return statistics;
    }

    /**
     * Export violations report
     */
    public Map<String, Object> exportViolations(CheatDetectionRequest.ExportViolationsRequest request)
            throws IOException {
        logger.debug("Exporting violations report in format: {}", request.getFormat());

        validateExportRequest(request);

        // Get violations for export
        List<CheatDetection> violations = getViolationsForExport(request);

        Map<String, Object> exportData = new HashMap<>();
        exportData.put("violations", violations);

        // Include statistics if requested
        if (request.getIncludeStatistics()) {
            CheatDetectionRequest.GenerateStatisticsRequest statsRequest = new CheatDetectionRequest.GenerateStatisticsRequest();
            statsRequest.setTimeRange(request.getTimeRange());
            statsRequest.setIdSchool(request.getIdSchool());
            Map<String, Object> statistics = generateStatistics(statsRequest);
            exportData.put("statistics", statistics);
        }

        // Include recommendations if requested
        if (request.getIncludeRecommendations()) {
            List<String> recommendations = generateRecommendations(violations);
            exportData.put("recommendations", recommendations);
        }

        // Format data according to requested format
        Map<String, Object> result = new HashMap<>();
        result.put("format", request.getFormat());
        result.put("data", exportData);
        result.put("fileName", generateFileName(request));
        result.put("generatedAt", Instant.now());

        return result;
    }

    // ==================== HELPER METHODS ====================

    /**
     * Find recent violation of same type (to prevent spam)
     */
    private CheatDetection findRecentViolation(String sessionId, String violationType, int secondsThreshold)
            throws IOException {
        List<CheatDetection> sessionViolations = cheatDetectionRepository.findBySessionId(sessionId);

        Instant threshold = Instant.now().minusSeconds(secondsThreshold);

        return sessionViolations.stream()
                .filter(v -> violationType.equals(v.getTypeViolation()))
                .filter(v -> v.getDetectedAt().isAfter(threshold))
                .findFirst()
                .orElse(null);
    }

    // ==================== ANALYSIS METHODS ====================

    /**
     * Analyze timing patterns ONLY - SIMPLIFIED
     */
    private Map<String, Object> analyzeTimingPatterns(CheatDetectionRequest.AnalyzeAnswerPatternsRequest request) {
        Map<String, Object> timingAnalysis = new HashMap<>();

        if (request.getTimeSpentPerQuestion() != null && !request.getTimeSpentPerQuestion().isEmpty()) {
            Map<String, Integer> timeSpent = request.getTimeSpentPerQuestion();

            // Calculate basic statistics only
            List<Integer> times = new ArrayList<>(timeSpent.values());
            Collections.sort(times);

            int totalQuestions = times.size();
            double averageTime = times.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            int minTime = times.get(0);
            int maxTime = times.get(totalQuestions - 1);

            // Simple suspicious timing detection
            boolean hasFastAnswers = times.stream().anyMatch(t -> t < 3);
            boolean hasVeryFastAnswers = times.stream().anyMatch(t -> t < 1);
            boolean hasUniformTiming = calculateTimeVariance(times) < 5;

            timingAnalysis.put("averageTime", averageTime);
            timingAnalysis.put("minTime", minTime);
            timingAnalysis.put("maxTime", maxTime);
            timingAnalysis.put("hasFastAnswers", hasFastAnswers);
            timingAnalysis.put("hasVeryFastAnswers", hasVeryFastAnswers);
            timingAnalysis.put("hasUniformTiming", hasUniformTiming);
            timingAnalysis.put("timeVariance", calculateTimeVariance(times));

            // Simplified suspicion score for timing
            double timingSuspicion = 0.0;
            if (hasVeryFastAnswers)
                timingSuspicion += 0.8;
            else if (hasFastAnswers)
                timingSuspicion += 0.4;
            if (hasUniformTiming && totalQuestions > 5)
                timingSuspicion += 0.3;
            if (averageTime < 5)
                timingSuspicion += 0.3;

            timingAnalysis.put("suspicionScore", Math.min(1.0, timingSuspicion));
        } else {
            timingAnalysis.put("suspicionScore", 0.0);
        }

        return timingAnalysis;
    }

    /**
     * Analyze answer changes ONLY
     */
    private Map<String, Object> analyzeAnswerChanges(CheatDetectionRequest.AnalyzeAnswerPatternsRequest request) {
        Map<String, Object> changeAnalysis = new HashMap<>();

        if (request.getAnswerHistory() != null && !request.getAnswerHistory().isEmpty()) {
            int totalChanges = 0;
            int questionsWithChanges = 0;
            List<Integer> changesPerQuestion = new ArrayList<>();

            for (Map.Entry<String, List<String>> entry : request.getAnswerHistory().entrySet()) {
                List<String> history = entry.getValue();
                if (history.size() > 1) {
                    questionsWithChanges++;
                    int changes = history.size() - 1;
                    totalChanges += changes;
                    changesPerQuestion.add(changes);
                }
            }

            double averageChangesPerQuestion = questionsWithChanges > 0 ? (double) totalChanges / questionsWithChanges
                    : 0.0;

            boolean hasExcessiveChanges = changesPerQuestion.stream().anyMatch(c -> c > 10);
            boolean hasVeryExcessiveChanges = changesPerQuestion.stream().anyMatch(c -> c > 20);

            changeAnalysis.put("totalChanges", totalChanges);
            changeAnalysis.put("questionsWithChanges", questionsWithChanges);
            changeAnalysis.put("averageChangesPerQuestion", averageChangesPerQuestion);
            changeAnalysis.put("hasExcessiveChanges", hasExcessiveChanges);
            changeAnalysis.put("hasVeryExcessiveChanges", hasVeryExcessiveChanges);

            // Simplified suspicion score for changes
            double changeSuspicion = 0.0;
            if (hasVeryExcessiveChanges)
                changeSuspicion += 0.8;
            else if (hasExcessiveChanges)
                changeSuspicion += 0.4;
            if (averageChangesPerQuestion > 8)
                changeSuspicion += 0.3;

            changeAnalysis.put("suspicionScore", Math.min(1.0, changeSuspicion));
        } else {
            changeAnalysis.put("suspicionScore", 0.0);
        }

        return changeAnalysis;
    }

    /**
     * SIMPLIFIED: Calculate overall suspicion score - HANYA TIMING & CHANGES
     */
    private double calculateSuspicionScore(Map<String, Object> timingAnalysis,
            Map<String, Object> sequenceAnalysis,
            Map<String, Object> changeAnalysis) {
        double timingScore = (Double) timingAnalysis.getOrDefault("suspicionScore", 0.0);
        double changeScore = (Double) changeAnalysis.getOrDefault("suspicionScore", 0.0);

        // Simplified weighted average (timing lebih penting)
        return (timingScore * 0.7) + (changeScore * 0.3);
    }

    // KEEP ONLY: calculateTimeVariance method
    private double calculateTimeVariance(List<Integer> times) {
        double mean = times.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double variance = times.stream()
                .mapToDouble(time -> Math.pow(time - mean, 2))
                .average()
                .orElse(0.0);

        return Math.sqrt(variance); // Standard deviation
    }

    // ==================== STATISTICS HELPER METHODS ====================

    private Map<String, Object> generateBasicStatistics(List<CheatDetection> violations) {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalViolations", violations.size());
        stats.put("uniqueSessions", violations.stream().map(CheatDetection::getSessionId).distinct().count());
        stats.put("uniqueParticipants", violations.stream().map(CheatDetection::getIdPeserta).distinct().count());

        // Count by severity
        Map<String, Long> severityCounts = violations.stream()
                .collect(Collectors.groupingBy(CheatDetection::getSeverity, Collectors.counting()));
        stats.put("bySeverity", severityCounts);

        // Count by type
        Map<String, Long> typeCounts = violations.stream()
                .collect(Collectors.groupingBy(CheatDetection::getTypeViolation, Collectors.counting()));
        stats.put("byType", typeCounts);

        // Count resolved vs unresolved
        long resolvedCount = violations.stream().filter(CheatDetection::isResolved).count();
        stats.put("resolved", resolvedCount);
        stats.put("unresolved", violations.size() - resolvedCount);

        return stats;
    }

    private Map<String, Object> generateGroupedStatistics(List<CheatDetection> violations, String groupBy) {
        Map<String, Object> groupedStats = new HashMap<>();

        switch (groupBy.toLowerCase()) {
            case "type":
                groupedStats = violations.stream()
                        .collect(Collectors.groupingBy(CheatDetection::getTypeViolation, Collectors.counting()))
                        .entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                break;

            case "severity":
                groupedStats = violations.stream()
                        .collect(Collectors.groupingBy(CheatDetection::getSeverity, Collectors.counting()))
                        .entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                break;

            case "peserta":
                groupedStats = violations.stream()
                        .collect(Collectors.groupingBy(CheatDetection::getIdPeserta, Collectors.counting()))
                        .entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                break;

            case "ujian":
                groupedStats = violations.stream()
                        .collect(Collectors.groupingBy(CheatDetection::getIdUjian, Collectors.counting()))
                        .entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                break;

            default:
                groupedStats.put("error", "Unsupported groupBy: " + groupBy);
        }

        return groupedStats;
    }

    private Map<String, Object> generateTrendsAnalysis(List<CheatDetection> violations) {
        Map<String, Object> trends = new HashMap<>();

        // Group violations by hour of day
        Map<Integer, Long> hourlyDistribution = violations.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getDetectedAt().atZone(java.time.ZoneId.systemDefault()).getHour(),
                        Collectors.counting()));

        trends.put("hourlyDistribution", hourlyDistribution);

        // Group violations by day for last 7 days
        Map<String, Long> dailyDistribution = violations.stream()
                .filter(v -> v.getDetectedAt().isAfter(Instant.now().minus(7, ChronoUnit.DAYS)))
                .collect(Collectors.groupingBy(
                        v -> v.getDetectedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString(),
                        Collectors.counting()));

        trends.put("dailyDistribution", dailyDistribution);

        return trends;
    }

    private Map<String, Object> generateTopViolatorsAnalysis(List<CheatDetection> violations) {
        Map<String, Object> topViolators = new HashMap<>();

        // Top violators by count
        Map<String, Long> violatorCounts = violations.stream()
                .collect(Collectors.groupingBy(CheatDetection::getIdPeserta, Collectors.counting()));

        List<Map.Entry<String, Long>> sortedViolators = violatorCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());

        topViolators.put("byCount", sortedViolators);

        // Top violation types
        Map<String, Long> typeCounts = violations.stream()
                .collect(Collectors.groupingBy(CheatDetection::getTypeViolation, Collectors.counting()));

        List<Map.Entry<String, Long>> sortedTypes = typeCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());

        topViolators.put("byType", sortedTypes);

        return topViolators;
    }

    // ==================== VALIDATION METHODS ====================

    private void validateRecordViolationRequest(CheatDetectionRequest.RecordViolationRequest request) {
        if (request.getSessionId() == null || request.getSessionId().trim().isEmpty()) {
            throw new BadRequestException("Session ID wajib diisi");
        }
        if (request.getIdPeserta() == null || request.getIdPeserta().trim().isEmpty()) {
            throw new BadRequestException("ID peserta wajib diisi");
        }
        if (request.getIdUjian() == null || request.getIdUjian().trim().isEmpty()) {
            throw new BadRequestException("ID ujian wajib diisi");
        }
        if (request.getTypeViolation() == null || request.getTypeViolation().trim().isEmpty()) {
            throw new BadRequestException("Type violation wajib diisi");
        }
    }

    private void validateBatchRecordRequest(CheatDetectionRequest.BatchRecordViolationsRequest request) {
        if (request.getSessionId() == null || request.getSessionId().trim().isEmpty()) {
            throw new BadRequestException("Session ID wajib diisi");
        }
        if (request.getViolations() == null || request.getViolations().isEmpty()) {
            throw new BadRequestException("Violations wajib diisi");
        }
    }

    private void validateAnalyzeRequest(CheatDetectionRequest.AnalyzeAnswerPatternsRequest request) {
        if (request.getSessionId() == null || request.getSessionId().trim().isEmpty()) {
            throw new BadRequestException("Session ID wajib diisi");
        }
    }

    private void validateTakeActionRequest(CheatDetectionRequest.TakeActionRequest request) {
        if (request.getIdDetection() == null || request.getIdDetection().trim().isEmpty()) {
            throw new BadRequestException("ID detection wajib diisi");
        }
        if (request.getActionTaken() == null || request.getActionTaken().trim().isEmpty()) {
            throw new BadRequestException("Action taken wajib diisi");
        }
    }

    private void validateResolveRequest(CheatDetectionRequest.ResolveViolationRequest request) {
        if (request.getIdDetection() == null || request.getIdDetection().trim().isEmpty()) {
            throw new BadRequestException("ID detection wajib diisi");
        }
        if (request.getResolvedBy() == null || request.getResolvedBy().trim().isEmpty()) {
            throw new BadRequestException("Resolved by wajib diisi");
        }
    }

    private void validateGetViolationsRequest(CheatDetectionRequest.GetViolationsRequest request) {
        if (request.getLimit() == null || request.getLimit() <= 0) {
            request.setLimit(50);
        }
        if (request.getLimit() > AppConstants.MAX_PAGE_SIZE) {
            request.setLimit(AppConstants.MAX_PAGE_SIZE);
        }
    }

    private void validateStatisticsRequest(CheatDetectionRequest.GenerateStatisticsRequest request) {
        if (request.getTimeRange() == null) {
            request.setTimeRange("WEEK");
        }
        if (request.getGroupBy() == null) {
            request.setGroupBy("TYPE");
        }
    }

    private void validateExportRequest(CheatDetectionRequest.ExportViolationsRequest request) {
        if (request.getFormat() == null) {
            request.setFormat("PDF");
        }
    }

    // ==================== UTILITY METHODS ====================

    private List<CheatDetection> applyFilters(List<CheatDetection> violations,
            CheatDetectionRequest.GetViolationsRequest request) {
        return violations.stream()
                .filter(v -> request.getTypeViolation() == null
                        || request.getTypeViolation().equals(v.getTypeViolation()))
                .filter(v -> request.getSeverity() == null || request.getSeverity().equals(v.getSeverity()))
                .filter(v -> request.getResolved() == null || request.getResolved().equals(v.getResolved()))
                .filter(v -> applyTimeRangeFilter(v, request.getTimeRange()))
                .collect(Collectors.toList());
    }

    private boolean applyTimeRangeFilter(CheatDetection violation, String timeRange) {
        if (timeRange == null || "ALL".equals(timeRange)) {
            return true;
        }

        // Check if detectedAt is null, if so, exclude from filter or use createdAt as
        // fallback
        Instant violationTime = violation.getDetectedAt();
        if (violationTime == null) {
            violationTime = violation.getCreatedAt();
            if (violationTime == null) {
                // If both detectedAt and createdAt are null, exclude from results
                logger.warn("CheatDetection {} has null detectedAt and createdAt, excluding from time filter",
                        violation.getIdDetection());
                return false;
            }
        }

        Instant now = Instant.now();
        Instant threshold;

        switch (timeRange) {
            case "LAST_HOUR":
                threshold = now.minus(1, ChronoUnit.HOURS);
                break;
            case "LAST_DAY":
                threshold = now.minus(1, ChronoUnit.DAYS);
                break;
            case "LAST_WEEK":
                threshold = now.minus(7, ChronoUnit.DAYS);
                break;
            default:
                return true;
        }

        return violationTime.isAfter(threshold);
    }

    private List<CheatDetection> applySorting(List<CheatDetection> violations,
            CheatDetectionRequest.GetViolationsRequest request) {
        Comparator<CheatDetection> comparator;

        switch (request.getSortBy()) {
            case "SEVERITY":
                comparator = Comparator.comparing(v -> SeverityLevel.getSeverityWeight(v.getSeverity()));
                break;
            case "TYPE":
                comparator = Comparator.comparing(CheatDetection::getTypeViolation);
                break;
            case "TIMESTAMP":
            default:
                // Handle null detectedAt by using createdAt as fallback
                comparator = Comparator.comparing(v -> {
                    Instant time = v.getDetectedAt();
                    if (time == null) {
                        time = v.getCreatedAt();
                        if (time == null) {
                            // If both are null, use current time to put at end
                            return Instant.now();
                        }
                    }
                    return time;
                });
                break;
        }

        if ("DESC".equals(request.getSortOrder())) {
            comparator = comparator.reversed();
        }

        return violations.stream().sorted(comparator).collect(Collectors.toList());
    }

    private List<CheatDetection> applyPagination(List<CheatDetection> violations,
            CheatDetectionRequest.GetViolationsRequest request) {
        return violations.stream()
                .limit(request.getLimit())
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> formatViolations(List<CheatDetection> violations,
            CheatDetectionRequest.GetViolationsRequest request) {
        return violations.stream().map(violation -> {
            Map<String, Object> formatted = new HashMap<>();
            formatted.put("idDetection", violation.getIdDetection());
            formatted.put("sessionId", violation.getSessionId());
            formatted.put("typeViolation", violation.getTypeViolation());
            formatted.put("severity", violation.getSeverity());
            formatted.put("violationCount", violation.getViolationCount());
            formatted.put("detectedAt", violation.getDetectedAt());
            formatted.put("resolved", violation.getResolved());
            formatted.put("actionTaken", violation.getActionTaken());

            if (request.getIncludeEvidence() && violation.getEvidence() != null) {
                formatted.put("evidence", violation.getEvidence());
            }

            if (request.getIncludeActions()) {
                Map<String, Object> actionInfo = new HashMap<>();
                actionInfo.put("actionTaken", violation.getActionTaken());
                actionInfo.put("actionBy", violation.getActionBy());
                actionInfo.put("actionAt", violation.getActionAt());
                actionInfo.put("actionReason", violation.getActionReason());
                formatted.put("actionInfo", actionInfo);
            }

            return formatted;
        }).collect(Collectors.toList());
    }

    private List<CheatDetection> getViolationsForStatistics(CheatDetectionRequest.GenerateStatisticsRequest request)
            throws IOException {
        List<CheatDetection> violations;

        if (request.getIdSchool() != null) {
            violations = cheatDetectionRepository.findBySchoolId(request.getIdSchool());
        } else {
            violations = cheatDetectionRepository.findAll(10000);
        }

        // Apply time range filter
        return violations.stream()
                .filter(v -> applyTimeRangeFilter(v, request.getTimeRange()))
                .filter(v -> request.getIncludeResolvedViolations() || !v.isResolved())
                .collect(Collectors.toList());
    }

    private List<CheatDetection> getViolationsForExport(CheatDetectionRequest.ExportViolationsRequest request)
            throws IOException {
        List<CheatDetection> violations;

        if (request.getIdSchool() != null) {
            violations = cheatDetectionRepository.findBySchoolId(request.getIdSchool());
        } else {
            violations = cheatDetectionRepository.findAll(10000);
        }

        // Apply filters
        return violations.stream()
                .filter(v -> applyTimeRangeFilter(v, request.getTimeRange()))
                .filter(v -> request.getViolationTypes() == null ||
                        java.util.Arrays.asList(request.getViolationTypes()).contains(v.getTypeViolation()))
                .filter(v -> request.getSeverityLevels() == null ||
                        java.util.Arrays.asList(request.getSeverityLevels()).contains(v.getSeverity()))
                .collect(Collectors.toList());
    }

    private List<String> generateRecommendations(List<CheatDetection> violations) {
        List<String> recommendations = new ArrayList<>();

        Map<String, Long> typeCounts = violations.stream()
                .collect(Collectors.groupingBy(CheatDetection::getTypeViolation, Collectors.counting()));

        // Generate recommendations based on violation patterns
        typeCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> {
                    String type = entry.getKey();
                    // Long count = entry.getValue(); // REMOVED unused variable

                    switch (type) {
                        case ViolationType.TAB_SWITCH:
                            recommendations.add(
                                    "Implementasikan fullscreen mode yang lebih ketat dan monitoring tab switching");
                            break;
                        case ViolationType.COPY_PASTE:
                            recommendations
                                    .add("Disable copy-paste functionality pada browser dan monitoring clipboard");
                            break;
                        case ViolationType.BROWSER_DEV_TOOLS:
                            recommendations
                                    .add("Implementasikan deteksi developer tools dan automatic session termination");
                            break;
                        default:
                            recommendations
                                    .add("Review dan perketat pengaturan keamanan untuk violation type: " + type);
                    }
                });

        if (recommendations.isEmpty()) {
            recommendations.add("Sistem keamanan berjalan dengan baik, lanjutkan monitoring");
        }

        return recommendations;
    }

    private String generateFileName(CheatDetectionRequest.ExportViolationsRequest request) {
        String timestamp = Instant.now().toString().replaceAll("[:.T-]", "");
        return String.format("cheat_violations_%s_%s.%s",
                request.getTimeRange().toLowerCase(),
                timestamp.substring(0, 8),
                request.getFormat().toLowerCase());
    }
}