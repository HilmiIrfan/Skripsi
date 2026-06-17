/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef, useCallback } from "react";
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
  Input,
  Space,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
  SearchOutlined,
} from "@ant-design/icons";
import {
  getSemester,
  deleteSemester,
  addSemester,
  editSemester,
} from "@/api/semester";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import TypingCard from "@/components/TypingCard";
import AddSemesterForm from "./forms/add-semester-form";
import EditSemesterForm from "./forms/edit-semester-form";
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { reqUserInfo, getUserById } from "@/api/user";
import { set } from "nprogress";

const Semester = () => {
  const [semesters, setSemesters] = useState([]);
  const [addSemesterModalVisible, setAddSemesterModalVisible] = useState(false);
  const [addSemesterModalLoading, setAddSemesterModalLoading] = useState(false);
  const [editSemesterModalVisible, setEditSemesterModalVisible] =
    useState(false);
  const [editSemesterModalLoading, setEditSemesterModalLoading] =
    useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [searchKeyword, setSearchKeyword] = useState("");
  const [userIdJson, setUserIdJson] = useState("");
  const [loading, setLoading] = useState(true);
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const [searchQuery, setSearchQuery] = useState("");

  // Fungsi Helper Table Search
  const { getColumnSearchProps } = useTableSearch();

  const editSemesterFormRef = useRef();
  const addSemesterFormRef = useRef();

  const fetchSemesters = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getSemester();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setSemesters(content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchSemesters();
  }, [fetchSemesters]);

  const handleDeleteSemester = (row) => {
    const { idSemester } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteSemester({ idSemester });
          message.success("Berhasil dihapus");
          fetchSemesters();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditSemester = (row) => {
    setCurrentRowData({ ...row });
    setEditSemesterModalVisible(true);
  };

  const handleAddSemesterOk = async (values) => {
    setAddSemesterModalLoading(true);
    try {
      const updatedData = {
        idSemester: null,
        namaSemester: values.namaSemester,
        idSekolah: values.idSchool,
      };
      await addSemester(updatedData);
      setAddSemesterModalVisible(false);
      message.success("Berhasil menambahkan");
      fetchSemesters();
    } catch (error) {
      setAddSemesterModalVisible(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddSemesterModalLoading(false);
    }
  };

  const handleEditSemesterOk = async (values) => {
    setEditSemesterModalLoading(true);
    try {
      const updatedData = {
        idSemester: values.idSemester,
        namaSemester: values.namaSemester,
        idSekolah: values.idSchool,
      };
      console.log("Updated Data:", updatedData);
      await editSemester(updatedData, currentRowData.idSemester);
      setEditSemesterModalVisible(false);
      message.success("Berhasil mengedit");
      fetchSemesters();
    } catch (error) {
      setEditSemesterModalVisible(false);
      message.error("Gagal mengedit: " + error.message);
    } finally {
      setEditSemesterModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddSemesterModalVisible(false);
    setEditSemesterModalVisible(false);
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getSemester();
  };

  const renderColumns = () => [
    {
      title: "No",
      dataIndex: "index",
      key: "index",
      align: "center",
      render: (_, __, index) => index + 1,
    },
    {
      title: "Nama Semester",
      dataIndex: "namaSemester",
      key: "namaSemester",
      align: "center",
      ...getColumnSearchProps("namaSemester"),
      sorter: (a, b) => a.namaSemester.localeCompare(b.namaSemester),
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
            onClick={() => handleEditSemester(row)}
          />
          <Divider type="vertical" />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteSemester(row)}
          />
        </span>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="idSemester"
      dataSource={semesters}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button type="primary" onClick={() => setAddSemesterModalVisible(true)}>
          Tambahkan Semester
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
        title="Manajemen Semester"
        source="Di sini, Anda dapat mengelola semester di sistem."
      />
      <br />
      {loading ? (
        <Card>
          <Skeleton active paragraph={{ rows: 10 }} />
        </Card>
      ) : (
        <Card style={{ overflowX: "scroll" }}>
          {/* Baris untuk tombol dan pencarian */}
          <Row
            justify="space-between"
            align="middle"
            style={{ marginBottom: 16 }}
          >
            {/* Tombol Tambah & Import */}
            {renderButtons()}

            {/* Kolom Pencarian */}
            <Col>
              <Input.Search
                key="search"
                placeholder="Cari semester..."
                allowClear
                enterButton
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ width: 300 }}
              />
            </Col>
          </Row>

          {/* Tabel */}
          {renderTable()}

          <AddSemesterForm
            wrappedComponentRef={addSemesterFormRef}
            visible={addSemesterModalVisible}
            confirmLoading={addSemesterModalLoading}
            onCancel={handleCancel}
            onOk={handleAddSemesterOk}
          />

          <EditSemesterForm
            wrappedComponentRef={editSemesterFormRef}
            currentRowData={currentRowData}
            visible={editSemesterModalVisible}
            confirmLoading={editSemesterModalLoading}
            onCancel={handleCancel}
            onOk={handleEditSemesterOk}
          />
        </Card>
      )}

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
        <Upload beforeUpload={() => false} accept=".csv,.xlsx,.xls">
          <Button>Pilih File</Button>
        </Upload>
      </Modal> */}
    </div>
  );
};

export default Semester;
