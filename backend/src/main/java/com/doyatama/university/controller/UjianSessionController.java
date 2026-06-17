package com.doyatama.university.controller;

import com.doyatama.university.model.UjianSession;
import com.doyatama.university.model.HasilUjian;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.UjianSessionRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.UjianSessionService;
import com.doyatama.university.util.AppConstants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ujian-session")
public class UjianSessionController {

    // PERBAIKAN: Tambahkan @Autowired untuk dependency injection
    @Autowired
    private UjianSessionService ujianSessionService;

    private static final Logger logger = LoggerFactory.getLogger(UjianSessionController.class);

    // PERBAIKAN: Tambahkan helper method untuk format durasi
    private String formatDuration(Integer durationInSeconds) {
        if (durationInSeconds == null || durationInSeconds <= 0) {
            return "0 menit";
        }

        int hours = durationInSeconds / 3600;
        int minutes = (durationInSeconds % 3600) / 60;
        int seconds = durationInSeconds % 60;

        if (hours > 0) {
            return String.format("%d jam %d menit %d detik", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d menit %d detik", minutes, seconds);
        } else {
            return String.format("%d detik", seconds);
        }
    }

    // ==================== SESSION MANAGEMENT ====================

    /**
     * Resume or Start ujian session - Support untuk melanjutkan ujian yang terputus
     */
    @PostMapping("/resume-or-start")
    public ResponseEntity<?> resumeOrStartUjianSession(
            @Valid @RequestBody UjianSessionRequest.StartSessionRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug(
                    "Resume/Start ujian session for user: " + currentUser.getId() + " ujian: " + request.getIdUjian());

            // Set peserta ID from current user
            request.setIdPeserta(currentUser.getId());

            // First check if there's existing session
            UjianSession existingSession = ujianSessionService.getActiveSession(request.getIdUjian(),
                    currentUser.getId());

            UjianSession session;
            boolean isResumed = false;

            if (existingSession != null && !existingSession.getIsSubmitted()) {
                // Try to start (which will resume if valid)
                session = ujianSessionService.startSession(request, currentUser.getSchoolId());
                isResumed = true;
            } else {
                // Create new session
                session = ujianSessionService.startSession(request, currentUser.getSchoolId());
                isResumed = false;
            }

            if (session == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Gagal memulai session ujian");
                errorResponse.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getSessionId());
            responseData.put("idSession", session.getIdSession());
            responseData.put("timeRemaining", session.getTimeRemaining());
            responseData.put("currentSoalIndex", session.getCurrentSoalIndex());
            responseData.put("totalQuestions", session.getTotalQuestions());
            responseData.put("attemptNumber", session.getAttemptNumber());
            responseData.put("startTime", session.getStartTime());
            responseData.put("isResumed", isResumed);
            responseData.put("answers", session.getAnswers());
            responseData.put("answeredQuestions", session.getAnsweredQuestions());

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 200);
            response.put("message", isResumed ? "Ujian berhasil dilanjutkan dari session sebelumnya"
                    : "Session ujian baru berhasil dimulai");
            response.put("content", responseData);

            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error resume/start", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Start ujian session - Memulai session ujian untuk peserta
     */
    @PostMapping("/start")
    public ResponseEntity<?> startUjianSession(
            @Valid @RequestBody UjianSessionRequest.StartSessionRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            logger.debug("Starting ujian session for user: " + currentUser.getId() + " ujian: " + request.getIdUjian());

            // Set peserta ID from current user
            request.setIdPeserta(currentUser.getId());

            UjianSession session = ujianSessionService.startSession(request, currentUser.getSchoolId());

            if (session == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusCode", 400);
                errorResponse.put("message", "Tidak dapat memulai ujian. Periksa jadwal dan status ujian.");
                errorResponse.put("content", null);
                return ResponseEntity.badRequest()
                        .body(errorResponse);
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getSessionId());
            responseData.put("idSession", session.getIdSession());
            responseData.put("timeRemaining", session.getTimeRemaining());
            responseData.put("currentSoalIndex", session.getCurrentSoalIndex());
            responseData.put("totalQuestions", session.getTotalQuestions());
            responseData.put("attemptNumber", session.getAttemptNumber());
            responseData.put("startTime", session.getStartTime());

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 200);
            response.put("message", "Session ujian berhasil dimulai");
            response.put("content", responseData);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error start", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Save individual jawaban
     */
    @PostMapping("/save-jawaban")
    public ResponseEntity<?> saveJawaban(
            @Valid @RequestBody UjianSessionRequest.SaveJawabanRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Set peserta dari user login
            request.setIdPeserta(currentUser.getId());

            // Pastikan frontend mengirim idBankSoal, bukan nomorSoal
            // Jika frontend masih mengirim nomorSoal, mapping ke idBankSoal di sini

            UjianSession session = ujianSessionService.saveJawaban(request);

            if (session == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Session tidak ditemukan atau tidak aktif"));
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getSessionId());
            responseData.put("currentSoalIndex", session.getCurrentSoalIndex());
            responseData.put("answeredQuestions", session.getAnsweredQuestions());
            responseData.put("lastUpdated", session.getUpdatedAt());
            responseData.put("progressPercentage", session.getProgressPercentage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Jawaban berhasil disimpan");
            response.put("data", responseData);

            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error saving jawaban", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Auto save progress (bulk save)
     */
    @PostMapping("/auto-save")
    public ResponseEntity<?> autoSaveProgress(
            @Valid @RequestBody UjianSessionRequest.AutoSaveProgressRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            request.setIdPeserta(currentUser.getId());

            UjianSession session = ujianSessionService.autoSaveProgress(request);

            if (session == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Session tidak ditemukan atau tidak aktif"));
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getSessionId());
            responseData.put("lastAutoSave", session.getLastAutoSave());
            responseData.put("answeredQuestions", session.getAnsweredQuestions());
            responseData.put("status", "auto_saved");

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Progress berhasil disimpan otomatis");
            response.put("data", responseData);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error auto saving progress", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Submit ujian final
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitUjian(
            @Valid @RequestBody UjianSessionRequest.SubmitUjianRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            request.setIdPeserta(currentUser.getId());

            HasilUjian hasilUjian = ujianSessionService.submitUjian(request, currentUser.getSchoolId());

            if (hasilUjian == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Session tidak ditemukan atau sudah selesai"));
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("idHasilUjian", hasilUjian.getIdHasilUjian());
            responseData.put("totalSkor", hasilUjian.getTotalSkor());
            responseData.put("persentase", hasilUjian.getPersentase());
            responseData.put("nilaiHuruf", hasilUjian.getNilaiHuruf());
            responseData.put("lulus", hasilUjian.getLulus());
            responseData.put("jumlahBenar", hasilUjian.getJumlahBenar());
            responseData.put("jumlahSalah", hasilUjian.getJumlahSalah());
            responseData.put("jumlahKosong", hasilUjian.getJumlahKosong());

            // PERBAIKAN: Ganti getFormattedDuration() dengan helper method
            responseData.put("durasiPengerjaan", formatDuration(hasilUjian.getDurasiPengerjaan()));

            responseData.put("waktuSelesai", hasilUjian.getWaktuSelesai());
            responseData.put("isAutoSubmit", hasilUjian.getIsAutoSubmit());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ujian berhasil diselesaikan");
            response.put("data", responseData);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error submitting ujian", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== SESSION MONITORING ====================

    /**
     * Get active session for participant
     */
    @GetMapping("/active/{idUjian}/{idPeserta}")
    public ResponseEntity<?> getActiveSession(
            @PathVariable String idUjian,
            @PathVariable String idPeserta,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validate user can access this session
            if (!idPeserta.equals(currentUser.getId())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusCode", 403);
                errorResponse.put("message", "Akses ditolak");
                errorResponse.put("content", null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(errorResponse);
            }

            UjianSession session = ujianSessionService.getActiveSession(idUjian, idPeserta);

            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 200);
            response.put("message", session != null ? "Session aktif ditemukan" : "Tidak ada session aktif");

            if (session != null) {
                Map<String, Object> sessionData = new HashMap<>();
                sessionData.put("sessionId", session.getSessionId());
                sessionData.put("idSession", session.getIdSession());
                sessionData.put("status", session.getStatus());
                sessionData.put("currentSoalIndex", session.getCurrentSoalIndex());
                sessionData.put("timeRemaining", session.getTimeRemaining());
                sessionData.put("answeredQuestions", session.getAnsweredQuestions());
                sessionData.put("totalQuestions", session.getTotalQuestions());
                sessionData.put("answers", session.getAnswers());
                sessionData.put("startTime", session.getStartTime());
                sessionData.put("attemptNumber", session.getAttemptNumber());
                sessionData.put("progressPercentage", session.getProgressPercentage());

                response.put("content", sessionData);
            } else {
                response.put("content", null);
            }

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting active session", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", "Terjadi kesalahan sistem: " + e.getMessage());
            errorResponse.put("content", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * Get ujian progress for resume
     */
    @GetMapping("/progress/{idUjian}/{idPeserta}")
    public ResponseEntity<?> getUjianProgress(
            @PathVariable String idUjian,
            @PathVariable String idPeserta,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validate user access
            if (!idPeserta.equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            Map<String, Object> progress = ujianSessionService.getUjianProgress(idUjian, idPeserta);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Progress ujian berhasil diambil");
            response.put("data", progress);

            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            logger.error("Error getting ujian progress", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Keep session alive (ping)
     */
    @PostMapping("/keep-alive/{idUjian}/{idPeserta}")
    public ResponseEntity<?> keepSessionAlive(
            @PathVariable String idUjian,
            @PathVariable String idPeserta,
            @RequestBody(required = false) UjianSessionRequest.KeepAliveRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validate user access
            if (!idPeserta.equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            if (request == null) {
                request = new UjianSessionRequest.KeepAliveRequest();
                request.setIdUjian(idUjian);
                request.setIdPeserta(idPeserta);
            }

            Map<String, Object> keepAliveData = ujianSessionService.keepSessionAlive(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Session berhasil diperbarui");
            response.put("data", keepAliveData);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error keeping session alive", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get time remaining for active session
     */
    @GetMapping("/time-remaining/{idUjian}/{idPeserta}")
    public ResponseEntity<?> getTimeRemaining(
            @PathVariable String idUjian,
            @PathVariable String idPeserta,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validate user access
            if (!idPeserta.equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            Map<String, Object> timeData = ujianSessionService.getTimeRemaining(idUjian, idPeserta);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Waktu tersisa berhasil diambil");
            response.put("data", timeData);

            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            logger.error("Error getting time remaining", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== NAVIGATION MANAGEMENT ====================

    /**
     * Update current soal index (navigation tracking)
     */
    @PostMapping("/update-current-soal")
    public ResponseEntity<?> updateCurrentSoal(
            @Valid @RequestBody UjianSessionRequest.UpdateCurrentSoalRequest request,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validate user access
            request.setIdPeserta(currentUser.getId());

            UjianSession session = ujianSessionService.updateCurrentSoal(request);

            if (session == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Session tidak ditemukan atau tidak aktif"));
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getSessionId());
            responseData.put("currentSoalIndex", session.getCurrentSoalIndex());
            responseData.put("navigationAction", request.getNavigationAction());
            responseData.put("updatedAt", session.getUpdatedAt());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Current soal berhasil diperbarui");
            response.put("data", responseData);

            return ResponseEntity.ok()
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating current soal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== VALIDATION ====================

    /**
     * Validate if participant can start ujian
     */
    @GetMapping("/validate-start/{idUjian}/{idPeserta}")
    public ResponseEntity<?> validateCanStart(
            @PathVariable String idUjian,
            @PathVariable String idPeserta,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Validate user access
            if (!idPeserta.equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Akses ditolak"));
            }

            Map<String, Object> validationResult = ujianSessionService.validateCanStart(idUjian, idPeserta,
                    currentUser.getSchoolId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Validasi berhasil");
            response.put("data", validationResult);
            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            logger.error("Error validating can start", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== ADMIN MONITORING ====================

    /**
     * Get all active sessions (for admin monitoring)
     */
    @GetMapping("/active-sessions")
    public ResponseEntity<?> getActiveSessions(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            String schoolId = currentUser.getSchoolId();
            Map<String, Object> activeSessions = ujianSessionService.getActiveSessions(page, size, schoolId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Session aktif berhasil diambil");
            response.put("data", activeSessions);
            return ResponseEntity.ok()
                    .body(response);
        } catch (Exception e) {
            logger.error("Error getting active sessions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Force end session (admin only)
     */
    @PostMapping("/force-end/{sessionId}")
    public ResponseEntity<?> forceEndSession(
            @PathVariable String sessionId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Check if user has admin role (implement role check logic)
            UjianSession session = ujianSessionService.forceEndSession(sessionId, currentUser.getId());

            if (session == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Session tidak ditemukan"));
            }

            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Session berhasil diakhiri secara paksa"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error force ending session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== SESSION STATISTICS ====================

    /**
     * Get session statistics for ujian
     */
    @GetMapping("/statistics/{idUjian}")
    public ResponseEntity<?> getSessionStatistics(
            @PathVariable String idUjian,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            String schoolId = currentUser.getSchoolId();
            Map<String, Object> statistics = ujianSessionService.getSessionStatistics(idUjian, schoolId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistik session berhasil diambil");
            response.put("data", statistics);
            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            logger.error("Error getting session statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

}
