/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from "react";
import { Card, List, Tag, Typography, Row, Col, Progress, Badge } from "antd";
import {
  BookOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  QuestionCircleOutlined,
  BarChartOutlined,
} from "@ant-design/icons";

const { Text } = Typography;

/**
 * ItemAnalysisCard - Reusable component for displaying question item analysis
 * Shows difficulty, discrimination, easiest/hardest questions
 */
const ItemAnalysisCard = ({
  itemAnalysis = {},
  questionDifficulty = {},
  easiestQuestions = [],
  hardestQuestions = [],
  averageDifficulty = 0,
  totalQuestions = 0,
  showDetailed = true,
  size = "default",
  style = {},
}) => {
  const getDifficultyColor = (difficulty) => {
    if (difficulty >= 0.8) return "#52c41a"; // Very Easy - Green
    if (difficulty >= 0.6) return "#1890ff"; // Easy - Blue
    if (difficulty >= 0.4) return "#faad14"; // Medium - Orange
    if (difficulty >= 0.2) return "#ff7875"; // Hard - Light Red
    return "#ff4d4f"; // Very Hard - Red
  };

  const getDifficultyLabel = (difficulty) => {
    if (difficulty >= 0.8) return "Sangat Mudah";
    if (difficulty >= 0.6) return "Mudah";
    if (difficulty >= 0.4) return "Sedang";
    if (difficulty >= 0.2) return "Sulit";
    return "Sangat Sulit";
  };

  const problematicQuestions = Object.entries(questionDifficulty).filter(
    ([_, diff]) => diff < 0.3 || diff > 0.9
  ).length;

  return (
    <Card
      title={
        <span>
          <BookOutlined style={{ marginRight: "8px" }} />
          Analisis Item Soal
        </span>
      }
      size={size}
      style={style}
    >
      {/* Summary Statistics */}
      <Row gutter={[16, 16]} style={{ marginBottom: "20px" }}>
        <Col xs={24} sm={6}>
          <div
            style={{
              textAlign: "center",
              padding: "16px",
              backgroundColor: "#f0f5ff",
              borderRadius: "8px",
            }}
          >
            <div
              style={{ fontSize: "24px", fontWeight: "bold", color: "#1890ff" }}
            >
              {totalQuestions || Object.keys(questionDifficulty).length}
            </div>
            <div style={{ fontSize: "12px", color: "#666", marginTop: "4px" }}>
              Total Soal
            </div>
          </div>
        </Col>

        <Col xs={24} sm={6}>
          <div
            style={{
              textAlign: "center",
              padding: "16px",
              backgroundColor: "#f6ffed",
              borderRadius: "8px",
            }}
          >
            <div
              style={{ fontSize: "24px", fontWeight: "bold", color: "#52c41a" }}
            >
              {averageDifficulty
                ? (averageDifficulty * 100).toFixed(1)
                : Object.values(questionDifficulty).length > 0
                ? (
                    (Object.values(questionDifficulty).reduce(
                      (a, b) => a + b,
                      0
                    ) /
                      Object.values(questionDifficulty).length) *
                    100
                  ).toFixed(1)
                : 0}
              %
            </div>
            <div style={{ fontSize: "12px", color: "#666", marginTop: "4px" }}>
              Rata-rata Kesulitan
            </div>
          </div>
        </Col>

        <Col xs={24} sm={6}>
          <div
            style={{
              textAlign: "center",
              padding: "16px",
              backgroundColor: "#fff7e6",
              borderRadius: "8px",
            }}
          >
            <div
              style={{ fontSize: "24px", fontWeight: "bold", color: "#fa8c16" }}
            >
              {easiestQuestions.length}
            </div>
            <div style={{ fontSize: "12px", color: "#666", marginTop: "4px" }}>
              Soal Termudah
            </div>
          </div>
        </Col>

        <Col xs={24} sm={6}>
          <div
            style={{
              textAlign: "center",
              padding: "16px",
              backgroundColor: "#fff2f0",
              borderRadius: "8px",
            }}
          >
            <div
              style={{ fontSize: "24px", fontWeight: "bold", color: "#ff4d4f" }}
            >
              {problematicQuestions}
            </div>
            <div style={{ fontSize: "12px", color: "#666", marginTop: "4px" }}>
              Soal Bermasalah
            </div>
          </div>
        </Col>
      </Row>

      {/* Question Lists */}
      <Row gutter={[16, 16]}>
        {/* Easiest Questions */}
        <Col xs={24} lg={12}>
          <div
            style={{
              border: "1px solid #d9f7be",
              borderRadius: "6px",
              padding: "12px",
              backgroundColor: "#f6ffed",
            }}
          >
            <div style={{ marginBottom: "12px" }}>
              <CheckCircleOutlined
                style={{ color: "#52c41a", marginRight: "8px" }}
              />
              <Text strong>Soal Termudah (Top 5)</Text>
            </div>

            {easiestQuestions && easiestQuestions.length > 0 ? (
              <List
                size="small"
                dataSource={easiestQuestions.slice(0, 5)}
                renderItem={(questionId, index) => (
                  <List.Item style={{ padding: "4px 0", border: "none" }}>
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
                          <Text>Soal #{questionId}</Text>
                        </div>
                        {questionDifficulty[questionId] && (
                          <Tag color="green">
                            {(questionDifficulty[questionId] * 100).toFixed(1)}%
                            benar
                          </Tag>
                        )}
                      </div>
                      {questionDifficulty[questionId] && (
                        <Progress
                          percent={questionDifficulty[questionId] * 100}
                          size="small"
                          strokeColor="#52c41a"
                          showInfo={false}
                          style={{ marginTop: "4px" }}
                        />
                      )}
                    </div>
                  </List.Item>
                )}
              />
            ) : (
              <Text type="secondary">Belum ada data soal termudah</Text>
            )}
          </div>
        </Col>

        {/* Hardest Questions */}
        <Col xs={24} lg={12}>
          <div
            style={{
              border: "1px solid #ffccc7",
              borderRadius: "6px",
              padding: "12px",
              backgroundColor: "#fff2f0",
            }}
          >
            <div style={{ marginBottom: "12px" }}>
              <CloseCircleOutlined
                style={{ color: "#ff4d4f", marginRight: "8px" }}
              />
              <Text strong>Soal Tersulit (Top 5)</Text>
            </div>

            {hardestQuestions && hardestQuestions.length > 0 ? (
              <List
                size="small"
                dataSource={hardestQuestions.slice(0, 5)}
                renderItem={(questionId, index) => (
                  <List.Item style={{ padding: "4px 0", border: "none" }}>
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
                              backgroundColor: "#ff4d4f",
                              marginRight: "8px",
                            }}
                          />
                          <Text>Soal #{questionId}</Text>
                        </div>
                        {questionDifficulty[questionId] && (
                          <Tag color="red">
                            {(questionDifficulty[questionId] * 100).toFixed(1)}%
                            benar
                          </Tag>
                        )}
                      </div>
                      {questionDifficulty[questionId] && (
                        <Progress
                          percent={questionDifficulty[questionId] * 100}
                          size="small"
                          strokeColor="#ff4d4f"
                          showInfo={false}
                          style={{ marginTop: "4px" }}
                        />
                      )}
                    </div>
                  </List.Item>
                )}
              />
            ) : (
              <Text type="secondary">Belum ada data soal tersulit</Text>
            )}
          </div>
        </Col>
      </Row>

      {/* Difficulty Distribution */}
      {showDetailed && Object.keys(questionDifficulty).length > 0 && (
        <div style={{ marginTop: "20px" }}>
          <Text strong style={{ marginBottom: "12px", display: "block" }}>
            <BarChartOutlined style={{ marginRight: "8px" }} />
            Distribusi Tingkat Kesulitan:
          </Text>

          <Row gutter={[8, 8]}>
            {Object.entries(questionDifficulty)
              .slice(0, 10)
              .map(([questionId, difficulty]) => (
                <Col key={questionId} xs={24} sm={12} md={8} lg={6}>
                  <div
                    style={{
                      padding: "8px",
                      border: "1px solid #f0f0f0",
                      borderRadius: "4px",
                      fontSize: "12px",
                    }}
                  >
                    <div style={{ fontWeight: "500" }}>Soal #{questionId}</div>
                    <Progress
                      percent={difficulty * 100}
                      size="small"
                      strokeColor={getDifficultyColor(difficulty)}
                      format={(percent) => `${percent?.toFixed(0)}%`}
                    />
                    <Tag
                      color={getDifficultyColor(difficulty)}
                      style={{ marginTop: "4px", fontSize: "10px" }}
                    >
                      {getDifficultyLabel(difficulty)}
                    </Tag>
                  </div>
                </Col>
              ))}
          </Row>

          {Object.keys(questionDifficulty).length > 10 && (
            <div style={{ textAlign: "center", marginTop: "12px" }}>
              <Text type="secondary">
                ... dan {Object.keys(questionDifficulty).length - 10} soal
                lainnya
              </Text>
            </div>
          )}
        </div>
      )}

      {/* Quality Assessment */}
      {problematicQuestions > 0 && (
        <div style={{ marginTop: "16px" }}>
          <div
            style={{
              padding: "12px",
              backgroundColor: "#fff7e6",
              border: "1px solid #ffd591",
              borderRadius: "6px",
            }}
          >
            <QuestionCircleOutlined
              style={{ color: "#fa8c16", marginRight: "8px" }}
            />
            <Text strong>Perhatian: </Text>
            <Text>
              Ditemukan {problematicQuestions} soal yang mungkin bermasalah
              (terlalu mudah &gt;90% atau terlalu sulit &lt;30% tingkat
              kebenaran). Disarankan untuk review soal-soal tersebut.
            </Text>
          </div>
        </div>
      )}
    </Card>
  );
};

export default ItemAnalysisCard;
