/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from "react";
import {
  Card,
  Spin,
  Alert,
  Typography,
  Divider,
  Tag,
  Table,
  Button,
  Row,
  Col,
  Statistic,
  List,
  Progress,
  Space,
  Tabs,
  Badge,
} from "antd";
import {
  TrophyOutlined,
  SafetyOutlined,
  UserOutlined,
  BookOutlined,
  WarningOutlined,
  CheckCircleOutlined,
  BulbOutlined,
  BarChartOutlined,
  EyeOutlined,
} from "@ant-design/icons";
import { getAnalysisByUjian } from "@/api/ujianAnalysis";
import { getViolationsByUjian } from "@/api/cheatDetection";
import { getHasilByUjian } from "@/api/hasilUjian";
import { useParams, useNavigate } from "react-router-dom";

const { Title, Text } = Typography;
const { TabPane } = Tabs;

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

const DetailAnalysis = () => {
  const { idUjian } = useParams();
  const navigate = useNavigate();
  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(true);
  const [violations, setViolations] = useState([]);
  const [hasilUjian, setHasilUjian] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        console.log(
          `DetailAnalysis: Fetching analysis for ujian ID: ${idUjian}`
        );

        const res = await getAnalysisByUjian(idUjian);
        console.log("DetailAnalysis: API response:", res);

        if (res.data && res.data.content && res.data.content.length > 0) {
          // Sort by generatedAt to get the latest analysis
          const sortedAnalysis = res.data.content.sort((a, b) => {
            const dateA = new Date(a.generatedAt || 0);
            const dateB = new Date(b.generatedAt || 0);
            return dateB - dateA; // Latest first
          });

          const latestAnalysis = sortedAnalysis[0];
          console.log("DetailAnalysis: Using latest analysis:", latestAnalysis);
          setAnalysis(latestAnalysis);
        } else {
          console.log("DetailAnalysis: No analysis found in response");
          setAnalysis(null);
        }

        const vres = await getViolationsByUjian(idUjian);
        setViolations(vres.data?.content || []);
        const hres = await getHasilByUjian(idUjian, true);
        setHasilUjian(hres.data?.content || []);
      } catch (err) {
        console.error("DetailAnalysis: Error fetching data:", err);
        setAnalysis(null);
      } finally {
        setLoading(false);
      }
    };
    if (idUjian) fetchData();
  }, [idUjian]);
  if (loading) return <Spin tip="Memuat analisis..." />;

  if (!analysis) {
    return (
      <div style={{ maxWidth: 900, margin: "40px auto" }}>
        <Alert
          message="Analisis ujian belum tersedia"
          description={`Analisis untuk ujian ID ${idUjian} belum dibuat atau belum selesai diproses. Silakan periksa apakah ujian sudah selesai dan coba generate analisis melalui menu Analisis Ujian.`}
          type="info"
          showIcon
          action={
            <Button size="small" onClick={() => navigate(-1)}>
              Kembali
            </Button>
          }
        />
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 1200, margin: "20px auto" }}>
      <Card>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "24px",
          }}
        >
          <Title level={2} style={{ margin: 0 }}>
            📊 Analisis Ujian Komprehensif
          </Title>
          <Button onClick={() => navigate(-1)}>Kembali</Button>
        </div>

        {/* Key Metrics Overview */}
        <Card
          size="small"
          style={{ marginBottom: "24px", backgroundColor: "#fafafa" }}
        >
          <Row gutter={[16, 16]}>
            <Col xs={24} sm={6}>
              <Statistic
                title="Rata-rata Kelas"
                value={analysis.averageScore || 0}
                precision={2}
                prefix={<TrophyOutlined />}
                valueStyle={{
                  color:
                    analysis.averageScore >= 75
                      ? "#3f8600"
                      : analysis.averageScore >= 60
                      ? "#fa8c16"
                      : "#cf1322",
                }}
              />
            </Col>
            <Col xs={24} sm={6}>
              <Statistic
                title="Tingkat Kelulusan"
                value={analysis.passRate || 0}
                precision={1}
                suffix="%"
                prefix={<CheckCircleOutlined />}
                valueStyle={{
                  color:
                    analysis.passRate >= 80
                      ? "#3f8600"
                      : analysis.passRate >= 60
                      ? "#fa8c16"
                      : "#cf1322",
                }}
              />
            </Col>
            <Col xs={24} sm={6}>
              <Statistic
                title="Skor Integritas"
                value={
                  analysis.integrityScore ? analysis.integrityScore * 100 : 0
                }
                precision={1}
                suffix="%"
                prefix={<SafetyOutlined />}
                valueStyle={{
                  color:
                    analysis.integrityScore >= 0.9
                      ? "#3f8600"
                      : analysis.integrityScore >= 0.7
                      ? "#fa8c16"
                      : "#cf1322",
                }}
              />
            </Col>
            <Col xs={24} sm={6}>
              <Statistic
                title="Total Peserta"
                value={analysis.totalParticipants || hasilUjian.length || 0}
                prefix={<UserOutlined />}
              />
            </Col>
          </Row>
        </Card>

        {/* Tab Navigation for Different Analysis Views */}
        <Tabs defaultActiveKey="overview" type="card">
          {/* Overview Tab */}
          <TabPane
            tab={
              <span>
                <BarChartOutlined />
                Ringkasan
              </span>
            }
            key="overview"
          >
            <Row gutter={[16, 16]}>
              {/* Descriptive Statistics */}
              <Col xs={24} lg={12}>
                <Card title="📈 Statistik Deskriptif" size="small">
                  <Row gutter={[8, 8]}>
                    <Col span={12}>
                      <Text>
                        <strong>Median:</strong>{" "}
                        {analysis.medianScore?.toFixed(1) || "-"}
                      </Text>
                    </Col>
                    <Col span={12}>
                      <Text>
                        <strong>Std. Deviasi:</strong>{" "}
                        {analysis.standardDeviation?.toFixed(1) || "-"}
                      </Text>
                    </Col>
                    <Col span={12}>
                      <Text>
                        <strong>Varians:</strong>{" "}
                        {analysis.variance?.toFixed(1) || "-"}
                      </Text>
                    </Col>
                    <Col span={12}>
                      <Text>
                        <strong>Range:</strong>{" "}
                        {analysis.lowestScore?.toFixed(1) || 0} -{" "}
                        {analysis.highestScore?.toFixed(1) || 0}
                      </Text>
                    </Col>
                  </Row>

                  {/* Grade Distribution */}
                  {analysis.gradeDistribution && (
                    <div style={{ marginTop: "16px" }}>
                      <Text strong>Distribusi Nilai:</Text>
                      <div style={{ marginTop: "8px" }}>
                        {Object.entries(analysis.gradeDistribution).map(
                          ([grade, count]) => (
                            <div key={grade} style={{ marginBottom: "8px" }}>
                              <Row>
                                <Col span={4}>
                                  <Tag color={getGradeColor(grade)}>
                                    {grade}
                                  </Tag>
                                </Col>
                                <Col span={12}>
                                  <Progress
                                    percent={
                                      analysis.gradePercentages?.[grade] || 0
                                    }
                                    size="small"
                                    showInfo={false}
                                    strokeColor={getGradeColor(grade)}
                                  />
                                </Col>
                                <Col span={8}>
                                  <Text>
                                    {count} siswa (
                                    {analysis.gradePercentages?.[
                                      grade
                                    ]?.toFixed(1) || 0}
                                    %)
                                  </Text>
                                </Col>
                              </Row>
                            </div>
                          )
                        )}
                      </div>
                    </div>
                  )}
                </Card>
              </Col>

              {/* Performance by Class */}
              <Col xs={24} lg={12}>
                <Card title="🏫 Performa per Kelas" size="small">
                  {analysis.performanceByKelas &&
                  Object.keys(analysis.performanceByKelas).length > 0 ? (
                    Object.entries(analysis.performanceByKelas).map(
                      ([kelas, data]) => (
                        <Card
                          key={kelas}
                          size="small"
                          style={{ marginBottom: "8px" }}
                        >
                          <Text strong>Kelas {kelas}</Text>
                          <Row gutter={[8, 4]} style={{ marginTop: "4px" }}>
                            <Col span={12}>
                              <Text>
                                <small>
                                  Rata-rata:{" "}
                                  <strong>
                                    {data.averageScore?.toFixed(1) || 0}
                                  </strong>
                                </small>
                              </Text>
                            </Col>
                            <Col span={12}>
                              <Text>
                                <small>
                                  Kelulusan:{" "}
                                  <strong>
                                    {data.passRate?.toFixed(1) || 0}%
                                  </strong>
                                </small>
                              </Text>
                            </Col>
                            <Col span={12}>
                              <Text>
                                <small>
                                  Peserta:{" "}
                                  <strong>{data.participantCount || 0}</strong>
                                </small>
                              </Text>
                            </Col>
                            <Col span={12}>
                              <Text>
                                <small>
                                  Tertinggi:{" "}
                                  <strong>
                                    {data.highestScore?.toFixed(1) || 0}
                                  </strong>
                                </small>
                              </Text>
                            </Col>
                          </Row>
                        </Card>
                      )
                    )
                  ) : (
                    <Text type="secondary">
                      Data performa kelas tidak tersedia
                    </Text>
                  )}
                </Card>
              </Col>
            </Row>
          </TabPane>

          {/* Item Analysis Tab */}
          <TabPane
            tab={
              <span>
                <BookOutlined />
                Analisis Soal
              </span>
            }
            key="item-analysis"
          >
            <Row gutter={[16, 16]}>
              <Col xs={24} lg={12}>
                <Card title="✅ Soal Termudah" size="small">
                  {analysis.easiestQuestions &&
                  analysis.easiestQuestions.length > 0 ? (
                    <List
                      size="small"
                      dataSource={analysis.easiestQuestions.slice(0, 10)}
                      renderItem={(questionId, index) => (
                        <List.Item>
                          <Badge
                            count={index + 1}
                            style={{ backgroundColor: "#52c41a" }}
                          >
                            <Text>Soal #{questionId}</Text>
                          </Badge>
                          {analysis.questionDifficulty?.[questionId] && (
                            <Text
                              type="secondary"
                              style={{ marginLeft: "12px" }}
                            >
                              Tingkat Kesulitan:{" "}
                              {(
                                analysis.questionDifficulty[questionId] * 100
                              ).toFixed(1)}
                              %
                            </Text>
                          )}
                        </List.Item>
                      )}
                    />
                  ) : (
                    <Text type="secondary">
                      Data soal termudah tidak tersedia
                    </Text>
                  )}
                </Card>
              </Col>

              <Col xs={24} lg={12}>
                <Card title="❌ Soal Tersulit" size="small">
                  {analysis.hardestQuestions &&
                  analysis.hardestQuestions.length > 0 ? (
                    <List
                      size="small"
                      dataSource={analysis.hardestQuestions.slice(0, 10)}
                      renderItem={(questionId, index) => (
                        <List.Item>
                          <Badge
                            count={index + 1}
                            style={{ backgroundColor: "#ff4d4f" }}
                          >
                            <Text>Soal #{questionId}</Text>
                          </Badge>
                          {analysis.questionDifficulty?.[questionId] && (
                            <Text
                              type="secondary"
                              style={{ marginLeft: "12px" }}
                            >
                              Tingkat Kesulitan:{" "}
                              {(
                                analysis.questionDifficulty[questionId] * 100
                              ).toFixed(1)}
                              %
                            </Text>
                          )}
                        </List.Item>
                      )}
                    />
                  ) : (
                    <Text type="secondary">
                      Data soal tersulit tidak tersedia
                    </Text>
                  )}
                </Card>
              </Col>

              {/* Item Analysis Summary */}
              {analysis.itemAnalysis && (
                <Col span={24}>
                  <Card title="📊 Ringkasan Item Analysis" size="small">
                    <Row gutter={[16, 8]}>
                      <Col xs={24} sm={8}>
                        <Text>
                          <strong>Total Soal Dianalisis:</strong>{" "}
                          {Object.keys(analysis.itemAnalysis).length}
                        </Text>
                      </Col>
                      <Col xs={24} sm={8}>
                        <Text>
                          <strong>Rata-rata Tingkat Kesulitan:</strong>{" "}
                          {Object.values(analysis.questionDifficulty || {})
                            .length > 0
                            ? (
                                (Object.values(
                                  analysis.questionDifficulty
                                ).reduce((a, b) => a + b, 0) /
                                  Object.values(analysis.questionDifficulty)
                                    .length) *
                                100
                              ).toFixed(1) + "%"
                            : "N/A"}
                        </Text>
                      </Col>
                      <Col xs={24} sm={8}>
                        <Text>
                          <strong>Soal Bermasalah:</strong>{" "}
                          {
                            Object.values(
                              analysis.questionDifficulty || {}
                            ).filter((diff) => diff < 0.3 || diff > 0.9).length
                          }
                        </Text>
                      </Col>
                    </Row>
                  </Card>
                </Col>
              )}
            </Row>
          </TabPane>

          {/* Security Analysis Tab */}
          <TabPane
            tab={
              <span>
                <SafetyOutlined />
                Analisis Keamanan
              </span>
            }
            key="security"
          >
            <Row gutter={[16, 16]}>
              {/* Security Overview */}
              <Col span={24}>
                <Alert
                  message={
                    analysis.integrityScore >= 0.9
                      ? "Ujian Aman - Tidak ada indikasi kecurangan signifikan"
                      : analysis.integrityScore >= 0.7
                      ? "Perlu Perhatian - Ada beberapa aktivitas mencurigakan"
                      : "Risiko Tinggi - Terdeteksi banyak pelanggaran"
                  }
                  type={
                    analysis.integrityScore >= 0.9
                      ? "success"
                      : analysis.integrityScore >= 0.7
                      ? "warning"
                      : "error"
                  }
                  showIcon
                  style={{ marginBottom: "16px" }}
                />
              </Col>

              {/* Security Metrics */}
              <Col xs={24} lg={8}>
                <Card title="🚨 Deteksi Pelanggaran" size="small">
                  <Space direction="vertical" style={{ width: "100%" }}>
                    <div>
                      <Text>
                        <strong>Total Pelanggaran:</strong>
                      </Text>
                      <Tag color="red" style={{ float: "right" }}>
                        {analysis.suspiciousSubmissions || 0}
                      </Tag>
                    </div>
                    <div>
                      <Text>
                        <strong>Peserta Ter-flag:</strong>
                      </Text>
                      <Tag color="orange" style={{ float: "right" }}>
                        {analysis.flaggedParticipants || 0}
                      </Tag>
                    </div>
                    <div>
                      <Text>
                        <strong>Tingkat Keamanan:</strong>
                      </Text>
                      <Progress
                        percent={analysis.integrityScore * 100 || 0}
                        size="small"
                        strokeColor={
                          analysis.integrityScore >= 0.9
                            ? "#52c41a"
                            : analysis.integrityScore >= 0.7
                            ? "#faad14"
                            : "#ff4d4f"
                        }
                      />
                    </div>
                  </Space>
                </Card>
              </Col>

              {/* Cheat Detection Details */}
              <Col xs={24} lg={16}>
                <Card title="🔍 Detail Pelanggaran" size="small">
                  <Table
                    dataSource={violations}
                    rowKey="idDetection"
                    size="small"
                    columns={[
                      {
                        title: "Peserta",
                        dataIndex: "idPeserta",
                        key: "idPeserta",
                        width: 120,
                      },
                      {
                        title: "Jenis Pelanggaran",
                        dataIndex: "typeViolation",
                        key: "typeViolation",
                        render: (type) => (
                          <Tag color="red" style={{ fontSize: "11px" }}>
                            {type}
                          </Tag>
                        ),
                      },
                      {
                        title: "Severity",
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
                        render: (t) =>
                          t ? new Date(t).toLocaleString("id-ID") : "-",
                        width: 150,
                      },
                      {
                        title: "Detail",
                        dataIndex: "description",
                        key: "description",
                        ellipsis: true,
                      },
                    ]}
                    pagination={{ pageSize: 5 }}
                    locale={{ emptyText: "Tidak ada pelanggaran terdeteksi" }}
                  />
                </Card>
              </Col>
            </Row>
          </TabPane>

          {/* Recommendations Tab */}
          <TabPane
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
                  {analysis.recommendations &&
                  analysis.recommendations.length > 0 ? (
                    <List
                      size="small"
                      dataSource={analysis.recommendations}
                      renderItem={(rec, index) => (
                        <List.Item>
                          <CheckCircleOutlined
                            style={{ color: "#1890ff", marginRight: "8px" }}
                          />
                          <Text>{rec}</Text>
                        </List.Item>
                      )}
                    />
                  ) : (
                    <Text type="secondary">
                      Tidak ada rekomendasi khusus tersedia.
                    </Text>
                  )}
                </Card>
              </Col>

              <Col xs={24} lg={12}>
                <Card title="🔧 Saran Perbaikan" size="small">
                  {analysis.improvementSuggestions &&
                  analysis.improvementSuggestions.length > 0 ? (
                    <List
                      size="small"
                      dataSource={analysis.improvementSuggestions}
                      renderItem={(suggestion, index) => (
                        <List.Item>
                          <BulbOutlined
                            style={{ color: "#faad14", marginRight: "8px" }}
                          />
                          <Text>{suggestion}</Text>
                        </List.Item>
                      )}
                    />
                  ) : (
                    <Text type="secondary">
                      Tidak ada saran perbaikan khusus.
                    </Text>
                  )}
                </Card>
              </Col>
            </Row>
          </TabPane>

          {/* Participants Tab */}
          <TabPane
            tab={
              <span>
                <UserOutlined />
                Peserta
              </span>
            }
            key="participants"
          >
            <Card title="👥 Distribusi Nilai Peserta" size="small">
              <Table
                dataSource={hasilUjian}
                rowKey="idHasilUjian"
                size="small"
                columns={[
                  {
                    title: "ID Peserta",
                    dataIndex: "idPeserta",
                    key: "idPeserta",
                    sorter: (a, b) => a.idPeserta.localeCompare(b.idPeserta),
                  },
                  {
                    title: "Nilai",
                    dataIndex: "totalSkor",
                    key: "totalSkor",
                    sorter: (a, b) => a.totalSkor - b.totalSkor,
                    render: (score) => (
                      <Text
                        strong
                        style={{
                          color:
                            score >= 75
                              ? "#3f8600"
                              : score >= 60
                              ? "#fa8c16"
                              : "#cf1322",
                        }}
                      >
                        {score}
                      </Text>
                    ),
                  },
                  {
                    title: "Status",
                    dataIndex: "lulus",
                    key: "lulus",
                    filters: [
                      { text: "Lulus", value: true },
                      { text: "Tidak Lulus", value: false },
                    ],
                    onFilter: (value, record) => record.lulus === value,
                    render: (lulus) =>
                      lulus ? (
                        <Tag color="green" icon={<CheckCircleOutlined />}>
                          Lulus
                        </Tag>
                      ) : (
                        <Tag color="red" icon={<WarningOutlined />}>
                          Tidak Lulus
                        </Tag>
                      ),
                  },
                  {
                    title: "Pelanggaran",
                    dataIndex: "violationIds",
                    key: "violationIds",
                    render: (v) =>
                      v && v.length > 0 ? (
                        <Badge
                          count={v.length}
                          style={{ backgroundColor: "#ff4d4f" }}
                        >
                          <Tag color="red">Ada Pelanggaran</Tag>
                        </Badge>
                      ) : (
                        <Tag color="green" icon={<SafetyOutlined />}>
                          Bersih
                        </Tag>
                      ),
                  },
                  {
                    title: "Durasi",
                    dataIndex: "durationMinutes",
                    key: "durationMinutes",
                    render: (duration) =>
                      duration ? `${duration} menit` : "-",
                    sorter: (a, b) =>
                      (a.durationMinutes || 0) - (b.durationMinutes || 0),
                  },
                ]}
                pagination={{
                  pageSize: 10,
                  showTotal: (total, range) =>
                    `${range[0]}-${range[1]} dari ${total} peserta`,
                  showSizeChanger: true,
                }}
                scroll={{ x: true }}
              />
            </Card>
          </TabPane>
        </Tabs>

        {/* Metadata Footer */}
        <Card
          size="small"
          style={{ marginTop: "24px", backgroundColor: "#f9f9f9" }}
        >
          <Text type="secondary">
            <strong>Analisis dibuat:</strong>{" "}
            {analysis.generatedAt
              ? new Date(analysis.generatedAt).toLocaleString("id-ID")
              : "-"}{" "}
            |<strong> ID Analisis:</strong> {analysis.idUjianAnalysis} |
            <strong> Versi:</strong> {analysis.analysisVersion || "1.0"}
          </Text>
        </Card>
      </Card>
    </div>
  );
};

export default DetailAnalysis;
