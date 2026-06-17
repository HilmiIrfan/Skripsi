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
  Steps,
  Tag,
  Alert,
  Select,
} from "antd";
import {
  UploadOutlined,
  EditOutlined,
  DeleteOutlined,
} from "@ant-design/icons";
import {
  fetchInitialData,
  getAvailableSemesters,
  getAvailableKelas,
  getAvailableMapels,
  filterData,
  renderSelectionSteps,
  renderActiveFilters,
} from "@/helper/mapelSelectionHelper.jsx";
import { getACP, deleteACP, editACP, addACP } from "@/api/acp";
import { getATP, deleteATP } from "@/api/atp";
import TypingCard from "@/components/TypingCard";
import EditACPForm from "./forms/edit-acp-form";
import AddACPForm from "./forms/add-acp-form";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import { reqUserInfo, getUserById } from "@/api/user";
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { read, utils } from "xlsx";
import { set } from "nprogress";

const ACP = () => {
  const [acp, setACP] = useState([]);
  const [editACPModalVisible, setEditACPModalVisible] = useState(false);
  const [editACPModalLoading, setEditACPModalLoading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addACPModalVisible, setAddACPModalVisible] = useState(false);
  const [addACPModalLoading, setAddACPModalLoading] = useState(false);
  const [importedData, setImportedData] = useState([]);
  const [columnTitles, setColumnTitles] = useState([]);
  const [fileName, setFileName] = useState("");
  const [uploading, setUploading] = useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [columnMapping, setColumnMapping] = useState({});
  const [tableLoading, setTableLoading] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [userIdJson, setUserIdJson] = useState("");
  const [loading, setLoading] = useState(true);
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const [searchQuery, setSearchQuery] = useState("");

  // Fungsi dari Helper
  const [currentStep, setCurrentStep] = useState(1);
  const [selectedTahunAjaran, setSelectedTahunAjaran] = useState(null);
  const [selectedKelas, setSelectedKelas] = useState(null);
  const [selectedSemester, setSelectedSemester] = useState(null);
  const [selectedMapel, setSelectedMapel] = useState(null);
  const [kelasList, setKelasList] = useState([]);
  const [semesterList, setSemesterList] = useState([]);
  const [tahunAjaranList, setTahunAjaranList] = useState([]);
  const [allMapelList, setAllMapelList] = useState([]);
  const [filteredMapelList, setFilteredMapelList] = useState([]);
  const [showTable, setShowTable] = useState(false);

  // Fungsi Helper Table Search
  const { getColumnSearchProps } = useTableSearch();

  const editACPFormRef = useRef(null);
  const addACPFormRef = useRef(null);

  const { Step } = Steps;

  const fetchACP = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getACP();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setACP(content);
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
    fetchACP();
  }, [fetchACP]);

  useEffect(() => {
    const loadData = async () => {
      const data = await fetchInitialData();
      setTahunAjaranList(data.tahunAjaranList);
      setSemesterList(data.semesterList);
      setKelasList(data.kelasList);
      setAllMapelList(data.allMapelList);
      setLoading(false);
    };
    loadData();
  }, []);

  // Hitung opsi yang tersedia
  const availableSemesters = getAvailableSemesters(
    selectedTahunAjaran,
    semesterList,
    allMapelList
  );
  const availableKelas = getAvailableKelas(
    selectedTahunAjaran,
    selectedSemester,
    kelasList,
    allMapelList
  );
  const availableMapels = getAvailableMapels(
    selectedTahunAjaran,
    selectedSemester,
    selectedKelas,
    allMapelList
  );

  // Handler functions
  const handleTahunAjaranChange = (value) => {
    setSelectedTahunAjaran(value);
    setSelectedSemester(null);
    setSelectedKelas(null);
    setSelectedMapel(null);
    setCurrentStep(2);
  };

  const handleSemesterChange = (value) => {
    setSelectedSemester(value);
    setSelectedKelas(null);
    setSelectedMapel(null);
    setCurrentStep(3);
  };

  const handleKelasChange = (value) => {
    setSelectedKelas(value);
    setSelectedMapel(null);
    setCurrentStep(4);
  };

  const handleMapelChange = (value) => {
    setSelectedMapel(value);
    setShowTable(true);
  };

  const handleStepBack = (step) => {
    setCurrentStep(step);
  };

  const handleBackClick = () => {
    setSelectedMapel(null);
    setCurrentStep(4);
  };

  // Filter data
  const filteredData = filterData(
    acp,
    selectedTahunAjaran,
    selectedSemester,
    selectedKelas,
    selectedMapel
  );

  const getFilteredACPList = () => {
    return filteredData.filter((item) => {
      // Global search filter
      const matchSearch = searchQuery
        ? item.namaAcp?.toLowerCase().includes(searchQuery.toLowerCase()) ||
          item.elemen?.namaElemen
            ?.toLowerCase()
            .includes(searchQuery.toLowerCase()) ||
          item.konsentrasiKeahlianSekolah?.namaKonsentrasiSekolah
            ?.toLowerCase()
            .includes(searchQuery.toLowerCase())
        : true;

      return matchSearch;
    });
  };

  const handleEditACP = (row) => {
    setCurrentRowData({ ...row });
    setEditACPModalVisible(true);
  };

  const handleCancel = () => {
    setEditACPModalVisible(false);
    setAddACPModalVisible(false);
  };

  const handleAddACP = () => {
    setAddACPModalVisible(true);
  };

  const handleDelete = (row) => {
    const { idAcp, namaAcp } = row;
    Modal.confirm({
      title: "Konfirmasi Cascade Delete",
      content: (
        <div>
          <p>
            <strong>⚠️ PERHATIAN: Penghapusan Berantai</strong>
          </p>
          <p>
            Menghapus ACP{" "}
            <strong>&quot;{namaAcp?.substring(0, 80)}...&quot;</strong> akan
            otomatis menghapus:
          </p>
          <ul>
            <li>✅ Semua ATP yang terkait dengan ACP ini</li>
            <li>❌ Elemen tetap tidak akan dihapus</li>
          </ul>
          <p>
            <strong>Apakah Anda yakin ingin melanjutkan?</strong>
          </p>
        </div>
      ),
      okText: "Ya, Hapus ATP Terkait",
      okType: "danger",
      cancelText: "Batal",
      width: 500,
      onOk: async () => {
        try {
          message.loading("Menghapus ATP terkait...", 0);

          // Step 1: Get all ATP related to this ACP
          console.log("🔍 Step 1: Mencari ATP terkait dengan ACP ID:", idAcp);
          const atpResponse = await getATP();
          const relatedATPs = atpResponse.data.content.filter(
            (atp) => atp.acp && atp.acp.idAcp === idAcp
          );
          console.log("📊 Found ATP to delete:", relatedATPs.length);

          // Step 2: Delete all related ATPs first
          for (const atp of relatedATPs) {
            console.log("🗑️ Deleting ATP:", atp.namaAtp);
            await deleteATP({ idAtp: atp.idAtp });
          }

          // Step 3: Delete the ACP
          console.log("🗑️ Deleting ACP:", namaAcp?.substring(0, 50) + "...");
          await deleteACP({ idAcp });

          message.destroy();
          message.success({
            content: `Berhasil menghapus ACP beserta ${relatedATPs.length} ATP terkait`,
            duration: 5,
          });

          fetchACP();
        } catch (error) {
          message.destroy();
          message.error("Gagal menghapus: " + error.message);
          console.error("Cascade delete error:", error);
        }
      },
    });
  };

  const handleAddOk = async (values) => {
    setAddACPModalLoading(true);
    try {
      const updatedValues = {
        idAcp: null,
        namaAcp: values.namaAcp,
        idElemen: values.idElemen,
        idKonsentrasiSekolah: values.idKonsentrasiSekolah,
        idKelas: values.idKelas,
        idTahun: values.idTahun,
        idSemester: values.idSemester,
        idMapel: values.idMapel,
        idSchool: values.idSchool,
      };
      console.log("respon data", updatedValues);
      await addACP(updatedValues);
      setAddACPModalVisible(false);
      setAddACPModalLoading(false);
      message.success("Berhasil menambahkan");
      fetchACP();
    } catch (error) {
      setAddACPModalLoading(false);
      message.error("Gagal menambahkan: " + error.message);
    }
  };

  const handleEditOk = async (values) => {
    setEditACPModalLoading(true);
    try {
      const updatedValues = {
        idAcp: values.idAcp,
        namaAcp: values.namaAcp,
        idElemen: values.idElemen,
        idKonsentrasiSekolah: values.idKonsentrasiSekolah,
        idKelas: values.idKelas,
        idTahun: values.idTahun,
        idSemester: values.idSemester,
        idMapel: values.idMapel,
        idSchool: values.idSchool,
      };

      console.log("respon data", updatedValues);
      await editACP(updatedValues, currentRowData.idAcp);
      setEditACPModalVisible(false);
      setEditACPModalLoading(false);
      message.success("Berhasil mengubah");
      fetchACP();
    } catch (error) {
      setEditACPModalLoading(false);
      message.error("Gagal mengubah: " + error.message);
    }
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getACP();
  };

  const renderColumns = () => [
    {
      title: "No.",
      dataIndex: "index",
      key: "index",
      align: "center",
      render: (_, __, index) => index + 1,
    },
    {
      title: "Capaian Pembelajaran",
      dataIndex: "namaAcp",
      key: "namaAcp",
      align: "center",
      ...getColumnSearchProps("namaAcp"),
      sorter: (a, b) => a.namaAcp.localeCompare(b.namaAcp),
    },
    {
      title: "Elemen",
      dataIndex: ["elemen", "namaElemen"],
      key: "namaElemen",
      align: "center",
      ...getColumnSearchProps("namaElemen", "elemen.namaElemen"),
      sorter: (a, b) => a.elemen.namaElemen.localeCompare(b.elemen.namaElemen),
    },
    {
      title: "Konsentrasi Keahlian Sekolah",
      dataIndex: ["konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah"],
      key: "namaKonsentrasiSekolah",
      align: "center",
      ...getColumnSearchProps(
        "namaKonsentrasiSekolah",
        "konsentrasiKeahlianSekolah.namaKonsentrasiSekolah"
      ),
      sorter: (a, b) =>
        a.konsentrasiKeahlianSekolah.namaKonsentrasiSekolah.localeCompare(
          b.konsentrasiKeahlianSekolah.namaKonsentrasiSekolah
        ),
    },
    {
      title: "Operasi",
      key: "action",
      align: "center",
      render: (_, row) => (
        <Space>
          <Button
            type="default"
            shape="circle"
            icon={<EditOutlined />}
            onClick={() => handleEditACP(row)}
          />
          <Button
            type="default"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(row)}
          />
        </Space>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="idAcp"
      dataSource={getFilteredACPList()}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  return (
    <div className="app-container">
      <TypingCard
        title="Manajemen Analisa Capaian Pembelajaran"
        source="Di sini, Anda dapat mengelola Analisa Capaian Pembelajaran di sistem."
      />
      <br />

      {loading ? (
        <Card>
          <Skeleton active paragraph={{ rows: 10 }} />
        </Card>
      ) : (
        <>
          {!selectedMapel ? (
            renderSelectionSteps({
              currentStep,
              tahunAjaranList,
              semesterList,
              kelasList,
              availableSemesters,
              availableKelas,
              availableMapels,
              selectedTahunAjaran,
              selectedSemester,
              selectedKelas,
              onTahunAjaranChange: handleTahunAjaranChange,
              onSemesterChange: handleSemesterChange,
              onKelasChange: handleKelasChange,
              onMapelChange: handleMapelChange,
              onStepBack: handleStepBack,
            })
          ) : (
            <>
              {renderActiveFilters({
                tahunAjaranList,
                semesterList,
                kelasList,
                filteredMapelList,
                selectedTahunAjaran,
                selectedSemester,
                selectedKelas,
                selectedMapel,
                onBackClick: handleBackClick,
              })}

              {/* Tabel Data */}
              <Card style={{ overflowX: "scroll" }}>
                {/* Baris untuk tombol dan pencarian */}
                <Row
                  justify="space-between"
                  align="middle"
                  style={{ marginBottom: 16 }}
                >
                  {/* Tombol Tambah */}
                  <Col>
                    <Button
                      type="primary"
                      onClick={() => setAddACPModalVisible(true)}
                    >
                      Tambahkan ACP
                    </Button>
                  </Col>

                  {/* Kolom Pencarian */}
                  <Col>
                    <Input.Search
                      key="search"
                      placeholder="Cari capaian pembelajaran, elemen, atau konsentrasi keahlian..."
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
              </Card>
            </>
          )}

          {/* Modal Forms */}
          <AddACPForm
            wrappedComponentRef={addACPFormRef}
            visible={addACPModalVisible}
            confirmLoading={addACPModalLoading}
            onCancel={handleCancel}
            onOk={handleAddOk}
          />

          <EditACPForm
            wrappedComponentRef={editACPFormRef}
            currentRowData={currentRowData}
            visible={editACPModalVisible}
            confirmLoading={editACPModalLoading}
            onCancel={handleCancel}
            onOk={handleEditOk}
          />
        </>
      )}
    </div>
  );
};

export default ACP;
