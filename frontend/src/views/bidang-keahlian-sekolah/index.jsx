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
  getBidangSekolah,
  deleteBidangSekolah,
  editBidangSekolah,
  addBidangSekolah,
} from "@/api/bidangKeahlianSekolah";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import TypingCard from "@/components/TypingCard";
import EditBidangSekolahForm from "./forms/edit-bidang-keahlian-sekolah-form";
import AddBidangSekolahForm from "./forms/add-bidang-keahlian-sekolah-form";
import { reqUserInfo, getUserById } from "@/api/user";
import { read, utils } from "xlsx";

const { Column } = Table;

const BidangSekolah = () => {
  const [bidangSekolah, setBidangSekolah] = useState([]);
  const [editBidangSekolahModalVisible, setEditBidangSekolahModalVisible] =
    useState(false);
  const [editBidangSekolahModalLoading, setEditBidangSekolahModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addBidangSekolahModalVisible, setAddBidangSekolahModalVisible] =
    useState(false);
  const [addBidangSekolahModalLoading, setAddBidangSekolahModalLoading] =
    useState(false);
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

  const editBidangSekolahFormRef = useRef();
  const addBidangSekolahFormRef = useRef();

  useEffect(() => {
    const initializeData = async () => {
      const userInfoResponse = await reqUserInfo();
      const { id: userId } = userInfoResponse.data;

      await getUserInfoJson(userId);
    };

    initializeData();
  }, []);

  const fetchBidangSekolah = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getBidangSekolah();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const filteredContent = content.filter(
          (item) => item.school?.idSchool === userIdJson
        );
        setBidangSekolah(filteredContent);
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
      fetchBidangSekolah();
    }
  }, [userIdJson, fetchBidangSekolah]);

  const filteredData = bidangSekolah.filter((item) => {
    const query = searchQuery.toLowerCase();
    return (
      (item?.namaBidangSekolah?.toLowerCase() || "").includes(query) ||
      (item?.nameSchool?.toLowerCase() || "").includes(query) ||
      (item?.bidang?.toLowerCase() || "").includes(query)
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
    const { idBidangSekolah } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteBidangSekolah({ idBidangSekolah });
          message.success("Berhasil dihapus");
          fetchBidangSekolah();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditOk = async (values) => {
    setEditBidangSekolahModalLoading(true);
    try {
      const updatedValues = {
        idBidangSekolah: values.idBidangSekolah,
        namaBidangSekolah: values.namaBidangSekolah,
        idSekolah: values.idSchool,
        idBidangKeahlian: values.id,
      };

      console.log("respon data", updatedValues);
      await editBidangSekolah(updatedValues, currentRowData.idBidangSekolah);
      setEditBidangSekolahModalVisible(false);
      setEditBidangSekolahModalLoading(false);
      message.success("Berhasil mengubah");
      fetchBidangSekolah();
    } catch (error) {
      setEditBidangSekolahModalLoading(false);
      message.error("Gagal mengubah: " + error.message);
    }
  };

  const handleEdit = (row) => {
    setCurrentRowData({ ...row });
    setEditBidangSekolahModalVisible(true);
  };

  const handleCancel = () => {
    setEditBidangSekolahModalVisible(false);
    setAddBidangSekolahModalVisible(false);
  };

  const handleAdd = () => {
    setAddBidangSekolahModalVisible(true);
  };
  const handleAddOk = async (values) => {
    setAddBidangSekolahModalLoading(true);
    try {
      const updatedValues = {
        idBidangSekolah: null,
        namaBidangSekolah: values.namaBidangSekolah,
        idSekolah: values.idSchool,
        idBidangKeahlian: values.id,
      };
      console.log("respon data", updatedValues);
      await addBidangSekolah(updatedValues);
      setAddBidangSekolahModalLoading(false);
      setAddBidangSekolahModalLoading(false);
      message.success("Berhasil menambahkan");
      fetchBidangSekolah();
    } catch (error) {
      setAddBidangSekolahModalLoading(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddBidangSekolahModalVisible(false);
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
    getBidangSekolah();
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
      title: "Bidang Keahlian Sekolah",
      dataIndex: "namaBidangSekolah",
      key: "namaBidangSekolah",
      align: "center",
      ...getColumnSearchProps("namaBidangSekolah"),
      sorter: (a, b) => a.namaBidangSekolah.localeCompare(b.namaBidangSekolah),
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
      title: "Bidang Keahlian",
      dataIndex: ["bidangKeahlian", "bidang"],
      key: "bidang",
      align: "center",
      ...getColumnSearchProps("bidang", "bidangKeahlian.bidang"),
      sorter: (a, b) =>
        a.bidangKeahlian.bidang.localeCompare(b.bidangKeahlian.bidang),
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
      rowKey="idBidangSekolah"
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
          onClick={() => setAddBidangSekolahModalVisible(true)}
        >
          Tambahkan Bidang Keahlian Sekolah
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
        title="Manajemen Analisa Bidang Keahlian Sekolah"
        source="Di sini, Anda dapat mengelola Analisa Bidang Keahlian Sekolah di sistem."
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
                placeholder="Cari bidang keahlian..."
                allowClear
                enterButton
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ width: 300 }}
              />
            </Col>
          </Row>

          {/* Tabel */}
          {renderTable()}
        </Card>
      )}

      {/* Modal Tambah Bidang Keahlian Sekolah */}
      <AddBidangSekolahForm
        wrappedComponentRef={addBidangSekolahFormRef}
        visible={addBidangSekolahModalVisible}
        confirmLoading={addBidangSekolahModalLoading}
        onCancel={handleCancel}
        onOk={handleAddOk}
      />

      {/* Modal Edit Bidang Keahlian Sekolah */}
      <EditBidangSekolahForm
        wrappedComponentRef={editBidangSekolahFormRef}
        currentRowData={currentRowData}
        visible={editBidangSekolahModalVisible}
        confirmLoading={editBidangSekolahModalLoading}
        onCancel={handleCancel}
        onOk={handleEditOk}
      />
    </div>
  );
};

export default BidangSekolah;
