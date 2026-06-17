/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
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
} from "antd";
import { getSeason, deleteSeason, editSeason, addSeason } from "@/api/season";
import TypingCard from "@/components/TypingCard";
import EditSeasonForm from "./forms/edit-season-form";
import AddSeasonForm from "./forms/add-season-form";
import { read, utils } from "xlsx";

const { Column } = Table;

const Season = () => {
  const [season, setSeason] = useState([]);
  const [modalState, setModalState] = useState({
    editVisible: false,
    editLoading: false,
    addVisible: false,
    addLoading: false,
    importVisible: false,
  });
  const [currentRowData, setCurrentRowData] = useState({});
  const [importData, setImportData] = useState({
    data: [],
    columnTitles: [],
    fileName: "",
    columnMapping: {},
  });
  const [uploading, setUploading] = useState(false);

  const editFormRef = useRef();
  const addFormRef = useRef();

  const fetchSeason = async () => {
    try {
      const result = await getSeason();
      if (result.data.statusCode === 200) {
        setSeason(result.data.content);
      }
    } catch (error) {
      console.error("Error fetching season data:", error);
    }
  };

  useEffect(() => {
    fetchSeason();
  }, []);

  const handleEditSeason = (row) => {
    setCurrentRowData({ ...row });
    setModalState((prev) => ({ ...prev, editVisible: true }));
  };

  const handleDeleteSeason = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteSeason({ id });
      message.success("Berhasil dihapus");
      fetchSeason();
    } catch (error) {
      message.error("Gagal menghapus");
    }
  };

  const handleEditSeasonOk = () => {
    const form = editFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, editLoading: true }));
      try {
        await editSeason(values, values.id);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          editVisible: false,
          editLoading: false,
        }));
        message.success("Berhasil diperbarui!");
        fetchSeason();
      } catch (error) {
        message.error("Gagal memperbarui");
        setModalState((prev) => ({ ...prev, editLoading: false }));
      }
    });
  };

  const handleCancel = () => {
    setModalState((prev) => ({
      ...prev,
      editVisible: false,
      addVisible: false,
      importVisible: false,
    }));
  };

  const handleAddSeason = () => {
    setModalState((prev) => ({ ...prev, addVisible: true }));
  };

  const handleAddSeasonOk = () => {
    const form = addFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, addLoading: true }));
      try {
        const payload = {
          ...values,
          student_id: Array.isArray(values.student_id)
            ? values.student_id.filter((id) => id !== null)
            : [],
          jadwalPelajaran_id: Array.isArray(values.jadwalPelajaran_id)
            ? values.jadwalPelajaran_id.filter((id) => id !== null)
            : [],
        };

        await addSeason(payload);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          addVisible: false,
          addLoading: false,
        }));
        message.success("Berhasil ditambahkan!");
        fetchSeason();
      } catch (error) {
        message.error("Gagal menambahkan, coba lagi!");
        setModalState((prev) => ({ ...prev, addLoading: false }));
      }
    });
  };

  const handleFileImport = (file) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const data = new Uint8Array(e.target.result);
      const workbook = read(data, { type: "array" });
      const worksheet = workbook.Sheets[workbook.SheetNames[0]];
      const jsonData = utils.sheet_to_json(worksheet, { header: 1 });

      const columnTitles = jsonData[0];
      const columnMapping = {};
      columnTitles.forEach((title, index) => {
        columnMapping[title] = index;
      });

      setImportData({
        data: jsonData.slice(1),
        columnTitles,
        fileName: file.name.toLowerCase(),
        columnMapping,
      });
    };
    reader.readAsArrayBuffer(file);
  };

  const handleUpload = async () => {
    if (importData.data.length === 0) {
      message.error("Tidak ada data untuk diimpor");
      return;
    }

    setUploading(true);
    let errorCount = 0;

    try {
      for (const row of importData.data) {
        const dataToSave = {
          id: row[importData.columnMapping["ID Konsentrasi"]],
          konsentrasi:
            row[importData.columnMapping["Nama Konsentrasi Keahlian"]],
          programKeahlian_id: row[importData.columnMapping["ID Program"]],
        };

        const existingIdx = season.findIndex((p) => p.id === dataToSave.id);

        try {
          if (existingIdx > -1) {
            await editSeason(dataToSave, dataToSave.id);
            setSeason((prev) => {
              const updated = [...prev];
              updated[existingIdx] = dataToSave;
              return updated;
            });
          } else {
            await addSeason(dataToSave);
            setSeason((prev) => [...prev, dataToSave]);
          }
        } catch (error) {
          errorCount++;
          console.error("Gagal menyimpan data:", error);
        }
      }

      if (errorCount === 0) {
        message.success("Semua data berhasil disimpan");
      } else {
        message.error(`${errorCount} data gagal disimpan`);
      }
    } catch (error) {
      console.error("Gagal memproses data:", error);
      message.error("Gagal memproses data");
    } finally {
      setUploading(false);
      setModalState((prev) => ({ ...prev, importVisible: false }));
      setImportData({
        data: [],
        columnTitles: [],
        fileName: "",
        columnMapping: {},
      });
    }
  };

  const cardContent = `Di sini, Anda dapat mengelola kelas di sistem, seperti menambahkan kelas baru, atau mengubah kelas yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Kelas Ajaran" source={cardContent} />
      <br />
      <Card
        title={
          <Row gutter={[16, 16]} justify="start" style={{ paddingLeft: 9 }}>
            <Col xs={24} sm={12} md={8} lg={6} xl={6}>
              <Button type="primary" onClick={handleAddSeason}>
                Tambahkan Kelas
              </Button>
            </Col>
            <Col xs={24} sm={12} md={8} lg={6} xl={6}>
              <Button
                onClick={() =>
                  setModalState((prev) => ({ ...prev, importVisible: true }))
                }
              >
                Import File
              </Button>
            </Col>
          </Row>
        }
      >
        <Table
          variant
          rowKey="id"
          dataSource={season}
          pagination={{ pageSize: 10 }}
        >
          <Column
            title="Tahun Ajaran"
            dataIndex="tahunAjaran.tahunAjaran"
            align="center"
          />
          <Column
            title="Bidang Keahlian"
            dataIndex="bidangKeahlian.bidang"
            align="center"
          />
          <Column
            title="Program Keahlian"
            dataIndex="programKeahlian.program"
            align="center"
          />
          <Column
            title="Konsentrasi Keahlian"
            dataIndex="konsentrasiKeahlian.konsentrasi"
            align="center"
          />
          <Column title="Kelas" dataIndex="kelas.namaKelas" align="center" />
          <Column title="Wali Kelas" dataIndex="lecture.name" align="center" />
          <Column title="Siswa" dataIndex="student" align="center" />
          <Column
            title="Mata Pelajaran"
            dataIndex="jadwalPelajaran"
            align="center"
          />
        </Table>
      </Card>

      <EditSeasonForm
        wrappedComponentRef={editFormRef}
        currentRowData={currentRowData}
        visible={modalState.editVisible}
        confirmLoading={modalState.editLoading}
        onCancel={handleCancel}
        onOk={handleEditSeasonOk}
      />

      <AddSeasonForm
        wrappedComponentRef={addFormRef}
        visible={modalState.addVisible}
        confirmLoading={modalState.addLoading}
        onCancel={handleCancel}
        onOk={handleAddSeasonOk}
      />

      <Modal
        title="Import File"
        visible={modalState.importVisible}
        onCancel={handleCancel}
        footer={[
          <Button key="cancel" onClick={handleCancel}>
            Cancel
          </Button>,
          <Button
            key="upload"
            type="primary"
            loading={uploading}
            onClick={handleUpload}
          >
            Upload
          </Button>,
        ]}
      >
        <Upload beforeUpload={handleFileImport}>
          <Button>Pilih File</Button>
        </Upload>
      </Modal>
    </div>
  );
};

export default Season;
