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
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
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
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { getElemen, deleteElemen, addElemen, editElemen } from "@/api/elemen";
import { getACP, deleteACP } from "@/api/acp";
import { getATP, deleteATP } from "@/api/atp";
import TypingCard from "@/components/TypingCard";
import AddElemenForm from "./forms/add-elemen-form";
import EditElemenForm from "./forms/edit-elemen-form";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import { reqUserInfo, getUserById } from "@/api/user";

const Elemen = () => {
  const [elemen, setElemen] = useState([]);
  const [addModalVisible, setAddModalVisible] = useState(false);
  const [addModalLoading, setAddModalLoading] = useState(false);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [editModalLoading, setEditModalLoading] = useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
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

  const addFormRef = useRef(null);
  const editFormRef = useRef(null);

  const { Step } = Steps;

  const fetchELemen = useCallback(async () => {
    try {
      setLoading(true);
      const result = await getElemen();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setElemen(content);
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
    fetchELemen();
  }, [fetchELemen]);

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
    elemen,
    selectedTahunAjaran,
    selectedSemester,
    selectedKelas,
    selectedMapel
  );

  const getFilteredElemenList = () => {
    return filteredData.filter((item) => {
      // Global search filter
      const matchSearch = searchQuery
        ? item.namaElemen?.toLowerCase().includes(searchQuery.toLowerCase()) ||
          item.mapel?.name?.toLowerCase().includes(searchQuery.toLowerCase()) ||
          item.konsentrasiKeahlianSekolah?.namaKonsentrasiSekolah
            ?.toLowerCase()
            .includes(searchQuery.toLowerCase())
        : true;

      return matchSearch;
    });
  };

  const handleDelete = (row) => {
    const { idElemen, namaElemen } = row;
    Modal.confirm({
      title: "Konfirmasi Cascade Delete",
      content: (
        <div>
          <p>
            <strong>⚠️ PERHATIAN: Penghapusan Berantai</strong>
          </p>
          <p>
            Menghapus Elemen <strong>&quot;{namaElemen}&quot;</strong> akan
            otomatis menghapus:
          </p>
          <ul>
            <li>✅ Semua ACP yang terkait dengan Elemen ini</li>
            <li>✅ Semua ATP yang terkait dengan ACP tersebut</li>
          </ul>
          <p>
            <strong>Apakah Anda yakin ingin melanjutkan?</strong>
          </p>
        </div>
      ),
      okText: "Ya, Hapus Semua",
      okType: "danger",
      cancelText: "Batal",
      width: 500,
      onOk: async () => {
        try {
          message.loading("Menghapus data terkait...", 0);

          // Step 1: Get all ACP related to this Elemen
          console.log(
            "🔍 Step 1: Mencari ACP terkait dengan Elemen ID:",
            idElemen
          );
          const acpResponse = await getACP();
          const relatedACPs = acpResponse.data.content.filter(
            (acp) => acp.elemen && acp.elemen.idElemen === idElemen
          );
          console.log("📊 Found ACP to delete:", relatedACPs.length);

          // Step 2: Get all ATP related to these ACPs
          const atpResponse = await getATP();
          const relatedATPs = atpResponse.data.content.filter((atp) =>
            relatedACPs.some((acp) => acp.idAcp === atp.acp?.idAcp)
          );
          console.log("📊 Found ATP to delete:", relatedATPs.length);

          // Step 3: Delete ATPs first (bottom-up)
          for (const atp of relatedATPs) {
            console.log("🗑️ Deleting ATP:", atp.namaAtp);
            await deleteATP({ idAtp: atp.idAtp });
          }

          // Step 4: Delete ACPs
          for (const acp of relatedACPs) {
            console.log(
              "🗑️ Deleting ACP:",
              acp.namaAcp?.substring(0, 50) + "..."
            );
            await deleteACP({ idAcp: acp.idAcp });
          }

          // Step 5: Finally delete the Elemen
          console.log("🗑️ Deleting Elemen:", namaElemen);
          await deleteElemen({ idElemen });

          message.destroy();
          message.success({
            content: `Berhasil menghapus Elemen &quot;${namaElemen}&quot; beserta ${relatedACPs.length} ACP dan ${relatedATPs.length} ATP terkait`,
            duration: 5,
          });

          fetchELemen();
        } catch (error) {
          message.destroy();
          message.error("Gagal menghapus: " + error.message);
          console.error("Cascade delete error:", error);
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
      const updatedValues = {
        idElemen: null,
        namaElemen: values.namaElemen,
        idKonsentrasiSekolah: values.idKonsentrasiSekolah,
        idKelas: values.idKelas,
        idTahun: values.idTahun,
        idSemester: values.idSemester,
        idMapel: values.idMapel,
        idSekolah: values.idSchool,
      };
      console.log("respon data", updatedValues);
      await addElemen(updatedValues);
      setAddModalVisible(false);
      setAddModalLoading(false);
      message.success("Berhasil menambahkan");
      fetchELemen();
    } catch (error) {
      setAddModalLoading(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddModalLoading(false);
    }
  };

  const handleEditOk = async (values) => {
    setEditModalLoading(true);
    try {
      const updatedValues = {
        idElemen: values.idElemen,
        namaElemen: values.namaElemen,
        idKonsentrasiSekolah: values.idKonsentrasiSekolah,
        idKelas: values.idKelas,
        idTahun: values.idTahun,
        idSemester: values.idSemester,
        idMapel: values.idMapel,
        idSekolah: values.idSchool,
      };

      console.log("respon data", updatedValues);
      await editElemen(updatedValues, currentRowData.idElemen);
      setEditModalVisible(false);
      setEditModalLoading(false);
      message.success("Berhasil mengubah");
      fetchELemen();
    } catch (error) {
      setEditModalLoading(false);
      message.error("Gagal mengubah: " + error.message);
    }
  };

  const handleCancel = () => {
    setAddModalVisible(false);
    setEditModalVisible(false);
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getElemen();
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
      title: "Elemen",
      dataIndex: ["namaElemen"],
      key: "namaElemen",
      align: "center",
      ...getColumnSearchProps("namaElemen", "namaElemen"),
      sorter: (a, b) => a.namaElemen.localeCompare(b.namaElemen),
    },
    {
      title: "Mata Pelajaran",
      dataIndex: ["mapel", "name"],
      key: "name",
      align: "center",
      ...getColumnSearchProps("name", "mapel.name"),
      sorter: (a, b) => a.mapel.name.localeCompare(b.mapel.name),
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
            onClick={() => handleEdit(row)}
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
      rowKey="idElemen"
      dataSource={getFilteredElemenList()}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  return (
    <div className="app-container">
      <TypingCard
        title="Manajemen Elemen"
        source="Di sini, Anda dapat mengelola elemen  di sistem."
      />
      <br />
      {loading ? (
        <Card>
          <Skeleton active paragraph={{ rows: 10 }} />
        </Card>
      ) : (
        <>
          {/* Tampilkan selection steps atau tabel */}
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
                      onClick={() => setAddModalVisible(true)}
                    >
                      Tambahkan Elemen
                    </Button>
                  </Col>

                  {/* Kolom Pencarian */}
                  <Col>
                    <Input.Search
                      key="search"
                      placeholder="Cari elemen, mata pelajaran, atau konsentrasi keahlian..."
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

          <AddElemenForm
            wrappedComponentRef={addFormRef}
            visible={addModalVisible}
            confirmLoading={addModalLoading}
            onCancel={handleCancel}
            onOk={handleAddOk}
          />

          <EditElemenForm
            wrappedComponentRef={editFormRef}
            currentRowData={currentRowData}
            visible={editModalVisible}
            confirmLoading={editModalLoading}
            onCancel={handleCancel}
            onOk={handleEditOk}
          />
        </>
      )}
    </div>
  );
};

export default Elemen;
