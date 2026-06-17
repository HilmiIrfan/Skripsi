package com.doyatama.university.controller;

import com.doyatama.university.model.CheatDetection;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.CheatDetectionRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.CheatDetectionService;
import com.doyatama.university.util.AppConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/cheat-detection")
public class CheatDetectionController {

    @Autowired
    private CheatDetectionService cheatDetectionService;

    private static final Logger logger = LoggerFactory.getLogger(CheatDetectionController.class);

    // ==================== VIOLATION RECORDING ====================

    /**
     * Record single violation from frontend
     */
    @PostMapping("/record-violation")
    public ResponseEntity<?> recordViolation(
            @Valid @RequestBody CheatDetectionRequest.RecordViolationRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Recording violation: {} for session: {}", request.getTypeViolation(), request.getSessionId());
            logger.debug("Request payload - idPeserta: {}, idUjian: {}, evidence: {}",
                    request.getIdPeserta(), request.getIdUjian(), request.getEvidence()); // Validate user access to
                                                                                          // this session
            if (!request.getIdPeserta().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak - session bukan milik user ini"));
            }

            // Set school ID from current user if not provided
            if (request.getIdSchool() == null) {
                if (currentUser.getSchoolId() != null) {
                    request.setIdSchool(currentUser.getSchoolId());
                } else {
                    logger.warn("No school ID available for user: {}", currentUser.getId());
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "User tidak memiliki school ID"));
                }
            }

            CheatDetection detection = cheatDetectionService.recordViolation(request);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{detectionId}")
                    .buildAndExpand(detection.getIdDetection()).toUri();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("detectionId", detection.getIdDetection());
            responseData.put("violationType", detection.getTypeViolation());
            responseData.put("severity", detection.getSeverity());
            responseData.put("violationCount", detection.getViolationCount());
            responseData.put("actionTaken", detection.getActionTaken());
            responseData.put("detectedAt", detection.getDetectedAt());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Violation berhasil dicatat");
            response.put("data", responseData);

            return ResponseEntity.created(location).body(response);

        } catch (IllegalArgumentException e) {
            logger.error("Bad request in recordViolation: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error recording violation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Record multiple violations in batch
     */
    @PostMapping("/record-batch")
    public ResponseEntity<?> recordViolationBatch(
            @Valid @RequestBody CheatDetectionRequest.BatchRecordViolationsRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Recording batch violations for session: {}", request.getSessionId());

            // Validate user access
            if (!request.getIdPeserta().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            // Set school ID from current user if not provided
            if (request.getIdSchool() == null) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            Map<String, Object> batchResult = cheatDetectionService.recordViolationBatch(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Batch violations berhasil diproses");
            response.put("data", batchResult);

            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error recording batch violations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== PATTERN ANALYSIS ====================

    /**
     * Analyze answer patterns for suspicious behavior
     */
    @PostMapping("/analyze-patterns")
    public ResponseEntity<?> analyzeAnswerPatterns(
            @Valid @RequestBody CheatDetectionRequest.AnalyzeAnswerPatternsRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Analyzing patterns for session: {}", request.getSessionId());

            // Validate user access (admin/proctor can analyze any session)
            if (!hasAnalysisPermission(currentUser, request.getSessionId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak - tidak memiliki permission untuk analisis"));
            }

            Map<String, Object> analysisResult = cheatDetectionService.analyzeAnswerPatterns(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Analisis pattern berhasil dilakukan");
            response.put("data", analysisResult);

            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error analyzing patterns", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== ACTION MANAGEMENT ====================

    /**
     * Take action on a violation (admin/proctor only)
     */
    @PostMapping("/take-action")
    public ResponseEntity<?> takeAction(
            @Valid @RequestBody CheatDetectionRequest.TakeActionRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Taking action on violation: {}", request.getIdDetection());

            // Validate user has permission to take action
            if (!hasActionPermission(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false,
                                "Akses ditolak - hanya admin/proctor yang dapat mengambil tindakan"));
            }

            // Set actionBy from current user
            request.setActionBy(currentUser.getId());

            CheatDetection detection = cheatDetectionService.takeAction(request);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("detectionId", detection.getIdDetection());
            responseData.put("actionTaken", detection.getActionTaken());
            responseData.put("actionBy", detection.getActionBy());
            responseData.put("actionAt", detection.getActionAt());
            responseData.put("actionReason", detection.getActionReason());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tindakan berhasil diambil");
            response.put("data", responseData);

            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error taking action", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Resolve violation (mark as reviewed)
     */
    @PostMapping("/resolve")
    public ResponseEntity<?> resolveViolation(
            @Valid @RequestBody CheatDetectionRequest.ResolveViolationRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Resolving violation: {}", request.getIdDetection());

            // Validate user has permission to resolve
            if (!hasActionPermission(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false,
                                "Akses ditolak - hanya admin/proctor yang dapat resolve violation"));
            }

            // Set resolvedBy from current user
            request.setResolvedBy(currentUser.getId());

            CheatDetection detection = cheatDetectionService.resolveViolation(request);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("detectionId", detection.getIdDetection());
            responseData.put("resolved", detection.getResolved());
            responseData.put("resolvedBy", detection.getResolvedBy());
            responseData.put("resolvedAt", detection.getResolvedAt());
            responseData.put("resolutionNotes", detection.getResolutionNotes());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Violation berhasil di-resolve");
            response.put("data", responseData);

            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error resolving violation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== QUERY METHODS ====================

    /**
     * Get violations by criteria
     */
    @GetMapping("/violations")
    public ResponseEntity<?> getViolations(
            @RequestParam(value = "sessionId", required = false) String sessionId,
            @RequestParam(value = "idUjian", required = false) String idUjian,
            @RequestParam(value = "idPeserta", required = false) String idPeserta,
            @RequestParam(value = "typeViolation", required = false) String typeViolation,
            @RequestParam(value = "severity", required = false) String severity,
            @RequestParam(value = "resolved", required = false) Boolean resolved,
            @RequestParam(value = "timeRange", defaultValue = "ALL") String timeRange,
            @RequestParam(value = "sortBy", defaultValue = "TIMESTAMP") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder,
            @RequestParam(value = "limit", defaultValue = "50") Integer limit,
            @RequestParam(value = "includeEvidence", defaultValue = "false") Boolean includeEvidence,
            @RequestParam(value = "includeActions", defaultValue = "true") Boolean includeActions,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Getting violations with criteria");

            // Create request object
            CheatDetectionRequest.GetViolationsRequest request = new CheatDetectionRequest.GetViolationsRequest();
            request.setSessionId(sessionId);
            request.setIdUjian(idUjian);
            request.setIdPeserta(idPeserta);
            request.setTypeViolation(typeViolation);
            request.setSeverity(severity);
            request.setResolved(resolved);
            request.setTimeRange(timeRange);
            request.setSortBy(sortBy);
            request.setSortOrder(sortOrder);
            request.setLimit(limit);
            request.setIncludeEvidence(includeEvidence);
            request.setIncludeActions(includeActions);

            // Set school filter for non-admin users
            if (!isAdmin(currentUser)) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            // Additional access control for student users
            if (isStudent(currentUser) && idPeserta == null) {
                request.setIdPeserta(currentUser.getId()); // Students can only see their own violations
            }

            Map<String, Object> violationsResult = cheatDetectionService.getViolations(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Violations berhasil diambil");
            response.put("data", violationsResult);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting violations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get violation by ID
     */
    @GetMapping("/violations/{detectionId}")
    public ResponseEntity<?> getViolationById(
            @PathVariable String detectionId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Getting violation by ID: {}", detectionId);

            // This would require implementing findById in service, for now use
            // getViolations with filter
            CheatDetectionRequest.GetViolationsRequest request = new CheatDetectionRequest.GetViolationsRequest();
            request.setLimit(1);
            request.setIncludeEvidence(true);
            request.setIncludeActions(true);

            // Set school filter for non-admin users
            if (!isAdmin(currentUser)) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            Map<String, Object> violations = cheatDetectionService.getViolations(request);

            // Filter by detection ID (this is not efficient, ideally implement proper
            // findById)
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> violationList = (List<Map<String, Object>>) violations.get("violations");

            Map<String, Object> violation = violationList.stream()
                    .filter(v -> detectionId.equals(v.get("idDetection")))
                    .findFirst()
                    .orElse(null);

            if (violation == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Violation berhasil ditemukan");
            response.put("data", violation);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting violation by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get violations by session ID
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getViolationsBySession(
            @PathVariable String sessionId,
            @RequestParam(value = "includeEvidence", defaultValue = "false") Boolean includeEvidence,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Getting violations for session: {}", sessionId);

            CheatDetectionRequest.GetViolationsRequest request = new CheatDetectionRequest.GetViolationsRequest();
            request.setSessionId(sessionId);
            request.setLimit(1000);
            request.setIncludeEvidence(includeEvidence);
            request.setIncludeActions(true);
            request.setSortBy("TIMESTAMP");
            request.setSortOrder("ASC");

            // Set school filter for non-admin users
            if (!isAdmin(currentUser)) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            Map<String, Object> violationsResult = cheatDetectionService.getViolations(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Session violations berhasil diambil");
            response.put("data", violationsResult);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting violations by session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get violations by ujian ID
     */
    @GetMapping("/ujian/{idUjian}")
    public ResponseEntity<?> getViolationsByUjian(
            @PathVariable String idUjian,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "severity", required = false) String severity,
            @RequestParam(value = "resolved", required = false) Boolean resolved,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Getting violations for ujian: {}", idUjian);

            CheatDetectionRequest.GetViolationsRequest request = new CheatDetectionRequest.GetViolationsRequest();
            request.setIdUjian(idUjian);
            request.setSeverity(severity);
            request.setResolved(resolved);
            request.setLimit(size);
            request.setIncludeActions(true);
            request.setSortBy("TIMESTAMP");
            request.setSortOrder("DESC");

            // Set school filter for non-admin users
            if (!isAdmin(currentUser)) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            Map<String, Object> violationsResult = cheatDetectionService.getViolations(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ujian violations berhasil diambil");
            response.put("data", violationsResult);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting violations by ujian", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== STATISTICS & REPORTS ====================

    /**
     * Generate violation statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> generateStatistics(
            @RequestParam(value = "timeRange", defaultValue = "WEEK") String timeRange,
            @RequestParam(value = "groupBy", defaultValue = "TYPE") String groupBy,
            @RequestParam(value = "includeTrends", defaultValue = "true") Boolean includeTrends,
            @RequestParam(value = "includeTopViolators", defaultValue = "true") Boolean includeTopViolators,
            @RequestParam(value = "includeResolvedViolations", defaultValue = "false") Boolean includeResolvedViolations,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Generating violation statistics");

            // Only admin/proctor can access statistics
            if (!hasAnalysisPermission(currentUser, null)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false,
                                "Akses ditolak - hanya admin/proctor yang dapat melihat statistik"));
            }

            CheatDetectionRequest.GenerateStatisticsRequest request = new CheatDetectionRequest.GenerateStatisticsRequest();
            request.setTimeRange(timeRange);
            request.setGroupBy(groupBy);
            request.setIncludeTrends(includeTrends);
            request.setIncludeTopViolators(includeTopViolators);
            request.setIncludeResolvedViolations(includeResolvedViolations);

            // Set school filter for non-admin users
            if (!isAdmin(currentUser)) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            Map<String, Object> statistics = cheatDetectionService.generateStatistics(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistik berhasil digenerate");
            response.put("data", statistics);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error generating statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Export violations report
     */
    @PostMapping("/export")
    public ResponseEntity<?> exportViolations(
            @Valid @RequestBody CheatDetectionRequest.ExportViolationsRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Exporting violations report in format: {}", request.getFormat());

            // Only admin/proctor can export
            if (!hasAnalysisPermission(currentUser, null)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak - hanya admin/proctor yang dapat export data"));
            }

            // Set school filter for non-admin users
            if (!isAdmin(currentUser) && request.getIdSchool() == null) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            Map<String, Object> exportResult = cheatDetectionService.exportViolations(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Report berhasil digenerate");
            response.put("data", exportResult);

            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error exporting violations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== MONITORING ENDPOINTS ====================

    /**
     * Get unresolved violations (for dashboard alert)
     */
    @GetMapping("/unresolved")
    public ResponseEntity<?> getUnresolvedViolations(
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "severity", required = false) String severity,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Getting unresolved violations");

            CheatDetectionRequest.GetViolationsRequest request = new CheatDetectionRequest.GetViolationsRequest();
            request.setResolved(false);
            request.setSeverity(severity);
            request.setLimit(limit);
            request.setSortBy("TIMESTAMP");
            request.setSortOrder("DESC");
            request.setIncludeActions(true);

            // Set school filter for non-admin users
            if (!isAdmin(currentUser)) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            Map<String, Object> violationsResult = cheatDetectionService.getViolations(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Unresolved violations berhasil diambil");
            response.put("data", violationsResult);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting unresolved violations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get critical violations (for immediate attention)
     */
    @GetMapping("/critical")
    public ResponseEntity<?> getCriticalViolations(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Getting critical violations");

            CheatDetectionRequest.GetViolationsRequest request = new CheatDetectionRequest.GetViolationsRequest();
            request.setSeverity("CRITICAL");
            request.setResolved(false);
            request.setLimit(limit);
            request.setSortBy("TIMESTAMP");
            request.setSortOrder("DESC");
            request.setIncludeActions(true);
            request.setIncludeEvidence(true);

            // Set school filter for non-admin users
            if (!isAdmin(currentUser)) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            Map<String, Object> violationsResult = cheatDetectionService.getViolations(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Critical violations berhasil diambil");
            response.put("data", violationsResult);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting critical violations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Check if user has permission to analyze patterns
     */
    private boolean hasAnalysisPermission(UserPrincipal currentUser, String sessionId) {
        // Admin and proctor can analyze any session
        if (isAdmin(currentUser) || isProctor(currentUser)) {
            return true;
        }

        // Students cannot analyze patterns
        return false;
    }

    /**
     * Check if user has permission to take action on violations
     */
    private boolean hasActionPermission(UserPrincipal currentUser) {
        return isAdmin(currentUser) || isProctor(currentUser);
    }

    /**
     * Check if user is admin
     */
    private boolean isAdmin(UserPrincipal currentUser) {
        if (currentUser == null || currentUser.getRoles() == null) {
            return false;
        }
        return currentUser.getRoles().equalsIgnoreCase("1"); // 1 = Administrator
    }

    /**
     * Check if user is proctor
     */
    private boolean isProctor(UserPrincipal currentUser) {
        if (currentUser == null || currentUser.getRoles() == null) {
            return false;
        }
        // 2 = Operator, 3 = Teacher (keduanya bisa jadi proctor)
        return currentUser.getRoles().equalsIgnoreCase("2") || currentUser.getRoles().equalsIgnoreCase("3");
    }

    /**
     * Check if user is student
     */
    private boolean isStudent(UserPrincipal currentUser) {
        if (currentUser == null || currentUser.getRoles() == null) {
            return false;
        }
        return currentUser.getRoles().equalsIgnoreCase("5"); // 5 = Student
    }
}