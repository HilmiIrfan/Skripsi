package com.doyatama.university.controller;

import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller untuk mengelola Report Nilai Siswa
 * Menangani report dan analytics untuk nilai siswa
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    /**
     * Get report nilai siswa dengan filter dan paginasi
     */
    @GetMapping("/nilai-siswa")
    public ResponseEntity<Map<String, Object>> getReportNilaiSiswa(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "ujianId", required = false) String ujianId,
            @RequestParam(value = "kelasId", required = false) String kelasId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "search", required = false) String search,
            @CurrentUser UserPrincipal currentUser) {

        try {
            logger.info("Getting report nilai siswa for user: {}", currentUser.getUsername());

            // Validate user role - check by role number
            String userRole = currentUser.getRoles().equalsIgnoreCase("1") ? "ROLE_ADMINISTRATOR"
                    : currentUser.getRoles().equalsIgnoreCase("2") ? "ROLE_OPERATOR"
                            : currentUser.getRoles().equalsIgnoreCase("3") ? "ROLE_TEACHER"
                                    : currentUser.getRoles().equalsIgnoreCase("4") ? "ROLE_DUDI" : "ROLE_STUDENT";
            
            if (!userRole.equals("ROLE_OPERATOR") && !userRole.equals("ROLE_ADMINISTRATOR")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Access denied. Only operators and administrators can view reports.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            Map<String, Object> reportData = reportService.getReportNilaiSiswa(
                    page, size, ujianId, kelasId, startDate, endDate, search, currentUser);

            return ResponseEntity.ok(reportData);

        } catch (Exception e) {
            logger.error("Error getting report nilai siswa", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get detail report untuk siswa tertentu
     */
    @GetMapping("/nilai-siswa/detail/{siswaId}")
    public ResponseEntity<Map<String, Object>> getDetailReportSiswa(
            @PathVariable String siswaId,
            @RequestParam(value = "ujianId", required = false) String ujianId,
            @CurrentUser UserPrincipal currentUser) {
        
        try {
            logger.info("Getting detail report for siswa: {} by user: {}", siswaId, currentUser.getUsername());
            
            // Validate user role - check by role number
            String userRole = currentUser.getRoles().equalsIgnoreCase("1") ? "ROLE_ADMINISTRATOR"
                    : currentUser.getRoles().equalsIgnoreCase("2") ? "ROLE_OPERATOR"
                            : currentUser.getRoles().equalsIgnoreCase("3") ? "ROLE_TEACHER"
                                    : currentUser.getRoles().equalsIgnoreCase("4") ? "ROLE_DUDI" : "ROLE_STUDENT";
            
            if (!userRole.equals("ROLE_OPERATOR") && !userRole.equals("ROLE_ADMINISTRATOR")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Access denied. Only operators and administrators can view reports.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            Map<String, Object> detailData = reportService.getDetailReportSiswa(siswaId, ujianId, currentUser);

            return ResponseEntity.ok(detailData);

        } catch (Exception e) {
            logger.error("Error getting detail report for siswa: {}", siswaId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving detail report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get statistics untuk report nilai siswa
     */
    @GetMapping("/nilai-siswa/statistics")
    public ResponseEntity<Map<String, Object>> getReportStatistics(
            @RequestParam(value = "ujianId", required = false) String ujianId,
            @RequestParam(value = "kelasId", required = false) String kelasId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @CurrentUser UserPrincipal currentUser) {
        
        try {
            logger.info("Getting report statistics for user: {}", currentUser.getUsername());
            
            // Validate user role - check by role number
            String userRole = currentUser.getRoles().equalsIgnoreCase("1") ? "ROLE_ADMINISTRATOR"
                    : currentUser.getRoles().equalsIgnoreCase("2") ? "ROLE_OPERATOR"
                            : currentUser.getRoles().equalsIgnoreCase("3") ? "ROLE_TEACHER"
                                    : currentUser.getRoles().equalsIgnoreCase("4") ? "ROLE_DUDI" : "ROLE_STUDENT";
            
            if (!userRole.equals("ROLE_OPERATOR") && !userRole.equals("ROLE_ADMINISTRATOR")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Access denied. Only operators and administrators can view statistics.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            Map<String, Object> statistics = reportService.getReportStatistics(
                ujianId, kelasId, startDate, endDate, currentUser
            );

            return ResponseEntity.ok(statistics);

        } catch (Exception e) {
            logger.error("Error getting report statistics", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Export report nilai siswa ke Excel
     */
    @PostMapping("/nilai-siswa/export")
    public ResponseEntity<Map<String, Object>> exportReportNilaiSiswa(
            @RequestParam(value = "ujianId", required = false) String ujianId,
            @RequestParam(value = "kelasId", required = false) String kelasId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "format", defaultValue = "EXCEL") String format,
            @CurrentUser UserPrincipal currentUser) {
        
        try {
            logger.info("Exporting report nilai siswa for user: {}", currentUser.getUsername());
            
            // Validate user role - check by role number
            String userRole = currentUser.getRoles().equalsIgnoreCase("1") ? "ROLE_ADMINISTRATOR"
                    : currentUser.getRoles().equalsIgnoreCase("2") ? "ROLE_OPERATOR"
                            : currentUser.getRoles().equalsIgnoreCase("3") ? "ROLE_TEACHER"
                                    : currentUser.getRoles().equalsIgnoreCase("4") ? "ROLE_DUDI" : "ROLE_STUDENT";
            
            if (!userRole.equals("ROLE_OPERATOR") && !userRole.equals("ROLE_ADMINISTRATOR")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Access denied. Only operators and administrators can export reports.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            Map<String, Object> exportResult = reportService.exportReportNilaiSiswa(
                ujianId, kelasId, startDate, endDate, format, currentUser
            );

            return ResponseEntity.ok(exportResult);

        } catch (Exception e) {
            logger.error("Error exporting report nilai siswa", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error exporting report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
