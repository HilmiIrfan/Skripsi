/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
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
  getProgramKeahlian,
  deleteProgramKeahlian,
  addProgramKeahlian,
} from "@/api/programKeahlian";
import TypingCard from "@/components/TypingCard";
import AddProgramKeahlianForm from "./forms/add-program-keahlian-form";
import EditProgramKeahlianForm from "./forms/edit-program-keahlian-form";

const ProgramKeahlian = () => {
  const [programKeahlians, setProgramKeahlians] = useState([]);
  const [addProgramKeahlianModalVisible, setAddProgramKeahlianModalVisible] =
    useState(false);
  const [addProgramKeahlianModalLoading, setAddProgramKeahlianModalLoading] =
    useState(false);
  const [editProgramKeahlianModalVisible, setEditProgramKeahlianModalVisible] =
    useState(false);
  const [editProgramKeahlianModalLoading, setEditProgramKeahlianModalLoading] =
    useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});

  const addProgramKeahlianFormRef = useRef();
  const editProgramKeahlianFormRef = useRef();

  useEffect(() => {
    fetchProgramKeahlian();
  }, []);

  const fetchProgramKeahlian = async () => {
    try {
      const result = await getProgramKeahlian();
      if (result.data.statusCode === 200) {
        setProgramKeahlians(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleDeleteProgramKeahlian = (row) => {
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteProgramKeahlian({ id: row.id });
          message.success("Berhasil dihapus");
          fetchProgramKeahlian();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditProgramKeahlian = (row) => {
    setCurrentRowData({ ...row });
    setEditProgramKeahlianModalVisible(true);
  };

  const handleAddProgramKeahlianOk = async (values) => {
    setAddProgramKeahlianModalLoading(true);
    try {
      await addProgramKeahlian(values);
      message.success("Berhasil menambahkan");
      fetchProgramKeahlian();
      setAddProgramKeahlianModalVisible(false);
    } catch (error) {
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddProgramKeahlianModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddProgramKeahlianModalVisible(false);
    setEditProgramKeahlianModalVisible(false);
  };

  const renderColumns = () => [
    {
      title: "Bidang Keahlian",
      dataIndex: ["bidangKeahlian", "bidang"],
      key: "bidangKeahlian.bidang",
      align: "center",
    },
    {
      title: "ID Program Keahlian",
      dataIndex: "id",
      key: "id",
      align: "center",
    },
    {
      title: "Program Keahlian",
      dataIndex: "program",
      key: "program",
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
            onClick={() => handleEditProgramKeahlian(row)}
          />
          <Divider type="vertical" />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteProgramKeahlian(row)}
          />
        </span>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="id"
      dataSource={programKeahlians}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button
          type="primary"
          onClick={() => setAddProgramKeahlianModalVisible(true)}
        >
          Tambahkan Program Keahlian
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
        title="Manajemen Program Keahlian"
        source="Di sini, Anda dapat mengelola program keahlian di sistem."
      />
      <br />
      <Card title={renderButtons()}>{renderTable()}</Card>

      <AddProgramKeahlianForm
        ref={addProgramKeahlianFormRef}
        visible={addProgramKeahlianModalVisible}
        confirmLoading={addProgramKeahlianModalLoading}
        onCancel={handleCancel}
        onOk={handleAddProgramKeahlianOk}
      />

      <EditProgramKeahlianForm
        ref={editProgramKeahlianFormRef}
        currentRowData={currentRowData}
        visible={editProgramKeahlianModalVisible}
        confirmLoading={editProgramKeahlianModalLoading}
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
            onClick={() => {
             
            }}
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

export default ProgramKeahlian;
