/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useCallback } from "react";
import {
  Table,
  Card,
  Button,
  Select,
  Input,
  Space,
  Tag,
  Modal,
  Descriptions,
  Typography,
  Row,
  Col,
  Statistic,
  DatePicker,
  message,
  Spin,
  Badge,
  Avatar,
  Alert,
  Tooltip,
  Progress,
  Popconfirm,
  Divider,
} from "antd";
import {
  FileExcelOutlined,
  EyeOutlined,
  SearchOutlined,
  ReloadOutlined,
  UserOutlined,
  TrophyOutlined,
  ClockCircleOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  DeleteOutlined,
  FileTextOutlined,
  WarningOutlined,
  ExclamationCircleOutlined,
  DatabaseOutlined, // Added this icon for the new feature
} from "@ant-design/icons";
import moment from "moment"; // Pastikan 'moment' terinstal: npm install moment
import dayjs from "dayjs"; // dayjs sudah diimpor dan dapat digunakan untuk format tanggal
import * as XLSX from "xlsx"; // Pastikan 'xlsx' terinstal: npm install xlsx

// --- IMPOR LOKAL ---
// Harap pastikan jalur impor ini benar di proyek Anda dan dependensi terinstal.
// Jika Anda menggunakan alias seperti '@/', pastikan dikonfigurasi di jsconfig.json/tsconfig.json Anda.
import { getUjian } from "@/api/ujian";
import { getHasilByUjian, getHasilUjian } from "@/api/hasilUjian";
import {
  getCheatDetectionByStudent,
  getViolations, // Used for fetching all violations
} from "@/api/cheatDetection";
import {
  safeGetHasilByUjian,
  safeGetAllHasilUjian,
  transformHasilUjianData,
  applySearchFilter,
} from "@/utils/safeHasilUjianApi";
import { deleteHasilUjian } from "@/api/hasilUjian";
import { getBankSoal } from "@/api/bankSoal"; // Import untuk mengambil detail soal
import { useAuth } from "@/contexts/AuthContext";
import TypingCard from "@/components/TypingCard";
// -------------------

const { Title, Text } = Typography;
const { Option } = Select;
const { RangePicker } = DatePicker;

const ReportNilaiSiswa = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  // NOTE: allViolationsData sudah tidak digunakan, violations diambil dari metadata
  const [data, setData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [ujianList, setUjianList] = useState([]);
  const [kelasList, setKelasList] = useState([]);
  const [selectedUjian, setSelectedUjian] = useState(null);
  const [selectedKelas, setSelectedKelas] = useState(null);
  const [searchText, setSearchText] = useState("");
  const [dateRange, setDateRange] = useState([]);
  const [detailModal, setDetailModal] = useState({
    visible: false,
    data: null,
  });
  const [violationModalVisible, setViolationModalVisible] = useState(false);
  const [violations, setViolations] = useState([]); // Violations for a specific student's exam session
  const [loadingViolations, setLoadingViolations] = useState(false);
  const [statistics, setStatistics] = useState({
    totalSiswa: 0,
    rataRataNilai: 0,
    siswaLulus: 0,
    siswaTidakLulus: 0,
  });

  // --- NEW STATE FOR GLOBAL VIOLATIONS MODAL ---
  const [showAllViolationsModal, setShowAllViolationsModal] = useState(false);
  const [loadingAllViolations, setLoadingAllViolations] = useState(false);

  // --- NEW STATE FOR EXPORT MODAL ---
  const [showExportModal, setShowExportModal] = useState(false);
  const [selectedUjianForExport, setSelectedUjianForExport] = useState(null);
  const [loadingExport, setLoadingExport] = useState(false);

  // State untuk menyimpan data bank soal (untuk detail pertanyaan)
  const [bankSoalMap, setBankSoalMap] = useState({});
  // ---------------------------------------------

  // Fetch bank soal details untuk mapping pertanyaan dengan jawaban
  const fetchBankSoalDetails = useCallback(async () => {
    try {
      const response = await getBankSoal();
      if (response.data.statusCode === 200 && response.data.content) {
        // Convert array to map dengan key idBankSoal
        const soalMap = {};
        response.data.content.forEach((soal) => {
          soalMap[soal.idBankSoal] = soal;
        });
        setBankSoalMap(soalMap);
      }
    } catch (error) {
      console.error("Error fetching bank soal:", error);
      message.error("Gagal memuat data bank soal untuk detail pertanyaan");
    }
  }, []);

  // ...existing code...

  const handleDeleteHasilUjian = async (record) => {
    Modal.confirm({
      title: "Konfirmasi Hapus",
      content:
        "Apakah Anda yakin ingin menghapus hasil ujian ini? Aksi ini tidak dapat dibatalkan.",
      okText: "Ya, Hapus",
      okType: "danger",
      cancelText: "Batal",
      onOk: async () => {
        try {
          await deleteHasilUjian(record);
          message.success("Hasil ujian berhasil dihapus");
          fetchReportData();
        } catch (error) {
          message.error("Gagal menghapus hasil ujian: " + error.message);
        }
      },
    });
  };

  // Fetch ujian list for filter
  const fetchUjianList = useCallback(async () => {
    try {
      const response = await getUjian();
      if (response.data.statusCode === 200) {
        setUjianList(response.data.content || []);
      }
    } catch (error) {
      console.error("Error fetching ujian list:", error);
      message.error("Gagal memuat daftar ujian");
    }
  }, []);

  // Fetch kelas list for filter
  const fetchKelasList = useCallback(async () => {
    try {
      // For now, we set an empty class list as the API might not be available
      setKelasList([]);
    } catch (error) {
      console.error("Error fetching kelas list:", error);
      message.error("Gagal memuat daftar kelas");
    }
  }, []);

  const fetchAllViolationDetection = useCallback(async () => {
    setLoadingAllViolations(true);
    try {
      const response = await getViolations({
        limit: 10000,
        includeEvidence: true,
        includeActions: true,
        timeRange: "ALL",
      });

      // Perhatikan struktur response.data.data.violations
      const violations =
        response?.data?.data?.violations || response?.data?.violations || [];

      // NOTE: Tidak lagi menyimpan allViolationsData, menggunakan metadata langsung
    } catch (error) {
      console.error("Error fetching violations info:", error);
    } finally {
      setLoadingAllViolations(false);
    }
  }, []);
  // ---------------------------------------------------

  // Calculate statistics - IMPROVED with null checks
  const calculateStatistics = (data) => {
    if (!data || !Array.isArray(data)) {
      setStatistics({
        totalSiswa: 0,
        rataRataNilai: 0,
        siswaLulus: 0,
        siswaTidakLulus: 0,
      });
      return;
    }

    const total = data.length;
    const lulus = data.filter((item) => {
      const nilai = parseFloat(item.nilai || item.skor || 0);
      const minScore = parseFloat(
        item.ujian?.minPassingScore || item.ujian?.nilaiMinimal || 75
      );
      return nilai >= minScore;
    }).length;
    const tidakLulus = total - lulus;
    const rataRata =
      total > 0
        ? (
            data.reduce(
              (sum, item) => sum + (parseFloat(item.nilai || item.skor) || 0),
              0
            ) / total
          ).toFixed(2)
        : 0;

    setStatistics({
      totalSiswa: total,
      rataRataNilai: parseFloat(rataRata),
      siswaLulus: lulus,
      siswaTidakLulus: tidakLulus,
    });
  };

  // Get status color and icon
  const getStatusDisplay = (record) => {
    if (!record)
      return {
        color: "default",
        icon: <ExclamationCircleOutlined />,
        text: "BELUM DIKETAHUI",
      };

    // Check if exam is completed and has a valid score
    const nilai = parseFloat(
      record.nilai || record.skor || record.persentase || 0
    );
    const hasValidScore = nilai > 0 || record.lulus !== undefined;

    // If we have explicit lulus status, use it
    if (record.lulus !== undefined && hasValidScore) {
      if (record.lulus === true) {
        return {
          color: "success",
          icon: <CheckCircleOutlined />,
          text: "LULUS",
        };
      } else {
        return {
          color: "error",
          icon: <CloseCircleOutlined />,
          text: "TIDAK LULUS",
        };
      }
    }

    // Fallback to score-based logic if lulus field is not available
    if (hasValidScore) {
      const minScore = parseFloat(
        record.ujian?.minPassingScore || record.ujian?.nilaiMinimal || 75
      );

      if (nilai >= minScore) {
        return {
          color: "success",
          icon: <CheckCircleOutlined />,
          text: "LULUS",
        };
      } else {
        return {
          color: "error",
          icon: <CloseCircleOutlined />,
          text: "TIDAK LULUS",
        };
      }
    } else {
      return {
        color: "processing",
        icon: <ClockCircleOutlined />,
        text: "DALAM PROSES",
      };
    }
  };

  // Fetch report data for the main table
  const fetchReportData = useCallback(async () => {
    setLoading(true);
    try {
      let allReports = [];

      if (selectedUjian) {
        // Tambahkan parameter untuk memastikan data peserta di-include
        const result = await safeGetHasilByUjian(selectedUjian, {
          showErrorMessage: false,
          // Tambahkan parameter ini jika API mendukung
          include: [
            "peserta",
            "ujian",
            "ujian.kelas",
            "ujian.mapel",
            "ujian.semester",
          ],
          // Atau parameter serupa
          expand: "peserta,ujian",
          // Atau pastikan semua field di-select
          fields: "*",
          // Parameter untuk memastikan relasi dimuat
          with: "peserta,ujian",
        });

        console.log("API Response for specific ujian:", result);

        if (result.success && result.data?.content) {
          allReports = result.data.content.map((hasil, index) => {
            const transformed = transformHasilUjianData(hasil, index);

            // Pastikan data peserta tidak hilang
            if (hasil.peserta && !transformed.peserta) {
              transformed.peserta = hasil.peserta;
            }

            return transformed;
          });
        }
      } else {
        // Sama untuk semua hasil ujian
        const result = await safeGetAllHasilUjian(1000, {
          showErrorMessage: false,
          // Tambahkan parameter yang sama
          include: [
            "peserta",
            "ujian",
            "ujian.kelas",
            "ujian.mapel",
            "ujian.semester",
          ],
          expand: "peserta,ujian",
          fields: "*",
          with: "peserta,ujian",
        });

        if (result.success && result.data?.content) {
          allReports = result.data.content.map((hasil, index) => {
            // Debug yang sama

            const transformed = transformHasilUjianData(hasil, index);

            // Pastikan data peserta tidak hilang
            if (hasil.peserta && !transformed.peserta) {
              transformed.peserta = hasil.peserta;
            }

            return transformed;
          });
        }
      }

      // Filter by date range if specified
      if (dateRange.length === 2) {
        allReports = allReports.filter((item) => {
          if (!item.waktuMulai) return false;
          const itemDate = dayjs(item.waktuMulai);
          return itemDate.isBetween(dateRange[0], dateRange[1], "day", "[]");
        });
      }

      setData(allReports);
      setFilteredData(allReports);
      calculateStatistics(allReports);
    } catch (error) {
      console.error("Error fetching report data:", error);
      message.error("Gagal memuat data report nilai siswa");
    } finally {
      setLoading(false);
    }
  }, [selectedUjian, dateRange]);

  // Show violations modal (for specific student's exam dengan deduplication)
  const showViolationsModal = (record) => {
    setDetailModal({
      visible: false, // Hide detail modal if it's open
      data: record, // Keep data for context in violations modal
    });
    setViolationModalVisible(true);

    // Ambil violations dari metadata langsung
    const violationsFromMetadata = record.metadata?.violations || [];

    // Deduplicate violations
    const uniqueViolations = deduplicateViolations(violationsFromMetadata);

    setLoadingViolations(false);
    setViolations(uniqueViolations);

    if (uniqueViolations.length === 0) {
      message.info("Tidak ada pelanggaran ditemukan untuk siswa ini.");
    }
  };

  // --- Initial data fetches when component mounts ---
  useEffect(() => {
    fetchUjianList();
    fetchKelasList();
    fetchBankSoalDetails(); // Fetch bank soal untuk detail export
    // Call the new function to fetch ALL violations initially
    fetchAllViolationDetection();
  }, [
    fetchUjianList,
    fetchKelasList,
    fetchBankSoalDetails,
    fetchAllViolationDetection,
  ]);

  useEffect(() => {
    fetchReportData();
  }, [fetchReportData]);

  // Filter data based on search text for the main report table
  useEffect(() => {
    const filteredBySearch = applySearchFilter(data, searchText);
    setFilteredData(filteredBySearch);
  }, [searchText, data]);

  // Show detail modal (for main report table)
  const showDetail = async (record) => {
    let data = { ...record };
    // Check if data is in fullData (fullData provides fallback answer data)

    // If bankSoalMap is empty, try to fetch it
    if (Object.keys(bankSoalMap).length === 0) {
      await fetchBankSoalDetails();
    }

    // Cari id ujian dari beberapa kemungkinan field
    const idUjian =
      data.idUjian || data.ujianId || data.ujian?.idUjian || data.ujian_id;

    // Merge data ujian dari ujianList jika ada, atau fetch jika tidak ada
    if (idUjian) {
      let ujianData = null;

      // Cari di ujianList dulu
      if (ujianList.length > 0) {
        ujianData = ujianList.find((u) => u.idUjian === idUjian);
      }

      // Jika tidak ada di ujianList, fetch langsung
      if (!ujianData) {
        try {
          const result = await getUjian();
          if (result.data?.statusCode === 200 && result.data?.content) {
            ujianData = result.data.content.find((u) => u.idUjian === idUjian);
          }
        } catch (error) {
          console.error("Error fetching ujian details:", error);
        }
      }

      if (ujianData) {
        data.ujian = ujianData;
      }
    }

    // IMPORTANT: Ensure answer data is available - check fullData fallback
    if (!data.jawabanPeserta && data.fullData?.jawabanPeserta) {
      data.jawabanPeserta = data.fullData.jawabanPeserta;
      data.jawabanBenar = data.fullData.jawabanBenar;
      data.skorPerSoal = data.fullData.skorPerSoal;
    }

    setDetailModal({
      visible: true,
      data,
    });
  };
  // --- NEW: Columns for All Violations Modal Table ---
  // These columns are designed to handle potential nulls from backend,
  // displaying "N/A" or "Tidak Diketahui" where data is missing.
  const allViolationsColumns = [
    {
      title: "No",
      key: "no",
      width: 60,
      render: (_, __, index) => index + 1,
    },
    {
      title: "ID Deteksi",
      dataIndex: "idDetection",
      key: "idDetection",
      width: 150,
      ellipsis: true,
      render: (text) => (
        <Tooltip title={text || "N/A"}>
          {text ? `${text.substring(0, 8)}...` : "N/A"}
        </Tooltip>
      ),
    },
    {
      title: "Nama Siswa",
      dataIndex: "studentName",
      key: "studentName",
      width: 150,
      ellipsis: true,
    },
    {
      title: "NISN",
      dataIndex: "studentNIM",
      key: "studentNIM",
      width: 120,
    },
    {
      title: "Nama Ujian",
      dataIndex: "ujianNama",
      key: "ujianNama",
      width: 200,
      ellipsis: true,
      render: (text) => (
        <Tooltip title={text || "Tidak tersedia"}>
          <Text strong style={{ color: "#1890ff" }}>
            {text || "Tidak tersedia"}
          </Text>
        </Tooltip>
      ),
    },
    {
      title: "Session ID",
      dataIndex: "sessionId",
      key: "sessionId",
      width: 150,
      ellipsis: true,
      render: (text) => (
        <Tooltip title={text || "N/A"}>
          {text ? `${text.substring(0, 8)}...` : "N/A"}
        </Tooltip>
      ),
    },
    {
      title: "Jenis Pelanggaran",
      dataIndex: "typeViolation",
      key: "typeViolation",
      render: (text) => <Tag color="red">{text || "Tidak Diketahui"}</Tag>,
    },
    {
      title: "Keparahan",
      dataIndex: "severity",
      key: "severity",
      render: (text) => {
        let color = "default";
        const severityText = text || "Tidak Diketahui";
        if (severityText === "HIGH") color = "red";
        else if (severityText === "MEDIUM") color = "orange";
        else if (severityText === "LOW") color = "yellow";
        return <Tag color={color}>{severityText}</Tag>;
      },
    },
    {
      title: "Waktu Deteksi",
      dataIndex: "detectedAt",
      key: "detectedAt",
      render: (text) =>
        text ? dayjs(text).format("DD/MM/YYYY HH:mm:ss") : "N/A",
    },
  ];
  // ---------------------------------------------------

  // Helper: Deduplicate violations based on detectedAt and typeViolation
  const deduplicateViolations = (violations) => {
    if (!violations || !Array.isArray(violations)) return [];

    const uniqueViolations = [];
    const seen = new Set();

    violations.forEach((violation) => {
      // Create unique key based on detection time and violation type
      const detectedAt =
        violation.detectedAt || violation.timestamp || violation.detectionTime;
      const typeViolation =
        violation.typeViolation || violation.violationType || violation.type;

      // Format waktu untuk konsistensi (gunakan ISO string atau timestamp)
      const normalizedTime = detectedAt
        ? new Date(detectedAt).toISOString()
        : "";
      const uniqueKey = `${normalizedTime}_${typeViolation}`;

      // Jika belum ada di set, tambahkan ke unique violations
      if (!seen.has(uniqueKey)) {
        seen.add(uniqueKey);
        uniqueViolations.push({
          ...violation,
          // Hilangkan violationCount untuk menghindari kebingungan
          violationCount: undefined,
        });
      }
    });

    return uniqueViolations;
  };

  // Helper: Get all violations from metadata across all records
  const getAllViolationsFromMetadata = () => {
    const allViolations = [];

    filteredData.forEach((item) => {
      const violations = item.metadata?.violations || [];
      // Deduplicate violations untuk setiap siswa
      const uniqueViolations = deduplicateViolations(violations);

      uniqueViolations.forEach((violation) => {
        allViolations.push({
          ...violation,
          // Add student context
          studentName: item.peserta?.name || item.namaSiswa || "-",
          studentNIM: item.peserta?.username || item.nisn || "-",
          idPeserta: item.idPeserta || "-",
          sessionId: item.sessionId || "-",
          ujianNama: item.ujian?.namaUjian || "-",
          mapelNama: item.ujian?.mapel?.name || "-",
        });
      });
    });

    return allViolations;
  };

  // Helper: Get violations count for a record (dari metadata dengan deduplication)
  const getViolationsCount = (record) => {
    // Add null check
    if (!record) return 0;

    // Ambil violations dari metadata langsung
    const violations = record.metadata?.violations || [];

    // Deduplicate violations
    const uniqueViolations = deduplicateViolations(violations);

    return uniqueViolations.length;
  };

  // Show export modal
  const showExportModalHandler = () => {
    if (filteredData.length === 0) {
      message.warning("Tidak ada data untuk diekspor");
      return;
    }
    setShowExportModal(true);
  };

  // Enhanced Export to Excel with detailed answers (dengan filter ujian)
  const exportToExcel = (selectedUjian = null) => {
    setLoadingExport(true);

    try {
      let dataToExport = filteredData;

      // Filter berdasarkan ujian yang dipilih jika ada
      if (selectedUjian) {
        dataToExport = filteredData.filter((item) => {
          const idUjian =
            item.idUjian ||
            item.ujianId ||
            item.ujian?.idUjian ||
            item.ujian_id;
          return idUjian === selectedUjian.idUjian;
        });

        if (dataToExport.length === 0) {
          message.warning(
            `Tidak ada data untuk ujian "${selectedUjian.namaUjian}"`
          );
          return;
        }

        console.log(
          `Exporting ${dataToExport.length} records for ujian: ${selectedUjian.namaUjian}`
        );
      } else {
        console.log(`Exporting all ${dataToExport.length} records`);
      }

      // Merge ujian detail ke setiap item
      const mergedData = dataToExport.map((item) => {
        const idUjian =
          item.idUjian || item.ujianId || item.ujian?.idUjian || item.ujian_id;
        const found = ujianList.find((u) => u.idUjian === idUjian);
        return found ? { ...item, ujian: found } : item;
      });

      // Helper: Get violations list (dari metadata dengan deduplication)
      const getViolationsList = (record) => {
        // Ambil violations dari metadata langsung
        const violations = record.metadata?.violations || [];

        // Deduplicate violations
        const uniqueViolations = deduplicateViolations(violations);

        console.log("=== VIOLATIONS FROM METADATA (DEDUPLICATED) ===");
        console.log("Record ID:", record.idHasilUjian);
        console.log("Original violations:", violations.length);
        console.log("Unique violations:", uniqueViolations.length);

        if (uniqueViolations.length === 0) {
          return "";
        }

        return uniqueViolations
          .map(
            (v) =>
              `[waktu deteksi: ${
                v.detectedAt || v.timestamp || v.detectionTime
                  ? dayjs(
                      v.detectedAt || v.timestamp || v.detectionTime
                    ).format("DD/MM/YYYY HH:mm:ss")
                  : "-"
              }, type : ${
                v.typeViolation || v.violationType || v.type || "UNKNOWN"
              }]`
          )
          .join(", ");
      };

      // Helper: Format jawaban untuk export
      const formatJawaban = (jawaban, bankSoal = null) => {
        if (Array.isArray(jawaban)) {
          return jawaban.join(", ");
        } else if (typeof jawaban === "object" && jawaban !== null) {
          // Khusus untuk soal COCOK, format dengan nama pasangan
          if (bankSoal?.jenisSoal === "COCOK" && bankSoal?.pasangan) {
            const pasanganMap = {};

            // Buat mapping dari pasangan
            Object.entries(bankSoal.pasangan).forEach(([key, value]) => {
              if (key.includes("_kiri")) {
                const index = key.replace("_kiri", "");
                pasanganMap[index] = { kiri: value };
              } else if (key.includes("_kanan")) {
                const index = key.replace("_kanan", "");
                if (pasanganMap[index]) {
                  pasanganMap[index].kanan = value;
                } else {
                  pasanganMap[index] = { kanan: value };
                }
              }
            });

            // Format jawaban dengan nama pasangan
            return Object.entries(jawaban)
              .map(([kiriIndex, kananIndex]) => {
                const kiri =
                  pasanganMap[kiriIndex]?.kiri || `Item ${kiriIndex}`;
                const kanan =
                  pasanganMap[kananIndex]?.kanan || `Item ${kananIndex}`;
                return `${kiri} = ${kanan}`;
              })
              .join(", ");
          }
          // Fallback untuk object lainnya
          return JSON.stringify(jawaban);
        } else {
          return String(jawaban || "-");
        }
      };

      // Create workbook with multiple sheets
      const wb = XLSX.utils.book_new();

      // Sheet 1: Summary Report
      const summaryData = mergedData.map((item, index) => ({
        No: index + 1,
        "ID Hasil": item.idHasilUjian || "-",
        "Session ID": item.sessionId || "-",
        NISN: item.peserta?.username || item.username || item.nisn || "-",
        "Nama Siswa": item.peserta?.name || item.namaSiswa || item.nama || "-",
        "ID Peserta": item.idPeserta || "-",
        Kelas:
          item.ujian?.kelas?.namaKelas || item.namaKelas || "Tidak Diketahui",
        Ujian: item.ujian?.namaUjian || item.namaUjian || "-",
        "Mata Pelajaran":
          item.ujian?.mapel?.name || item.mapelNama || item.mapel,
        Semester:
          item.ujian?.semester?.namaSemester || item.semesterNama || "-",
        Sekolah: item.namaSekolah || item.school || "Tidak Diketahui",
        "Percobaan Ke": item.attemptNumber || 1,
        "Status Pengerjaan": item.statusPengerjaan || "-",
        "Auto Submit": item.isAutoSubmit ? "Ya" : "Tidak",
        "Waktu Mulai": item.waktuMulai
          ? dayjs(item.waktuMulai).format("DD/MM/YYYY HH:mm:ss")
          : "-",
        "Waktu Selesai": item.waktuSelesai
          ? dayjs(item.waktuSelesai).format("DD/MM/YYYY HH:mm:ss")
          : "-",
        "Durasi Ujian": item.ujian?.durasiMenit
          ? `${item.ujian.durasiMenit} menit`
          : "-",
        "Durasi Pengerjaan": item.durasiPengerjaan
          ? `${Math.floor(item.durasiPengerjaan / 60)}m ${
              item.durasiPengerjaan % 60
            }s`
          : "-",
        "Sisa Waktu": (() => {
          const durasiUjianMenit = item.ujian?.durasiMenit || 0;
          const durasiPengerjaanDetik = item.durasiPengerjaan || 0;

          if (durasiUjianMenit > 0 && durasiPengerjaanDetik > 0) {
            const durasiUjianDetik = durasiUjianMenit * 60;
            const sisaWaktuDetik = durasiUjianDetik - durasiPengerjaanDetik;

            if (sisaWaktuDetik > 0) {
              const menit = Math.floor(sisaWaktuDetik / 60);
              const detik = sisaWaktuDetik % 60;
              return `${menit}m ${detik}s`;
            } else {
              return "0m 0s (Melebihi batas)";
            }
          }

          return "-";
        })(),
        "Total Skor": item.totalSkor || 0,
        "Skor Maksimal": item.skorMaksimal || 100,
        Persentase: item.persentase
          ? `${parseFloat(item.persentase).toFixed(2)}%`
          : "0%",
        "Total Soal": item.totalSoal || item.metadata?.totalQuestions || "-",
        "Soal Terjawab":
          item.metadata?.answeredQuestions ||
          Object.keys(item.jawabanPeserta || {}).length ||
          0,
        "Soal Benar": item.jumlahBenar || 0,
        "Soal Salah": item.jumlahSalah || 0,
        "Soal Kosong": item.jumlahKosong || 0,
        "Jumlah Pelanggaran": getViolationsCount(item),
        "Detail Pelanggaran": getViolationsList(item),
      }));

      const summaryWs = XLSX.utils.json_to_sheet(summaryData);
      XLSX.utils.book_append_sheet(wb, summaryWs, "Ringkasan Hasil");

      // Sheet 2: Detailed Answers with Question Details (ENHANCED)
      const detailAnswers = [];
      mergedData.forEach((item) => {
        // Get all possible questions (answered + unanswered) like in modal
        const jawabanData = item.jawabanPeserta || {};
        const bankSoalFromUjian = item.ujian?.bankSoalList || [];

        // Prioritize ujian's bankSoalList to ensure all questions are included
        let allSoalIds = [];
        if (bankSoalFromUjian.length > 0) {
          // Use ujian order as primary
          allSoalIds = bankSoalFromUjian.map((soal) => soal.idBankSoal);
          // Add any answered questions not in ujian list
          Object.keys(jawabanData).forEach((id) => {
            if (!allSoalIds.includes(id)) {
              allSoalIds.push(id);
            }
          });
        } else {
          // Fallback to answered questions only
          allSoalIds = Object.keys(jawabanData);
        }

        allSoalIds.forEach((soalId, questionIndex) => {
          const jawaban = jawabanData[soalId];
          const bankSoal =
            bankSoalMap[soalId] ||
            bankSoalFromUjian.find((s) => s.idBankSoal === soalId);

          // Enhanced formatting functions
          const formatOpsiJawaban = (opsi) => {
            if (!opsi || typeof opsi !== "object") return "-";
            return Object.entries(opsi)
              .map(([key, value]) => `${key}. ${value}`)
              .join(" | ");
          };

          const formatPasangan = (pasangan) => {
            if (!pasangan || typeof pasangan !== "object") return "-";
            const kiri = Object.entries(pasangan).filter(([k]) =>
              k.includes("_kiri")
            );
            const kanan = Object.entries(pasangan).filter(([k]) =>
              k.includes("_kanan")
            );
            return `KIRI: ${kiri.map(([k, v]) => v).join(", ")} | KANAN: ${kanan
              .map(([k, v]) => v)
              .join(", ")}`;
          };

          // Format jawaban siswa
          let jawabanSiswaFormatted = "";
          if (!jawaban) {
            jawabanSiswaFormatted = "TIDAK DIJAWAB";
          } else if (Array.isArray(jawaban)) {
            jawabanSiswaFormatted = jawaban.join(", ");
          } else if (typeof jawaban === "object" && jawaban !== null) {
            // Khusus untuk soal COCOK, format dengan nama pasangan
            if (bankSoal?.jenisSoal === "COCOK" && bankSoal?.pasangan) {
              const pasanganMap = {};

              // Buat mapping dari pasangan
              Object.entries(bankSoal.pasangan).forEach(([key, value]) => {
                if (key.includes("_kiri")) {
                  const index = key.replace("_kiri", "");
                  pasanganMap[index] = { kiri: value };
                } else if (key.includes("_kanan")) {
                  const index = key.replace("_kanan", "");
                  if (pasanganMap[index]) {
                    pasanganMap[index].kanan = value;
                  } else {
                    pasanganMap[index] = { kanan: value };
                  }
                }
              });

              // Format jawaban dengan nama pasangan
              jawabanSiswaFormatted = Object.entries(jawaban)
                .map(([kiriIndex, kananIndex]) => {
                  const kiri =
                    pasanganMap[kiriIndex]?.kiri || `Item ${kiriIndex}`;
                  const kanan =
                    pasanganMap[kananIndex]?.kanan || `Item ${kananIndex}`;
                  return `${kiri} = ${kanan}`;
                })
                .join(", ");
            } else {
              // Fallback untuk object lainnya
              jawabanSiswaFormatted = Object.entries(jawaban)
                .map(([k, v]) => `${k}: ${v}`)
                .join(", ");
            }
          } else {
            jawabanSiswaFormatted = String(jawaban);
          }

          // Format jawaban benar
          let jawabanBenarFormatted = "";
          if (bankSoal?.jawabanBenar) {
            if (Array.isArray(bankSoal.jawabanBenar)) {
              jawabanBenarFormatted = bankSoal.jawabanBenar.join(", ");
            } else {
              jawabanBenarFormatted = String(bankSoal.jawabanBenar);
            }
          } else {
            jawabanBenarFormatted = "-";
          }

          // Determine status
          let status = "TIDAK DIJAWAB";
          if (jawaban) {
            const isCorrect = item.skorPerSoal?.[soalId] > 0;
            status = isCorrect ? "BENAR" : "SALAH";
          }

          detailAnswers.push({
            "Nama Siswa":
              questionIndex === 0
                ? item.peserta?.name || item.namaSiswa || "-"
                : "",
            NISN:
              questionIndex === 0
                ? item.peserta?.username || item.nisn || "-"
                : "",
            "Session ID": questionIndex === 0 ? item.sessionId || "-" : "",
            Ujian: questionIndex === 0 ? item.ujian?.namaUjian || "-" : "",
            "No Soal": questionIndex + 1,
            "ID Soal": soalId,
            "Jenis Soal": bankSoal?.jenisSoal || "-",
            Pertanyaan: bankSoal?.pertanyaan || "-",
            "Pilihan Jawaban PG/MULTI":
              bankSoal?.jenisSoal === "PG" || bankSoal?.jenisSoal === "MULTI"
                ? formatOpsiJawaban(bankSoal?.opsi)
                : "-",
            "Pasangan COCOK":
              bankSoal?.jenisSoal === "COCOK"
                ? formatPasangan(bankSoal?.pasangan)
                : "-",
            "Toleransi Typo": bankSoal?.toleransiTypo || "-",
            "Jawaban Benar": jawabanBenarFormatted,
            "Jawaban Siswa": jawabanSiswaFormatted,
            Status: status,
            "Skor Diperoleh": item.skorPerSoal?.[soalId] || 0,
            "Bobot Soal": bankSoal?.bobot || "-",
            "Waktu Mulai Ujian":
              questionIndex === 0
                ? item.waktuMulai
                  ? dayjs(item.waktuMulai).format("DD/MM/YYYY HH:mm:ss")
                  : "-"
                : "",
          });
        });
      });

      if (detailAnswers.length > 0) {
        const detailWs = XLSX.utils.json_to_sheet(detailAnswers);

        // Add merge cells for students with same name and NISN
        const merges = [];
        let currentStudentKey = null;
        let groupStartRow = 1; // Starting from row 2 (after header at row 0)
        let groupRowCount = 0;

        detailAnswers.forEach((row, index) => {
          const namaSiswa = row["Nama Siswa"];
          const nisn = row["NISN"];

          // Only process rows that have actual student data (not empty rows)
          if (namaSiswa && nisn) {
            const studentKey = `${namaSiswa}_${nisn}`;

            if (currentStudentKey !== studentKey) {
              // Finish previous group if it had multiple rows
              if (groupRowCount > 1) {
                // Add merges for columns that should be merged
                [0, 1, 2, 3, 16].forEach((columnIndex) => {
                  // Nama, NISN, Session, Ujian, Waktu
                  merges.push({
                    s: { r: groupStartRow, c: columnIndex },
                    e: { r: groupStartRow + groupRowCount - 1, c: columnIndex },
                  });
                });
              }

              // Start new group
              currentStudentKey = studentKey;
              groupStartRow = index + 1; // +1 because header is at row 0
              groupRowCount = 1;
            } else {
              groupRowCount++;
            }
          } else {
            // This is a continuation row for the same student
            groupRowCount++;
          }
        });

        // Handle the last group
        if (groupRowCount > 1) {
          [0, 1, 2, 3, 16].forEach((columnIndex) => {
            merges.push({
              s: { r: groupStartRow, c: columnIndex },
              e: { r: groupStartRow + groupRowCount - 1, c: columnIndex },
            });
          });
        }

        // Apply merges to worksheet
        if (merges.length > 0) {
          detailWs["!merges"] = merges;
        }

        XLSX.utils.book_append_sheet(wb, detailWs, "Detail Jawaban");
      }

      // Sheet 3: Violations Detail (dari metadata)
      const violationDetails = [];

      // Ambil violations dari metadata setiap record dengan deduplication
      mergedData.forEach((item) => {
        // Ambil violations dari metadata langsung
        const violationsFromMetadata = item.metadata?.violations || [];

        // Deduplicate violations
        const studentViolations = deduplicateViolations(violationsFromMetadata);

        console.log(
          `Excel Export - Student ${
            item.peserta?.name || item.namaSiswa
          }: Found ${violationsFromMetadata.length} -> ${
            studentViolations.length
          } violations (after deduplication)`
        );

        // Add violations to details
        studentViolations.forEach((violation, violationIndex) => {
          console.log(
            `Processing violation ${violationIndex + 1} for ${
              item.peserta?.name
            }:`,
            violation
          );

          violationDetails.push({
            "Nama Siswa": item.peserta?.name || item.namaSiswa || "-",
            NISN: item.peserta?.username || item.nisn || "-",
            "ID Peserta": item.idPeserta || "-",
            "Session ID": item.sessionId || "-",
            Ujian: item.ujian?.namaUjian || "-",
            "Mata Pelajaran": item.ujian?.mapel?.name || item.mapelNama || "-",
            "Waktu Ujian": item.waktuMulai
              ? dayjs(item.waktuMulai).format("DD/MM/YYYY HH:mm:ss")
              : "-",
            "No Pelanggaran": violationIndex + 1,
            "ID Detection": violation.idDetection || "-",
            "Jenis Pelanggaran":
              violation.typeViolation ||
              violation.violationType ||
              violation.type ||
              "Tidak Diketahui",
            "Tingkat Keparahan":
              violation.severity || violation.level || "Tidak Diketahui",
            "Waktu Deteksi":
              violation.detectedAt ||
              violation.detectionTime ||
              violation.timestamp
                ? dayjs(
                    violation.detectedAt ||
                      violation.detectionTime ||
                      violation.timestamp
                  ).format("DD/MM/YYYY HH:mm:ss")
                : "Tidak Tersedia",
            "Detail Evidence": violation.evidence
              ? JSON.stringify(violation.evidence).substring(0, 200) + "..."
              : "Tidak Ada",
            Resolved: violation.resolved ? "Ya" : "Tidak",
            "Action Taken": violation.actionTaken || "Tidak Ada",
          });
        });

        // If no violations found but student has violation count, add placeholder
        if (studentViolations.length === 0 && getViolationsCount(item) > 0) {
          violationDetails.push({
            "Nama Siswa": item.peserta?.name || item.namaSiswa || "-",
            NISN: item.peserta?.username || item.nisn || "-",
            "ID Peserta": item.idPeserta || "-",
            "Session ID": item.sessionId || "-",
            Ujian: item.ujian?.namaUjian || "-",
            "Mata Pelajaran":
              item.ujian?.mapel?.name || item.mapelNama || item.mapel || "-",
            "Waktu Ujian": item.waktuMulai
              ? dayjs(item.waktuMulai).format("DD/MM/YYYY HH:mm:ss")
              : "-",
            "No Pelanggaran": 1,
            "ID Detection": "-",
            "Jenis Pelanggaran":
              "Pelanggaran Terdeteksi (Detail Tidak Tersedia)",
            "Tingkat Keparahan": "Tidak Diketahui",
            "Jumlah Pelanggaran": getViolationsCount(item),
            "Waktu Deteksi": "Tidak Tersedia",
            "Detail Evidence":
              "Data pelanggaran ada tapi detail tidak dapat dimuat",
            Resolved: "Tidak Diketahui",
            "Action Taken": "Tidak Ada",
          });
        }
      });

      if (violationDetails.length > 0) {
        const violationWs = XLSX.utils.json_to_sheet(violationDetails);
        XLSX.utils.book_append_sheet(wb, violationWs, "Detail Pelanggaran");
      }

      // Sheet 4: Master Data Soal (NEW)
      const masterSoalData = [];
      const processedUjian = new Set();

      mergedData.forEach((item) => {
        const ujianId = item.ujian?.idUjian;
        if (
          ujianId &&
          !processedUjian.has(ujianId) &&
          item.ujian?.bankSoalList
        ) {
          processedUjian.add(ujianId);

          item.ujian.bankSoalList.forEach((soal, index) => {
            const formatOpsi = (opsi) => {
              if (!opsi || typeof opsi !== "object") return "-";
              return Object.entries(opsi)
                .map(([key, value]) => `${key}. ${value}`)
                .join(" | ");
            };

            const formatPasangan = (pasangan) => {
              if (!pasangan || typeof pasangan !== "object") return "-";
              const kiri = Object.entries(pasangan).filter(([k]) =>
                k.includes("_kiri")
              );
              const kanan = Object.entries(pasangan).filter(([k]) =>
                k.includes("_kanan")
              );
              return `KIRI: ${kiri
                .map(([k, v]) => v)
                .join(", ")} | KANAN: ${kanan.map(([k, v]) => v).join(", ")}`;
            };

            masterSoalData.push({
              Ujian: item.ujian?.namaUjian || "-",
              "Mata Pelajaran":
                item.ujian?.mapel?.name || item.mapelNama || item.mapel || "-",
              Kelas: item.ujian?.kelas?.namaKelas || "-",
              Semester: item.ujian?.semester?.namaSemester || "-",
              "No Soal": index + 1,
              "ID Bank Soal": soal.idBankSoal || "-",
              "Jenis Soal": soal.jenisSoal || "-",
              Pertanyaan: soal.pertanyaan || "-",
              "Opsi PG/MULTI":
                soal.jenisSoal === "PG" || soal.jenisSoal === "MULTI"
                  ? formatOpsi(soal.opsi)
                  : "-",
              "Pasangan COCOK":
                soal.jenisSoal === "COCOK"
                  ? formatPasangan(soal.pasangan)
                  : "-",
              "Jawaban Benar": Array.isArray(soal.jawabanBenar)
                ? soal.jawabanBenar.join(", ")
                : soal.jawabanBenar || "-",
              Bobot: soal.bobot || "-",
              "Toleransi Typo": soal.toleransiTypo || "-",
            });
          });
        }
      });

      if (masterSoalData.length > 0) {
        const masterSoalWs = XLSX.utils.json_to_sheet(masterSoalData);
        XLSX.utils.book_append_sheet(wb, masterSoalWs, "Master Data Soal");
      }

      // Auto-size all sheets
      [
        summaryWs,
        detailAnswers.length > 0
          ? XLSX.utils.json_to_sheet(detailAnswers)
          : null,
        violationDetails.length > 0
          ? XLSX.utils.json_to_sheet(violationDetails)
          : null,
        masterSoalData.length > 0
          ? XLSX.utils.json_to_sheet(masterSoalData)
          : null,
      ]
        .filter(Boolean)
        .forEach((ws) => {
          if (ws && ws["!ref"]) {
            const range = XLSX.utils.decode_range(ws["!ref"]);
            const cols = [];
            for (let C = range.s.c; C <= range.e.c; ++C) {
              let maxWidth = 10;
              for (let R = range.s.r; R <= range.e.r; ++R) {
                const cellAddress = XLSX.utils.encode_cell({ r: R, c: C });
                const cell = ws[cellAddress];
                if (cell && cell.v) {
                  const cellLength = String(cell.v).length;
                  maxWidth = Math.max(maxWidth, cellLength);
                }
              }
              cols[C] = { width: Math.min(maxWidth + 2, 50) };
            }
            ws["!cols"] = cols;
          }
        });

      // Generate filename dengan info ujian jika ada filter
      const ujianInfo = selectedUjian
        ? `_${selectedUjian.namaUjian.replace(/[^\w\s]/gi, "")}`
        : "";
      const fileName = `Report_Lengkap_Nilai_Siswa${ujianInfo}_${dayjs().format(
        "YYYY-MM-DD_HH-mm"
      )}.xlsx`;

      XLSX.writeFile(wb, fileName);

      const successMsg = selectedUjian
        ? `Data ujian "${selectedUjian.namaUjian}" berhasil diekspor dengan ${wb.SheetNames.length} sheet`
        : `Data berhasil diekspor ke Excel dengan ${wb.SheetNames.length} sheet`;

      message.success(successMsg);

      // Tutup modal export jika berhasil
      setShowExportModal(false);
      setSelectedUjianForExport(null);
    } catch (error) {
      console.error("Error exporting to Excel:", error);
      message.error("Terjadi kesalahan saat mengekspor data ke Excel");
    } finally {
      setLoadingExport(false);
    }
  };

  // Handle export dengan ujian yang dipilih
  const handleExportWithSelectedUjian = () => {
    if (!selectedUjianForExport) {
      message.warning("Silakan pilih ujian terlebih dahulu");
      return;
    }
    exportToExcel(selectedUjianForExport);
  };

  // Export semua data (tanpa filter ujian)
  const handleExportAllData = () => {
    exportToExcel(null);
  };

  // Reset filters for the main report table
  const resetFilters = () => {
    setSelectedUjian(null);
    setSelectedKelas(null);
    setDateRange([]);
    setSearchText("");
  };

  // Table columns for the main report table
  const columns = [
    {
      title: "No",
      key: "no",
      width: 60,
      render: (_, __, index) => index + 1,
    },
    // ...existing code...
    {
      title: "Siswa",
      key: "siswa",
      render: (_, record) => {
        return (
          <div>
            <div style={{ fontWeight: "bold" }}>
              {record.peserta?.name ||
                record.namaSiswa ||
                record.nama ||
                "Nama tidak tersedia"}
            </div>
          </div>
        );
      },
    },
    {
      title: "NISN",
      key: "nisn",
      render: (_, record) => {
        return (
          <div>
            <Text Strong>
              {record.peserta?.username ||
                record.nisn ||
                record.username ||
                record.idPeserta ||
                "NISN tidak tersedia"}
            </Text>
          </div>
        );
      },
    },

    {
      title: "Ujian",
      key: "ujian",
      render: (_, record) => {
        return (
          <div>
            <Text strong>
              {record.ujian?.namaUjian || record.namaUjian || "Tidak tersedia"}
            </Text>
          </div>
        );
      },
    },
    {
      title: "Waktu Ujian",
      key: "waktu",
      render: (_, record) => (
        <div>
          <div>
            <Text type="secondary">Mulai: </Text>
            <Text>
              {record.waktuMulai
                ? dayjs(record.waktuMulai).format("DD/MM/YY HH:mm")
                : "-"}
            </Text>
          </div>
          <div>
            <Text type="secondary">Selesai: </Text>
            <Text>
              {record.waktuSelesai
                ? dayjs(record.waktuSelesai).format("DD/MM/YY HH:mm")
                : "-"}
            </Text>
          </div>
          {record.durasi && (
            <Text type="secondary">Durasi: {record.durasi} menit</Text>
          )}
        </div>
      ),
    },
    {
      title: "Nilai",
      key: "nilai",
      align: "center",
      sorter: (a, b) =>
        parseFloat(a.nilai || a.skor || 0) - parseFloat(b.nilai || b.skor || 0),
      render: (_, record) => {
        const ujian = record.ujian || record;
        const showNilai = ujian.tampilkanNilai !== false; // default true if no setting

        if (!showNilai) {
          return (
            <div style={{ textAlign: "center" }}>
              <Text type="secondary">Nilai Disembunyikan</Text>
            </div>
          );
        }

        const nilai = parseFloat(
          record.nilai || record.skor || record.persentase || 0
        );
        const nilaiMin = parseFloat(
          ujian.minPassingScore || ujian.nilaiMinimal || 0
        );

        return (
          <div>
            <div style={{ fontSize: "20px", fontWeight: "bold" }}>
              {nilai.toFixed(0)}
            </div>
            <div style={{ fontSize: "12px", color: "#666" }}>
              Min: {nilaiMin.toFixed(0)}
            </div>
            {nilai > 0 && (
              <Progress
                percent={(nilai / 100) * 100}
                size="small"
                status={nilai >= nilaiMin ? "success" : "exception"}
                showInfo={false}
              />
            )}
          </div>
        );
      },
    },
    // {
    //   title: "Status",
    //   key: "status",
    //   align: "center",
    //   render: (_, record) => {
    //     const { color, icon, text } = getStatusDisplay(record);
    //     return (
    //       <Tag color={color} icon={icon}>
    //         {text}
    //       </Tag>
    //     );
    //   },
    // },
    {
      title: "Pelanggaran",
      key: "violationCount",
      align: "center",
      render: (_, record) => {
        // Use the new helper function to get accurate violation count
        const violationCount = getViolationsCount(record);

        if (violationCount > 0) {
          return (
            <Badge count={violationCount} overflowCount={99}>
              <Button
                type="text"
                icon={<WarningOutlined />}
                danger
                size="small"
                onClick={() => showViolationsModal(record)}
              >
                Lihat
              </Button>
            </Badge>
          );
        }

        return <Tag color="success">Bersih</Tag>;
      },
    },
    {
      title: "Aksi",
      key: "action",
      width: 100,
      render: (_, record) => (
        <Space>
          <Tooltip title="Lihat Detail">
            <Button
              type="primary"
              icon={<EyeOutlined />}
              size="small"
              onClick={() => showDetail(record)}
            >
              Detail
            </Button>
          </Tooltip>
          <Popconfirm
            title="Hapus hasil ujian ini?"
            onConfirm={() => handleDeleteHasilUjian(record)}
            okText="Ya, Hapus"
            cancelText="Batal"
          >
            <Tooltip title="Hapus">
              <Button danger size="small" icon={<DeleteOutlined />} />
            </Tooltip>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div className="app-container">
      <TypingCard title="Report Nilai Siswa" source="" />

      {/* Statistics Cards */}
      <Row gutter={16} style={{ marginBottom: 24 }}>
        <Col span={6}>
          <Card>
            <Statistic
              title="Total Siswa"
              value={statistics.totalSiswa}
              prefix={<UserOutlined />}
              valueStyle={{ color: "#1890ff" }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="Siswa Lulus"
              value={statistics.siswaLulus}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: "#52c41a" }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="Siswa Tidak Lulus"
              value={statistics.siswaTidakLulus}
              prefix={<CloseCircleOutlined />}
              valueStyle={{ color: "#ff4d4f" }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="Rata-rata Nilai"
              value={statistics.rataRataNilai}
              prefix={<TrophyOutlined />}
              valueStyle={{ color: "#faad14" }}
              precision={1}
            />
          </Card>
        </Col>
      </Row>

      {/* Main Card */}
      <Card
        title={
          <Space>
            <TrophyOutlined />
            <span>Daftar Nilai Siswa</span>
          </Space>
        }
        extra={
          <Space>
            {/* --- NEW BUTTON TO SHOW ALL VIOLATIONS --- */}
            <Button
              icon={<DatabaseOutlined />}
              onClick={() => setShowAllViolationsModal(true)}
              loading={loadingAllViolations}
            >
              Lihat Semua Pelanggaran
            </Button>
            {/* ------------------------------------------- */}
            <Button
              type="primary"
              icon={<FileExcelOutlined />}
              onClick={showExportModalHandler}
              disabled={false}
            >
              Export Excel
            </Button>
            <Button
              icon={<ReloadOutlined />}
              onClick={fetchReportData}
              loading={loading}
            >
              Refresh
            </Button>
          </Space>
        }
      >
        {/* Filters */}
        <Card size="small" style={{ marginBottom: "16px" }}>
          <Row gutter={16}>
            <Col span={6}>
              <Select
                placeholder="Pilih Ujian"
                value={selectedUjian}
                onChange={setSelectedUjian}
                allowClear
                style={{ width: "100%" }}
              >
                {ujianList.map((ujian) => (
                  <Option key={`ujian-${ujian.idUjian}`} value={ujian.idUjian}>
                    {ujian.namaUjian}
                  </Option>
                ))}
              </Select>
            </Col>
            <Col span={6}>
              <Select
                placeholder="Pilih Kelas"
                value={selectedKelas}
                onChange={setSelectedKelas}
                allowClear
                style={{ width: "100%" }}
              >
                {kelasList.map((kelas, index) => (
                  <Option
                    key={kelas.id ? `kelas-${kelas.id}` : `kelas-${index}`}
                    value={kelas.id || `temp-${index}`}
                  >
                    {kelas.name || `Kelas ${index + 1}`}
                  </Option>
                ))}
              </Select>
            </Col>
            {/* <Col span={6}>
              <RangePicker
                value={dateRange}
                onChange={setDateRange}
                format="DD/MM/YYYY"
                placeholder={["Tanggal Mulai", "Tanggal Akhir"]}
                style={{ width: "100%" }}
              />
            </Col>
            <Col span={6}>
              <Input
                placeholder="Cari siswa, NISN, ujian..."
                prefix={<SearchOutlined />}
                value={searchText}
                onChange={(e) => setSearchText(e.target.value)}
                allowClear
              />
            </Col> */}
          </Row>
          <Row style={{ marginTop: "16px" }}>
            <Col>
              <Button onClick={resetFilters}>Reset Filter</Button>
            </Col>
          </Row>
        </Card>

        {/* Table for main report data */}
        <Spin spinning={loading}>
          <Table
            columns={columns}
            dataSource={filteredData}
            rowKey={(record) =>
              `${record.siswaId || record.idPeserta}-${
                record.ujianId || record.idUjian
              }-${record.waktuMulai || Math.random()}`
            }
            pagination={{
              total: filteredData.length,
              showSizeChanger: true,
              showQuickJumper: true,
              showTotal: (total, range) =>
                `${range[0]}-${range[1]} dari ${total} data`,
            }}
            size="small"
            locale={{
              emptyText: "Belum ada data hasil ujian",
            }}
          />
        </Spin>
      </Card>

      {/* Detail Modal (for individual student exam results) */}
      <Modal
        title={
          <Space>
            <Avatar icon={<UserOutlined />} />
            <span>Detail Hasil Ujian Siswa</span>
          </Space>
        }
        open={detailModal.visible}
        onCancel={() => setDetailModal({ visible: false, data: null })}
        footer={[
          <Button
            key="violations"
            icon={<WarningOutlined />}
            onClick={() => showViolationsModal(detailModal.data)}
            disabled={getViolationsCount(detailModal.data || {}) === 0}
          >
            Lihat Pelanggaran ({getViolationsCount(detailModal.data || {})})
          </Button>,
          <Button
            key="close"
            onClick={() => setDetailModal({ visible: false, data: null })}
          >
            Tutup
          </Button>,
        ]}
        width={1200}
      >
        {detailModal.data && (
          <div>
            {/* Nama Ujian - Tengah, Besar, Bold */}
            <div
              style={{
                textAlign: "center",
                marginBottom: 20,
                padding: "10px 0",
                borderBottom: "2px solid #f0f0f0",
              }}
            >
              <Typography.Title
                level={2}
                style={{ margin: 0, fontWeight: "bold", color: "#1890ff" }}
              >
                {detailModal.data.ujian?.namaUjian ||
                  detailModal.data.namaUjian ||
                  "Nama Ujian Tidak Tersedia"}
              </Typography.Title>
            </div>

            {/* Header Information */}
            <Card size="small" style={{ marginBottom: 16 }}>
              <Row gutter={16}>
                <Col span={12}>
                  <Descriptions size="small" column={1}>
                    <Descriptions.Item label="Nama Siswa">
                      <Text strong>
                        {detailModal.data.peserta?.name ||
                          detailModal.data.namaSiswa ||
                          detailModal.data.nama ||
                          "-"}
                      </Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Username/NISN">
                      {detailModal.data.peserta?.username ||
                        detailModal.data.nisn ||
                        detailModal.data.username ||
                        "-"}
                    </Descriptions.Item>
                    <Descriptions.Item label="ID Peserta">
                      {detailModal.data.idPeserta || "-"}
                    </Descriptions.Item>
                  </Descriptions>
                </Col>
                <Col span={12}>
                  <Descriptions size="small" column={1}>
                    <Descriptions.Item label="Session ID">
                      <Text code>{detailModal.data.sessionId || "-"}</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Sekolah">
                      {detailModal.data.namaSekolah ||
                        detailModal.data.school ||
                        "Tidak Diketahui"}
                    </Descriptions.Item>
                    <Descriptions.Item label="Durasi Ujian">
                      {detailModal.data.ujian?.durasiMenit
                        ? `${detailModal.data.ujian.durasiMenit} menit`
                        : "-"}
                    </Descriptions.Item>
                  </Descriptions>
                </Col>
              </Row>
            </Card>

            {/* Main Information */}
            <Descriptions
              bordered
              column={2}
              size="small"
              style={{ marginBottom: 16 }}
            >
              <Descriptions.Item label="Mata Pelajaran">
                {detailModal.data.mapelNama ||
                  detailModal.data.ujian?.mapel?.name ||
                  "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Kelas">
                {detailModal.data.ujian?.kelas?.namaKelas ||
                  detailModal.data.namaKelas ||
                  "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Semester">
                {detailModal.data.semesterNama ||
                  detailModal.data.ujian?.semester?.namaSemester ||
                  "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Percobaan Ke">
                {detailModal.data.attemptNumber || 1}
              </Descriptions.Item>
              <Descriptions.Item label="Waktu Mulai">
                {detailModal.data.waktuMulai
                  ? dayjs(detailModal.data.waktuMulai).format(
                      "DD/MM/YYYY HH:mm:ss"
                    )
                  : "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Waktu Selesai">
                {detailModal.data.waktuSelesai
                  ? dayjs(detailModal.data.waktuSelesai).format(
                      "DD/MM/YYYY HH:mm:ss"
                    )
                  : "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Durasi Pengerjaan">
                {detailModal.data.durasiPengerjaan
                  ? `${Math.floor(detailModal.data.durasiPengerjaan / 60)}m ${
                      detailModal.data.durasiPengerjaan % 60
                    }s`
                  : "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Sisa Waktu">
                {(() => {
                  // Hitung sisa waktu berdasarkan durasi ujian dari API
                  const durasiUjianMenit =
                    detailModal.data.ujian?.durasiMenit || 0;
                  const durasiPengerjaanDetik =
                    detailModal.data.durasiPengerjaan || 0;

                  if (durasiUjianMenit > 0 && durasiPengerjaanDetik > 0) {
                    // Konversi durasi ujian ke detik
                    const durasiUjianDetik = durasiUjianMenit * 60;
                    const sisaWaktuDetik =
                      durasiUjianDetik - durasiPengerjaanDetik;

                    if (sisaWaktuDetik > 0) {
                      const menit = Math.floor(sisaWaktuDetik / 60);
                      const detik = sisaWaktuDetik % 60;
                      return `${menit}m ${detik}s`;
                    } else {
                      return "0m 0s (Melebihi batas waktu)";
                    }
                  }

                  return "-";
                })()}
              </Descriptions.Item>
              <Descriptions.Item label="Status Pengerjaan">
                <Tag
                  color={
                    detailModal.data.statusPengerjaan === "SELESAI"
                      ? "green"
                      : "orange"
                  }
                >
                  {detailModal.data.statusPengerjaan || "TIDAK DIKETAHUI"}
                </Tag>
              </Descriptions.Item>
              <Descriptions.Item label="Auto Submit">
                <Tag color={detailModal.data.isAutoSubmit ? "red" : "green"}>
                  {detailModal.data.isAutoSubmit ? "Ya" : "Tidak"}
                </Tag>
              </Descriptions.Item>

              {/* Display score only if allowed */}
              {detailModal.data.ujian?.tampilkanNilai !== false && (
                <>
                  <Descriptions.Item label="Total Skor">
                    <Text strong style={{ fontSize: "16px" }}>
                      {detailModal.data.totalSkor || 0} /{" "}
                      {detailModal.data.skorMaksimal || 100}
                    </Text>
                  </Descriptions.Item>
                  <Descriptions.Item label="Persentase">
                    <Text strong style={{ fontSize: "16px" }}>
                      {parseFloat(detailModal.data.persentase || 0).toFixed(1)}%
                    </Text>
                  </Descriptions.Item>

                  <Descriptions.Item label="Status Kelulusan">
                    <Tag
                      color={detailModal.data.lulus ? "green" : "red"}
                      icon={
                        detailModal.data.lulus ? (
                          <CheckCircleOutlined />
                        ) : (
                          <CloseCircleOutlined />
                        )
                      }
                    >
                      {detailModal.data.lulus ? "LULUS" : "TIDAK LULUS"}
                    </Tag>
                  </Descriptions.Item>
                </>
              )}

              {/* Statistics */}
              <Descriptions.Item label="Total Soal">
                {detailModal.data.totalSoal ||
                  detailModal.data.metadata?.totalQuestions ||
                  "-"}
              </Descriptions.Item>
              <Descriptions.Item label="Soal Terjawab">
                {detailModal.data.metadata?.answeredQuestions ||
                  Object.keys(detailModal.data.jawabanPeserta || {}).length ||
                  0}
              </Descriptions.Item>
              <Descriptions.Item label="Soal Benar">
                <Text style={{ color: "#52c41a", fontWeight: "bold" }}>
                  {detailModal.data.jumlahBenar || 0}
                </Text>
              </Descriptions.Item>
              <Descriptions.Item label="Soal Salah">
                <Text style={{ color: "#ff4d4f", fontWeight: "bold" }}>
                  {detailModal.data.jumlahSalah || 0}
                </Text>
              </Descriptions.Item>
              <Descriptions.Item label="Soal Kosong">
                <Text style={{ color: "#faad14", fontWeight: "bold" }}>
                  {detailModal.data.jumlahKosong || 0}
                </Text>
              </Descriptions.Item>
              <Descriptions.Item label="Akurasi Jawaban">
                {detailModal.data.metadata?.answeredQuestions > 0
                  ? `${Math.round(
                      (detailModal.data.jumlahBenar /
                        detailModal.data.metadata.answeredQuestions) *
                        100
                    )}%`
                  : "0%"}
              </Descriptions.Item>
            </Descriptions>

            <Divider orientation="left">📝 Detail Jawaban Per Soal</Divider>

            {(detailModal.data?.jawabanPeserta &&
              Object.keys(detailModal.data.jawabanPeserta).length > 0) ||
            (detailModal.data?.ujian?.bankSoalList &&
              detailModal.data.ujian.bankSoalList.length > 0) ? (
              <div style={{ maxHeight: "500px", overflow: "auto" }}>
                <Table
                  size="small"
                  pagination={false}
                  expandable={{
                    expandedRowRender: (record) => {
                      if (record.jenisSoal === "PG" && record.opsi) {
                        return (
                          <div style={{ padding: "8px 0" }}>
                            <Text
                              strong
                              style={{ display: "block", marginBottom: "8px" }}
                            >
                              Pilihan Jawaban:
                            </Text>
                            <Row gutter={[16, 8]}>
                              {Object.entries(record.opsi).map(
                                ([key, value]) => (
                                  <Col span={12} key={key}>
                                    <div
                                      style={{
                                        padding: "4px 8px",
                                        border: "1px solid #d9d9d9",
                                        borderRadius: "4px",
                                        backgroundColor:
                                          record.jawabanSiswa === key
                                            ? "#e6f7ff"
                                            : record.jawabanBenar?.includes(key)
                                            ? "#f6ffed"
                                            : "#fafafa",
                                      }}
                                    >
                                      <Text strong>{key}.</Text> {value}
                                      {record.jawabanSiswa === key && (
                                        <Tag
                                          color="blue"
                                          size="small"
                                          style={{ marginLeft: "8px" }}
                                        >
                                          Dipilih
                                        </Tag>
                                      )}
                                      {record.jawabanBenar?.includes(key) && (
                                        <Tag
                                          color="green"
                                          size="small"
                                          style={{ marginLeft: "8px" }}
                                        >
                                          Benar
                                        </Tag>
                                      )}
                                    </div>
                                  </Col>
                                )
                              )}
                            </Row>
                          </div>
                        );
                      } else if (record.jenisSoal === "MULTI" && record.opsi) {
                        return (
                          <div style={{ padding: "8px 0" }}>
                            <Text
                              strong
                              style={{ display: "block", marginBottom: "8px" }}
                            >
                              Pilihan Jawaban (Multiple Choice):
                            </Text>
                            <Row gutter={[16, 8]}>
                              {Object.entries(record.opsi).map(
                                ([key, value]) => (
                                  <Col span={12} key={key}>
                                    <div
                                      style={{
                                        padding: "4px 8px",
                                        border: "1px solid #d9d9d9",
                                        borderRadius: "4px",
                                        backgroundColor:
                                          record.jawabanSiswaArray?.includes(
                                            key
                                          )
                                            ? "#e6f7ff"
                                            : record.jawabanBenar?.includes(key)
                                            ? "#f6ffed"
                                            : "#fafafa",
                                      }}
                                    >
                                      <Text strong>{key}.</Text> {value}
                                      {record.jawabanSiswaArray?.includes(
                                        key
                                      ) && (
                                        <Tag
                                          color="blue"
                                          size="small"
                                          style={{ marginLeft: "8px" }}
                                        >
                                          Dipilih
                                        </Tag>
                                      )}
                                      {record.jawabanBenar?.includes(key) && (
                                        <Tag
                                          color="green"
                                          size="small"
                                          style={{ marginLeft: "8px" }}
                                        >
                                          Benar
                                        </Tag>
                                      )}
                                    </div>
                                  </Col>
                                )
                              )}
                            </Row>
                          </div>
                        );
                      } else if (
                        record.jenisSoal === "COCOK" &&
                        record.pasangan
                      ) {
                        const kiri = Object.entries(record.pasangan).filter(
                          ([k]) => k.includes("_kiri")
                        );
                        const kanan = Object.entries(record.pasangan).filter(
                          ([k]) => k.includes("_kanan")
                        );

                        // Parse jawaban siswa dan jawaban benar
                        const jawabanSiswa = record.jawabanSiswa || {};
                        const jawabanBenar = record.jawabanBenar || [];

                        // Buat mapping jawaban benar untuk mudah lookup
                        const jawabanBenarMap = {};
                        jawabanBenar.forEach((jawab) => {
                          const parts = jawab.split("=");
                          if (parts.length === 2) {
                            const [kiriText, kananText] = parts;
                            // Cari indeks dari text
                            const kiriIndex = kiri
                              .find(
                                ([k, v]) => v.trim() === kiriText.trim()
                              )?.[0]
                              ?.replace("_kiri", "");
                            const kananIndex = kanan
                              .find(
                                ([k, v]) => v.trim() === kananText.trim()
                              )?.[0]
                              ?.replace("_kanan", "");
                            if (kiriIndex && kananIndex) {
                              jawabanBenarMap[kiriIndex] = kananIndex;
                            }
                          }
                        });

                        // Generate warna tipis yang konsisten untuk setiap pasangan yang dipilih
                        const generatePairColor = (pairKey) => {
                          const colors = [
                            "#ffeee6", // orange muda
                            "#e6f7ff", // biru muda
                            "#f6ffed", // hijau muda
                            "#fff7e6", // kuning muda
                            "#f9f0ff", // ungu muda
                            "#e6fffb", // cyan muda
                            "#fff0f6", // pink muda
                            "#f0f5ff", // indigo muda
                            "#fcffe6", // lime muda
                            "#fff2e8", // peach muda
                            "#f4f1ff", // lavender muda
                            "#e8f5e8", // mint muda
                          ];
                          // Buat hash dari pairKey untuk konsistensi warna
                          let hash = 0;
                          for (let i = 0; i < pairKey.length; i++) {
                            hash = pairKey.charCodeAt(i) + ((hash << 5) - hash);
                          }
                          return colors[Math.abs(hash) % colors.length];
                        };

                        return (
                          <div style={{ padding: "8px 0" }}>
                            <Text
                              strong
                              style={{ display: "block", marginBottom: "8px" }}
                            >
                              Pasangan untuk Mencocokkan:
                            </Text>

                            {/* Tampilkan pasangan yang dipilih siswa */}
                            {Object.keys(jawabanSiswa).length > 0 && (
                              <div
                                style={{
                                  marginBottom: "16px",
                                  padding: "8px",
                                  backgroundColor: "#f0f8ff",
                                  borderRadius: "4px",
                                }}
                              >
                                <Text strong style={{ color: "#1890ff" }}>
                                  Jawaban Siswa:
                                </Text>
                                <div style={{ marginTop: "4px" }}>
                                  {Object.entries(jawabanSiswa).map(
                                    ([kiriIdx, kananIdx]) => {
                                      const kiriText =
                                        kiri.find(
                                          ([k]) =>
                                            k.replace("_kiri", "") === kiriIdx
                                        )?.[1] || `Item ${kiriIdx}`;
                                      const kananText =
                                        kanan.find(
                                          ([k]) =>
                                            k.replace("_kanan", "") === kananIdx
                                        )?.[1] || `Item ${kananIdx}`;
                                      const isBenar =
                                        jawabanBenarMap[kiriIdx] === kananIdx;
                                      // Warna berdasarkan pasangan yang dipilih
                                      const pairKey = `${kiriIdx}-${kananIdx}`;
                                      const pairColor =
                                        generatePairColor(pairKey);

                                      return (
                                        <Tag
                                          key={pairKey}
                                          style={{
                                            margin: "2px",
                                            backgroundColor: pairColor,
                                            border: `1px solid ${
                                              isBenar ? "#52c41a" : "#ff4d4f"
                                            }`,
                                            color: "#000",
                                          }}
                                        >
                                          {kiriText} = {kananText}
                                          {isBenar && (
                                            <Text
                                              style={{
                                                color: "#52c41a",
                                                marginLeft: 4,
                                              }}
                                            >
                                              ✓
                                            </Text>
                                          )}
                                          {!isBenar && (
                                            <Text
                                              style={{
                                                color: "#ff4d4f",
                                                marginLeft: 4,
                                              }}
                                            >
                                              ✗
                                            </Text>
                                          )}
                                        </Tag>
                                      );
                                    }
                                  )}
                                </div>
                              </div>
                            )}

                            <Row gutter={24}>
                              <Col span={12}>
                                <Text strong>Kolom Kiri:</Text>
                                {kiri.map(([key, value]) => {
                                  const index = key.replace("_kiri", "");
                                  const isSelected =
                                    jawabanSiswa[index] !== undefined;
                                  const selectedKananIndex =
                                    jawabanSiswa[index];
                                  // Warna berdasarkan pasangan yang dipilih
                                  let pairColor = "#fafafa";
                                  if (isSelected) {
                                    const pairKey = `${index}-${selectedKananIndex}`;
                                    pairColor = generatePairColor(pairKey);
                                  }

                                  return (
                                    <div
                                      key={key}
                                      style={{
                                        margin: "4px 0",
                                        padding: "4px 8px",
                                        border: `1px solid ${
                                          isSelected ? "#1890ff" : "#d9d9d9"
                                        }`,
                                        borderRadius: "4px",
                                        backgroundColor: pairColor,
                                        fontWeight: isSelected
                                          ? "bold"
                                          : "normal",
                                      }}
                                    >
                                      <Text>{value}</Text>
                                      {isSelected && (
                                        <Tag
                                          color="blue"
                                          size="small"
                                          style={{ marginLeft: "8px" }}
                                        >
                                          Dipilih →{" "}
                                          {kanan.find(
                                            ([k]) =>
                                              k.replace("_kanan", "") ===
                                              selectedKananIndex
                                          )?.[1] || "Unknown"}
                                        </Tag>
                                      )}
                                    </div>
                                  );
                                })}
                              </Col>
                              <Col span={12}>
                                <Text strong>Kolom Kanan:</Text>
                                {kanan.map(([key, value]) => {
                                  const index = key.replace("_kanan", "");
                                  const selectedByKiri = Object.entries(
                                    jawabanSiswa
                                  ).find(([k, v]) => v === index)?.[0];
                                  const isSelected =
                                    selectedByKiri !== undefined;
                                  // Warna berdasarkan pasangan yang dipilih
                                  let pairColor = "#fafafa";
                                  if (isSelected) {
                                    const pairKey = `${selectedByKiri}-${index}`;
                                    pairColor = generatePairColor(pairKey);
                                  }

                                  return (
                                    <div
                                      key={key}
                                      style={{
                                        margin: "4px 0",
                                        padding: "4px 8px",
                                        border: `1px solid ${
                                          isSelected ? "#1890ff" : "#d9d9d9"
                                        }`,
                                        borderRadius: "4px",
                                        backgroundColor: pairColor,
                                        fontWeight: isSelected
                                          ? "bold"
                                          : "normal",
                                      }}
                                    >
                                      <Text>{value}</Text>
                                      {isSelected && (
                                        <Tag
                                          color="blue"
                                          size="small"
                                          style={{ marginLeft: "8px" }}
                                        >
                                          ← Dipilih oleh{" "}
                                          {kiri.find(
                                            ([k]) =>
                                              k.replace("_kiri", "") ===
                                              selectedByKiri
                                          )?.[1] || "Unknown"}
                                        </Tag>
                                      )}
                                    </div>
                                  );
                                })}
                              </Col>
                            </Row>
                            <div style={{ marginTop: "12px" }}>
                              <Text strong>Jawaban Benar:</Text>
                              <div style={{ marginTop: "4px" }}>
                                {record.jawabanBenar?.map((jawab, idx) => (
                                  <Tag
                                    key={idx}
                                    color="green"
                                    style={{ margin: "2px" }}
                                  >
                                    {jawab}
                                  </Tag>
                                ))}
                              </div>
                            </div>
                          </div>
                        );
                      }
                      return null;
                    },
                    rowExpandable: (record) =>
                      record.jenisSoal === "PG" ||
                      record.jenisSoal === "MULTI" ||
                      record.jenisSoal === "COCOK",
                  }}
                  dataSource={(() => {
                    // Create comprehensive data source from both answered and unanswered questions
                    const jawabanData = detailModal.data?.jawabanPeserta || {};
                    const bankSoalFromUjian =
                      detailModal.data?.ujian?.bankSoalList || [];
                    const allSoalIds = new Set([
                      ...Object.keys(jawabanData),
                      ...bankSoalFromUjian.map((soal) => soal.idBankSoal),
                    ]);

                    return Array.from(allSoalIds).map((soalId, index) => {
                      const jawaban = jawabanData[soalId];
                      const bankSoal =
                        bankSoalMap[soalId] ||
                        bankSoalFromUjian.find((s) => s.idBankSoal === soalId);

                      // Format jawaban display
                      let jawabanDisplay = "";
                      let jawabanSiswaArray = [];
                      let jawabanSiswa = "";

                      if (!jawaban) {
                        jawabanDisplay = "Tidak dijawab";
                        jawabanSiswa = null;
                      } else if (Array.isArray(jawaban)) {
                        jawabanDisplay = jawaban.join(", ");
                        jawabanSiswaArray = jawaban;
                        jawabanSiswa = jawaban[0];
                      } else if (
                        typeof jawaban === "object" &&
                        jawaban !== null
                      ) {
                        // For COCOK type questions - format dengan nama pasangan
                        if (
                          bankSoal?.jenisSoal === "COCOK" &&
                          bankSoal?.pasangan
                        ) {
                          const pasanganMap = {};

                          // Buat mapping dari pasangan
                          Object.entries(bankSoal.pasangan).forEach(
                            ([key, value]) => {
                              if (key.includes("_kiri")) {
                                const index = key.replace("_kiri", "");
                                pasanganMap[index] = { kiri: value };
                              } else if (key.includes("_kanan")) {
                                const index = key.replace("_kanan", "");
                                if (pasanganMap[index]) {
                                  pasanganMap[index].kanan = value;
                                } else {
                                  pasanganMap[index] = { kanan: value };
                                }
                              }
                            }
                          );

                          // Format jawaban dengan nama pasangan
                          jawabanDisplay = Object.entries(jawaban)
                            .map(([kiriIndex, kananIndex]) => {
                              const kiri =
                                pasanganMap[kiriIndex]?.kiri ||
                                `Item ${kiriIndex}`;
                              const kanan =
                                pasanganMap[kananIndex]?.kanan ||
                                `Item ${kananIndex}`;
                              return `${kiri} = ${kanan}`;
                            })
                            .join(", ");
                        } else {
                          // Fallback untuk object lainnya
                          jawabanDisplay = Object.entries(jawaban)
                            .map(([k, v]) => `${k}: ${v}`)
                            .join(", ");
                        }
                        jawabanSiswa = jawaban;
                      } else {
                        jawabanDisplay = String(jawaban || "Tidak dijawab");
                        jawabanSiswa = jawaban;
                      }

                      // Format jawaban benar
                      let jawabanBenarDisplay = "";
                      if (bankSoal?.jawabanBenar) {
                        if (Array.isArray(bankSoal.jawabanBenar)) {
                          jawabanBenarDisplay =
                            bankSoal.jawabanBenar.join(", ");
                        } else {
                          jawabanBenarDisplay = String(bankSoal.jawabanBenar);
                        }
                      }

                      // Determine correctness
                      const isCorrect =
                        detailModal.data?.skorPerSoal?.[soalId] > 0;

                      return {
                        key: soalId,
                        no: index + 1,
                        soalId,
                        pertanyaan:
                          bankSoal?.pertanyaan || "Pertanyaan tidak ditemukan",
                        jenisSoal: bankSoal?.jenisSoal || "Unknown",
                        opsi: bankSoal?.opsi,
                        pasangan: bankSoal?.pasangan,
                        jawaban: jawabanDisplay,
                        jawabanSiswa,
                        jawabanSiswaArray,
                        jawabanBenar: bankSoal?.jawabanBenar,
                        jawabanBenarDisplay,
                        benar: jawaban
                          ? isCorrect
                            ? "Benar"
                            : "Salah"
                          : "Tidak dijawab",
                        skor: detailModal.data?.skorPerSoal?.[soalId] || 0,
                        bobot: bankSoal?.bobot || "-",
                        toleransiTypo: bankSoal?.toleransiTypo,
                      };
                    });
                  })()}
                  columns={[
                    {
                      title: "No",
                      dataIndex: "no",
                      width: 50,
                      align: "center",
                    },
                    {
                      title: "Pertanyaan",
                      dataIndex: "pertanyaan",
                      width: 300,
                      ellipsis: true,
                      render: (text) => (
                        <Tooltip title={text}>
                          <div
                            dangerouslySetInnerHTML={{
                              __html:
                                text.length > 100
                                  ? `${text.substring(0, 100)}...`
                                  : text,
                            }}
                            style={{ fontSize: "12px" }}
                          />
                        </Tooltip>
                      ),
                    },
                    {
                      title: "Jenis",
                      dataIndex: "jenisSoal",
                      width: 80,
                      align: "center",
                      render: (text) => {
                        const colorMap = {
                          PG: "blue",
                          MULTI: "green",
                          COCOK: "orange",
                          ISIAN: "purple",
                        };
                        return (
                          <Tag color={colorMap[text] || "default"}>{text}</Tag>
                        );
                      },
                    },
                    {
                      title: "Jawaban Siswa",
                      dataIndex: "jawaban",
                      width: 180,
                      ellipsis: true,
                      render: (text, record) => (
                        <Tooltip title={text}>
                          <div>
                            <Text
                              style={{
                                fontSize: "12px",
                                color:
                                  text === "Tidak dijawab"
                                    ? "#ff4d4f"
                                    : "inherit",
                              }}
                            >
                              {text.length > 40
                                ? `${text.substring(0, 40)}...`
                                : text}
                            </Text>
                            {record.toleransiTypo && (
                              <div>
                                <Tag size="small" color="cyan">
                                  Toleransi: {record.toleransiTypo}
                                </Tag>
                              </div>
                            )}
                          </div>
                        </Tooltip>
                      ),
                    },
                    {
                      title: "Jawaban Benar",
                      dataIndex: "jawabanBenarDisplay",
                      width: 180,
                      ellipsis: true,
                      render: (text) => (
                        <Tooltip title={text}>
                          <Text style={{ fontSize: "12px", color: "#52c41a" }}>
                            {text.length > 40
                              ? `${text.substring(0, 40)}...`
                              : text || "-"}
                          </Text>
                        </Tooltip>
                      ),
                    },
                    {
                      title: "Status",
                      dataIndex: "benar",
                      width: 100,
                      align: "center",
                      render: (text) => {
                        const colorMap = {
                          Benar: "success",
                          Salah: "error",
                          "Tidak dijawab": "warning",
                        };
                        return <Tag color={colorMap[text]}>{text}</Tag>;
                      },
                    },
                    {
                      title: "Skor",
                      dataIndex: "skor",
                      width: 80,
                      align: "center",
                      render: (skor, record) => (
                        <div style={{ textAlign: "center" }}>
                          <Text
                            strong
                            style={{
                              color: skor > 0 ? "#52c41a" : "#ff4d4f",
                              fontSize: "14px",
                            }}
                          >
                            {skor}
                          </Text>
                          <br />
                          <Text type="secondary" style={{ fontSize: "10px" }}>
                            /{record.bobot}
                          </Text>
                        </div>
                      ),
                    },
                  ]}
                  scroll={{ x: 1000 }}
                />
              </div>
            ) : detailModal.data?.fullData?.jawabanPeserta ? (
              <div style={{ marginBottom: 16 }}>
                <Alert
                  message="Menggunakan data fallback dari fullData"
                  type="warning"
                  closable
                  style={{ marginBottom: 16 }}
                />
                <div style={{ maxHeight: "400px", overflow: "auto" }}>
                  <Table
                    size="small"
                    pagination={false}
                    dataSource={Object.entries(
                      detailModal.data.fullData.jawabanPeserta || {}
                    ).map(([soalId, jawaban], index) => {
                      // Find question details from bankSoalMap
                      const bankSoal = bankSoalMap[soalId];
                      const jawabanBenar =
                        detailModal.data?.fullData?.jawabanBenar?.[soalId];
                      const skorSoal =
                        detailModal.data?.fullData?.skorPerSoal?.[soalId] || 0;

                      return {
                        key: soalId,
                        no: index + 1,
                        pertanyaan:
                          bankSoal?.pertanyaan || `Soal ID: ${soalId}`,
                        jawaban: jawaban || "-",
                        jawabanBenar: jawabanBenar || "-",
                        skor: skorSoal,
                        bobot: bankSoal?.bobot || 1,
                        tipe: bankSoal?.tipeSoal || "Unknown",
                      };
                    })}
                    columns={[
                      {
                        title: "No",
                        dataIndex: "no",
                        width: 50,
                        align: "center",
                      },
                      {
                        title: "Tipe",
                        dataIndex: "tipe",
                        width: 80,
                        render: (type) => (
                          <Tag
                            color={
                              type === "PILIHAN_GANDA"
                                ? "blue"
                                : type === "ESSAY"
                                ? "green"
                                : type === "COCOK"
                                ? "orange"
                                : "default"
                            }
                          >
                            {type}
                          </Tag>
                        ),
                      },
                      {
                        title: "Pertanyaan",
                        dataIndex: "pertanyaan",
                        width: 300,
                        ellipsis: true,
                        render: (text) => (
                          <Tooltip title={text}>
                            <div
                              dangerouslySetInnerHTML={{
                                __html:
                                  text.length > 100
                                    ? `${text.substring(0, 100)}...`
                                    : text,
                              }}
                            />
                          </Tooltip>
                        ),
                      },
                      {
                        title: "Jawaban Siswa",
                        dataIndex: "jawaban",
                        width: 150,
                        ellipsis: true,
                        render: (text) => (
                          <Tooltip title={text}>
                            <Text style={{ fontSize: "12px" }}>
                              {text.length > 30
                                ? `${text.substring(0, 30)}...`
                                : text}
                            </Text>
                          </Tooltip>
                        ),
                      },
                      {
                        title: "Jawaban Benar",
                        dataIndex: "jawabanBenar",
                        width: 150,
                        ellipsis: true,
                        render: (text) => (
                          <Tooltip title={text}>
                            <Text style={{ fontSize: "12px" }}>
                              {text.length > 30
                                ? `${text.substring(0, 30)}...`
                                : text}
                            </Text>
                          </Tooltip>
                        ),
                      },
                      {
                        title: "Skor",
                        dataIndex: "skor",
                        width: 80,
                        align: "center",
                        render: (skor, record) => (
                          <div>
                            <Text
                              strong
                              style={{
                                color: skor > 0 ? "#52c41a" : "#ff4d4f",
                                fontSize: "14px",
                              }}
                            >
                              {skor}
                            </Text>
                            <br />
                            <Text type="secondary" style={{ fontSize: "10px" }}>
                              /{record.bobot}
                            </Text>
                          </div>
                        ),
                      },
                    ]}
                    scroll={{ x: 800 }}
                  />
                </div>
              </div>
            ) : (
              <Alert
                message="Tidak ada data jawaban tersedia"
                description="Data jawaban peserta tidak ditemukan untuk ujian ini."
                type="info"
                showIcon
              />
            )}

            {/* Notice for score and review */}
            {detailModal.data.ujian?.tampilkanNilai === false && (
              <Alert
                message="Nilai Disembunyikan"
                description="Pengajar telah mengatur agar nilai tidak ditampilkan untuk ujian ini."
                type="info"
                showIcon
                style={{ marginTop: 16 }}
              />
            )}
          </div>
        )}
      </Modal>

      {/* Violations Modal (for a specific student's exam session) */}
      <Modal
        title={
          <Space>
            <WarningOutlined style={{ color: "#ff4d4f" }} />
            <span>Rincian Pelanggaran</span>
          </Space>
        }
        visible={violationModalVisible}
        onCancel={() => setViolationModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setViolationModalVisible(false)}>
            Tutup
          </Button>,
        ]}
        width={700}
      >
        {detailModal.data && ( // Ensure detailModal.data is available for context
          <div>
            <Alert
              message={`Ditemukan ${violations.length} pelanggaran selama ujian`}
              type="warning"
              showIcon
              style={{ marginBottom: 16 }}
            />

            {loadingViolations ? (
              <div style={{ textAlign: "center", padding: "20px" }}>
                <Spin />
                <Text>Memuat data pelanggaran...</Text>
              </div>
            ) : violations.length > 0 ? (
              violations.map((violation, index) => (
                <Card
                  key={index}
                  size="small"
                  style={{ marginBottom: 8 }}
                  title={
                    <Space>
                      <WarningOutlined style={{ color: "#ff4d4f" }} />
                      <Text strong>Pelanggaran #{index + 1}</Text>
                    </Space>
                  }
                >
                  <Descriptions size="small" column={1}>
                    <Descriptions.Item label="Jenis Pelanggaran">
                      {/* Handle null typeViolation */}
                      <Tag color="red">
                        {violation.typeViolation || "Tidak diketahui"}
                      </Tag>
                    </Descriptions.Item>
                    <Descriptions.Item label="Tingkat Keparahan">
                      {/* Handle null severity */}
                      <Tag
                        color={
                          violation.severity === "HIGH"
                            ? "red"
                            : violation.severity === "MEDIUM"
                            ? "orange"
                            : violation.severity === "LOW"
                            ? "yellow"
                            : "default"
                        }
                      >
                        {violation.severity || "Tidak diketahui"}
                      </Tag>
                    </Descriptions.Item>
                    <Descriptions.Item label="Waktu">
                      {/* Handle null detectedAt */}
                      {violation.detectedAt
                        ? dayjs(violation.detectedAt).format(
                            "DD/MM/YYYY HH:mm:ss"
                          )
                        : "Tidak tersedia"}
                    </Descriptions.Item>
                    {/* Jumlah Pelanggaran dihapus karena sudah di-deduplicate */}
                    {(violation.details ||
                      Object.keys(violation.evidence || {}).length > 0) && (
                      <Descriptions.Item label="Detail">
                        <Text type="secondary">
                          {violation.details ||
                            (Object.keys(violation.evidence || {}).length > 0
                              ? JSON.stringify(violation.evidence, null, 2)
                              : "Tidak ada detail")}
                        </Text>
                      </Descriptions.Item>
                    )}
                  </Descriptions>
                </Card>
              ))
            ) : (
              <div style={{ textAlign: "center", padding: "20px" }}>
                <Text type="secondary">
                  Tidak ada data pelanggaran ditemukan
                </Text>
              </div>
            )}
          </div>
        )}
      </Modal>

      {/* --- NEW: Modal to Display All Cheat Detections --- */}

      <Modal
        title={
          <Space>
            <DatabaseOutlined />
            <span>Semua Data Pelanggaran</span>
          </Space>
        }
        visible={showAllViolationsModal}
        onCancel={() => setShowAllViolationsModal(false)}
        footer={[
          <Button key="close" onClick={() => setShowAllViolationsModal(false)}>
            Tutup
          </Button>,
        ]}
        width={1200}
        style={{ top: 20 }}
        bodyStyle={{ maxHeight: "calc(100vh - 200px)", overflowY: "auto" }}
      >
        {loadingAllViolations ? (
          <div style={{ textAlign: "center", padding: "50px" }}>
            <Spin size="large" />
            <Title level={4} style={{ marginTop: 20 }}>
              Memuat semua data pelanggaran...
            </Title>
            <Text type="secondary">
              Ini mungkin membutuhkan waktu jika datanya banyak.
            </Text>
          </div>
        ) : (
          <Table
            columns={allViolationsColumns}
            dataSource={getAllViolationsFromMetadata()}
            rowKey={(record, index) =>
              record.idDetection || `violation-${index}`
            }
            pagination={{
              total: getAllViolationsFromMetadata().length,
              showSizeChanger: true,
              showQuickJumper: true,
              showTotal: (total, range) =>
                `${range[0]}-${range[1]} dari ${total} data`,
            }}
            size="small"
            locale={{
              emptyText: "Tidak ada data pelanggaran di database.",
            }}
          />
        )}
      </Modal>
      {/* --------------------------------------------------- */}

      {/* Modal Export Excel dengan Filter Ujian */}
      <Modal
        title={
          <Space>
            <FileExcelOutlined style={{ color: "#52c41a" }} />
            <span>Export Data ke Excel</span>
          </Space>
        }
        visible={showExportModal}
        onCancel={() => {
          setShowExportModal(false);
          setSelectedUjianForExport(null);
        }}
        footer={[
          <Button
            key="export-all"
            onClick={handleExportAllData}
            loading={loadingExport}
          >
            Export Semua Data
          </Button>,
          <Button
            key="export-selected"
            type="primary"
            onClick={handleExportWithSelectedUjian}
            disabled={!selectedUjianForExport}
            loading={loadingExport}
          >
            Export Data Terpilih
          </Button>,
          <Button
            key="cancel"
            onClick={() => {
              setShowExportModal(false);
              setSelectedUjianForExport(null);
            }}
          >
            Batal
          </Button>,
        ]}
        width={600}
      >
        <div style={{ marginBottom: 16 }}>
          <Alert
            message="Pilih Ujian untuk Export"
            description="Anda dapat memilih ujian tertentu untuk di-export, atau export semua data yang tersedia."
            type="info"
            showIcon
            style={{ marginBottom: 16 }}
          />
        </div>

        <div style={{ marginBottom: 16 }}>
          <Typography.Text strong>Pilih Ujian:</Typography.Text>
          <Select
            style={{ width: "100%", marginTop: 8 }}
            placeholder="Pilih ujian yang akan di-export..."
            value={selectedUjianForExport?.idUjian}
            onChange={(value) => {
              const selected = ujianList.find(
                (ujian) => ujian.idUjian === value
              );
              setSelectedUjianForExport(selected);
            }}
            showSearch
            optionFilterProp="children"
            filterOption={(input, option) =>
              option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
            allowClear
            onClear={() => setSelectedUjianForExport(null)}
          >
            {ujianList.map((ujian) => (
              <Select.Option key={ujian.idUjian} value={ujian.idUjian}>
                <div>
                  <div style={{ fontWeight: "bold" }}>{ujian.namaUjian}</div>
                  <div style={{ fontSize: "12px", color: "#666" }}>
                    {ujian.mapel?.name} • {ujian.kelas?.namaKelas} •{" "}
                    {ujian.semester?.namaSemester}
                  </div>
                </div>
              </Select.Option>
            ))}
          </Select>
        </div>

        {selectedUjianForExport && (
          <div
            style={{
              padding: 12,
              backgroundColor: "#f6ffed",
              border: "1px solid #b7eb8f",
              borderRadius: 6,
              marginBottom: 16,
            }}
          >
            <Typography.Text strong style={{ color: "#52c41a" }}>
              Ujian Terpilih: {selectedUjianForExport.namaUjian}
            </Typography.Text>
            <br />
            <Typography.Text type="secondary">
              Mata Pelajaran: {selectedUjianForExport.mapel?.name || "-"} |
              Kelas: {selectedUjianForExport.kelas?.namaKelas || "-"} |
              Semester: {selectedUjianForExport.semester?.namaSemester || "-"}
            </Typography.Text>
          </div>
        )}

        <div
          style={{
            padding: 12,
            backgroundColor: "#fff7e6",
            border: "1px solid #ffd591",
            borderRadius: 6,
          }}
        >
          <Typography.Text strong>Informasi Export:</Typography.Text>
          <ul style={{ margin: "8px 0 0 0", paddingLeft: 20 }}>
            <li>
              File akan berisi 4 sheet: Ringkasan, Detail Jawaban, Detail
              Pelanggaran, Master Soal
            </li>
            <li>
              Data violations akan di-deduplicate berdasarkan waktu dan jenis
              pelanggaran
            </li>
            <li>Format file: Excel (.xlsx)</li>
          </ul>
        </div>
      </Modal>
      {/* --------------------------------------------------- */}
    </div>
  );
};

export default ReportNilaiSiswa;
