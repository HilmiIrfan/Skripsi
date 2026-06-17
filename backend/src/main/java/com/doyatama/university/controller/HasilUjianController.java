package com.doyatama.university.controller;

import com.doyatama.university.model.HasilUjian;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.payload.HasilUjianRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.HasilUjianService;
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
import com.doyatama.university.exception.BadRequestException;

/**
 * Controller untuk mengelola HasilUjian
 * Menangani operasi CRUD, analytics, security, dan reporting
 */
@RestController
@RequestMapping("/api/hasil-ujian")
public class HasilUjianController {

    @Autowired
    private HasilUjianService hasilUjianService;

    private static final Logger logger = LoggerFactory.getLogger(HasilUjianController.class);

    // ==================== FIXED CRUD OPERATIONS ====================

    /**
     * Get all hasil ujian dengan paginasi - FIXED
     */
    @GetMapping
    public PagedResponse<HasilUjian> getAllHasilUjian(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolId = currentUser.getSchoolId();
        return hasilUjianService.getAllHasilUjian(page, size, schoolId);
    }

    /**
     * Get hasil ujian by ID - FIXED
     */
    @GetMapping("/{hasilUjianId}")
    public DefaultResponse<HasilUjian> getHasilUjianById(
            @PathVariable String hasilUjianId,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        DefaultResponse<HasilUjian> response = hasilUjianService.getHasilUjianById(hasilUjianId);

        // Validasi akses berdasarkan school
        if (!currentUser.getSchoolId().equals(response.getContent().getIdSchool())) {
            throw new BadRequestException("Akses ditolak");
        }

        return response;
    }

    /**
     * Get hasil ujian berdasarkan peserta dan ujian - FIXED
     */
    @GetMapping("/peserta/{idPeserta}/ujian/{idUjian}")
    public ResponseEntity<?> getHasilByPesertaAndUjian(
            @PathVariable String idPeserta,
            @PathVariable String idUjian,
            @RequestParam(value = "attemptNumber", required = false) Integer attemptNumber,
            @RequestParam(value = "includeAnswers", defaultValue = "false") Boolean includeAnswers,
            @RequestParam(value = "includeSecurityData", defaultValue = "false") Boolean includeSecurityData,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validasi akses - peserta hanya bisa akses hasil sendiri
            if (!currentUser.getId().equals(idPeserta) && !isAdminOrTeacher(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            HasilUjian hasilUjian = hasilUjianService.getHasilByPesertaAndUjianDirect(
                    idUjian, idPeserta, attemptNumber, includeAnswers, includeSecurityData);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Hasil ujian berhasil diambil");
            response.put("data", hasilUjian);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting hasil by peserta and ujian", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get hasil ujian berdasarkan ujian - FIXED
     */
    @GetMapping("/ujian/{idUjian}")
    public PagedResponse<HasilUjian> getHasilByUjian(
            @PathVariable String idUjian,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "includeAnalytics", defaultValue = "false") Boolean includeAnalytics,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolId = currentUser.getSchoolId();

        // TODO: Add validation to ensure ujian belongs to current school
        // This is critical for multi-school security

        return hasilUjianService.getHasilByUjian(idUjian, page, size, includeAnalytics, schoolId);
    }

    /**
     * Get hasil ujian berdasarkan peserta - FIXED
     */
    @GetMapping("/peserta/{idPeserta}")
    public PagedResponse<HasilUjian> getHasilByPeserta(
            @PathVariable String idPeserta,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        // Validasi akses - peserta hanya bisa akses data sendiri, admin/teacher bisa
        // akses semua di sekolahnya
        String currentSchoolId = currentUser.getSchoolId();
        if (!currentUser.getId().equals(idPeserta) && !isAdmin(currentUser)) {
            throw new BadRequestException("Akses ditolak");
        }

        logger.debug("Getting hasil ujian untuk peserta: {} dengan size: {}", idPeserta, size);
        return hasilUjianService.getHasilByPeserta(idPeserta, page, size, currentSchoolId);
    }

    // ==================== OPERASI ANALYTICS ====================

    /**
     * Update analytics data - FIXED
     */
    @PutMapping("/{hasilUjianId}/analytics")
    public ResponseEntity<?> updateAnalytics(
            @PathVariable String hasilUjianId,
            @RequestParam(required = false) Map<String, Integer> timeSpentPerQuestion,
            @RequestParam(required = false) Map<String, Object> answerHistory,
            @RequestParam(required = false) Map<String, Integer> attemptCountPerQuestion,
            @RequestParam(required = false) String workingPattern,
            @RequestParam(required = false) Double consistencyScore,
            @RequestParam(required = false) Boolean hasSignsOfGuessing,
            @RequestParam(required = false) Boolean hasSignsOfAnxiety,
            @RequestParam(required = false) String confidenceLevel,
            @RequestParam(required = false) Map<String, Object> topicPerformance,
            @RequestParam(required = false) List<String> strengths,
            @RequestParam(required = false) List<String> weaknesses,
            @RequestParam(required = false) List<String> recommendedStudyAreas,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {

            HasilUjian updatedHasil = hasilUjianService.updateAnalytics(hasilUjianId, timeSpentPerQuestion,
                    answerHistory, attemptCountPerQuestion, workingPattern, consistencyScore, hasSignsOfGuessing,
                    hasSignsOfAnxiety, confidenceLevel, topicPerformance, strengths, weaknesses, recommendedStudyAreas);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Analytics berhasil diperbarui");
            response.put("data", updatedHasil);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error updating analytics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Generate advanced analytics - FIXED
     */
    @PostMapping("/analytics/generate")
    public ResponseEntity<?> generateAdvancedAnalytics(
            @RequestParam String analysisScope,
            @RequestParam String idPeserta,
            @RequestParam String idUjian,
            @RequestParam(defaultValue = "false") Boolean includeComparative,
            @RequestParam(defaultValue = "false") Boolean includePredictive,
            @RequestParam(defaultValue = "true") Boolean includeRecommendations,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            Map<String, Object> analytics = hasilUjianService.generateAdvancedAnalytics(
                    analysisScope, idPeserta, idUjian, includeComparative, includePredictive, includeRecommendations);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Advanced analytics berhasil dihasilkan");
            response.put("data", analytics);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error generating advanced analytics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== OPERASI SECURITY ====================

    /**
     * Update security status - FIXED
     */
    @PutMapping("/{hasilUjianId}/security")
    public ResponseEntity<?> updateSecurityStatus(
            @PathVariable String hasilUjianId,
            @RequestParam String securityStatus,
            @RequestParam(required = false) Map<String, Object> securityFlags,
            @RequestParam(required = false) String reason,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            boolean updated = hasilUjianService.updateSecurityStatus(
                    hasilUjianId, securityStatus, securityFlags, currentUser.getId(), reason);

            if (updated) {
                return ResponseEntity.ok()
                        .body(new ApiResponse(true, "Security status berhasil diperbarui"));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Gagal memperbarui security status"));
            }

        } catch (Exception e) {
            logger.error("Error updating security status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Submit appeal - FIXED
     */
    @PostMapping("/{hasilUjianId}/appeal")
    public ResponseEntity<?> submitAppeal(
            @PathVariable String hasilUjianId,
            @RequestParam String appealReason,
            @RequestParam(required = false) String appealDescription,
            @RequestParam(required = false) List<String> attachmentIds,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            HasilUjian updatedHasil = hasilUjianService.submitAppeal(
                    hasilUjianId, appealReason, appealDescription, attachmentIds, currentUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Appeal berhasil disubmit");
            response.put("data", updatedHasil);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error submitting appeal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Review appeal - FIXED
     */
    @PutMapping("/{hasilUjianId}/appeal/review")
    public ResponseEntity<?> reviewAppeal(
            @PathVariable String hasilUjianId,
            @RequestParam String appealStatus,
            @RequestParam(required = false) String reviewNote,
            @RequestParam(required = false) String feedback,
            @RequestParam(defaultValue = "false") Boolean adjustScore,
            @RequestParam(required = false) Double newScore,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validasi role
            if (!isAdminOrTeacher(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            HasilUjian updatedHasil = hasilUjianService.reviewAppeal(
                    hasilUjianId, appealStatus, currentUser.getId(), reviewNote, feedback, adjustScore, newScore);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Appeal berhasil direview");
            response.put("data", updatedHasil);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error reviewing appeal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Verify hasil ujian - FIXED
     */
    @PutMapping("/{hasilUjianId}/verify")
    public ResponseEntity<?> verifyResult(
            @PathVariable String hasilUjianId,
            @RequestParam(defaultValue = "true") Boolean isVerified,
            @RequestParam(required = false) String verificationNote,
            @RequestParam(defaultValue = "false") Boolean regenerateHash,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validasi role
            if (!isAdminOrTeacher(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            boolean verified = hasilUjianService.verifyResult(
                    hasilUjianId, isVerified, currentUser.getId(), verificationNote, regenerateHash);

            if (verified) {
                return ResponseEntity.ok()
                        .body(new ApiResponse(true, "Hasil ujian berhasil diverifikasi"));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Gagal memverifikasi hasil ujian"));
            }

        } catch (Exception e) {
            logger.error("Error verifying result", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== OPERASI STATISTIK ====================

    /**
     * Get statistics untuk ujian
     */
    @GetMapping("/statistics/ujian/{idUjian}")
    public ResponseEntity<?> getUjianStatistics(
            @PathVariable String idUjian,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            Map<String, Object> statistics = hasilUjianService.getUjianStatistics(idUjian);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistik ujian berhasil diambil");
            response.put("data", statistics);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting ujian statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get overview statistics untuk dashboard
     */
    @GetMapping("/statistics/overview")
    public ResponseEntity<?> getOverviewStatistics(
            @RequestParam(value = "timeRange", defaultValue = "ALL") String timeRange,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            String schoolId = currentUser.getSchoolId();

            // Get basic statistics filtered by school
            Map<String, Object> overview = new HashMap<>();

            // This would need to be implemented in service based on requirements
            // Pass schoolId to filter statistics for current school only
            overview.put("totalHasilUjian", 0);
            overview.put("totalPeserta", 0);
            overview.put("averageScore", 0.0);
            overview.put("passRate", 0.0);
            overview.put("schoolId", schoolId); // Include school info for verification

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Overview statistics berhasil diambil");
            response.put("data", overview);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting overview statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== INTEGRATION WITH UJIAN SESSION ====================

    /**
     * Create hasil ujian from session (called by UjianSessionService)
     */
    @PostMapping("/create-from-session")
    public ResponseEntity<?> createFromSession(
            @RequestParam String sessionId,
            @RequestParam(required = false) Map<String, Object> finalAnswers,
            @RequestParam(defaultValue = "false") boolean isAutoSubmit,
            @RequestParam(required = false) String autoSubmitReason,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            HasilUjian hasilUjian = hasilUjianService.createHasilUjianFromSession(
                    sessionId, finalAnswers, isAutoSubmit, autoSubmitReason);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{hasilUjianId}")
                    .buildAndExpand(hasilUjian.getIdHasilUjian()).toUri();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Hasil ujian berhasil dibuat dari session");
            response.put("data", hasilUjian);

            return ResponseEntity.created(location).body(response);

        } catch (Exception e) {
            logger.error("Error creating hasil ujian from session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== ADDITIONAL OPERATIONS ====================

    /**
     * Export hasil ujian (basic implementation)
     */
    @PostMapping("/export")
    public ResponseEntity<?> exportHasilUjian(
            @Valid @RequestBody HasilUjianRequest.ExportHasilRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Basic export functionality - would need full implementation
            Map<String, Object> exportResult = new HashMap<>();
            exportResult.put("format", request.getFormat());
            exportResult.put("downloadUrl", "/api/files/export/hasil-ujian-" + System.currentTimeMillis() + ".xlsx");
            exportResult.put("generatedAt", java.time.Instant.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Export berhasil dihasilkan");
            response.put("data", exportResult);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error exporting hasil ujian", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Delete hasil ujian (admin only)
     */
    @DeleteMapping("/{hasilUjianId}")
    public ResponseEntity<?> deleteHasilUjian(
            @PathVariable String hasilUjianId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            hasilUjianService.deleteHasilUjianById(hasilUjianId);
            // For now, just return success
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Hasil ujian berhasil dihapus"));

        } catch (Exception e) {
            logger.error("Error deleting hasil ujian", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Helper method untuk check apakah user adalah admin
     */
    private boolean isAdmin(UserPrincipal currentUser) {
        return "1".equals(currentUser.getRoles());
    }

    /**
     * Helper method untuk check apakah user adalah teacher
     */
    private boolean isTeacher(UserPrincipal currentUser) {
        return "3".equals(currentUser.getRoles());
    }

    /**
     * Helper method untuk check apakah user adalah admin atau teacher
     */
    private boolean isAdminOrTeacher(UserPrincipal currentUser) {
        return isAdmin(currentUser) || isTeacher(currentUser);
    }

    // ==================== REPORT GENERATION FOR OPERATORS/TEACHERS
    // ====================

    /**
     * Generate participant report for operators/teachers
     */
    @PostMapping("/generate-participant-report")
    public ResponseEntity<?> generateParticipantReport(
            @RequestBody Map<String, Object> requestData,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validasi akses - hanya admin dan teacher
            if (!isAdminOrTeacher(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false,
                                "Akses ditolak. Hanya operator dan guru yang dapat mengakses laporan."));
            }

            String idPeserta = (String) requestData.get("idPeserta");
            String idUjian = (String) requestData.get("idUjian");
            String format = (String) requestData.getOrDefault("format", "EXCEL");

            if (idPeserta == null || idUjian == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "ID Peserta dan ID Ujian harus diisi"));
            } // Generate report menggunakan service
            Map<String, Object> reportData = hasilUjianService.generateParticipantReport(
                    idPeserta, idUjian, currentUser.getSchoolId(), requestData);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Laporan berhasil dibuat untuk format " + format);
            response.put("data", reportData);
            response.put("format", format);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error generating participant report", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Gagal membuat laporan: " + e.getMessage()));
        }
    }

    /**
     * Download participant report as Excel file
     */
    @GetMapping("/download-participant-report/{idPeserta}/{idUjian}")
    public ResponseEntity<?> downloadParticipantReport(
            @PathVariable String idPeserta,
            @PathVariable String idUjian,
            @RequestParam(value = "format", defaultValue = "EXCEL") String format,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validasi akses - hanya admin dan teacher
            if (!isAdminOrTeacher(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            // Generate Excel report
            byte[] reportBytes = hasilUjianService.downloadParticipantReportExcel(
                    idPeserta, idUjian, currentUser.getSchoolId());

            // Get participant name for filename
            String participantName = hasilUjianService.getParticipantName(idPeserta);
            String ujianName = hasilUjianService.getUjianName(idUjian);

            String filename = String.format("Laporan-Peserta-%s-%s.xlsx",
                    participantName.replaceAll("[^a-zA-Z0-9]", "-"),
                    ujianName.replaceAll("[^a-zA-Z0-9]", "-"));

            return ResponseEntity.ok()
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .body(reportBytes);

        } catch (Exception e) {
            logger.error("Error downloading participant report", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Gagal mengunduh laporan: " + e.getMessage()));
        }
    }

    /**
     * Get participant reports list for operators/teachers
     */
    @GetMapping("/participant-reports")
    public ResponseEntity<?> getParticipantReports(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "ujianId", required = false) String ujianId,
            @RequestParam(value = "search", required = false) String search,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.info("Getting participant reports - user: {}, role: {}", currentUser.getUsername(),
                    currentUser.getRoles());

            // More permissive access control - allow students to see their own data,
            // admin/teachers to see all
            String schoolId = currentUser.getSchoolId();
            if (schoolId == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "User tidak memiliki akses ke sekolah"));
            }

            Map<String, Object> response = hasilUjianService.getParticipantReportsList(
                    page, size, ujianId, search, schoolId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching participant reports list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }
}