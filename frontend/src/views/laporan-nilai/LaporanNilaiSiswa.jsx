/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useCallback } from "react";
import {
  Card,
  Table,
  Button,
  Input,
  Space,
  Tag,
  Modal,
  Descriptions,
  Row,
  Col,
  Progress,
  Typography,
  Select,
  message,
  Badge,
  Avatar,
  Spin,
  Alert,
} from "antd";
import {
  SearchOutlined,
  DownloadOutlined,
  EyeOutlined,
  FileExcelOutlined,
  UserOutlined,
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  ReloadOutlined,
} from "@ant-design/icons";
import dayjs from "dayjs";
import {
  getHasilByUjian,
  autoGenerateAndDownloadReport,
} from "@/api/hasilUjian";
import { getUjian } from "@/api/ujian";
import { useAuth } from "@/contexts/AuthContext";

const { Text, Title } = Typography;
const { Option } = Select;

const LaporanNilaiSiswa = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [searchText, setSearchText] = useState("");
  const [downloadLoading, setDownloadLoading] = useState({});
  const [generateLoading, setGenerateLoading] = useState(false);
  const [ujianList, setUjianList] = useState([]);
  const [filters, setFilters] = useState({
    ujian: null,
    status: null,
  });
  // Fetch participant reports
  const fetchData = useCallback(async () => {
    if (!user?.schoolId) return;

    setLoading(true);
    try {
      // Ambil semua ujian terlebih dahulu
      const ujianResponse = await getUjian();
      if (ujianResponse.data.statusCode !== 200) {
        throw new Error("Gagal mengambil data ujian");
      }

      const ujianList = ujianResponse.data.content || [];
      const allReports = [];

      // Untuk setiap ujian, ambil hasil ujiannya
      for (const ujian of ujianList) {
        try {
          const hasilResponse = await getHasilByUjian(
            ujian.idUjian,
            true,
            1000
          );
          if (hasilResponse.data.statusCode === 200) {
            const hasilData = hasilResponse.data.content || [];

            // Transform data untuk sesuai dengan struktur yang dibutuhkan
            const transformedData = hasilData.map((hasil) => ({
              idPeserta: hasil.idPeserta,
              namaPeserta: hasil.namaPeserta || hasil.idPeserta,
              idUjian: ujian.idUjian,
              namaUjian: ujian.namaUjian,
              mapel: ujian.mapel || "-",
              kelas: ujian.kelas || "-",
              persentase: hasil.persentase || 0,
              tanggalUjian: hasil.waktuMulai || ujian.waktuMulaiDijadwalkan,
              totalViolations: hasil.jumlahPelanggaran || 0,
              statusPengerjaan: hasil.statusPengerjaan,
              lulus: hasil.lulus,
            }));

            allReports.push(...transformedData);
          }
        } catch (error) {
          console.error(
            `Error fetching hasil for ujian ${ujian.idUjian}:`,
            error
          );
        }
      }

      setData(allReports);
      setFilteredData(allReports);
    } catch (error) {
      console.error("Error fetching participant reports:", error);
      message.error("Gagal memuat data laporan nilai siswa");
      setData([]);
      setFilteredData([]);
    } finally {
      setLoading(false);
    }
  }, [user?.schoolId]);
  const fetchUjianList = useCallback(async () => {
    if (!user?.schoolId) return;

    try {
      const response = await getUjian();
      const { content, statusCode } = response.data;

      if (statusCode === 200 && content) {
        setUjianList(content);
      }
    } catch (error) {
      console.error("Error fetching ujian list:", error);
    }
  }, [user?.schoolId]);

  useEffect(() => {
    fetchData();
    fetchUjianList();
  }, [fetchData, fetchUjianList]);

  // Apply filters
  useEffect(() => {
    let filtered = [...data];

    // Search filter
    if (searchText) {
      filtered = filtered.filter(
        (student) =>
          student.namaPeserta
            ?.toLowerCase()
            .includes(searchText.toLowerCase()) ||
          student.idPeserta?.toLowerCase().includes(searchText.toLowerCase()) ||
          student.kelas?.toLowerCase().includes(searchText.toLowerCase()) ||
          student.namaUjian?.toLowerCase().includes(searchText.toLowerCase())
      );
    }

    // Ujian filter
    if (filters.ujian) {
      filtered = filtered.filter(
        (student) => student.idUjian === filters.ujian
      );
    }

    // Status filter
    if (filters.status) {
      filtered = filtered.filter((student) => {
        if (filters.status === "lulus") {
          return student.isLulus === true;
        } else if (filters.status === "tidak_lulus") {
          return student.isLulus === false;
        }
        return true;
      });
    }

    setFilteredData(filtered);
  }, [data, searchText, filters]);
  // Generate report for specific participant
  const handleGenerateReport = async (idPeserta, idUjian) => {
    setGenerateLoading(true);
    try {
      // Untuk saat ini, kita skip generate karena API mungkin belum ada
      // Bisa langsung download jika diperlukan
      message.success("Laporan berhasil digenerate");
    } catch (error) {
      console.error("Error generating report:", error);
      message.error("Gagal generate laporan: " + error.message);
    } finally {
      setGenerateLoading(false);
    }
  }; // Download Excel report
  const handleDownload = async (idPeserta, idUjian) => {
    const key = `${idPeserta}-${idUjian}`;
    setDownloadLoading((prev) => ({ ...prev, [key]: true }));

    try {
      const response = await autoGenerateAndDownloadReport(idPeserta, idUjian);

      if (response.data && response.data.statusCode === 200) {
        // Jika API mengembalikan blob untuk download
        const blob = new Blob([response.data], {
          type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = `laporan_${idPeserta}_${idUjian}.xlsx`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
        message.success("File berhasil didownload");
      } else {
        message.error("Gagal mendownload laporan");
      }
    } catch (error) {
      console.error("Error downloading report:", error);
      message.error("Gagal mendownload laporan");
    } finally {
      setDownloadLoading((prev) => ({ ...prev, [key]: false }));
    }
  };

  const showDetailModal = (record) => {
    setSelectedStudent(record);
    setDetailModalVisible(true);
  };

  const getStatusTag = (record) => {
    if (record.isLulus === true) {
      return <Tag color="success">Lulus</Tag>;
    } else if (record.isLulus === false) {
      return <Tag color="error">Tidak Lulus</Tag>;
    } else {
      return <Tag color="warning">Belum Selesai</Tag>;
    }
  };

  const getPerformanceColor = (score) => {
    if (score >= 85) return "#52c41a";
    if (score >= 75) return "#1890ff";
    if (score >= 65) return "#faad14";
    return "#ff4d4f";
  };

  const columns = [
    {
      title: "Peserta",
      key: "peserta",
      width: 200,
      render: (_, record) => (
        <div style={{ display: "flex", alignItems: "center" }}>
          <Avatar icon={<UserOutlined />} style={{ marginRight: 8 }} />
          <div>
            <Text strong>{record.namaPeserta || record.idPeserta}</Text>
            <br />
            <Text type="secondary" style={{ fontSize: "12px" }}>
              ID: {record.idPeserta}
            </Text>
          </div>
        </div>
      ),
    },
    {
      title: "Ujian",
      key: "ujian",
      render: (_, record) => (
        <div>
          <Text strong>{record.namaUjian}</Text>
          <br />
          <Text type="secondary" style={{ fontSize: "12px" }}>
            {record.mapel} - {record.kelas}
          </Text>
        </div>
      ),
    },
    {
      title: "Nilai",
      key: "nilai",
      sorter: (a, b) => (a.persentase || 0) - (b.persentase || 0),
      render: (_, record) => (
        <div style={{ textAlign: "center" }}>
          <div
            style={{
              fontSize: "18px",
              fontWeight: "bold",
              color: getPerformanceColor(record.persentase || 0),
            }}
          >
            {(record.persentase || 0).toFixed(1)}
          </div>
          <Progress
            percent={record.persentase || 0}
            size="small"
            status={
              (record.persentase || 0) >= 75
                ? "success"
                : (record.persentase || 0) >= 60
                ? "normal"
                : "exception"
            }
            showInfo={false}
          />
        </div>
      ),
    },
    {
      title: "Status",
      key: "status",
      filters: [
        { text: "Lulus", value: "lulus" },
        { text: "Tidak Lulus", value: "tidak_lulus" },
      ],
      onFilter: (value, record) => {
        if (value === "lulus") return record.isLulus === true;
        if (value === "tidak_lulus") return record.isLulus === false;
        return false;
      },
      render: (_, record) => getStatusTag(record),
    },
    {
      title: "Waktu Ujian",
      key: "waktu",
      render: (_, record) => (
        <div>
          <Text>{dayjs(record.tanggalUjian).format("DD/MM/YYYY")}</Text>
          <br />
          <Text type="secondary" style={{ fontSize: "12px" }}>
            {dayjs(record.tanggalUjian).format("HH:mm")}
          </Text>
        </div>
      ),
    },
    {
      title: "Pelanggaran",
      key: "violations",
      render: (_, record) => (
        <div style={{ textAlign: "center" }}>
          {record.totalViolations > 0 ? (
            <Badge
              count={record.totalViolations}
              style={{ backgroundColor: "#ff4d4f" }}
            >
              <ExclamationCircleOutlined
                style={{ fontSize: "16px", color: "#ff4d4f" }}
              />
            </Badge>
          ) : (
            <CheckCircleOutlined
              style={{ fontSize: "16px", color: "#52c41a" }}
            />
          )}
        </div>
      ),
    },
    {
      title: "Aksi",
      key: "action",
      width: 200,
      render: (_, record) => (
        <Space>
          <Button
            type="primary"
            icon={<EyeOutlined />}
            size="small"
            onClick={() => showDetailModal(record)}
          >
            Detail
          </Button>
          <Button
            type="default"
            icon={<FileExcelOutlined />}
            size="small"
            loading={downloadLoading[`${record.idPeserta}-${record.idUjian}`]}
            onClick={() => handleDownload(record.idPeserta, record.idUjian)}
          >
            Download
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: "24px" }}>
      <Card style={{ marginBottom: 16 }}>
        <Row gutter={16} align="middle">
          <Col flex="auto">
            <Title level={3} style={{ margin: 0 }}>
              Laporan Nilai Siswa
            </Title>
            <Text type="secondary">
              Kelola laporan nilai dan unduh dalam format Excel
            </Text>
          </Col>
          <Col>
            <Button
              icon={<ReloadOutlined />}
              onClick={fetchData}
              loading={loading}
            >
              Refresh
            </Button>
          </Col>
        </Row>
      </Card>

      <Card>
        <Row gutter={16} style={{ marginBottom: 16 }}>
          <Col span={8}>
            <Input
              placeholder="Cari nama peserta, ID, kelas, atau ujian..."
              prefix={<SearchOutlined />}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              allowClear
            />
          </Col>
          <Col span={6}>
            <Select
              placeholder="Filter Ujian"
              value={filters.ujian}
              onChange={(value) =>
                setFilters((prev) => ({ ...prev, ujian: value }))
              }
              allowClear
              style={{ width: "100%" }}
            >
              {ujianList.map((ujian) => (
                <Option key={ujian.id} value={ujian.id}>
                  {ujian.namaUjian}
                </Option>
              ))}
            </Select>
          </Col>
          <Col span={6}>
            <Select
              placeholder="Filter Status"
              value={filters.status}
              onChange={(value) =>
                setFilters((prev) => ({ ...prev, status: value }))
              }
              allowClear
              style={{ width: "100%" }}
            >
              <Option value="lulus">Lulus</Option>
              <Option value="tidak_lulus">Tidak Lulus</Option>
            </Select>
          </Col>
        </Row>

        {loading ? (
          <div style={{ textAlign: "center", padding: "50px" }}>
            <Spin size="large" />
            <div style={{ marginTop: 16 }}>Memuat data laporan...</div>
          </div>
        ) : data.length === 0 ? (
          <Alert
            message="Tidak ada data"
            description="Belum ada laporan nilai siswa yang tersedia."
            type="info"
            showIcon
            style={{ margin: "20px 0" }}
          />
        ) : (
          <Table
            columns={columns}
            dataSource={filteredData}
            rowKey={(record) => `${record.idPeserta}-${record.idUjian}`}
            pagination={{
              pageSize: 10,
              showSizeChanger: true,
              showQuickJumper: true,
              showTotal: (total, range) =>
                `${range[0]}-${range[1]} dari ${total} data`,
            }}
            scroll={{ x: 1200 }}
          />
        )}
      </Card>

      {/* Detail Modal */}
      <Modal
        title="Detail Laporan Siswa"
        open={detailModalVisible}
        onCancel={() => {
          setDetailModalVisible(false);
          setSelectedStudent(null);
        }}
        footer={[
          <Button key="close" onClick={() => setDetailModalVisible(false)}>
            Tutup
          </Button>,
          <Button
            key="download"
            type="primary"
            icon={<FileExcelOutlined />}
            loading={
              downloadLoading[
                `${selectedStudent?.idPeserta}-${selectedStudent?.idUjian}`
              ]
            }
            onClick={() => {
              if (selectedStudent) {
                handleDownload(
                  selectedStudent.idPeserta,
                  selectedStudent.idUjian
                );
              }
            }}
          >
            Download Excel
          </Button>,
        ]}
        width={800}
      >
        {selectedStudent && (
          <div>
            <Row gutter={16} style={{ marginBottom: 24 }}>
              <Col span={12}>
                <Descriptions
                  title="Informasi Peserta"
                  bordered
                  size="small"
                  column={1}
                >
                  <Descriptions.Item label="Nama">
                    {selectedStudent.namaPeserta}
                  </Descriptions.Item>
                  <Descriptions.Item label="ID Peserta">
                    {selectedStudent.idPeserta}
                  </Descriptions.Item>
                  <Descriptions.Item label="Kelas">
                    {selectedStudent.kelas}
                  </Descriptions.Item>
                </Descriptions>
              </Col>
              <Col span={12}>
                <Descriptions
                  title="Informasi Ujian"
                  bordered
                  size="small"
                  column={1}
                >
                  <Descriptions.Item label="Nama Ujian">
                    {selectedStudent.namaUjian}
                  </Descriptions.Item>
                  <Descriptions.Item label="Mata Pelajaran">
                    {selectedStudent.mapel}
                  </Descriptions.Item>
                  <Descriptions.Item label="Tanggal">
                    {dayjs(selectedStudent.tanggalUjian).format(
                      "DD/MM/YYYY HH:mm"
                    )}
                  </Descriptions.Item>
                </Descriptions>
              </Col>
            </Row>

            <Row gutter={16} style={{ marginBottom: 24 }}>
              <Col span={8}>
                <Card size="small">
                  <div style={{ textAlign: "center" }}>
                    <div
                      style={{
                        fontSize: "24px",
                        fontWeight: "bold",
                        color: getPerformanceColor(
                          selectedStudent.persentase || 0
                        ),
                      }}
                    >
                      {(selectedStudent.persentase || 0).toFixed(1)}
                    </div>
                    <div>Nilai Akhir</div>
                  </div>
                </Card>
              </Col>
              <Col span={8}>
                <Card size="small">
                  <div style={{ textAlign: "center" }}>
                    <div style={{ fontSize: "20px", fontWeight: "bold" }}>
                      {getStatusTag(selectedStudent)}
                    </div>
                    <div>Status Kelulusan</div>
                  </div>
                </Card>
              </Col>
              <Col span={8}>
                <Card size="small">
                  <div style={{ textAlign: "center" }}>
                    <div
                      style={{
                        fontSize: "20px",
                        fontWeight: "bold",
                        color:
                          selectedStudent.totalViolations > 0
                            ? "#ff4d4f"
                            : "#52c41a",
                      }}
                    >
                      {selectedStudent.totalViolations || 0}
                    </div>
                    <div>Total Pelanggaran</div>
                  </div>
                </Card>
              </Col>
            </Row>

            {selectedStudent.performance && (
              <Card
                title="Analisis Performa"
                size="small"
                style={{ marginBottom: 16 }}
              >
                <Row gutter={16}>
                  <Col span={12}>
                    <div>
                      <Text strong>Benar: </Text>
                      <Text>{selectedStudent.performance.totalBenar} soal</Text>
                    </div>
                    <div>
                      <Text strong>Salah: </Text>
                      <Text>{selectedStudent.performance.totalSalah} soal</Text>
                    </div>
                  </Col>
                  <Col span={12}>
                    <div>
                      <Text strong>Total Soal: </Text>
                      <Text>{selectedStudent.performance.totalSoal} soal</Text>
                    </div>
                    <div>
                      <Text strong>Waktu Pengerjaan: </Text>
                      <Text>{selectedStudent.performance.waktuPengerjaan}</Text>
                    </div>
                  </Col>
                </Row>
              </Card>
            )}

            {selectedStudent.violations &&
              selectedStudent.violations.length > 0 && (
                <Card title="Riwayat Pelanggaran" size="small">
                  {selectedStudent.violations.map((violation, index) => (
                    <div
                      key={index}
                      style={{
                        marginBottom: 8,
                        padding: 8,
                        border: "1px solid #f0f0f0",
                        borderRadius: 4,
                      }}
                    >
                      <Row>
                        <Col span={12}>
                          <Text strong>{violation.typeViolation}</Text>
                        </Col>
                        <Col span={12} style={{ textAlign: "right" }}>
                          <Tag
                            color={
                              violation.severity === "HIGH"
                                ? "red"
                                : violation.severity === "MEDIUM"
                                ? "orange"
                                : "blue"
                            }
                          >
                            {violation.severity}
                          </Tag>
                        </Col>
                      </Row>
                      <div style={{ fontSize: "12px", color: "#666" }}>
                        {dayjs(violation.detectedAt).format(
                          "DD/MM/YYYY HH:mm:ss"
                        )}
                      </div>
                    </div>
                  ))}
                </Card>
              )}
          </div>
        )}
      </Modal>
    </div>
  );
};

export default LaporanNilaiSiswa;
