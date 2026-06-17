/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef, useCallback } from "react";
import {
  Card,
  Button,
  Table,
  message,
  Modal,
  Row,
  Col,
  Upload,
  Divider,
  Input,
  Space,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
  PlusOutlined,
  SearchOutlined,
} from "@ant-design/icons";
import {
  getTahunAjaran,
  deleteTahunAjaran,
  addTahunAjaran,
  editTahunAjaran,
} from "@/api/tahun-ajaran";
import TypingCard from "@/components/TypingCard";
import Highlighter from "react-highlight-words";
import AddTahunAjaranForm from "./forms/add-tahun-ajaran-form";
import EditTahunAjaranForm from "./forms/edit-tahun-ajaran-form";
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { reqUserInfo, getUserById } from "@/api/user";
import { Skeleton } from "antd";
import { use } from "react";

const TahunAjaran = () => {
  const [tahunAjaran, setTahunAjaran] = useState([]);
  const [addTahunAjaranModalVisible, setAddTahunAjaranModalVisible] =
    useState(false);
  const [addTahunAjaranModalLoading, setAddTahunAjaranModalLoading] =
    useState(false);
  const [editTahunAjaranModalVisible, setEditTahunAjaranModalVisible] =
    useState(false);
  const [editTahunAjaranModalLoading, setEditTahunAjaranModalLoading] =
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

  const editTahunAjaranFormRef = useRef();
  const addTahunAjaranFormRef = useRef();

  const fetchTahunAjaran = useCallback(async () => {
    setLoading(true);
    try {
      const response = await getTahunAjaran();
      const { content, statusCode } = response.data;
      if (statusCode === 200) {
        setTahunAjaran(content);
      } else {
        message.error("Gagal mendapatkan data: " + response.message);
      }
    } catch (error) {
      message.error("Terjadi Kesalahan: " + error.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchTahunAjaran();
  }, [fetchTahunAjaran]);

  const handleDeleteTahunAjaran = (row) => {
    const { idTahun } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteTahunAjaran({ idTahun });
          message.success("Berhasil dihapus");
          fetchTahunAjaran();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditTahunAjaran = (row) => {
    setCurrentRowData({ ...row });
    setEditTahunAjaranModalVisible(true);
  };

  const handleAddTahunAjaran = () => {
    setAddTahunAjaranModalVisible(true);
  };

  const handleAddTahunAjaranOk = async (values) => {
    setAddTahunAjaranModalLoading(true);
    try {
      const updatedData = {
        idTahun: null,
        tahunAjaran: values.tahunAjaran,
        idSekolah: values.idSchool,
      };
      await addTahunAjaran(updatedData);
      setAddTahunAjaranModalVisible(false);
      message.success("Berhasil menambahkan");
      fetchTahunAjaran();
    } catch (error) {
      setAddTahunAjaranModalVisible(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddTahunAjaranModalLoading(false);
    }
  };

  const handleEditTahunAjaranOk = async (values) => {
    setEditTahunAjaranModalLoading(true);
    try {
      const updatedData = {
        idTahun: values.idTahun,
        tahunAjaran: values.tahunAjaran,
        idSekolah: values.idSchool,
      };
      console.log("Updated Data:", updatedData);
      await editTahunAjaran(updatedData);
      setEditTahunAjaranModalVisible(false);
      setEditTahunAjaranModalLoading(false);
      message.success("Berhasil mengedit");
      fetchTahunAjaran();
    } catch (error) {
      setEditTahunAjaranModalVisible(false);
      setEditTahunAjaranModalLoading(false);
      message.error("Gagal mengedit: " + error.message);
    } finally {
      setEditTahunAjaranModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddTahunAjaranModalVisible(false);
    setEditTahunAjaranModalVisible(false);
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getTahunAjaran();
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
      title: "Tahun Ajaran",
      dataIndex: "tahunAjaran",
      key: "tahunAjaran",
      align: "center",
      ...getColumnSearchProps("tahunAjaran"),
      sorter: (a, b) => a.tahunAjaran.localeCompare(b.tahunAjaran),
    },
    {
      title: "Operasi",
      key: "action",
      align: "center",
      render: (__, row) => (
        <span>
          <Button
            type="primary"
            shape="circle"
            icon={<EditOutlined />}
            onClick={() => handleEditTahunAjaran(row)}
          />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteTahunAjaran(row)}
          />
        </span>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="idTahun"
      dataSource={tahunAjaran}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button
          type="primary"
          onClick={() => setAddTahunAjaranModalVisible(true)}
        >
          Tambahkan Tahun Ajaran
        </Button>
      </Col>
      {/* <Col>
        <Button
          icon={<UploadOutlined />}
          onClick={() => setImportModalVisible(true)}
        >
          Import File
        </Button>
      </Col> */}
    </Row>
  );

  return (
    <div className="app-container">
      <TypingCard
        title="Manajemen Tahun Ajaran"
        source="Di sini, Anda dapat mengelola tahun ajaran di sistem."
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
                placeholder="Cari bidang tahun ajaran..."
                allowClear
                enterButton
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ width: 300 }}
              />
            </Col>
          </Row>

          {/* Tabel */}
          {renderTable()}

          <AddTahunAjaranForm
            wrappedComponentRef={addTahunAjaranFormRef}
            visible={addTahunAjaranModalVisible}
            confirmLoading={addTahunAjaranModalLoading}
            onCancel={handleCancel}
            onOk={handleAddTahunAjaranOk}
          />

          <EditTahunAjaranForm
            wrappedComponentRef={editTahunAjaranFormRef}
            currentRowData={currentRowData}
            visible={editTahunAjaranModalVisible}
            confirmLoading={editTahunAjaranModalLoading}
            onCancel={handleCancel}
            onOk={handleEditTahunAjaranOk}
          />
        </Card>
      )}
    </div>
  );
};

export default TahunAjaran;
