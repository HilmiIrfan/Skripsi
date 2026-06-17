import request from "@/utils/request";

// ==================== BASIC CRUD OPERATIONS ====================

export function getUjian() {
  return request({
    url: "/ujian",
    method: "get",
  });
}

export function getUjianByStatus() {
  return request({
    url: `/ujian/status/${status}`,
    method: "get",
  });
}

export function getUjianAktif() {
  return request({
    url: "/ujian/aktif",
    method: "get",
  });
}

export function addUjian(data) {
  return request({
    url: "/ujian",
    method: "post",
    data,
  });
}

export function getUjianById(ujianId) {
  return request({
    url: `/ujian/${ujianId}`,
    method: "get",
  });
}

export function editUjian(data, ujianId) {
  return request({
    url: `/ujian/${ujianId}`,
    method: "put",
    data,
  });
}

export function deleteUjian(data) {
  return request({
    url: `/ujian/${data.idUjian}`,
    method: "delete",
  });
}

// ==================== UJIAN STATE MANAGEMENT ====================

export function activateUjian(ujianId) {
  return request({
    url: `/ujian/${ujianId}/activate`,
    method: "post",
  });
}

export function startUjian(ujianId) {
  return request({
    url: `/ujian/${ujianId}/start`,
    method: "post",
  });
}

export function endUjian(ujianId) {
  return request({
    url: `/ujian/${ujianId}/end`,
    method: "post",
  });
}

export function cancelUjian(ujianId) {
  return request({
    url: `/ujian/${ujianId}/cancel`,
    method: "post",
  });
}

// ==================== BANK SOAL MANAGEMENT ====================

export function addBankSoalToUjian(ujianId, bankSoalId) {
  return request({
    url: `/ujian/${ujianId}/bankSoal/${bankSoalId}`,
    method: "post",
  });
}

export function removeBankSoalFromUjian(ujianId, bankSoalId) {
  return request({
    url: `/ujian/${ujianId}/bankSoal/${bankSoalId}`,
    method: "delete",
  });
}

// ==================== PARTICIPANT ACCESS CONTROL ====================

export function canParticipantStartUjian(ujianId) {
  return request({
    url: `/ujian/${ujianId}/canStart`,
    method: "get",
  });
}

export function getUjianTimeInfo(ujianId) {
  return request({
    url: `/ujian/${ujianId}/timeInfo`,
    method: "get",
  });
}

// ==================== STATISTICS AND MONITORING ====================

export function getUjianStatistics() {
  return request({
    url: "/ujian/statistics",
    method: "get",
  });
}

export function createAndActivateUjian(data) {
  return request({
    url: "/ujian/createAndActivate",
    method: "post",
    data,
  });
}

// Alias functions for compatibility
export const getAllUjian = getUjian;
export const createUjian = addUjian;
export const updateUjian = (ujianId, data) => editUjian(data, ujianId);
