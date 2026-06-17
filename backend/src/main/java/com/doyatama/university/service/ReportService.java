package com.doyatama.university.service;

import com.doyatama.university.model.*;
import com.doyatama.university.repository.*;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service untuk mengelola Report Nilai Siswa
 * Menangani business logic untuk report dan analytics nilai siswa
 */
@Service
public class ReportService {
    @Autowired
    private HasilUjianRepository hasilUjianRepository;

    @Autowired
    private UjianRepository ujianRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheatDetectionRepository cheatDetectionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    /**
     * Get report nilai siswa dengan filter dan paginasi
     */
    public Map<String, Object> getReportNilaiSiswa(
            int page, int size, String ujianId, String kelasId,
            String startDate, String endDate, String search, UserPrincipal currentUser) {
        logger.info("Getting report nilai siswa with filters - ujianId: {}, kelasId: {}, search: {}",
                ujianId, kelasId, search);

        try {
            // Get all hasil ujian based on filters
            List<HasilUjian> allHasilUjian = hasilUjianRepository.findAll(50); // Limit to avoid memory issues

            // Apply filters
            List<HasilUjian> filteredHasil = allHasilUjian.stream()
                    .filter(hasil -> {
                        // Filter by ujian
                        if (ujianId != null && !ujianId.isEmpty()) {
                            if (!ujianId.equals(hasil.getIdUjian()))
                                return false;
                        }

                        // Filter by date range
                        if (startDate != null && !startDate.isEmpty() && hasil.getWaktuMulai() != null) {
                            try {
                                LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
                                if (hasil.getWaktuMulai().isBefore(start.atStartOfDay().toInstant(ZoneOffset.UTC))) {
                                    return false;
                                }
                            } catch (Exception e) {
                                logger.warn("Invalid start date format: {}", startDate);
                            }
                        }

                        if (endDate != null && !endDate.isEmpty() && hasil.getWaktuMulai() != null) {
                            try {
                                LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
                                if (hasil.getWaktuMulai()
                                        .isAfter(end.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))) {
                                    return false;
                                }
                            } catch (Exception e) {
                                logger.warn("Invalid end date format: {}", endDate);
                            }
                        }

                        return true;
                    })
                    .collect(Collectors.toList());

            // Get report data for each hasil ujian
            List<Map<String, Object>> reportData = new ArrayList<>();

            for (HasilUjian hasil : filteredHasil) {
                try {
                    Map<String, Object> reportItem = createReportItem(hasil, kelasId, search);
                    if (reportItem != null) {
                        reportData.add(reportItem);
                    }
                } catch (Exception e) {
                    logger.warn("Error creating report item for hasil ujian: {}", hasil.getIdHasilUjian(), e);
                }
            }

            // Apply search filter
            if (search != null && !search.isEmpty()) {
                String searchLower = search.toLowerCase();
                reportData = reportData.stream()
                        .filter(item -> {
                            String namaSiswa = (String) item.get("namaSiswa");
                            String nimSiswa = (String) item.get("nimSiswa");
                            String namaUjian = (String) item.get("namaUjian");
                            String namaKelas = (String) item.get("namaKelas");

                            return (namaSiswa != null && namaSiswa.toLowerCase().contains(searchLower)) ||
                                    (nimSiswa != null && nimSiswa.toLowerCase().contains(searchLower)) ||
                                    (namaUjian != null && namaUjian.toLowerCase().contains(searchLower)) ||
                                    (namaKelas != null && namaKelas.toLowerCase().contains(searchLower));
                        })
                        .collect(Collectors.toList());
            }

            // Sort by nilai desc
            reportData.sort((a, b) -> {
                Double nilaiA = (Double) a.get("nilai");
                Double nilaiB = (Double) b.get("nilai");
                if (nilaiA == null)
                    nilaiA = 0.0;
                if (nilaiB == null)
                    nilaiB = 0.0;
                return Double.compare(nilaiB, nilaiA);
            });

            // Apply pagination
            int start = page * size;
            int end = Math.min(start + size, reportData.size());
            List<Map<String, Object>> paginatedData = reportData.subList(start, end);

            // Prepare result
            Map<String, Object> result = new HashMap<>();
            result.put("data", paginatedData);
            result.put("totalElements", reportData.size());
            result.put("totalPages", (int) Math.ceil((double) reportData.size() / size));
            result.put("currentPage", page);
            result.put("size", size);
            result.put("hasNext", end < reportData.size());
            result.put("hasPrev", start > 0);

            logger.info("Report nilai siswa retrieved successfully. Total: {}", reportData.size());
            return result;

        } catch (Exception e) {
            logger.error("Error getting report nilai siswa", e);
            throw new RuntimeException("Error retrieving report data: " + e.getMessage());
        }
    }

    /**
     * Create report item untuk satu hasil ujian
     */
    private Map<String, Object> createReportItem(HasilUjian hasil, String kelasId, String search) {
        try {
            // Get student info
            User siswa = null;
            try {
                siswa = userRepository.findById(hasil.getIdPeserta());
            } catch (Exception e) {
                logger.warn("Student not found: {}", hasil.getIdPeserta());
                return null;
            }
            if (siswa == null)
                return null;

            // Get ujian info
            Ujian ujian = null;
            try {
                ujian = ujianRepository.findById(hasil.getIdUjian());
            } catch (Exception e) {
                logger.warn("Ujian not found: {}", hasil.getIdUjian());
                return null;
            }
            if (ujian == null)
                return null;

            // Get kelas info - Note: User model doesn't have idKelas field
            // We'll use "-" for now or get from school data
            String namaKelas = "-"; // Get cheat detection count
            List<CheatDetection> violations = new ArrayList<>();
            try {
                violations = cheatDetectionRepository.findBySessionId(hasil.getSessionId());
            } catch (Exception e) {
                logger.warn("Error getting violations for session: {}", hasil.getSessionId());
            }
            int jumlahPelanggaran = violations != null ? violations.size() : 0;

            // Calculate duration in minutes
            Integer durasi = null;
            if (hasil.getDurasiPengerjaan() != null) {
                durasi = hasil.getDurasiPengerjaan() / 60; // convert seconds to minutes
            }

            // Create report item
            Map<String, Object> reportItem = new HashMap<>();
            reportItem.put("siswaId", siswa.getId());
            reportItem.put("nimSiswa", siswa.getUsername());
            reportItem.put("namaSiswa", siswa.getName());
            reportItem.put("namaKelas", namaKelas);
            reportItem.put("ujianId", ujian.getIdUjian());
            reportItem.put("namaUjian", ujian.getNamaUjian());
            reportItem.put("nilai", hasil.getTotalSkor() != null ? hasil.getTotalSkor() : 0.0);
            reportItem.put("nilaiHuruf", hasil.getNilaiHuruf());
            reportItem.put("lulus", hasil.getLulus() != null ? hasil.getLulus() : false);
            reportItem.put("waktuMulai", hasil.getWaktuMulai());
            reportItem.put("waktuSelesai", hasil.getWaktuSelesai());
            reportItem.put("durasi", durasi);
            reportItem.put("jumlahSoal", hasil.getTotalSoal());
            reportItem.put("soalTerjawab",
                    hasil.getTotalSoal() != null && hasil.getJumlahKosong() != null
                            ? hasil.getTotalSoal() - hasil.getJumlahKosong()
                            : 0);
            reportItem.put("soalBenar", hasil.getJumlahBenar());
            reportItem.put("soalSalah", hasil.getJumlahSalah());
            reportItem.put("soalKosong", hasil.getJumlahKosong());
            reportItem.put("persentase", hasil.getPersentase());
            reportItem.put("jumlahPelanggaran", jumlahPelanggaran);
            reportItem.put("statusPengerjaan", hasil.getStatusPengerjaan());
            reportItem.put("isAutoSubmit", hasil.getIsAutoSubmit());

            return reportItem;

        } catch (Exception e) {
            logger.error("Error creating report item for hasil ujian: {}", hasil.getIdHasilUjian(), e);
            return null;
        }
    }

    /**
     * Get detail report untuk siswa tertentu
     */
    public Map<String, Object> getDetailReportSiswa(String siswaId, String ujianId, UserPrincipal currentUser) {
        logger.info("Getting detail report for siswa: {}, ujian: {}", siswaId, ujianId);

        try {
            // Get student info
            User siswa = null;
            try {
                siswa = userRepository.findById(siswaId);
            } catch (Exception e) {
                throw new ResourceNotFoundException("Student", "id", siswaId);
            }
            if (siswa == null) {
                throw new ResourceNotFoundException("Student", "id", siswaId);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("siswa", createSiswaInfo(siswa));

            // Get hasil ujian for this student
            List<HasilUjian> hasilUjianList = hasilUjianRepository.findAll(100).stream()
                    .filter(hasil -> siswaId.equals(hasil.getIdPeserta()))
                    .filter(hasil -> ujianId == null || ujianId.equals(hasil.getIdUjian()))
                    .collect(Collectors.toList());

            List<Map<String, Object>> detailList = new ArrayList<>();
            for (HasilUjian hasil : hasilUjianList) {
                Map<String, Object> detail = createDetailReportItem(hasil);
                if (detail != null) {
                    detailList.add(detail);
                }
            }

            result.put("hasilUjian", detailList);
            result.put("totalUjian", detailList.size());

            // Calculate statistics
            if (!detailList.isEmpty()) {
                double totalNilai = detailList.stream()
                        .mapToDouble(item -> (Double) item.get("nilai"))
                        .sum();
                double rataRata = totalNilai / detailList.size();

                long ujianLulus = detailList.stream()
                        .mapToLong(item -> (Boolean) item.get("lulus") ? 1 : 0)
                        .sum();

                Map<String, Object> statistics = new HashMap<>();
                statistics.put("rataRataNilai", Math.round(rataRata * 100.0) / 100.0);
                statistics.put("ujianLulus", ujianLulus);
                statistics.put("ujianTidakLulus", detailList.size() - ujianLulus);
                statistics.put("persentaseLulus", Math.round((double) ujianLulus / detailList.size() * 100.0));

                result.put("statistics", statistics);
            }

            logger.info("Detail report for siswa {} retrieved successfully", siswaId);
            return result;

        } catch (Exception e) {
            logger.error("Error getting detail report for siswa: {}", siswaId, e);
            throw new RuntimeException("Error retrieving detail report: " + e.getMessage());
        }
    }

    /**
     * Create siswa info map
     */
    private Map<String, Object> createSiswaInfo(User siswa) {
        Map<String, Object> siswaInfo = new HashMap<>();
        siswaInfo.put("id", siswa.getId());
        siswaInfo.put("name", siswa.getName());
        siswaInfo.put("username", siswa.getUsername());
        siswaInfo.put("email", siswa.getEmail());

        // Since User model doesn't have idKelas field, we'll use "-"
        siswaInfo.put("kelas", "-");

        return siswaInfo;
    }

    /**
     * Create detail report item
     */
    private Map<String, Object> createDetailReportItem(HasilUjian hasil) {
        try {
            // Get ujian info
            Ujian ujian = null;
            try {
                ujian = ujianRepository.findById(hasil.getIdUjian());
            } catch (Exception e) {
                logger.warn("Ujian not found: {}", hasil.getIdUjian());
                return null;
            }
            if (ujian == null)
                return null; // Get violations
            List<CheatDetection> violations = new ArrayList<>();
            try {
                violations = cheatDetectionRepository.findBySessionId(hasil.getSessionId());
            } catch (Exception e) {
                logger.warn("Error getting violations for session: {}", hasil.getSessionId());
            }

            Map<String, Object> detail = new HashMap<>();
            detail.put("idHasilUjian", hasil.getIdHasilUjian());
            detail.put("ujianId", ujian.getIdUjian());
            detail.put("namaUjian", ujian.getNamaUjian());
            detail.put("nilai", hasil.getTotalSkor() != null ? hasil.getTotalSkor() : 0.0);
            detail.put("nilaiHuruf", hasil.getNilaiHuruf());
            detail.put("lulus", hasil.getLulus() != null ? hasil.getLulus() : false);
            detail.put("waktuMulai", hasil.getWaktuMulai());
            detail.put("waktuSelesai", hasil.getWaktuSelesai());
            detail.put("durasiPengerjaan", hasil.getDurasiPengerjaan());
            detail.put("statusPengerjaan", hasil.getStatusPengerjaan());
            detail.put("isAutoSubmit", hasil.getIsAutoSubmit());
            detail.put("jumlahBenar", hasil.getJumlahBenar());
            detail.put("jumlahSalah", hasil.getJumlahSalah());
            detail.put("jumlahKosong", hasil.getJumlahKosong());
            detail.put("totalSoal", hasil.getTotalSoal());
            detail.put("persentase", hasil.getPersentase());
            detail.put("jumlahPelanggaran", violations != null ? violations.size() : 0);

            return detail;

        } catch (Exception e) {
            logger.error("Error creating detail report item", e);
            return null;
        }
    }

    /**
     * Get statistics untuk report
     */
    public Map<String, Object> getReportStatistics(
            String ujianId, String kelasId, String startDate, String endDate, UserPrincipal currentUser) {

        logger.info("Getting report statistics");

        try {
            // Get filtered report data (reuse existing method)
            Map<String, Object> reportResult = getReportNilaiSiswa(0, Integer.MAX_VALUE, ujianId, kelasId, startDate,
                    endDate, null, currentUser);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> reportData = (List<Map<String, Object>>) reportResult.get("data");

            Map<String, Object> statistics = new HashMap<>();

            if (reportData.isEmpty()) {
                statistics.put("totalSiswa", 0);
                statistics.put("rataRataNilai", 0.0);
                statistics.put("siswaLulus", 0);
                statistics.put("siswaTidakLulus", 0);
                statistics.put("persentaseLulus", 0.0);
                statistics.put("nilaiTertinggi", 0.0);
                statistics.put("nilaiTerendah", 0.0);
                return statistics;
            }

            // Calculate statistics
            int totalSiswa = reportData.size();
            double totalNilai = reportData.stream()
                    .mapToDouble(item -> (Double) item.get("nilai"))
                    .sum();
            double rataRataNilai = totalNilai / totalSiswa;

            long siswaLulus = reportData.stream()
                    .mapToLong(item -> (Boolean) item.get("lulus") ? 1 : 0)
                    .sum();

            double nilaiTertinggi = reportData.stream()
                    .mapToDouble(item -> (Double) item.get("nilai"))
                    .max().orElse(0.0);

            double nilaiTerendah = reportData.stream()
                    .mapToDouble(item -> (Double) item.get("nilai"))
                    .min().orElse(0.0);

            statistics.put("totalSiswa", totalSiswa);
            statistics.put("rataRataNilai", Math.round(rataRataNilai * 100.0) / 100.0);
            statistics.put("siswaLulus", siswaLulus);
            statistics.put("siswaTidakLulus", totalSiswa - siswaLulus);
            statistics.put("persentaseLulus", Math.round((double) siswaLulus / totalSiswa * 100.0));
            statistics.put("nilaiTertinggi", nilaiTertinggi);
            statistics.put("nilaiTerendah", nilaiTerendah);

            logger.info("Report statistics calculated successfully");
            return statistics;

        } catch (Exception e) {
            logger.error("Error getting report statistics", e);
            throw new RuntimeException("Error retrieving statistics: " + e.getMessage());
        }
    }

    /**
     * Export report nilai siswa
     */
    public Map<String, Object> exportReportNilaiSiswa(
            String ujianId, String kelasId, String startDate, String endDate,
            String format, UserPrincipal currentUser) {

        logger.info("Exporting report nilai siswa in format: {}", format);

        try {
            // Get all report data
            Map<String, Object> reportResult = getReportNilaiSiswa(0, Integer.MAX_VALUE, ujianId, kelasId, startDate,
                    endDate, null, currentUser);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> reportData = (List<Map<String, Object>>) reportResult.get("data");

            Map<String, Object> exportResult = new HashMap<>();
            exportResult.put("totalRecords", reportData.size());
            exportResult.put("exportedAt", Instant.now());
            exportResult.put("format", format);

            // For now, return the data directly
            // In a real implementation, you would generate Excel/PDF files
            exportResult.put("data", reportData);
            exportResult.put("downloadUrl", "/api/report/download/" + System.currentTimeMillis());

            logger.info("Report exported successfully. Total records: {}", reportData.size());
            return exportResult;

        } catch (Exception e) {
            logger.error("Error exporting report", e);
            throw new RuntimeException("Error exporting report: " + e.getMessage());
        }
    }
}
