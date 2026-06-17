/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import { Row, Col, Card } from "antd";
import { Typography } from "antd";
const { Text, Title } = Typography;
import "./index.less";

const Dashboard = () => {
  return (
    <div className="app-container">
      <a
        target="_blank"
        rel="noopener noreferrer"
        className="github-corner"
      ></a>

      <Card
        style={{
          marginBottom: "25px",
          background: "linear-gradient(to right, #1890ff, #52c41a)",
          borderRadius: "10px",
          boxShadow: "0 4px 12px rgba(0, 0, 0, 0.15)",
        }}
      >
        <Row align="middle" justify="space-between">
          <Col>
            <Title level={2} style={{ color: "#fff", margin: 0 }}>
              Selamat Datang di Dashboard
            </Title>
            <Text style={{ color: "#fff", fontSize: "16px" }}>
              Selamat bekerja dan semoga harimu menyenangkan!
            </Text>
          </Col>
          <Col>
            <div style={{ fontSize: "60px", color: "#fff", opacity: 0.8 }}>
              👋
            </div>
          </Col>
        </Row>
      </Card>
    </div>
  );
};

export default Dashboard;
