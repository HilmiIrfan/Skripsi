/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from "react";
import { Card, Table, Tag, Divider, Spin, Alert, Button, message } from "antd";
import { getUjian } from "@/api/ujian";
import { getHasilByUjian } from "@/api/hasilUjian";
import { getViolationsByUjian } from "@/api/cheatDetection";
import { getUserById } from "@/api/user";
import { useNavigate } from "react-router-dom";

const IntegrasiPesertaDashboard = () => {
  const [loading, setLoading] = useState(true);
  const [ujians, setUjians] = useState([]);
  const [selectedUjian, setSelectedUjian] = useState(null);
  const [enrichedParticipants, setEnrichedParticipants] = useState([]);
  const [participantNames, setParticipantNames] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUjians = async () => {
      setLoading(true);
      try {
        const res = await getUjian();
        setUjians(res.data?.content || []);
      } catch (e) {
        setUjians([]);
      } finally {
        setLoading(false);
      }
    };
    fetchUjians();
  }, []);

  // Function to fetch participant names
  const fetchParticipantNames = async (participantIds) => {
    try {
      const names = {};
      const uniqueIds = [...new Set(participantIds)]; // Remove duplicates

      const promises = uniqueIds.map(async (id) => {
        try {
          const user = await getUserById(id);
          names[id] = user.data?.name || user.data?.username || id;
        } catch (error) {
          console.log(`Failed to fetch user ${id}:`, error);
          names[id] = id; // Fallback to ID
        }
      });

      await Promise.all(promises);
      setParticipantNames((prev) => ({ ...prev, ...names }));
    } catch (error) {
      console.error("Error fetching participant names:", error);
    }
  };

  const handleSelectUjian = async (ujian) => {
    setSelectedUjian(ujian);
    setLoading(true);
    try {
      console.log(`Loading participant data for ujian: ${ujian.idUjian}`);

      // Fetch hasil ujian (participant results)
      const hasilResponse = await getHasilByUjian(ujian.idUjian, true, 1000);
      const hasilData = hasilResponse.data?.content || [];

      console.log("Participant results count:", hasilData.length);

      // Enrich each participant with their violation data and analysis
      const enrichedParticipants = await Promise.all(
        hasilData.map(async (hasil) => {
          // Fetch violations for this participant
          let violations = [];
          try {
            const violationResponse = await getViolationsByUjian(ujian.idUjian);
            const allViolations = violationResponse.data?.content || [];
            violations = allViolations.filter(
              (v) => v.idPeserta === hasil.idPeserta
            );
          } catch (error) {
            console.error(
              `Error fetching violations for participant ${hasil.idPeserta}:`,
              error
            );
          }

          // Calculate behavioral metrics
          const totalViolations = violations.length;
          const severityScore = violations.reduce((sum, v) => {
            const severity = v.severityLevel || "LOW";
            return (
              sum + (severity === "HIGH" ? 3 : severity === "MEDIUM" ? 2 : 1)
            );
          }, 0);

          // Determine risk level
          let riskLevel = "LOW";
          if (totalViolations > 10) riskLevel = "HIGH";
          else if (totalViolations > 5) riskLevel = "MEDIUM";

          // Calculate behavior score
          const behaviorScore = Math.max(0, 100 - severityScore * 5);

          // Determine working pattern
          let workingPattern = "Normal";
          if (totalViolations > 10) workingPattern = "High Risk";
          else if (totalViolations > 5) workingPattern = "Suspicious";

          // Determine learning style based on response patterns
          let learningStyle = "Mixed";
          if (hasil.durasiPengerjaan) {
            const avgTimePerQuestion =
              hasil.durasiPengerjaan / (hasil.totalSoal || 1);
            if (avgTimePerQuestion < 30) learningStyle = "Quick";
            else if (avgTimePerQuestion > 120) learningStyle = "Careful";
          }

          return {
            ...hasil,
            violations,
            violationCount: totalViolations,
            severityScore,
            riskLevel,
            behaviorScore,
            workingPattern,
            learningStyle,
            needsReview: totalViolations > 3 || (hasil.persentase || 0) < 50,
            integrityScore: Math.max(0, 100 - totalViolations * 10),
          };
        })
      );

      // Fetch participant names
      const participantIds = enrichedParticipants.map((p) => p.idPeserta);
      if (participantIds.length > 0) {
        await fetchParticipantNames(participantIds);
      }

      setEnrichedParticipants(enrichedParticipants);

      console.log("Enriched participants:", enrichedParticipants.length);
    } catch (e) {
      console.error("Error in handleSelectUjian:", e);
      setEnrichedParticipants([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 1200, margin: "40px auto" }}>
      <Card title="Integrasi Analisis Peserta, Hasil, dan Cheat Detection">
        <Divider>Daftar Ujian</Divider>
        <Table
          dataSource={ujians}
          rowKey="idUjian"
          loading={loading}
          size="small"
          onRow={(record) => ({
            onClick: () => handleSelectUjian(record),
            style: { cursor: "pointer" },
          })}
          columns={[
            {
              title: "Nama Ujian",
              dataIndex: "namaUjian",
              key: "namaUjian",
              render: (text, record) => (
                <div>
                  <div style={{ fontWeight: "bold" }}>{text}</div>
                  <div style={{ fontSize: "12px", color: "#666" }}>
                    ID: {record.idUjian}
                  </div>
                </div>
              ),
            },
            {
              title: "Status",
              dataIndex: "status",
              key: "status",
              render: (status) => (
                <Tag color={status === "ACTIVE" ? "green" : "red"}>
                  {status || "INACTIVE"}
                </Tag>
              ),
            },
          ]}
          pagination={{ pageSize: 8 }}
        />

        <Divider>Detail Analisis Peserta</Divider>
        {loading && selectedUjian && <Spin tip="Memuat data peserta..." />}

        {!loading && selectedUjian && (
          <>
            {/* Summary Statistics - Participant Based */}
            <Card
              type="inner"
              title="Ringkasan Data Peserta"
              style={{ marginBottom: 16 }}
            >
              <div style={{ display: "flex", gap: "24px", flexWrap: "wrap" }}>
                <div style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "24px",
                      fontWeight: "bold",
                      color: "#1890ff",
                    }}
                  >
                    {enrichedParticipants.length}
                  </div>
                  <div style={{ color: "#666" }}>Total Peserta</div>
                </div>
                <div style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "24px",
                      fontWeight: "bold",
                      color: "#52c41a",
                    }}
                  >
                    {enrichedParticipants.filter((p) => p.lulus).length}
                  </div>
                  <div style={{ color: "#666" }}>Lulus</div>
                </div>
                <div style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "24px",
                      fontWeight: "bold",
                      color: "#ff4d4f",
                    }}
                  >
                    {enrichedParticipants.filter((p) => !p.lulus).length}
                  </div>
                  <div style={{ color: "#666" }}>Tidak Lulus</div>
                </div>
                <div style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "24px",
                      fontWeight: "bold",
                      color: "#fa8c16",
                    }}
                  >
                    {enrichedParticipants.reduce(
                      (sum, p) => sum + p.violationCount,
                      0
                    )}
                  </div>
                  <div style={{ color: "#666" }}>Total Pelanggaran</div>
                </div>
                <div style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "24px",
                      fontWeight: "bold",
                      color: "#722ed1",
                    }}
                  >
                    {
                      enrichedParticipants.filter((p) => p.violationCount > 0)
                        .length
                    }
                  </div>
                  <div style={{ color: "#666" }}>Peserta Bermasalah</div>
                </div>
                <div style={{ textAlign: "center" }}>
                  <div
                    style={{
                      fontSize: "24px",
                      fontWeight: "bold",
                      color: "#ff4d4f",
                    }}
                  >
                    {
                      enrichedParticipants.filter((p) => p.riskLevel === "HIGH")
                        .length
                    }
                  </div>
                  <div style={{ color: "#666" }}>Risiko Tinggi</div>
                </div>
                {enrichedParticipants.length > 0 && (
                  <div style={{ textAlign: "center" }}>
                    <div
                      style={{
                        fontSize: "24px",
                        fontWeight: "bold",
                        color: "#13c2c2",
                      }}
                    >
                      {(
                        enrichedParticipants.reduce(
                          (sum, p) => sum + (p.totalSkor || 0),
                          0
                        ) / enrichedParticipants.length
                      ).toFixed(1)}
                    </div>
                    <div style={{ color: "#666" }}>Rata-rata Nilai</div>
                  </div>
                )}
                {enrichedParticipants.length > 0 && (
                  <div style={{ textAlign: "center" }}>
                    <div
                      style={{
                        fontSize: "24px",
                        fontWeight: "bold",
                        color: "#52c41a",
                      }}
                    >
                      {(
                        enrichedParticipants.reduce(
                          (sum, p) => sum + (p.behaviorScore || 0),
                          0
                        ) / enrichedParticipants.length
                      ).toFixed(1)}
                    </div>
                    <div style={{ color: "#666" }}>Avg Behavior Score</div>
                  </div>
                )}
              </div>
            </Card>

            {/* Participant Analysis Section */}
            <Card
              type="inner"
              title={`Analisis Peserta: ${selectedUjian.namaUjian}`}
              style={{ marginBottom: 16 }}
            >
              {enrichedParticipants.length > 0 ? (
                <div style={{ display: "flex", gap: "16px", flexWrap: "wrap" }}>
                  <div style={{ textAlign: "center", padding: "8px" }}>
                    <div style={{ fontSize: "16px", fontWeight: "bold" }}>
                      {
                        enrichedParticipants.filter(
                          (p) => p.riskLevel === "HIGH"
                        ).length
                      }
                    </div>
                    <div style={{ color: "#ff4d4f", fontSize: "12px" }}>
                      Risiko Tinggi
                    </div>
                  </div>
                  <div style={{ textAlign: "center", padding: "8px" }}>
                    <div style={{ fontSize: "16px", fontWeight: "bold" }}>
                      {enrichedParticipants.filter((p) => p.needsReview).length}
                    </div>
                    <div style={{ color: "#fa8c16", fontSize: "12px" }}>
                      Perlu Review
                    </div>
                  </div>
                  <div style={{ textAlign: "center", padding: "8px" }}>
                    <div style={{ fontSize: "16px", fontWeight: "bold" }}>
                      {enrichedParticipants.length > 0
                        ? (
                            enrichedParticipants.reduce(
                              (sum, p) => sum + p.integrityScore,
                              0
                            ) / enrichedParticipants.length
                          ).toFixed(1)
                        : 0}
                    </div>
                    <div style={{ color: "#52c41a", fontSize: "12px" }}>
                      Avg Integrity
                    </div>
                  </div>
                  <div style={{ textAlign: "center", padding: "8px" }}>
                    <div style={{ fontSize: "16px", fontWeight: "bold" }}>
                      {enrichedParticipants.length > 0
                        ? (
                            (enrichedParticipants.filter((p) => p.lulus)
                              .length /
                              enrichedParticipants.length) *
                            100
                          ).toFixed(1)
                        : 0}
                      %
                    </div>
                    <div style={{ color: "#1890ff", fontSize: "12px" }}>
                      Pass Rate
                    </div>
                  </div>
                </div>
              ) : (
                <Alert
                  message="Belum ada data peserta"
                  description="Belum ada peserta yang mengerjakan ujian ini."
                  type="info"
                  showIcon
                />
              )}
            </Card>

            <Divider>Detail Peserta dan Pelanggaran</Divider>
            {enrichedParticipants.length > 0 ? (
              <Table
                dataSource={enrichedParticipants}
                rowKey={(record) => `${record.idPeserta}-${record.idUjian}`}
                size="small"
                scroll={{ x: 1200 }}
                rowClassName={(record) => {
                  if (record.riskLevel === "HIGH") return "high-risk-row";
                  if (record.needsReview) return "needs-review-row";
                  return "";
                }}
                columns={[
                  {
                    title: "Peserta",
                    dataIndex: "idPeserta",
                    key: "idPeserta",
                    render: (idPeserta, record) => (
                      <div>
                        <div style={{ fontWeight: "bold" }}>
                          {participantNames[idPeserta] ||
                            `Peserta ${idPeserta}`}
                        </div>
                        <div style={{ fontSize: "12px", color: "#666" }}>
                          ID: {idPeserta}
                        </div>
                      </div>
                    ),
                  },
                  {
                    title: "Nilai",
                    dataIndex: "totalSkor",
                    key: "totalSkor",
                    render: (score, record) => (
                      <div style={{ textAlign: "center" }}>
                        <div
                          style={{
                            fontWeight: "bold",
                            color:
                              score >= 80
                                ? "#52c41a"
                                : score >= 60
                                ? "#fa8c16"
                                : "#ff4d4f",
                          }}
                        >
                          {score || 0}
                        </div>
                        <div style={{ fontSize: "12px", color: "#666" }}>
                          {record.persentase?.toFixed(1) || 0}%
                        </div>
                      </div>
                    ),
                    sorter: (a, b) => (a.totalSkor || 0) - (b.totalSkor || 0),
                  },
                  {
                    title: "Status",
                    dataIndex: "lulus",
                    key: "lulus",
                    render: (lulus) => (
                      <Tag color={lulus ? "green" : "red"}>
                        {lulus ? "LULUS" : "TIDAK LULUS"}
                      </Tag>
                    ),
                    filters: [
                      { text: "Lulus", value: true },
                      { text: "Tidak Lulus", value: false },
                    ],
                    onFilter: (value, record) => record.lulus === value,
                  },
                  {
                    title: "Pelanggaran",
                    dataIndex: "violationCount",
                    key: "violationCount",
                    render: (count, record) => (
                      <div style={{ textAlign: "center" }}>
                        <Tag
                          color={
                            count > 10
                              ? "red"
                              : count > 5
                              ? "orange"
                              : count > 0
                              ? "yellow"
                              : "green"
                          }
                        >
                          {count} pelanggaran
                        </Tag>
                        <div style={{ fontSize: "12px", color: "#666" }}>
                          Severity: {record.severityScore}
                        </div>
                      </div>
                    ),
                    sorter: (a, b) =>
                      (a.violationCount || 0) - (b.violationCount || 0),
                  },
                  {
                    title: "Risk Level",
                    dataIndex: "riskLevel",
                    key: "riskLevel",
                    render: (level) => (
                      <Tag
                        color={
                          level === "HIGH"
                            ? "red"
                            : level === "MEDIUM"
                            ? "orange"
                            : "green"
                        }
                      >
                        {level}
                      </Tag>
                    ),
                    filters: [
                      { text: "High Risk", value: "HIGH" },
                      { text: "Medium Risk", value: "MEDIUM" },
                      { text: "Low Risk", value: "LOW" },
                    ],
                    onFilter: (value, record) => record.riskLevel === value,
                  },
                  {
                    title: "Working Pattern",
                    dataIndex: "workingPattern",
                    key: "workingPattern",
                    render: (pattern) => (
                      <Tag
                        color={
                          pattern === "High Risk"
                            ? "red"
                            : pattern === "Suspicious"
                            ? "orange"
                            : "blue"
                        }
                      >
                        {pattern}
                      </Tag>
                    ),
                  },
                  {
                    title: "Learning Style",
                    dataIndex: "learningStyle",
                    key: "learningStyle",
                    render: (style) => (
                      <Tag
                        color={
                          style === "Quick"
                            ? "blue"
                            : style === "Careful"
                            ? "green"
                            : "purple"
                        }
                      >
                        {style}
                      </Tag>
                    ),
                  },
                  {
                    title: "Behavior Score",
                    dataIndex: "behaviorScore",
                    key: "behaviorScore",
                    render: (score) => (
                      <div style={{ textAlign: "center" }}>
                        <div
                          style={{
                            fontWeight: "bold",
                            color:
                              score >= 80
                                ? "#52c41a"
                                : score >= 60
                                ? "#fa8c16"
                                : "#ff4d4f",
                          }}
                        >
                          {score || 0}
                        </div>
                        <div style={{ fontSize: "10px", color: "#666" }}>
                          /100
                        </div>
                      </div>
                    ),
                    sorter: (a, b) =>
                      (a.behaviorScore || 0) - (b.behaviorScore || 0),
                  },
                  {
                    title: "Integrity Score",
                    dataIndex: "integrityScore",
                    key: "integrityScore",
                    render: (score) => (
                      <div style={{ textAlign: "center" }}>
                        <div
                          style={{
                            fontWeight: "bold",
                            color:
                              score >= 80
                                ? "#52c41a"
                                : score >= 60
                                ? "#fa8c16"
                                : "#ff4d4f",
                          }}
                        >
                          {score || 0}
                        </div>
                        <div style={{ fontSize: "10px", color: "#666" }}>
                          /100
                        </div>
                      </div>
                    ),
                    sorter: (a, b) =>
                      (a.integrityScore || 0) - (b.integrityScore || 0),
                  },
                  {
                    title: "Review Status",
                    dataIndex: "needsReview",
                    key: "needsReview",
                    render: (needsReview) => (
                      <Tag color={needsReview ? "red" : "green"}>
                        {needsReview ? "PERLU REVIEW" : "NORMAL"}
                      </Tag>
                    ),
                    filters: [
                      { text: "Perlu Review", value: true },
                      { text: "Normal", value: false },
                    ],
                    onFilter: (value, record) => record.needsReview === value,
                  },
                  {
                    title: "Aksi",
                    key: "action",
                    render: (_, record) => (
                      <Button
                        type="link"
                        size="small"
                        onClick={() => {
                          // Navigate to participant detail
                          navigate(
                            `/peserta-analysis/detail/${record.idPeserta}/${record.idUjian}`
                          );
                        }}
                      >
                        Detail
                      </Button>
                    ),
                  },
                ]}
                pagination={{
                  pageSize: 10,
                  showSizeChanger: true,
                  showQuickJumper: true,
                  showTotal: (total, range) =>
                    `${range[0]}-${range[1]} dari ${total} peserta`,
                }}
              />
            ) : (
              <Alert
                message="Belum ada data peserta"
                description="Belum ada peserta yang mengerjakan ujian ini."
                type="info"
                showIcon
              />
            )}

            {/* Violation Summary */}
            {enrichedParticipants.length > 0 && (
              <div style={{ marginTop: "24px" }}>
                <Card title="Rangkuman Pelanggaran per Peserta" size="small">
                  {enrichedParticipants
                    .filter((p) => p.violationCount > 0)
                    .map((participant) => (
                      <div
                        key={participant.idPeserta}
                        style={{
                          marginBottom: "8px",
                          padding: "8px",
                          border: "1px solid #ddd",
                          borderRadius: "4px",
                          backgroundColor:
                            participant.riskLevel === "HIGH"
                              ? "#fff2f0"
                              : participant.riskLevel === "MEDIUM"
                              ? "#fff7e6"
                              : "#f6ffed",
                        }}
                      >
                        <strong>
                          {participantNames[participant.idPeserta] ||
                            participant.idPeserta}
                        </strong>{" "}
                        - {participant.violationCount} violations (Risk:{" "}
                        {participant.riskLevel})
                        <div>
                          {participant.violations &&
                            participant.violations.map((violation, index) => (
                              <Tag
                                key={index}
                                style={{ margin: "2px" }}
                                color={
                                  violation.severityLevel === "HIGH"
                                    ? "red"
                                    : violation.severityLevel === "MEDIUM"
                                    ? "orange"
                                    : "yellow"
                                }
                              >
                                {violation.violationType}:{" "}
                                {violation.severityLevel}
                              </Tag>
                            ))}
                        </div>
                      </div>
                    ))}
                </Card>
              </div>
            )}
          </>
        )}

        {!selectedUjian && (
          <Alert
            message="Pilih ujian untuk melihat analisis peserta"
            description="Klik pada salah satu ujian di tabel di atas untuk melihat detail analisis peserta."
            type="info"
            showIcon
          />
        )}
      </Card>{" "}
      <style>{`
        .high-risk-row {
          background-color: #fff2f0;
        }
        .needs-review-row {
          background-color: #fff7e6;
        }
        .ant-table-row:hover.high-risk-row {
          background-color: #ffede7;
        }
        .ant-table-row:hover.needs-review-row {
          background-color: #fff1db;
        }
      `}</style>
    </div>
  );
};

export default IntegrasiPesertaDashboard;
