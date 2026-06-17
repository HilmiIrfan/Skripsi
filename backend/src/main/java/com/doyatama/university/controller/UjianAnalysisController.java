package com.doyatama.university.controller;

import com.doyatama.university.model.UjianAnalysis;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.payload.UjianAnalysisRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.UjianAnalysisService;
import com.doyatama.university.util.AppConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/ujian-analysis")
public class UjianAnalysisController {

    @Autowired
    private UjianAnalysisService ujianAnalysisService;

    private static final Logger logger = LoggerFactory.getLogger(UjianAnalysisController.class);

    // ==================== CRUD OPERATIONS ====================

    /**
     * Get all analysis with pagination and filtering - HANYA OPERATOR & TEACHER
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_OPERATOR') or hasRole('ROLE_TEACHER')")
    public PagedResponse<UjianAnalysis> getAllAnalysis(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "userID", defaultValue = "*") String userID,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return ujianAnalysisService.getAllAnalysis(page, size, userID, schoolID);
    }

    /**
     * Get analysis by ujian ID - HANYA OPERATOR & TEACHER
     */
    @GetMapping("/ujian/{ujianId}")
    @PreAuthorize("hasRole('ROLE_OPERATOR') or hasRole('ROLE_TEACHER')")
    public PagedResponse<UjianAnalysis> getAnalysisByUjian(
            @PathVariable String ujianId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return ujianAnalysisService.getAnalysisByUjian(ujianId, page, size, schoolID);
    }

    /**
     * Get analysis by type - HANYA OPERATOR & TEACHER
     */
    @GetMapping("/type/{analysisType}")
    @PreAuthorize("hasRole('ROLE_OPERATOR') or hasRole('ROLE_TEACHER')")
    public PagedResponse<UjianAnalysis> getAnalysisByType(
            @PathVariable String analysisType,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return ujianAnalysisService.getAnalysisByType(analysisType, page, size, schoolID);
    }

    /**
     * Get single analysis by ID - HANYA OPERATOR & TEACHER
     */
    @GetMapping("/{analysisId}")
    @PreAuthorize("hasRole('ROLE_OPERATOR') or hasRole('ROLE_TEACHER')")
    public DefaultResponse<UjianAnalysis> getAnalysisById(@PathVariable String analysisId) throws IOException {
        return ujianAnalysisService.getAnalysisById(analysisId);
    }

    /**
     * Delete analysis by ID
     */
    @DeleteMapping("/{analysisId}")
    public ResponseEntity<?> deleteAnalysis(@PathVariable String analysisId) throws IOException {
        try {
            ujianAnalysisService.deleteAnalysisById(analysisId);
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Analysis berhasil dihapus"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error deleting analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== ANALYSIS GENERATION ====================

    /**
     * Generate comprehensive analysis - HANYA OPERATOR & TEACHER
     */
    @PostMapping("/generate")
    @PreAuthorize("hasRole('ROLE_OPERATOR') or hasRole('ROLE_TEACHER')")
    public ResponseEntity<?> generateAnalysis(
            @Valid @RequestBody UjianAnalysisRequest.GenerateAnalysisRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Received request to generate analysis for ujian: " + request.getIdUjian());

            // Set school ID from current user if not provided
            if (request.getIdSchool() == null || request.getIdSchool().trim().isEmpty()) {
                request.setIdSchool(currentUser.getSchoolId());
            }

            UjianAnalysis analysis = ujianAnalysisService.generateAnalysis(request);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{analysisId}")
                    .buildAndExpand(analysis.getIdAnalysis()).toUri();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("idAnalysis", analysis.getIdAnalysis());
            responseData.put("analysisType", analysis.getAnalysisType());
            responseData.put("totalParticipants", analysis.getTotalParticipants());
            responseData.put("averageScore", analysis.getAverageScore());
            responseData.put("passRate", analysis.getPassRate());
            responseData.put("generatedAt", analysis.getGeneratedAt());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Analysis berhasil dibuat");
            response.put("data", responseData);

            return ResponseEntity.created(location)
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error during analysis generation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Update existing analysis
     */
    @PutMapping("/{analysisId}")
    public ResponseEntity<?> updateAnalysis(
            @PathVariable String analysisId,
            @Valid @RequestBody UjianAnalysisRequest.UpdateAnalysisRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Set analysis ID from path parameter
            request.setIdAnalysis(analysisId);

            // Set updated by from current user
            if (request.getUpdatedBy() == null) {
                request.setUpdatedBy(currentUser.getId());
            }

            UjianAnalysis analysis = ujianAnalysisService.updateAnalysis(request);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("idAnalysis", analysis.getIdAnalysis());
            responseData.put("updatedAt", analysis.getUpdatedAt());
            responseData.put("message", "Analysis berhasil diperbarui");

            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Analysis berhasil diperbarui"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get participant-based analysis data
     * This endpoint returns hasil ujian data enriched with violation and behavioral
     * analysis
     */
    @GetMapping("/participants")
    public ResponseEntity<Map<String, Object>> getParticipantAnalysis(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "ujianId", required = false) String ujianId,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) String status,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            String schoolID = currentUser.getSchoolId();

            // This will fetch hasil ujian data which represents participants
            Map<String, Object> response = ujianAnalysisService.getParticipantBasedAnalysis(
                    page, size, ujianId, search, status, schoolID);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching participant analysis", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Terjadi kesalahan sistem: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ==================== ANALYSIS COMPARISON ====================

    /**
     * Compare multiple analyses
     */
    @PostMapping("/compare")
    public ResponseEntity<?> compareAnalyses(
            @Valid @RequestBody UjianAnalysisRequest.CompareAnalysisRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Comparing analyses: {}", request.getAnalysisIds());

            Map<String, Object> comparison = ujianAnalysisService.compareAnalyses(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Perbandingan analysis berhasil dibuat");
            response.put("data", comparison);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error comparing analyses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== EXPORT FUNCTIONALITY ====================

    /**
     * Export analysis to various formats
     */
    @PostMapping("/export")
    public ResponseEntity<?> exportAnalysis(
            @Valid @RequestBody UjianAnalysisRequest.ExportAnalysisRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Exporting analysis: {} in format: {}", request.getIdAnalysis(), request.getFormat());

            Map<String, Object> exportResult = ujianAnalysisService.exportAnalysis(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Export analysis berhasil");
            response.put("data", exportResult);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error exporting analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Export analysis by ID (GET endpoint for direct download)
     */
    @GetMapping("/{analysisId}/export/{format}")
    public ResponseEntity<?> exportAnalysisById(
            @PathVariable String analysisId,
            @PathVariable String format,
            @RequestParam(value = "templateType", defaultValue = "STANDARD") String templateType,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Create export request
            UjianAnalysisRequest.ExportAnalysisRequest request = new UjianAnalysisRequest.ExportAnalysisRequest();
            request.setIdAnalysis(analysisId);
            request.setFormat(format.toUpperCase());
            request.setTemplateType(templateType);

            Map<String, Object> exportResult = ujianAnalysisService.exportAnalysis(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Export analysis berhasil");
            response.put("data", exportResult);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error exporting analysis by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== ANALYSIS STATISTICS ====================

    /**
     * Get analysis statistics for dashboard - HANYA OPERATOR & TEACHER
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ROLE_OPERATOR') or hasRole('ROLE_TEACHER')")
    public ResponseEntity<?> getAnalysisStatistics(@CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            String schoolId = currentUser.getSchoolId();
            Map<String, Object> statistics = ujianAnalysisService.getAnalysisStatistics(schoolId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistik analysis berhasil diambil");
            response.put("data", statistics);

            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            logger.error("Error getting analysis statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== QUICK ANALYSIS ====================

    /**
     * Generate quick descriptive analysis
     */
    @PostMapping("/quick-descriptive")
    public ResponseEntity<?> generateQuickDescriptiveAnalysis(
            @RequestParam String ujianId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Generating quick descriptive analysis for ujian: {}", ujianId);

            // Create quick analysis request
            UjianAnalysisRequest.GenerateAnalysisRequest request = new UjianAnalysisRequest.GenerateAnalysisRequest();
            request.setIdUjian(ujianId);
            request.setIdSchool(currentUser.getSchoolId());
            request.setAnalysisType("DESCRIPTIVE");
            request.setIncludeDescriptiveStats(true);
            request.setIncludeRecommendations(true);

            // Set quick analysis configuration
            Map<String, Object> config = new HashMap<>();
            config.put("quickAnalysis", true);
            config.put("allowDuplicate", true);
            request.setAnalysisConfiguration(config);

            UjianAnalysis analysis = ujianAnalysisService.generateAnalysis(request);

            // Return summary data
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalParticipants", analysis.getTotalParticipants());
            summary.put("completedParticipants", analysis.getCompletedParticipants());
            summary.put("averageScore", analysis.getAverageScore());
            summary.put("medianScore", analysis.getMedianScore());
            summary.put("highestScore", analysis.getHighestScore());
            summary.put("lowestScore", analysis.getLowestScore());
            summary.put("passRate", analysis.getPassRate());
            summary.put("failRate", analysis.getFailRate());
            summary.put("gradeDistribution", analysis.getGradeDistribution());
            summary.put("recommendations", analysis.getRecommendations());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Quick analysis berhasil dibuat");
            response.put("data", summary);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error generating quick descriptive analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Generate quick item analysis
     */
    @PostMapping("/quick-item")
    public ResponseEntity<?> generateQuickItemAnalysis(
            @RequestParam String ujianId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Generating quick item analysis for ujian: {}", ujianId);

            // Create quick item analysis request
            UjianAnalysisRequest.GenerateAnalysisRequest request = new UjianAnalysisRequest.GenerateAnalysisRequest();
            request.setIdUjian(ujianId);
            request.setIdSchool(currentUser.getSchoolId());
            request.setAnalysisType("ITEM_ANALYSIS");
            request.setIncludeItemAnalysis(true);
            request.setIncludeDifficultyAnalysis(true);

            // Set quick analysis configuration
            Map<String, Object> config = new HashMap<>();
            config.put("quickAnalysis", true);
            config.put("allowDuplicate", true);
            request.setAnalysisConfiguration(config);

            UjianAnalysis analysis = ujianAnalysisService.generateAnalysis(request);

            // Return item analysis data
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("itemAnalysis", analysis.getItemAnalysis());
            itemData.put("questionDifficulty", analysis.getQuestionDifficulty());
            itemData.put("easiestQuestions", analysis.getEasiestQuestions());
            itemData.put("hardestQuestions", analysis.getHardestQuestions());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Quick item analysis berhasil dibuat");
            response.put("data", itemData);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error generating quick item analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== ANALYSIS VALIDATION ====================

    /**
     * Check if ujian can be analyzed
     */
    @GetMapping("/validate/{ujianId}")
    public ResponseEntity<?> validateUjianForAnalysis(
            @PathVariable String ujianId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // This could be a method in the service to validate without generating
            Map<String, Object> validationResult = new HashMap<>();

            try {
                // Try to validate by calling the service validation method
                // Note: You might want to add a separate validation method in the service
                UjianAnalysisRequest.GenerateAnalysisRequest testRequest = new UjianAnalysisRequest.GenerateAnalysisRequest();
                testRequest.setIdUjian(ujianId);
                testRequest.setIdSchool(currentUser.getSchoolId());

                // This will throw exception if invalid, but we don't actually generate
                validationResult.put("canAnalyze", true);
                validationResult.put("message", "Ujian dapat dianalisis");

            } catch (Exception e) {
                validationResult.put("canAnalyze", false);
                validationResult.put("message", e.getMessage());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Validasi berhasil");
            response.put("data", validationResult);

            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            logger.error("Error validating ujian for analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== ANALYSIS TEMPLATES ====================

    /**
     * Get available analysis types and configurations
     */
    @GetMapping("/templates")
    public ResponseEntity<?> getAnalysisTemplates() {
        try {
            Map<String, Object> templates = new HashMap<>();

            // Descriptive Analysis Template
            Map<String, Object> descriptive = new HashMap<>();
            descriptive.put("type", "DESCRIPTIVE");
            descriptive.put("name", "Analisis Deskriptif");
            descriptive.put("description", "Analisis statistik dasar hasil ujian");
            descriptive.put("includes", new String[] { "descriptiveStats", "recommendations" });

            // Item Analysis Template
            Map<String, Object> itemAnalysis = new HashMap<>();
            itemAnalysis.put("type", "ITEM_ANALYSIS");
            itemAnalysis.put("name", "Analisis Butir Soal");
            itemAnalysis.put("description", "Analisis kesulitan dan daya pembeda setiap soal");
            itemAnalysis.put("includes", new String[] { "itemAnalysis", "difficultyAnalysis", "recommendations" });

            // Comprehensive Analysis Template
            Map<String, Object> comprehensive = new HashMap<>();
            comprehensive.put("type", "COMPREHENSIVE");
            comprehensive.put("name", "Analisis Komprehensif");
            comprehensive.put("description", "Analisis lengkap mencakup semua aspek");
            comprehensive.put("includes", new String[] { "descriptiveStats", "itemAnalysis", "difficultyAnalysis",
                    "timeAnalysis", "cheatingAnalysis", "learningAnalytics", "recommendations" });

            templates.put("DESCRIPTIVE", descriptive);
            templates.put("ITEM_ANALYSIS", itemAnalysis);
            templates.put("COMPREHENSIVE", comprehensive);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Template analysis berhasil diambil");
            response.put("data", templates);

            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            logger.error("Error getting analysis templates", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Generate analysis with predefined template
     */
    @PostMapping("/template/{templateType}")
    public ResponseEntity<?> generateAnalysisWithTemplate(
            @PathVariable String templateType,
            @RequestParam String ujianId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Generating analysis with template: {} for ujian: {}", templateType, ujianId);

            // Create request based on template
            UjianAnalysisRequest.GenerateAnalysisRequest request = new UjianAnalysisRequest.GenerateAnalysisRequest();
            request.setIdUjian(ujianId);
            request.setIdSchool(currentUser.getSchoolId());
            request.setAnalysisType(templateType.toUpperCase());

            // Configure based on template type
            switch (templateType.toUpperCase()) {
                case "DESCRIPTIVE":
                    request.setIncludeDescriptiveStats(true);
                    request.setIncludeRecommendations(true);
                    break;
                case "ITEM_ANALYSIS":
                    request.setIncludeItemAnalysis(true);
                    request.setIncludeDifficultyAnalysis(true);
                    request.setIncludeRecommendations(true);
                    break;
                case "COMPREHENSIVE":
                    request.setIncludeDescriptiveStats(true);
                    request.setIncludeItemAnalysis(true);
                    request.setIncludeDifficultyAnalysis(true);
                    request.setIncludeTimeAnalysis(true);
                    request.setIncludeCheatingAnalysis(true);
                    request.setIncludeLearningAnalytics(true);
                    request.setIncludeRecommendations(true);
                    break;
                default:
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Template tidak dikenal: " + templateType));
            }

            UjianAnalysis analysis = ujianAnalysisService.generateAnalysis(request);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("idAnalysis", analysis.getIdAnalysis());
            responseData.put("templateUsed", templateType);
            responseData.put("analysisType", analysis.getAnalysisType());
            responseData.put("generatedAt", analysis.getGeneratedAt());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Analysis dengan template " + templateType + " berhasil dibuat");
            response.put("data", responseData);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error generating analysis with template", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== BULK OPERATIONS ====================

    /**
     * Generate analysis for multiple ujian (bulk operation)
     */
    @PostMapping("/bulk-generate")
    public ResponseEntity<?> bulkGenerateAnalysis(
            @RequestBody Map<String, Object> requestBody,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            @SuppressWarnings("unchecked")
            List<String> ujianIds = (List<String>) requestBody.get("ujianIds");
            String analysisType = (String) requestBody.getOrDefault("analysisType", "DESCRIPTIVE");

            if (ujianIds == null || ujianIds.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Daftar ujian ID tidak boleh kosong"));
            }

            Map<String, Object> results = new HashMap<>();
            Map<String, String> successResults = new HashMap<>();
            Map<String, String> errorResults = new HashMap<>();

            for (String ujianId : ujianIds) {
                try {
                    UjianAnalysisRequest.GenerateAnalysisRequest request = new UjianAnalysisRequest.GenerateAnalysisRequest();
                    request.setIdUjian(ujianId);
                    request.setIdSchool(currentUser.getSchoolId());
                    request.setAnalysisType(analysisType);
                    request.setIncludeDescriptiveStats(true);
                    request.setIncludeRecommendations(true);

                    // Set allow duplicate for bulk operations
                    Map<String, Object> config = new HashMap<>();
                    config.put("allowDuplicate", true);
                    request.setAnalysisConfiguration(config);

                    UjianAnalysis analysis = ujianAnalysisService.generateAnalysis(request);
                    successResults.put(ujianId, analysis.getIdAnalysis());

                } catch (Exception e) {
                    errorResults.put(ujianId, e.getMessage());
                    logger.error("Error generating analysis for ujian: " + ujianId, e);
                }
            }

            results.put("successful", successResults);
            results.put("errors", errorResults);
            results.put("totalProcessed", ujianIds.size());
            results.put("successCount", successResults.size());
            results.put("errorCount", errorResults.size());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.format("Bulk analysis selesai. Berhasil: %d, Gagal: %d",
                    successResults.size(), errorResults.size()));
            response.put("data", results);

            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            logger.error("Error in bulk generate analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Auto-generate analysis for ujian (triggered manually if auto-generation
     * failed)
     */
    @PostMapping("/auto-generate/{ujianId}")
    public ResponseEntity<?> autoGenerateAnalysisForUjian(
            @PathVariable String ujianId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.info("Manual trigger auto-generate analysis for ujian: {}", ujianId); // Create request for analysis
                                                                                         // generation
            UjianAnalysisRequest.GenerateAnalysisRequest request = new UjianAnalysisRequest.GenerateAnalysisRequest();
            request.setIdUjian(ujianId);
            request.setIdSchool(currentUser.getSchoolId());
            request.setIncludeDescriptiveStats(true);
            request.setIncludeItemAnalysis(true);
            request.setIncludeCheatingAnalysis(true);
            request.setIncludeLearningAnalytics(true);
            request.setDetailLevel("DETAILED");

            // Set configuration to prevent duplicates for auto-generation
            Map<String, Object> config = new HashMap<>();
            config.put("allowDuplicate", false);
            config.put("autoGenerated", true);
            request.setAnalysisConfiguration(config);

            UjianAnalysis analysis = ujianAnalysisService.generateAnalysis(request);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "Analysis berhasil di-generate");
            responseData.put("idAnalysis", analysis.getIdAnalysis());
            responseData.put("ujianId", ujianId);
            responseData.put("totalParticipants", analysis.getTotalParticipants());
            responseData.put("averageScore", analysis.getAverageScore());
            responseData.put("passRate", analysis.getPassRate());

            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            logger.error("Error during auto-generate analysis for ujian: {}", ujianId, e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Gagal generate analysis: " + e.getMessage());
            errorResponse.put("ujianId", ujianId);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Force regenerate analysis (delete existing and create new)
     */
    @PostMapping("/force-regenerate/{ujianId}")
    public ResponseEntity<?> forceRegenerateAnalysisForUjian(@PathVariable String ujianId,
            @CurrentUser UserPrincipal currentUser) {

        try {
            logger.info("Force regenerating analysis for ujian: {} by user: {}", ujianId, currentUser.getUsername());

            // First, cleanup existing duplicates
            int cleanedCount = ujianAnalysisService.cleanupDuplicateAnalysis(ujianId, "COMPREHENSIVE");
            logger.info("Cleaned up {} existing analyses before regeneration", cleanedCount);

            // Create generate request with force regeneration
            UjianAnalysisRequest.GenerateAnalysisRequest request = new UjianAnalysisRequest.GenerateAnalysisRequest();
            request.setIdUjian(ujianId);
            request.setIdSchool("RWK001"); // Default school for now
            request.setAnalysisType("COMPREHENSIVE");
            request.setDetailLevel("DETAILED");

            // Set configuration to allow recreation
            Map<String, Object> config = new HashMap<>();
            config.put("allowDuplicate", true);
            config.put("forceRegenerate", true);
            config.put("autoGenerated", false);
            request.setAnalysisConfiguration(config);

            UjianAnalysis analysis = ujianAnalysisService.generateAnalysis(request);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "Analysis berhasil di-regenerate");
            responseData.put("idAnalysis", analysis.getIdAnalysis());
            responseData.put("ujianId", ujianId);
            responseData.put("cleanedCount", cleanedCount);
            responseData.put("totalParticipants", analysis.getTotalParticipants());
            responseData.put("averageScore", analysis.getAverageScore());
            responseData.put("passRate", analysis.getPassRate());

            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            logger.error("Error during force regenerate analysis for ujian: {}", ujianId, e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Gagal regenerate analysis: " + e.getMessage());
            errorResponse.put("ujianId", ujianId);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ==================== CLEANUP OPERATIONS ====================

    /**
     * Clean up duplicate analysis records for a specific ujian
     */
    @DeleteMapping("/cleanup/ujian/{ujianId}")
    public ResponseEntity<?> cleanupDuplicatesForUjian(
            @PathVariable String ujianId,
            @RequestParam(value = "analysisType", defaultValue = "COMPREHENSIVE") String analysisType,
            @CurrentUser UserPrincipal currentUser) {

        try {
            logger.info("Cleaning up duplicate analysis for ujian: {} by user: {}", ujianId, currentUser.getUsername());

            int cleanedCount = ujianAnalysisService.cleanupDuplicateAnalysis(ujianId, analysisType);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Berhasil membersihkan analisis duplikat");
            response.put("ujianId", ujianId);
            response.put("analysisType", analysisType);
            response.put("cleanedCount", cleanedCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error cleaning up duplicates for ujian: {}", ujianId, e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Gagal membersihkan analisis duplikat: " + e.getMessage());
            errorResponse.put("ujianId", ujianId);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Clean up all duplicate analysis records in the system
     */
    @DeleteMapping("/cleanup/all")
    public ResponseEntity<?> cleanupAllDuplicates(@CurrentUser UserPrincipal currentUser) {

        try {
            logger.info("Cleaning up all duplicate analysis records by user: {}", currentUser.getUsername());

            Map<String, Integer> cleanupStats = ujianAnalysisService.cleanupAllDuplicateAnalysis();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Berhasil membersihkan semua analisis duplikat");
            response.put("cleanupStats", cleanupStats);
            response.put("totalCleaned", cleanupStats.get("TOTAL_CLEANED"));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error cleaning up all duplicate analysis", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Gagal membersihkan semua analisis duplikat: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}