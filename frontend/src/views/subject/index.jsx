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
  Select,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
  PlusOutlined,
  SearchOutlined,
} from "@ant-design/icons";
import { getMapel, deleteMapel, addMapel, editMapel } from "@/api/mapel";
import { getKelas } from "@/api/kelas";
import { getSemester } from "@/api/semester";
import { getTahunAjaran } from "@/api/tahun-ajaran";
import TypingCard from "@/components/TypingCard";
import AddMapelForm from "./forms/add-mapel-form";
import EditMapelForm from "./forms/edit-mapel-form";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { reqUserInfo, getUserById } from "@/api/user";
import { render } from "less";

const Mapel = () => {
  const [mapel, setMapel] = useState([]);
  const [addMapelModalVisible, setAddMapelModalVisible] = useState(false);
  const [addMapelModalLoading, setAddMapelModalLoading] = useState(false);
  const [editMapelModalVisible, setEditMapelModalVisible] = useState(false);
  const [editMapelModalLoading, setEditMapelModalLoading] = useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [searchKeyword, setSearchKeyword] = useState("");
  const [userIdJson, setUserIdJson] = useState("");
  const [loading, setLoading] = useState(true);
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const [searchQuery, setSearchQuery] = useState("");

  const [selectedTahunAjaran, setSelectedTahunAjaran] = useState(null);
  const [selectedKelas, setSelectedKelas] = useState(null);
  const [selectedSemester, setSelectedSemester] = useState(null);
  const [tahunAjaranList, setTahunAjaranList] = useState([]);
  const [kelasList, setKelasList] = useState([]);
  const [semesterList, setSemesterList] = useState([]);

  const [filteredMapel, setFilteredMapel] = useState([]);

  // Fungsi Helper Table Search
  const { getColumnSearchProps } = useTableSearch();

  const editMapelFormRef = useRef();
  const addMapelFormRef = useRef();

  const fetchMapel = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getMapel();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setMapel(content);
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
    fetchMapel();
  }, [fetchMapel]);

  useEffect(() => {
    const fetchTahunAjaranAndKelasAndSemester = async () => {
      setLoading(true);
      try {
        const tahunAjaranResult = await getTahunAjaran();
        const kelasResult = await getKelas();
        const semesterResult = await getSemester();

        if (tahunAjaranResult.data.statusCode === 200) {
          let tahunAjaranContent = tahunAjaranResult.data.content || [];
          // Urutkan berdasarkan createdAt ASCENDING
          tahunAjaranContent.sort(
            (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
          );
          setTahunAjaranList(tahunAjaranContent);

          // JANGAN setSelectedTahunAjaran di sini
        }

        if (kelasResult.data.statusCode === 200) {
          let kelasContent = kelasResult.data.content || [];
          kelasContent.sort(
            (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
          );
          setKelasList(kelasContent);

          // JANGAN setSelectedKelas di sini
        }

        if (semesterResult.data.statusCode === 200) {
          let semesterContent = semesterResult.data.content || [];
          semesterContent.sort(
            (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
          );
          setSemesterList(semesterContent);

          // JANGAN setSelectedSemester di sini
        }
      } catch (error) {
        message.error(
          "Gagal mengambil data kelas atau semester: " + error.message
        );
      } finally {
        setLoading(false);
      }
    };

    fetchTahunAjaranAndKelasAndSemester();
  }, []);

  const handleDeleteMapel = (row) => {
    const { idMapel } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          await deleteMapel({ idMapel });
          message.success("Berhasil dihapus");
          fetchMapel();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleEditMapel = (row) => {
    setCurrentRowData({ ...row });
    setEditMapelModalVisible(true);
  };

  const handleAddMapel = () => {
    setAddMapelModalVisible(true);
  };

  const handleAddMapelOk = async (values) => {
    setAddMapelModalLoading(true);
    try {
      const updatedData = {
        idMapel: null,
        name: values.name,
        idSekolah: values.idSchool,
        idTahun: values.idTahun,
        idKelas: values.idKelas,
        idSemester: values.idSemester,
      };
      await addMapel(updatedData);
      setAddMapelModalVisible(false);
      message.success("Berhasil menambahkan");
      fetchMapel();
    } catch (error) {
      setAddMapelModalVisible(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddMapelModalLoading(false);
      fetchMapel();
    }
  };

  const handleEditMapelOk = async (values) => {
    setEditMapelModalLoading(true);
    try {
      const updatedData = {
        idMapel: values.idMapel,
        name: values.name,
        idSekolah: values.idSchool,
        idTahun: values.idTahun,
        idKelas: values.idKelas,
        idSemester: values.idSemester,
      };
      await editMapel(updatedData, currentRowData.idMapel);
      setEditMapelModalVisible(false);
      setEditMapelModalLoading(false);
      message.success("Berhasil mengedit");
      fetchMapel();
    } catch (error) {
      setEditMapelModalVisible(false);
      message.error("Gagal mengedit: " + error.message);
    } finally {
      setEditMapelModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddMapelModalVisible(false);
    setEditMapelModalVisible(false);
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getMapel();
  };

  const getFilteredMapelList = () => {
    return mapel.filter((item) => {
      const matchTahunAjaran = selectedTahunAjaran
        ? item.tahunAjaran?.idTahun === selectedTahunAjaran
        : true;
      const matchKelas = selectedKelas
        ? item.kelas?.idKelas === selectedKelas
        : true;
      const matchSemester = selectedSemester
        ? item.semester?.idSemester === selectedSemester
        : true;

      // Global search filter
      const matchSearch = searchQuery
        ? item.name?.toLowerCase().includes(searchQuery.toLowerCase()) ||
          item.tahunAjaran?.tahunAjaran
            ?.toLowerCase()
            .includes(searchQuery.toLowerCase()) ||
          item.semester?.namaSemester
            ?.toLowerCase()
            .includes(searchQuery.toLowerCase()) ||
          item.kelas?.namaKelas
            ?.toLowerCase()
            .includes(searchQuery.toLowerCase())
        : true;

      return matchTahunAjaran && matchKelas && matchSemester && matchSearch;
    });
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
      dataIndex: ["tahunAjaran", "tahunAjaran"],
      key: "tahunAjaran",
      align: "center",
      ...getColumnSearchProps("tahunAjaran", "tahunAjaran.tahunAjaran"),
      sorter: (a, b) =>
        a.tahunAjaran.tahunAjaran.localeCompare(b.tahunAjaran.tahunAjaran),
    },
    {
      title: "Semester",
      dataIndex: ["semester", "namaSemester"],
      key: "namaSemester",
      align: "center",
      ...getColumnSearchProps("namaSemester", "semester.namaSemester"),
      sorter: (a, b) =>
        a.semester.namaSemester.localeCompare(b.semester.namaSemester),
    },
    {
      title: "Kelas",
      dataIndex: ["kelas", "namaKelas"],
      key: "namaKelas",
      align: "center",
      ...getColumnSearchProps("namaKelas", "kelas.namaKelas"),
      sorter: (a, b) => a.kelas.namaKelas.localeCompare(b.kelas.namaKelas),
    },
    {
      title: "Nama Mapel",
      dataIndex: "name",
      key: "name",
      align: "center",
      ...getColumnSearchProps("name"),
      sorter: (a, b) => a.name.localeCompare(b.name),
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
            onClick={() => handleEditMapel(row)}
          />
          <Divider type="vertical" />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteMapel(row)}
          />
        </span>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="idMapel"
      dataSource={getFilteredMapelList()}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button type="primary" onClick={() => setAddMapelModalVisible(true)}>
          Tambahkan Mapel
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
      <Col>
        <Select
          placeholder="Pilih Tahun Ajaran"
          value={selectedTahunAjaran}
          style={{ width: 150 }}
          onChange={(value) => setSelectedTahunAjaran(value)}
          allowClear
        >
          {tahunAjaranList.map((tahunAjaran) => (
            <Select.Option
              key={tahunAjaran.idTahun}
              value={tahunAjaran.idTahun}
            >
              {tahunAjaran.tahunAjaran}
            </Select.Option>
          ))}
        </Select>
      </Col>
      <Col>
        <Select
          placeholder="Pilih Kelas"
          value={selectedKelas}
          style={{ width: 150 }}
          onChange={(value) => setSelectedKelas(value)}
          allowClear
        >
          {kelasList.map((kelas) => (
            <Select.Option key={kelas.idKelas} value={kelas.idKelas}>
              {kelas.namaKelas}
            </Select.Option>
          ))}
        </Select>
      </Col>
      <Col>
        <Select
          placeholder="Pilih Semester"
          value={selectedSemester}
          style={{ width: 150 }}
          onChange={(value) => setSelectedSemester(value)}
          allowClear
        >
          {semesterList.map((semester) => (
            <Select.Option
              key={semester.idSemester}
              value={semester.idSemester}
            >
              {semester.namaSemester}
            </Select.Option>
          ))}
        </Select>
      </Col>
    </Row>
  );

  return (
    <div className="app-container">
      <TypingCard
        title="Manajemen Mapel"
        source="Di sini, Anda dapat mengelola mapel di sistem."
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
                placeholder="Cari tahun ajaran, semester, kelas, atau nama mapel..."
                allowClear
                enterButton
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onSearch={(value) => setSearchQuery(value)}
                style={{ width: 400 }}
              />
            </Col>
          </Row>

          {/* Tabel */}
          {renderTable()}

          <AddMapelForm
            wrappedComponentRef={addMapelFormRef}
            visible={addMapelModalVisible}
            confirmLoading={addMapelModalLoading}
            onCancel={handleCancel}
            onOk={handleAddMapelOk}
          />

          <EditMapelForm
            wrappedComponentRef={editMapelFormRef}
            currentRowData={currentRowData}
            visible={editMapelModalVisible}
            confirmLoading={editMapelModalLoading}
            onCancel={handleCancel}
            onOk={handleEditMapelOk}
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

export default Mapel;
