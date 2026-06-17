/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from "react";
import { Card, Statistic, Progress, Tag, Row, Col } from "antd";
import {
  TrophyOutlined,
  CheckCircleOutlined,
  WarningOutlined,
  StarOutlined,
} from "@ant-design/icons";

/**
 * PerformanceCard - Reusable component for displaying performance metrics
 * Used across ujian analysis views
 */
const PerformanceCard = ({
  title = "Performa",
  averageScore = 0,
  passRate = 0,
  totalParticipants = 0,
  gradeDistribution = {},
  showGradeDistribution = true,
  size = "default",
  style = {},
}) => {
  const getScoreColor = (score) => {
    if (score >= 85) return "#52c41a";
    if (score >= 70) return "#1890ff";
    if (score >= 60) return "#faad14";
    return "#ff4d4f";
  };

  const getGradeColor = (grade) => {
    const colors = {
      A: "green",
      B: "blue",
      C: "orange",
      D: "red",
      E: "red",
      F: "red",
    };
    return colors[grade] || "default";
  };

  return (
    <Card title={title} size={size} style={style}>
      <Row gutter={[16, 16]}>
        {/* Primary Metrics */}
        <Col xs={24} sm={8}>
          <Statistic
            title="Rata-rata"
            value={averageScore}
            precision={1}
            prefix={<TrophyOutlined />}
            valueStyle={{ color: getScoreColor(averageScore) }}
          />
        </Col>

        <Col xs={24} sm={8}>
          <Statistic
            title="Tingkat Kelulusan"
            value={passRate}
            precision={1}
            suffix="%"
            prefix={<CheckCircleOutlined />}
            valueStyle={{
              color:
                passRate >= 80
                  ? "#52c41a"
                  : passRate >= 60
                  ? "#faad14"
                  : "#ff4d4f",
            }}
          />
        </Col>

        <Col xs={24} sm={8}>
          <Statistic
            title="Total Peserta"
            value={totalParticipants}
            prefix={<StarOutlined />}
          />
        </Col>

        {/* Grade Distribution */}
        {showGradeDistribution && Object.keys(gradeDistribution).length > 0 && (
          <Col span={24}>
            <div style={{ marginTop: "16px" }}>
              <div
                style={{
                  marginBottom: "12px",
                  fontSize: "14px",
                  fontWeight: "500",
                }}
              >
                Distribusi Nilai:
              </div>
              <div>
                {Object.entries(gradeDistribution).map(([grade, count]) => {
                  const percentage =
                    totalParticipants > 0
                      ? (count / totalParticipants) * 100
                      : 0;
                  return (
                    <div key={grade} style={{ marginBottom: "8px" }}>
                      <Row align="middle">
                        <Col span={4}>
                          <Tag color={getGradeColor(grade)}>{grade}</Tag>
                        </Col>
                        <Col span={12}>
                          <Progress
                            percent={percentage}
                            size="small"
                            strokeColor={getGradeColor(grade)}
                            showInfo={false}
                          />
                        </Col>
                        <Col span={8}>
                          <span style={{ fontSize: "12px", color: "#666" }}>
                            {count} siswa ({percentage.toFixed(1)}%)
                          </span>
                        </Col>
                      </Row>
                    </div>
                  );
                })}
              </div>
            </div>
          </Col>
        )}
      </Row>
    </Card>
  );
};

export default PerformanceCard;
