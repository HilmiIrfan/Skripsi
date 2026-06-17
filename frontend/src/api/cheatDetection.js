import request from "../utils/request";

// Record single violation from frontend
export function recordViolation(data) {
  return request({
    url: "/cheat-detection/record-violation",
    method: "post",
    data,
  });
}

// Record multiple violations in batch
export function recordViolationBatch(data) {
  return request({
    url: "/cheat-detection/record-batch",
    method: "post",
    data,
  });
}

// Analyze answer patterns for suspicious behavior
export function analyzeAnswerPatterns(data) {
  return request({
    url: "/cheat-detection/analyze-patterns",
    method: "post",
    data,
  });
}

// Take action on a violation (admin/proctor only)
export function takeAction(data) {
  return request({
    url: "/cheat-detection/take-action",
    method: "post",
    data,
  });
}

// Resolve violation (mark as reviewed)
export function resolveViolation(data) {
  return request({
    url: "/cheat-detection/resolve",
    method: "post",
    data,
  });
}

// Get violations by criteria
export function getViolations(params = {}) {
  return request({
    url: "/cheat-detection/violations",
    method: "get",
    params,
  });
}

// Get violation by ID
export function getViolationById(detectionId) {
  return request({
    url: `/cheat-detection/violations/${detectionId}`,
    method: "get",
  });
}

// Get violations by session ID
export function getViolationsBySession(sessionId, params = {}) {
  return request({
    url: `/cheat-detection/session/${sessionId}`,
    method: "get",
    params,
  });
}

// Get violations by ujian ID
export function getViolationsByUjian(idUjian, params = {}) {
  return request({
    url: `/cheat-detection/ujian/${idUjian}`,
    method: "get",
    params,
  });
}

// Generate violation statistics
export function generateStatistics(params = {}) {
  return request({
    url: "/cheat-detection/statistics",
    method: "get",
    params,
  });
}

// Export violations report
export function exportViolations(data) {
  return request({
    url: "/cheat-detection/export",
    method: "post",
    data,
    responseType: "blob",
  });
}

// Get unresolved violations (for dashboard alert)
export function getUnresolvedViolations(params = {}) {
  return request({
    url: "/cheat-detection/unresolved",
    method: "get",
    params,
  });
}

// Get critical violations (for immediate attention)
export function getCriticalViolations(params = {}) {
  return request({
    url: "/cheat-detection/critical",
    method: "get",
    params,
  });
}

// Get violations by student and ujian (for history)
export function getCheatDetectionByStudent(params = {}) {
  return request({
    url: "/cheat-detection/violations",
    method: "get",
    params,
  });
}

export default {
  recordViolation,
  recordViolationBatch,
  analyzeAnswerPatterns,
  takeAction,
  resolveViolation,
  getViolations,
  getViolationById,
  getViolationsBySession,
  getViolationsByUjian,
  generateStatistics,
  exportViolations,
  getUnresolvedViolations,
  getCriticalViolations,
  getCheatDetectionByStudent,
};
