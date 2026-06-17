/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import {
  Card,
  Button,
  Table,
  message,
  Modal,
  Row,
  Col,
  Divider,
  Upload,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
} from "@ant-design/icons";
import {
  getBidangKeahlian,
  deleteBidangKeahlian,
  addBidangKeahlian,
} from "@/api/bidangKeahlian";
import TypingCard from "@/components/TypingCard";
import AddBidangKeahlianForm from "./forms/add-bidang-keahlian-form";
import EditBidangKeahlianForm from "./forms/edit-bidang-keahlian-form";

const BidangKeahlian = () => {
  const [bidangKeahlians, setBidangKeahlians] = useState([]);
  const [addBidangKeahlianModalVisible, setAddBidangKeahlianModalVisible] =
    useState(false);
  const [addBidangKeahlianModalLoading, setAddBidangKeahlianModalLoading] =
    useState(false);
  const [editBidangKeahlianModalVisible, setEditBidangKeahlianModalVisible] =
    useState(false);
  const [editBidangKeahlianModalLoading, setEditBidangKeahlianModalLoading] =
    useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});

  useEffect(() => {
    fetchBidangKeahlian();
  }, []);

  const fetchBidangKeahlian = async () => {
    try {
      const result = await getBidangKeahlian();
      if (result.data.statusCode === 200) {
        setBidangKeahlians(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleDeleteBidangKeahlian = (row) => {
    const { id } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteBidangKeahlian({ id });
          message.success("Berhasil dihapus");
          fetchBidangKeahlian();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditBidangKeahlian = (row) => {
    setCurrentRowData({ ...row });
    setEditBidangKeahlianModalVisible(true);
  };

  const handleAddBidangKeahlianOk = async (values) => {
    setAddBidangKeahlianModalLoading(true);
    try {
      await addBidangKeahlian(values);
      message.success("Berhasil menambahkan");
      fetchBidangKeahlian();
      setAddBidangKeahlianModalVisible(false);
    } catch (error) {
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddBidangKeahlianModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddBidangKeahlianModalVisible(false);
    setEditBidangKeahlianModalVisible(false);
  };

  const renderColumns = () => [
    {
      title: "ID Bidang Keahlian",
      dataIndex: "id",
      key: "id",
      align: "center",
    },
    {
      title: "Nama Bidang Keahlian",
      dataIndex: "bidang",
      key: "bidang",
      align: "center",
    },
    {
      title: "Operasi",
      key: "action",
      align: "center",
      render: (text, row) => (
        <span>
          <Button
            type="primary"
            shape="circle"
            icon={<EditOutlined />}
            onClick={() => handleEditBidangKeahlian(row)}
          />
          <Divider type="vertical" />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteBidangKeahlian(row)}
          />
        </span>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="id"
      dataSource={bidangKeahlians}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button
          type="primary"
          onClick={() => setAddBidangKeahlianModalVisible(true)}
        >
          Tambahkan Bidang Keahlian
        </Button>
      </Col>
      <Col>
        <Button
          icon={<UploadOutlined />}
          onClick={() => setImportModalVisible(true)}
        >
          Import File
        </Button>
      </Col>
    </Row>
  );

  return (
    <div className="app-container">
      <TypingCard
        title="Manajemen Bidang Keahlian"
        source="Di sini, Anda dapat mengelola bidang keahlian di sistem."
      />
      <br />
      <Card title={renderButtons()}>{renderTable()}</Card>

      <AddBidangKeahlianForm
        visible={addBidangKeahlianModalVisible}
        confirmLoading={addBidangKeahlianModalLoading}
        onCancel={handleCancel}
        onOk={handleAddBidangKeahlianOk}
      />

      <EditBidangKeahlianForm
        currentRowData={currentRowData}
        visible={editBidangKeahlianModalVisible}
        confirmLoading={editBidangKeahlianModalLoading}
        onCancel={handleCancel}
      />

      {/* <Modal
        title="Import File"
        open={importModalVisible}
        onCancel={() => setImportModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setImportModalVisible(false)}>
            Cancel
          </Button>,
          <Button
            key="upload"
            type="primary"
            loading={uploading}
            onClick={() => {}}
          >
            Upload
          </Button>,
        ]}
      >
        <Upload
          beforeUpload={() => {
            return false;
          }}
          accept=".csv,.xlsx,.xls"
        >
          <Button>Pilih File</Button>
        </Upload>
      </Modal> */}
    </div>
  );
};

export default BidangKeahlian;
