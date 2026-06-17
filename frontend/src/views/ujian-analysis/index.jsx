/* eslint-disable no-unused-vars */
import React, { useEffect, useState, useCallback } from "react";
import jsPDF from "jspdf";
import "jspdf-autotable";
import {
  Card,
  Table,
  Button,
  Space,
  Tag,
  Spin,
  Alert,
  Typography,
  Statistic,
  Row,
  Col,
  Modal,
  Progress,
  Tooltip,
  Select,
  Input,
  message,
  List,
  Badge,
  Tabs,
} from "antd";
import {
  EyeOutlined,
  BarChartOutlined,
  ExclamationCircleOutlined,
  UserOutlined,
  TrophyOutlined,
  ClockCircleOutlined,
  SearchOutlined,
  ReloadOutlined,
  UsergroupAddOutlined,
  DownloadOutlined,
  CheckCircleOutlined,
  BookOutlined,
  SafetyOutlined,
  BulbOutlined,
  SecurityScanOutlined,
} from "@ant-design/icons";
import { getUjian } from "@/api/ujian";
import { getHasilByUjian, getHasilUjian } from "@/api/hasilUjian";
import {
  getAnalysisByUjian,
  getAllAnalysis,
  generateAnalysis,
} from "@/api/ujianAnalysis";
import { getUsers, getUserById } from "@/api/user";
import { useNavigate } from "react-router-dom";

const { Title, Text } = Typography;
const { Option } = Select;

const AnalisisUjian = () => {
  const [siswaData, setSiswaData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [generating, setGenerating] = useState(false);
  const [searchText, setSearchText] = useState("");
  const [filterUjian, setFilterUjian] = useState(null);
  const [filterKelas, setFilterKelas] = useState(null);
  const [ujianList, setUjianList] = useState([]);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0,
  });

  // Detail modal states
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [selectedDetailData, setSelectedDetailData] = useState(null);
  const [detailLoading, setDetailLoading] = useState(false);

  // Students data for name mapping
  const [studentsData, setStudentsData] = useState({});
  const [studentNamesCache, setStudentNamesCache] = useState({});

  // Helper function for grade colors
  const getGradeColor = (grade) => {
    const gradeColors = {
      A: "green",
      B: "blue",
      C: "orange",
      D: "red",
      E: "red",
      F: "red",
      Lulus: "green",
      "Tidak Lulus": "red",
    };
    return gradeColors[grade] || "default";
  };

  // Export to PDF function
  const exportToPDF = (data) => {
    try {
      const doc = new jsPDF();

      // Header
      doc.setFontSize(16);
      doc.setFont(undefined, "bold");
      doc.text("Laporan Analisis Ujian Komprehensif", 20, 20);

      // Basic Info
      doc.setFontSize(12);
      doc.setFont(undefined, "normal");
      doc.text(`Ujian: ${data.ujianNama || "N/A"}`, 20, 35);
      doc.text(`Total Peserta: ${data.totalParticipants || 0}`, 20, 45);
      doc.text(
        `Rata-rata Nilai: ${(data.averageScore || 0).toFixed(1)}`,
        20,
        55
      );
      doc.text(
        `Tingkat Kelulusan: ${(data.passRate || 0).toFixed(1)}%`,
        20,
        65
      );
      doc.text(`Dibuat: ${new Date().toLocaleDateString("id-ID")}`, 20, 75);

      // Statistics Table
      const statisticData = [
        ["Median", (data.medianScore || 0).toFixed(1)],
        ["Std. Deviasi", (data.standardDeviation || 0).toFixed(1)],
        ["Nilai Tertinggi", (data.highestScore || 0).toFixed(1)],
        ["Nilai Terendah", (data.lowestScore || 0).toFixed(1)],
        ["Jumlah Lulus", data.passedCount || 0],
        ["Jumlah Tidak Lulus", data.failedCount || 0],
      ];

      doc.autoTable({
        head: [["Statistik", "Nilai"]],
        body: statisticData,
        startY: 85,
        theme: "grid",
        styles: { fontSize: 10 },
      });

      // Grade Distribution
      if (
        data.gradeDistribution &&
        Object.keys(data.gradeDistribution).length > 0
      ) {
        const gradeData = Object.entries(data.gradeDistribution).map(
          ([grade, count]) => [
            `Nilai ${grade}`,
            count,
            `${(data.gradePercentages?.[grade] || 0).toFixed(1)}%`,
          ]
        );

        doc.autoTable({
          head: [["Grade", "Jumlah Siswa", "Persentase"]],
          body: gradeData,
          startY: doc.lastAutoTable.finalY + 10,
          theme: "grid",
          styles: { fontSize: 10 },
        });
      }

      // Item Analysis Summary
      if (data.itemAnalysis) {
        const itemData = Object.entries(data.itemAnalysis).map(
          ([id, item], index) => [
            index + 1,
            item.pertanyaan?.substring(0, 50) + "...",
            item.jenisSoal,
            item.totalResponses,
            item.correctResponses,
            `${(item.correctPercentage || 0).toFixed(1)}%`,
            item.difficultyLevel,
          ]
        );

        doc.autoTable({
          head: [
            [
              "No",
              "Pertanyaan",
              "Jenis",
              "Respons",
              "Benar",
              "%Benar",
              "Kesulitan",
            ],
          ],
          body: itemData,
          startY: doc.lastAutoTable.finalY + 10,
          theme: "grid",
          styles: { fontSize: 8 },
          columnStyles: {
            1: { cellWidth: 60 },
          },
        });
      }

      // Save PDF
      doc.save(
        `Analisis_Ujian_${data.ujianNama?.replace(/\s+/g, "_") || "Report"}_${
          new Date().toISOString().split("T")[0]
        }.pdf`
      );

      message.success("Laporan PDF berhasil diunduh!");
    } catch (error) {
      console.error("Error generating PDF:", error);
      message.error("Gagal membuat laporan PDF");
    }
  };

  const navigate = useNavigate();

  // Fetch ujian list for filter
  const fetchUjianList = useCallback(async () => {
    try {
      const response = await getUjian();
      setUjianList(response.data?.content || []);
    } catch (error) {
      console.error("Error fetching ujian list:", error);
    }
  }, []);

  // Fetch ujian analysis data (comprehensive analysis with violations) - FIXED TO USE ANALYSIS API
  const fetchHasilUjian = useCallback(
    async (page = 1, size = 10) => {
      setLoading(true);
      try {
        let result;

        if (filterUjian) {
          // Get analysis for specific ujian - USE ANALYSIS API
          console.log("🎯 Fetching analysis for specific ujian:", filterUjian);
          try {
            result = await getAnalysisByUjian(filterUjian, {
              page,
              size: 1000,
            }); // Get analysis with violations
            console.log("✅ Analysis API Response for ujian:", result);
            result._dataSource = "ujian-analysis"; // Mark data source
          } catch (ujianError) {
            console.error("Error fetching analysis by ujian:", ujianError);
            console.warn(
              "⚠️ FALLBACK: Using HasilUjian instead of UjianAnalysis - violations may be 0!"
            );
            // Fallback to hasil ujian if analysis not available
            try {
              result = await getHasilByUjian(filterUjian, true); // Include analytics
              result._dataSource = "hasil-ujian"; // Mark fallback data source
              console.log("Fallback to Hasil Ujian API:", result);
            } catch (fallbackError) {
              result = { data: { content: [] } };
              message.warning(
                "Gagal memuat data analisis untuk ujian yang dipilih"
              );
            }
          }
        } else {
          // Get all analysis data - USE ANALYSIS API
          console.log("📊 Fetching ALL analysis data from ujian-analysis API");
          try {
            result = await getAllAnalysis({ page, size: 1000 }); // Get all analysis
            console.log("✅ All Analysis API Response:", result);
            result._dataSource = "ujian-analysis"; // Mark data source
          } catch (allError) {
            console.error("Error fetching all analysis:", allError);
            console.warn(
              "⚠️ FALLBACK: Using HasilUjian instead of UjianAnalysis - violations will be 0!"
            );
            // Fallback to hasil ujian
            try {
              result = await getHasilUjian(1000);
              result._dataSource = "hasil-ujian"; // Mark fallback data source
              console.log("Fallback to All Hasil Ujian:", result);
            } catch (fallbackError) {
              result = { data: { content: [] } };
              message.warning("Gagal memuat semua data analisis");
            }
          }
        }

        console.log("Analysis API Response:", result);
        console.log(
          "API Endpoint used:",
          filterUjian
            ? `getAnalysisByUjian(${filterUjian})`
            : "getAllAnalysis()"
        );
        console.log("Data Source:", result._dataSource || "unknown");

        if (result.data?.content) {
          console.log("Found", result.data.content.length, "analysis records");
          console.log("Sample record structure:", result.data.content[0]);

          // Warning if using fallback data
          if (result._dataSource === "hasil-ujian") {
            message.warning(
              "⚠️ Menampilkan data hasil ujian - data pelanggaran mungkin tidak lengkap. Pastikan data analisis tersedia di database.",
              5
            );
          }

          // DEDUPLICATE ANALYSIS RECORDS - Get latest/best record per ujian
          let processedContent = result.data.content;

          if (processedContent.length > 1) {
            console.log(
              `🔍 Found ${processedContent.length} total analysis records`
            );

            // Group by ujian ID to handle duplicates
            const groupedByUjian = {};
            processedContent.forEach((item) => {
              const ujianId = item.idUjian || item.ujian?.idUjian;
              if (!groupedByUjian[ujianId]) {
                groupedByUjian[ujianId] = [];
              }
              groupedByUjian[ujianId].push(item);
            });

            // For each ujian, select the best record
            const deduplicatedContent = [];
            Object.entries(groupedByUjian).forEach(([ujianId, records]) => {
              if (records.length === 1) {
                deduplicatedContent.push(records[0]);
              } else {
                console.log(
                  `🔍 Found ${records.length} duplicate records for ujian ${ujianId}`
                );
                console.log(
                  "Records by status:",
                  records.map((item) => ({
                    id: item.idAnalysis,
                    status: item.ujian?.statusUjian,
                    generatedAt: item.generatedAt,
                    updatedAt: item.updatedAt,
                  }))
                );

                // Priority: SELESAI status > latest updatedAt/generatedAt
                const sortedRecords = records.sort((a, b) => {
                  // First priority: SELESAI status
                  if (
                    a.ujian?.statusUjian === "SELESAI" &&
                    b.ujian?.statusUjian !== "SELESAI"
                  )
                    return -1;
                  if (
                    b.ujian?.statusUjian === "SELESAI" &&
                    a.ujian?.statusUjian !== "SELESAI"
                  )
                    return 1;

                  // Second priority: latest timestamp
                  const aTime = new Date(a.updatedAt || a.generatedAt || 0);
                  const bTime = new Date(b.updatedAt || b.generatedAt || 0);
                  return bTime - aTime;
                });

                deduplicatedContent.push(sortedRecords[0]); // Take the best record
                console.log(`✅ Selected best record for ujian ${ujianId}:`, {
                  id: sortedRecords[0].idAnalysis,
                  status: sortedRecords[0].ujian?.statusUjian,
                  generatedAt: sortedRecords[0].generatedAt,
                  violations: sortedRecords[0].violationIds?.length || 0,
                });
              }
            });

            processedContent = deduplicatedContent;
            console.log(
              `✅ After deduplication: ${processedContent.length} unique ujian records`
            );
          }

          // Map analysis data to table format - COMPREHENSIVE DEBUGGING
          let allData = processedContent.map((item, index) => {
            // DEBUG: Log actual structure received
            console.log(`Item ${index} structure:`, item);

            // Smart extraction handling multiple API response structures
            let hasil,
              analysisData,
              isFromAnalysisAPI = false;

            if (item.idHasilUjian && !item.analysisData) {
              // Direct HasilUjian structure (fallback data)
              hasil = item;
              analysisData = item.analysisMetadata
                ? JSON.parse(item.analysisMetadata || "{}")
                : {};
              isFromAnalysisAPI = false;
            } else if (item.hasilUjian || item.idAnalysis) {
              // UjianAnalysis structure (preferred)
              hasil = item.hasilUjian || item;
              analysisData = item.analysisData || item;
              isFromAnalysisAPI = true;
            } else {
              // Fallback
              hasil = item;
              analysisData = item;
              isFromAnalysisAPI = !!item.analysisData;
            }

            console.log(
              `Item ${index} - From Analysis API: ${isFromAnalysisAPI}`,
              { hasil, analysisData }
            );

            // Extract participant and exam information safely
            // For UjianAnalysis, this is ujian-level data, not per-participant
            const participantName = "Semua Peserta"; // This is ujian-level analysis
            const ujianName =
              item.ujian?.namaUjian ||
              hasil.ujian?.namaUjian ||
              item.ujianNama ||
              "Ujian";

            // Log the actual structure we're working with
            console.log(`📊 Processing ujian-level analysis:`, {
              ujianId: item.idUjian,
              ujianName: ujianName,
              totalParticipants: item.totalParticipants,
              completedParticipants: item.completedParticipants,
              violationStructure: {
                violationIds: item.violationIds?.length,
                cheatDetections: item.cheatDetections?.length,
                suspiciousSubmissions: item.suspiciousSubmissions,
              },
            });

            // Extract REAL violation data with correct structure
            let violationCount = 0;
            let cheatingData = {};

            // Since we're getting UjianAnalysis data directly, extract from correct fields
            if (
              item.violationIds ||
              item.cheatDetections ||
              item.suspiciousSubmissions
            ) {
              // Direct UjianAnalysis structure - extract violation data
              cheatingData = {
                violationIds: item.violationIds || [],
                cheatDetections: item.cheatDetections || [],
                suspiciousSubmissions: item.suspiciousSubmissions || 0,
                flaggedParticipants: item.flaggedParticipants || 0,
                integrityScore: item.integrityScore || 0,
              };

              // Count violations from multiple sources
              violationCount = Math.max(
                (item.violationIds || []).length,
                (item.cheatDetections || []).length,
                item.suspiciousSubmissions || 0
              );

              console.log(`🔍 Analysis Data Violations for ${ujianName}:`, {
                violationIds: item.violationIds?.length || 0,
                cheatDetections: item.cheatDetections?.length || 0,
                suspiciousSubmissions: item.suspiciousSubmissions || 0,
                finalCount: violationCount,
              });
            } else if (isFromAnalysisAPI) {
              // Nested analysis data - look for violations in nested structure
              cheatingData =
                analysisData.cheating ||
                analysisData.violations ||
                analysisData.security ||
                {};
              violationCount =
                cheatingData.totalViolations ||
                cheatingData.violationCount ||
                cheatingData.count ||
                analysisData.securityMetrics?.violationCount ||
                analysisData.totalViolations ||
                analysisData.violationsCount ||
                0;
            } else {
              // HasilUjian fallback - try to find any violation data
              violationCount =
                hasil.violationCount || hasil.totalViolations || 0;

              console.warn(
                `⚠️ Using HasilUjian data for ${participantName} - violation data may be incomplete`
              );
            }

            console.log(
              `Violations for ${participantName}:`,
              violationCount,
              "from:",
              isFromAnalysisAPI ? "Analysis API" : "HasilUjian API",
              cheatingData
            );

            // Calculate risk level based on REAL violations and ujian-level metrics
            const score =
              item.averageScore || hasil.persentase || item.finalScore || 0;
            const integrityScore = item.integrityScore || 0;

            let riskLevel = "LOW";

            // Risk calculation for ujian-level analysis
            if (violationCount > 3 || integrityScore < 30 || score < 40) {
              riskLevel = "HIGH";
            } else if (
              violationCount > 1 ||
              integrityScore < 70 ||
              score < 60
            ) {
              riskLevel = "MEDIUM";
            }

            console.log(`🎯 Risk calculation for ${ujianName}:`, {
              violationCount,
              integrityScore,
              avgScore: score,
              riskLevel,
            });

            return {
              key: item.idAnalysis || hasil.idHasilUjian || `analysis-${index}`,

              // Student information - For ujian-level analysis, show summary
              pesertaId: "ALL", // This is ujian-level analysis
              pesertaNama: participantName, // "Semua Peserta"
              pesertaUsername: `${item.totalParticipants || 0} peserta`,

              // Exam information - Support both data structures
              ujianId: item.idUjian || hasil.idUjian || item.ujianId, // Use UjianAnalysis structure first
              ujianNama: ujianName,

              // Performance data - Ujian level averages
              nilai: score, // Average score
              totalSkor: item.highestScore || 0,
              skorMaksimal: 100,
              nilaiHuruf:
                score >= 80
                  ? "A"
                  : score >= 70
                  ? "B"
                  : score >= 60
                  ? "C"
                  : score >= 50
                  ? "D"
                  : "E",
              lulus: (item.passedCount || 0) > 0,

              // Question breakdown - Ujian level summary
              totalSoal: Object.keys(item.itemAnalysis || {}).length || 0,
              jumlahBenar: item.totalParticipants || 0, // Placeholder
              jumlahSalah: 0, // Ujian level doesn't have this detail
              jumlahKosong: 0,

              // Time metrics - Ujian level averages
              durasi: item.averageCompletionTime || 0,
              waktuMulai: item.generatedAt,
              waktuSelesai: item.updatedAt,
              statusPengerjaan: item.ujian?.statusUjian || "SELESAI",

              // Security & Analytics - USE REAL UJIAN-LEVEL DATA
              violationCount: violationCount,
              riskLevel: riskLevel,
              needsReview:
                riskLevel === "HIGH" ||
                integrityScore < 50 ||
                violationCount > 2,
              integrityScore: integrityScore,

              // Learning analytics - Extract from ujian analysis
              workingPattern: violationCount > 3 ? "Irregular" : "Normal",
              learningStyle: "Mixed",
              confidenceLevel:
                integrityScore > 70
                  ? "HIGH"
                  : integrityScore > 40
                  ? "MEDIUM"
                  : "LOW",

              // Class information - From ujian analysis
              kelas:
                Object.keys(item.performanceByKelas || {}).join(", ") ||
                "Semua Kelas",

              // School information
              school: item.school?.nameSchool || "Tidak Diketahui",

              // Full data for detail view
              fullData: hasil,
            };
          });

          // Apply search filter if needed
          if (searchText) {
            allData = allData.filter(
              (item) =>
                item.pesertaNama
                  .toLowerCase()
                  .includes(searchText.toLowerCase()) ||
                item.ujianNama.toLowerCase().includes(searchText.toLowerCase())
            );
          }

          // Apply pagination
          const startIndex = (page - 1) * size;
          const endIndex = startIndex + size;
          const paginatedData = allData.slice(startIndex, endIndex);

          setSiswaData(paginatedData);
          setPagination({
            current: page,
            pageSize: size,
            total: allData.length,
          });
        } else {
          setSiswaData([]);
          setPagination((prev) => ({ ...prev, total: 0 }));
        }
      } catch (error) {
        console.error("Error fetching hasil ujian:", error);
        setSiswaData([]);
        Modal.error({
          title: "Error",
          content: `Gagal mengambil data hasil ujian: ${error.message}`,
        });
      } finally {
        setLoading(false);
      }
    },
    [filterUjian, searchText]
  );

  // Function to fetch student names by IDs
  const fetchStudentNames = useCallback(
    async (studentIds) => {
      try {
        // Check which student IDs we don't have cached yet
        const uncachedIds = studentIds.filter((id) => !studentNamesCache[id]);

        if (uncachedIds.length === 0) {
          return studentNamesCache;
        }

        console.log("🔍 Fetching student names for IDs:", uncachedIds);

        // First try to get all users and filter by IDs
        try {
          const usersResponse = await getUsers();
          const allUsers =
            usersResponse.data?.content || usersResponse.data || [];

          const newNameCache = { ...studentNamesCache };

          // Map student IDs to names from users data
          uncachedIds.forEach((studentId) => {
            const user = allUsers.find(
              (u) =>
                u.id === studentId ||
                u.userId === studentId ||
                u.username === studentId ||
                u.pesertaId === studentId
            );

            if (user) {
              newNameCache[studentId] =
                user.nama ||
                user.namaLengkap ||
                user.name ||
                user.fullName ||
                `User ${studentId.substring(0, 8)}`;
            } else {
              // Fallback: try to get individual user by ID
              newNameCache[studentId] = `Loading...`;
            }
          });

          setStudentNamesCache(newNameCache);

          // For any remaining "Loading..." entries, try individual API calls
          const stillLoading = Object.entries(newNameCache).filter(
            ([id, name]) => name === "Loading..."
          );

          if (stillLoading.length > 0 && stillLoading.length <= 10) {
            // Limit concurrent requests
            const individualFetches = stillLoading.map(async ([studentId]) => {
              try {
                const userResponse = await getUserById(studentId);
                const user = userResponse.data;
                if (user) {
                  return {
                    id: studentId,
                    name:
                      user.nama ||
                      user.namaLengkap ||
                      user.name ||
                      user.fullName ||
                      `Siswa ${studentId.substring(0, 8)}`,
                  };
                }
              } catch (error) {
                console.warn(`Failed to fetch user ${studentId}:`, error);
                return {
                  id: studentId,
                  name: `ID: ${
                    studentId.length > 10
                      ? studentId.substring(0, 8) + "..."
                      : studentId
                  }`,
                };
              }
            });

            const results = await Promise.all(individualFetches);
            const finalNameCache = { ...newNameCache };

            results.forEach((result) => {
              if (result) {
                finalNameCache[result.id] = result.name;
              }
            });

            setStudentNamesCache(finalNameCache);
            return finalNameCache;
          }

          return newNameCache;
        } catch (error) {
          console.error("Error fetching users:", error);

          // Fallback: Create default names for unknown IDs
          const fallbackCache = { ...studentNamesCache };
          uncachedIds.forEach((studentId) => {
            fallbackCache[studentId] = `ID: ${
              studentId.length > 10
                ? studentId.substring(0, 8) + "..."
                : studentId
            }`;
          });

          setStudentNamesCache(fallbackCache);
          return fallbackCache;
        }
      } catch (error) {
        console.error("Error in fetchStudentNames:", error);
        return studentNamesCache;
      }
    },
    [studentNamesCache]
  );

  // Function to get student name from cache
  const getStudentName = useCallback(
    (studentId) => {
      if (!studentId) return "Nama tidak tersedia";

      // Check cache first
      const cachedName = studentNamesCache[studentId];
      if (cachedName) {
        return cachedName;
      }

      // If not in cache and not currently loading, trigger fetch
      if (!studentNamesCache[studentId]) {
        fetchStudentNames([studentId]);
      }

      // Return a user-friendly loading state
      return "Memuat nama...";
    },
    [studentNamesCache, fetchStudentNames]
  );

  useEffect(() => {
    fetchUjianList();
    fetchHasilUjian();
  }, [fetchUjianList, fetchHasilUjian]);

  // Handle table pagination
  const handleTableChange = (paginationInfo) => {
    fetchHasilUjian(paginationInfo.current, paginationInfo.pageSize);
  };

  // Handle generate analysis for specific student
  const handleGenerateAnalysis = (pesertaId, ujianId) => {
    Modal.confirm({
      title: "Generate Analysis",
      icon: <ExclamationCircleOutlined />,
      content:
        "Apakah Anda yakin ingin generate ulang analisis untuk siswa ini?",
      okText: "Ya, Generate",
      cancelText: "Batal",
      onOk: async () => {
        setGenerating(true);
        try {
          // Call backend API to regenerate analysis using proper API function
          console.log("🔄 Generating analysis with params:", {
            idUjian: ujianId,
            pesertaId: pesertaId,
          });

          await generateAnalysis({
            idUjian: ujianId, // Use idUjian instead of ujianId
            pesertaId: pesertaId,
          });

          await fetchHasilUjian(pagination.current, pagination.pageSize);
          Modal.success({
            title: "Berhasil",
            content: "Analisis berhasil di-generate ulang!",
          });
        } catch (error) {
          console.error("Generate analysis error:", error);
          Modal.error({
            title: "Gagal",
            content: "Gagal generate analisis. Silakan coba lagi.",
          });
        } finally {
          setGenerating(false);
        }
      },
    });
  };
  // Handle view detail for ujian analysis - Show comprehensive HBase-style modal
  const handleViewDetail = async (record) => {
    setDetailLoading(true);
    setDetailModalVisible(true);

    try {
      // Fetch full analysis data for this ujian
      const result = await getAnalysisByUjian(record.ujianId, {
        page: 1,
        size: 1000,
      });

      let analysisData = null;
      if (result.data?.content && result.data.content.length > 0) {
        // Use deduplication logic to get the best record
        const records = result.data.content;
        if (records.length > 1) {
          // Sort by SELESAI status and latest timestamp
          const sortedRecords = records.sort((a, b) => {
            if (
              a.ujian?.statusUjian === "SELESAI" &&
              b.ujian?.statusUjian !== "SELESAI"
            )
              return -1;
            if (
              b.ujian?.statusUjian === "SELESAI" &&
              a.ujian?.statusUjian !== "SELESAI"
            )
              return 1;

            const aTime = new Date(a.updatedAt || a.generatedAt || 0);
            const bTime = new Date(b.updatedAt || b.generatedAt || 0);
            return bTime - aTime;
          });
          analysisData = sortedRecords[0];
        } else {
          analysisData = records[0];
        }
      }

      // Add ujian name to the analysis data for modal display
      if (analysisData) {
        analysisData.ujianNama =
          record.ujianNama || analysisData.ujian?.namaUjian;
      }
      setSelectedDetailData(analysisData);
    } catch (error) {
      console.error("Error fetching analysis detail:", error);
      message.error("Gagal memuat detail analisis");
    } finally {
      setDetailLoading(false);
    }
  };

  // Get risk level color
  const getRiskColor = (level) => {
    switch (level?.toUpperCase()) {
      case "HIGH":
        return "red";
      case "MEDIUM":
        return "orange";
      case "LOW":
        return "green";
      default:
        return "default";
    }
  }; // Table columns - Student focused
  const columns = [
    {
      title: "Siswa",
      dataIndex: "pesertaNama",
      key: "pesertaNama",
      render: (text, record) => (
        <div>
          <Text strong>{text || "Siswa"}</Text>
          <br />
          <Text type="secondary" style={{ fontSize: "12px" }}>
            {record.pesertaUsername} ({record.pesertaId})
          </Text>
        </div>
      ),
      filterable: true,
    },
    {
      title: "Kelas",
      dataIndex: "kelas",
      key: "kelas",
      render: (text) => <Tag color="blue">{text || "Tidak Diketahui"}</Tag>,
    },
    {
      title: "Ujian",
      dataIndex: "ujianNama",
      key: "ujianNama",
      render: (text, record) => (
        <div>
          <Text strong>{text || "Ujian"}</Text>
          <br />
          <Text type="secondary" style={{ fontSize: "12px" }}>
            Status: {record.statusPengerjaan}
          </Text>
        </div>
      ),
    },
    {
      title: "Nilai",
      key: "performance",
      render: (_, record) => (
        <Space direction="vertical" size="small">
          <div>
            <TrophyOutlined
              style={{
                color:
                  record.nilai >= 80
                    ? "green"
                    : record.nilai >= 60
                    ? "orange"
                    : "red",
              }}
            />{" "}
            <Text strong>{record.nilai}</Text> / 100
          </div>
          <div>
            <Text type="secondary">
              {record.jumlahBenar}B {record.jumlahSalah}S {record.jumlahKosong}K
            </Text>
          </div>
          <div>
            <Tag color={record.lulus ? "green" : "red"}>
              {record.lulus ? "LULUS" : "TIDAK LULUS"}
            </Tag>
          </div>
        </Space>
      ),
    },
    {
      title: "Waktu",
      key: "timing",
      render: (_, record) => (
        <Space direction="vertical" size="small">
          <div>
            <ClockCircleOutlined /> <Text>{record.durasi || 0} menit</Text>
          </div>
          <div>
            <Text type="secondary" style={{ fontSize: "12px" }}>
              {record.waktuMulai
                ? new Date(record.waktuMulai).toLocaleString("id-ID")
                : "Tidak ada data"}
            </Text>
          </div>
        </Space>
      ),
    },
    {
      title: "Pelanggaran",
      dataIndex: "violationCount",
      key: "violations",
      render: (count, record) => (
        <Space direction="vertical" size="small">
          <Tag color={count === 0 ? "green" : count <= 2 ? "orange" : "red"}>
            {count} pelanggaran
          </Tag>
          <div>
            <Text type="secondary">Integritas: {record.integrityScore}%</Text>
          </div>
        </Space>
      ),
    },
    {
      title: "Tingkat Risiko",
      dataIndex: "riskLevel",
      key: "riskLevel",
      render: (risk, record) => (
        <Space direction="vertical" size="small">
          <Tag color={getRiskColor(risk)}>{risk || "LOW"}</Tag>
          <div>
            <Text type="secondary">{record.confidenceLevel}</Text>
          </div>
        </Space>
      ),
    },
    {
      title: "Status Review",
      dataIndex: "needsReview",
      key: "needsReview",
      render: (needsReview) => (
        <Tag color={needsReview ? "red" : "green"}>
          {needsReview ? "Perlu Review" : "Normal"}
        </Tag>
      ),
    },
    {
      title: "Aksi",
      key: "action",
      render: (_, record) => (
        <Space>
          <Tooltip title="Lihat Detail Siswa">
            <Button
              type="primary"
              icon={<EyeOutlined />}
              size="small"
              onClick={() => handleViewDetail(record)}
            />
          </Tooltip>
          <Tooltip title="Generate Ulang Analisis">
            <Button
              icon={<BarChartOutlined />}
              size="small"
              loading={generating}
              onClick={() =>
                handleGenerateAnalysis(record.pesertaId, record.ujianId)
              }
            />
          </Tooltip>
        </Space>
      ),
    },
  ];

  // Calculate summary statistics from student results
  const summaryStats = {
    totalSiswa: siswaData.length,
    avgScore:
      siswaData.length > 0
        ? parseFloat(
            (
              siswaData.reduce((sum, item) => sum + (item.nilai || 0), 0) /
              siswaData.length
            ).toFixed(1)
          )
        : 0,
    totalViolations: siswaData.reduce(
      (sum, item) => sum + (item.violationCount || 0),
      0
    ),
    highRiskStudents: siswaData.filter((item) => item.riskLevel === "HIGH")
      .length,
    needsReview: siswaData.filter((item) => item.needsReview).length,
    avgIntegrityScore:
      siswaData.length > 0
        ? parseFloat(
            (
              siswaData.reduce(
                (sum, item) => sum + (item.integrityScore || 0),
                0
              ) / siswaData.length
            ).toFixed(1)
          )
        : 0,
    lulusCount: siswaData.filter((item) => item.lulus).length,
  };

  // Effect to fetch student names when modal opens with studyRecommendations
  useEffect(() => {
    if (selectedDetailData?.studyRecommendations && detailModalVisible) {
      const studentIds = Object.keys(selectedDetailData.studyRecommendations);
      if (studentIds.length > 0) {
        console.log(
          "🎓 Fetching student names for study recommendations:",
          studentIds
        );
        fetchStudentNames(studentIds);
      }
    }
  }, [selectedDetailData, detailModalVisible, fetchStudentNames]);

  // Render detailed analysis modal in modern style
  const renderDetailModal = () => {
    if (!selectedDetailData) return null;

    const data = selectedDetailData;

    // Calculate additional metrics for better insights
    const totalQuestions = Object.keys(data.itemAnalysis || {}).length;
    const easyQuestions = Object.values(data.itemAnalysis || {}).filter(
      (q) => q.difficultyLevel === "EASY"
    ).length;
    const hardQuestions = Object.values(data.itemAnalysis || {}).filter(
      (q) => q.difficultyLevel === "HARD"
    ).length;
    const veryHardQuestions = Object.values(data.itemAnalysis || {}).filter(
      (q) => q.difficultyLevel === "VERY_HARD"
    ).length;
    const goodQuestions = Object.values(data.itemAnalysis || {}).filter((q) =>
      q.recommendation?.includes("GOOD")
    ).length;

    const avgDifficultyIndex =
      totalQuestions > 0
        ? Object.values(data.questionDifficulty || {}).reduce(
            (sum, val) => sum + val,
            0
          ) / totalQuestions
        : 0;

    const securityStatus =
      data.integrityScore >= 0.9
        ? "AMAN"
        : data.integrityScore >= 0.7
        ? "PERLU_PERHATIAN"
        : "RISIKO_TINGGI";

    // Helper function untuk difficulty level color
    const getDifficultyColor = (level) => {
      const colors = {
        EASY: "green",
        HARD: "orange",
        VERY_HARD: "red",
      };
      return colors[level] || "default";
    };

    // Helper function untuk recommendation color
    const getRecommendationColor = (recommendation) => {
      if (recommendation?.includes("GOOD")) return "green";
      if (recommendation?.includes("REVIEW")) return "orange";
      return "default";
    };

    return (
      <Modal
        title={
          <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
            <BarChartOutlined style={{ color: "#1890ff" }} />
            <span>📊 Analisis Komprehensif Ujian</span>
          </div>
        }
        open={detailModalVisible}
        onCancel={() => {
          setDetailModalVisible(false);
          setSelectedDetailData(null);
        }}
        footer={[
          <Button
            key="export"
            type="primary"
            icon={<DownloadOutlined />}
            onClick={() => exportToPDF(data)}
          >
            Export PDF
          </Button>,
          <Button
            key="close"
            onClick={() => {
              setDetailModalVisible(false);
              setSelectedDetailData(null);
            }}
          >
            Tutup
          </Button>,
        ]}
        width={1200}
        style={{ top: 20 }}
        bodyStyle={{ maxHeight: "80vh", overflow: "auto" }}
      >
        <Spin spinning={detailLoading}>
          {/* Header Information */}
          <Card
            size="small"
            style={{
              marginBottom: "16px",
              background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
              color: "white",
            }}
          >
            <Row gutter={[16, 8]}>
              <Col xs={24} sm={6}>
                <Statistic
                  title={
                    <span style={{ color: "rgba(255,255,255,0.85)" }}>
                      Ujian
                    </span>
                  }
                  value={data.ujianNama || "N/A"}
                  valueStyle={{ color: "#fff", fontSize: "16px" }}
                />
              </Col>
              <Col xs={24} sm={6}>
                <Statistic
                  title={
                    <span style={{ color: "rgba(255,255,255,0.85)" }}>
                      Total Peserta
                    </span>
                  }
                  value={data.totalParticipants || 0}
                  valueStyle={{ color: "#fff" }}
                  prefix={<UserOutlined />}
                />
              </Col>
              <Col xs={24} sm={6}>
                <Statistic
                  title={
                    <span style={{ color: "rgba(255,255,255,0.85)" }}>
                      Rata-rata Nilai
                    </span>
                  }
                  value={data.averageScore || 0}
                  precision={1}
                  valueStyle={{ color: "#fff" }}
                  prefix={<TrophyOutlined />}
                />
              </Col>
              <Col xs={24} sm={6}>
                <Statistic
                  title={
                    <span style={{ color: "rgba(255,255,255,0.85)" }}>
                      Tingkat Kelulusan
                    </span>
                  }
                  value={data.passRate || 0}
                  precision={1}
                  suffix="%"
                  valueStyle={{ color: "#fff" }}
                  prefix={<CheckCircleOutlined />}
                />
              </Col>
            </Row>
          </Card>

          <Tabs defaultActiveKey="executive" type="card">
            {/* Tab Executive Summary */}
            <Tabs.TabPane
              tab={
                <span>
                  <TrophyOutlined />
                  Executive Summary
                </span>
              }
              key="executive"
            >
              <Row gutter={[16, 16]}>
                {/* Performance Overview */}
                <Col span={24}>
                  <Card
                    title="🎯 Ringkasan Eksekutif"
                    size="small"
                    extra={
                      <Tag
                        color={
                          data.averageScore >= 80
                            ? "green"
                            : data.averageScore >= 60
                            ? "orange"
                            : "red"
                        }
                      >
                        {data.averageScore >= 80
                          ? "EXCELLENT"
                          : data.averageScore >= 60
                          ? "GOOD"
                          : "NEEDS IMPROVEMENT"}
                      </Tag>
                    }
                  >
                    <Row gutter={[16, 16]}>
                      <Col xs={24} sm={6}>
                        <Statistic
                          title="Performa Keseluruhan"
                          value={data.averageScore || 0}
                          precision={1}
                          suffix="/100"
                          valueStyle={{
                            color:
                              data.averageScore >= 75
                                ? "#3f8600"
                                : data.averageScore >= 60
                                ? "#fa8c16"
                                : "#cf1322",
                          }}
                        />
                      </Col>
                      <Col xs={24} sm={6}>
                        <Statistic
                          title="Tingkat Kelulusan"
                          value={data.passRate || 0}
                          precision={1}
                          suffix="%"
                          valueStyle={{
                            color:
                              data.passRate >= 80
                                ? "#3f8600"
                                : data.passRate >= 60
                                ? "#fa8c16"
                                : "#cf1322",
                          }}
                        />
                      </Col>
                      <Col xs={24} sm={6}>
                        <Statistic
                          title="Indeks Kesulitan Soal"
                          value={avgDifficultyIndex * 100 || 0}
                          precision={1}
                          suffix="%"
                          valueStyle={{ color: "#1890ff" }}
                        />
                      </Col>
                      <Col xs={24} sm={6}>
                        <Statistic
                          title="Status Keamanan"
                          value={securityStatus}
                          valueStyle={{
                            color:
                              securityStatus === "AMAN"
                                ? "#3f8600"
                                : securityStatus === "PERLU_PERHATIAN"
                                ? "#fa8c16"
                                : "#cf1322",
                          }}
                        />
                      </Col>
                    </Row>
                  </Card>
                </Col>

                {/* Key Insights */}
                <Col xs={24} lg={12}>
                  <Card title="📊 Insight Utama" size="small">
                    <List
                      size="small"
                      dataSource={[
                        {
                          icon: "🎯",
                          title: "Performa Siswa",
                          content: `${
                            data.totalParticipants
                          } siswa mengikuti ujian dengan rata-rata ${data.averageScore?.toFixed(
                            1
                          )} poin`,
                        },
                        {
                          icon: "📚",
                          title: "Kualitas Soal",
                          content: `${goodQuestions}/${totalQuestions} soal berkualitas baik (${(
                            (goodQuestions / totalQuestions) *
                            100
                          ).toFixed(1)}%)`,
                        },
                        {
                          icon: "⏱️",
                          title: "Efisiensi Waktu",
                          content: `Rata-rata pengerjaan ${data.averageCompletionTime?.toFixed(
                            1
                          )} menit`,
                        },
                        {
                          icon: "🔒",
                          title: "Integritas Ujian",
                          content: `${
                            data.suspiciousSubmissions || 0
                          } pelanggaran terdeteksi pada ${
                            data.flaggedParticipants || 0
                          } siswa`,
                        },
                      ]}
                      renderItem={(item) => (
                        <List.Item>
                          <div
                            style={{
                              display: "flex",
                              alignItems: "center",
                              gap: "12px",
                            }}
                          >
                            <span style={{ fontSize: "20px" }}>
                              {item.icon}
                            </span>
                            <div>
                              <Text strong>{item.title}</Text>
                              <br />
                              <Text type="secondary">{item.content}</Text>
                            </div>
                          </div>
                        </List.Item>
                      )}
                    />
                  </Card>
                </Col>

                {/* Action Items */}
                <Col xs={24} lg={12}>
                  <Card title="⚡ Rekomendasi Tindakan" size="small">
                    <List
                      size="small"
                      dataSource={[
                        ...(data.recommendations?.slice(0, 2) || []).map(
                          (rec, index) => ({
                            type: "recommendation",
                            priority: index === 0 ? "HIGH" : "MEDIUM",
                            content: rec,
                          })
                        ),
                        ...(data.improvementSuggestions?.slice(0, 2) || []).map(
                          (suggestion) => ({
                            type: "improvement",
                            priority: "MEDIUM",
                            content: suggestion,
                          })
                        ),
                      ].slice(0, 4)}
                      renderItem={(item) => (
                        <List.Item>
                          <div
                            style={{
                              display: "flex",
                              alignItems: "center",
                              gap: "8px",
                            }}
                          >
                            <Tag
                              color={
                                item.priority === "HIGH" ? "red" : "orange"
                              }
                            >
                              {item.priority}
                            </Tag>
                            <Text style={{ fontSize: "13px" }}>
                              {item.content}
                            </Text>
                          </div>
                        </List.Item>
                      )}
                    />
                  </Card>
                </Col>

                {/* Quick Stats Grid */}
                <Col span={24}>
                  <Card title="📈 Statistik Cepat" size="small">
                    <Row gutter={[16, 8]}>
                      <Col xs={12} sm={6}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "12px",
                            background: "#f0f9ff",
                            borderRadius: "6px",
                          }}
                        >
                          <Text
                            strong
                            style={{ color: "#1890ff", fontSize: "18px" }}
                          >
                            {easyQuestions}
                          </Text>
                          <br />
                          <Text type="secondary" style={{ fontSize: "12px" }}>
                            Soal Mudah
                          </Text>
                        </div>
                      </Col>
                      <Col xs={12} sm={6}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "12px",
                            background: "#fff7e6",
                            borderRadius: "6px",
                          }}
                        >
                          <Text
                            strong
                            style={{ color: "#fa8c16", fontSize: "18px" }}
                          >
                            {hardQuestions}
                          </Text>
                          <br />
                          <Text type="secondary" style={{ fontSize: "12px" }}>
                            Soal Sulit
                          </Text>
                        </div>
                      </Col>
                      <Col xs={12} sm={6}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "12px",
                            background: "#fff2f0",
                            borderRadius: "6px",
                          }}
                        >
                          <Text
                            strong
                            style={{ color: "#ff4d4f", fontSize: "18px" }}
                          >
                            {veryHardQuestions}
                          </Text>
                          <br />
                          <Text type="secondary" style={{ fontSize: "12px" }}>
                            Sangat Sulit
                          </Text>
                        </div>
                      </Col>
                      <Col xs={12} sm={6}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "12px",
                            background: "#f6ffed",
                            borderRadius: "6px",
                          }}
                        >
                          <Text
                            strong
                            style={{ color: "#52c41a", fontSize: "18px" }}
                          >
                            {data.passedCount}
                          </Text>
                          <br />
                          <Text type="secondary" style={{ fontSize: "12px" }}>
                            Siswa Lulus
                          </Text>
                        </div>
                      </Col>
                    </Row>
                  </Card>
                </Col>
              </Row>
            </Tabs.TabPane>

            {/* Tab Detailed Overview */}
            <Tabs.TabPane
              tab={
                <span>
                  <BarChartOutlined />
                  Analisis Detil
                </span>
              }
              key="overview"
            >
              <Row gutter={[16, 16]}>
                {/* Statistik Deskriptif */}
                <Col xs={24} lg={12}>
                  <Card title="📈 Statistik Deskriptif" size="small">
                    <Row gutter={[8, 16]}>
                      <Col span={12}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "12px",
                            background: "#f0f9ff",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "20px", color: "#1890ff" }}
                          >
                            {data.medianScore?.toFixed(1) || 0}
                          </Text>
                          <br />
                          <Text type="secondary">Median</Text>
                        </div>
                      </Col>
                      <Col span={12}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "12px",
                            background: "#f6ffed",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "20px", color: "#52c41a" }}
                          >
                            {data.standardDeviation?.toFixed(1) || 0}
                          </Text>
                          <br />
                          <Text type="secondary">Std. Deviasi</Text>
                        </div>
                      </Col>
                      <Col span={12}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "12px",
                            background: "#fff2f0",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "20px", color: "#ff4d4f" }}
                          >
                            {data.lowestScore?.toFixed(1) || 0}
                          </Text>
                          <br />
                          <Text type="secondary">Nilai Terendah</Text>
                        </div>
                      </Col>
                      <Col span={12}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "12px",
                            background: "#f9f0ff",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "20px", color: "#722ed1" }}
                          >
                            {data.highestScore?.toFixed(1) || 0}
                          </Text>
                          <br />
                          <Text type="secondary">Nilai Tertinggi</Text>
                        </div>
                      </Col>
                    </Row>

                    {/* Distribusi Grade */}
                    {data.gradeDistribution &&
                      Object.keys(data.gradeDistribution).length > 0 && (
                        <div style={{ marginTop: "20px" }}>
                          <Text
                            strong
                            style={{ marginBottom: "12px", display: "block" }}
                          >
                            📊 Distribusi Nilai:
                          </Text>
                          {Object.entries(data.gradeDistribution).map(
                            ([grade, count]) => (
                              <div key={grade} style={{ marginBottom: "12px" }}>
                                <Row align="middle" gutter={8}>
                                  <Col span={3}>
                                    <Tag
                                      color={getGradeColor(grade)}
                                      style={{
                                        fontSize: "14px",
                                        fontWeight: "bold",
                                      }}
                                    >
                                      {grade}
                                    </Tag>
                                  </Col>
                                  <Col span={13}>
                                    <Progress
                                      percent={
                                        data.gradePercentages?.[grade] || 0
                                      }
                                      size="small"
                                      showInfo={false}
                                      strokeColor={getGradeColor(grade)}
                                    />
                                  </Col>
                                  <Col span={8}>
                                    <Text strong>
                                      {count} siswa (
                                      {data.gradePercentages?.[grade]?.toFixed(
                                        1
                                      ) || 0}
                                      %)
                                    </Text>
                                  </Col>
                                </Row>
                              </div>
                            )
                          )}
                        </div>
                      )}
                  </Card>
                </Col>

                {/* Performance by Kelas */}
                <Col xs={24} lg={12}>
                  <Card title="🏫 Performa per Kelas" size="small">
                    {data.performanceByKelas &&
                    Object.keys(data.performanceByKelas).length > 0 ? (
                      Object.entries(data.performanceByKelas).map(
                        ([kelas, klasData]) => (
                          <Card
                            key={kelas}
                            size="small"
                            style={{
                              marginBottom: "12px",
                              border: "1px solid #d9d9d9",
                              borderRadius: "8px",
                            }}
                          >
                            <div
                              style={{
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                                marginBottom: "8px",
                              }}
                            >
                              <Text strong style={{ fontSize: "16px" }}>
                                Kelas {kelas}
                              </Text>
                              <Tag color="blue">
                                {klasData.participantCount} siswa
                              </Tag>
                            </div>
                            <Row gutter={[8, 8]}>
                              <Col span={12}>
                                <div
                                  style={{
                                    textAlign: "center",
                                    padding: "8px",
                                    background: "#f0f9ff",
                                    borderRadius: "6px",
                                  }}
                                >
                                  <Text strong style={{ color: "#1890ff" }}>
                                    {klasData.averageScore?.toFixed(1) || 0}
                                  </Text>
                                  <br />
                                  <Text
                                    type="secondary"
                                    style={{ fontSize: "12px" }}
                                  >
                                    Rata-rata
                                  </Text>
                                </div>
                              </Col>
                              <Col span={12}>
                                <div
                                  style={{
                                    textAlign: "center",
                                    padding: "8px",
                                    background: "#f6ffed",
                                    borderRadius: "6px",
                                  }}
                                >
                                  <Text strong style={{ color: "#52c41a" }}>
                                    {klasData.passRate?.toFixed(1) || 0}%
                                  </Text>
                                  <br />
                                  <Text
                                    type="secondary"
                                    style={{ fontSize: "12px" }}
                                  >
                                    Kelulusan
                                  </Text>
                                </div>
                              </Col>
                              <Col span={12}>
                                <div
                                  style={{
                                    textAlign: "center",
                                    padding: "8px",
                                    background: "#fff7e6",
                                    borderRadius: "6px",
                                  }}
                                >
                                  <Text strong style={{ color: "#fa8c16" }}>
                                    {klasData.highestScore?.toFixed(1) || 0}
                                  </Text>
                                  <br />
                                  <Text
                                    type="secondary"
                                    style={{ fontSize: "12px" }}
                                  >
                                    Tertinggi
                                  </Text>
                                </div>
                              </Col>
                              <Col span={12}>
                                <div
                                  style={{
                                    textAlign: "center",
                                    padding: "8px",
                                    background: "#fff2f0",
                                    borderRadius: "6px",
                                  }}
                                >
                                  <Text strong style={{ color: "#ff4d4f" }}>
                                    {klasData.lowestScore?.toFixed(1) || 0}
                                  </Text>
                                  <br />
                                  <Text
                                    type="secondary"
                                    style={{ fontSize: "12px" }}
                                  >
                                    Terendah
                                  </Text>
                                </div>
                              </Col>
                            </Row>
                          </Card>
                        )
                      )
                    ) : (
                      <div style={{ textAlign: "center", padding: "40px" }}>
                        <Text type="secondary">
                          Data performa kelas tidak tersedia
                        </Text>
                      </div>
                    )}
                  </Card>
                </Col>
              </Row>

              {/* Analisis Waktu */}
              <Row gutter={[16, 16]} style={{ marginTop: "16px" }}>
                <Col span={24}>
                  <Card title="⏱️ Analisis Waktu Pengerjaan" size="small">
                    <Row gutter={[16, 8]}>
                      <Col xs={24} sm={8}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "16px",
                            background: "#e6f7ff",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "20px", color: "#1890ff" }}
                          >
                            {data.averageCompletionTime?.toFixed(1) || 0} menit
                          </Text>
                          <br />
                          <Text type="secondary">Rata-rata Waktu</Text>
                        </div>
                      </Col>
                      <Col xs={24} sm={8}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "16px",
                            background: "#f6ffed",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "20px", color: "#52c41a" }}
                          >
                            {data.shortestCompletionTime?.toFixed(1) || 0} menit
                          </Text>
                          <br />
                          <Text type="secondary">Tercepat</Text>
                        </div>
                      </Col>
                      <Col xs={24} sm={8}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "16px",
                            background: "#fff2f0",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "20px", color: "#ff4d4f" }}
                          >
                            {data.longestCompletionTime?.toFixed(1) || 0} menit
                          </Text>
                          <br />
                          <Text type="secondary">Terlama</Text>
                        </div>
                      </Col>
                    </Row>
                  </Card>
                </Col>
              </Row>
            </Tabs.TabPane>

            {/* Tab Item Analysis */}
            <Tabs.TabPane
              tab={
                <span>
                  <BookOutlined />
                  Analisis Soal
                </span>
              }
              key="item-analysis"
            >
              <Row gutter={[16, 16]}>
                {/* Summary Item Analysis */}
                <Col span={24}>
                  <Card title="📊 Ringkasan Analisis Soal" size="small">
                    <Row gutter={[16, 8]}>
                      <Col xs={24} sm={6}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "16px",
                            background: "#f0f9ff",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "24px", color: "#1890ff" }}
                          >
                            {Object.keys(data.itemAnalysis || {}).length}
                          </Text>
                          <br />
                          <Text type="secondary">Total Soal</Text>
                        </div>
                      </Col>
                      <Col xs={24} sm={6}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "16px",
                            background: "#f6ffed",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "24px", color: "#52c41a" }}
                          >
                            {
                              Object.values(data.itemAnalysis || {}).filter(
                                (item) => item.recommendation?.includes("GOOD")
                              ).length
                            }
                          </Text>
                          <br />
                          <Text type="secondary">Soal Baik</Text>
                        </div>
                      </Col>
                      <Col xs={24} sm={6}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "16px",
                            background: "#fff7e6",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "24px", color: "#fa8c16" }}
                          >
                            {
                              Object.values(data.itemAnalysis || {}).filter(
                                (item) =>
                                  item.recommendation?.includes("REVIEW")
                              ).length
                            }
                          </Text>
                          <br />
                          <Text type="secondary">Perlu Review</Text>
                        </div>
                      </Col>
                      <Col xs={24} sm={6}>
                        <div
                          style={{
                            textAlign: "center",
                            padding: "16px",
                            background: "#fff2f0",
                            borderRadius: "8px",
                          }}
                        >
                          <Text
                            strong
                            style={{ fontSize: "24px", color: "#ff4d4f" }}
                          >
                            {
                              Object.values(data.itemAnalysis || {}).filter(
                                (item) => item.difficultyLevel === "VERY_HARD"
                              ).length
                            }
                          </Text>
                          <br />
                          <Text type="secondary">Sangat Sulit</Text>
                        </div>
                      </Col>
                    </Row>
                  </Card>
                </Col>

                {/* Soal Termudah & Tersulit */}
                <Col xs={24} lg={12}>
                  <Card title="✅ Soal Termudah" size="small">
                    {data.easiestQuestions &&
                    data.easiestQuestions.length > 0 ? (
                      <List
                        size="small"
                        dataSource={data.easiestQuestions.slice(0, 5)}
                        renderItem={(questionId, index) => {
                          const questionData = data.itemAnalysis?.[questionId];
                          return (
                            <List.Item>
                              <div style={{ width: "100%" }}>
                                <div
                                  style={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: "8px",
                                    marginBottom: "4px",
                                  }}
                                >
                                  <Badge
                                    count={index + 1}
                                    style={{ backgroundColor: "#52c41a" }}
                                  />
                                  <Tag color="green">MUDAH</Tag>
                                  <Text
                                    type="secondary"
                                    style={{ fontSize: "12px" }}
                                  >
                                    {(
                                      data.questionDifficulty?.[questionId] *
                                        100 || 0
                                    ).toFixed(1)}
                                    % benar
                                  </Text>
                                </div>
                                <Text ellipsis style={{ fontSize: "13px" }}>
                                  {questionData?.pertanyaan?.substring(0, 80)}
                                  ...
                                </Text>
                              </div>
                            </List.Item>
                          );
                        }}
                      />
                    ) : (
                      <div style={{ textAlign: "center", padding: "20px" }}>
                        <Text type="secondary">
                          Tidak ada data soal termudah
                        </Text>
                      </div>
                    )}
                  </Card>
                </Col>

                <Col xs={24} lg={12}>
                  <Card title="❌ Soal Tersulit" size="small">
                    {data.hardestQuestions &&
                    data.hardestQuestions.length > 0 ? (
                      <List
                        size="small"
                        dataSource={data.hardestQuestions.slice(0, 5)}
                        renderItem={(questionId, index) => {
                          const questionData = data.itemAnalysis?.[questionId];
                          return (
                            <List.Item>
                              <div style={{ width: "100%" }}>
                                <div
                                  style={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: "8px",
                                    marginBottom: "4px",
                                  }}
                                >
                                  <Badge
                                    count={index + 1}
                                    style={{ backgroundColor: "#ff4d4f" }}
                                  />
                                  <Tag color="red">SULIT</Tag>
                                  <Text
                                    type="secondary"
                                    style={{ fontSize: "12px" }}
                                  >
                                    {(
                                      data.questionDifficulty?.[questionId] *
                                        100 || 0
                                    ).toFixed(1)}
                                    % benar
                                  </Text>
                                </div>
                                <Text ellipsis style={{ fontSize: "13px" }}>
                                  {questionData?.pertanyaan?.substring(0, 80)}
                                  ...
                                </Text>
                              </div>
                            </List.Item>
                          );
                        }}
                      />
                    ) : (
                      <div style={{ textAlign: "center", padding: "20px" }}>
                        <Text type="secondary">
                          Tidak ada data soal tersulit
                        </Text>
                      </div>
                    )}
                  </Card>
                </Col>

                {/* Detailed Item Analysis Table */}
                <Col span={24}>
                  <Card title="📝 Detail Analisis per Soal" size="small">
                    <Table
                      dataSource={Object.entries(data.itemAnalysis || {}).map(
                        ([id, item], index) => ({
                          key: id,
                          no: index + 1,
                          id,
                          ...item,
                        })
                      )}
                      columns={[
                        {
                          title: "No",
                          dataIndex: "no",
                          key: "no",
                          width: 50,
                          align: "center",
                        },
                        {
                          title: "Pertanyaan",
                          dataIndex: "pertanyaan",
                          key: "pertanyaan",
                          ellipsis: true,
                          width: 250,
                          render: (text) => (
                            <Tooltip title={text}>
                              <Text>{text?.substring(0, 50)}...</Text>
                            </Tooltip>
                          ),
                        },
                        {
                          title: "Jenis",
                          dataIndex: "jenisSoal",
                          key: "jenisSoal",
                          width: 80,
                          render: (type) => <Tag color="blue">{type}</Tag>,
                        },
                        {
                          title: "Respons",
                          key: "responses",
                          width: 100,
                          render: (_, record) => (
                            <div style={{ textAlign: "center" }}>
                              <Text strong>{record.totalResponses}</Text>
                              <br />
                              <Text type="success">
                                {record.correctResponses} benar
                              </Text>
                            </div>
                          ),
                        },
                        {
                          title: "Tingkat Kesulitan",
                          dataIndex: "difficultyLevel",
                          key: "difficultyLevel",
                          width: 120,
                          render: (level, record) => (
                            <div style={{ textAlign: "center" }}>
                              <Tag color={getDifficultyColor(level)}>
                                {level}
                              </Tag>
                              <br />
                              <Text type="secondary">
                                {((record.difficultyIndex || 0) * 100).toFixed(
                                  1
                                )}
                                %
                              </Text>
                            </div>
                          ),
                        },
                        {
                          title: "Persentase Benar",
                          dataIndex: "correctPercentage",
                          key: "correctPercentage",
                          width: 120,
                          render: (percentage) => (
                            <div style={{ textAlign: "center" }}>
                              <Progress
                                type="circle"
                                percent={percentage || 0}
                                width={50}
                                strokeColor={
                                  percentage >= 75
                                    ? "#52c41a"
                                    : percentage >= 50
                                    ? "#faad14"
                                    : "#ff4d4f"
                                }
                              />
                            </div>
                          ),
                        },
                        {
                          title: "Rekomendasi",
                          dataIndex: "recommendation",
                          key: "recommendation",
                          width: 150,
                          render: (rec) => (
                            <Tag color={getRecommendationColor(rec)}>
                              {rec?.split(" - ")[0] || "N/A"}
                            </Tag>
                          ),
                        },
                      ]}
                      size="small"
                      pagination={{
                        pageSize: 8,
                        showSizeChanger: true,
                        showTotal: (total, range) =>
                          `${range[0]}-${range[1]} dari ${total} soal`,
                      }}
                      scroll={{ x: 900 }}
                    />
                  </Card>
                </Col>
              </Row>
            </Tabs.TabPane>

            {/* Tab Security Analysis */}
            <Tabs.TabPane
              tab={
                <span>
                  <SecurityScanOutlined />
                  Keamanan Ujian
                </span>
              }
              key="security"
            >
              <Row gutter={[16, 16]}>
                {/* Security Overview */}
                <Col span={24}>
                  <Alert
                    message={
                      data.integrityScore >= 0.9
                        ? "🛡️ Ujian Aman - Tidak ada indikasi kecurangan signifikan"
                        : data.integrityScore >= 0.7
                        ? "⚠️ Perlu Perhatian - Ada beberapa aktivitas mencurigakan"
                        : "🚨 Risiko Tinggi - Terdeteksi banyak pelanggaran"
                    }
                    type={
                      data.integrityScore >= 0.9
                        ? "success"
                        : data.integrityScore >= 0.7
                        ? "warning"
                        : "error"
                    }
                    showIcon
                    style={{ marginBottom: "16px" }}
                  />
                </Col>

                {/* Security Metrics */}
                <Col xs={24} lg={8}>
                  <Card title="🔒 Metrik Keamanan" size="small">
                    <Space
                      direction="vertical"
                      style={{ width: "100%" }}
                      size="middle"
                    >
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                          alignItems: "center",
                        }}
                      >
                        <Text>
                          <strong>Skor Integritas:</strong>
                        </Text>
                        <Progress
                          percent={(data.integrityScore || 0) * 100}
                          size="small"
                          strokeColor={
                            data.integrityScore >= 0.9
                              ? "#52c41a"
                              : data.integrityScore >= 0.7
                              ? "#faad14"
                              : "#ff4d4f"
                          }
                        />
                      </div>
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Text>
                          <strong>Total Pelanggaran:</strong>
                        </Text>
                        <Tag color="red" style={{ fontSize: "14px" }}>
                          {data.suspiciousSubmissions || 0}
                        </Tag>
                      </div>
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Text>
                          <strong>Peserta Ter-flag:</strong>
                        </Text>
                        <Tag color="orange" style={{ fontSize: "14px" }}>
                          {data.flaggedParticipants || 0}
                        </Tag>
                      </div>
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Text>
                          <strong>ID Pelanggaran:</strong>
                        </Text>
                        <Tag color="red">
                          {data.violationIds?.length || 0} ID
                        </Tag>
                      </div>
                    </Space>
                  </Card>
                </Col>

                {/* Violation Details */}
                <Col xs={24} lg={16}>
                  <Card title="🚨 Detail Pelanggaran" size="small">
                    {data.cheatDetections && data.cheatDetections.length > 0 ? (
                      <Table
                        dataSource={data.cheatDetections}
                        rowKey="idDetection"
                        size="small"
                        columns={[
                          {
                            title: "ID Peserta",
                            dataIndex: "idPeserta",
                            key: "idPeserta",
                            width: 120,
                            render: (id) => <Text code>{id}</Text>,
                          },
                          {
                            title: "Jenis Pelanggaran",
                            dataIndex: "typeViolation",
                            key: "typeViolation",
                            render: (type) => (
                              <Tag color="red" style={{ fontSize: "11px" }}>
                                {type?.replace("_", " ")}
                              </Tag>
                            ),
                          },
                          {
                            title: "Tingkat",
                            dataIndex: "severity",
                            key: "severity",
                            render: (sev) => (
                              <Tag
                                color={
                                  sev === "CRITICAL"
                                    ? "red"
                                    : sev === "HIGH"
                                    ? "orange"
                                    : sev === "MEDIUM"
                                    ? "blue"
                                    : "default"
                                }
                              >
                                {sev}
                              </Tag>
                            ),
                          },
                          {
                            title: "Waktu",
                            dataIndex: "detectedAt",
                            key: "detectedAt",
                            render: (time) =>
                              time
                                ? new Date(time).toLocaleString("id-ID", {
                                    day: "2-digit",
                                    month: "2-digit",
                                    year: "numeric",
                                    hour: "2-digit",
                                    minute: "2-digit",
                                  })
                                : "-",
                            width: 130,
                          },
                          {
                            title: "Status",
                            dataIndex: "resolved",
                            key: "resolved",
                            render: (resolved) => (
                              <Tag color={resolved ? "green" : "red"}>
                                {resolved ? "Resolved" : "Pending"}
                              </Tag>
                            ),
                          },
                        ]}
                        pagination={{ pageSize: 8 }}
                        locale={{
                          emptyText: "Tidak ada pelanggaran terdeteksi",
                        }}
                      />
                    ) : (
                      <div style={{ textAlign: "center", padding: "40px" }}>
                        <CheckCircleOutlined
                          style={{ fontSize: "48px", color: "#52c41a" }}
                        />
                        <br />
                        <Text type="success" style={{ fontSize: "16px" }}>
                          Tidak ada pelanggaran terdeteksi
                        </Text>
                      </div>
                    )}
                  </Card>
                </Col>
              </Row>
            </Tabs.TabPane>

            {/* Tab Recommendations */}
            <Tabs.TabPane
              tab={
                <span>
                  <BulbOutlined />
                  Rekomendasi
                </span>
              }
              key="recommendations"
            >
              <Row gutter={[16, 16]}>
                <Col xs={24} lg={12}>
                  <Card title="💡 Rekomendasi Umum" size="small">
                    {data.recommendations && data.recommendations.length > 0 ? (
                      <List
                        size="small"
                        dataSource={data.recommendations}
                        renderItem={(rec, index) => (
                          <List.Item style={{ border: "none", paddingLeft: 0 }}>
                            <div
                              style={{
                                display: "flex",
                                alignItems: "flex-start",
                                gap: "12px",
                              }}
                            >
                              <Badge
                                count={index + 1}
                                style={{ backgroundColor: "#1890ff" }}
                              />
                              <Text>{rec}</Text>
                            </div>
                          </List.Item>
                        )}
                      />
                    ) : (
                      <div style={{ textAlign: "center", padding: "40px" }}>
                        <BulbOutlined
                          style={{ fontSize: "48px", color: "#faad14" }}
                        />
                        <br />
                        <Text type="secondary">
                          Tidak ada rekomendasi khusus tersedia.
                        </Text>
                      </div>
                    )}
                  </Card>
                </Col>

                <Col xs={24} lg={12}>
                  <Card title="🔧 Saran Perbaikan" size="small">
                    {data.improvementSuggestions &&
                    data.improvementSuggestions.length > 0 ? (
                      <List
                        size="small"
                        dataSource={data.improvementSuggestions}
                        renderItem={(suggestion, index) => (
                          <List.Item style={{ border: "none", paddingLeft: 0 }}>
                            <div
                              style={{
                                display: "flex",
                                alignItems: "flex-start",
                                gap: "12px",
                              }}
                            >
                              <Badge
                                count={index + 1}
                                style={{ backgroundColor: "#52c41a" }}
                              />
                              <Text>{suggestion}</Text>
                            </div>
                          </List.Item>
                        )}
                      />
                    ) : (
                      <div style={{ textAlign: "center", padding: "40px" }}>
                        <CheckCircleOutlined
                          style={{ fontSize: "48px", color: "#52c41a" }}
                        />
                        <br />
                        <Text type="secondary">
                          Tidak ada saran perbaikan khusus.
                        </Text>
                      </div>
                    )}
                  </Card>
                </Col>

                {/* Learning Gaps */}
                {data.learningGaps &&
                  Object.keys(data.learningGaps).length > 0 && (
                    <Col span={24}>
                      <Card
                        title="📚 Gap Pembelajaran yang Teridentifikasi"
                        size="small"
                      >
                        <Row gutter={[8, 8]}>
                          {Object.entries(data.learningGaps).map(
                            ([topik, deskripsi]) => (
                              <Col xs={24} sm={12} lg={8} key={topik}>
                                <Card
                                  size="small"
                                  style={{
                                    border: "1px solid #ff7875",
                                    borderRadius: "8px",
                                    backgroundColor: "#fff2f0",
                                  }}
                                >
                                  <Text strong style={{ color: "#cf1322" }}>
                                    {topik}
                                  </Text>
                                  <br />
                                  <Text
                                    type="secondary"
                                    style={{ fontSize: "12px" }}
                                  >
                                    {Array.isArray(deskripsi)
                                      ? deskripsi[0]?.substring(0, 60) + "..."
                                      : deskripsi}
                                  </Text>
                                </Card>
                              </Col>
                            )
                          )}
                        </Row>
                      </Card>
                    </Col>
                  )}

                {/* Study Recommendations */}
                {data.studyRecommendations &&
                  Object.keys(data.studyRecommendations).length > 0 && (
                    <Col span={24}>
                      <Card
                        title="👨‍🎓 Rekomendasi Belajar per Siswa"
                        size="small"
                      >
                        <List
                          size="small"
                          dataSource={Object.entries(data.studyRecommendations)}
                          renderItem={([studentId, recommendation]) => {
                            // Get student name using the new API-based function
                            const studentName = getStudentName(studentId);

                            // Show loading indicator if name is being fetched
                            const isLoading = studentName === "Memuat nama...";

                            return (
                              <List.Item>
                                <div style={{ width: "100%" }}>
                                  <div
                                    style={{
                                      display: "flex",
                                      justifyContent: "space-between",
                                      alignItems: "center",
                                      marginBottom: "4px",
                                    }}
                                  >
                                    <div
                                      style={{
                                        display: "flex",
                                        alignItems: "center",
                                        gap: "8px",
                                      }}
                                    >
                                      {isLoading && <Spin size="small" />}
                                      <Text
                                        strong
                                        style={{
                                          color: isLoading ? "#999" : "inherit",
                                          fontStyle: isLoading
                                            ? "italic"
                                            : "normal",
                                        }}
                                      >
                                        {studentName}
                                      </Text>
                                    </div>
                                    <Tag color="blue">Individual</Tag>
                                  </div>
                                  <Text>{recommendation}</Text>
                                </div>
                              </List.Item>
                            );
                          }}
                        />
                      </Card>
                    </Col>
                  )}
              </Row>
            </Tabs.TabPane>
          </Tabs>

          {/* Footer Information */}
          <Card
            size="small"
            style={{
              marginTop: "16px",
              background: "#fafafa",
              border: "1px solid #d9d9d9",
            }}
          >
            <Row gutter={[16, 8]} align="middle">
              <Col xs={24} sm={8}>
                <Text type="secondary">
                  <strong>📅 Dibuat:</strong>{" "}
                  {data.generatedAt
                    ? new Date(data.generatedAt).toLocaleString("id-ID")
                    : "-"}
                </Text>
              </Col>
              <Col xs={24} sm={8}>
                <Text type="secondary">
                  <strong>🆔 ID Analisis:</strong>{" "}
                  {data.idAnalysis?.substring(0, 8)}...
                </Text>
              </Col>
              <Col xs={24} sm={8}>
                <Text type="secondary">
                  <strong>📊 Versi:</strong> {data.analysisVersion || "1.0"}
                  <Tag color="green" style={{ marginLeft: "8px" }}>
                    {data.analysisType || "COMPREHENSIVE"}
                  </Tag>
                </Text>
              </Col>
            </Row>
          </Card>
        </Spin>
      </Modal>
    );
  };

  return (
    <div style={{ padding: "24px", background: "#f0f2f5", minHeight: "100vh" }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "24px",
        }}
      >
        <Title level={2} style={{ margin: 0 }}>
          📊 Dashboard Analisis Ujian
        </Title>
        <Space>
          <Button
            type="primary"
            icon={<DownloadOutlined />}
            onClick={() => message.info("Fitur ekspor akan tersedia segera")}
          >
            Ekspor Data
          </Button>
          <Button
            icon={<ReloadOutlined />}
            onClick={() => fetchHasilUjian(1, pagination.pageSize)}
            loading={loading}
          >
            Refresh
          </Button>
        </Space>
      </div>
      {/* Enhanced Key Metrics - 2 Rows Layout */}
      <Row gutter={[16, 16]} style={{ marginBottom: "24px" }}>
        {/* Row 1: Primary Metrics */}
        <Col xs={24} sm={6}>
          <Card
            style={{
              background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
              border: "none",
            }}
          >
            <Statistic
              title="Total Peserta"
              value={summaryStats.totalSiswa}
              prefix={<UserOutlined />}
              valueStyle={{ color: "#fff" }}
              style={{ color: "#fff" }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={6}>
          <Card
            style={{
              background: "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)",
              border: "none",
            }}
          >
            <Statistic
              title="Rata-rata Nilai"
              value={summaryStats.avgScore}
              prefix={<TrophyOutlined />}
              precision={1}
              valueStyle={{ color: "#fff" }}
              style={{ color: "#fff" }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={6}>
          <Card
            style={{
              background:
                summaryStats.totalViolations > 0
                  ? "linear-gradient(135deg, #ff6b6b 0%, #ffa500 100%)"
                  : "linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%)",
              border: "none",
            }}
          >
            <Statistic
              title="Total Pelanggaran"
              value={summaryStats.totalViolations}
              prefix={<ExclamationCircleOutlined />}
              valueStyle={{ color: "#fff" }}
              style={{ color: "#fff" }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={6}>
          <Card
            style={{
              background: "linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)",
              border: "none",
            }}
          >
            <Statistic
              title="Tingkat Kelulusan"
              value={
                ((summaryStats.totalSiswa - summaryStats.highRiskStudents) /
                  Math.max(summaryStats.totalSiswa, 1)) *
                  100 || 0
              }
              prefix={<CheckCircleOutlined />}
              precision={1}
              suffix="%"
              valueStyle={{ color: "#d4691a" }}
            />
          </Card>
        </Col>

        {/* Row 2: Secondary Metrics */}
        <Col xs={24} sm={6}>
          <Card>
            <Statistic
              title="Risiko Tinggi"
              value={summaryStats.highRiskStudents}
              prefix={<ExclamationCircleOutlined />}
              valueStyle={{ color: "#cf1322" }}
            />
            <Progress
              percent={
                (summaryStats.highRiskStudents /
                  Math.max(summaryStats.totalSiswa, 1)) *
                  100 || 0
              }
              size="small"
              strokeColor="#ff4d4f"
              showInfo={false}
            />
          </Card>
        </Col>
        <Col xs={24} sm={6}>
          <Card>
            <Statistic
              title="Perlu Review"
              value={summaryStats.needsReview}
              prefix={<EyeOutlined />}
              valueStyle={{ color: "#faad14" }}
            />
            <Progress
              percent={
                (summaryStats.needsReview /
                  Math.max(summaryStats.totalSiswa, 1)) *
                  100 || 0
              }
              size="small"
              strokeColor="#faad14"
              showInfo={false}
            />
          </Card>
        </Col>
        <Col xs={24} sm={6}>
          <Card>
            <Statistic
              title="Skor Integritas"
              value={summaryStats.avgIntegrityScore}
              prefix={<BarChartOutlined />}
              precision={1}
              suffix="%"
              valueStyle={{
                color:
                  summaryStats.avgIntegrityScore >= 90
                    ? "#3f8600"
                    : summaryStats.avgIntegrityScore >= 70
                    ? "#faad14"
                    : "#cf1322",
              }}
            />
            <Progress
              percent={summaryStats.avgIntegrityScore || 0}
              size="small"
              strokeColor={
                summaryStats.avgIntegrityScore >= 90
                  ? "#52c41a"
                  : summaryStats.avgIntegrityScore >= 70
                  ? "#faad14"
                  : "#ff4d4f"
              }
              showInfo={false}
            />
          </Card>
        </Col>
        <Col xs={24} sm={6}>
          <Card>
            <Statistic
              title="Ujian Aktif"
              value={
                ujianList.filter((ujian) => ujian.status === "ACTIVE").length ||
                ujianList.length
              }
              prefix={<ClockCircleOutlined />}
            />
            <div style={{ marginTop: "8px" }}>
              <Text type="secondary" style={{ fontSize: "12px" }}>
                Total {ujianList.length} ujian
              </Text>
            </div>
          </Card>
        </Col>
      </Row>
      {/* Enhanced Analytics Cards */}
      <Row gutter={[16, 16]} style={{ marginBottom: "24px" }}>
        {/* Performance Distribution */}
        <Col xs={24} lg={12}>
          <Card title="📈 Distribusi Performa" size="small">
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
              }}
            >
              <div>
                <Text strong>Excellent (≥90): </Text>
                <Tag color="green">
                  {siswaData.filter((s) => s.nilai >= 90).length} siswa
                </Tag>
              </div>
              <Progress
                type="circle"
                percent={
                  (siswaData.filter((s) => s.nilai >= 90).length /
                    Math.max(siswaData.length, 1)) *
                    100 || 0
                }
                size={50}
                strokeColor="#52c41a"
              />
            </div>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                marginTop: "12px",
              }}
            >
              <div>
                <Text strong>Good (75-89): </Text>
                <Tag color="blue">
                  {
                    siswaData.filter((s) => s.nilai >= 75 && s.nilai < 90)
                      .length
                  }{" "}
                  siswa
                </Tag>
              </div>
              <Progress
                type="circle"
                percent={
                  (siswaData.filter((s) => s.nilai >= 75 && s.nilai < 90)
                    .length /
                    Math.max(siswaData.length, 1)) *
                    100 || 0
                }
                size={50}
                strokeColor="#1890ff"
              />
            </div>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                marginTop: "12px",
              }}
            >
              <div>
                <Text strong>Needs Improvement (&lt;75): </Text>
                <Tag color="red">
                  {siswaData.filter((s) => s.nilai < 75).length} siswa
                </Tag>
              </div>
              <Progress
                type="circle"
                percent={
                  (siswaData.filter((s) => s.nilai < 75).length /
                    Math.max(siswaData.length, 1)) *
                    100 || 0
                }
                size={50}
                strokeColor="#ff4d4f"
              />
            </div>
          </Card>
        </Col>

        {/* Security Overview */}
        <Col xs={24} lg={12}>
          <Card title="🔒 Ringkasan Keamanan" size="small">
            <Alert
              message={
                summaryStats.avgIntegrityScore >= 90
                  ? "Status Keamanan: AMAN"
                  : summaryStats.avgIntegrityScore >= 70
                  ? "Status Keamanan: PERLU PERHATIAN"
                  : "Status Keamanan: RISIKO TINGGI"
              }
              type={
                summaryStats.avgIntegrityScore >= 90
                  ? "success"
                  : summaryStats.avgIntegrityScore >= 70
                  ? "warning"
                  : "error"
              }
              showIcon
              style={{ marginBottom: "16px" }}
            />

            <Row gutter={[8, 8]}>
              <Col span={12}>
                <div
                  style={{
                    textAlign: "center",
                    padding: "8px",
                    backgroundColor: "#fff2f0",
                    borderRadius: "6px",
                  }}
                >
                  <Text type="danger" strong>
                    {summaryStats.totalViolations}
                  </Text>
                  <br />
                  <Text type="secondary" style={{ fontSize: "12px" }}>
                    Total Pelanggaran
                  </Text>
                </div>
              </Col>
              <Col span={12}>
                <div
                  style={{
                    textAlign: "center",
                    padding: "8px",
                    backgroundColor: "#f6ffed",
                    borderRadius: "6px",
                  }}
                >
                  <Text type="success" strong>
                    {siswaData.filter((s) => s.integrityScore === 100).length}
                  </Text>
                  <br />
                  <Text type="secondary" style={{ fontSize: "12px" }}>
                    Peserta Bersih
                  </Text>
                </div>
              </Col>
              <Col span={12}>
                <div
                  style={{
                    textAlign: "center",
                    padding: "8px",
                    backgroundColor: "#fff7e6",
                    borderRadius: "6px",
                  }}
                >
                  <Text style={{ color: "#fa8c16" }} strong>
                    {summaryStats.needsReview}
                  </Text>
                  <br />
                  <Text type="secondary" style={{ fontSize: "12px" }}>
                    Perlu Review
                  </Text>
                </div>
              </Col>
              <Col span={12}>
                <div
                  style={{
                    textAlign: "center",
                    padding: "8px",
                    backgroundColor: "#f0f5ff",
                    borderRadius: "6px",
                  }}
                >
                  <Text style={{ color: "#1890ff" }} strong>
                    {(summaryStats.avgIntegrityScore || 0).toFixed(1)}%
                  </Text>
                  <br />
                  <Text type="secondary" style={{ fontSize: "12px" }}>
                    Avg. Integritas
                  </Text>
                </div>
              </Col>
            </Row>
          </Card>
        </Col>
      </Row>
      {/* Recent Activity & Quick Stats */}
      <Row gutter={[16, 16]} style={{ marginBottom: "24px" }}>
        <Col xs={24} lg={8}>
          <Card title="⚡ Aktivitas Terkini" size="small">
            <List
              size="small"
              dataSource={siswaData.slice(0, 5)}
              renderItem={(item) => (
                <List.Item>
                  <div style={{ width: "100%" }}>
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                      }}
                    >
                      <Text strong>{item.pesertaNama}</Text>
                      <Tag color={item.nilai >= 75 ? "green" : "red"}>
                        {item.nilai}
                      </Tag>
                    </div>
                    <Text type="secondary" style={{ fontSize: "12px" }}>
                      {item.ujianNama} • {item.kelas}
                    </Text>
                  </div>
                </List.Item>
              )}
            />
          </Card>
        </Col>

        <Col xs={24} lg={8}>
          <Card title="🎯 Top Performers" size="small">
            <List
              size="small"
              dataSource={siswaData
                .filter((s) => s.nilai >= 85)
                .sort((a, b) => b.nilai - a.nilai)
                .slice(0, 5)}
              renderItem={(item, index) => (
                <List.Item>
                  <div style={{ width: "100%" }}>
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                      }}
                    >
                      <div>
                        <Badge
                          count={index + 1}
                          style={{
                            backgroundColor: "#52c41a",
                            marginRight: "8px",
                          }}
                        />
                        <Text strong>{item.pesertaNama}</Text>
                      </div>
                      <Tag color="green">{item.nilai}</Tag>
                    </div>
                    <Text type="secondary" style={{ fontSize: "12px" }}>
                      {item.ujianNama}
                    </Text>
                  </div>
                </List.Item>
              )}
            />
          </Card>
        </Col>

        <Col xs={24} lg={8}>
          <Card title="⚠️ Butuh Perhatian" size="small">
            <List
              size="small"
              dataSource={siswaData
                .filter((s) => s.riskLevel === "HIGH" || s.nilai < 60)
                .sort((a, b) => a.nilai - b.nilai)
                .slice(0, 5)}
              renderItem={(item) => (
                <List.Item>
                  <div style={{ width: "100%" }}>
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                      }}
                    >
                      <Text strong>{item.pesertaNama}</Text>
                      <Space>
                        {item.violationCount > 0 && (
                          <Tag color="red" style={{ fontSize: "10px" }}>
                            {item.violationCount} pelanggaran
                          </Tag>
                        )}
                        <Tag color="red">{item.nilai}</Tag>
                      </Space>
                    </div>
                    <Text type="secondary" style={{ fontSize: "12px" }}>
                      {item.ujianNama}
                    </Text>
                  </div>
                </List.Item>
              )}
            />
          </Card>
        </Col>
      </Row>
      {/* Filters */}
      <Card style={{ marginBottom: "24px" }}>
        <Row gutter={16}>
          <Col span={6}>
            <Input
              placeholder="Cari nama peserta..."
              prefix={<SearchOutlined />}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              onPressEnter={() => fetchHasilUjian(1, pagination.pageSize)}
            />
          </Col>
          <Col span={6}>
            <Select
              placeholder="Filter by Ujian"
              style={{ width: "100%" }}
              allowClear
              value={filterUjian}
              onChange={(value) => setFilterUjian(value)}
              onClear={() => setFilterUjian(null)}
            >
              {ujianList.map((ujian) => (
                <Option key={ujian.idUjian} value={ujian.idUjian}>
                  {ujian.namaUjian}
                </Option>
              ))}
            </Select>
          </Col>
          <Col span={6}>
            {" "}
            <Select
              placeholder="Filter by Kelas"
              style={{ width: "100%" }}
              allowClear
              value={filterKelas}
              onChange={(value) => setFilterKelas(value)}
              onClear={() => setFilterKelas(null)}
            >
              <Option value="X">Kelas X</Option>
              <Option value="XI">Kelas XI</Option>
              <Option value="XII">Kelas XII</Option>
            </Select>
          </Col>
          <Col span={6}>
            <Space>
              <Button
                type="primary"
                icon={<SearchOutlined />}
                onClick={() => fetchHasilUjian(1, pagination.pageSize)}
              >
                Cari
              </Button>
              <Button
                icon={<ReloadOutlined />}
                onClick={() => {
                  setSearchText("");
                  setFilterUjian(null);
                  setFilterKelas(null);
                  fetchHasilUjian(1, pagination.pageSize);
                }}
              >
                Reset
              </Button>
            </Space>
          </Col>
        </Row>
      </Card>{" "}
      {/* Student Results Table */}
      <Card title="Daftar Hasil Ujian Siswa">
        <Table
          columns={columns}
          dataSource={siswaData}
          loading={loading}
          pagination={{
            ...pagination,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) =>
              `${range[0]}-${range[1]} dari ${total} siswa`,
          }}
          onChange={handleTableChange}
          rowKey={(record) => record.key}
          scroll={{ x: 1200 }}
          rowClassName={(record) => {
            if (record.riskLevel === "HIGH") return "high-risk-row";
            if (record.needsReview) return "needs-review-row";
            return "";
          }}
        />
      </Card>
      {/* Detail Modal */}
      {renderDetailModal()}
    </div>
  );
};

export default AnalisisUjian;
