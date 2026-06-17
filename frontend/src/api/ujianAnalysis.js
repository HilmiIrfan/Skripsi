import request from "../utils/request";

// Get all analysis (with optional pagination & filter)
export function getAllAnalysis(params = {}) {
  return request({
    url: "/ujian-analysis",
    method: "get",
    params,
  });
}

// Get analysis by ujian ID
export function getAnalysisByUjian(ujianId, params = {}) {
  // Ensure we have a reasonable default size to get all analysis for the ujian
  const defaultParams = { size: 50, ...params };
  return request({
    url: `/ujian-analysis/ujian/${ujianId}`,
    method: "get",
    params: defaultParams,
  });
}

// Get analysis by type
export function getAnalysisByType(analysisType, params = {}) {
  return request({
    url: `/ujian-analysis/type/${analysisType}`,
    method: "get",
    params,
  });
}

// Get single analysis by ID
export function getAnalysisById(analysisId) {
  return request({
    url: `/ujian-analysis/${analysisId}`,
    method: "get",
  });
}

// Generate comprehensive analysis
export function generateAnalysis(data) {
  return request({
    url: "/ujian-analysis/generate",
    method: "post",
    data,
  });
}

// Update existing analysis
export function updateAnalysis(analysisId, data) {
  return request({
    url: `/ujian-analysis/${analysisId}`,
    method: "put",
    data,
  });
}

// Delete analysis by ID
export function deleteAnalysis(analysisId) {
  return request({
    url: `/ujian-analysis/${analysisId}`,
    method: "delete",
  });
}

// Compare multiple analyses
export function compareAnalyses(data) {
  return request({
    url: "/ujian-analysis/compare",
    method: "post",
    data,
  });
}

// Export analysis (POST)
export function exportAnalysis(data) {
  return request({
    url: "/ujian-analysis/export",
    method: "post",
    data,
  });
}

// Export analysis by ID (GET)
export function exportAnalysisById(
  analysisId,
  format,
  templateType = "STANDARD"
) {
  return request({
    url: `/ujian-analysis/${analysisId}/export/${format}`,
    method: "get",
    params: { templateType },
  });
}

// Get dashboard statistics
export function getAnalysisStatistics() {
  return request({
    url: "/ujian-analysis/statistics",
    method: "get",
  });
}

// Quick descriptive analysis
export function quickDescriptiveAnalysis(ujianId) {
  return request({
    url: "/ujian-analysis/quick-descriptive",
    method: "post",
    params: { ujianId },
  });
}

// Quick item analysis
export function quickItemAnalysis(ujianId) {
  return request({
    url: "/ujian-analysis/quick-item",
    method: "post",
    params: { ujianId },
  });
}

// Validate ujian for analysis
export function validateUjianForAnalysis(ujianId) {
  return request({
    url: `/ujian-analysis/validate/${ujianId}`,
    method: "get",
  });
}

// Auto-generate analysis for ujian (manual trigger)
export function autoGenerateAnalysisForUjian(ujianId) {
  return request({
    url: `/ujian-analysis/auto-generate/${ujianId}`,
    method: "post",
  });
}

// Clean up duplicate analysis for a specific ujian
export function cleanupDuplicatesForUjian(
  ujianId,
  analysisType = "COMPREHENSIVE"
) {
  return request({
    url: `/ujian-analysis/cleanup/ujian/${ujianId}`,
    method: "delete",
    params: { analysisType },
  });
}

// Clean up all duplicate analysis records
export function cleanupAllDuplicates() {
  return request({
    url: "/ujian-analysis/cleanup/all",
    method: "delete",
  });
}

// Force regenerate analysis for ujian
export function forceRegenerateAnalysis(ujianId) {
  return request({
    url: `/ujian-analysis/force-regenerate/${ujianId}`,
    method: "post",
  });
}

// Generate participant-based analysis for ujian (use existing auto-generate endpoint)
export function generateParticipantBasedAnalysis(ujianId) {
  return request({
    url: `/ujian-analysis/auto-generate/${ujianId}`,
    method: "post",
  });
}

// Get participant-based analysis
export function getParticipantBasedAnalysis(params = {}) {
  return request({
    url: "/ujian-analysis/participants",
    method: "get",
    params,
  });
}
