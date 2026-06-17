/**
 * Safe wrapper for hasil ujian API calls to handle backend metadata parsing issues
 * This utility provides safe defaults and error handling for the Map field deserialization issues
 */
import { getHasilByUjian, getHasilUjian } from "@/api/hasilUjian";
import { message } from "antd";

/**
 * Safely get hasil ujian by ujian ID with fallback handling
 */
export async function safeGetHasilByUjian(idUjian, options = {}) {
  const {
    showErrorMessage = true,
    includeAnalytics = true,
    includeSecurityData = true,
  } = options; // Add includeAnalytics and includeSecurityData

  try {
    // Pass includeAnalytics and includeSecurityData to the underlying API call
    const result = await getHasilByUjian(
      idUjian,
      includeAnalytics,
      includeSecurityData
    );
    return {
      success: true,
      data: result.data,
      error: null,
    };
  } catch (error) {
    console.error("Error in safeGetHasilByUjian:", error);

    if (showErrorMessage) {
      message.warning(
        "Gagal memuat data hasil ujian. Data mungkin bermasalah di backend."
      );
    }

    return {
      success: false,
      data: { content: [] },
      error: error.message,
    };
  }
}

/**
 * Safely get all hasil ujian with fallback handling
 */
export async function safeGetAllHasilUjian(size = 1000, options = {}) {
  const {
    showErrorMessage = true,
    includeAnalytics = true,
    includeSecurityData = true,
  } = options; // Add includeAnalytics and includeSecurityData

  try {
    // Pass includeAnalytics and includeSecurityData to the underlying API call
    const result = await getHasilUjian(size, {
      includeAnalytics,
      includeSecurityData,
    }); // Pass options as an object
    return {
      success: true,
      data: result.data,
      error: null,
    };
  } catch (error) {
    console.error("Error in safeGetAllHasilUjian:", error);

    if (showErrorMessage) {
      message.warning(
        "Gagal memuat semua data hasil ujian. Data metadata mungkin bermasalah."
      );
    }

    return {
      success: false,
      data: { content: [] },
      error: error.message,
    };
  }
}

/**
 * Transform hasil ujian data to safe format, handling missing metadata fields
 * This function provides safe defaults for fields that may cause backend JSON parsing issues
 */
export function transformHasilUjianData(hasil, index = 0) {
  return {
    // Basic identification
    key: hasil.idHasilUjian || `hasil-${index}`,
    siswaId: hasil.idPeserta,
    ujianId: hasil.idUjian,

    // Student information
    nimSiswa: hasil.peserta?.nim || hasil.peserta?.id || hasil.idPeserta,
    namaSiswa: hasil.peserta?.nama || hasil.peserta?.name || hasil.idPeserta,
    pesertaNama: hasil.peserta?.nama || hasil.peserta?.name || hasil.idPeserta,
    pesertaUsername: hasil.peserta?.username || hasil.idPeserta, // Class and school information - check both direct and ujian-nested locations
    namaKelas:
      hasil.kelas?.namaKelas ||
      hasil.ujian?.kelas?.namaKelas ||
      "Tidak Diketahui",
    kelas:
      hasil.kelas?.namaKelas ||
      hasil.ujian?.kelas?.namaKelas ||
      "Tidak Diketahui",
    mapel: hasil.mapel?.name || hasil.ujian?.mapel?.name || "Tidak Diketahui",
    namaMapel:
      hasil.mapel?.name || hasil.ujian?.mapel?.name || "Tidak Diketahui",
    school:
      hasil.school?.nameSchool ||
      hasil.ujian?.school?.nameSchool ||
      "Tidak Diketahui",
    namaSekolah:
      hasil.school?.nameSchool ||
      hasil.ujian?.school?.nameSchool ||
      "Tidak Diketahui",

    // Exam information
    namaUjian: hasil.ujian?.namaUjian || "Ujian",
    ujianNama: hasil.ujian?.namaUjian || "Ujian",

    // Performance data - using consistent field names based on actual response
    nilai: hasil.persentase || hasil.totalSkor || 0, // Use persentase as primary nilai
    totalSkor: hasil.totalSkor || 0,
    skorMaksimal: hasil.skorMaksimal || 100,
    persentase: hasil.persentase || 0,
    nilaiHuruf: hasil.nilaiHuruf || "E",
    lulus: hasil.lulus || false,

    // Question breakdown
    jumlahSoal: hasil.totalSoal || hasil.ujian?.jumlahSoal || 0,
    totalSoal: hasil.totalSoal || hasil.ujian?.jumlahSoal || 0,
    jumlahBenar: hasil.jumlahBenar || 0,
    jumlahSalah: hasil.jumlahSalah || 0,
    jumlahKosong: hasil.jumlahKosong || 0,

    // Legacy field mappings for backward compatibility
    soalTerjawab: hasil.totalSoal - (hasil.jumlahKosong || 0),
    soalBenar: hasil.jumlahBenar || 0,
    soalSalah: hasil.jumlahSalah || 0,
    soalKosong: hasil.jumlahKosong || 0,

    // Time metrics - convert seconds to minutes for durasi, or calculate from timestamps
    durasi: (() => {
      if (hasil.durasiPengerjaan) {
        return Math.round(hasil.durasiPengerjaan / 60);
      } else if (hasil.waktuMulai && hasil.waktuSelesai) {
        const mulai = new Date(hasil.waktuMulai);
        const selesai = new Date(hasil.waktuSelesai);
        const diffMs = selesai - mulai;
        return Math.round(diffMs / (1000 * 60)); // convert to minutes
      }
      return hasil.durasi || 0;
    })(),
    durasiPengerjaan: hasil.durasiPengerjaan || hasil.durasi || 0,
    sisaWaktu: (() => {
      // Calculate sisaWaktu if not provided
      if (hasil.sisaWaktu) {
        return hasil.sisaWaktu;
      } else if (hasil.ujian?.durasiMenit && hasil.durasiPengerjaan) {
        const durasiUjianDetik = hasil.ujian.durasiMenit * 60;
        const sisaWaktuDetik = durasiUjianDetik - hasil.durasiPengerjaan;
        return Math.max(0, sisaWaktuDetik); // tidak boleh negatif
      }
      return 0;
    })(),
    waktuMulai: hasil.waktuMulai,
    waktuSelesai: hasil.waktuSelesai,
    statusPengerjaan: hasil.statusPengerjaan || "SELESAI",

    // Security & Analytics (with safe defaults due to backend JSON parsing issues)
    violationCount: getViolationCount(hasil),
    jumlahPelanggaran: getViolationCount(hasil),

    // Calculate derived fields
    riskLevel: calculateRiskLevel(
      hasil.persentase || 0,
      getViolationCount(hasil)
    ),
    needsReview: (hasil.persentase || 0) < 60 || getViolationCount(hasil) > 0,
    integrityScore: Math.max(0, 100 - getViolationCount(hasil) * 20), // Learning analytics (safe defaults)
    workingPattern: hasil.workingPattern || "Normal",
    learningStyle: hasil.learningStyle || "Mixed",
    confidenceLevel: hasil.confidenceLevel || "MEDIUM",

    // Security data - preserve original structure for direct access
    securityFlags: hasil.securityFlags || {},
    metadata: hasil.metadata || {},

    // Answer data - IMPORTANT: Preserve original answer structures
    jawabanPeserta: hasil.jawabanPeserta || {},
    jawabanBenar: hasil.jawabanBenar || {},
    skorPerSoal: hasil.skorPerSoal || {},

    // Additional exam data
    sessionId: hasil.sessionId,
    idHasilUjian: hasil.idHasilUjian,
    idPeserta: hasil.idPeserta,
    idUjian: hasil.idUjian,
    attemptNumber: hasil.attemptNumber || 1,
    isAutoSubmit: hasil.isAutoSubmit || false,

    // Preserve peserta object
    peserta: hasil.peserta || {
      id: hasil.idPeserta,
      name: hasil.peserta?.name || hasil.idPeserta,
      username: hasil.peserta?.username || hasil.idPeserta,
    },

    // Preserve ujian object
    ujian: hasil.ujian || {},

    // Full data for detail view and legacy compatibility
    fullData: hasil,
  };
}

/**
 * Safely extract violation count from various possible field structures
 */
function getViolationCount(hasil) {
  // Try different possible field locations for violation count
  if (hasil.violationCount !== undefined) {
    return parseInt(hasil.violationCount) || 0;
  }

  if (hasil.securityFlags?.violationCount !== undefined) {
    return parseInt(hasil.securityFlags.violationCount) || 0;
  }

  if (hasil.metadata?.violations && Array.isArray(hasil.metadata.violations)) {
    // Sum violationCount from each item in the violations array
    return hasil.metadata.violations.reduce(
      (sum, v) => sum + (parseInt(v.violationCount) || 0),
      0
    );
  }

  // Fallback: count from violations array in metadata (if it's not an array, try to parse it)
  if (hasil.metadata?.violations) {
    try {
      const violations = Array.isArray(hasil.metadata.violations)
        ? hasil.metadata.violations
        : [hasil.metadata.violations]; // Treat as single item if not array
      return violations
        .filter((v) => v && v.violationCount)
        .reduce((sum, v) => sum + (parseInt(v.violationCount) || 0), 0);
    } catch (error) {
      console.warn("Error parsing violations from metadata:", error);
      return 0;
    }
  }

  return 0;
}

/**
 * Calculate risk level based on score and violation count
 */
function calculateRiskLevel(score, violationCount = 0) {
  if (violationCount > 2 || score < 40) {
    return "HIGH";
  } else if (violationCount > 0 || score < 60) {
    return "MEDIUM";
  } else {
    return "LOW";
  }
}

/**
 * Apply search filter to hasil ujian data
 */
export function applySearchFilter(data, searchText) {
  if (!searchText) return data;

  const lowerSearchText = searchText.toLowerCase();
  return data.filter(
    (item) =>
      (item.namaSiswa || item.pesertaNama || "")
        .toLowerCase()
        .includes(lowerSearchText) ||
      (item.namaUjian || item.ujianNama || "")
        .toLowerCase()
        .includes(lowerSearchText) ||
      (item.nimSiswa || "").toLowerCase().includes(lowerSearchText)
  );
}

/**
 * Apply pagination to data
 */
export function applyPagination(data, page, size) {
  const startIndex = (page - 1) * size;
  const endIndex = startIndex + size;
  return data.slice(startIndex, endIndex);
}
