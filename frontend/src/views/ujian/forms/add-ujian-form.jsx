/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import {
  Form,
  Input,
  Modal,
  Select,
  Table,
  Tabs,
  Row,
  Col,
  message,
  DatePicker,
  TimePicker,
  InputNumber,
  Switch,
  Card,
  Button,
  Divider,
  Radio,
} from "antd";
import {
  ClockCircleOutlined,
  SettingOutlined,
  BookOutlined,
  PlayCircleOutlined,
  PlusOutlined,
  DeleteOutlined,
  SafetyOutlined,
  ToolOutlined,
} from "@ant-design/icons";
import { getUjian } from "@/api/ujian";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";
import { getBankSoal } from "@/api/bankSoal";
import moment from "moment";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;
const { RangePicker } = DatePicker;

const AddUjianForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();
  const [ujian, setUjian] = useState([]);

  const [tableLoading, setTableLoading] = useState(false);
  const [userInfo, setUserInfo] = useState("");
  const [userSchoolId, setUserSchoolId] = useState("");
  const [schoolList, setSchoolList] = useState([]);
  const [bankSoalList, setBankSoalList] = useState([]);
  const [selectedBankSoal, setSelectedBankSoal] = useState([]);
  const [activeTab, setActiveTab] = useState("1");
  const [selectedBankSoalId, setSelectedBankSoalId] = useState(null);

  // State untuk pengaturan waktu
  const [isFlexibleTiming, setIsFlexibleTiming] = useState(false);
  const [allowLateStart, setAllowLateStart] = useState(false);
  const [isAutoStart, setIsAutoStart] = useState(false);
  const [isAutoEnd, setIsAutoEnd] = useState(true);
  const [createAndActivate, setCreateAndActivate] = useState(false);
  const [showTimerToParticipants, setShowTimerToParticipants] = useState(true);
  const [preventCheating, setPreventCheating] = useState(true);
  const [allowReview, setAllowReview] = useState(true);
  const [allowBacktrack, setAllowBacktrack] = useState(false);
  const [autoEndAfterDuration, setAutoEndAfterDuration] = useState(true);
  const [isCatEnabled, setIsCatEnabled] = useState(true);

  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo();
      setUserSchoolId(response.data.school_id);
      setUserInfo(response.data.id);
      console.log("User School ID: ", response.data.school_id);
    } catch (error) {
      message.error("Gagal mengambil informasi pengguna");
    }
  };

  const fetchSchoolList = async () => {
    try {
      const result = await getSchool();
      if (result.data.statusCode === 200) {
        setSchoolList(result.data.content);
      } else {
        message.error("Gagal mengambil data sekolah");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const fetchBankSoal = async () => {
    try {
      const result = await getBankSoal();
      if (result.data.statusCode === 200) {
        // Group by namaUjian, kumpulkan semua idBankSoal
        const grouped = result.data.content.reduce((acc, item) => {
          const key = item.namaUjian?.trim() || "Tanpa Nama";
          if (!acc[key]) {
            acc[key] = {
              namaBankSoal: key,
              idBankSoalList: [item.idBankSoal],
              jumlahSoal: 1,
              totalBobot: parseFloat(item.bobot) || 10,
              school: item.school,
              mapel: item.mapel,
              kelas: item.kelas,
              semester: item.semester,
              tahunAjaran: item.tahunAjaran,
              konsentrasiKeahlianSekolah: item.konsentrasiKeahlianSekolah,
            };
          } else {
            acc[key].idBankSoalList.push(item.idBankSoal);
            acc[key].jumlahSoal += 1;
            acc[key].totalBobot += parseFloat(item.bobot);
          }
          return acc;
        }, {});
        // Simpan ke state
        setBankSoalList(Object.values(grouped));
      } else {
        message.error("Gagal mengambil data bank soal");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const fetchUjian = async () => {
    setTableLoading(true);
    try {
      const result = await getUjian();
      if (result.data.statusCode === 200) {
        setUjian(result.data.content);
      } else {
        message.error("Gagal mengambil data ujian");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
    }
  };

  useEffect(() => {
    fetchUserInfo();
    fetchSchoolList();
    fetchBankSoal();
    fetchUjian();
  }, []);

  useEffect(() => {
    if (userSchoolId) {
      form.setFieldsValue({ idSchool: userSchoolId });
    }
  }, [userSchoolId, form]);

  // Handle tambah bank soal
  const handleAddBankSoal = () => {
    if (!selectedBankSoalId) {
      message.warning("Pilih bank soal terlebih dahulu");
      return;
    }

    // Cari berdasarkan namaBankSoal
    const bankSoal = bankSoalList.find(
      (item) => item.namaBankSoal === selectedBankSoalId
    );

    if (!bankSoal) {
      message.error("Bank soal tidak ditemukan");
      return;
    }

    // Cek duplikat berdasarkan namaBankSoal
    if (
      selectedBankSoal.find((item) => item.namaBankSoal === selectedBankSoalId)
    ) {
      message.warning("Bank soal sudah dipilih");
      return;
    }

    // Cek kompatibilitas relasi (opsional, bisa pakai kode kamu sebelumnya)
    if (selectedBankSoal.length > 0) {
      const first = selectedBankSoal[0];
      const isCompatible =
        bankSoal.tahunAjaran?.idTahun === first.tahunAjaran?.idTahun &&
        bankSoal.mapel?.idMapel === first.mapel?.idMapel &&
        bankSoal.kelas?.idKelas === first.kelas?.idKelas &&
        bankSoal.konsentrasiKeahlianSekolah?.idKonsentrasiSekolah ===
          first.konsentrasiKeahlianSekolah?.idKonsentrasiSekolah;
      if (!isCompatible) {
        message.error("Bank soal tidak kompatibel dengan yang sudah dipilih");
        return;
      }
    }

    setSelectedBankSoal([...selectedBankSoal, bankSoal]);
    setSelectedBankSoalId(null);
    message.success("Bank soal berhasil ditambahkan");
  };

  const getAvailableBankSoal = () => {
    if (selectedBankSoal.length === 0) {
      return bankSoalList;
    }

    const firstBankSoal = selectedBankSoal[0];

    return bankSoalList.filter((item) => {
      // Don't show already selected bank soal
      if (
        selectedBankSoal.find(
          (selected) => selected.idBankSoal === item.idBankSoal
        )
      ) {
        return false;
      }

      // Only show compatible bank soal
      return (
        item.tahunAjaran?.idTahun === firstBankSoal.tahunAjaran?.idTahun &&
        item.mapel?.idMapel === firstBankSoal.mapel?.idMapel &&
        item.kelas?.idKelas === firstBankSoal.kelas?.idKelas &&
        item.konsentrasiKeahlianSekolah?.idKonsentrasiSekolah ===
          firstBankSoal.konsentrasiKeahlianSekolah?.idKonsentrasiSekolah
      );
    });
  };

  // Handle hapus bank soal
  const handleRemoveBankSoal = (bankSoalId) => {
    setSelectedBankSoal(
      selectedBankSoal.filter((item) => item.idBankSoal !== bankSoalId)
    );
    message.success("Bank soal berhasil dihapus");
  };

  // Function untuk mengambil data relasi dari bank soal yang dipilih
  const getRelationDataFromBankSoal = () => {
    if (selectedBankSoal.length === 0) return {};

    const firstBankSoal = selectedBankSoal[0];

    return {
      idTahun: firstBankSoal.tahunAjaran?.idTahun || null,
      idKelas: firstBankSoal.kelas?.idKelas || null,
      idSemester: firstBankSoal.semester?.idSemester || null,
      idMapel: firstBankSoal.mapel?.idMapel || null,
      idKonsentrasiKeahlianSekolah:
        firstBankSoal.konsentrasiKeahlianSekolah?.idKonsentrasiSekolah || null,
    };
  };

  // Kolom tabel bank soal terpilih
  const bankSoalColumns = [
    {
      title: "No",
      dataIndex: "index",
      key: "index",
      width: 50,
      render: (_, __, index) => index + 1,
    },
    {
      title: "Nama Bank Soal",
      dataIndex: "namaBankSoal",
      key: "namaBankSoal",
      render: (text, record) => (
        <div>
          <div style={{ fontWeight: "bold" }}>{text}</div>
          <div style={{ fontSize: "12px", color: "#666" }}>
            {record.mapel?.name} - {record.kelas?.namaKelas} -{" "}
            {record.semester?.namaSemester}
          </div>
          <div style={{ fontSize: "11px", color: "#999" }}>
            {record.tahunAjaran?.tahunAjaran}
          </div>
        </div>
      ),
    },
    {
      title: "Jumlah Soal",
      dataIndex: "jumlahSoal",
      key: "jumlahSoal",
      width: 100,
      render: (count) => (
        <span
          style={{
            background: "#f0f0f0",
            padding: "2px 8px",
            borderRadius: "4px",
            fontSize: "12px",
          }}
        >
          {count || 0} soal
        </span>
      ),
    },
    {
      title: "Total Bobot",
      dataIndex: "totalBobot",
      key: "totalBobot",
      width: 100,
      render: (bobot, record) => (
        <span
          style={{
            background: "#e6f7ff",
            padding: "2px 8px",
            borderRadius: "4px",
            fontSize: "12px",
            color: "#1890ff",
          }}
        >
          {(bobot || record.jumlahSoal * 10).toFixed(1)}
        </span>
      ),
    },
    {
      title: "Aksi",
      key: "action",
      width: 80,
      render: (_, record) => (
        <Button
          type="primary"
          danger
          size="small"
          icon={<DeleteOutlined />}
          onClick={() => handleRemoveBankSoal(record.idBankSoal)}
          title="Hapus bank soal"
        />
      ),
    },
  ];

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      // Validasi bank soal
      if (selectedBankSoal.length === 0) {
        message.error("Pilih minimal satu bank soal");
        setActiveTab("3");
        return;
      }

      const idBankSoalList = selectedBankSoal.flatMap(
        (item) => item.idBankSoalList
      );

      // Helper function untuk convert undefined ke null dengan default values
      const convertUndefinedToNull = (value, defaultValue = null) => {
        if (value === undefined || value === null || value === "") {
          return defaultValue;
        }
        return value;
      };

      // Ambil data relasi dari bank soal
      const relationData = getRelationDataFromBankSoal();

      // Calculate total jumlah soal dari semua bank soal yang dipilih
      const totalSoalAvailable = selectedBankSoal.reduce(
        (total, item) => total + (item.jumlahSoal || 0),
        0
      );

      // Calculate total bobot
      const totalBobot = selectedBankSoal.reduce(
        (total, item) => total + (item.totalBobot || item.jumlahSoal * 10),
        0
      );

      // Format data sesuai dengan UjianRequest backend
      const ujianData = {
        // Basic info
        namaUjian: values.namaUjian,
        deskripsi: convertUndefinedToNull(values.deskripsiUjian, ""),
        durasiMenit: values.durasiMenit,
        statusUjian: createAndActivate ? "AKTIF" : "DRAFT",

        // Timing settings
        waktuMulaiDijadwalkan: values.waktuMulaiDijadwalkan
          ? values.waktuMulaiDijadwalkan.toISOString()
          : null,
        waktuSelesaiOtomatis: values.waktuSelesaiOtomatis
          ? values.waktuSelesaiOtomatis.toISOString()
          : null,

        // Boolean settings - sesuai dengan UjianRequest
        isLive: createAndActivate,
        isAutoStart: isAutoStart,
        isAutoEnd: isAutoEnd,
        tampilkanNilai: convertUndefinedToNull(values.tampilkanNilai, true),

        // Question settings
        jumlahSoal: convertUndefinedToNull(
          values.jumlahSoalDitampilkan,
          totalSoalAvailable
        ),
        totalBobot: parseFloat(totalBobot.toFixed(1)),
        tipeSoal: values.tipeSoal || "ACAK",

        // Bank soal - hanya ID list
        idBankSoalList,

        // Pengaturan object
        pengaturan: {
          timezone: values.timezone || "Asia/Jakarta",
          createAndActivate: createAndActivate,
          kodeUjian: values.kodeUjian,
        },

        // Timestamp
        createdAt: new Date().toISOString(),

        // CAT Settings
        isCatEnabled: isCatEnabled,
        catSettings: {
          timezone: values.timezone || "Asia/Jakarta",
          showTimer: showTimerToParticipants,
          preventCheating: preventCheating,
        },

        // Late start settings
        allowLateStart: allowLateStart,
        maxLateStartMinutes: convertUndefinedToNull(
          values.maxLateStartMinutes,
          allowLateStart ? 15 : 0
        ),

        // Timer and cheating prevention
        showTimerToParticipants: showTimerToParticipants,
        preventCheating: preventCheating,

        // Flexible timing
        isFlexibleTiming: isFlexibleTiming,
        batasAkhirMulai: values.batasAkhirMulai
          ? values.batasAkhirMulai.toISOString()
          : null,
        autoEndAfterDuration: autoEndAfterDuration,
        toleransiKeterlambatanMenit: convertUndefinedToNull(
          values.toleransiKeterlambatanMenit,
          5
        ),

        // Question strategy and scoring
        strategiPemilihanSoal: values.strategiPemilihanSoal || "RANDOM",
        minPassingScore: convertUndefinedToNull(values.nilaiMinimumLulus, 60.0),
        allowReview: allowReview,
        allowBacktrack: allowBacktrack,
        maxAttempts: convertUndefinedToNull(values.maxAttempts, 1),

        // Relations - dari bank soal yang dipilih
        idTahun: relationData.idTahun,
        idKelas: relationData.idKelas,
        idSemester: relationData.idSemester,
        idMapel: relationData.idMapel,
        idKonsentrasiKeahlianSekolah: relationData.idKonsentrasiKeahlianSekolah,

        // User and school relations
        idCreatedBy: userInfo,
        idSchool: values.idSchool,
      };

      console.log("Formatted ujian data for backend:", ujianData);
      console.log("Relation data from bank soal:", relationData);
      console.log("Total soal available:", totalSoalAvailable);
      console.log("Total bobot calculated:", totalBobot);

      onOk(ujianData);
    } catch (error) {
      console.error("Validation failed:", error);
      message.error("Validasi form gagal");
    }
  };

  const handleCancel = () => {
    // Reset form fields
    form.resetFields();

    // Reset state arrays
    setSelectedBankSoal([]);
    setSelectedBankSoalId(null);

    // Reset tabs
    setActiveTab("1");

    // Reset boolean states
    setIsFlexibleTiming(false);
    setAllowLateStart(false);
    setIsAutoStart(false);
    setIsAutoEnd(true);
    setCreateAndActivate(false);
    setShowTimerToParticipants(true);
    setPreventCheating(true);
    setAllowReview(true);
    setAllowBacktrack(false);
    setAutoEndAfterDuration(true);
    setIsCatEnabled(true);

    // Reset school ID to user's school
    if (userSchoolId) {
      setTimeout(() => {
        form.setFieldsValue({ idSchool: userSchoolId });
      }, 0);
    }

    // Call parent cancel handler
    onCancel();
  };

  // Hitung total soal
  const totalSoal = selectedBankSoal.reduce(
    (total, item) => total + (item.jumlahSoal || 0),
    0
  );

  // Calculate total bobot
  const totalBobot = selectedBankSoal.reduce(
    (total, item) => total + (item.totalBobot || item.jumlahSoal * 10),
    0
  );

  // Tampilkan informasi relasi dari bank soal yang dipilih
  const renderRelationInfo = () => {
    if (selectedBankSoal.length === 0) return null;

    const relationData = getRelationDataFromBankSoal();
    const firstBankSoal = selectedBankSoal[0];

    return (
      <Card
        title="Data Relasi (Diambil dari Bank Soal)"
        size="small"
        style={{ marginTop: 16 }}
      >
        <Row gutter={16}>
          <Col xs={24} sm={12} md={8}>
            <div style={{ marginBottom: 8 }}>
              <strong>Tahun Ajaran:</strong>
              <div style={{ color: "#666" }}>
                {firstBankSoal.tahunAjaran?.tahunAjaran || "-"}
              </div>
            </div>
          </Col>
          <Col xs={24} sm={12} md={8}>
            <div style={{ marginBottom: 8 }}>
              <strong>Kelas:</strong>
              <div style={{ color: "#666" }}>
                {firstBankSoal.kelas?.namaKelas || "-"}
              </div>
            </div>
          </Col>
          <Col xs={24} sm={12} md={8}>
            <div style={{ marginBottom: 8 }}>
              <strong>Semester:</strong>
              <div style={{ color: "#666" }}>
                {firstBankSoal.semester?.namaSemester || "-"}
              </div>
            </div>
          </Col>
          <Col xs={24} sm={12} md={8}>
            <div style={{ marginBottom: 8 }}>
              <strong>Mata Pelajaran:</strong>
              <div style={{ color: "#666" }}>
                {firstBankSoal.mapel?.name || "-"}
              </div>
            </div>
          </Col>
          <Col xs={24} sm={12} md={8}>
            <div style={{ marginBottom: 8 }}>
              <strong>Konsentrasi Keahlian:</strong>
              <div style={{ color: "#666" }}>
                {firstBankSoal.konsentrasiKeahlianSekolah
                  ?.namaKonsentrasiSekolah || "-"}
              </div>
            </div>
          </Col>
          <Col xs={24} sm={12} md={8}>
            <div style={{ marginBottom: 8 }}>
              <strong>Total Soal:</strong>
              <div style={{ color: "#1890ff", fontWeight: "bold" }}>
                {totalSoal} soal
              </div>
            </div>
          </Col>
        </Row>
        {selectedBankSoal.length > 1 && (
          <div
            style={{
              marginTop: 8,
              padding: "8px 12px",
              background: "#fff7e6",
              border: "1px solid #ffd591",
              borderRadius: "4px",
              fontSize: "12px",
              color: "#d46b08",
            }}
          >
            <strong>Catatan:</strong> Data relasi diambil dari bank soal pertama
            yang dipilih. Pastikan semua bank soal memiliki relasi yang sama
            untuk konsistensi.
          </div>
        )}
      </Card>
    );
  };

  return (
    <Modal
      title="Tambah Ujian CAT"
      open={visible}
      onCancel={handleCancel}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
      okText="Simpan"
      width={1000}
      style={{ top: 20 }}
      styles={{
        body: { maxHeight: "70vh", overflowY: "auto" },
      }}
    >
      <Tabs activeKey={activeTab} onChange={setActiveTab}>
        {/* Tab 1: Informasi Dasar */}
        <TabPane
          tab={
            <span>
              <SettingOutlined />
              Informasi Dasar
            </span>
          }
          key="1"
        >
          <Form form={form} layout="vertical">
            <Row gutter={16}>
              <Col xs={24} sm={24} md={24}>
                <Form.Item
                  label="Sekolah:"
                  name="idSchool"
                  style={{ display: "none" }}
                  rules={[{ required: true, message: "Sekolah diperlukan" }]}
                >
                  <Select defaultValue={userSchoolId} disabled>
                    {schoolList
                      .filter(({ idSchool }) => idSchool === userSchoolId)
                      .map(({ idSchool, nameSchool }) => (
                        <Option key={idSchool} value={idSchool}>
                          {nameSchool}
                        </Option>
                      ))}
                  </Select>
                </Form.Item>
              </Col>

              <Col xs={24} sm={12} md={12}>
                <Form.Item
                  label="Nama Ujian:"
                  name="namaUjian"
                  rules={[{ required: true, message: "Nama ujian diperlukan" }]}
                >
                  <Input placeholder="Masukkan nama ujian" />
                </Form.Item>
              </Col>

              <Col xs={24} sm={12} md={12}>
                <Form.Item
                  label="Kode Ujian:"
                  name="kodeUjian"
                  rules={[{ required: true, message: "Kode ujian diperlukan" }]}
                  help="Kode ujian untuk peserta"
                >
                  <Input placeholder="Masukkan kode ujian" />
                </Form.Item>
              </Col>

              <Col xs={24} sm={24} md={24}>
                <Form.Item label="Deskripsi Ujian:" name="deskripsiUjian">
                  <TextArea
                    rows={3}
                    placeholder="Masukkan deskripsi ujian (opsional)"
                  />
                </Form.Item>
              </Col>

              <Col xs={24} sm={8} md={8}>
                <Form.Item
                  label="Durasi (Menit):"
                  name="durasiMenit"
                  rules={[{ required: true, message: "Durasi diperlukan" }]}
                >
                  <InputNumber
                    min={1}
                    max={600}
                    placeholder="120"
                    style={{ width: "100%" }}
                  />
                </Form.Item>
              </Col>

              <Col xs={24} sm={8} md={8}>
                <Form.Item
                  label="Jumlah Soal Ditampilkan:"
                  name="jumlahSoalDitampilkan"
                  help="Kosongkan untuk menampilkan semua soal"
                >
                  <InputNumber
                    min={1}
                    max={200}
                    placeholder={`Total tersedia: ${totalSoal}`}
                    style={{ width: "100%" }}
                  />
                </Form.Item>
              </Col>

              <Col xs={24} sm={8} md={8}>
                <Form.Item
                  label="Nilai Minimum Lulus:"
                  name="nilaiMinimumLulus"
                  help="Nilai minimum untuk lulus (0-100)"
                >
                  <InputNumber
                    min={0}
                    max={100}
                    placeholder="60"
                    style={{ width: "100%" }}
                    formatter={(value) => `${value}%`}
                    parser={(value) => value.replace("%", "")}
                  />
                </Form.Item>
              </Col>

              <Col xs={24} sm={12} md={8}>
                <Form.Item
                  label="Tipe Soal:"
                  name="tipeSoal"
                  initialValue="ACAK"
                >
                  <Radio.Group>
                    <Radio value="ACAK">Acak</Radio>
                    <Radio value="BERURUTAN">Berurutan</Radio>
                  </Radio.Group>
                </Form.Item>
              </Col>

              <Col xs={24} sm={12} md={8}>
                <Form.Item
                  label="Strategi Pemilihan Soal:"
                  name="strategiPemilihanSoal"
                  initialValue="RANDOM"
                >
                  <Radio.Group>
                    <Radio value="RANDOM">Random</Radio>
                    <Radio value="SEQUENTIAL">Sequential</Radio>
                  </Radio.Group>
                </Form.Item>
              </Col>

              <Col xs={24} sm={12} md={8}>
                <Form.Item
                  label="Maksimal Percobaan:"
                  name="maxAttempts"
                  initialValue={1}
                >
                  <InputNumber min={1} max={5} style={{ width: "100%" }} />
                </Form.Item>
              </Col>
            </Row>

            <Divider />

            <Row gutter={16}>
              <Col xs={24} sm={12} md={12}>
                <Form.Item label="Pengaturan Ujian:">
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      gap: "8px",
                    }}
                  >
                    <div>
                      <Switch
                        checked={createAndActivate}
                        onChange={setCreateAndActivate}
                      />
                      <span style={{ marginLeft: 8 }}>
                        Buat dan Aktifkan Langsung
                      </span>
                    </div>
                    <div>
                      <Switch checked={isAutoStart} onChange={setIsAutoStart} />
                      <span style={{ marginLeft: 8 }}>Mulai Otomatis</span>
                    </div>
                    <div>
                      <Switch checked={isAutoEnd} onChange={setIsAutoEnd} />
                      <span style={{ marginLeft: 8 }}>Selesai Otomatis</span>
                    </div>
                    <div>
                      <Switch
                        checked={autoEndAfterDuration}
                        onChange={setAutoEndAfterDuration}
                      />
                      <span style={{ marginLeft: 8 }}>
                        Selesai Otomatis Setelah Durasi
                      </span>
                    </div>
                  </div>
                </Form.Item>
              </Col>

              <Col xs={24} sm={12} md={12}>
                <Form.Item label="Pengaturan Waktu:">
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      gap: "8px",
                    }}
                  >
                    <div>
                      <Switch
                        checked={isFlexibleTiming}
                        onChange={setIsFlexibleTiming}
                      />
                      <span style={{ marginLeft: 8 }}>Waktu Fleksibel</span>
                    </div>
                    <div>
                      <Switch
                        checked={allowLateStart}
                        onChange={setAllowLateStart}
                      />
                      <span style={{ marginLeft: 8 }}>
                        Izinkan Mulai Terlambat
                      </span>
                    </div>
                    <div>
                      <Form.Item
                        name="tampilkanNilai"
                        valuePropName="checked"
                        style={{ margin: 0 }}
                        initialValue={true}
                      >
                        <Switch />
                      </Form.Item>
                      <span style={{ marginLeft: 8 }}>Tampilkan Nilai</span>
                    </div>
                  </div>
                </Form.Item>
              </Col>
            </Row>

            {/* Tampilkan informasi relasi jika ada bank soal yang dipilih */}
            {renderRelationInfo()}
          </Form>
        </TabPane>

        {/* Tab 2: Pengaturan Waktu */}
        <TabPane
          tab={
            <span>
              <ClockCircleOutlined />
              Pengaturan Waktu
            </span>
          }
          key="2"
        >
          <Form form={form} layout="vertical">
            <Card title="Jadwal Ujian" size="small">
              <Row gutter={16}>
                <Col xs={24} sm={12} md={12}>
                  <Form.Item
                    label="Waktu Mulai Dijadwalkan:"
                    name="waktuMulaiDijadwalkan"
                    rules={[
                      { required: true, message: "Waktu mulai diperlukan" },
                    ]}
                  >
                    <DatePicker
                      showTime
                      format="YYYY-MM-DD HH:mm:ss"
                      placeholder="Pilih waktu mulai"
                      style={{ width: "100%" }}
                    />
                  </Form.Item>
                </Col>

                <Col xs={24} sm={12} md={12}>
                  <Form.Item
                    label="Waktu Selesai Otomatis:"
                    name="waktuSelesaiOtomatis"
                    rules={[
                      {
                        required: isAutoEnd,
                        message: "Waktu selesai diperlukan jika auto end aktif",
                      },
                    ]}
                  >
                    <DatePicker
                      showTime
                      format="YYYY-MM-DD HH:mm:ss"
                      placeholder="Pilih waktu selesai"
                      style={{ width: "100%" }}
                      disabled={!isAutoEnd}
                    />
                  </Form.Item>
                </Col>

                {isFlexibleTiming && (
                  <Col xs={24} sm={12} md={12}>
                    <Form.Item
                      label="Batas Akhir Mulai:"
                      name="batasAkhirMulai"
                      help="Batas waktu terakhir peserta dapat memulai ujian"
                    >
                      <DatePicker
                        showTime
                        format="YYYY-MM-DD HH:mm:ss"
                        placeholder="Pilih batas akhir mulai"
                        style={{ width: "100%" }}
                      />
                    </Form.Item>
                  </Col>
                )}

                <Col xs={24} sm={12} md={12}>
                  <Form.Item
                    label="Toleransi Keterlambatan (Menit):"
                    name="toleransiKeterlambatanMenit"
                    help="Toleransi keterlambatan dalam menit"
                    initialValue={5}
                  >
                    <InputNumber
                      min={0}
                      max={60}
                      placeholder="5"
                      style={{ width: "100%" }}
                    />
                  </Form.Item>
                </Col>

                {allowLateStart && (
                  <Col xs={24} sm={12} md={12}>
                    <Form.Item
                      label="Maksimal Keterlambatan Mulai (Menit):"
                      name="maxLateStartMinutes"
                      help="Maksimal menit keterlambatan yang diizinkan"
                      initialValue={15}
                    >
                      <InputNumber
                        min={0}
                        max={120}
                        placeholder="15"
                        style={{ width: "100%" }}
                      />
                    </Form.Item>
                  </Col>
                )}
              </Row>
            </Card>

            <br />

            <Card title="Pengaturan Zona Waktu" size="small">
              <Row gutter={16}>
                <Col xs={24} sm={12} md={12}>
                  <Form.Item
                    label="Zona Waktu:"
                    name="timezone"
                    initialValue="Asia/Jakarta"
                  >
                    <Select placeholder="Pilih zona waktu">
                      <Option value="Asia/Jakarta">WIB (Asia/Jakarta)</Option>
                      <Option value="Asia/Makassar">
                        WITA (Asia/Makassar)
                      </Option>
                      <Option value="Asia/Jayapura">WIT (Asia/Jayapura)</Option>
                    </Select>
                  </Form.Item>
                </Col>
              </Row>
            </Card>
          </Form>
        </TabPane>

        {/* Tab 3: Bank Soal */}
        <TabPane
          tab={
            <span>
              <BookOutlined />
              Bank Soal ({selectedBankSoal.length})
            </span>
          }
          key="3"
        >
          <Card title="Pilih Bank Soal" size="small">
            <Row gutter={16}>
              <Col xs={24} sm={18} md={20}>
                <Select
                  placeholder="Pilih bank soal untuk ditambahkan"
                  style={{ width: "100%" }}
                  showSearch
                  value={selectedBankSoalId}
                  onChange={setSelectedBankSoalId}
                  filterOption={(input, option) =>
                    option.children
                      .toLowerCase()
                      .indexOf(input.toLowerCase()) >= 0
                  }
                  optionFilterProp="children"
                >
                  {getAvailableBankSoal().map((item) => (
                    <Option key={item.namaBankSoal} value={item.namaBankSoal}>
                      <div>
                        <div style={{ fontWeight: "bold" }}>
                          {item.namaBankSoal}
                        </div>
                        <div style={{ fontSize: "12px", color: "#666" }}>
                          {item.mapel?.name} - {item.kelas?.namaKelas} -{" "}
                          {item.semester?.namaSemester} ({item.jumlahSoal} soal,
                          Bobot:{" "}
                          {(item.totalBobot || item.jumlahSoal * 10).toFixed(1)}
                          )
                        </div>
                        <div style={{ fontSize: "11px", color: "#999" }}>
                          {item.tahunAjaran?.tahunAjaran} -{" "}
                          {item.school?.nameSchool}
                        </div>
                      </div>
                    </Option>
                  ))}
                </Select>
              </Col>

              <Col xs={24} sm={6} md={4}>
                <Button
                  type="primary"
                  icon={<PlusOutlined />}
                  onClick={handleAddBankSoal}
                  disabled={!selectedBankSoalId}
                  style={{ width: "100%" }}
                >
                  Tambah
                </Button>
              </Col>
            </Row>

            {selectedBankSoal.length > 0 && (
              <div style={{ marginTop: 16 }}>
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    marginBottom: 8,
                  }}
                >
                  <span style={{ fontWeight: "bold" }}>
                    Bank Soal Terpilih ({selectedBankSoal.length})
                  </span>
                  <div style={{ display: "flex", gap: "8px" }}>
                    <span
                      style={{
                        background: "#e6f7ff",
                        padding: "4px 8px",
                        borderRadius: "4px",
                        fontSize: "12px",
                        color: "#1890ff",
                      }}
                    >
                      Total: {totalSoal} soal
                    </span>
                    <span
                      style={{
                        background: "#f6ffed",
                        padding: "4px 8px",
                        borderRadius: "4px",
                        fontSize: "12px",
                        color: "#52c41a",
                      }}
                    >
                      Bobot: {totalBobot.toFixed(1)}
                    </span>
                  </div>
                </div>

                <Table
                  columns={bankSoalColumns}
                  dataSource={selectedBankSoal}
                  rowKey="idBankSoal"
                  pagination={false}
                  size="small"
                  scroll={{ x: 600 }}
                />
              </div>
            )}
          </Card>

          {selectedBankSoal.length === 0 && (
            <Card
              style={{
                marginTop: 16,
                textAlign: "center",
                border: "2px dashed #d9d9d9",
              }}
            >
              <div style={{ padding: "20px 0", color: "#999" }}>
                <BookOutlined style={{ fontSize: "48px", marginBottom: 16 }} />
                <div style={{ fontSize: "16px", marginBottom: 8 }}>
                  Belum ada bank soal yang dipilih
                </div>
                <div style={{ fontSize: "12px" }}>
                  Pilih minimal satu bank soal untuk membuat ujian
                </div>
              </div>
            </Card>
          )}
        </TabPane>

        {/* Tab 4: Pengaturan CAT & Keamanan */}
        <TabPane
          tab={
            <span>
              <SafetyOutlined />
              CAT & Keamanan
            </span>
          }
          key="4"
        >
          <Form form={form} layout="vertical">
            <Card title="Pengaturan CAT (Computer Adaptive Test)" size="small">
              <Row gutter={16}>
                <Col xs={24} sm={12} md={12}>
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      gap: "12px",
                    }}
                  >
                    <div>
                      <Switch
                        checked={isCatEnabled}
                        onChange={setIsCatEnabled}
                      />
                      <span style={{ marginLeft: 8, fontWeight: "bold" }}>
                        Aktifkan Mode CAT
                      </span>
                      <div
                        style={{
                          fontSize: "12px",
                          color: "#666",
                          marginTop: 4,
                        }}
                      >
                        Mode Computer Adaptive Test untuk ujian yang lebih
                        adaptif
                      </div>
                    </div>

                    <div>
                      <Switch
                        checked={showTimerToParticipants}
                        onChange={setShowTimerToParticipants}
                      />
                      <span style={{ marginLeft: 8 }}>
                        Tampilkan Timer ke Peserta
                      </span>
                      <div
                        style={{
                          fontSize: "12px",
                          color: "#666",
                          marginTop: 4,
                        }}
                      >
                        Peserta dapat melihat sisa waktu ujian
                      </div>
                    </div>

                    <div>
                      <Switch
                        checked={preventCheating}
                        onChange={setPreventCheating}
                      />
                      <span style={{ marginLeft: 8 }}>
                        Pencegahan Kecurangan
                      </span>
                      <div
                        style={{
                          fontSize: "12px",
                          color: "#666",
                          marginTop: 4,
                        }}
                      >
                        Aktifkan fitur anti-cheating
                      </div>
                    </div>
                  </div>
                </Col>

                <Col xs={24} sm={12} md={12}>
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      gap: "12px",
                    }}
                  >
                    <div>
                      <Switch checked={allowReview} onChange={setAllowReview} />
                      <span style={{ marginLeft: 8 }}>
                        Izinkan Review Jawaban
                      </span>
                      <div
                        style={{
                          fontSize: "12px",
                          color: "#666",
                          marginTop: 4,
                        }}
                      >
                        Peserta dapat mereview jawaban sebelum submit
                      </div>
                    </div>

                    <div>
                      <Switch
                        checked={allowBacktrack}
                        onChange={setAllowBacktrack}
                      />
                      <span style={{ marginLeft: 8 }}>
                        Izinkan Kembali ke Soal Sebelumnya
                      </span>
                      <div
                        style={{
                          fontSize: "12px",
                          color: "#666",
                          marginTop: 4,
                        }}
                      >
                        Peserta dapat navigasi ke soal sebelumnya
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
            </Card>

            <br />

            <Card title="Informasi Pengaturan CAT" size="small">
              <div
                style={{
                  padding: "16px",
                  background: "#f0f8ff",
                  borderRadius: "4px",
                }}
              >
                <h4 style={{ marginBottom: "12px", color: "#1890ff" }}>
                  Settings yang akan disimpan:
                </h4>
                <Row gutter={16}>
                  <Col xs={24} sm={12}>
                    <ul style={{ marginBottom: 0, fontSize: "13px" }}>
                      <li>
                        CAT Enabled:{" "}
                        <strong>{isCatEnabled ? "Ya" : "Tidak"}</strong>
                      </li>
                      <li>
                        Show Timer:{" "}
                        <strong>
                          {showTimerToParticipants ? "Ya" : "Tidak"}
                        </strong>
                      </li>
                      <li>
                        Prevent Cheating:{" "}
                        <strong>{preventCheating ? "Ya" : "Tidak"}</strong>
                      </li>
                    </ul>
                  </Col>
                  <Col xs={24} sm={12}>
                    <ul style={{ marginBottom: 0, fontSize: "13px" }}>
                      <li>
                        Allow Review:{" "}
                        <strong>{allowReview ? "Ya" : "Tidak"}</strong>
                      </li>
                      <li>
                        Allow Backtrack:{" "}
                        <strong>{allowBacktrack ? "Ya" : "Tidak"}</strong>
                      </li>
                      <li>
                        Auto End After Duration:{" "}
                        <strong>{autoEndAfterDuration ? "Ya" : "Tidak"}</strong>
                      </li>
                    </ul>
                  </Col>
                </Row>
              </div>
            </Card>
          </Form>
        </TabPane>

        {/* Tab 5: Preview */}
        <TabPane
          tab={
            <span>
              <PlayCircleOutlined />
              Preview
            </span>
          }
          key="5"
        >
          <Card title="Preview Ujian" size="small">
            <div style={{ marginBottom: 16 }}>
              <h3 style={{ marginBottom: 8 }}>
                {form.getFieldValue("namaUjian") || "Nama Ujian Belum Diisi"}
              </h3>
              <p style={{ color: "#666", margin: 0 }}>
                {form.getFieldValue("deskripsiUjian") || "Tidak ada deskripsi"}
              </p>
              <div style={{ marginTop: 8, fontSize: "12px", color: "#999" }}>
                Kode Ujian:{" "}
                <strong>
                  {form.getFieldValue("kodeUjian") || "Belum diisi"}
                </strong>
              </div>
            </div>

            <Row gutter={16}>
              <Col xs={24} sm={8} md={6}>
                <Card size="small" style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "20px",
                      fontWeight: "bold",
                      color: "#1890ff",
                    }}
                  >
                    {form.getFieldValue("durasiMenit") || 0}
                  </div>
                  <div style={{ fontSize: "12px", color: "#666" }}>Menit</div>
                </Card>
              </Col>
              <Col xs={24} sm={8} md={6}>
                <Card size="small" style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "20px",
                      fontWeight: "bold",
                      color: "#52c41a",
                    }}
                  >
                    {form.getFieldValue("jumlahSoalDitampilkan") || totalSoal}
                  </div>
                  <div style={{ fontSize: "12px", color: "#666" }}>Soal</div>
                </Card>
              </Col>
              <Col xs={24} sm={8} md={6}>
                <Card size="small" style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "20px",
                      fontWeight: "bold",
                      color: "#fa8c16",
                    }}
                  >
                    {selectedBankSoal.length}
                  </div>
                  <div style={{ fontSize: "12px", color: "#666" }}>
                    Bank Soal
                  </div>
                </Card>
              </Col>
              <Col xs={24} sm={8} md={6}>
                <Card size="small" style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "20px",
                      fontWeight: "bold",
                      color: "#722ed1",
                    }}
                  >
                    {totalBobot.toFixed(1)}
                  </div>
                  <div style={{ fontSize: "12px", color: "#666" }}>
                    Total Bobot
                  </div>
                </Card>
              </Col>
            </Row>

            <Divider />

            <Row gutter={16}>
              <Col xs={24} sm={12}>
                <h4>Pengaturan Ujian</h4>
                <ul style={{ paddingLeft: 20, fontSize: "13px" }}>
                  <li>Status: {createAndActivate ? "Aktif" : "Draft"}</li>
                  <li>Mulai Otomatis: {isAutoStart ? "Ya" : "Tidak"}</li>
                  <li>Selesai Otomatis: {isAutoEnd ? "Ya" : "Tidak"}</li>
                  <li>Waktu Fleksibel: {isFlexibleTiming ? "Ya" : "Tidak"}</li>
                  <li>Izin Terlambat: {allowLateStart ? "Ya" : "Tidak"}</li>
                  <li>
                    Tampilkan Nilai:{" "}
                    {form.getFieldValue("tampilkanNilai") !== false
                      ? "Ya"
                      : "Tidak"}
                  </li>
                  <li>Tipe Soal: {form.getFieldValue("tipeSoal") || "ACAK"}</li>
                  <li>
                    Strategi Pemilihan:{" "}
                    {form.getFieldValue("strategiPemilihanSoal") || "RANDOM"}
                  </li>
                  <li>
                    Max Percobaan: {form.getFieldValue("maxAttempts") || 1}
                  </li>
                  <li>
                    Nilai Min. Lulus:{" "}
                    {form.getFieldValue("nilaiMinimumLulus") || 60}%
                  </li>
                </ul>
              </Col>
              <Col xs={24} sm={12}>
                <h4>Pengaturan CAT & Keamanan</h4>
                <ul style={{ paddingLeft: 20, fontSize: "13px" }}>
                  <li>CAT Enabled: {isCatEnabled ? "Ya" : "Tidak"}</li>
                  <li>
                    Show Timer: {showTimerToParticipants ? "Ya" : "Tidak"}
                  </li>
                  <li>Prevent Cheating: {preventCheating ? "Ya" : "Tidak"}</li>
                  <li>Allow Review: {allowReview ? "Ya" : "Tidak"}</li>
                  <li>Allow Backtrack: {allowBacktrack ? "Ya" : "Tidak"}</li>
                  <li>
                    Auto End After Duration:{" "}
                    {autoEndAfterDuration ? "Ya" : "Tidak"}
                  </li>
                  <li>
                    Toleransi Keterlambatan:{" "}
                    {form.getFieldValue("toleransiKeterlambatanMenit") || 5}{" "}
                    menit
                  </li>
                </ul>
              </Col>
            </Row>

            <Divider />

            <h4>Jadwal Ujian</h4>
            <Row gutter={16}>
              <Col xs={24} sm={12}>
                <ul style={{ paddingLeft: 20, fontSize: "13px" }}>
                  <li>
                    Mulai:{" "}
                    {form.getFieldValue("waktuMulaiDijadwalkan")
                      ? form
                          .getFieldValue("waktuMulaiDijadwalkan")
                          .format("DD/MM/YYYY HH:mm")
                      : "Belum diset"}
                  </li>
                  <li>
                    Selesai:{" "}
                    {form.getFieldValue("waktuSelesaiOtomatis")
                      ? form
                          .getFieldValue("waktuSelesaiOtomatis")
                          .format("DD/MM/YYYY HH:mm")
                      : "Belum diset"}
                  </li>
                  <li>
                    Zona Waktu:{" "}
                    {form.getFieldValue("timezone") || "Asia/Jakarta"}
                  </li>
                </ul>
              </Col>
              <Col xs={24} sm={12}>
                <ul style={{ paddingLeft: 20, fontSize: "13px" }}>
                  <li>
                    Batas Akhir Mulai:{" "}
                    {form.getFieldValue("batasAkhirMulai")
                      ? form
                          .getFieldValue("batasAkhirMulai")
                          .format("DD/MM/YYYY HH:mm")
                      : "Tidak diset"}
                  </li>
                  <li>
                    Max Late Start:{" "}
                    {form.getFieldValue("maxLateStartMinutes") || 0} menit
                  </li>
                </ul>
              </Col>
            </Row>

            {selectedBankSoal.length > 0 && (
              <>
                <Divider />
                <h4>Detail Bank Soal</h4>
                <div style={{ maxHeight: "300px", overflowY: "auto" }}>
                  {selectedBankSoal.map((item, index) => (
                    <Card
                      key={item.idBankSoal}
                      size="small"
                      style={{ marginBottom: 8 }}
                      title={`${index + 1}. ${item.namaBankSoal}`}
                    >
                      <Row gutter={16}>
                        <Col xs={24} sm={12} md={8}>
                          <div style={{ fontSize: "12px" }}>
                            <strong>Mata Pelajaran:</strong>{" "}
                            {item.mapel?.name || "-"}
                          </div>
                        </Col>
                        <Col xs={24} sm={12} md={8}>
                          <div style={{ fontSize: "12px" }}>
                            <strong>Kelas:</strong>{" "}
                            {item.kelas?.namaKelas || "-"}
                          </div>
                        </Col>
                        <Col xs={24} sm={12} md={8}>
                          <div style={{ fontSize: "12px" }}>
                            <strong>Jumlah Soal:</strong> {item.jumlahSoal || 0}
                          </div>
                        </Col>
                        <Col xs={24} sm={12} md={8}>
                          <div style={{ fontSize: "12px" }}>
                            <strong>Total Bobot:</strong>{" "}
                            {(item.totalBobot || item.jumlahSoal * 10).toFixed(
                              1
                            )}
                          </div>
                        </Col>
                        <Col xs={24} sm={12} md={8}>
                          <div style={{ fontSize: "12px" }}>
                            <strong>Semester:</strong>{" "}
                            {item.semester?.namaSemester || "-"}
                          </div>
                        </Col>
                        <Col xs={24} sm={12} md={8}>
                          <div style={{ fontSize: "12px" }}>
                            <strong>Tahun Ajaran:</strong>{" "}
                            {item.tahunAjaran?.tahunAjaran || "-"}
                          </div>
                        </Col>
                      </Row>
                    </Card>
                  ))}
                </div>
              </>
            )}

            {selectedBankSoal.length === 0 && (
              <div
                style={{
                  textAlign: "center",
                  padding: "40px 0",
                  color: "#999",
                  background: "#fafafa",
                  borderRadius: "4px",
                  marginTop: 16,
                }}
              >
                <BookOutlined style={{ fontSize: "48px", marginBottom: 16 }} />
                <div>Pilih bank soal untuk melihat preview lengkap</div>
              </div>
            )}
          </Card>
        </TabPane>
      </Tabs>
    </Modal>
  );
};

export default AddUjianForm;
