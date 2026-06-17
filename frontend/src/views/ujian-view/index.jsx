/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef, useCallback } from "react";
import {
  Card,
  Button,
  Radio,
  Checkbox,
  Input,
  Row,
  Col,
  Typography,
  Tag,
  Space,
  Progress,
  Alert,
  Modal,
  Spin,
  Badge,
  Divider,
  Grid,
  message,
  Statistic,
  List,
} from "antd";
import {
  ClockCircleOutlined,
  BookOutlined,
  CheckCircleOutlined,
  LoginOutlined,
  SendOutlined,
  TrophyOutlined,
  SafetyOutlined,
  UserOutlined,
  BulbOutlined,
  LeftOutlined,
  RightOutlined,
  EyeOutlined,
  EyeInvisibleOutlined,
  ExclamationCircleOutlined,
  PlayCircleOutlined,
  SaveOutlined,
} from "@ant-design/icons";
import { useParams, useNavigate } from "react-router-dom";
import moment from "moment";

// Import API functions - SIMPLIFIED
import { getUjian } from "@/api/ujian"; // Gunakan yang sudah ada
import {
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
} from "@/api/ujianSession";
import { reqUserInfo } from "@/api/user";
import { getAnalysisByUjian } from "@/api/ujianAnalysis";
import { recordViolation } from "@/api/cheatDetection";

const { Title, Text } = Typography;
const { useBreakpoint } = Grid;

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

const UjianCATView = () => {
  const screens = useBreakpoint();
  const navigate = useNavigate();
  const { kodeUjian } = useParams();

  // Add CSS animation for pulse effect
  useEffect(() => {
    const style = document.createElement("style");
    style.textContent = `
      @keyframes pulse {
        0% { transform: scale(1); opacity: 1; }
        50% { transform: scale(1.05); opacity: 0.8; }
        100% { transform: scale(1); opacity: 1; }
      }
    `;
    document.head.appendChild(style);

    return () => {
      document.head.removeChild(style);
    };
  }, []);
  // State management
  const [ujianData, setUjianData] = useState(null);
  const [soalList, setSoalList] = useState([]);
  const [userInfo, setUserInfo] = useState(null);
  const [schoolInfo, setSchoolInfo] = useState(null);
  const [studentInfo, setStudentInfo] = useState(null);
  const [loading, setLoading] = useState(true);

  // Session states
  const [sessionId, setSessionId] = useState(null);
  const [sessionStarted, setSessionStarted] = useState(false);
  const [sessionActive, setSessionActive] = useState(false);

  // Ujian states
  const [currentSoal, setCurrentSoal] = useState(0);
  const [jawaban, setJawaban] = useState({});
  const [timeLeft, setTimeLeft] = useState(0);
  const [isStarted, setIsStarted] = useState(false);
  const [isFinished, setIsFinished] = useState(false);
  const [showSoalPanel, setShowSoalPanel] = useState(false);
  const [attemptNumber, setAttemptNumber] = useState(1);

  // Login states
  const [showLogin, setShowLogin] = useState(true);
  const [inputKodeUjian, setInputKodeUjian] = useState(kodeUjian || "");
  const [loginLoading, setLoginLoading] = useState(false);

  // Progress tracking
  const [autoSaveStatus, setAutoSaveStatus] = useState("idle");
  const [lastSaved, setLastSaved] = useState(null);
  // Analisis ujian
  const [ujianAnalysis, setUjianAnalysis] = useState(null);
  const [analysisLoading, setAnalysisLoading] = useState(false);

  // State untuk soal matching/cocok
  const [selectedLeftItem, setSelectedLeftItem] = useState(null);

  // Hasil ujian setelah submit - TAMBAHAN BARU
  const [hasilUjian, setHasilUjian] = useState(null);
  // Server timeout detection
  const [serverTimeoutDetected, setServerTimeoutDetected] = useState(false);
  const [isWaitingAutoSubmit, setIsWaitingAutoSubmit] = useState(false);
  // Tambah state untuk pelanggaran
  const [violationCount, setViolationCount] = useState(0);
  const [violationModalVisible, setViolationModalVisible] = useState(false);
  const [violationReason, setViolationReason] = useState("");
  const [lastViolationType, setLastViolationType] = useState("");
  const [violationDetails, setViolationDetails] = useState("");
  const [criticalViolationModal, setCriticalViolationModal] = useState(false);

  const timerRef = useRef(null);
  const autoSaveRef = useRef(null);
  const keepAliveRef = useRef(null);
  const timeSyncRef = useRef(null);

  // Enhanced Auto Fullscreen - Cannot be disabled during exam
  useEffect(() => {
    const requestFullscreenSilent = async () => {
      try {
        if (!document.fullscreenElement) {
          if (document.documentElement.requestFullscreen) {
            await document.documentElement.requestFullscreen();
          } else if (document.documentElement.mozRequestFullScreen) {
            await document.documentElement.mozRequestFullScreen();
          } else if (document.documentElement.webkitRequestFullscreen) {
            await document.documentElement.webkitRequestFullscreen();
          } else if (document.documentElement.msRequestFullscreen) {
            await document.documentElement.msRequestFullscreen();
          }
        }
      } catch (error) {
        console.warn("Auto fullscreen denied:", error);
      }
    };

    // Auto trigger fullscreen on page load
    requestFullscreenSilent();

    // Enhanced fullscreen exit handler - auto re-enter during exam
    const handleFullscreenChange = () => {
      // Re-enter fullscreen if user exits during active exam session
      if (
        !document.fullscreenElement &&
        !document.webkitFullscreenElement &&
        !document.msFullscreenElement &&
        !document.mozFullScreenElement &&
        isStarted &&
        sessionActive &&
        !isFinished
      ) {
        // Show warning message
        message.warning({
          content: "⚠️ Mode fullscreen wajib selama ujian berlangsung!",
          duration: 2,
        });

        // Auto re-enter fullscreen after short delay
        setTimeout(() => {
          requestFullscreenSilent();
        }, 500);
      }
    };

    // Listen for all fullscreen change events
    document.addEventListener("fullscreenchange", handleFullscreenChange);
    document.addEventListener("webkitfullscreenchange", handleFullscreenChange);
    document.addEventListener("mozfullscreenchange", handleFullscreenChange);
    document.addEventListener("MSFullscreenChange", handleFullscreenChange);

    // Prevent ESC key during exam
    const handleKeyDown = (e) => {
      if (e.key === "Escape" && isStarted && sessionActive && !isFinished) {
        e.preventDefault();
        e.stopPropagation();
        message.warning("⚠️ Tidak dapat keluar dari fullscreen selama ujian!");
        return false;
      }
    };

    document.addEventListener("keydown", handleKeyDown, true);

    // Prevent browser navigation during exam
    const handleBeforeUnload = (e) => {
      if (isStarted && sessionActive && !isFinished) {
        e.preventDefault();
        e.returnValue =
          "Ujian sedang berlangsung. Yakin ingin meninggalkan halaman?";
        return "Ujian sedang berlangsung. Yakin ingin meninggalkan halaman?";
      }
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      document.removeEventListener("fullscreenchange", handleFullscreenChange);
      document.removeEventListener(
        "webkitfullscreenchange",
        handleFullscreenChange
      );
      document.removeEventListener(
        "mozfullscreenchange",
        handleFullscreenChange
      );
      document.removeEventListener(
        "MSFullscreenChange",
        handleFullscreenChange
      );
      document.removeEventListener("keydown", handleKeyDown, true);
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [isStarted, sessionActive, isFinished]);

  // Set body style untuk mode ujian fullscreen
  useEffect(() => {
    // Set CSS untuk body saat ujian aktif
    if (isStarted && sessionActive && !isFinished) {
      // Simpan style original
      const originalOverflow = document.body.style.overflow;
      const originalMargin = document.body.style.margin;
      const originalPadding = document.body.style.padding;

      // Set style untuk ujian
      document.body.style.overflow = "hidden";
      document.body.style.margin = "0";
      document.body.style.padding = "0";

      // Cleanup saat component unmount atau ujian selesai
      return () => {
        document.body.style.overflow = originalOverflow;
        document.body.style.margin = originalMargin;
        document.body.style.padding = originalPadding;
      };
    }
  }, [isStarted, sessionActive, isFinished]);

  // Fetch user info PERTAMA
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await reqUserInfo();
        setUserInfo(response.data);
        setStudentInfo(response.data.id); // Asumsikan response.data sudah berisi info siswa
        setSchoolInfo(response.data.school_id); // Asumsikan ada school info di response
      } catch (error) {
        console.error("Error fetching user info:", error);
        message.error("Gagal mengambil informasi pengguna");
        navigate("/login");
      } finally {
        // Always set loading false after fetching user info
        setLoading(false);
      }
    };

    fetchUserInfo();
  }, [navigate]); // Auto-login jika ada kodeUjian dari URL dan userInfo sudah ada
  useEffect(() => {
    if (kodeUjian && userInfo && userInfo.id && showLogin) {
      // Set inputKodeUjian jika belum ada
      if (!inputKodeUjian) {
        setInputKodeUjian(kodeUjian);
      }
      handleLoginUjian();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [kodeUjian, userInfo, showLogin]);
  // Handle login dengan kode ujian - PERBAIKI ERROR HANDLING
  const handleLoginUjian = async () => {
    const kodeToUse = kodeUjian || inputKodeUjian.trim();

    if (!kodeToUse) {
      message.error("Masukkan kode ujian");
      return;
    }

    setLoginLoading(true);
    try {
      // Gunakan API /ujian yang sudah ada
      const response = await getUjian();
      if (response.data.statusCode === 200) {
        const ujianList = response.data.content;

        // Cari ujian berdasarkan kode
        const ujian = ujianList.find(
          (u) => u.pengaturan?.kodeUjian === kodeToUse
        );

        if (!ujian) {
          message.error("Kode ujian tidak ditemukan");
          setLoginLoading(false);
          return;
        }

        // Validasi status ujian
        if (!["AKTIF", "BERLANGSUNG"].includes(ujian.statusUjian)) {
          message.error("Ujian tidak tersedia untuk dikerjakan");
          setLoginLoading(false);
          return;
        }

        // Validasi waktu ujian
        const now = moment();
        const mulai = moment(ujian.waktuMulaiDijadwalkan);
        const selesai = ujian.waktuSelesaiOtomatis
          ? moment(ujian.waktuSelesaiOtomatis)
          : null;

        if (now.isBefore(mulai) && !ujian.allowLateStart) {
          message.error(
            `Ujian belum dimulai. Waktu mulai: ${mulai.format(
              "DD/MM/YYYY HH:mm"
            )}`
          );
          setLoginLoading(false);
          return;
        }

        if (selesai && now.isAfter(selesai)) {
          message.error("Waktu ujian telah berakhir");
          setLoginLoading(false);
          return;
        }

        // Set data ujian dan soal sekaligus
        setUjianData(ujian);

        // Langsung set soal dari bankSoalList (tanpa jawabanBenar untuk keamanan)
        if (ujian.bankSoalList && ujian.bankSoalList.length > 0) {
          const soalFiltered = ujian.bankSoalList.map((soal) => {
            const { jawabanBenar, ...soalWithoutAnswer } = soal;
            return soalWithoutAnswer;
          });

          // Acak soal jika tipeSoal = "ACAK"
          if (ujian.tipeSoal === "ACAK") {
            setSoalList(shuffleArray(soalFiltered));
          } else {
            setSoalList(soalFiltered);
          }
        } else {
          message.warning("Ujian belum memiliki soal");
          setSoalList([]);
        }

        setTimeLeft(ujian.durasiMenit * 60);
        setShowLogin(false);

        // Check if user can start - PASTIKAN userInfo sudah ada
        if (userInfo) {
          await checkCanStart(ujian.idUjian);
        } else {
          console.error("User info not available for checkCanStart");
          setLoading(false);
        }
      } else {
        message.error("Gagal mengambil data ujian");
        setLoading(false);
      }
    } catch (error) {
      console.error("Login ujian error:", error);
      message.error("Kode ujian tidak ditemukan: " + error.message);
      setLoading(false);
    } finally {
      setLoginLoading(false);
    }
  };

  // Check if user can start ujian - PERBAIKI ERROR HANDLING
  const checkCanStart = async (idUjian) => {
    try {
      if (!userInfo) {
        console.error("No user info available for validation");
        setLoading(false);
        return;
      }

      console.log("Validating can start for:", {
        idUjian,
        userId: userInfo.id,
      });

      const validateResponse = await validateCanStart(idUjian, userInfo.id);

      if (validateResponse.data.statusCode === 200) {
        const validation = validateResponse.data.content;

        if (!validation.canStart) {
          message.error(validation.reason || "Tidak dapat memulai ujian");
          setLoading(false);
          return;
        }

        // Check for existing session
        await checkExistingSession(idUjian);
      } else {
        console.error(
          "Validation failed with status:",
          validateResponse.data.statusCode
        );
        setLoading(false);
      }
    } catch (error) {
      console.error("Validation failed:", error);

      // Jika API validateCanStart belum ada, langsung lanjut tanpa validasi
      if (error.response?.status === 404) {
        console.log("Validation endpoint not found, skipping validation");
        await checkExistingSession(idUjian);
      } else {
        message.error("Gagal memvalidasi ujian: " + error.message);
        setLoading(false);
      }
    }
  };

  // Check existing session - UPDATED to work with resume-or-start flow
  const checkExistingSession = async (idUjian) => {
    try {
      console.log("Checking existing session for:", {
        idUjian,
        userId: userInfo.id,
      });

      const activeResponse = await getActiveSession(idUjian, userInfo.id);

      if (
        activeResponse.data.statusCode === 200 &&
        activeResponse.data.content
      ) {
        // Show resume confirmation instead of auto-resume
        const sessionData = activeResponse.data.content;

        // Auto resume without modal confirmation - SEAMLESS MODE
        console.log("Auto-resuming existing session:", sessionData);

        // Brief info message only - no modal blocking
        message.info(
          `Melanjutkan ujian sebelumnya. Waktu tersisa: ${Math.floor(
            (sessionData.timeRemaining || 0) / 60
          )} menit.`,
          3
        );
      } else {
        console.log("No active session found, user can start fresh");
      }
    } catch (error) {
      console.error("Failed to check existing session:", error);

      // Jika API session belum ada, tidak masalah - user bisa start baru
      if (error.response?.status === 404) {
        console.log("Session endpoint not found, user can start new session");
      } else {
        message.warning("Tidak dapat memeriksa session: " + error.message);
      }
    } finally {
      // PASTIKAN loading selalu di-set false di akhir
      setLoading(false);
    }
  };

  // Load soal ujian - TAMBAHKAN FALLBACK
  const loadSoalUjian = async (idUjian) => {
    try {
      // Soal sudah di-set di handleLoginUjian, fungsi ini hanya untuk fallback
      if (soalList.length === 0 && ujianData?.bankSoalList) {
        const soalFiltered = ujianData.bankSoalList.map((soal) => {
          const { jawabanBenar, ...soalWithoutAnswer } = soal;
          return soalWithoutAnswer;
        });

        if (ujianData.tipeSoal === "ACAK") {
          setSoalList(shuffleArray(soalFiltered));
        } else {
          setSoalList(soalFiltered);
        }
      }
    } catch (error) {
      console.error("Failed to load soal:", error);
      message.error("Gagal memuat soal ujian");
    } finally {
      setLoading(false);
    }
  };

  // Helper function untuk mengacak array
  const shuffleArray = (array) => {
    const shuffled = [...array];
    for (let i = shuffled.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
    }
    return shuffled;
  };
  // Start ujian session - UPDATED to use resume-or-start
  const handleStartUjian = async () => {
    try {
      setLoading(true);

      // Enhanced fullscreen when starting exam - MANDATORY MODE
      try {
        if (!document.fullscreenElement) {
          if (document.documentElement.requestFullscreen) {
            await document.documentElement.requestFullscreen();
          } else if (document.documentElement.mozRequestFullScreen) {
            await document.documentElement.mozRequestFullScreen();
          } else if (document.documentElement.webkitRequestFullscreen) {
            await document.documentElement.webkitRequestFullscreen();
          } else if (document.documentElement.msRequestFullscreen) {
            await document.documentElement.msRequestFullscreen();
          }
        }
        // Show success message for fullscreen activation
        message.success("Mode fullscreen diaktifkan untuk ujian", 2);
      } catch (fullscreenError) {
        console.warn("Fullscreen not supported or denied:", fullscreenError);
        message.warning(
          "Peringatan: Gunakan fullscreen untuk pengalaman ujian terbaik"
        );
      }

      // Use resume-or-start endpoint (RECOMMENDED by backend)
      const startResponse = await resumeOrStartSession({
        idUjian: ujianData.idUjian,
        kodeUjian: ujianData.pengaturan?.kodeUjian,
        idPeserta: userInfo.id,
      });

      if (startResponse.data.statusCode === 200) {
        const sessionData = startResponse.data.content;

        // Handle resume vs new session
        if (sessionData.isResumed) {
          message.success("Melanjutkan ujian dari session sebelumnya");

          // Load existing answers and progress
          setJawaban(sessionData.answers || {});
          setCurrentSoal(sessionData.currentSoalIndex || 0);
          setTimeLeft(sessionData.timeRemaining || 0);
          setAttemptNumber(sessionData.attemptNumber || 1);
        } else {
          message.success("Session ujian baru dimulai. Selamat mengerjakan!");
        }

        // Set session data
        setSessionId(sessionData.sessionId);
        setSessionStarted(true);
        setSessionActive(true);
        setIsStarted(true);

        // Hide sidebar on small screens immediately when exam starts
        if (screens.xs || screens.sm) {
          setShowSoalPanel(false);
        }

        // Start keep-alive mechanism
        startKeepAlive();

        // Start server time synchronization
        startTimeSync();
      }
    } catch (error) {
      console.error("Start ujian error:", error);
      message.error("Gagal memulai ujian: " + error.message);
    } finally {
      setLoading(false);
    }
  };
  // Timer countdown - Updated with server timeout support
  useEffect(() => {
    if (
      isStarted &&
      !isFinished &&
      timeLeft > 0 &&
      sessionActive &&
      !serverTimeoutDetected
    ) {
      timerRef.current = setInterval(() => {
        setTimeLeft((prev) => {
          if (prev <= 1) {
            // Only client-side auto-submit if server timeout not detected
            if (!serverTimeoutDetected) {
              handleAutoSubmit();
            }
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    } else if (timerRef.current) {
      clearInterval(timerRef.current);
    }

    return () => {
      if (timerRef.current) {
        clearInterval(timerRef.current);
      }
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isStarted, isFinished, timeLeft, sessionActive, serverTimeoutDetected]);
  // Auto save mechanism
  useEffect(() => {
    if (isStarted && !isFinished && sessionActive) {
      autoSaveRef.current = setInterval(() => {
        handleAutoSave();
      }, 30000); // Auto save every 30 seconds

      return () => {
        if (autoSaveRef.current) {
          clearInterval(autoSaveRef.current);
        }
      };
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isStarted, isFinished, jawaban, sessionActive]);

  // Keep session alive
  const startKeepAlive = () => {
    keepAliveRef.current = setInterval(async () => {
      try {
        if (sessionActive && ujianData && userInfo) {
          await keepSessionAlive(ujianData.idUjian, userInfo.id);
        }
      } catch (error) {
        console.error("Keep alive failed:", error);
      }
    }, 60000); // Ping every minute
  };

  // Server time synchronization (RECOMMENDED by backend)
  const startTimeSync = () => {
    timeSyncRef.current = setInterval(async () => {
      try {
        if (sessionActive && ujianData && userInfo && !isFinished) {
          const timeResponse = await getTimeRemaining(
            ujianData.idUjian,
            userInfo.id
          );

          if (timeResponse.data.statusCode === 200) {
            const timeData = timeResponse.data.content;

            // Check if server detected timeout
            if (timeData.hasTimedOut || timeData.remainingSeconds <= 0) {
              console.log("Server detected timeout, preparing for auto-submit");
              handleServerTimeout();
            } else {
              // Sync client time with server time
              setTimeLeft(timeData.remainingSeconds);

              // Show warning if less than 5 minutes
              if (
                timeData.remainingSeconds < 300 &&
                timeData.remainingSeconds > 0
              ) {
                const minutes = Math.ceil(timeData.remainingSeconds / 60);
                message.warning(`⚠️ Waktu tersisa ${minutes} menit!`, 3);
              }
            }
          }
        }
      } catch (error) {
        console.error("Time sync failed:", error);
      }
    }, 30000); // Sync every 30 seconds as recommended
  };

  // Handle server-side timeout detection
  const handleServerTimeout = () => {
    if (isWaitingAutoSubmit || isFinished) return;

    setServerTimeoutDetected(true);
    setIsWaitingAutoSubmit(true);
    setSessionActive(false);

    // Disable all form inputs
    const formElements = document.querySelectorAll(
      "input, button, textarea, select"
    );
    formElements.forEach((element) => {
      element.disabled = true;
    });

    message.warning({
      content:
        "⏰ Waktu ujian telah berakhir. Jawaban Anda sedang diproses secara otomatis...",
      duration: 0, // Don't auto-close
      key: "auto-submit-warning",
    });

    // Poll for hasil ujian (since server handles auto-submit)
    setTimeout(() => {
      // Redirect to results or check submission status
      setIsFinished(true);
      setIsStarted(false);
      message.destroy("auto-submit-warning");
      message.success("Ujian telah selesai dan jawaban otomatis dikumpulkan!");
    }, 5000);
  };

  // Clean up intervals
  useEffect(() => {
    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
      if (autoSaveRef.current) clearInterval(autoSaveRef.current);
      if (keepAliveRef.current) clearInterval(keepAliveRef.current);
      if (timeSyncRef.current) clearInterval(timeSyncRef.current);
    };
  }, []);
  // Auto save function
  const handleAutoSave = useCallback(async () => {
    if (!sessionActive || Object.keys(jawaban).length === 0) return;

    try {
      setAutoSaveStatus("saving");

      await autoSaveProgress({
        idUjian: ujianData.idUjian,
        idPeserta: userInfo.id,
        sessionId: sessionId,
        answers: jawaban,
        currentSoalIndex: currentSoal,
        timestamp: new Date().toISOString(),
      });

      setAutoSaveStatus("saved");
      setLastSaved(new Date());

      setTimeout(() => setAutoSaveStatus("idle"), 2000);
    } catch (error) {
      setAutoSaveStatus("error");
      console.error("Auto save failed:", error);
    }
  }, [sessionActive, jawaban, ujianData, userInfo, sessionId, currentSoal]);

  // Handle manual save
  const handleManualSave = async () => {
    await handleAutoSave();
    message.success("Jawaban berhasil disimpan");
  };

  // Format waktu
  const formatTime = (seconds) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;

    if (hours > 0) {
      return `${hours.toString().padStart(2, "0")}:${minutes
        .toString()
        .padStart(2, "0")}:${secs.toString().padStart(2, "0")}`;
    }
    return `${minutes.toString().padStart(2, "0")}:${secs
      .toString()
      .padStart(2, "0")}`;
  };

  // Warna timer
  const getTimerColor = () => {
    const percentage = (timeLeft / (ujianData?.durasiMenit * 60)) * 100;
    if (percentage <= 10) return "#ff4d4f";
    if (percentage <= 25) return "#fa8c16";
    return "#52c41a";
  };

  // Handle jawaban berdasarkan jenis soal
  const handleJawaban = async (soalId, jawaban_baru) => {
    const soal = soalList.find((s) => s.idBankSoal === soalId);
    let formattedJawaban = jawaban_baru;

    // Format jawaban berdasarkan jenis soal
    if (soal?.jenisSoal === "MULTI") {
      formattedJawaban = Array.isArray(jawaban_baru)
        ? jawaban_baru
        : [jawaban_baru];
    } else if (soal?.jenisSoal === "COCOK") {
      formattedJawaban = jawaban_baru;
    } else if (soal?.jenisSoal === "ISIAN") {
      formattedJawaban = jawaban_baru;
    } else {
      formattedJawaban = jawaban_baru;
    }

    setJawaban((prev) => ({
      ...prev,
      [soalId]: formattedJawaban,
    }));

    // Save individual answer immediately
    try {
      await saveJawaban({
        idUjian: ujianData.idUjian,
        idPeserta: userInfo.id,
        sessionId: sessionId,
        idBankSoal: soalId,
        jawaban: formattedJawaban,
        attemptNumber: attemptNumber,
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      console.error("Failed to save individual answer:", error);
    }
  };

  // Navigasi soal
  const goToSoal = async (index) => {
    setCurrentSoal(index);
    setSelectedLeftItem(null); // Reset selected item when changing questions

    // Update current soal index on server
    try {
      await updateCurrentSoal(ujianData.idUjian, userInfo.id, index);
    } catch (error) {
      console.error("Failed to update current soal:", error);
    }

    if (screens.xs || screens.sm) {
      setShowSoalPanel(false);
    }
  };

  const nextSoal = () => {
    if (currentSoal < soalList.length - 1) {
      goToSoal(currentSoal + 1);
    }
  };

  const prevSoal = () => {
    if (currentSoal > 0 && ujianData.allowBacktrack) {
      goToSoal(currentSoal - 1);
    }
  };

  // Auto submit ketika waktu habis
  const handleAutoSubmit = async () => {
    await handleSubmitUjian(true);
  }; // Submit ujian
  const handleSubmitUjian = useCallback(
    async (isAutoSubmit = false) => {
      const submitAction = async () => {
        try {
          setLoading(true);

          // Final save before submit
          await handleAutoSave();

          // Submit ujian
          const submitResponse = await submitUjian({
            idUjian: ujianData.idUjian,
            idPeserta: userInfo.id,
            sessionId: sessionId,
            answers: jawaban,
            attemptNumber: attemptNumber,
            isAutoSubmit: isAutoSubmit,
            submittedAt: new Date().toISOString(),
          }); // Check if submit was successful - either statusCode 200 or success: true
          if (
            submitResponse.data.statusCode === 200 ||
            submitResponse.data.success === true
          ) {
            setIsFinished(true);
            setIsStarted(false);
            setSessionActive(false);

            message.success(
              isAutoSubmit
                ? "Waktu habis! Ujian otomatis dikumpulkan."
                : "Ujian berhasil dikumpulkan!"
            );

            // Clear all intervals
            if (timerRef.current) clearInterval(timerRef.current);
            if (autoSaveRef.current) clearInterval(autoSaveRef.current);
            if (keepAliveRef.current) clearInterval(keepAliveRef.current); // Store result data for display
            const resultData = submitResponse.data.data;
            setHasilUjian(resultData);

            // Show result immediately instead of redirecting to dashboard
            setTimeout(() => {
              console.log("Submit berhasil, menampilkan hasil:", resultData);
            }, 1000);
          }
        } catch (error) {
          console.error("Submit error:", error);

          // Even if submit fails, mark session as ended and redirect
          setIsFinished(true);
          setIsStarted(false);
          setSessionActive(false);

          // Clear all intervals
          if (timerRef.current) clearInterval(timerRef.current);
          if (autoSaveRef.current) clearInterval(autoSaveRef.current);
          if (keepAliveRef.current) clearInterval(keepAliveRef.current);

          message.error("Gagal mengumpulkan ujian: " + error.message);

          // Redirect to dashboard after 3 seconds even on error
          setTimeout(() => {
            navigate("/dashboard");
          }, 3000);
        } finally {
          setLoading(false);
        }
      };

      if (isAutoSubmit) {
        await submitAction();
      } else {
        Modal.confirm({
          title: "Konfirmasi Submit Ujian",
          content:
            "Apakah Anda yakin ingin mengumpulkan ujian? Jawaban tidak dapat diubah setelah dikumpulkan.",
          okText: "Ya, Kumpulkan",
          cancelText: "Batal",
          onOk: submitAction,
        });
      }
    },
    [
      ujianData,
      userInfo,
      sessionId,
      jawaban,
      attemptNumber,
      navigate,
      handleAutoSave,
    ]
  );

  // Status soal
  const getSoalStatus = (index) => {
    const soalId = soalList[index]?.idBankSoal;
    if (jawaban[soalId]) return "answered";
    return "unanswered";
  };

  // Statistik jawaban
  const jawabanStats = {
    dijawab: Object.keys(jawaban).length,
    belumDijawab: Math.max(
      0,
      (soalList?.length || 0) - Object.keys(jawaban).length
    ),
  };

  // Render komponen jawaban berdasarkan jenis soal
  const renderSoalComponent = (soal) => {
    switch (soal.jenisSoal) {
      case "MULTI":
        return (
          <Checkbox.Group
            value={jawaban[soal.idBankSoal] || []}
            onChange={(checkedValues) =>
              handleJawaban(soal.idBankSoal, checkedValues)
            }
            style={{ width: "100%" }}
          >
            <Space direction="vertical" style={{ width: "100%" }}>
              {Object.entries(soal.opsi || {}).map(([key, value]) => (
                <Checkbox
                  key={key}
                  value={key}
                  style={{
                    padding: "12px",
                    border: "1px solid #d9d9d9",
                    borderRadius: "6px",
                    marginBottom: "8px",
                    width: "100%",
                    display: "block",
                  }}
                >
                  <strong>{key}.</strong> {value}
                </Checkbox>
              ))}
            </Space>
          </Checkbox.Group>
        );

      case "COCOK": {
        // Initialize matching state if not exists
        const currentMatching = jawaban[soal.idBankSoal] || {};

        // Parse left and right items from soal.pasangan
        const leftItems = Object.entries(soal.pasangan || {})
          .filter(([key]) => key.includes("_kiri"))
          .map(([key, value]) => ({
            id: key.replace("_kiri", ""),
            label: key.replace("_kiri", "").toUpperCase(),
            text: value,
          }));

        const rightItems = Object.entries(soal.pasangan || {})
          .filter(([key]) => key.includes("_kanan"))
          .map(([key, value]) => ({
            id: key.replace("_kanan", ""),
            label: key.replace("_kanan", "").toUpperCase(),
            text: value,
          }));

        return (
          <div>
            <Alert
              message="Instruksi Menjodohkan"
              description="1️⃣ Klik item di sebelah kiri (akan berwarna biru) → 2️⃣ Klik item di sebelah kanan untuk menjodohkan. Klik item berpasangan untuk membatalkan."
              type="info"
              style={{ marginBottom: "16px" }}
            />

            <Row gutter={24}>
              <Col span={11}>
                <div
                  style={{
                    backgroundColor: "#f8f9fa",
                    padding: "12px",
                    borderRadius: "8px",
                    border: "2px solid #e9ecef",
                  }}
                >
                  <Text
                    strong
                    style={{
                      fontSize: "16px",
                      marginBottom: "12px",
                      display: "block",
                    }}
                  >
                    📋 Sisi Kiri
                  </Text>
                  <Space direction="vertical" style={{ width: "100%" }}>
                    {leftItems.map((item) => {
                      const isMatched = Object.keys(currentMatching).includes(
                        item.id
                      );
                      const isSelected = selectedLeftItem === item.id;
                      const rightMatch = currentMatching[item.id];
                      const rightMatchLabel = rightItems.find(
                        (r) => r.id === rightMatch
                      )?.label;

                      return (
                        <div
                          key={item.id}
                          onClick={() => {
                            if (isMatched) {
                              // Remove matching if already matched
                              const newMatching = { ...currentMatching };
                              delete newMatching[item.id];
                              handleJawaban(soal.idBankSoal, newMatching);
                              setSelectedLeftItem(null);
                            } else {
                              // Select this left item
                              setSelectedLeftItem(item.id);
                            }
                          }}
                          style={{
                            padding: "12px 16px",
                            border: isSelected
                              ? "2px solid #1890ff"
                              : isMatched
                              ? "2px solid #52c41a"
                              : "2px solid #d9d9d9",
                            borderRadius: "8px",
                            backgroundColor: isSelected
                              ? "#e6f7ff"
                              : isMatched
                              ? "#f6ffed"
                              : "#ffffff",
                            cursor: "pointer",
                            transition: "all 0.3s ease",
                            position: "relative",
                            boxShadow: isSelected
                              ? "0 0 8px rgba(24, 144, 255, 0.3)"
                              : "none",
                          }}
                        >
                          <Text
                            strong
                            style={{
                              color: isSelected
                                ? "#1890ff"
                                : isMatched
                                ? "#52c41a"
                                : "#666",
                            }}
                          >
                            {item.label}.
                          </Text>{" "}
                          {item.text}
                          {isSelected && (
                            <span
                              style={{
                                position: "absolute",
                                right: "8px",
                                top: "50%",
                                transform: "translateY(-50%)",
                                color: "#1890ff",
                                fontSize: "14px",
                                fontWeight: "bold",
                              }}
                            >
                              👆
                            </span>
                          )}
                          {isMatched && (
                            <div
                              style={{
                                position: "absolute",
                                right: "8px",
                                top: "50%",
                                transform: "translateY(-50%)",
                                color: "#52c41a",
                                fontSize: "12px",
                              }}
                            >
                              → {rightMatchLabel}
                            </div>
                          )}
                        </div>
                      );
                    })}
                  </Space>
                </div>
              </Col>

              <Col
                span={2}
                style={{ textAlign: "center", alignSelf: "center" }}
              >
                <div
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    justifyContent: "center",
                    height: "100%",
                    minHeight: "200px",
                  }}
                >
                  <div
                    style={{
                      fontSize: "32px",
                      marginBottom: "8px",
                    }}
                  >
                    🔗
                  </div>
                  {selectedLeftItem && (
                    <div
                      style={{
                        padding: "4px 8px",
                        backgroundColor: "#1890ff",
                        color: "white",
                        borderRadius: "4px",
                        fontSize: "10px",
                        textAlign: "center",
                        animation: "pulse 1.5s infinite",
                      }}
                    >
                      {
                        leftItems.find((item) => item.id === selectedLeftItem)
                          ?.label
                      }{" "}
                      terpilih
                    </div>
                  )}
                  <Text
                    type="secondary"
                    style={{
                      fontSize: "12px",
                      textAlign: "center",
                      marginTop: "8px",
                    }}
                  >
                    {selectedLeftItem
                      ? "Pilih pasangan di kanan →"
                      : "Pilih item kiri dulu ←"}
                  </Text>
                </div>
              </Col>

              <Col span={11}>
                <div
                  style={{
                    backgroundColor: "#f0f8ff",
                    padding: "12px",
                    borderRadius: "8px",
                    border: "2px solid #cce7ff",
                  }}
                >
                  <Text
                    strong
                    style={{
                      fontSize: "16px",
                      marginBottom: "12px",
                      display: "block",
                    }}
                  >
                    📝 Sisi Kanan
                  </Text>
                  <Space direction="vertical" style={{ width: "100%" }}>
                    {rightItems.map((item) => {
                      const matchingLeft = Object.keys(currentMatching).find(
                        (leftKey) => currentMatching[leftKey] === item.id
                      );
                      const isMatched = !!matchingLeft;
                      const canSelect = selectedLeftItem && !isMatched;

                      return (
                        <div
                          key={item.id}
                          onClick={() => {
                            if (isMatched) {
                              // Remove matching if already matched
                              const newMatching = { ...currentMatching };
                              delete newMatching[matchingLeft];
                              handleJawaban(soal.idBankSoal, newMatching);
                              setSelectedLeftItem(null);
                            } else if (selectedLeftItem) {
                              // Create new matching
                              const newMatching = {
                                ...currentMatching,
                                [selectedLeftItem]: item.id,
                              };
                              handleJawaban(soal.idBankSoal, newMatching);
                              setSelectedLeftItem(null);
                            } else {
                              message.info(
                                "Pilih item di sebelah kiri terlebih dahulu"
                              );
                            }
                          }}
                          style={{
                            padding: "12px 16px",
                            border: canSelect
                              ? "2px dashed #1890ff"
                              : isMatched
                              ? "2px solid #52c41a"
                              : "2px solid #d9d9d9",
                            borderRadius: "8px",
                            backgroundColor: canSelect
                              ? "#f0f9ff"
                              : isMatched
                              ? "#f6ffed"
                              : "#ffffff",
                            cursor:
                              selectedLeftItem || isMatched
                                ? "pointer"
                                : "not-allowed",
                            transition: "all 0.3s ease",
                            position: "relative",
                            opacity: !selectedLeftItem && !isMatched ? 0.6 : 1,
                          }}
                        >
                          <Text
                            strong
                            style={{
                              color: canSelect
                                ? "#1890ff"
                                : isMatched
                                ? "#52c41a"
                                : "#666",
                            }}
                          >
                            {item.label}.
                          </Text>{" "}
                          {item.text}
                          {canSelect && (
                            <span
                              style={{
                                position: "absolute",
                                right: "8px",
                                top: "50%",
                                transform: "translateY(-50%)",
                                color: "#1890ff",
                                fontSize: "14px",
                              }}
                            >
                              👈
                            </span>
                          )}
                          {isMatched && (
                            <div
                              style={{
                                position: "absolute",
                                right: "8px",
                                top: "50%",
                                transform: "translateY(-50%)",
                                color: "#52c41a",
                                fontSize: "12px",
                              }}
                            >
                              ←{" "}
                              {
                                leftItems.find((l) => l.id === matchingLeft)
                                  ?.label
                              }
                            </div>
                          )}
                        </div>
                      );
                    })}
                  </Space>
                </div>
              </Col>
            </Row>

            {/* Display current matches */}
            {Object.keys(currentMatching).length > 0 && (
              <div
                style={{
                  marginTop: "16px",
                  padding: "12px",
                  backgroundColor: "#f6ffed",
                  border: "1px solid #b7eb8f",
                  borderRadius: "6px",
                }}
              >
                <Text strong style={{ color: "#52c41a" }}>
                  ✅ Pasangan yang sudah dibuat:
                </Text>
                <div style={{ marginTop: "8px" }}>
                  {Object.entries(currentMatching).map(([leftId, rightId]) => {
                    const leftItem = leftItems.find(
                      (item) => item.id === leftId
                    );
                    const rightItem = rightItems.find(
                      (item) => item.id === rightId
                    );
                    return (
                      <Tag
                        key={`${leftId}-${rightId}`}
                        color="green"
                        style={{ margin: "2px" }}
                      >
                        {leftItem?.label} ↔ {rightItem?.label}
                      </Tag>
                    );
                  })}
                </div>
              </div>
            )}
          </div>
        );
      }

      case "ISIAN":
        return (
          <Input
            placeholder="Masukkan jawaban Anda"
            value={jawaban[soal.idBankSoal] || ""}
            onChange={(e) => handleJawaban(soal.idBankSoal, e.target.value)}
            size="large"
          />
        );

      default:
        // Single choice (PG)
        return (
          <Radio.Group
            value={jawaban[soal.idBankSoal]}
            onChange={(e) => handleJawaban(soal.idBankSoal, e.target.value)}
            style={{ width: "100%" }}
          >
            <Space direction="vertical" style={{ width: "100%" }}>
              {Object.entries(soal.opsi || {}).map(([key, value]) => (
                <Radio
                  key={key}
                  value={key}
                  style={{
                    padding: "12px",
                    border: "1px solid #d9d9d9",
                    borderRadius: "6px",
                    marginBottom: "8px",
                    width: "100%",
                    display: "block",
                  }}
                >
                  <strong>{key}.</strong> {value}
                </Radio>
              ))}
            </Space>
          </Radio.Group>
        );
    }
  };

  // Auto save status indicator
  const renderAutoSaveStatus = () => {
    switch (autoSaveStatus) {
      case "saving":
        return (
          <Tag icon={<SaveOutlined />} color="processing">
            Menyimpan...
          </Tag>
        );
      case "saved":
        return (
          <Tag icon={<CheckCircleOutlined />} color="success">
            Tersimpan
          </Tag>
        );
      case "error":
        return (
          <Tag icon={<ExclamationCircleOutlined />} color="error">
            Error
          </Tag>
        );
      default:
        return null;
    }
  };

  // Fetch analysis after ujian selesai - HANYA UNTUK OPERATOR & TEACHER
  useEffect(() => {
    if (isFinished && ujianData && userInfo) {
      // Check if user is OPERATOR or TEACHER
      const canViewAnalysis =
        userInfo.authorities &&
        userInfo.authorities.some((auth) =>
          ["ROLE_OPERATOR", "ROLE_TEACHER"].includes(auth.authority)
        );

      if (canViewAnalysis) {
        const fetchAnalysis = async () => {
          setAnalysisLoading(true);
          try {
            const res = await getAnalysisByUjian(ujianData.idUjian);
            if (res.data && res.data.content && res.data.content.length > 0) {
              setUjianAnalysis(res.data.content[0]);
            } else {
              setUjianAnalysis(null);
            }
          } catch (err) {
            setUjianAnalysis(null);
          } finally {
            setAnalysisLoading(false);
          }
        };
        fetchAnalysis();
      } else {
        // Siswa tidak bisa melihat analisis
        setUjianAnalysis(null);
        setAnalysisLoading(false);
      }
    }
  }, [isFinished, ujianData, userInfo]);
  // === ANTI-CHEAT EVENT LISTENER ===
  useEffect(() => {
    if (!(isStarted && sessionActive && ujianData && userInfo && sessionId))
      return;

    // Function to get violation description
    const getViolationDescription = (type) => {
      const descriptions = {
        WINDOW_BLUR: "Anda beralih dari window/aplikasi ujian",
        TAB_SWITCH: "Anda beralih ke tab lain atau meminimalkan browser",
        COPY_PASTE: "Anda melakukan copy/paste pada halaman ujian",
        FULLSCREEN_EXIT: "Anda keluar dari mode fullscreen",
        CTRL_C_V: "Anda menggunakan shortcut Ctrl+C atau Ctrl+V",
        BROWSER_DEV_TOOLS: "Anda membuka Developer Tools (F12)",
        ALT_TAB: "Anda menggunakan Alt+Tab untuk berganti aplikasi",
        PRINT_SCREEN: "Anda menggunakan tombol Print Screen",
        RIGHT_CLICK: "Anda melakukan klik kanan pada halaman ujian",
        DRAG_DROP: "Anda melakukan drag & drop pada halaman ujian",
      };
      return descriptions[type] || `Terdeteksi pelanggaran: ${type}`;
    };

    // Handler untuk kirim pelanggaran ke backend dan update counter
    const handleViolation = (type, extra = {}) => {
      // Ensure evidence always has at least one non-empty key
      const evidence =
        Object.keys(extra).length > 0
          ? extra
          : { source: "frontend", detector: "anti-cheat" };

      recordViolation({
        sessionId,
        idPeserta: userInfo.id,
        idUjian: ujianData.idUjian,
        typeViolation: type,
        detectedAt: new Date().toISOString(),
        evidence: evidence,
      });

      setViolationCount((prev) => {
        const next = prev + 1;

        // Set violation details for alert
        setLastViolationType(type);
        setViolationDetails(getViolationDescription(type));

        // Show immediate violation alert
        setViolationModalVisible(true);

        // Critical violation check - if more than 10 violations
        // if (next > 10) {
        //   setCriticalViolationModal(true);
        //   setViolationReason(type);

        //   // Auto submit ujian jika pelanggaran > 10
        //   if (!isFinished && sessionActive) {
        //     setTimeout(() => {
        //       handleSubmitUjian(true);
        //     }, 5000); // Give 3 seconds for user to see the message
        //   }
        // }

        return next;
      });
    };

    // Tab/window blur
    const handleBlur = () => handleViolation("WINDOW_BLUR");
    // Visibility change
    const handleVisibility = () => {
      if (document.visibilityState === "hidden") handleViolation("TAB_SWITCH");
    };
    // Copy
    const handleCopy = (e) =>
      handleViolation("COPY_PASTE", {
        clipboard: e.clipboardData?.getData("text"),
      });
    // Paste
    const handlePaste = (e) =>
      handleViolation("COPY_PASTE", {
        clipboard: e.clipboardData?.getData("text"),
      }); // Fullscreen exit
    const handleFullscreen = () => {
      // Check if not in fullscreen
      if (
        !document.fullscreenElement &&
        !document.webkitFullscreenElement &&
        !document.msFullscreenElement &&
        !document.mozFullScreenElement
      ) {
        handleViolation("FULLSCREEN_EXIT");
        // Immediately re-enter fullscreen - NO DELAY
        setTimeout(() => {
          try {
            if (document.documentElement.requestFullscreen) {
              document.documentElement.requestFullscreen();
            } else if (document.documentElement.webkitRequestFullscreen) {
              document.documentElement.webkitRequestFullscreen();
            } else if (document.documentElement.msRequestFullscreen) {
              document.documentElement.msRequestFullscreen();
            }
          } catch (error) {
            console.warn("Cannot re-enter fullscreen:", error);
          }
        }, 100); // Minimal delay for immediate re-entry
      }
    };
    // Keyboard shortcut (Ctrl+C, Ctrl+V, Alt+Tab, F12, dsb)
    const handleKeydown = (e) => {
      if ((e.ctrlKey && e.key === "c") || (e.ctrlKey && e.key === "v")) {
        handleViolation("CTRL_C_V", { key: e.key });
      }
      if (e.key === "F12") {
        e.preventDefault(); // Prevent opening dev tools
        handleViolation("BROWSER_DEV_TOOLS");
      }
      if (e.altKey && e.key.toLowerCase() === "tab") handleViolation("ALT_TAB");
      // Print screen
      if (e.key === "PrintScreen") handleViolation("PRINT_SCREEN");
    };
    // Right click
    const handleContextMenu = (e) => {
      e.preventDefault(); // Prevent default right-click menu
      handleViolation("RIGHT_CLICK");
    };

    // Drag and drop
    const handleDragStart = () => handleViolation("DRAG_DROP");

    window.addEventListener("blur", handleBlur);
    document.addEventListener("visibilitychange", handleVisibility);
    document.addEventListener("copy", handleCopy);
    document.addEventListener("paste", handlePaste);
    document.addEventListener("fullscreenchange", handleFullscreen);
    window.addEventListener("keydown", handleKeydown);
    document.addEventListener("contextmenu", handleContextMenu);
    document.addEventListener("dragstart", handleDragStart);

    return () => {
      window.removeEventListener("blur", handleBlur);
      document.removeEventListener("visibilitychange", handleVisibility);
      document.removeEventListener("copy", handleCopy);
      document.removeEventListener("paste", handlePaste);
      document.removeEventListener("fullscreenchange", handleFullscreen);
      window.removeEventListener("keydown", handleKeydown);
      document.removeEventListener("contextmenu", handleContextMenu);
      document.removeEventListener("dragstart", handleDragStart);
    };
  }, [
    isStarted,
    sessionActive,
    ujianData,
    userInfo,
    sessionId,
    isFinished,
    handleSubmitUjian,
  ]);

  if (loading) {
    return (
      <div style={{ padding: "24px", textAlign: "center" }}>
        <Spin size="large" />
        <div style={{ marginTop: "16px" }}>Memuat ujian...</div>
      </div>
    );
  }

  // Login screen
  if (showLogin) {
    return (
      <div style={{ padding: "24px", maxWidth: "500px", margin: "0 auto" }}>
        <Card>
          <div style={{ textAlign: "center", padding: "40px 20px" }}>
            <LoginOutlined
              style={{
                fontSize: "64px",
                color: "#1890ff",
                marginBottom: "24px",
              }}
            />
            <Title level={2}>Masuk Ujian CAT</Title>
            <div style={{ marginBottom: "24px" }}>
              <Input
                placeholder="Masukkan kode ujian"
                value={inputKodeUjian}
                onChange={(e) => setInputKodeUjian(e.target.value)}
                size="large"
                onPressEnter={handleLoginUjian}
              />
            </div>
            <Button
              type="primary"
              size="large"
              loading={loginLoading}
              onClick={handleLoginUjian}
              style={{ width: "100%" }}
            >
              Masuk Ujian
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  // Pre-start screen
  if (!isStarted && !isFinished && ujianData && soalList.length > 0) {
    return (
      <div style={{ padding: "24px", maxWidth: "800px", margin: "0 auto" }}>
        <Card>
          <div style={{ textAlign: "center", padding: "40px 20px" }}>
            <BookOutlined
              style={{
                fontSize: "64px",
                color: "#1890ff",
                marginBottom: "24px",
              }}
            />
            <Title level={2}>{ujianData.namaUjian}</Title>
            <Text
              type="secondary"
              style={{
                fontSize: "16px",
                display: "block",
                marginBottom: "24px",
              }}
            >
              Kode: {ujianData.pengaturan?.kodeUjian}
            </Text>

            <Card style={{ marginBottom: "24px", backgroundColor: "#f6ffed" }}>
              <Row gutter={[24, 16]}>
                <Col xs={24} sm={8}>
                  <div style={{ textAlign: "center" }}>
                    <ClockCircleOutlined
                      style={{ fontSize: "24px", color: "#52c41a" }}
                    />
                    <div style={{ marginTop: "8px" }}>
                      <Text strong>{ujianData.durasiMenit} Menit</Text>
                      <div>Waktu Ujian</div>
                    </div>
                  </div>
                </Col>
                <Col xs={24} sm={8}>
                  <div style={{ textAlign: "center" }}>
                    <BookOutlined
                      style={{ fontSize: "24px", color: "#1890ff" }}
                    />
                    <div style={{ marginTop: "8px" }}>
                      <Text strong>{soalList.length} Soal</Text>
                      <div>Total Soal</div>
                    </div>
                  </div>
                </Col>
                <Col xs={24} sm={8}>
                  <div style={{ textAlign: "center" }}>
                    <CheckCircleOutlined
                      style={{ fontSize: "24px", color: "#fa8c16" }}
                    />
                    <div style={{ marginTop: "8px" }}>
                      <Text strong>{ujianData.tipeSoal}</Text>
                      <div>Tipe Soal</div>
                    </div>
                  </div>
                </Col>
              </Row>
            </Card>

            {ujianData.deskripsi && (
              <Alert
                message="Deskripsi Ujian"
                description={ujianData.deskripsi}
                type="info"
                style={{ marginBottom: "24px" }}
              />
            )}

            <Alert
              message="Petunjuk Ujian"
              description={
                <ul style={{ textAlign: "left", paddingLeft: "20px" }}>
                  <li>Bacalah setiap soal dengan teliti sebelum menjawab</li>
                  <li>Pilih jawaban yang paling tepat untuk setiap soal</li>
                  {ujianData.allowBacktrack && (
                    <li>
                      Anda dapat kembali ke soal sebelumnya untuk mengubah
                      jawaban
                    </li>
                  )}
                  {ujianData.allowReview && (
                    <li>Anda dapat mereview jawaban sebelum mengumpulkan</li>
                  )}
                  <li>Jawaban akan tersimpan otomatis setiap 30 detik</li>
                  <li>Pastikan koneksi internet stabil selama ujian</li>
                  <li>Ujian akan otomatis terkumpul ketika waktu habis</li>
                  <li>Maksimal percobaan: {ujianData.maxAttempts} kali</li>
                </ul>
              }
              type="info"
              style={{ marginBottom: "32px" }}
            />

            <Button
              type="primary"
              size="large"
              icon={<PlayCircleOutlined />}
              onClick={handleStartUjian}
              loading={loading}
              style={{ padding: "8px 32px", height: "auto" }}
            >
              Mulai Ujian
            </Button>
          </div>
        </Card>
      </div>
    );
  }
  // Finished screen
  if (isFinished) {
    return (
      <div style={{ maxWidth: 800, margin: "40px auto", padding: "20px" }}>
        <Card>
          {" "}
          <div style={{ textAlign: "center", marginBottom: "24px" }}>
            <CheckCircleOutlined
              style={{
                fontSize: "48px",
                color: "#52c41a",
                marginBottom: "16px",
              }}
            />
            <Title level={2}>Ujian Berhasil Diselesaikan!</Title>
            <Text type="secondary">
              {ujianData?.namaUjian} - Kode: {ujianData?.pengaturan?.kodeUjian}
            </Text>
          </div>
          <Divider />
          {/* Hasil Ujian */}
          {hasilUjian && (
            <div style={{ marginBottom: "24px" }}>
              <Title level={4}>Hasil Ujian Anda</Title>
              <Row gutter={[16, 16]}>
                <Col xs={24} sm={12}>
                  <Card size="small" style={{ backgroundColor: "#f6ffed" }}>
                    <Statistic
                      title="Total Skor"
                      value={hasilUjian.totalSkor || 0}
                      precision={1}
                      suffix={`/ ${hasilUjian.skorMaksimal || 100}`}
                    />
                  </Card>
                </Col>
                <Col xs={24} sm={12}>
                  <Card size="small" style={{ backgroundColor: "#f0f5ff" }}>
                    <Statistic
                      title="Persentase"
                      value={hasilUjian.persentase || 0}
                      precision={1}
                      suffix="%"
                    />
                  </Card>
                </Col>
              </Row>

              {/* Detail Jawaban */}
              <div style={{ marginTop: "16px" }}>
                <Title level={5}>Detail Jawaban</Title>{" "}
                <Row gutter={[16, 8]}>
                  <Col span={8}>
                    <Text>
                      <CheckCircleOutlined style={{ color: "#52c41a" }} />{" "}
                      Benar: {hasilUjian.jumlahBenar || 0}
                    </Text>
                  </Col>
                  <Col span={8}>
                    <Text>❌ Salah: {hasilUjian.jumlahSalah || 0}</Text>
                  </Col>
                  <Col span={8}>
                    <Text>➖ Kosong: {hasilUjian.jumlahKosong || 0}</Text>
                  </Col>
                </Row>
              </div>

              {/* Waktu */}
              <div style={{ marginTop: "16px" }}>
                <Title level={5}>Informasi Waktu</Title>{" "}
                <Text>
                  <ClockCircleOutlined /> Durasi Pengerjaan:{" "}
                  {hasilUjian.durasiPengerjaan || "-"}
                </Text>
              </div>
            </div>
          )}
          <Divider />
          {/* Analisis Ujian - HANYA UNTUK OPERATOR & TEACHER */}
          {userInfo &&
          userInfo.authorities &&
          userInfo.authorities.some((auth) =>
            ["ROLE_OPERATOR", "ROLE_TEACHER"].includes(auth.authority)
          ) ? (
            <>
              {analysisLoading ? (
                <div style={{ textAlign: "center" }}>
                  <Spin tip="Memuat analisis ujian..." size="large" />
                </div>
              ) : ujianAnalysis ? (
                <div>
                  <Title level={4}>📊 Analisis Ujian Kelas</Title>

                  {/* Enhanced Statistics Cards */}
                  <Row gutter={[16, 16]} style={{ marginBottom: "24px" }}>
                    <Col xs={24} sm={6}>
                      <Card size="small" style={{ backgroundColor: "#f6ffed" }}>
                        <Statistic
                          title="Rata-rata Kelas"
                          value={ujianAnalysis.averageScore}
                          precision={2}
                          prefix={<TrophyOutlined />}
                        />
                      </Card>
                    </Col>
                    <Col xs={24} sm={6}>
                      <Card size="small" style={{ backgroundColor: "#f0f5ff" }}>
                        <Statistic
                          title="Tingkat Kelulusan"
                          value={ujianAnalysis.passRate}
                          precision={1}
                          suffix="%"
                          prefix={<CheckCircleOutlined />}
                        />
                      </Card>
                    </Col>
                    <Col xs={24} sm={6}>
                      <Card size="small" style={{ backgroundColor: "#fff2f0" }}>
                        <Statistic
                          title="Skor Integritas"
                          value={
                            ujianAnalysis.integrityScore
                              ? (ujianAnalysis.integrityScore * 100).toFixed(1)
                              : 0
                          }
                          precision={1}
                          suffix="%"
                          prefix={<SafetyOutlined />}
                        />
                      </Card>
                    </Col>
                    <Col xs={24} sm={6}>
                      <Card size="small" style={{ backgroundColor: "#f9f0ff" }}>
                        <Statistic
                          title="Total Peserta"
                          value={ujianAnalysis.totalParticipants || 0}
                          prefix={<UserOutlined />}
                        />
                      </Card>
                    </Col>
                  </Row>

                  {/* Enhanced Detailed Statistics */}
                  {(ujianAnalysis.medianScore ||
                    ujianAnalysis.standardDeviation ||
                    ujianAnalysis.gradeDistribution) && (
                    <Card size="small" style={{ marginBottom: "16px" }}>
                      <Title level={5}>📈 Statistik Detail</Title>
                      <Row gutter={[16, 8]}>
                        {ujianAnalysis.medianScore && (
                          <Col xs={24} sm={8}>
                            <Text>
                              <strong>Median:</strong>{" "}
                              {ujianAnalysis.medianScore.toFixed(1)}
                            </Text>
                          </Col>
                        )}
                        {ujianAnalysis.standardDeviation && (
                          <Col xs={24} sm={8}>
                            <Text>
                              <strong>Std. Deviasi:</strong>{" "}
                              {ujianAnalysis.standardDeviation.toFixed(1)}
                            </Text>
                          </Col>
                        )}
                        {ujianAnalysis.highestScore &&
                          ujianAnalysis.lowestScore && (
                            <Col xs={24} sm={8}>
                              <Text>
                                <strong>Range:</strong>{" "}
                                {ujianAnalysis.lowestScore.toFixed(1)} -{" "}
                                {ujianAnalysis.highestScore.toFixed(1)}
                              </Text>
                            </Col>
                          )}
                      </Row>

                      {/* Grade Distribution */}
                      {ujianAnalysis.gradeDistribution && (
                        <div style={{ marginTop: "12px" }}>
                          <Text strong>Distribusi Nilai:</Text>
                          <div style={{ marginTop: "8px" }}>
                            {Object.entries(
                              ujianAnalysis.gradeDistribution
                            ).map(([grade, count]) => (
                              <Tag
                                key={grade}
                                color={getGradeColor(grade)}
                                style={{ margin: "2px" }}
                              >
                                {grade}: {count} siswa (
                                {ujianAnalysis.gradePercentages?.[
                                  grade
                                ]?.toFixed(1) || 0}
                                %)
                              </Tag>
                            ))}
                          </div>
                        </div>
                      )}
                    </Card>
                  )}

                  {/* Item Analysis Preview */}
                  {ujianAnalysis.easiestQuestions &&
                    ujianAnalysis.easiestQuestions.length > 0 && (
                      <Card size="small" style={{ marginBottom: "16px" }}>
                        <Title level={5}>📝 Analisis Soal</Title>
                        <Row gutter={[16, 8]}>
                          <Col xs={24} sm={12}>
                            <Text>
                              <strong>Soal Termudah:</strong>
                            </Text>
                            <div style={{ marginTop: "4px" }}>
                              {ujianAnalysis.easiestQuestions
                                .slice(0, 3)
                                .map((questionId, index) => (
                                  <Tag
                                    key={questionId}
                                    color="green"
                                    style={{ margin: "2px", fontSize: "10px" }}
                                  >
                                    #{index + 1}
                                  </Tag>
                                ))}
                            </div>
                          </Col>
                          {ujianAnalysis.hardestQuestions &&
                            ujianAnalysis.hardestQuestions.length > 0 && (
                              <Col xs={24} sm={12}>
                                <Text>
                                  <strong>Soal Tersulit:</strong>
                                </Text>
                                <div style={{ marginTop: "4px" }}>
                                  {ujianAnalysis.hardestQuestions
                                    .slice(0, 3)
                                    .map((questionId, index) => (
                                      <Tag
                                        key={questionId}
                                        color="red"
                                        style={{
                                          margin: "2px",
                                          fontSize: "10px",
                                        }}
                                      >
                                        #{index + 1}
                                      </Tag>
                                    ))}
                                </div>
                              </Col>
                            )}
                        </Row>
                      </Card>
                    )}

                  {/* Security Analysis */}
                  {(ujianAnalysis.suspiciousSubmissions > 0 ||
                    ujianAnalysis.flaggedParticipants > 0) && (
                    <Card
                      size="small"
                      style={{ marginBottom: "16px", borderColor: "#ff7875" }}
                    >
                      <Title level={5} style={{ color: "#ff7875" }}>
                        🔒 Analisis Keamanan
                      </Title>
                      <Row gutter={[16, 8]}>
                        <Col xs={24} sm={8}>
                          <Text>
                            <strong>Pelanggaran:</strong>{" "}
                            {ujianAnalysis.suspiciousSubmissions || 0}
                          </Text>
                        </Col>
                        <Col xs={24} sm={8}>
                          <Text>
                            <strong>Peserta Ter-flag:</strong>{" "}
                            {ujianAnalysis.flaggedParticipants || 0}
                          </Text>
                        </Col>
                        <Col xs={24} sm={8}>
                          <Text>
                            <strong>Status:</strong>
                            <Tag
                              color={
                                ujianAnalysis.integrityScore > 0.8
                                  ? "green"
                                  : ujianAnalysis.integrityScore > 0.6
                                  ? "orange"
                                  : "red"
                              }
                            >
                              {ujianAnalysis.integrityScore > 0.8
                                ? "Aman"
                                : ujianAnalysis.integrityScore > 0.6
                                ? "Perlu Perhatian"
                                : "Berisiko Tinggi"}
                            </Tag>
                          </Text>
                        </Col>
                      </Row>
                    </Card>
                  )}

                  {/* Enhanced Recommendations */}
                  {ujianAnalysis.recommendations &&
                    ujianAnalysis.recommendations.length > 0 && (
                      <Card size="small" style={{ marginBottom: "16px" }}>
                        <Title level={5}>💡 Rekomendasi & Saran</Title>
                        <List
                          size="small"
                          dataSource={ujianAnalysis.recommendations}
                          renderItem={(item, index) => (
                            <List.Item style={{ padding: "4px 0" }}>
                              <Text>
                                <BookOutlined
                                  style={{
                                    color: "#1890ff",
                                    marginRight: "8px",
                                  }}
                                />
                                {item}
                              </Text>
                            </List.Item>
                          )}
                        />

                        {/* Improvement Suggestions */}
                        {ujianAnalysis.improvementSuggestions &&
                          ujianAnalysis.improvementSuggestions.length > 0 && (
                            <div style={{ marginTop: "12px" }}>
                              <Text strong>Saran Perbaikan:</Text>
                              <List
                                size="small"
                                dataSource={
                                  ujianAnalysis.improvementSuggestions
                                }
                                renderItem={(item, index) => (
                                  <List.Item style={{ padding: "2px 0" }}>
                                    <Text type="secondary">
                                      <BulbOutlined
                                        style={{
                                          color: "#faad14",
                                          marginRight: "8px",
                                        }}
                                      />
                                      {item}
                                    </Text>
                                  </List.Item>
                                )}
                              />
                            </div>
                          )}
                      </Card>
                    )}

                  {/* Performance by Class */}
                  {ujianAnalysis.performanceByKelas &&
                    Object.keys(ujianAnalysis.performanceByKelas).length >
                      0 && (
                      <Card size="small">
                        <Title level={5}>🏫 Performa per Kelas</Title>
                        {Object.entries(ujianAnalysis.performanceByKelas).map(
                          ([kelas, data]) => (
                            <div key={kelas} style={{ marginBottom: "8px" }}>
                              <Text strong>Kelas {kelas}:</Text>
                              <Row gutter={[8, 4]} style={{ marginTop: "4px" }}>
                                <Col xs={24} sm={6}>
                                  <Text>
                                    <small>
                                      Rata-rata:{" "}
                                      {data.averageScore?.toFixed(1) || 0}
                                    </small>
                                  </Text>
                                </Col>
                                <Col xs={24} sm={6}>
                                  <Text>
                                    <small>
                                      Kelulusan:{" "}
                                      {data.passRate?.toFixed(1) || 0}%
                                    </small>
                                  </Text>
                                </Col>
                                <Col xs={24} sm={6}>
                                  <Text>
                                    <small>
                                      Peserta: {data.participantCount || 0}
                                    </small>
                                  </Text>
                                </Col>
                                <Col xs={24} sm={6}>
                                  <Text>
                                    <small>
                                      Tertinggi:{" "}
                                      {data.highestScore?.toFixed(1) || 0}
                                    </small>
                                  </Text>
                                </Col>
                              </Row>
                            </div>
                          )
                        )}
                      </Card>
                    )}
                </div>
              ) : (
                <Alert
                  message="Analisis Kelas"
                  description="Analisis ujian sedang diproses dan akan tersedia segera."
                  type="info"
                  showIcon
                />
              )}
            </>
          ) : (
            <Alert
              message="Fitur Analisis"
              description="Analisis ujian hanya tersedia untuk Operator dan Guru."
              type="info"
              showIcon
            />
          )}
          <Divider />
          {/* Actions */}
          <div style={{ textAlign: "center" }}>
            {" "}
            <Button
              type="primary"
              size="large"
              onClick={() => navigate("/dashboard")}
              style={{ marginRight: "16px" }}
            >
              🏠 Kembali ke Dashboard
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  // Main exam interface - FULLSCREEN MODE
  return (
    <div
      style={{
        padding: 0, // Hilangkan padding untuk fullscreen
        margin: 0,
        minHeight: "100vh",
        height: "100vh", // Full viewport height
        backgroundColor: "#f5f5f5",
        overflow: "hidden", // Prevent scrolling in fullscreen
        position: "relative",
      }}
    >
      {/* Fullscreen Mode Indicator */}
      {isStarted && sessionActive && !isFinished && (
        <div
          style={{
            backgroundColor: "#52c41a",
            color: "white",
            textAlign: "center",
            padding: "4px 8px",
            fontSize: "12px",
            fontWeight: "500",
          }}
        >
          🔒 MODE UJIAN AKTIF - Fullscreen Wajib | ESC Dinonaktifkan
        </div>
      )}

      {/* Header - Compact for fullscreen */}
      <Card
        style={{
          marginBottom: "8px",
          marginLeft: "8px",
          marginRight: "8px",
          marginTop: isStarted && sessionActive && !isFinished ? "0" : "8px",
        }}
      >
        <Row align="middle" justify="space-between">
          <Col xs={24} sm={12} md={8}>
            <div>
              <Text strong style={{ fontSize: "16px" }}>
                {ujianData.namaUjian}
              </Text>
              <div>
                <Text type="secondary">
                  Kode: {ujianData.pengaturan?.kodeUjian}
                </Text>
              </div>
              <div>
                <Text type="secondary" style={{ fontSize: "12px" }}>
                  Session: {sessionId ? "Aktif" : "Tidak Aktif"}
                </Text>
              </div>
            </div>
          </Col>
          <Col xs={24} sm={12} md={8}>
            {ujianData.showTimerToParticipants && (
              <div style={{ textAlign: "center" }}>
                <div
                  style={{
                    fontSize: "24px",
                    fontWeight: "bold",
                    color: getTimerColor(),
                  }}
                >
                  <ClockCircleOutlined /> {formatTime(timeLeft)}
                </div>
                <Progress
                  percent={(timeLeft / (ujianData.durasiMenit * 60)) * 100}
                  strokeColor={getTimerColor()}
                  showInfo={false}
                  size="small"
                />
              </div>
            )}
          </Col>
          <Col xs={24} sm={24} md={8}>
            <div style={{ textAlign: "right" }}>
              <Space>
                {" "}
                {violationCount > 0 && (
                  <Badge
                    count={violationCount}
                    style={{
                      backgroundColor:
                        violationCount > 10
                          ? "#ff4d4f"
                          : violationCount > 5
                          ? "#fa8c16"
                          : "#faad14",
                    }}
                  >
                    <Tag
                      color={
                        violationCount > 10
                          ? "red"
                          : violationCount > 5
                          ? "orange"
                          : "warning"
                      }
                    >
                      Pelanggaran
                    </Tag>
                  </Badge>
                )}
                {renderAutoSaveStatus()}
                {screens.xs || screens.sm ? (
                  <Button
                    icon={
                      showSoalPanel ? <EyeInvisibleOutlined /> : <EyeOutlined />
                    }
                    onClick={() => setShowSoalPanel(!showSoalPanel)}
                  >
                    {showSoalPanel ? "Sembunyikan" : "Daftar Soal"}
                  </Button>
                ) : null}
                <Button icon={<SaveOutlined />} onClick={handleManualSave}>
                  Simpan
                </Button>
                <Button
                  type="primary"
                  danger
                  onClick={() => handleSubmitUjian(false)}
                >
                  <SendOutlined /> Kumpulkan
                </Button>
              </Space>
            </div>
          </Col>
        </Row>
      </Card>

      <div
        style={{
          padding: "0 8px",
          height: "calc(100vh - 120px)",
          overflow: "auto",
        }}
      >
        <Row gutter={8}>
          {/* Panel Soal */}
          {(!screens.xs && !screens.sm) || showSoalPanel ? (
            <Col xs={24} sm={24} md={6} style={{ marginBottom: "16px" }}>
              <Card
                title={`Daftar Soal (${jawabanStats.dijawab}/${soalList.length})`}
                size="small"
              >
                <div style={{ maxHeight: "400px", overflowY: "auto" }}>
                  <Row gutter={[8, 8]}>
                    {soalList.map((_, index) => (
                      <Col span={6} key={index}>
                        <Badge
                          dot={getSoalStatus(index) === "answered"}
                          color="#52c41a"
                        >
                          <Button
                            size="small"
                            type={currentSoal === index ? "primary" : "default"}
                            onClick={() => goToSoal(index)}
                            style={{
                              width: "100%",
                              backgroundColor:
                                getSoalStatus(index) === "answered"
                                  ? "#f6ffed"
                                  : undefined,
                            }}
                          >
                            {index + 1}
                          </Button>
                        </Badge>
                      </Col>
                    ))}
                  </Row>
                </div>

                <Divider />

                <Row gutter={8}>
                  <Col span={12}>
                    <div style={{ textAlign: "center" }}>
                      <Badge color="#52c41a" />
                      <Text style={{ fontSize: "12px" }}>
                        Dijawab: {jawabanStats.dijawab}
                      </Text>
                    </div>
                  </Col>
                  <Col span={12}>
                    <div style={{ textAlign: "center" }}>
                      <Badge color="#d9d9d9" />
                      <Text style={{ fontSize: "12px" }}>
                        Kosong: {jawabanStats.belumDijawab}
                      </Text>
                    </div>
                  </Col>
                </Row>
              </Card>
            </Col>
          ) : null}

          {/* Area Soal */}
          <Col
            xs={24}
            sm={24}
            md={(!screens.xs && !screens.sm) || showSoalPanel ? 18 : 24}
          >
            <Card>
              {soalList[currentSoal] && (
                <>
                  <div style={{ marginBottom: "16px" }}>
                    <Tag color="blue">
                      Soal {currentSoal + 1} dari {soalList.length}
                    </Tag>
                    <Tag color="purple">
                      Jenis: {soalList[currentSoal].jenisSoal}
                    </Tag>
                    <Tag color="orange">
                      Bobot: {soalList[currentSoal].bobot}
                    </Tag>
                    {getSoalStatus(currentSoal) === "answered" && (
                      <Tag color="green">Sudah Dijawab</Tag>
                    )}
                    {lastSaved && (
                      <Text
                        type="secondary"
                        style={{ fontSize: "12px", marginLeft: "8px" }}
                      >
                        Terakhir disimpan:{" "}
                        {moment(lastSaved).format("HH:mm:ss")}
                      </Text>
                    )}
                  </div>

                  <div
                    style={{
                      fontSize: "16px",
                      lineHeight: "1.6",
                      marginBottom: "24px",
                      fontWeight: "500",
                    }}
                  >
                    {soalList[currentSoal].pertanyaan}
                  </div>

                  {renderSoalComponent(soalList[currentSoal])}
                </>
              )}

              <Divider />

              <Row justify="space-between" align="middle">
                <Col>
                  <Button
                    icon={<LeftOutlined />}
                    onClick={prevSoal}
                    disabled={currentSoal === 0 || !ujianData.allowBacktrack}
                  >
                    Sebelumnya
                  </Button>
                </Col>
                <Col>
                  <Text type="secondary">
                    {currentSoal + 1} / {soalList.length}
                  </Text>
                </Col>
                <Col>
                  <Button
                    type="primary"
                    icon={<RightOutlined />}
                    onClick={nextSoal}
                    disabled={currentSoal === soalList.length - 1}
                  >
                    Selanjutnya
                  </Button>
                </Col>
              </Row>
            </Card>
          </Col>
        </Row>
        {/* Warning untuk waktu hampir habis */}
        {timeLeft <= 300 &&
          timeLeft > 0 &&
          ujianData.showTimerToParticipants && (
            <Modal
              title={
                <span style={{ color: "#fa8c16" }}>
                  <ExclamationCircleOutlined /> Peringatan!
                </span>
              }
              open={timeLeft <= 300 && timeLeft > 60}
              footer={null}
              closable={false}
              centered
            >
              <Alert
                message={`Waktu tersisa ${Math.floor(timeLeft / 60)} menit ${
                  timeLeft % 60
                } detik!`}
                description="Segera selesaikan ujian Anda. Ujian akan otomatis terkumpul ketika waktu habis."
                type="warning"
                showIcon
              />
            </Modal>
          )}{" "}
        {/* Modal notifikasi pelanggaran individual */}
        {violationModalVisible && (
          <Modal
            open={violationModalVisible}
            title={
              <span style={{ color: "#ff4d4f" }}>
                <ExclamationCircleOutlined /> Pelanggaran Terdeteksi!
              </span>
            }
            onOk={() => setViolationModalVisible(false)}
            onCancel={() => setViolationModalVisible(false)}
            okText="Mengerti"
            cancelButtonProps={{ style: { display: "none" } }}
            centered
            destroyOnClose
          >
            <Alert
              message={`Pelanggaran ${violationCount}: ${lastViolationType}`}
              description={
                <>
                  <div style={{ marginBottom: 8 }}>
                    <b>{violationDetails}</b>
                  </div>
                  <div>
                    Total pelanggaran: <b>{violationCount}</b>
                  </div>
                  <div style={{ color: "#ff4d4f", marginTop: 8 }}>
                    ⚠️ Peringatan: Jika pelanggaran melebihi 10 kali, ujian akan
                    otomatis dikumpulkan!
                  </div>
                </>
              }
              type="warning"
              showIcon
            />
          </Modal>
        )}
        {/* Modal pelanggaran kritis (>10) */}
        {criticalViolationModal && (
          <Modal
            open={criticalViolationModal}
            title={
              <span style={{ color: "#ff4d4f" }}>
                <ExclamationCircleOutlined /> UJIAN DIHENTIKAN!
              </span>
            }
            closable={false}
            footer={null}
            centered
            destroyOnClose
          >
            <Alert
              message="Ujian Anda telah dihentikan karena terlalu banyak pelanggaran!"
              description={
                <>
                  <div style={{ marginBottom: 8 }}>
                    Total pelanggaran: <b>{violationCount}</b>
                  </div>
                  <div style={{ marginBottom: 8 }}>
                    Pelanggaran terakhir: <b>{violationReason}</b>
                  </div>
                  <div style={{ color: "#ff4d4f", fontWeight: "bold" }}>
                    🚫 Ujian akan otomatis dikumpulkan dalam 3 detik...
                  </div>
                </>
              }
              type="error"
              showIcon
            />
          </Modal>
        )}
      </div>
    </div>
  );
};

export default UjianCATView;
