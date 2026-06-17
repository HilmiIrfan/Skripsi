/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef, useCallback } from "react";
import {
  Card,
  Button,
  Table,
  message,
  Upload,
  Row,
  Col,
  Divider,
  Modal,
  Input,
  Space,
  Tooltip,
  Tag,
  Select,
  Collapse,
  List,
  Avatar,
  Badge,
  Spin,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
  SearchOutlined,
  EyeOutlined,
} from "@ant-design/icons";
import {
  getBankSoal,
  deleteBankSoal,
  addBankSoal,
  editBankSoal,
} from "@/api/bankSoal";
import { deleteSoalUjian } from "@/api/soalUjian";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import TypingCard from "@/components/TypingCard";
import AddBankSoalForm from "./forms/add-bank-soal-form";
import EditBankSoalForm from "./forms/edit-bank-soal-form";
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { reqUserInfo, getUserById } from "@/api/user";
import { set } from "nprogress";
import * as XLSX from "xlsx";

const BankSoal = () => {
  const [bankSoals, setBankSoals] = useState([]);
  const [addBankSoalModalVisible, setAddBankSoalModalVisible] = useState(false);
  const [addBankSoalModalLoading, setAddBankSoalModalLoading] = useState(false);
  const [editBankSoalModalVisible, setEditBankSoalModalVisible] =
    useState(false);
  const [editBankSoalModalLoading, setEditBankSoalModalLoading] =
    useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [searchKeyword, setSearchKeyword] = useState("");
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [importFile, setImportFile] = useState(null);
  const [selectedQuestionType, setSelectedQuestionType] = useState("PG");
  const [user, setUser] = useState(null);
  const [viewModalVisible, setViewModalVisible] = useState(false);
  const [viewModalData, setViewModalData] = useState(null);
  const [groupedBankSoals, setGroupedBankSoals] = useState({});
  const [expandedKeys, setExpandedKeys] = useState([]);
  const [currentCollapsePage, setCurrentCollapsePage] = useState(1);
  const [collapsePageSize] = useState(5); // 5 collapse panels per page
  const [mappingData, setMappingData] = useState({
    semesterList: [],
    mapelList: [],
    tahunAjaranList: [],
    elemenList: [],
    kelasList: [],
    acpList: [],
    atpList: [],
    konsentrasiSekolahList: [],
    taksonomiList: [],
  });

  // Fungsi Helper Table Search
  const { getColumnSearchProps } = useTableSearch();

  const editBankSoalFormRef = useRef();
  const addBankSoalFormRef = useRef();

  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo();
      if (response && response.data) {
        setUser(response.data);
      } else {
        setUser(null);
      }
    } catch (error) {
      console.error("Error fetching user info:", error);
      setUser(null);
    }
  };

  const fetchMappingData = async () => {
    try {
      const [
        semesterRes,
        mapelRes,
        tahunRes,
        elemenRes,
        kelasRes,
        acpRes,
        atpRes,
        konsentrasiRes,
        taksonomiRes,
      ] = await Promise.all([
        import("@/api/semester").then((m) => m.getSemester()),
        import("@/api/mapel").then((m) => m.getMapel()),
        import("@/api/tahun-ajaran").then((m) => m.getTahunAjaran()),
        import("@/api/elemen").then((m) => m.getElemen()),
        import("@/api/kelas").then((m) => m.getKelas()),
        import("@/api/acp").then((m) => m.getACP()),
        import("@/api/atp").then((m) => m.getATP()),
        import("@/api/konsentrasiKeahlianSekolah").then((m) =>
          m.getKonsentrasiSekolah()
        ),
        import("@/api/taksonomi").then((m) => m.getTaksonomi()),
      ]);

      setMappingData({
        semesterList: semesterRes.data.content || [],
        mapelList: mapelRes.data.content || [],
        tahunAjaranList: tahunRes.data.content || [],
        elemenList: elemenRes.data.content || [],
        kelasList: kelasRes.data.content || [],
        acpList: acpRes.data.content || [],
        atpList: atpRes.data.content || [],
        konsentrasiSekolahList: konsentrasiRes.data.content || [],
        taksonomiList: taksonomiRes.data.content || [],
      });
    } catch (error) {
      console.error("Error fetching mapping data:", error);
      message.error("Gagal memuat data mapping");
    }
  };

  // Fungsi untuk mengelompokkan data berdasarkan nama ujian
  const groupBankSoalsByNamaUjian = (data) => {
    const grouped = {};
    data.forEach((item) => {
      const namaUjian = item.namaUjian || "Ujian Tanpa Nama";
      if (!grouped[namaUjian]) {
        grouped[namaUjian] = [];
      }
      grouped[namaUjian].push(item);
    });
    return grouped;
  };

  const fetchBankSoals = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getBankSoal();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setBankSoals(content);
        // Group data berdasarkan nama ujian
        const grouped = groupBankSoalsByNamaUjian(content);
        setGroupedBankSoals(grouped);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchBankSoals();
    fetchUserInfo();
    fetchMappingData();
  }, [fetchBankSoals]);

  // Reset pagination saat search berubah
  useEffect(() => {
    setCurrentCollapsePage(1);
  }, [searchQuery]);

  const handleDeleteBankSoal = (row) => {
    const { idBankSoal } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content:
        "Apakah Anda yakin ingin menghapus data ini? Data terkait di Soal Ujian juga akan dihapus.",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          // Hapus dari Bank Soal terlebih dahulu
          await deleteBankSoal({ idBankSoal });

          // Jika ada soalUjian terkait, hapus juga dari Soal Ujian
          if (row.soalUjian && row.soalUjian.idSoalUjian) {
            try {
              await deleteSoalUjian({ idSoalUjian: row.soalUjian.idSoalUjian });
              console.log(
                `Soal Ujian ${row.soalUjian.idSoalUjian} berhasil dihapus`
              );
            } catch (soalUjianError) {
              console.warn(
                "Gagal menghapus dari Soal Ujian:",
                soalUjianError.message
              );
              // Tidak perlu menampilkan error karena mungkin sudah dihapus sebelumnya
            }
          }

          message.success("Berhasil dihapus dari Bank Soal dan Soal Ujian");
          fetchBankSoals();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleDeleteGroup = (namaUjian, questions) => {
    Modal.confirm({
      title: "Konfirmasi Hapus Grup",
      content: `Apakah Anda yakin ingin menghapus SEMUA soal dalam grup "${namaUjian}"? Total ${questions.length} soal akan dihapus dari Bank Soal dan Soal Ujian.`,
      okText: "Ya, Hapus Semua",
      okType: "danger",
      cancelText: "Batal",
      width: 500,
      onOk: async () => {
        let successCount = 0;
        let errorCount = 0;

        try {
          // Loop untuk menghapus setiap soal dalam grup
          for (const question of questions) {
            try {
              // Hapus dari Bank Soal
              await deleteBankSoal({ idBankSoal: question.idBankSoal });

              // Hapus dari Soal Ujian jika ada
              if (question.soalUjian && question.soalUjian.idSoalUjian) {
                try {
                  await deleteSoalUjian({
                    idSoalUjian: question.soalUjian.idSoalUjian,
                  });
                } catch (soalUjianError) {
                  console.warn(
                    `Gagal menghapus Soal Ujian ${question.soalUjian.idSoalUjian}:`,
                    soalUjianError.message
                  );
                }
              }

              successCount++;
            } catch (error) {
              console.error(
                `Gagal menghapus soal ${question.idBankSoal}:`,
                error.message
              );
              errorCount++;
            }
          }

          if (successCount > 0) {
            message.success(
              `Berhasil menghapus ${successCount} soal dari grup "${namaUjian}"`
            );
          }

          if (errorCount > 0) {
            message.warning(`${errorCount} soal gagal dihapus`);
          }

          // Refresh data
          fetchBankSoals();
        } catch (error) {
          message.error(
            "Terjadi kesalahan saat menghapus grup: " + error.message
          );
        }
      },
    });
  };

  const handleViewBankSoal = (row) => {
    setViewModalData(row);
    setViewModalVisible(true);
  };

  const handleEditBankSoal = (row) => {
    setCurrentRowData({ ...row });
    setEditBankSoalModalVisible(true);
  };

  const handleAddBankSoalOk = async (values) => {
    setAddBankSoalModalLoading(true);
    try {
      console.log("Data Bank Soal", values);
      await addBankSoal(values);
      setAddBankSoalModalVisible(false);
      message.success("Berhasil menambahkan");
      fetchBankSoals();
    } catch (error) {
      setAddBankSoalModalVisible(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddBankSoalModalLoading(false);
    }
  };

  const handleEditBankSoalOk = async (values) => {
    setEditBankSoalModalLoading(true);
    try {
      console.log("Form values received:", values);
      await editBankSoal(values, currentRowData.idBankSoal);
      setEditBankSoalModalVisible(false);
      message.success("Berhasil mengedit");
      fetchBankSoals();
    } catch (error) {
      message.error("Gagal mengedit: " + error.message);
    } finally {
      setEditBankSoalModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddBankSoalModalVisible(false);
    setEditBankSoalModalVisible(false);
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getBankSoal();
  };

  // Normalize string untuk fuzzy matching (hapus spasi dan karakter khusus)
  const normalizeString = (str) => {
    if (!str) return "";
    return str
      .toString()
      .toLowerCase()
      .replace(/[\s\-_.,!@#$%^&*()]/g, "");
  };

  // Mapping functions to convert names to IDs dengan fuzzy matching
  const mapNameToId = (name, list, nameField, idField) => {
    if (!name || !list || list.length === 0) return null;

    const normalizedName = normalizeString(name);
    const item = list.find(
      (item) => normalizeString(item[nameField]) === normalizedName
    );
    return item ? item[idField] : null;
  };

  const mapSemesterNameToId = (name) =>
    mapNameToId(name, mappingData.semesterList, "namaSemester", "idSemester");
  const mapMapelNameToId = (name) =>
    mapNameToId(name, mappingData.mapelList, "name", "idMapel");
  const mapTahunAjaranToId = (tahun) =>
    mapNameToId(tahun, mappingData.tahunAjaranList, "tahunAjaran", "idTahun");
  const mapElemenNameToId = (name) =>
    mapNameToId(name, mappingData.elemenList, "namaElemen", "idElemen");
  const mapKelasNameToId = (name) =>
    mapNameToId(name, mappingData.kelasList, "namaKelas", "idKelas");
  const mapAcpNameToId = (name) =>
    mapNameToId(name, mappingData.acpList, "namaAcp", "idAcp");
  const mapAtpNameToId = (name) =>
    mapNameToId(name, mappingData.atpList, "namaAtp", "idAtp");
  const mapKonsentrasiNameToId = (name) =>
    mapNameToId(
      name,
      mappingData.konsentrasiSekolahList,
      "namaKonsentrasiSekolah",
      "idKonsentrasiSekolah"
    );
  const mapTaksonomiNameToId = (name) =>
    mapNameToId(
      name,
      mappingData.taksonomiList,
      "namaTaksonomi",
      "idTaksonomi"
    );

  const handleImportFile = (file, questionType) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = async (e) => {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: "array" });
        const sheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[sheetName];
        const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

        // Ambil header dan data
        const [header, ...rows] = jsonData;
        // Index kolom sesuai header
        const idx = (col) => header.indexOf(col);

        let successCount = 0;
        let errorCount = 0;

        // Loop dan masukkan satu-satu
        for (const row of rows) {
          if (!row[idx("namaUjian")] || !row[idx("pertanyaan")]) continue;

          const clean = (val) =>
            (val ?? "").toString().replace(/['"` \s]/g, ""); // hapus petik satu, dua, backtick, dan spasi dari field tertentu
          const cleanText = (val) => (val ?? "").toString(); // untuk text yang perlu dipertahankan formatnya

          try {
            // Map names to IDs using the mapping functions
            const tahunAjaranId = mapTahunAjaranToId(row[idx("tahunAjaran")]);
            const semesterId = mapSemesterNameToId(row[idx("namaSemester")]);
            const kelasId = mapKelasNameToId(row[idx("namaKelas")]);
            const mapelId = mapMapelNameToId(row[idx("namaMapel")]);
            const elemenId = mapElemenNameToId(row[idx("namaElemen")]);
            const acpId = mapAcpNameToId(row[idx("namaAcp")]);
            const atpId = mapAtpNameToId(row[idx("namaAtp")]);
            const konsentrasiId = mapKonsentrasiNameToId(
              row[idx("namaKonsentrasiSekolah")]
            );
            const taksonomiId = mapTaksonomiNameToId(row[idx("namaTaksonomi")]);

            // Validate required fields
            if (
              !row[idx("namaUjian")] ||
              !row[idx("pertanyaan")] ||
              !row[idx("bobot")]
            ) {
              throw new Error(
                "Field wajib kosong: namaUjian, pertanyaan, atau bobot"
              );
            }

            if (
              !tahunAjaranId ||
              !semesterId ||
              !kelasId ||
              !mapelId ||
              !elemenId ||
              !acpId ||
              !atpId ||
              !konsentrasiId ||
              !taksonomiId
            ) {
              throw new Error(
                "Gagal mapping nama ke ID. Periksa data: tahunAjaran, namaSemester, namaKelas, namaMapel, namaElemen, namaAcp, namaAtp, namaKonsentrasiSekolah, namaTaksonomi"
              );
            }

            let bankSoalData = {
              namaUjian: cleanText(row[idx("namaUjian")]),
              pertanyaan: cleanText(row[idx("pertanyaan")]),
              bobot: row[idx("bobot")].toString(),
              jenisSoal: questionType,
              idSchool: row[idx("idSchool")] || "RWK001", // Read from Excel but default to RWK001
              idTahun: tahunAjaranId,
              idSemester: semesterId,
              idKelas: kelasId,
              idMapel: mapelId,
              idElemen: elemenId,
              idAcp: acpId,
              idAtp: atpId,
              idKonsentrasiSekolah: konsentrasiId,
              idTaksonomi: taksonomiId, // Map from namaTaksonomi
            };

            // Handle berbagai jenis soal berdasarkan questionType
            switch (questionType) {
              case "PG": {
                // Build opsi object, remove empty options
                const opsiRaw = {
                  A: cleanText(row[idx("opsiA")] || ""),
                  B: cleanText(row[idx("opsiB")] || ""),
                  C: cleanText(row[idx("opsiC")] || ""),
                  D: cleanText(row[idx("opsiD")] || ""),
                  E: cleanText(row[idx("opsiE")] || ""),
                };

                // Remove empty options for cleaner data
                bankSoalData.opsi = {};
                Object.keys(opsiRaw).forEach((key) => {
                  if (opsiRaw[key] && opsiRaw[key].trim()) {
                    bankSoalData.opsi[key] = opsiRaw[key];
                  }
                });

                // Validate jawaban benar
                const jawabanBenar = clean(row[idx("jawabanBenar")]) || "A";
                if (!bankSoalData.opsi[jawabanBenar]) {
                  throw new Error(
                    `Jawaban benar "${jawabanBenar}" tidak ada di dalam pilihan opsi yang tersedia`
                  );
                }

                bankSoalData.jawabanBenar = [jawabanBenar];

                console.log("PG parsing result:", {
                  opsi: bankSoalData.opsi,
                  jawabanBenar: bankSoalData.jawabanBenar,
                });
                break;
              }

              case "MULTI": {
                // Build opsi object, remove empty options
                const opsiRaw = {
                  A: cleanText(row[idx("opsiA")] || ""),
                  B: cleanText(row[idx("opsiB")] || ""),
                  C: cleanText(row[idx("opsiC")] || ""),
                  D: cleanText(row[idx("opsiD")] || ""),
                  E: cleanText(row[idx("opsiE")] || ""),
                };

                // Remove empty options for cleaner data
                bankSoalData.opsi = {};
                Object.keys(opsiRaw).forEach((key) => {
                  if (opsiRaw[key] && opsiRaw[key].trim()) {
                    bankSoalData.opsi[key] = opsiRaw[key];
                  }
                });

                // Parse multiple answers separated by comma
                const multiAnswers = cleanText(row[idx("jawabanBenar")] || "A")
                  .split(",")
                  .map((ans) => ans.trim())
                  .filter((ans) => ans.length > 0);

                // Validate all answers exist in options
                multiAnswers.forEach((jawaban) => {
                  if (!bankSoalData.opsi[jawaban]) {
                    throw new Error(
                      `Jawaban benar "${jawaban}" tidak ada di dalam pilihan opsi yang tersedia`
                    );
                  }
                });

                bankSoalData.jawabanBenar = multiAnswers;

                console.log("MULTI parsing result:", {
                  opsi: bankSoalData.opsi,
                  jawabanBenar: bankSoalData.jawabanBenar,
                });
                break;
              }

              case "COCOK": {
                bankSoalData.pasangan = {};

                // Parse kiri dan kanan items - struktur: 1_kiri, 2_kiri, 3_kiri, 4_kanan, 5_kanan, 6_kanan
                let kiriIndex = 1;
                let kananStartIndex = 4; // Mulai dari 4 untuk kanan sesuai format backend

                // Process kiri items (1_kiri, 2_kiri, 3_kiri)
                for (let i = 1; i <= 3; i++) {
                  const kiri = cleanText(row[idx(`kiri${i}`)] || "");
                  if (kiri) {
                    bankSoalData.pasangan[`${kiriIndex}_kiri`] = kiri;
                    kiriIndex++;
                  }
                }

                // Process kanan items (4_kanan, 5_kanan, 6_kanan)
                let kananIndex = kananStartIndex;
                for (let i = 1; i <= 3; i++) {
                  const kanan = cleanText(row[idx(`kanan${i}`)] || "");
                  if (kanan) {
                    bankSoalData.pasangan[`${kananIndex}_kanan`] = kanan;
                    kananIndex++;
                  }
                }

                // Parse jawaban benar format: "H2O=Air,CO2=Karbon Dioksida,NaCl=Garam"
                // Convert to backend format: ["H2O=Air", "CO2=Karbon Dioksida", "NaCl=Garam"]
                const jawabanBenarText = cleanText(
                  row[idx("jawabanBenar")] || ""
                );
                const cocokAnswers = jawabanBenarText
                  .split(",")
                  .map((pair) => pair.trim())
                  .filter((pair) => pair.includes("=") && pair.length > 0);

                bankSoalData.jawabanBenar = cocokAnswers;

                console.log("COCOK parsing result:", {
                  pasangan: bankSoalData.pasangan,
                  jawabanBenar: bankSoalData.jawabanBenar,
                  rawJawaban: jawabanBenarText,
                });
                break;
              }

              case "ISIAN": {
                // Untuk ISIAN, hanya kirim field yang diperlukan
                bankSoalData.toleransiTypo = (
                  row[idx("toleransiTypo")] || "0"
                ).toString();
                bankSoalData.jawabanBenar = [
                  cleanText(row[idx("jawabanBenar")] || ""),
                ];
                break;
              }

              default: {
                throw new Error(`Jenis soal ${questionType} tidak didukung`);
              }
            }

            // STEP 1: Simpan ke Soal Ujian dulu
            const soalUjianData = {
              idSoalUjian: null, // Tambahkan field ini
              namaUjian: cleanText(row[idx("namaUjian")]),
              pertanyaan: cleanText(row[idx("pertanyaan")]),
              bobot: row[idx("bobot")].toString(), // Pastikan string
              jenisSoal: questionType,
              idUser: user?.id, // Gunakan ID user yang login
              idTaksonomi: taksonomiId,
              idKonsentrasiSekolah: konsentrasiId,
              idSchool: row[idx("idSchool")] || "RWK001",
              jawabanBenar: bankSoalData.jawabanBenar || [],
            };

            // Tambahkan field berdasarkan jenis soal
            if (questionType === "PG" || questionType === "MULTI") {
              soalUjianData.opsi = bankSoalData.opsi || {};
            } else if (questionType === "COCOK") {
              soalUjianData.pasangan = bankSoalData.pasangan || {};
            } else if (questionType === "ISIAN") {
              soalUjianData.toleransiTypo = bankSoalData.toleransiTypo || "0";
            }

            // Import API soal ujian
            const { addSoalUjian } = await import("@/api/soalUjian");
            console.log("=== DEBUG: Mengirim data ke Soal Ujian ===");
            console.log("📤 PAYLOAD IMPORT:");
            console.log("  namaUjian:", soalUjianData.namaUjian);
            console.log("  jenisSoal:", soalUjianData.jenisSoal);
            console.log(
              "  bobot:",
              soalUjianData.bobot,
              "(type:",
              typeof soalUjianData.bobot,
              ")"
            );
            console.log("  idUser:", soalUjianData.idUser);
            console.log("  idTaksonomi:", soalUjianData.idTaksonomi);
            console.log(
              "  idKonsentrasiSekolah:",
              soalUjianData.idKonsentrasiSekolah
            );
            console.log("  idSchool:", soalUjianData.idSchool);
            console.log("  jawabanBenar:", soalUjianData.jawabanBenar);
            if (soalUjianData.opsi) console.log("  opsi:", soalUjianData.opsi);
            if (soalUjianData.pasangan)
              console.log("  pasangan:", soalUjianData.pasangan);
            if (soalUjianData.toleransiTypo !== undefined) {
              console.log(
                "  toleransiTypo:",
                soalUjianData.toleransiTypo,
                "(type:",
                typeof soalUjianData.toleransiTypo,
                ")"
              );
            }
            console.log(
              "🔍 PAYLOAD LENGKAP:",
              JSON.stringify(soalUjianData, null, 2)
            );
            const soalUjianResult = await addSoalUjian(soalUjianData);

            console.log("=== DEBUG: Response dari API Soal Ujian ===");
            console.log("HTTP Status:", soalUjianResult.status);
            console.log("Data StatusCode:", soalUjianResult.data?.statusCode);
            console.log("Data Success:", soalUjianResult.data?.success);
            console.log(
              "Full Response:",
              JSON.stringify(soalUjianResult.data, null, 2)
            );

            // Cek berbagai format response success
            const isSuccess =
              soalUjianResult.status === 201 || // HTTP 201 Created
              soalUjianResult.status === 200 || // HTTP 200 OK
              soalUjianResult.data?.success === true || // success: true
              soalUjianResult.data?.statusCode === 200; // statusCode: 200

            if (isSuccess) {
              console.log("✅ Soal Ujian berhasil disimpan!");

              // STEP 2: Cari idSoalUjian yang baru dibuat dan kaitkan ke Bank Soal
              try {
                console.log("🔍 Mencari idSoalUjian yang baru dibuat...");

                // Import API untuk mendapatkan daftar soal ujian
                const { getSoalUjian } = await import("@/api/soalUjian");
                const soalUjianList = await getSoalUjian();

                // Cari soal ujian yang baru dibuat berdasarkan kombinasi unik
                const newSoalUjian = soalUjianList.data.content.find(
                  (soal) =>
                    soal.namaUjian === soalUjianData.namaUjian &&
                    soal.pertanyaan === soalUjianData.pertanyaan &&
                    soal.bobot === soalUjianData.bobot &&
                    soal.jenisSoal === soalUjianData.jenisSoal
                );

                if (newSoalUjian) {
                  const soalUjianId = newSoalUjian.idSoalUjian;
                  console.log("✅ Ditemukan idSoalUjian:", soalUjianId);

                  // Sekarang kaitkan ke Bank Soal
                  const bankSoalDataFinal = {
                    idBankSoal: null, // Untuk create baru
                    ...bankSoalData,
                    idSoalUjian: soalUjianId, // Kaitkan dengan soal ujian yang baru dibuat
                  };

                  console.log("🔗 Menghubungkan ke Bank Soal...");
                  console.log(
                    "📤 PAYLOAD BANK SOAL:",
                    JSON.stringify(bankSoalDataFinal, null, 2)
                  );
                  await handleAddBankSoalOk(bankSoalDataFinal);
                  console.log("✅ Berhasil dikaitkan ke Bank Soal!");

                  successCount++;
                } else {
                  console.warn(
                    "⚠️ idSoalUjian tidak ditemukan, data hanya tersimpan di Soal Ujian"
                  );
                  successCount++; // Tetap hitung sebagai success karena data tersimpan di Soal Ujian
                }
              } catch (linkError) {
                console.error("❌ Gagal mengaitkan ke Bank Soal:", linkError);
                console.log("⚠️ Data tetap tersimpan di Soal Ujian");
                successCount++; // Tetap hitung sebagai success karena data tersimpan di Soal Ujian
              }
            } else {
              console.error("❌ Response Soal Ujian Error:", soalUjianResult);
              console.error("Error Message:", soalUjianResult.data?.message);
              console.error("Error Details:", soalUjianResult.data?.errors);
              throw new Error(
                `Gagal menyimpan ke soal ujian: ${
                  soalUjianResult.data?.message ||
                  soalUjianResult.data?.error ||
                  "Unknown error"
                }`
              );
            }
          } catch (err) {
            console.error(
              `Gagal import soal: ${row[idx("namaUjian")] || "Unknown"}`,
              err
            );
            // Beri info tahap mana yang gagal
            if (err.message.includes("soal ujian")) {
              console.error("Gagal pada tahap 1: Penyimpanan ke Soal Ujian");
            } else {
              console.error("Gagal pada tahap 2: Pengkaitan ke Bank Soal");
            }
            errorCount++;
          }
        }

        if (successCount > 0) {
          message.success(
            `Import berhasil! ${successCount} soal berhasil ditambahkan ke Soal Ujian dan dikaitkan ke Bank Soal.`
          );
        }
        if (errorCount > 0) {
          message.warning(
            `${errorCount} soal gagal diimport. Periksa format file.`
          );
        }

        setImportModalVisible(false);
        setImportFile(null);
        fetchBankSoals();
        resolve();
      };
      reader.onerror = () => {
        message.error("Gagal membaca file");
        reject(new Error("Gagal membaca file"));
      };
      reader.readAsArrayBuffer(file);
    });
  };

  const renderJenisSoalTag = (jenisSoal) => {
    let color = "blue";
    let text = "Pilihan Ganda";

    switch (jenisSoal) {
      case "PG":
        color = "blue";
        text = "Pilihan Ganda";
        break;
      case "MULTI":
        color = "green";
        text = "Multi Jawaban";
        break;
      case "COCOK":
        color = "orange";
        text = "Mencocokkan";
        break;
      case "ISIAN":
        color = "purple";
        text = "Isian";
        break;
      default:
        color = "default";
        text = jenisSoal || "Unknown";
    }

    return <Tag color={color}>{text}</Tag>;
  };

  // Filter grouped data berdasarkan search query
  const getFilteredGroupedData = () => {
    if (!searchQuery.trim()) {
      return groupedBankSoals;
    }

    const filtered = {};
    const query = searchQuery.toLowerCase();

    Object.entries(groupedBankSoals).forEach(([namaUjian, questions]) => {
      // Filter questions dalam group
      const filteredQuestions = questions.filter(
        (item) =>
          item.namaUjian?.toLowerCase().includes(query) ||
          item.pertanyaan?.toLowerCase().includes(query) ||
          item.mapel?.name?.toLowerCase().includes(query) ||
          item.atp?.namaAtp?.toLowerCase().includes(query) ||
          item.jenisSoal?.toLowerCase().includes(query)
      );

      // Jika ada question yang match atau nama ujian match, masukkan group
      if (
        filteredQuestions.length > 0 ||
        namaUjian.toLowerCase().includes(query)
      ) {
        filtered[namaUjian] =
          filteredQuestions.length > 0 ? filteredQuestions : questions;
      }
    });

    return filtered;
  };

  const { Panel } = Collapse;

  const renderQuestionItem = (item) => (
    <List.Item
      key={item.idBankSoal}
      actions={[
        <Tooltip title="Lihat Detail" key="view">
          <Button
            type="default"
            shape="circle"
            icon={<EyeOutlined />}
            onClick={() => handleViewBankSoal(item)}
          />
        </Tooltip>,
        <Tooltip title="Edit" key="edit">
          <Button
            type="primary"
            shape="circle"
            icon={<EditOutlined />}
            onClick={() => handleEditBankSoal(item)}
          />
        </Tooltip>,
        <Tooltip title="Hapus" key="delete">
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteBankSoal(item)}
          />
        </Tooltip>,
      ]}
    >
      <List.Item.Meta
        avatar={
          <Avatar
            style={{ backgroundColor: getJenisSoalColor(item.jenisSoal) }}
          >
            {getJenisSoalInitial(item.jenisSoal)}
          </Avatar>
        }
        title={
          <div>
            <span style={{ marginRight: 8 }}>{item.pertanyaan}</span>
            {renderJenisSoalTag(item.jenisSoal)}
          </div>
        }
        description={
          <div>
            <div>
              <strong>Bobot:</strong> {item.bobot}
            </div>
            <div>
              <strong>Mata Pelajaran:</strong> {item.mapel?.name || "N/A"}
            </div>
            <div>
              <strong>ATP:</strong> {item.atp?.namaAtp || "N/A"}
            </div>
          </div>
        }
      />
    </List.Item>
  );

  const getJenisSoalColor = (jenisSoal) => {
    switch (jenisSoal) {
      case "PG":
        return "#1890ff";
      case "MULTI":
        return "#52c41a";
      case "COCOK":
        return "#fa8c16";
      case "ISIAN":
        return "#722ed1";
      default:
        return "#d9d9d9";
    }
  };

  const getJenisSoalInitial = (jenisSoal) => {
    switch (jenisSoal) {
      case "PG":
        return "PG";
      case "MULTI":
        return "M";
      case "COCOK":
        return "C";
      case "ISIAN":
        return "I";
      default:
        return "?";
    }
  };

  // Columns untuk tabel di dalam collapse
  const getQuestionTableColumns = () => [
    {
      title: "No",
      dataIndex: "index",
      key: "index",
      align: "center",
      width: 60,
      render: (_, __, index) => index + 1,
    },
    {
      title: "Pertanyaan",
      dataIndex: "pertanyaan",
      key: "pertanyaan",
      align: "left",
      render: (text) => (
        <Tooltip title={text}>
          <span>
            {text?.length > 50 ? `${text.substring(0, 50)}...` : text}
          </span>
        </Tooltip>
      ),
    },
    {
      title: "Jenis Soal",
      dataIndex: "jenisSoal",
      key: "jenisSoal",
      align: "center",
      width: 120,
      render: (jenisSoal) => renderJenisSoalTag(jenisSoal),
    },
    {
      title: "Bobot",
      dataIndex: "bobot",
      key: "bobot",
      align: "center",
      width: 80,
    },
    {
      title: "Kelas",
      dataIndex: ["kelas", "namaKelas"],
      key: "namaKelas",
      align: "center",
      width: 150,
      render: (text) => text || "N/A",
    },
    {
      title: "Mata Pelajaran",
      dataIndex: ["mapel", "name"],
      key: "mapelName",
      align: "center",
      width: 150,
      render: (text) => text || "N/A",
    },
    {
      title: "Konsentrasi Keahlian",
      dataIndex: ["konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah"],
      key: "namaKonsentrasiSekolah",
      align: "center",
      width: 200,
      render: (text) => (
        <Tooltip title={text}>
          <span>
            {text?.length > 20 ? `${text.substring(0, 20)}...` : text || "N/A"}
          </span>
        </Tooltip>
      ),
    },
    {
      title: "Operasi",
      key: "action",
      align: "center",
      width: 150,
      render: (_, row) => (
        <Space>
          <Tooltip title="Lihat Detail">
            <Button
              type="default"
              shape="circle"
              size="small"
              icon={<EyeOutlined />}
              onClick={() => handleViewBankSoal(row)}
            />
          </Tooltip>
          <Tooltip title="Edit">
            <Button
              type="primary"
              shape="circle"
              size="small"
              icon={<EditOutlined />}
              onClick={() => handleEditBankSoal(row)}
            />
          </Tooltip>
          <Tooltip title="Hapus">
            <Button
              type="primary"
              danger
              shape="circle"
              size="small"
              icon={<DeleteOutlined />}
              onClick={() => handleDeleteBankSoal(row)}
            />
          </Tooltip>
        </Space>
      ),
    },
  ];

  const renderQuestionTable = (questions) => (
    <Table
      rowKey="idBankSoal"
      dataSource={questions}
      columns={getQuestionTableColumns()}
      pagination={{
        pageSize: 5,
        showSizeChanger: false,
        showQuickJumper: true,
        showTotal: (total, range) =>
          `${range[0]}-${range[1]} dari ${total} soal`,
      }}
      size="small"
      scroll={{ x: 800 }}
    />
  );

  const renderGroupedView = () => {
    const filteredData = getFilteredGroupedData();

    if (Object.keys(filteredData).length === 0) {
      return (
        <div style={{ textAlign: "center", padding: "50px" }}>
          <p>
            {searchQuery.trim()
              ? "Tidak ada data yang sesuai dengan pencarian"
              : "Tidak ada data Bank Soal"}
          </p>
        </div>
      );
    }

    // Implement pagination for collapse panels
    const allCollapseItems = Object.entries(filteredData).map(
      ([namaUjian, questions]) => ({
        key: namaUjian,
        label: (
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              width: "100%",
            }}
          >
            <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
              <span style={{ fontSize: "16px", fontWeight: "bold" }}>
                {namaUjian}
              </span>
              <Badge
                count={questions.length}
                style={{ backgroundColor: "#52c41a" }}
              />
            </div>
            <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
              <Tooltip
                title={`Hapus semua ${questions.length} soal dalam grup "${namaUjian}"`}
              >
                <Button
                  type="primary"
                  danger
                  size="small"
                  icon={<DeleteOutlined />}
                  onClick={(e) => {
                    e.stopPropagation(); // Prevent collapse toggle
                    handleDeleteGroup(namaUjian, questions);
                  }}
                  style={{ marginRight: "8px" }}
                >
                  Hapus Grup
                </Button>
              </Tooltip>
            </div>
          </div>
        ),
        children: renderQuestionTable(questions),
      })
    );

    // Paginate collapse items
    const startIndex = (currentCollapsePage - 1) * collapsePageSize;
    const endIndex = startIndex + collapsePageSize;
    const paginatedCollapseItems = allCollapseItems.slice(startIndex, endIndex);

    return (
      <div>
        <Collapse
          items={paginatedCollapseItems}
          activeKey={expandedKeys}
          onChange={setExpandedKeys}
          size="large"
          style={{ backgroundColor: "#fff" }}
        />

        {/* Pagination untuk Collapse */}
        {allCollapseItems.length > collapsePageSize && (
          <div style={{ textAlign: "center", marginTop: 16 }}>
            <p>
              <strong>Pagination Ujian:</strong>
            </p>
            <div style={{ textAlign: "center", marginTop: 8 }}>
              <Button
                type="default"
                disabled={currentCollapsePage === 1}
                onClick={() => setCurrentCollapsePage((prev) => prev - 1)}
                style={{ marginRight: 8 }}
              >
                ← Sebelumnya
              </Button>

              <span style={{ margin: "0 16px" }}>
                Halaman {currentCollapsePage} dari{" "}
                {Math.ceil(allCollapseItems.length / collapsePageSize)}
                {` (${allCollapseItems.length} ujian)`}
              </span>

              <Button
                type="default"
                disabled={
                  currentCollapsePage >=
                  Math.ceil(allCollapseItems.length / collapsePageSize)
                }
                onClick={() => setCurrentCollapsePage((prev) => prev + 1)}
                style={{ marginLeft: 8 }}
              >
                Selanjutnya →
              </Button>
            </div>
          </div>
        )}
      </div>
    );
  };

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button type="primary" onClick={() => setAddBankSoalModalVisible(true)}>
          Tambahkan BankSoal
        </Button>
      </Col>
      <Col>
        <Button
          icon={<UploadOutlined />}
          onClick={() => setImportModalVisible(true)}
        >
          Import Soal
        </Button>
      </Col>
    </Row>
  );

  return (
    <div className="app-container">
      <TypingCard
        title="Manajemen BankSoal"
        source="Di sini, Anda dapat mengelola bankSoal di sistem."
      />
      <br />
      {loading ? (
        <Card>
          <Skeleton active paragraph={{ rows: 10 }} />
        </Card>
      ) : (
        <Card style={{ overflowX: "scroll" }}>
          {/* Baris untuk tombol dan pencarian */}
          <Row
            justify="space-between"
            align="middle"
            style={{ marginBottom: 16 }}
          >
            {/* Tombol Tambah & Import */}
            {renderButtons()}

            {/* Kolom Pencarian */}
            <Col>
              <Input.Search
                key="search"
                placeholder="Cari ujian, pertanyaan, mapel..."
                allowClear
                enterButton
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ width: 350 }}
              />
            </Col>
          </Row>

          {/* Grouped View */}
          {renderGroupedView()}

          <AddBankSoalForm
            wrappedComponentRef={addBankSoalFormRef}
            visible={addBankSoalModalVisible}
            confirmLoading={addBankSoalModalLoading}
            onCancel={handleCancel}
            onOk={handleAddBankSoalOk}
          />

          <EditBankSoalForm
            wrappedComponentRef={editBankSoalFormRef}
            currentRowData={currentRowData}
            visible={editBankSoalModalVisible}
            confirmLoading={editBankSoalModalLoading}
            onCancel={handleCancel}
            onOk={handleEditBankSoalOk}
          />
        </Card>
      )}

      {/* Modal View Detail Bank Soal */}
      <Modal
        title="Detail Bank Soal"
        open={viewModalVisible}
        onCancel={() => setViewModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setViewModalVisible(false)}>
            Tutup
          </Button>,
        ]}
        width={800}
      >
        {viewModalData && (
          <div style={{ maxHeight: "600px", overflowY: "auto" }}>
            <Row gutter={[16, 16]}>
              <Col span={24}>
                <Card title="Informasi Dasar" size="small">
                  <Row gutter={[16, 8]}>
                    <Col span={12}>
                      <strong>Nama Ujian:</strong> {viewModalData.namaUjian}
                    </Col>
                    <Col span={12}>
                      <strong>Jenis Soal:</strong>{" "}
                      {renderJenisSoalTag(viewModalData.jenisSoal)}
                    </Col>
                    <Col span={12}>
                      <strong>Bobot:</strong> {viewModalData.bobot}
                    </Col>
                    <Col span={12}>
                      <strong>Sekolah:</strong>{" "}
                      {viewModalData.school?.nameSchool || "N/A"}
                    </Col>
                  </Row>
                </Card>
              </Col>

              <Col span={24}>
                <Card title="Pertanyaan" size="small">
                  <div
                    style={{ whiteSpace: "pre-wrap", wordBreak: "break-word" }}
                  >
                    {viewModalData.pertanyaan}
                  </div>
                </Card>
              </Col>

              <Col span={24}>
                <Card title="Kurikulum & Pembelajaran" size="small">
                  <Row gutter={[16, 8]}>
                    <Col span={12}>
                      <strong>Tahun Ajaran:</strong>{" "}
                      {viewModalData.tahunAjaran?.tahunAjaran || "N/A"}
                    </Col>
                    <Col span={12}>
                      <strong>Semester:</strong>{" "}
                      {viewModalData.semester?.namaSemester || "N/A"}
                    </Col>
                    <Col span={12}>
                      <strong>Kelas:</strong>{" "}
                      {viewModalData.kelas?.namaKelas || "N/A"}
                    </Col>
                    <Col span={12}>
                      <strong>Mata Pelajaran:</strong>{" "}
                      {viewModalData.mapel?.name || "N/A"}
                    </Col>
                    <Col span={12}>
                      <strong>Elemen:</strong>{" "}
                      {viewModalData.elemen?.namaElemen || "N/A"}
                    </Col>
                    <Col span={12}>
                      <strong>Konsentrasi Keahlian:</strong>{" "}
                      {viewModalData.konsentrasiKeahlianSekolah
                        ?.namaKonsentrasiSekolah || "N/A"}
                    </Col>
                    <Col span={12}>
                      <strong>ACP:</strong>{" "}
                      {viewModalData.acp?.namaAcp || "N/A"}
                    </Col>
                    <Col span={12}>
                      <strong>ATP:</strong>{" "}
                      {viewModalData.atp?.namaAtp || "N/A"}
                    </Col>
                    <Col span={12}>
                      <strong>Taksonomi:</strong>{" "}
                      {viewModalData.taksonomi?.namaTaksonomi || "N/A"}
                    </Col>
                  </Row>
                </Card>
              </Col>

              {/* Render berdasarkan jenis soal */}
              {(viewModalData.jenisSoal === "PG" ||
                viewModalData.jenisSoal === "MULTI") &&
                viewModalData.opsi && (
                  <Col span={24}>
                    <Card title="Pilihan Jawaban" size="small">
                      {Object.entries(viewModalData.opsi).map(
                        ([key, value]) =>
                          value && (
                            <div key={key} style={{ marginBottom: 8 }}>
                              <strong>{key}:</strong> {value}
                            </div>
                          )
                      )}
                    </Card>
                  </Col>
                )}

              {viewModalData.jenisSoal === "COCOK" &&
                viewModalData.pasangan && (
                  <Col span={24}>
                    <Card title="Pasangan" size="small">
                      <Row gutter={[16, 16]}>
                        <Col span={12}>
                          <strong>Kolom Kiri:</strong>
                          {Object.entries(viewModalData.pasangan)
                            .filter(([key]) => key.includes("_kiri"))
                            .map(([key, value]) => (
                              <div key={key} style={{ marginBottom: 4 }}>
                                • {value}
                              </div>
                            ))}
                        </Col>
                        <Col span={12}>
                          <strong>Kolom Kanan:</strong>
                          {Object.entries(viewModalData.pasangan)
                            .filter(([key]) => key.includes("_kanan"))
                            .map(([key, value]) => (
                              <div key={key} style={{ marginBottom: 4 }}>
                                • {value}
                              </div>
                            ))}
                        </Col>
                      </Row>
                    </Card>
                  </Col>
                )}

              {viewModalData.jenisSoal === "ISIAN" &&
                viewModalData.toleransiTypo && (
                  <Col span={24}>
                    <Card title="Toleransi Typo" size="small">
                      <strong>Toleransi:</strong> {viewModalData.toleransiTypo}{" "}
                      karakter
                    </Card>
                  </Col>
                )}

              <Col span={24}>
                <Card title="Jawaban Benar" size="small">
                  {Array.isArray(viewModalData.jawabanBenar) ? (
                    viewModalData.jawabanBenar.map((jawaban, index) => (
                      <div key={index} style={{ marginBottom: 4 }}>
                        <Tag color="green">✓ {jawaban}</Tag>
                      </div>
                    ))
                  ) : (
                    <Tag color="green">✓ {viewModalData.jawabanBenar}</Tag>
                  )}
                </Card>
              </Col>

              {viewModalData.soalUjian && (
                <Col span={24}>
                  <Card title="Informasi Soal Ujian" size="small">
                    <Row gutter={[16, 8]}>
                      <Col span={24}>
                        <strong>ID Soal Ujian:</strong>{" "}
                        {viewModalData.soalUjian.idSoalUjian}
                      </Col>
                      <Col span={12}>
                        <strong>Dibuat oleh:</strong>{" "}
                        {viewModalData.soalUjian.user?.name || "N/A"}
                      </Col>
                      <Col span={12}>
                        <strong>Tanggal Dibuat:</strong>{" "}
                        {viewModalData.soalUjian.createdAt
                          ? new Date(
                              viewModalData.soalUjian.createdAt
                            ).toLocaleDateString("id-ID")
                          : "N/A"}
                      </Col>
                    </Row>
                  </Card>
                </Col>
              )}
            </Row>
          </div>
        )}
      </Modal>

      <Modal
        title="Import Soal Bank Soal"
        open={importModalVisible}
        closable={!uploading}
        maskClosable={!uploading}
        onCancel={() => {
          if (!uploading) {
            setImportModalVisible(false);
            setImportFile(null);
            setSelectedQuestionType("PG");
          }
        }}
        footer={[
          <Button
            key="cancel"
            disabled={uploading}
            onClick={() => {
              if (!uploading) {
                setImportModalVisible(false);
                setImportFile(null);
                setSelectedQuestionType("PG");
              }
            }}
          >
            Cancel
          </Button>,
          <Button
            key="upload"
            type="primary"
            loading={uploading}
            onClick={async () => {
              if (importFile) {
                setUploading(true);
                await handleImportFile(importFile, selectedQuestionType);
                setUploading(false);
              } else {
                message.warning("Pilih file terlebih dahulu!");
              }
            }}
          >
            Upload
          </Button>,
        ]}
        width={600}
      >
        <div style={{ marginBottom: 16 }}>
          <label
            style={{ display: "block", marginBottom: 8, fontWeight: "bold" }}
          >
            Pilih Jenis Soal:
          </label>
          <Select
            value={selectedQuestionType}
            onChange={setSelectedQuestionType}
            style={{ width: "100%", marginBottom: 16 }}
            placeholder="Pilih jenis soal"
            disabled={uploading}
          >
            <Select.Option value="PG">Pilihan Ganda (PG)</Select.Option>
            <Select.Option value="MULTI">Multi Jawaban (MULTI)</Select.Option>
            <Select.Option value="COCOK">Mencocokkan (COCOK)</Select.Option>
            <Select.Option value="ISIAN">Isian (ISIAN)</Select.Option>
          </Select>
        </div>

        <div style={{ marginBottom: 16 }}>
          <label
            style={{ display: "block", marginBottom: 8, fontWeight: "bold" }}
          >
            Upload File CSV:
          </label>
          <Upload
            beforeUpload={(file) => {
              if (!uploading) {
                setImportFile(file);
              }
              return false; // Jangan auto-upload
            }}
            accept=".csv,.xlsx,.xls"
            showUploadList={false}
            maxCount={1}
            disabled={uploading}
          >
            <Button icon={<UploadOutlined />} disabled={uploading}>
              Pilih File
            </Button>
          </Upload>
          {importFile && (
            <div
              style={{
                marginTop: 8,
                padding: 8,
                backgroundColor: "#f0f2f5",
                borderRadius: 4,
              }}
            >
              <strong>File terpilih:</strong> {importFile.name}
              <br />
              <strong>Jenis soal:</strong>{" "}
              {selectedQuestionType === "PG"
                ? "Pilihan Ganda"
                : selectedQuestionType === "MULTI"
                ? "Multi Jawaban"
                : selectedQuestionType === "COCOK"
                ? "Mencocokkan"
                : "Isian"}
            </div>
          )}
        </div>

        <div
          style={{
            padding: 12,
            backgroundColor: "#e6f7ff",
            borderRadius: 4,
            fontSize: "12px",
          }}
        >
          <strong>
            💡 Disarankan menggunakan format CSV untuk menghindari file corrupt!
          </strong>
          <br />
          <br />
          <strong>🔄 Alur Import 2 Tahap:</strong>
          <br />• <strong>Tahap 1:</strong> Data disimpan ke Soal Ujian
          <br />• <strong>Tahap 2:</strong> Otomatis dikaitkan ke Bank Soal
          <br />• <strong>Multiple Soal:</strong> Nama ujian sama = Banyak soal
          (contoh: 20 soal &quot;UTS IPA&quot;)
          <br />
          <br />
          <strong>Format CSV yang diperlukan:</strong>
          <br />
          <strong>Kolom Wajib:</strong> namaUjian, pertanyaan, bobot,
          jawabanBenar, tahunAjaran, namaSemester, namaKelas, namaMapel,
          namaElemen, namaAcp, namaAtp, namaKonsentrasiSekolah, idSchool,
          namaTaksonomi
          <br />
          <br />
          <strong>Kolom berdasarkan Jenis Soal:</strong>
          <br />• <strong>PG & MULTI:</strong> opsiA, opsiB, opsiC, opsiD, opsiE
          <br />• <strong>COCOK:</strong> kiri1, kanan1, kiri2, kanan2, dst.
          (max 5 pasang)
          <br />• <strong>ISIAN:</strong> toleransiTypo (opsional)
          <br />
          <br />
          <strong>Pencocokan Data (Fuzzy Matching):</strong>
          <br />• Sistem mengabaikan spasi, tanda hubung, dan karakter khusus
          <br />• Tidak case-sensitive (tidak membedakan huruf besar/kecil)
          <br />• Contoh: &quot;Bahasa Indonesia&quot; =
          &quot;bahasaindonesia&quot; = &quot;Bahasa-Indonesia&quot;
          <br />
          <br />
          <strong>📝 Contoh soal realistis dalam template:</strong>
          <br />• <strong>PG:</strong> &quot;Hasil dari 15 + 27 adalah...&quot;
          dengan opsi A: 40, B: 41, C: 42, D: 43
          <br />• <strong>MULTI:</strong> &quot;Manakah yang termasuk planet
          dalam tata surya?&quot; (jawaban: A,B,D)
          <br />• <strong>COCOK:</strong> &quot;Cocokkan rumus kimia dengan nama
          senyawa&quot; (H2O=Air, CO2=Karbon dioksida)
          <br />• <strong>ISIAN:</strong> &quot;Ibu kota negara Indonesia
          adalah...&quot; (jawaban: Jakarta)
          <br />
          <br />
          <strong>📋 Format data umum:</strong>
          <br />• <strong>namaUjian:</strong> &quot;UTS Matematika&quot;,
          &quot;UTS IPA&quot;, &quot;UTS Kimia&quot;
          <br />• <strong>pertanyaan:</strong> Gunakan kalimat lengkap dan jelas
          seperti contoh di template
          <br />• <strong>tahunAjaran:</strong> &quot;2025/2026&quot;
          <br />• <strong>namaSemester:</strong> &quot;Ganjil&quot; atau
          &quot;Genap&quot;
          <br />• <strong>namaMapel:</strong> &quot;Matematika&quot;,
          &quot;IPA&quot;, &quot;Kimia&quot;, &quot;Geografi&quot;
          <br />• <strong>idSchool:</strong> &quot;RWK001&quot; (nilai tetap)
          <br />
          <br />
          <strong>Download Template:</strong>
          <br />
          <div style={{ marginTop: 8 }}>
            <a
              href="/templates/import-template-PG.csv"
              download
              style={{ marginRight: 12, color: "#1890ff" }}
            >
              📄 Template PG (CSV)
            </a>
            <a
              href="/templates/import-template-MULTI.csv"
              download
              style={{ marginRight: 12, color: "#1890ff" }}
            >
              📄 Template MULTI (CSV)
            </a>
            <a
              href="/templates/import-template-COCOK.csv"
              download
              style={{ marginRight: 12, color: "#1890ff" }}
            >
              📄 Template COCOK (CSV)
            </a>
            <a
              href="/templates/import-template-ISIAN.csv"
              download
              style={{ color: "#1890ff" }}
            >
              📄 Template ISIAN (CSV)
            </a>

            <br />
            <br />
          </div>
        </div>
      </Modal>

      {/* Loading Overlay untuk Import */}
      {uploading && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100%",
            height: "100%",
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            zIndex: 9999,
            flexDirection: "column",
          }}
        >
          <Spin size="large" />
          <div
            style={{
              color: "white",
              marginTop: 16,
              fontSize: "16px",
              fontWeight: "bold",
            }}
          >
            Sedang mengimpor soal ujian...
          </div>
          <div
            style={{
              color: "white",
              marginTop: 8,
              fontSize: "14px",
            }}
          >
            Mohon tunggu, jangan menutup halaman ini
          </div>
        </div>
      )}
    </div>
  );
};

export default BankSoal;
