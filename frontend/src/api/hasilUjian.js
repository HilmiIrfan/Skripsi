import request from "@/utils/request";

// ==================== BASIC CRUD OPERATIONS ====================

/**
 * Get all hasil ujian
 * Menambahkan parameter options untuk includeAnalytics dan includeSecurityData
 */
export function getHasilUjian(size = 50, options = {}) {
  return request({
    url: "/hasil-ujian",
    method: "get",
    params: {
      size,
      includeAnalytics: options.includeAnalytics || false,
      includeSecurityData: options.includeSecurityData || false,
    },
  });
}

/**
 * Get hasil ujian by ID
 */
export function getHasilUjianById(hasilUjianId) {
  return request({
    url: `/hasil-ujian/${hasilUjianId}`,
    method: "get",
  });
}

/**
 * Get hasil ujian berdasarkan peserta dan ujian
 */
export function getHasilByPesertaAndUjian(idPeserta, idUjian, options = {}) {
  return request({
    url: `/hasil-ujian/peserta/${idPeserta}/ujian/${idUjian}`,
    method: "get",
    params: {
      attemptNumber: options.attemptNumber,
      includeAnswers: options.includeAnswers || false,
      includeAnalysis: options.includeAnalysis || true,
      includeTimeAnalytics: options.includeTimeAnalytics || false,
      includeBehavioralAnalysis: options.includeBehavioralAnalysis || false,
      includeSecurityData: options.includeSecurityData || false,
    },
  });
}

/**
 * Get hasil ujian berdasarkan ujian (untuk analisis kelas)
 * Mengubah parameter includeAnalytics menjadi objek options
 */
export function getHasilByUjian(idUjian, options = {}) {
  return request({
    url: `/hasil-ujian/ujian/${idUjian}`,
    method: "get",
    params: {
      includeAnalytics: options.includeAnalytics || false,
      includeSecurityData: options.includeSecurityData || false, // Tambahkan ini
    },
  });
}

/**
 * Get hasil ujian berdasarkan peserta (riwayat)
 */
export function getHasilByPeserta(idPeserta, size = 50) {
  return request({
    url: `/hasil-ujian/peserta/${idPeserta}`,
    method: "get",
    params: { size },
  });
}

// ==================== ANALYTICS OPERATIONS ====================

/**
 * Update analytics data
 */
export function updateAnalytics(hasilUjianId, analyticsData) {
  return request({
    url: `/hasil-ujian/${hasilUjianId}/analytics`,
    method: "put",
    data: analyticsData,
  });
}

/**
 * Generate advanced analytics
 */
export function generateAdvancedAnalytics(data) {
  return request({
    url: "/hasil-ujian/analytics/generate",
    method: "post",
    data,
  });
}

/**
 * Generate individual analytics
 */
export function generateIndividualAnalytics(idPeserta, idUjian, options = {}) {
  return request({
    url: "/hasil-ujian/analytics/generate",
    method: "post",
    data: {
      idPeserta,
      idUjian,
      analysisScope: "INDIVIDUAL",
      includeComparative: options.includeComparative || false,
      includePredictive: options.includePredictive || false,
      includeRecommendations: options.includeRecommendations || true,
      analyticsLevel: options.analyticsLevel || "DETAILED",
    },
  });
}

/**
 * Generate class analytics
 */
export function generateClassAnalytics(idUjian, options = {}) {
  return request({
    url: "/hasil-ujian/analytics/generate",
    method: "post",
    data: {
      idUjian,
      analysisScope: "CLASS",
      includeComparative: options.includeComparative || true,
      includePredictive: options.includePredictive || false,
      includeRecommendations: options.includeRecommendations || false,
      analyticsLevel: options.analyticsLevel || "COMPREHENSIVE",
    },
  });
}

// ==================== SECURITY OPERATIONS ====================

/**
 * Update security status
 */
export function updateSecurityStatus(hasilUjianId, data) {
  return request({
    url: `/hasil-ujian/${hasilUjianId}/security`,
    method: "put",
    data,
  });
}

/**
 * Submit appeal
 */
export function submitAppeal(hasilUjianId, appealData) {
  return request({
    url: `/hasil-ujian/${hasilUjianId}/appeal`,
    method: "post",
    data: appealData,
  });
}

/**
 * Review appeal (admin/teacher)
 */
export function reviewAppeal(hasilUjianId, reviewData) {
  return request({
    url: `/hasil-ujian/${hasilUjianId}/appeal/review`,
    method: "put",
    data: reviewData,
  });
}

/**
 * Verify hasil ujian
 */
export function verifyResult(hasilUjianId, verificationData) {
  return request({
    url: `/hasil-ujian/${hasilUjianId}/verify`,
    method: "put",
    data: verificationData,
  });
}

// ==================== STATISTICS OPERATIONS ====================

/**
 * Get statistics untuk ujian
 */
export function getUjianStatistics(idUjian) {
  return request({
    url: `/hasil-ujian/statistics/ujian/${idUjian}`,
    method: "get",
  });
}

/**
 * Get overview statistics untuk dashboard
 */
export function getOverviewStatistics(timeRange = "ALL") {
  return request({
    url: "/hasil-ujian/statistics/overview",
    method: "get",
    params: { timeRange },
  });
}

/**
 * Get statistics dengan filter
 */
export function getFilteredStatistics(filters = {}) {
  return request({
    url: "/hasil-ujian/statistics/filtered",
    method: "get",
    params: filters,
  });
}

// ==================== INTEGRATION OPERATIONS ====================

/**
 * Create hasil ujian from session (internal use)
 */
export function createFromSession(sessionData) {
  return request({
    url: "/hasil-ujian/create-from-session",
    method: "post",
    params: sessionData,
  });
}

// ==================== EXPORT AND UTILITY OPERATIONS ====================

/**
 * Export hasil ujian
 */
export function exportHasilUjian(exportData) {
  return request({
    url: "/hasil-ujian/export",
    method: "post",
    data: exportData,
    responseType: "blob", // For file download
  });
}

/**
 * Export hasil ujian to Excel
 */
export function exportToExcel(idUjian, options = {}) {
  return request({
    url: "/hasil-ujian/export",
    method: "post",
    data: {
      idUjian,
      format: "EXCEL",
      includeAnswers: options.includeAnswers || false,
      includeAnalysis: options.includeAnalysis || true,
      includeStatistics: options.includeStatistics || true,
      sortBy: options.sortBy || "SCORE_DESC",
      filterStatus: options.filterStatus || "ALL",
    },
    responseType: "blob",
  });
}

/**
 * Export hasil ujian to PDF
 */
export function exportToPDF(idUjian, options = {}) {
  return request({
    url: "/hasil-ujian/export",
    method: "post",
    data: {
      idUjian,
      format: "PDF",
      includeAnalysis: options.includeAnalysis || true,
      includeStatistics: options.includeStatistics || true,
      sortBy: options.sortBy || "SCORE_DESC",
    },
    responseType: "blob",
  });
}

/**
 * Delete hasil ujian (admin only)
 */

export function deleteHasilUjian(data) {
  // Jika data adalah string (id saja), konversi ke objek
  const id =
    typeof data === "string"
      ? data
      : data.idHasilUjian || data.key || data.id || "";

  return request({
    url: `/hasil-ujian/${id}`,
    method: "delete",
  });
}

// ==================== CONVENIENCE FUNCTIONS ====================

/**
 * Get hasil ujian with full analytics for student view
 */
export function getStudentResult(idPeserta, idUjian) {
  return getHasilByPesertaAndUjian(idPeserta, idUjian, {
    includeAnalysis: true,
    includeTimeAnalytics: true,
    includeBehavioralAnalysis: true,
    includeAnswers: false, // Security: don't show answers
  });
}

/**
 * Get hasil ujian with answers for teacher review
 */
export function getTeacherReview(idPeserta, idUjian) {
  return getHasilByPesertaAndUjian(idPeserta, idUjian, {
    includeAnalysis: true,
    includeTimeAnalytics: true,
    includeBehavioralAnalysis: true,
    includeAnswers: true,
    includeSecurityData: true,
  });
}

/**
 * Get class performance overview
 */
export function getClassPerformanceOverview(idUjian) {
  return Promise.all([
    getHasilByUjian(idUjian, {
      includeAnalytics: true,
      includeSecurityData: true,
    }), // Updated call
    getUjianStatistics(idUjian),
    generateClassAnalytics(idUjian),
  ]).then(([results, statistics, analytics]) => ({
    results: results.data,
    statistics: statistics.data,
    analytics: analytics.data,
  }));
}

/**
 * Get student performance with recommendations
 */
export function getStudentPerformanceWithRecommendations(idPeserta, idUjian) {
  return Promise.all([
    getStudentResult(idPeserta, idUjian),
    generateIndividualAnalytics(idPeserta, idUjian, {
      includeComparative: true,
      includePredictive: true,
      includeRecommendations: true,
    }),
  ]).then(([result, analytics]) => ({
    result: result.data,
    analytics: analytics.data,
  }));
}

/**
 * Generate participant report after exam completion
 * TODO: Backend endpoint needs to be implemented
 */
export function generateParticipantReport(idPeserta, idUjian, options = {}) {
  // TODO: Implement backend endpoint: POST /api/hasil-ujian/generate-participant-report
  return request({
    url: "/hasil-ujian/generate-participant-report",
    method: "post",
    data: {
      idPeserta,
      idUjian,
      format: options.format || "PDF",
      includeAnalysis: options.includeAnalysis !== false,
      includeViolations: options.includeViolations !== false,
      includeBehavioralAnalysis: options.includeBehavioralAnalysis !== false,
      includeRecommendations: options.includeRecommendations !== false,
      reportType: options.reportType || "PARTICIPANT_COMPREHENSIVE",
    },
    responseType: "blob",
  });
}

/**
 * Auto-generate and download participant report
 * TODO: Backend endpoint needs to be implemented
 */
export function autoGenerateAndDownloadReport(
  idPeserta,
  idUjian,
  sessionId,
  options = {}
) {
  // TODO: Implement backend endpoint: POST /api/hasil-ujian/auto-generate-report
  return request({
    url: "/hasil-ujian/auto-generate-report",
    method: "post",
    data: {
      idPeserta,
      idUjian,
      sessionId,
      format: options.format || "PDF",
      includeAnalysis: true,
      includeViolations: true,
      includeBehavioralAnalysis: true,
      includeRecommendations: true,
      reportType: "PARTICIPANT_COMPREHENSIVE",
      autoDownload: true,
    },
    responseType: "blob",
  });
}

// ==================== VALIDATION HELPERS ====================

/**
 * Validate hasil ujian data
 */
export function validateHasilUjianData(data) {
  const errors = [];

  if (!data.idUjian) errors.push("ID Ujian diperlukan");
  if (!data.idPeserta) errors.push("ID Peserta diperlukan");
  if (!data.sessionId) errors.push("Session ID diperlukan");

  return {
    isValid: errors.length === 0,
    errors,
  };
}

/**
 * Format hasil ujian for display
 */
export function formatHasilUjianForDisplay(hasilUjian) {
  return {
    ...hasilUjian,
    formattedScore: `${hasilUjian.totalSkor}/${hasilUjian.skorMaksimal}`,
    formattedPercentage: `${hasilUjian.persentase?.toFixed(1)}%`,
    formattedDuration: formatDuration(hasilUjian.durasiPengerjaan),
    statusBadge: getStatusBadge(hasilUjian.statusPengerjaan),
    gradeBadge: getGradeBadge(hasilUjian.nilaiHuruf),
  };
}

// ==================== UTILITY FUNCTIONS ====================

function formatDuration(seconds) {
  if (!seconds) return "0 menit";

  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);

  if (hours > 0) {
    return `${hours} jam ${minutes} menit`;
  }
  return `${minutes} menit`;
}

function getStatusBadge(status) {
  const badges = {
    SELESAI: { text: "Selesai", class: "success" },
    TIMEOUT: { text: "Timeout", class: "warning" },
    DIBATALKAN: { text: "Dibatalkan", class: "danger" },
  };
  return badges[status] || { text: status, class: "secondary" };
}

function getGradeBadge(grade) {
  const badges = {
    A: { text: "A", class: "success" },
    B: { text: "B", class: "info" },
    C: { text: "C", class: "warning" },
    D: { text: "D", class: "orange" },
    F: { text: "F", class: "danger" },
  };
  return badges[grade] || { text: grade, class: "secondary" };
}

// Export all functions as default object for easier import
export default {
  // CRUD
  getHasilUjian,
  getHasilUjianById,
  getHasilByPesertaAndUjian,
  getHasilByUjian,
  getHasilByPeserta,
  deleteHasilUjian,

  // Analytics
  updateAnalytics,
  generateAdvancedAnalytics,
  generateIndividualAnalytics,
  generateClassAnalytics,

  // Security
  updateSecurityStatus,
  submitAppeal,
  reviewAppeal,
  verifyResult,

  // Statistics
  getUjianStatistics,
  getOverviewStatistics,
  getFilteredStatistics,

  // Export
  exportHasilUjian,
  exportToExcel,
  exportToPDF,

  // Integration
  createFromSession,

  // Convenience
  getStudentResult,
  getTeacherReview,
  getClassPerformanceOverview,
  getStudentPerformanceWithRecommendations,

  // Utilities
  validateHasilUjianData,
  formatHasilUjianForDisplay,
  generateParticipantReport,
  autoGenerateAndDownloadReport,
};
