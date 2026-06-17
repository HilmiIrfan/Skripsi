/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import {
  Card,
  Button,
  Table,
  message,
  Upload,
  Row,
  Col,
  Divider,
  Modal,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
} from "@ant-design/icons";
import {
  getKonsentrasiKeahlian,
  deleteKonsentrasiKeahlian,
  addKonsentrasiKeahlian,
} from "@/api/konsentrasiKeahlian";
import TypingCard from "@/components/TypingCard";
import AddKonsentrasiKeahlianForm from "./forms/add-konsentrasi-keahlian-form";
import EditKonsentrasiKeahlianForm from "./forms/edit-konsentrasi-keahlian-form";

const KonsentrasiKeahlian = () => {
  const [konsentrasiKeahlians, setKonsentrasiKeahlians] = useState([]);
  const [addModalVisible, setAddModalVisible] = useState(false);
  const [addModalLoading, setAddModalLoading] = useState(false);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [editModalLoading, setEditModalLoading] = useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});

  const addFormRef = useRef();
  const editFormRef = useRef();

  useEffect(() => {
    fetchKonsentrasiKeahlian();
  }, []);

  const fetchKonsentrasiKeahlian = async () => {
    try {
      const result = await getKonsentrasiKeahlian();
      if (result.data.statusCode === 200) {
        setKonsentrasiKeahlians(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleDelete = (row) => {
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteKonsentrasiKeahlian({ id: row.id });
          message.success("Berhasil dihapus");
          fetchKonsentrasiKeahlian();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEdit = (row) => {
    setCurrentRowData({ ...row });
    setEditModalVisible(true);
  };

  const handleAddOk = async (values) => {
    setAddModalLoading(true);
    try {
      await addKonsentrasiKeahlian(values);
      message.success("Berhasil menambahkan");
      fetchKonsentrasiKeahlian();
      setAddModalVisible(false);
    } catch (error) {
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddModalVisible(false);
    setEditModalVisible(false);
  };

  const renderColumns = () => [
    {
      title: "Program Keahlian",
      dataIndex: ["programKeahlian", "program"],
      key: "program",
      align: "center",
    },
    {
      title: "ID Konsentrasi Keahlian",
      dataIndex: "id",
      key: "id",
      align: "center",
    },
    {
      title: "Konsentrasi Keahlian",
      dataIndex: "konsentrasi",
      key: "konsentrasi",
      align: "center",
    },
    {
      title: "Operasi",
      key: "action",
      align: "center",
      render: (_, row) => (
        <span>
          <Button
            type="primary"
            shape="circle"
            icon={<EditOutlined />}
            onClick={() => handleEdit(row)}
          />
          <Divider type="vertical" />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(row)}
          />
        </span>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="id"
      dataSource={konsentrasiKeahlians}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button type="primary" onClick={() => setAddModalVisible(true)}>
          Tambahkan Konsentrasi Keahlian
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
        title="Manajemen Konsentrasi Keahlian"
        source="Di sini, Anda dapat mengelola konsentrasi keahlian di sistem."
      />
      <br />
      <Card title={renderButtons()}>{renderTable()}</Card>

      <AddKonsentrasiKeahlianForm
        ref={addFormRef}
        visible={addModalVisible}
        confirmLoading={addModalLoading}
        onCancel={handleCancel}
        onOk={handleAddOk}
      />

      <EditKonsentrasiKeahlianForm
        ref={editFormRef}
        currentRowData={currentRowData}
        visible={editModalVisible}
        confirmLoading={editModalLoading}
        onCancel={handleCancel}
      />

      {/* <Modal
        title="Import File"
        visible={importModalVisible}
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

export default KonsentrasiKeahlian;
