package com.doyatama.university.controller;

import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.UjianAutoSubmitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller untuk monitoring dan management auto-submit ujian
 * Admin/Teacher only endpoints
 */
@RestController
@RequestMapping("/api/ujian-auto-submit")
public class UjianAutoSubmitController {

    @Autowired
    private UjianAutoSubmitService ujianAutoSubmitService;

    private static final Logger logger = LoggerFactory.getLogger(UjianAutoSubmitController.class);

    /**
     * Get auto-submit statistics for monitoring dashboard
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getAutoSubmitStatistics(@CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // TODO: Add role validation for admin/teacher only

            Map<String, Object> statistics = ujianAutoSubmitService.getAutoSubmitStatistics();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Auto-submit statistics retrieved successfully");
            response.put("data", statistics);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting auto-submit statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Force auto-submit specific session (admin only)
     */
    @PostMapping("/force-submit/{sessionId}")
    public ResponseEntity<?> forceAutoSubmitSession(
            @PathVariable String sessionId,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            // TODO: Add admin role validation

            Map<String, Object> result = ujianAutoSubmitService.forceAutoSubmitSession(
                    sessionId, currentUser.getId());

            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error force auto-submitting session: {}", sessionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

    /**
     * Check current time and sessions at risk of timeout
     */
    @GetMapping("/timeout-risk")
    public ResponseEntity<?> getSessionsAtTimeoutRisk(@CurrentUser UserPrincipal currentUser) throws IOException {
        try {
            Map<String, Object> statistics = ujianAutoSubmitService.getAutoSubmitStatistics();

            Map<String, Object> riskData = new HashMap<>();
            riskData.put("sessionsAtRisk", statistics.get("sessionsAtRisk"));
            riskData.put("activeSessions", statistics.get("activeSessions"));
            riskData.put("lastCheckTime", statistics.get("lastCheckTime"));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Timeout risk data retrieved");
            response.put("data", riskData);

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error("Error getting timeout risk data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Terjadi kesalahan sistem: " + e.getMessage()));
        }
    }

}