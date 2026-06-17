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
  getProgramSekolah,
  deleteProgramSekolah,
  editProgramSekolah,
  addProgramSekolah,
} from "@/api/programKeahlianSekolah";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import TypingCard from "@/components/TypingCard";
import EditProgramSekolahForm from "./forms/edit-program-keahlian-sekolah-form";
import AddProgramSekolahForm from "./forms/add-program-keahlian-sekolah-form";
import { reqUserInfo, getUserById } from "@/api/user";
import { read, utils } from "xlsx";

const { Column } = Table;

const ProgramSekolah = () => {
  const [programSekolah, setProgramSekolah] = useState([]);
  const [editProgramSekolahModalVisible, setEditProgramSekolahModalVisible] =
    useState(false);
  const [editProgramSekolahModalLoading, setEditProgramSekolahModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addProgramSekolahModalVisible, setAddProgramSekolahModalVisible] =
    useState(false);
  const [addProgramSekolahModalLoading, setAddProgramSekolahModalLoading] =
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

  const editProgramSekolahFormRef = useRef();
  const addProgramSekolahFormRef = useRef();

  useEffect(() => {
    const initializeData = async () => {
      const userInfoResponse = await reqUserInfo();
      const { id: userId } = userInfoResponse.data;

      await getUserInfoJson(userId);
    };

    initializeData();
  }, []);

  const fetchProgramSekolah = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getProgramSekolah();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const filteredContent = content.filter(
          (item) => item.school?.idSchool === userIdJson
        );
        setProgramSekolah(filteredContent);
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
      fetchProgramSekolah();
    }
  }, [userIdJson, fetchProgramSekolah]);

  const filteredData = programSekolah.filter((item) => {
    const query = searchQuery.toLowerCase();
    return (
      (item?.namaProgramSekolah?.toLowerCase() || "").includes(query) ||
      (item?.nameSchool?.toLowerCase() || "").includes(query) ||
      (item?.program?.toLowerCase() || "").includes(query)
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
    const { idProgramSekolah } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteProgramSekolah({ idProgramSekolah });
          message.success("Berhasil dihapus");
          fetchProgramSekolah();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditOk = async (values) => {
    setEditProgramSekolahModalLoading(true);
    try {
      const updatedValues = {
        idProgramSekolah: values.idProgramSekolah,
        namaProgramSekolah: values.namaProgramSekolah,
        idSekolah: values.idSchool,
        idProgramKeahlian: values.id,
      };

      console.log("respon data", updatedValues);
      await editProgramSekolah(updatedValues, currentRowData.idProgramSekolah);
      setEditProgramSekolahModalVisible(false);
      setEditProgramSekolahModalLoading(false);
      message.success("Berhasil mengubah");
      fetchProgramSekolah();
    } catch (error) {
      setEditProgramSekolahModalLoading(false);
      message.error("Gagal mengubah: " + error.message);
    }
  };

  const handleEdit = (row) => {
    setCurrentRowData({ ...row });
    setEditProgramSekolahModalVisible(true);
  };

  const handleCancel = () => {
    setEditProgramSekolahModalVisible(false);
    setAddProgramSekolahModalVisible(false);
  };

  const handleAdd = () => {
    setAddProgramSekolahModalVisible(true);
  };
  const handleAddOk = async (values) => {
    setAddProgramSekolahModalLoading(true);
    try {
      const updatedValues = {
        idProgramSekolah: null,
        namaProgramSekolah: values.namaProgramSekolah,
        idSekolah: values.idSchool,
        idProgramKeahlian: values.id,
      };
      console.log("respon data", updatedValues);
      await addProgramSekolah(updatedValues);
      setAddProgramSekolahModalLoading(false);
      setAddProgramSekolahModalLoading(false);
      message.success("Berhasil menambahkan");
      fetchProgramSekolah();
    } catch (error) {
      setAddProgramSekolahModalLoading(false);
      if (error.response?.status === 400) {
        message.error("Data sudah ada" + error.response.data.message);
      }
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddProgramSekolahModalVisible(false);
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
    getProgramSekolah();
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
      title: "Program Keahlian Sekolah",
      dataIndex: "namaProgramSekolah",
      key: "namaProgramSekolah",
      align: "center",
      ...getColumnSearchProps("namaProgramSekolah"),
      sorter: (a, b) =>
        a.namaProgramSekolah.localeCompare(b.namaProgramSekolah),
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
      title: "Program Keahlian",
      dataIndex: ["programKeahlian", "program"],
      key: "program",
      align: "center",
      ...getColumnSearchProps("program", "programKeahlian.program"),
      sorter: (a, b) =>
        a.programKeahlian.program.localeCompare(b.programKeahlian.program),
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
      rowKey="idProgramSekolah"
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
          onClick={() => setAddProgramSekolahModalVisible(true)}
        >
          Tambahkan Program Keahlian Sekolah
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
        title="Manajemen Analisa Program Keahlian Sekolah"
        source="Di sini, Anda dapat mengelola Analisa Program Keahlian Sekolah di sistem."
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
                placeholder="Cari program keahlian..."
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

      {/* Modal Tambah Program Keahlian Sekolah */}
      <AddProgramSekolahForm
        wrappedComponentRef={addProgramSekolahFormRef}
        visible={addProgramSekolahModalVisible}
        confirmLoading={addProgramSekolahModalLoading}
        onCancel={handleCancel}
        onOk={handleAddOk}
      />

      {/* Modal Edit Program Keahlian Sekolah */}
      <EditProgramSekolahForm
        wrappedComponentRef={editProgramSekolahFormRef}
        currentRowData={currentRowData}
        visible={editProgramSekolahModalVisible}
        confirmLoading={editProgramSekolahModalLoading}
        onCancel={handleCancel}
        onOk={handleEditOk}
      />
    </div>
  );
};

export default ProgramSekolah;
