/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from "react";
import { Card, Alert, Row, Col, Tag, Progress, Typography } from "antd";
import {
  SafetyOutlined,
  ExclamationCircleOutlined,
  ShieldOutlined,
  WarningOutlined,
} from "@ant-design/icons";

const { Text } = Typography;

/**
 * SecurityCard - Reusable component for displaying security analysis
 * Used across ujian analysis views for cheat detection and integrity
 */
const SecurityCard = ({
  integrityScore = 100,
  totalViolations = 0,
  flaggedParticipants = 0,
  suspiciousSubmissions = 0,
  cheatDetections = [],
  totalParticipants = 1,
  showDetailed = true,
  size = "default",
  style = {},
}) => {
  const getSecurityLevel = (score) => {
    if (score >= 90)
      return { level: "AMAN", type: "success", color: "#52c41a" };
    if (score >= 70)
      return { level: "PERLU PERHATIAN", type: "warning", color: "#faad14" };
    return { level: "RISIKO TINGGI", type: "error", color: "#ff4d4f" };
  };

  const security = getSecurityLevel(integrityScore);
  const cleanParticipants = Math.max(
    0,
    totalParticipants - flaggedParticipants
  );

  return (
    <Card
      title={
        <span>
          <ShieldOutlined style={{ marginRight: "8px" }} />
          Analisis Keamanan
        </span>
      }
      size={size}
      style={style}
    >
      {/* Security Status Alert */}
      <Alert
        message={`Status Keamanan: ${security.level}`}
        type={security.type}
        showIcon
        style={{ marginBottom: "16px" }}
      />

      {/* Security Metrics Grid */}
      <Row gutter={[8, 8]}>
        <Col xs={12} sm={6}>
          <div
            style={{
              textAlign: "center",
              padding: "12px 8px",
              backgroundColor: "#fff2f0",
              borderRadius: "6px",
              border: "1px solid #ffccc7",
            }}
          >
            <div
              style={{ fontSize: "18px", fontWeight: "bold", color: "#cf1322" }}
            >
              {totalViolations}
            </div>
            <div style={{ fontSize: "12px", color: "#666", marginTop: "4px" }}>
              Total Pelanggaran
            </div>
          </div>
        </Col>

        <Col xs={12} sm={6}>
          <div
            style={{
              textAlign: "center",
              padding: "12px 8px",
              backgroundColor: "#fff7e6",
              borderRadius: "6px",
              border: "1px solid #ffd591",
            }}
          >
            <div
              style={{ fontSize: "18px", fontWeight: "bold", color: "#d4691a" }}
            >
              {flaggedParticipants}
            </div>
            <div style={{ fontSize: "12px", color: "#666", marginTop: "4px" }}>
              Peserta Ter-flag
            </div>
          </div>
        </Col>

        <Col xs={12} sm={6}>
          <div
            style={{
              textAlign: "center",
              padding: "12px 8px",
              backgroundColor: "#f6ffed",
              borderRadius: "6px",
              border: "1px solid #b7eb8f",
            }}
          >
            <div
              style={{ fontSize: "18px", fontWeight: "bold", color: "#389e0d" }}
            >
              {cleanParticipants}
            </div>
            <div style={{ fontSize: "12px", color: "#666", marginTop: "4px" }}>
              Peserta Bersih
            </div>
          </div>
        </Col>

        <Col xs={12} sm={6}>
          <div
            style={{
              textAlign: "center",
              padding: "12px 8px",
              backgroundColor: "#f0f5ff",
              borderRadius: "6px",
              border: "1px solid #adc6ff",
            }}
          >
            <div
              style={{ fontSize: "18px", fontWeight: "bold", color: "#1d39c4" }}
            >
              {integrityScore.toFixed(1)}%
            </div>
            <div style={{ fontSize: "12px", color: "#666", marginTop: "4px" }}>
              Skor Integritas
            </div>
          </div>
        </Col>
      </Row>

      {/* Integrity Score Progress Bar */}
      <div style={{ marginTop: "16px" }}>
        <Text strong>Tingkat Keamanan:</Text>
        <Progress
          percent={integrityScore}
          strokeColor={{
            "0%": security.color,
            "100%": security.color,
          }}
          style={{ marginTop: "8px" }}
        />
      </div>

      {/* Detailed Security Breakdown */}
      {showDetailed && (
        <div style={{ marginTop: "16px" }}>
          <Row gutter={[16, 8]}>
            <Col span={12}>
              <Text>
                <strong>Submission Mencurigakan:</strong>
              </Text>
              <div>
                <Tag color={suspiciousSubmissions > 0 ? "red" : "green"}>
                  {suspiciousSubmissions} submission
                </Tag>
              </div>
            </Col>
            <Col span={12}>
              <Text>
                <strong>Tingkat Keamanan:</strong>
              </Text>
              <div>
                <Tag
                  color={
                    security.type === "success"
                      ? "green"
                      : security.type === "warning"
                      ? "orange"
                      : "red"
                  }
                >
                  {security.level}
                </Tag>
              </div>
            </Col>
          </Row>

          {/* Risk Assessment */}
          <div style={{ marginTop: "12px" }}>
            <Text strong>Penilaian Risiko:</Text>
            <div style={{ marginTop: "4px" }}>
              {integrityScore >= 95 && (
                <Tag color="green" icon={<SafetyOutlined />}>
                  Risiko Sangat Rendah - Ujian sangat aman
                </Tag>
              )}
              {integrityScore >= 80 && integrityScore < 95 && (
                <Tag color="blue" icon={<SafetyOutlined />}>
                  Risiko Rendah - Ujian cukup aman
                </Tag>
              )}
              {integrityScore >= 60 && integrityScore < 80 && (
                <Tag color="orange" icon={<WarningOutlined />}>
                  Risiko Sedang - Perlu monitoring tambahan
                </Tag>
              )}
              {integrityScore < 60 && (
                <Tag color="red" icon={<ExclamationCircleOutlined />}>
                  Risiko Tinggi - Perlu investigasi menyeluruh
                </Tag>
              )}
            </div>
          </div>
        </div>
      )}
    </Card>
  );
};

export default SecurityCard;
