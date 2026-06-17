package com.doyatama.university.controller;

import com.doyatama.university.model.Ujian;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.payload.UjianRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.UjianService;
import com.doyatama.university.util.AppConstants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ujian")
public class UjianController {

    private UjianService ujianService = new UjianService();
    private static final Logger logger = LoggerFactory.getLogger(UjianController.class);

    /**
     * Get all ujian with pagination
     */
    @GetMapping
    public PagedResponse<Ujian> getAllUjian(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "userID", defaultValue = "*") String userID,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return ujianService.getAllUjian(page, size, userID, schoolID);
    }

    /**
     * Get ujian by status (DRAFT, AKTIF, SELESAI, DIBATALKAN)
     */
    @GetMapping("/status/{status}")
    public PagedResponse<Ujian> getUjianByStatus(
            @PathVariable String status,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return ujianService.getUjianByStatus(status, page, size, schoolID);
    }

    /**
     * Get active ujian (live exams)
     */
    @GetMapping("/aktif")
    public PagedResponse<Ujian> getUjianAktif(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return ujianService.getUjianAktif(page, size, schoolID);
    }

    /**
     * Create new ujian
     */
    @PostMapping
    public ResponseEntity<?> createUjian(
            @Valid @RequestBody UjianRequest ujianRequest,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {

            logger.debug("Received request to create ujian: " + ujianRequest);

            Ujian ujian = ujianService.createUjian(ujianRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{ujianId}")
                    .buildAndExpand(ujian.getIdUjian()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Soal Ujian Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error during ujian creation", e); // Log error details for easier debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get ujian by ID
     */
    @GetMapping("/{ujianId}")
    public DefaultResponse<Ujian> getUjianById(@PathVariable String ujianId) throws IOException {
        return ujianService.getUjianById(ujianId);
    }

    /**
     * Update ujian (only allowed for DRAFT status)
     */
    @PutMapping("/{ujianId}")
    public ResponseEntity<?> updateUjian(
            @PathVariable String ujianId,
            @Valid @RequestBody UjianRequest ujianRequest) throws IOException {
        try {
            Ujian ujian = ujianService.updateUjian(ujianId, ujianRequest);

            return ResponseEntity.ok()
                    .location(ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{ujianId}")
                            .buildAndExpand(ujian.getIdUjian()).toUri())
                    .body(new ApiResponse(true, "Ujian berhasil diperbarui"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Delete ujian (only allowed for non-active ujian)
     */
    @DeleteMapping("/{ujianId}")
    public ResponseEntity<?> deleteUjian(@PathVariable String ujianId) throws IOException {
        try {
            ujianService.deleteUjianById(ujianId);
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Ujian berhasil dihapus"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== UJIAN STATE MANAGEMENT ====================

    /**
     * Activate ujian (change status from DRAFT to AKTIF)
     * This makes the exam ready to start at scheduled time
     */
    @PostMapping("/{ujianId}/activate")
    public ResponseEntity<?> activateUjian(@PathVariable String ujianId) throws IOException {
        try {
            Ujian ujian = ujianService.activateUjian(ujianId);
            if (ujian == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Ujian tidak ditemukan atau sudah aktif"));
            }
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Ujian telah diaktifkan dan siap untuk dimulai"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Start ujian (make it live for participants)
     * Can be called manually or automatically at scheduled time
     */
    @PostMapping("/{ujianId}/start")
    public ResponseEntity<?> startUjian(@PathVariable String ujianId) throws IOException {
        try {
            Ujian ujian = ujianService.startUjian(ujianId);
            if (ujian == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Ujian tidak ditemukan atau sudah dimulai"));
            }
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Ujian telah dimulai"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * End ujian (finish the exam)
     * Can be called manually or automatically when time is up
     */
    @PostMapping("/{ujianId}/end")
    public ResponseEntity<?> endUjian(@PathVariable String ujianId) throws IOException {
        try {
            Ujian ujian = ujianService.endUjian(ujianId);
            if (ujian == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Ujian tidak ditemukan atau sudah berakhir"));
            }
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Ujian telah berakhir"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Cancel ujian
     */
    @PostMapping("/{ujianId}/cancel")
    public ResponseEntity<?> cancelUjian(@PathVariable String ujianId) throws IOException {
        try {
            Ujian ujian = ujianService.cancelUjian(ujianId);
            if (ujian == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Ujian tidak ditemukan atau sudah dibatalkan"));
            }
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Ujian telah dibatalkan"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== BANK SOAL MANAGEMENT ====================

    /**
     * Add bank soal to ujian
     */
    @PostMapping("/{ujianId}/bankSoal/{bankSoalId}")
    public ResponseEntity<?> addBankSoalToUjian(
            @PathVariable String ujianId,
            @PathVariable String bankSoalId) throws IOException {
        try {
            Ujian ujian = ujianService.addBankSoalToUjian(ujianId, bankSoalId);
            if (ujian == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Ujian tidak ditemukan atau bank soal sudah ditambahkan"));
            }
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Bank soal berhasil ditambahkan ke ujian"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Remove bank soal from ujian
     */
    @DeleteMapping("/{ujianId}/bankSoal/{bankSoalId}")
    public ResponseEntity<?> removeBankSoalFromUjian(
            @PathVariable String ujianId,
            @PathVariable String bankSoalId) throws IOException {
        try {
            Ujian ujian = ujianService.removeBankSoalFromUjian(ujianId, bankSoalId);
            if (ujian == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Ujian tidak ditemukan atau bank soal tidak ada di ujian ini"));
            }
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Bank soal berhasil dihapus dari ujian"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== PARTICIPANT ACCESS CONTROL ====================

    /**
     * Check if participant can start ujian (based on time and settings)
     * This endpoint will be called by participants to check if start button should
     * be shown
     */
    @GetMapping("/{ujianId}/canStart")
    public ResponseEntity<?> canParticipantStartUjian(
            @PathVariable String ujianId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            DefaultResponse<Ujian> ujianResponse = ujianService.getUjianById(ujianId);
            Ujian ujian = ujianResponse.getContent();

            boolean canStart = false;
            String message = "";
            Instant currentTime = Instant.now();

            if (!ujian.isAktif()) {
                message = "Ujian belum diaktifkan";
            } else if (!ujian.getIsLive()) {
                // Check if current time is within allowed start time
                Instant scheduledStart = ujian.getWaktuMulaiDijadwalkan();
                Instant allowedEnd = ujian.getBatasAkhirMulai();

                if (currentTime.isBefore(scheduledStart)) {
                    message = "Ujian belum dimulai. Waktu mulai: " + scheduledStart.toString();
                } else if (allowedEnd != null && currentTime.isAfter(allowedEnd)) {
                    message = "Waktu untuk memulai ujian telah berakhir";
                } else {
                    // Check if automatic end time has passed
                    if (ujian.getWaktuSelesaiOtomatis() != null &&
                            currentTime.isAfter(ujian.getWaktuSelesaiOtomatis())) {
                        message = "Ujian telah berakhir";
                    } else {
                        canStart = true;
                        message = "Ujian dapat dimulai";
                    }
                }
            } else {
                // Ujian is already live
                if (ujian.getWaktuSelesaiOtomatis() != null &&
                        currentTime.isAfter(ujian.getWaktuSelesaiOtomatis())) {
                    message = "Ujian telah berakhir";
                } else {
                    canStart = true;
                    message = "Ujian sedang berlangsung";
                }
            }

            java.util.Map<String, Object> responseMap = new java.util.HashMap<>();
            responseMap.put("canStart", canStart);
            responseMap.put("message", message);
            responseMap.put("currentTime", currentTime);
            responseMap.put("scheduledStart", ujian.getWaktuMulaiDijadwalkan());
            responseMap.put("scheduledEnd", ujian.getWaktuSelesaiOtomatis());
            responseMap.put("isLive", ujian.getIsLive());
            responseMap.put("status", ujian.getStatusUjian());
            return ResponseEntity.ok()
                    .body(responseMap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Get ujian time information for participants
     */
    @GetMapping("/{ujianId}/timeInfo")
    public ResponseEntity<?> getUjianTimeInfo(@PathVariable String ujianId) throws IOException {
        try {
            DefaultResponse<Ujian> ujianResponse = ujianService.getUjianById(ujianId);
            Ujian ujian = ujianResponse.getContent();

            Map<String, Object> timeInfo = new java.util.HashMap<>();
            timeInfo.put("waktuMulaiDijadwalkan", ujian.getWaktuMulaiDijadwalkan());
            timeInfo.put("waktuSelesaiOtomatis", ujian.getWaktuSelesaiOtomatis());
            timeInfo.put("batasAkhirMulai", ujian.getBatasAkhirMulai());
            timeInfo.put("durasiMenit", ujian.getDurasiMenit());
            timeInfo.put("isFlexibleTiming", ujian.getIsFlexibleTiming());
            timeInfo.put("allowLateStart", ujian.getAllowLateStart());
            timeInfo.put("maxLateStartMinutes", ujian.getMaxLateStartMinutes());
            timeInfo.put("toleransiKeterlambatanMenit", ujian.getToleransiKeterlambatanMenit());
            timeInfo.put("currentTime", Instant.now());
            timeInfo.put("status", ujian.getStatusUjian());
            timeInfo.put("isLive", ujian.getIsLive());
            return ResponseEntity.ok().body(timeInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    // ==================== STATISTICS AND MONITORING ====================

    /**
     * Get ujian statistics for admin dashboard
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getUjianStatistics(@CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            String schoolId = currentUser.getSchoolId();
            Map<String, Object> statistics = ujianService.getUjianStatistics(schoolId);

            java.util.Map<String, Object> responseMap = new java.util.HashMap<>();
            responseMap.put("success", true);
            responseMap.put("data", statistics);
            responseMap.put("message", "Statistik ujian berhasil diambil");
            return ResponseEntity.ok()
                    .body(responseMap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Create and immediately activate ujian (for quick start)
     */
    @PostMapping("/createAndActivate")
    public ResponseEntity<?> createAndActivateUjian(
            @Valid @RequestBody UjianRequest ujianRequest,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // Set creator ID from current user
            ujianRequest.setIdCreatedBy(currentUser.getId());
            ujianRequest.setIdSchool(currentUser.getSchoolId());

            // Create ujian
            Ujian ujian = ujianService.createUjian(ujianRequest);

            // Immediately activate it
            ujian = ujianService.activateUjian(ujian.getIdUjian());

            // If auto start is enabled and scheduled time is now or past, start it
            if (ujian.getIsAutoStart() != null && ujian.getIsAutoStart() &&
                    ujian.getWaktuMulaiDijadwalkan() != null &&
                    !Instant.now().isBefore(ujian.getWaktuMulaiDijadwalkan())) {
                ujian = ujianService.startUjian(ujian.getIdUjian());
            }

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{ujianId}")
                    .buildAndExpand(ujian.getIdUjian()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Ujian berhasil dibuat dan diaktifkan"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }
}