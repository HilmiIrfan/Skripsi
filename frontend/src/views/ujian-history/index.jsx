/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useCallback } from "react";
import {
  Card,
  Table,
  Tag,
  Button,
  Space,
  Typography,
  Descriptions,
  Modal,
  message,
  Tooltip,
  Alert,
  Row,
  Col,
  Statistic,
  Divider,
  Progress,
  Badge,
} from "antd";
import {
  EyeOutlined,
  FileTextOutlined,
  WarningOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  ClockCircleOutlined,
  TrophyOutlined,
  ExclamationCircleOutlined,
  ReloadOutlined,
} from "@ant-design/icons";
import { getHasilByPeserta } from "@/api/hasilUjian";
import { getCheatDetectionByStudent } from "@/api/cheatDetection";
import TypingCard from "@/components/TypingCard";
import { useSelector } from "react-redux";
import dayjs from "dayjs";

const { Title, Text, Paragraph } = Typography;

const UjianHistory = () => {
  const [loading, setLoading] = useState(false);
  const [historyData, setHistoryData] = useState([]);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [violationModalVisible, setViolationModalVisible] = useState(false);
  const [selectedHistory, setSelectedHistory] = useState(null);
  const [violations, setViolations] = useState([]);
  const [loadingViolations, setLoadingViolations] = useState(false);
  const [statistics, setStatistics] = useState({
    totalUjian: 0,
    lulus: 0,
    tidakLulus: 0,
    rataRataNilai: 0,
  });
  const userState = useSelector((state) => state.user || {});

  // Reconstruct user object for compatibility
  const user = userState.idUser
    ? {
        id: userState.idUser,
        name: userState.name,
        role: userState.role,
        avatar: userState.avatar,
        token: userState.token,
      }
    : null;

  // Fetch history data
  const fetchHistoryData = useCallback(async () => {
    if (!user?.id) {
      setHistoryData([]);
      setStatistics({
        totalUjian: 0,
        lulus: 0,
        tidakLulus: 0,
        rataRataNilai: 0,
      });
      return;
    }

    setLoading(true);
    try {
      const response = await getHasilByPeserta(user.id, 50);
      const { content, statusCode } = response.data;

      if (statusCode === 200 && content) {
        const sortedData = content.sort(
          (a, b) =>
            dayjs(b.waktuSelesai || b.createdDate).valueOf() -
            dayjs(a.waktuSelesai || a.createdDate).valueOf()
        );
        setHistoryData(sortedData);
        calculateStatistics(sortedData);
      } else {
        setHistoryData([]);
      }
    } catch (error) {
      console.error("Error fetching history:", error);
      message.error("Gagal memuat riwayat ujian");
      setHistoryData([]);
    } finally {
      setLoading(false);
    }
  }, [user?.id]);
  // Calculate statistics - IMPROVED with null checks
  const calculateStatistics = (data) => {
    if (!data || !Array.isArray(data)) {
      setStatistics({
        totalUjian: 0,
        lulus: 0,
        tidakLulus: 0,
        rataRataNilai: 0,
      });
      return;
    }

    const total = data.length;
    const lulus = data.filter((item) => {
      const nilai = parseFloat(item.skor || item.nilai || 0);
      const minScore = parseFloat(
        item.ujian?.minPassingScore || item.ujian?.nilaiMinimal || 0
      );
      return nilai >= minScore;
    }).length;
    const tidakLulus = total - lulus;
    const rataRata =
      total > 0
        ? (
            data.reduce(
              (sum, item) => sum + (parseFloat(item.skor || item.nilai) || 0),
              0
            ) / total
          ).toFixed(2)
        : 0;

    setStatistics({
      totalUjian: total,
      lulus,
      tidakLulus,
      rataRataNilai: parseFloat(rataRata),
    });
  };
  // Fetch violations for specific ujian
  const fetchViolations = async (ujianId, pesertaId) => {
    setLoadingViolations(true);
    try {
      const response = await getCheatDetectionByStudent(ujianId, pesertaId);
      const { content, statusCode } = response.data;

      if (statusCode === 200 && content) {
        setViolations(content);
      }
    } catch (error) {
      console.error("Error fetching violations:", error);
      setViolations([]);
    } finally {
      setLoadingViolations(false);
    }
  };
  useEffect(() => {
    fetchHistoryData();
  }, [fetchHistoryData]); // Get status color and icon
  const getStatusDisplay = (record) => {
    if (!record)
      return {
        color: "default",
        icon: <ExclamationCircleOutlined />,
        text: "BELUM DIKETAHUI",
      };

    // Check if exam is completed and has a valid score
    const nilai = parseFloat(
      record.skor || record.nilai || record.persentase || 0
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
          color: "success",
          icon: <CheckCircleOutlined />,
          text: "LULUS",
        };
      }
    }

    // Fallback to score-based logic if lulus field is not available
    if (hasValidScore) {
      const minScore = parseFloat(
        record.ujian?.minPassingScore || record.ujian?.nilaiMinimal || 0
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

  // Show detail modal
  const showDetailModal = (record) => {
    setSelectedHistory(record);
    setDetailModalVisible(true);
  };
  // Show violations modal
  const showViolationsModal = (record) => {
    setSelectedHistory(record);
    setViolationModalVisible(true);
    const ujianId = record.ujian?.idUjian || record.idUjian;
    const pesertaId = record.idPeserta || user?.id;
    fetchViolations(ujianId, pesertaId);
  };
  // Table columns
  const columns = [
    {
      title: "Ujian",
      dataIndex: ["ujian", "namaUjian"],
      key: "namaUjian",
      render: (text, record) => (
        <div>
          <Text strong>
            {record.ujian?.namaUjian ||
              record.namaUjian ||
              "Nama Ujian Tidak Tersedia"}
          </Text>
          <br />
          <Text type="secondary" style={{ fontSize: "12px" }}>
            {record.ujian?.mapel?.name || record.mapelNama || "Mata Pelajaran"}
          </Text>
          <br />
          <Text type="secondary" style={{ fontSize: "11px" }}>
            {record.ujian?.kelas?.namaKelas || record.kelasNama || ""} -{" "}
            {record.ujian?.semester?.namaSemester || record.semesterNama || ""}
          </Text>
        </div>
      ),
    },
    {
      title: "Waktu Ujian",
      key: "waktu",
      render: (_, record) => (
        <div>
          <div>
            <Text type="secondary">Mulai: </Text>
            <Text>
              {record.ujian?.waktuMulaiDijadwalkan
                ? dayjs(record.ujian.waktuMulaiDijadwalkan).format(
                    "DD/MM/YYYY HH:mm"
                  )
                : record.waktuMulai
                ? dayjs(record.waktuMulai).format("DD/MM/YYYY HH:mm")
                : "-"}
            </Text>
          </div>
          <div>
            <Text type="secondary">Selesai: </Text>
            <Text>
              {record.ujian?.waktuSelesaiOtomatis
                ? dayjs(record.ujian.waktuSelesaiOtomatis).format(
                    "DD/MM/YYYY HH:mm"
                  )
                : record.waktuSelesai
                ? dayjs(record.waktuSelesai).format("DD/MM/YYYY HH:mm")
                : "-"}
            </Text>
          </div>
          <div>
            <Text type="secondary">Durasi: </Text>
            <Text>
              {record.ujian?.durasiMenit || record.durasiMenit || 0} menit
            </Text>
          </div>
        </div>
      ),
    },
    {
      title: "Nilai",
      key: "nilai",
      align: "center",
      render: (_, record) => {
        const ujian = record.ujian || record;
        const showNilai = ujian.tampilkanNilai !== false; // default true jika tidak ada setting

        if (!showNilai) {
          return (
            <div style={{ textAlign: "center" }}>
              <Text type="secondary">Nilai Disembunyikan</Text>
            </div>
          );
        }

        const nilai = parseFloat(
          record.skor || record.nilai || record.persentase || 0
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
    {
      title: "Status",
      key: "status",
      align: "center",
      render: (_, record) => {
        const { color, icon, text } = getStatusDisplay(record);
        return (
          <Tag color={color} icon={icon}>
            {text}
          </Tag>
        );
      },
    },
    // {
    //   title: "Pelanggaran",
    //   key: "violations",
    //   align: "center",
    //   render: (_, record) => {
    //     const violationCount = parseInt(record.jumlahPelanggaran) || 0;

    //     if (violationCount > 0) {
    //       return (
    //         <Badge count={violationCount} overflowCount={99}>
    //           <Button
    //             type="text"
    //             icon={<WarningOutlined />}
    //             danger
    //             onClick={() => showViolationsModal(record)}
    //           >
    //             Lihat
    //           </Button>
    //         </Badge>
    //       );
    //     }

    //     return <Tag color="success">Bersih</Tag>;
    //   },
    // },
    {
      title: "Aksi",
      key: "action",
      align: "center",
      render: (_, record) => (
        <Space>
          <Tooltip title="Lihat Detail">
            <Button
              type="primary"
              icon={<EyeOutlined />}
              size="small"
              onClick={() => showDetailModal(record)}
            >
              Detail
            </Button>
          </Tooltip>

          {record.ujian?.allowReview && (
            <Tooltip title="Review Jawaban">
              <Button
                icon={<FileTextOutlined />}
                size="small"
                onClick={() => showDetailModal(record)}
              >
                Review
              </Button>
            </Tooltip>
          )}
        </Space>
      ),
    },
  ];
  return (
    <div className="app-container">
      <TypingCard title="Riwayat Ujian Saya" source="" />

      {!user ? (
        <Card>
          <Alert
            message="User tidak ditemukan"
            description="Silakan login kembali untuk mengakses halaman ini."
            type="warning"
            showIcon
          />
        </Card>
      ) : (
        <>
          {/* Statistics Cards */}
          <Row gutter={16} style={{ marginBottom: 24 }}>
            <Col span={6}>
              <Card>
                <Statistic
                  title="Total Ujian"
                  value={statistics.totalUjian}
                  prefix={<FileTextOutlined />}
                  valueStyle={{ color: "#1890ff" }}
                />
              </Card>
            </Col>
            <Col span={6}>
              <Card>
                <Statistic
                  title="Lulus"
                  value={statistics.lulus}
                  prefix={<CheckCircleOutlined />}
                  valueStyle={{ color: "#52c41a" }}
                />
              </Card>
            </Col>
            {/* <Col span={6}>
              <Card>
                <Statistic
                  title="Tidak Lulus"
                  value={statistics.tidakLulus}
                  prefix={<CloseCircleOutlined />}
                  valueStyle={{ color: "#ff4d4f" }}
                />
              </Card>
            </Col> */}
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

          {/* Main Table */}
          <Card
            title={
              <Space>
                <FileTextOutlined />
                <span>Daftar Riwayat Ujian</span>
              </Space>
            }
            extra={
              <Button
                type="primary"
                icon={<ReloadOutlined />}
                onClick={fetchHistoryData}
                loading={loading}
              >
                Refresh
              </Button>
            }
          >
            <Table
              columns={columns}
              dataSource={historyData}
              loading={loading}
              rowKey="id"
              pagination={{
                showSizeChanger: true,
                showQuickJumper: true,
                showTotal: (total, range) =>
                  `${range[0]}-${range[1]} dari ${total} ujian`,
              }}
              locale={{
                emptyText: "Belum ada riwayat ujian",
              }}
            />
          </Card>
        </>
      )}

      {/* Detail Modal */}
      <Modal
        title={
          <Space>
            <EyeOutlined />
            <span>Detail Hasil Ujian</span>
          </Space>
        }
        open={detailModalVisible}
        onCancel={() => setDetailModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setDetailModalVisible(false)}>
            Tutup
          </Button>,
        ]}
        width={800}
      >
        {selectedHistory && (
          <div>
            {" "}
            <Descriptions bordered column={2}>
              <Descriptions.Item label="Nama Ujian" span={2}>
                <Text strong>
                  {selectedHistory.ujian?.namaUjian ||
                    selectedHistory.namaUjian ||
                    "Tidak tersedia"}
                </Text>
              </Descriptions.Item>
              {/* <Descriptions.Item label="Mata Pelajaran">
                {selectedHistory.ujian?.mapel?.name ||
                  selectedHistory.mapelNama ||
                  "Tidak tersedia"}
              </Descriptions.Item>
              <Descriptions.Item label="Kelas">
                {selectedHistory.ujian?.kelas?.namaKelas ||
                  selectedHistory.kelasNama ||
                  "Tidak tersedia"}
              </Descriptions.Item>{" "} */}
              <Descriptions.Item label="Waktu Mulai Pengerjaan">
                {selectedHistory.waktuMulai
                  ? dayjs(selectedHistory.waktuMulai).format(
                      "DD/MM/YYYY HH:mm:ss"
                    )
                  : "Tidak tersedia"}
              </Descriptions.Item>
              <Descriptions.Item label="Waktu Selesai Pengerjaan">
                {selectedHistory.waktuSelesai
                  ? dayjs(selectedHistory.waktuSelesai).format(
                      "DD/MM/YYYY HH:mm:ss"
                    )
                  : "Tidak tersedia"}
              </Descriptions.Item>
              <Descriptions.Item label="Durasi Ujian">
                {selectedHistory.ujian?.durasiMenit ||
                  selectedHistory.durasiMenit ||
                  "Tidak dibatasi"}{" "}
                {selectedHistory.ujian?.durasiMenit ? "menit" : ""}
              </Descriptions.Item>{" "}
              <Descriptions.Item label="Jumlah Soal">
                {selectedHistory.ujian?.jumlahSoal ||
                  selectedHistory.jumlahSoal ||
                  selectedHistory.totalSoal ||
                  "Tidak tersedia"}
              </Descriptions.Item>
              <Descriptions.Item label="Status Pengerjaan">
                <Tag
                  color={
                    selectedHistory.statusPengerjaan === "SELESAI"
                      ? "green"
                      : "orange"
                  }
                >
                  {selectedHistory.statusPengerjaan || "Tidak diketahui"}
                </Tag>
              </Descriptions.Item>
              <Descriptions.Item label="Attempt Number">
                {selectedHistory.attemptNumber || 1}
              </Descriptions.Item>
              <Descriptions.Item label="Auto Submit">
                <Tag color={selectedHistory.isAutoSubmit ? "red" : "green"}>
                  {selectedHistory.isAutoSubmit ? "Ya" : "Tidak"}
                </Tag>
              </Descriptions.Item>
              {/* Tampilkan nilai hanya jika diizinkan */}
              {selectedHistory.ujian?.tampilkanNilai !== false && (
                <>
                  <Descriptions.Item label="Jawaban Benar">
                    <Text style={{ color: "#52c41a", fontWeight: "bold" }}>
                      {selectedHistory.jumlahBenar || 0}
                    </Text>
                  </Descriptions.Item>
                  <Descriptions.Item label="Jawaban Salah">
                    <Text style={{ color: "#ff4d4f", fontWeight: "bold" }}>
                      {selectedHistory.jumlahSalah || 0}
                    </Text>
                  </Descriptions.Item>
                  <Descriptions.Item label="Jawaban Kosong">
                    <Text style={{ color: "#faad14", fontWeight: "bold" }}>
                      {selectedHistory.jumlahKosong || 0}
                    </Text>
                  </Descriptions.Item>
                  <Descriptions.Item label="Total Soal">
                    <Text style={{ fontWeight: "bold" }}>
                      {selectedHistory.totalSoal || 0}
                    </Text>
                  </Descriptions.Item>
                  <Descriptions.Item label="Nilai Akhir">
                    <div style={{ textAlign: "center" }}>
                      <div
                        style={{
                          fontSize: "24px",
                          fontWeight: "bold",
                          color: selectedHistory.lulus ? "#52c41a" : "#ff4d4f",
                        }}
                      >
                        {parseFloat(selectedHistory.totalSkor || 0).toFixed(1)}
                      </div>
                      <div style={{ fontSize: "14px", color: "#666" }}>
                        Persentase:{" "}
                        {parseFloat(selectedHistory.persentase || 0).toFixed(1)}
                        %
                      </div>
                      {/* <div style={{ fontSize: "12px", color: "#666" }}>
                        Grade: {selectedHistory.nilaiHuruf || "-"}
                      </div> */}
                    </div>
                  </Descriptions.Item>
                  <Descriptions.Item label="Durasi Pengerjaan">
                    <Text>
                      {selectedHistory.durasiPengerjaan
                        ? `${Math.floor(
                            selectedHistory.durasiPengerjaan / 60
                          )} menit ${
                            selectedHistory.durasiPengerjaan % 60
                          } detik`
                        : "Tidak tersedia"}
                    </Text>
                  </Descriptions.Item>
                </>
              )}
              <Descriptions.Item label="Status">
                {(() => {
                  const { color, icon, text } =
                    getStatusDisplay(selectedHistory);
                  return (
                    <Tag color={color} icon={icon}>
                      {text}
                    </Tag>
                  );
                })()}
              </Descriptions.Item>
            </Descriptions>
            {/* Analytics Section */}
            {(selectedHistory.rataRataKelas ||
              selectedHistory.persentilSiswa ||
              selectedHistory.tingkatKesulitan) && (
              <>
                <Divider orientation="left">Analisis Kelas</Divider>
                <Row gutter={16}>
                  {selectedHistory.rataRataKelas && (
                    <Col span={8}>
                      <Card size="small">
                        <Statistic
                          title="Rata-rata Kelas"
                          value={parseFloat(
                            selectedHistory.rataRataKelas
                          ).toFixed(1)}
                          precision={1}
                        />
                      </Card>
                    </Col>
                  )}
                  {selectedHistory.persentilSiswa && (
                    <Col span={8}>
                      <Card size="small">
                        <Statistic
                          title="Persentil Anda"
                          value={parseFloat(
                            selectedHistory.persentilSiswa
                          ).toFixed(0)}
                          suffix="%"
                        />
                      </Card>
                    </Col>
                  )}
                  {selectedHistory.tingkatKesulitan && (
                    <Col span={8}>
                      <Card size="small">
                        <Statistic
                          title="Tingkat Kesulitan"
                          value={selectedHistory.tingkatKesulitan}
                        />
                      </Card>
                    </Col>
                  )}
                </Row>
              </>
            )}{" "}
            {/* Notice untuk nilai dan review */}
            {selectedHistory.ujian?.tampilkanNilai === false && (
              <Alert
                message="Nilai Disembunyikan"
                description="Pengajar telah mengatur agar nilai tidak ditampilkan untuk ujian ini."
                type="info"
                showIcon
                style={{ marginTop: 16 }}
              />
            )}
            {!selectedHistory.ujian?.allowReview && (
              <Alert
                message="Review Jawaban Tidak Diizinkan"
                description="Pengajar tidak mengizinkan untuk melihat detail jawaban pada ujian ini."
                type="info"
                showIcon
                style={{ marginTop: 16 }}
              />
            )}
          </div>
        )}
      </Modal>

      {/* Violations Modal */}
      <Modal
        title={
          <Space>
            <WarningOutlined style={{ color: "#ff4d4f" }} />
            <span>Rincian Pelanggaran</span>
          </Space>
        }
        open={violationModalVisible}
        onCancel={() => setViolationModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setViolationModalVisible(false)}>
            Tutup
          </Button>,
        ]}
        width={700}
      >
        {selectedHistory && (
          <div>
            <Alert
              message={`Ditemukan ${violations.length} pelanggaran selama ujian`}
              type="warning"
              showIcon
              style={{ marginBottom: 16 }}
            />

            {loadingViolations ? (
              <div style={{ textAlign: "center", padding: "20px" }}>
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
                      <Tag color="red">
                        {violation.jenisKecurangan || "Tidak diketahui"}
                      </Tag>
                    </Descriptions.Item>
                    <Descriptions.Item label="Waktu">
                      {violation.timestamp
                        ? dayjs(violation.timestamp).format(
                            "DD/MM/YYYY HH:mm:ss"
                          )
                        : "Tidak tersedia"}
                    </Descriptions.Item>
                    <Descriptions.Item label="Tingkat Kepercayaan">
                      <Progress
                        percent={
                          parseFloat(violation.confidenceScore || 0) * 100
                        }
                        size="small"
                        status={
                          violation.confidenceScore > 0.7
                            ? "exception"
                            : "normal"
                        }
                      />
                    </Descriptions.Item>
                    {violation.details && (
                      <Descriptions.Item label="Detail">
                        <Text type="secondary">{violation.details}</Text>
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
    </div>
  );
};

export default UjianHistory;
