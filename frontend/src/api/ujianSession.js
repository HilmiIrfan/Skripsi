import request from "../utils/request";

// Mulai session ujian
export function startUjianSession({ idUjian, idPeserta }) {
  return request({
    url: "/ujian-session/start",
    method: "post",
    data: { idUjian, idPeserta },
  });
}

// Resume atau mulai session ujian baru (RECOMMENDED)
export function resumeOrStartSession({ idUjian, idPeserta, kodeUjian }) {
  return request({
    url: "/ujian-session/resume-or-start",
    method: "post",
    data: { idUjian, idPeserta, kodeUjian },
  });
}

// Simpan jawaban individual
export function saveJawaban({
  idUjian,
  idPeserta,
  sessionId,
  idBankSoal,
  jawaban,
  currentSoalIndex,
}) {
  return request({
    url: "/ujian-session/save-jawaban",
    method: "post",
    data: {
      idUjian,
      idPeserta,
      sessionId,
      idBankSoal,
      jawaban,
      currentSoalIndex,
    },
  });
}

// Submit ujian final
export function submitUjian({
  idUjian,
  idPeserta,
  sessionId,
  answers,
  isAutoSubmit,
  finalTimeRemaining,
}) {
  return request({
    url: "/ujian-session/submit",
    method: "post",
    data: {
      idUjian,
      idPeserta,
      sessionId,
      answers,
      isAutoSubmit,
      finalTimeRemaining,
    },
  });
}

// Ambil progress ujian peserta (untuk resume session)
export function getUjianProgress(idUjian, idPeserta) {
  return request({
    url: `/ujian-session/progress/${idUjian}/${idPeserta}`,
    method: "get",
  });
}

// Ambil detail session ujian yang sedang berjalan
export function getActiveSession(idUjian, idPeserta) {
  return request({
    url: `/ujian-session/active/${idUjian}/${idPeserta}`,
    method: "get",
  });
}

// Auto save progress (save multiple answers at once)
export function autoSaveProgress({
  idUjian,
  idPeserta,
  sessionId,
  answers,
  currentSoalIndex,
  timeRemaining,
}) {
  return request({
    url: "/ujian-session/auto-save",
    method: "post",
    data: {
      idUjian,
      idPeserta,
      sessionId,
      answers,
      currentSoalIndex,
      timeRemaining,
    },
  });
}

// Validasi apakah peserta bisa memulai ujian
export function validateCanStart(idUjian, idPeserta) {
  return request({
    url: `/ujian-session/validate-start/${idUjian}/${idPeserta}`,
    method: "get",
  });
}

// Ping untuk keep session alive
export function keepSessionAlive(idUjian, idPeserta, data = {}) {
  return request({
    url: `/ujian-session/keep-alive/${idUjian}/${idPeserta}`,
    method: "post",
    data,
  });
}

// Ambil time remaining untuk session yang sudah dimulai
export function getTimeRemaining(idUjian, idPeserta) {
  return request({
    url: `/ujian-session/time-remaining/${idUjian}/${idPeserta}`,
    method: "get",
  });
}

// Update current soal index (untuk tracking progress)
export function updateCurrentSoal({
  idUjian,
  idPeserta,
  sessionId,
  currentSoalIndex,
  previousSoalIndex,
  navigationAction,
}) {
  return request({
    url: "/ujian-session/update-current-soal",
    method: "post",
    data: {
      idUjian,
      idPeserta,
      sessionId,
      currentSoalIndex,
      previousSoalIndex,
      navigationAction,
    },
  });
}

export default {
  startUjianSession,
  resumeOrStartSession,
  saveJawaban,
  submitUjian,
  getUjianProgress,
  getActiveSession,
  autoSaveProgress,
  validateCanStart,
  keepSessionAlive,
  getTimeRemaining,
  updateCurrentSoal,
};
