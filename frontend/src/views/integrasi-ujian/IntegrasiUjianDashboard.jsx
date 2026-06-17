/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from "react";
import { Card, Table, Tag, Divider, Spin, Alert, Button, message } from "antd";
import { getUjian } from "@/api/ujian";
import { getHasilByUjian } from "@/api/hasilUjian";
import { getViolationsByUjian } from "@/api/cheatDetection";
import { getUserById } from "@/api/user";
import {
  getAnalysisByUjian,
  autoGenerateAnalysisForUjian,
  forceRegenerateAnalysis,
  cleanupDuplicatesForUjian,
  cleanupAllDuplicates,
} from "@/api/ujianAnalysis";
import { useNavigate } from "react-router-dom";

const IntegrasiUjianDashboard = () => {
  const [loading, setLoading] = useState(true);
  const [ujians, setUjians] = useState([]);
  const [selectedUjian, setSelectedUjian] = useState(null);
  const [pesertaData, setPesertaData] = useState([]);
  const [enrichedParticipants, setEnrichedParticipants] = useState([]);
  const [participantNames, setParticipantNames] = useState({});
  const [analysis, setAnalysis] = useState(null);
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

          return {
            ...hasil,
            violations,
            violationCount: totalViolations,
            severityScore,
            riskLevel,
            behaviorScore,
            workingPattern,
            needsReview: totalViolations > 3 || (hasil.persentase || 0) < 50,
            integrityScore: Math.max(0, 100 - totalViolations * 10),
          };
        })
      );

      // Fetch participant names
      const participantIds = enrichedParticipants.map((p) => p.idPeserta);
      if (participantIds.length > 0) {
        await fetchParticipantNames(participantIds);
      }      setEnrichedParticipants(enrichedParticipants);
      setPesertaData(hasilData);

      // Generate analysis from enriched participants
      const totalParticipants = enrichedParticipants.length;
      const passedParticipants = enrichedParticipants.filter(p => p.lulus).length;
      const totalViolations = enrichedParticipants.reduce((sum, p) => sum + p.violationCount, 0);
      const flaggedParticipants = enrichedParticipants.filter(p => p.violationCount > 0).length;
      const averageScore = totalParticipants > 0 ? 
        enrichedParticipants.reduce((sum, p) => sum + (p.totalSkor || 0), 0) / totalParticipants : 0;
      const averageIntegrityScore = totalParticipants > 0 ?
        enrichedParticipants.reduce((sum, p) => sum + (p.integrityScore || 0), 0) / totalParticipants : 0;

      const analysisData = {
        idAnalysis: `generated-${ujian.idUjian}`,
        generatedAt: new Date().toISOString(),
        averageScore: averageScore,
        passRate: totalParticipants > 0 ? (passedParticipants / totalParticipants) * 100 : 0,
        integrityScore: averageIntegrityScore,
        suspiciousSubmissions: totalViolations,
        flaggedParticipants: flaggedParticipants
      };

      setAnalysis(analysisData);

      console.log("Enriched participants:", enrichedParticipants.length);    } catch (e) {
      console.error("Error in handleSelectUjian:", e);
      setEnrichedParticipants([]);
      setPesertaData([]);
      setAnalysis(null);
    } finally {
      setLoading(false);
    }
  };

  const handleCleanupDuplicates = async (ujianId) => {
    try {
      const result = await cleanupDuplicatesForUjian(ujianId);
      message.success(
        `Berhasil membersihkan ${result.data.cleanedCount} analisis duplikat`
      ); // Refresh data after cleanup
      if (selectedUjian && selectedUjian.idUjian === ujianId) {
        await handleSelectUjian(selectedUjian);
      }
    } catch (error) {
      console.error("Failed to cleanup duplicates:", error);
      message.error("Gagal membersihkan analisis duplikat");
    }
  };

  const handleCleanupAllDuplicates = async () => {
    try {
      const result = await cleanupAllDuplicates();
      message.success(
        `Berhasil membersihkan ${result.data.totalCleaned} analisis duplikat dari sistem`
      );
      // Refresh current ujian data if any selected
      if (selectedUjian) {
        await handleSelectUjian(selectedUjian);
      }
    } catch (error) {
      console.error("Failed to cleanup all duplicates:", error);
      message.error("Gagal membersihkan semua analisis duplikat");
    }
  };

  return (
    <div style={{ maxWidth: 1100, margin: "40px auto" }}>
      <Card title="Integrasi Ujian, Hasil, Cheat Detection, dan Analisis">
        <Divider>Daftar Ujian</Divider>
        <Table
          dataSource={ujians}
          rowKey="idUjian"
          size="small"
          columns={[
            { title: "Nama Ujian", dataIndex: "namaUjian", key: "namaUjian" },
            {
              title: "Kode",
              dataIndex: ["pengaturan", "kodeUjian"],
              key: "kodeUjian",
              render: (_, r) => r.pengaturan?.kodeUjian || "-",
            },
            {
              title: "Status",
              dataIndex: "statusUjian",
              key: "statusUjian",
              render: (s) => <Tag>{s}</Tag>,
            },
            {
              title: "Aksi",
              key: "aksi",
              render: (_, r) => (
                <Button onClick={() => handleSelectUjian(r)}>
                  Lihat Integrasi
                </Button>
              ),
            },
          ]}
          pagination={{ pageSize: 8 }}
        />
        <Divider>Detail Integrasi</Divider>
        {loading && selectedUjian && <Spin tip="Memuat data..." />}{" "}
        {!loading && selectedUjian && (
          <>
            {" "}
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
            <Card
              type="inner"
              title={`Analisis Ujian: ${selectedUjian.namaUjian}`}
              extra={
                <div>
                  {" "}
                  <Button
                    onClick={() =>
                      handleCleanupDuplicates(selectedUjian.idUjian)
                    }
                    size="small"
                    style={{ marginRight: 8 }}
                  >
                    Cleanup Duplikat
                  </Button>{" "}
                  <Button
                    onClick={async () => {
                      try {
                        await forceRegenerateAnalysis(selectedUjian.idUjian);
                        message.success("Analisis berhasil di-regenerate");
                        await handleSelectUjian(selectedUjian);
                      } catch (error) {
                        message.error(
                          "Gagal regenerate analisis: " + error.message
                        );
                      }
                    }}
                    size="small"
                    type="primary"
                    style={{ marginRight: 8 }}
                  >
                    Regenerate
                  </Button>
                  <Button
                    onClick={handleCleanupAllDuplicates}
                    size="small"
                    type="dashed"
                  >
                    Cleanup Semua
                  </Button>
                </div>
              }
            >
              {" "}
              {analysis ? (
                <>
                  <div
                    style={{
                      marginBottom: "10px",
                      fontSize: "12px",
                      color: "#666",
                    }}
                  >
                    Debug: Analysis ID: {analysis.idAnalysis} | Generated:{" "}
                    {analysis.generatedAt}
                  </div>
                  <p>
                    <b>Rata-rata Nilai:</b>{" "}
                    {analysis.averageScore?.toFixed(2) || "-"}
                  </p>
                  <p>
                    <b>Pass Rate:</b> {analysis.passRate?.toFixed(2) || "-"}%
                  </p>
                  <p>
                    <b>Integrity Score:</b>{" "}
                    {analysis.integrityScore?.toFixed(2) || "-"}
                  </p>
                  <p>
                    <b>Jumlah Pelanggaran:</b>{" "}
                    {analysis.suspiciousSubmissions || 0}
                  </p>
                  <p>
                    <b>Peserta Ter-flag:</b> {analysis.flaggedParticipants || 0}
                  </p>
                </>
              ) : (
                <Alert
                  message="Analisis belum tersedia"
                  description={
                    <div>
                      <p>
                        Analisis untuk ujian ini belum dibuat atau belum selesai
                        diproses.
                      </p>                      <p>
                        Debug info: Hasil ujian: {pesertaData.length}, Ujian ID:{" "}
                        {selectedUjian.idUjian}
                      </p>
                      {pesertaData.length > 0 ? (
                        <p>
                          Ada {pesertaData.length} hasil ujian. Analisis bisa
                          di-generate secara otomatis.
                        </p>
                      ) : (
                        <p>Belum ada hasil ujian untuk dianalisis.</p>
                      )}
                    </div>
                  }
                  type="info"
                  showIcon
                />
              )}
            </Card>{" "}            <Divider>Pelanggaran (Cheat Detection)</Divider>
            {(() => {
              const allViolations = enrichedParticipants.flatMap(p => p.violations || []);
              return allViolations.length > 0 ? (
                <Table
                  dataSource={allViolations}
                  rowKey="idDetection"
                  size="small"
                columns={[
                  {
                    title: "Peserta",
                    dataIndex: "idPeserta",
                    key: "idPeserta",
                    render: (idPeserta) => (
                      <div>
                        <div style={{ fontWeight: "bold" }}>
                          {participantNames[idPeserta] || idPeserta}
                        </div>
                        {participantNames[idPeserta] && (
                          <div style={{ fontSize: "12px", color: "#666" }}>
                            ID: {idPeserta}
                          </div>
                        )}
                      </div>
                    ),
                  },
                  {
                    title: "Jenis Pelanggaran",
                    dataIndex: "typeViolation",
                    key: "typeViolation",
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
                            : "blue"
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
                    render: (t) => (t ? new Date(t).toLocaleString() : "-"),
                  },
                  {
                    title: "Details",
                    dataIndex: "details",
                    key: "details",
                    render: (details) => (
                      <div style={{ fontSize: "12px", color: "#666" }}>
                        {details || "No additional details"}
                      </div>
                    ),
                  },                ]}
                pagination={{ pageSize: 5 }}
              />
            ) : (
              <div
                style={{ textAlign: "center", padding: "20px", color: "#666" }}
              >
                <Tag color="green">Tidak ada pelanggaran terdeteksi</Tag>
              </div>
            );
            })()}{" "}            <Divider>Distribusi Nilai Peserta</Divider>
            {enrichedParticipants.length > 0 ? (
              <Table
                dataSource={enrichedParticipants}
                rowKey="idHasilUjian"
                size="small"
                columns={[
                  {
                    title: "Peserta",
                    dataIndex: "idPeserta",
                    key: "idPeserta",
                    render: (idPeserta) => (
                      <div>
                        <div style={{ fontWeight: "bold" }}>
                          {participantNames[idPeserta] || idPeserta}
                        </div>
                        {participantNames[idPeserta] && (
                          <div style={{ fontSize: "12px", color: "#666" }}>
                            ID: {idPeserta}
                          </div>
                        )}
                      </div>
                    ),
                  },
                  {
                    title: "Nilai",
                    dataIndex: "totalSkor",
                    key: "totalSkor",
                    render: (score, record) => (
                      <div>
                        <div style={{ fontWeight: "bold", fontSize: "16px" }}>
                          {parseFloat(score || 0).toFixed(1)}
                        </div>
                        <div style={{ fontSize: "12px", color: "#666" }}>
                          {record.persentase
                            ? `${parseFloat(record.persentase).toFixed(1)}%`
                            : "0%"}
                        </div>
                        <div style={{ fontSize: "12px", color: "#666" }}>
                          Grade: {record.nilaiHuruf || "-"}
                        </div>
                      </div>
                    ),
                  },
                  {
                    title: "Jawaban",
                    key: "jawaban",
                    render: (_, record) => (
                      <div style={{ fontSize: "12px" }}>
                        <div style={{ color: "#52c41a" }}>
                          ✓ Benar: {record.jumlahBenar || 0}
                        </div>
                        <div style={{ color: "#ff4d4f" }}>
                          ✗ Salah: {record.jumlahSalah || 0}
                        </div>
                        <div style={{ color: "#faad14" }}>
                          ○ Kosong: {record.jumlahKosong || 0}
                        </div>
                      </div>
                    ),
                  },
                  {
                    title: "Waktu Pengerjaan",
                    key: "waktu",
                    render: (_, record) => (
                      <div style={{ fontSize: "12px" }}>
                        <div>
                          Mulai:{" "}
                          {record.waktuMulai
                            ? new Date(record.waktuMulai).toLocaleString()
                            : "-"}
                        </div>
                        <div>
                          Selesai:{" "}
                          {record.waktuSelesai
                            ? new Date(record.waktuSelesai).toLocaleString()
                            : "-"}
                        </div>
                        <div style={{ color: "#666" }}>
                          Durasi:{" "}
                          {record.durasiPengerjaan
                            ? `${Math.floor(record.durasiPengerjaan / 60)}m ${
                                record.durasiPengerjaan % 60
                              }s`
                            : "-"}
                        </div>
                      </div>
                    ),
                  },
                  {
                    title: "Status",
                    dataIndex: "lulus",
                    key: "lulus",
                    render: (lulus, record) => (
                      <div>
                        <Tag color={lulus ? "green" : "red"}>
                          {lulus ? "Lulus" : "Tidak Lulus"}
                        </Tag>
                        <div style={{ fontSize: "12px", color: "#666" }}>
                          {record.statusPengerjaan || "SELESAI"}
                        </div>
                      </div>
                    ),
                  },                  {
                    title: "Pelanggaran",
                    key: "violations",
                    render: (_, record) => {
                      const participantViolations = record.violations || [];
                      return participantViolations.length > 0 ? (
                        <Tag color="red">
                          {participantViolations.length} pelanggaran
                        </Tag>
                      ) : (
                        <Tag color="green">Bersih</Tag>
                      );
                    },
                  },
                ]}
                pagination={{ pageSize: 10 }}
              />
            ) : (
              <div
                style={{ textAlign: "center", padding: "20px", color: "#666" }}
              >
                <Tag color="orange">
                  Belum ada peserta yang mengerjakan ujian ini
                </Tag>
              </div>
            )}{" "}            <Divider>Rekap Pelanggaran per Peserta</Divider>
            {(() => {
              const allViolations = enrichedParticipants.flatMap(p => p.violations || []);
              return allViolations.length > 0 ? (
                <Table
                  dataSource={(() => {
                    // Grouping: { idPeserta: { typeViolation: count } }
                    const map = {};
                    allViolations.forEach((v) => {
                      if (!map[v.idPeserta]) map[v.idPeserta] = {};
                      if (!map[v.idPeserta][v.typeViolation])
                        map[v.idPeserta][v.typeViolation] = 0;
                      map[v.idPeserta][v.typeViolation]++;
                    });
                  // Flatten to array
                  const rows = [];
                  Object.entries(map).forEach(([idPeserta, types]) => {
                    Object.entries(types).forEach(([typeViolation, count]) => {
                      rows.push({ idPeserta, typeViolation, count });
                    });
                  });
                  return rows;
                })()}
                rowKey={(r) => r.idPeserta + r.typeViolation}
                size="small"
                columns={[
                  {
                    title: "Peserta",
                    dataIndex: "idPeserta",
                    key: "idPeserta",
                    render: (idPeserta) => (
                      <div>
                        <div style={{ fontWeight: "bold" }}>
                          {participantNames[idPeserta] || idPeserta}
                        </div>
                        {participantNames[idPeserta] && (
                          <div style={{ fontSize: "12px", color: "#666" }}>
                            ID: {idPeserta}
                          </div>
                        )}
                      </div>
                    ),
                  },
                  {
                    title: "Jenis Pelanggaran",
                    dataIndex: "typeViolation",
                    key: "typeViolation",
                  },
                  {
                    title: "Jumlah",
                    dataIndex: "count",
                    key: "count",
                    render: (count) => (
                      <Tag
                        color={
                          count > 5 ? "red" : count > 2 ? "orange" : "blue"
                        }
                      >
                        {count}
                      </Tag>
                    ),
                  },                ]}
                pagination={{ pageSize: 10 }}
              />
            ) : (
              <div
                style={{ textAlign: "center", padding: "20px", color: "#666" }}
              >
                <Tag color="green">Tidak ada pelanggaran untuk direkap</Tag>
              </div>
            );
            })()}
          </>
        )}
      </Card>
    </div>
  );
};

export default IntegrasiUjianDashboard;
