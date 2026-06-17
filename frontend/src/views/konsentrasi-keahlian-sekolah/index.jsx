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
  UploadOutlined,
  EditOutlined,
  DeleteOutlined,
  SearchOutlined,
} from "@ant-design/icons";
import {
  getKonsentrasiSekolah,
  deleteKonsentrasiSekolah,
  editKonsentrasiSekolah,
  addKonsentrasiSekolah,
} from "@/api/konsentrasiKeahlianSekolah";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import TypingCard from "@/components/TypingCard";
import EditKonsentrasiSekolahForm from "./forms/edit-konsentrasi-keahlian-sekolah-form";
import AddKonsentrasiSekolahForm from "./forms/add-konsentrasi-keahlian-sekolah-form";
import { reqUserInfo, getUserById } from "@/api/user";
import { read, utils } from "xlsx";

const { Column } = Table;

const KonsentrasiSekolah = () => {
  const [konsentrasiSekolah, setKonsentrasiSekolah] = useState([]);
  const [
    editKonsentrasiSekolahModalVisible,
    setEditKonsentrasiSekolahModalVisible,
  ] = useState(false);
  const [
    editKonsentrasiSekolahModalLoading,
    setEditKonsentrasiSekolahModalLoading,
  ] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [
    addKonsentrasiSekolahModalVisible,
    setAddKonsentrasiSekolahModalVisible,
  ] = useState(false);
  const [
    addKonsentrasiSekolahModalLoading,
    setAddKonsentrasiSekolahModalLoading,
  ] = useState(false);
  const [importedData, setImportedData] = useState([]);
  const [columnTitles, setColumnTitles] = useState([]);
  const [fileName, setFileName] = useState("");
  const [uploading, setUploading] = useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [columnMapping, setColumnMapping] = useState({});
  const [searchKeyword, setSearchKeyword] = useState("");
  const [tableLoading, setTableLoading] = useState(false);
  const [userIdJson, setUserIdJson] = useState("");
  const [loading, setLoading] = useState(true);
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const [searchQuery, setSearchQuery] = useState("");

  const searchInput = useRef(null);

  const editKonsentrasiSekolahFormRef = useRef();
  const addKonsentrasiSekolahFormRef = useRef();

  useEffect(() => {
    const initializeData = async () => {
      const userInfoResponse = await reqUserInfo();
      const { id: userId } = userInfoResponse.data;

      await getUserInfoJson(userId);
    };

    initializeData();
  }, []);

  const fetchKonsentrasiSekolah = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getKonsentrasiSekolah();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const filteredContent = content.filter(
          (item) => item.school?.idSchool === userIdJson
        );
        setKonsentrasiSekolah(filteredContent);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setLoading(false);
    }
  }, [userIdJson]);

  useEffect(() => {
    if (userIdJson) {
      fetchKonsentrasiSekolah();
    }
  }, [userIdJson, fetchKonsentrasiSekolah]);

  const filteredData = konsentrasiSekolah.filter((item) => {
    const query = searchQuery.toLowerCase();
    return (
      (item?.namaKonsentrasiSekolah?.toLowerCase() || "").includes(query) ||
      (item?.nameSchool?.toLowerCase() || "").includes(query) ||
      (item?.konsentrasi?.toLowerCase() || "").includes(query)
    );
  });

  const getUserInfoJson = async (userId) => {
    const result = await getUserById(userId);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setUserIdJson(content[0].school.idSchool); // Ubah dari userId ke schoolId
    }
  };

  const handleDelete = (row) => {
    const { idKonsentrasiSekolah } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteKonsentrasiSekolah({ idKonsentrasiSekolah });
          message.success("Berhasil dihapus");
          fetchKonsentrasiSekolah();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditOk = async (values) => {
    setEditKonsentrasiSekolahModalLoading(true);
    try {
      const updatedValues = {
        idKonsentrasiSekolah: values.idKonsentrasiSekolah,
        namaKonsentrasiSekolah: values.namaKonsentrasiSekolah,
        idSekolah: values.idSchool,
        idKonsentrasiKeahlian: values.id,
      };

      console.log("respon data", updatedValues);
      await editKonsentrasiSekolah(
        updatedValues,
        currentRowData.idKonsentrasiSekolah
      );
      setEditKonsentrasiSekolahModalVisible(false);
      setEditKonsentrasiSekolahModalLoading(false);
      message.success("Berhasil mengubah");
      fetchKonsentrasiSekolah();
    } catch (error) {
      setEditKonsentrasiSekolahModalLoading(false);
      message.error("Gagal mengubah: " + error.message);
    }
  };

  const handleEdit = (row) => {
    setCurrentRowData({ ...row });
    setEditKonsentrasiSekolahModalVisible(true);
  };

  const handleCancel = () => {
    setEditKonsentrasiSekolahModalVisible(false);
    setAddKonsentrasiSekolahModalVisible(false);
  };

  const handleAdd = () => {
    setAddKonsentrasiSekolahModalVisible(true);
  };
  const handleAddOk = async (values) => {
    setAddKonsentrasiSekolahModalLoading(true);
    try {
      const updatedValues = {
        idKonsentrasiSekolah: null,
        namaKonsentrasiSekolah: values.namaKonsentrasiSekolah,
        idSekolah: values.idSchool,
        idKonsentrasiKeahlian: values.id,
      };
      console.log("respon data", updatedValues);
      await addKonsentrasiSekolah(updatedValues);
      setAddKonsentrasiSekolahModalLoading(false);
      setAddKonsentrasiSekolahModalLoading(false);
      message.success("Berhasil menambahkan");
      fetchKonsentrasiSekolah();
    } catch (error) {
      setAddKonsentrasiSekolahModalLoading(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddKonsentrasiSekolahModalVisible(false);
    }
  };

  const handleSearchTable = (selectedKeys, confirm, dataIndex) => {
    confirm();
    setSearchText(selectedKeys[0]);
    setSearchedColumn(dataIndex);
  };

  const handleReset = (clearFilters) => {
    clearFilters();
    setSearchText("");
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getKonsentrasiSekolah();
  };

  const getColumnSearchProps = (dataIndex, nestedPath) => ({
    filterDropdown: ({
      setSelectedKeys,
      selectedKeys,
      confirm,
      clearFilters,
      close,
    }) => (
      <div style={{ padding: 8 }} onKeyDown={(e) => e.stopPropagation()}>
        <Input
          ref={searchInput}
          placeholder={`Search ${dataIndex}`}
          value={selectedKeys[0]}
          onChange={(e) =>
            setSelectedKeys(e.target.value ? [e.target.value] : [])
          }
          onPressEnter={() =>
            handleSearchTable(selectedKeys, confirm, dataIndex)
          }
          style={{ marginBottom: 8, display: "block" }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() => handleSearchTable(selectedKeys, confirm, dataIndex)}
            icon={<SearchOutlined />}
            size="small"
            style={{ width: 90 }}
          >
            Search
          </Button>
          <Button
            onClick={() => clearFilters && handleReset(clearFilters)}
            size="small"
            style={{ width: 90 }}
          >
            Reset
          </Button>
          <Button
            type="link"
            size="small"
            onClick={() => {
              confirm({ closeDropdown: false });
              setSearchText(selectedKeys[0]);
              setSearchedColumn(dataIndex);
            }}
          >
            Filter
          </Button>
          <Button type="link" size="small" onClick={() => close()}>
            Close
          </Button>
        </Space>
      </div>
    ),
    filterIcon: (filtered) => (
      <SearchOutlined style={{ color: filtered ? "#1677ff" : undefined }} />
    ),
    onFilter: (value, record) => {
      if (nestedPath) {
        const nestedValue = nestedPath
          .split(".")
          .reduce((obj, key) => obj?.[key], record);
        return nestedValue
          ?.toString()
          .toLowerCase()
          .includes(value.toLowerCase());
      }
      return record[dataIndex]
        ?.toString()
        .toLowerCase()
        .includes(value.toLowerCase());
    },
    filterDropdownProps: {
      onOpenChange(open) {
        if (open) setTimeout(() => searchInput.current?.select(), 100);
      },
    },
    render: (text) =>
      searchedColumn === dataIndex ? (
        <Highlighter
          highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
          searchWords={[searchText]}
          autoEscape
          textToHighlight={text?.toString() || ""}
        />
      ) : (
        text
      ),
  });

  const renderColumns = () => [
    {
      title: "No.",
      dataIndex: "index",
      key: "index",
      align: "center",
      render: (_, __, index) => index + 1,
    },
    {
      title: "Konsentrasi Keahlian Sekolah",
      dataIndex: "namaKonsentrasiSekolah",
      key: "namaKonsentrasiSekolah",
      align: "center",
      ...getColumnSearchProps("namaKonsentrasiSekolah"),
      sorter: (a, b) =>
        a.namaKonsentrasiSekolah.localeCompare(b.namaKonsentrasiSekolah),
    },
    {
      title: "Sekolah",
      dataIndex: ["school", "nameSchool"],
      key: "nameSchool",
      align: "center",
      ...getColumnSearchProps("nameSchool", "school.nameSchool"),
      sorter: (a, b) => a.school.nameSchool.localeCompare(b.school.nameSchool),
    },
    {
      title: "Konsentrasi Keahlian",
      dataIndex: ["konsentrasiKeahlian", "konsentrasi"],
      key: "konsentrasi",
      align: "center",
      ...getColumnSearchProps("konsentrasi", "konsentrasiKeahlian.konsentrasi"),
      sorter: (a, b) =>
        a.konsentrasiKeahlian.konsentrasi.localeCompare(
          b.konsentrasiKeahlian.konsentrasi
        ),
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
      rowKey="idKonsentrasiSekolah"
      dataSource={filteredData}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button
          type="primary"
          onClick={() => setAddKonsentrasiSekolahModalVisible(true)}
        >
          Tambahkan Konsentrasi Keahlian Sekolah
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
        title="Manajemen Analisa Konsentrasi Keahlian Sekolah"
        source="Di sini, Anda dapat mengelola Analisa Konsentrasi Keahlian Sekolah di sistem."
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
                placeholder="Cari konsentrasi keahlian..."
                allowClear
                enterButton
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ width: 300 }}
              />
            </Col>
          </Row>

          {/* Tabel */}
          {renderTable()}
          {/* Modal Tambah Konsentrasi Keahlian Sekolah */}
          <AddKonsentrasiSekolahForm
            wrappedComponentRef={addKonsentrasiSekolahFormRef}
            visible={addKonsentrasiSekolahModalVisible}
            confirmLoading={addKonsentrasiSekolahModalLoading}
            onCancel={handleCancel}
            onOk={handleAddOk}
          />

          {/* Modal Edit Konsentrasi Keahlian Sekolah */}
          <EditKonsentrasiSekolahForm
            wrappedComponentRef={editKonsentrasiSekolahFormRef}
            currentRowData={currentRowData}
            visible={editKonsentrasiSekolahModalVisible}
            confirmLoading={editKonsentrasiSekolahModalLoading}
            onCancel={handleCancel}
            onOk={handleEditOk}
          />
        </Card>
      )}
    </div>
  );
};

export default KonsentrasiSekolah;
