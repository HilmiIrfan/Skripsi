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
  getTaksonomi,
  deleteTaksonomi,
  addTaksonomi,
  editTaksonomi,
} from "@/api/taksonomi";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import TypingCard from "@/components/TypingCard";
import AddTaksonomiForm from "./forms/add-taksonomi-form";
import EditTaksonomiForm from "./forms/edit-taksonomi-form";
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { reqUserInfo, getUserById } from "@/api/user";
import { set } from "nprogress";

const Taksonomi = () => {
  const [taksonomis, setTaksonomis] = useState([]);
  const [addTaksonomiModalVisible, setAddTaksonomiModalVisible] =
    useState(false);
  const [addTaksonomiModalLoading, setAddTaksonomiModalLoading] =
    useState(false);
  const [editTaksonomiModalVisible, setEditTaksonomiModalVisible] =
    useState(false);
  const [editTaksonomiModalLoading, setEditTaksonomiModalLoading] =
    useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [searchKeyword, setSearchKeyword] = useState("");
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");

  // Fungsi Helper Table Search
  const { getColumnSearchProps } = useTableSearch();

  const editTaksonomiFormRef = useRef();
  const addTaksonomiFormRef = useRef();

  const fetchTaksonomis = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getTaksonomi();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setTaksonomis(content);
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
    fetchTaksonomis();
  }, [fetchTaksonomis]);

  const handleDeleteTaksonomi = (row) => {
    const { idTaksonomi } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteTaksonomi({ idTaksonomi });
          message.success("Berhasil dihapus");
          fetchTaksonomis();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditTaksonomi = (row) => {
    setCurrentRowData({ ...row });
    setEditTaksonomiModalVisible(true);
  };

  const handleAddTaksonomiOk = async (values) => {
    setAddTaksonomiModalLoading(true);
    try {
      const updatedData = {
        idTaksonomi: null,
        namaTaksonomi: values.namaTaksonomi,
        deskripsiTaksonomi: values.deskripsiTaksonomi,
        idSekolah: values.idSchool,
      };
      console.log("Updated Data:", updatedData);
      await addTaksonomi(updatedData);
      setAddTaksonomiModalVisible(false);
      message.success("Berhasil menambahkan");
      fetchTaksonomis();
    } catch (error) {
      setAddTaksonomiModalVisible(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddTaksonomiModalLoading(false);
    }
  };

  const handleEditTaksonomiOk = async (values) => {
    setEditTaksonomiModalLoading(true);
    try {
      const updatedData = {
        idTaksonomi: values.idTaksonomi,
        namaTaksonomi: values.namaTaksonomi,
        deskripsiTaksonomi: values.deskripsiTaksonomi,
        idSekolah: values.idSchool,
      };
      console.log("Updated Data:", updatedData);
      await editTaksonomi(updatedData, currentRowData.idTaksonomi);
      setEditTaksonomiModalVisible(false);
      message.success("Berhasil mengedit");
      fetchTaksonomis();
    } catch (error) {
      setEditTaksonomiModalVisible(false);
      message.error("Gagal mengedit: " + error.message);
    } finally {
      setEditTaksonomiModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddTaksonomiModalVisible(false);
    setEditTaksonomiModalVisible(false);
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getTaksonomi();
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
      title: "Nama Taksonomi",
      dataIndex: "namaTaksonomi",
      key: "namaTaksonomi",
      align: "center",
      ...getColumnSearchProps("namaTaksonomi"),
      sorter: (a, b) => a.namaTaksonomi.localeCompare(b.namaTaksonomi),
    },
    {
      title: "Deskripsi Taksonomi",
      dataIndex: "deskripsiTaksonomi",
      key: "deskripsiTaksonomi",
      align: "center",
      ...getColumnSearchProps("deskripsiTaksonomi"),
      sorter: (a, b) =>
        a.deskripsiTaksonomi.localeCompare(b.deskripsiTaksonomi),
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
            onClick={() => handleEditTaksonomi(row)}
          />
          <Divider type="vertical" />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteTaksonomi(row)}
          />
        </span>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="idTaksonomi"
      dataSource={taksonomis}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button
          type="primary"
          onClick={() => setAddTaksonomiModalVisible(true)}
        >
          Tambahkan Taksonomi
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
        title="Manajemen Taksonomi"
        source="Di sini, Anda dapat mengelola taksonomi di sistem."
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
                placeholder="Cari taksonomi..."
                allowClear
                enterButton
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ width: 300 }}
              />
            </Col>
          </Row>

          {/* Tabel */}
          {renderTable()}

          <AddTaksonomiForm
            wrappedComponentRef={addTaksonomiFormRef}
            visible={addTaksonomiModalVisible}
            confirmLoading={addTaksonomiModalLoading}
            onCancel={handleCancel}
            onOk={handleAddTaksonomiOk}
          />

          <EditTaksonomiForm
            wrappedComponentRef={editTaksonomiFormRef}
            currentRowData={currentRowData}
            visible={editTaksonomiModalVisible}
            confirmLoading={editTaksonomiModalLoading}
            onCancel={handleCancel}
            onOk={handleEditTaksonomiOk}
          />
        </Card>
      )}

      <Modal
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
      </Modal>
    </div>
  );
};

export default Taksonomi;
