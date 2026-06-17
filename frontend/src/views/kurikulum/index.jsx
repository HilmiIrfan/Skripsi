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
} from "antd";
import {
  getKurikulum,
  deleteKurikulum,
  editKurikulum,
  addKurikulum,
} from "@/api/kurikulum";
import TypingCard from "@/components/TypingCard";
import EditKurikulumForm from "./forms/edit-kurikulum-form";
import AddKurikulumForm from "./forms/add-kurikulum-form";
import { read, utils } from "xlsx";

const { Column } = Table;

const Kurikulum = () => {
  const [jadwalPelajaran, setJadwalPelajaran] = useState([]);
  const [editKurikulumModalVisible, setEditKurikulumModalVisible] =
    useState(false);
  const [editKurikulumModalLoading, setEditKurikulumModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addKurikulumModalVisible, setAddKurikulumModalVisible] =
    useState(false);
  const [addKurikulumModalLoading, setAddKurikulumModalLoading] =
    useState(false);
  const [importedData, setImportedData] = useState([]);
  const [columnTitles, setColumnTitles] = useState([]);
  const [fileName, setFileName] = useState("");
  const [uploading, setUploading] = useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [columnMapping, setColumnMapping] = useState({});

  const editKurikulumFormRef = useRef();
  const addKurikulumFormRef = useRef();

  const fetchKurikulum = async () => {
    try {
      const result = await getKurikulum();
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setJadwalPelajaran(content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditKurikulum = (row) => {
    setCurrentRowData({ ...row });
    setEditKurikulumModalVisible(true);
  };

  const handleDeleteKurikulum = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteKurikulum({ id });
      message.success("Berhasil dihapus");
      fetchKurikulum();
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleEditKurikulumOk = () => {
    const form = editKurikulumFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditKurikulumModalLoading(true);
      editKurikulum(values, values.id)
        .then((response) => {
          form.resetFields();
          setEditKurikulumModalVisible(false);
          setEditKurikulumModalLoading(false);
          message.success("Berhasil diubah!");
          fetchKurikulum();
        })
        .catch((error) => {
          setEditKurikulumModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  const handleCancel = () => {
    setEditKurikulumModalVisible(false);
    setAddKurikulumModalVisible(false);
  };

  const handleAddKurikulum = () => {
    setAddKurikulumModalVisible(true);
  };

  const handleAddKurikulumOk = () => {
    const form = addKurikulumFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddKurikulumModalLoading(true);
      addKurikulum(values)
        .then((response) => {
          form.resetFields();
          setAddKurikulumModalVisible(false);
          setAddKurikulumModalLoading(false);
          message.success("Berhasil ditambahkan!");
          fetchKurikulum();
        })
        .catch((error) => {
          setAddKurikulumModalLoading(false);
          message.error("Gagal menambahkan: " + error.message);
        });
    });
  };

  const handleFileImport = (file) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      try {
        const data = new Uint8Array(e.target.result);
        const workbook = read(data, { type: "array" });
        const worksheet = workbook.Sheets[workbook.SheetNames[0]];
        const jsonData = utils.sheet_to_json(worksheet, { header: 1 });

        if (jsonData.length < 2) {
          message.error("File tidak memiliki data yang valid");
          return;
        }

        const newImportedData = jsonData.slice(1);
        const newColumnTitles = jsonData[0];
        const newFileName = file.name.toLowerCase();

        const newColumnMapping = {};
        newColumnTitles.forEach((title, index) => {
          newColumnMapping[title] = index;
        });

        setImportedData(newImportedData);
        setColumnTitles(newColumnTitles);
        setFileName(newFileName);
        setColumnMapping(newColumnMapping);
      } catch (error) {
        message.error("Gagal membaca file: " + error.message);
      }
    };
    reader.onerror = () => {
      message.error("Gagal membaca file");
    };
    reader.readAsArrayBuffer(file);
    return false;
  };

  const saveImportedData = async () => {
    let errorCount = 0;
    const successRecords = [];

    try {
      for (const row of importedData) {
        const dataToSave = {
          id: row[columnMapping["ID Konsentrasi"]],
          konsentrasi: row[columnMapping["Nama Konsentrasi Keahlian"]],
          programKeahlian_id: row[columnMapping["ID Program"]],
        };

        if (
          !dataToSave.id ||
          !dataToSave.konsentrasi ||
          !dataToSave.programKeahlian_id
        ) {
          errorCount++;
          continue;
        }

        const existingIndex = jadwalPelajaran.findIndex(
          (p) => p.id === dataToSave.id
        );

        try {
          if (existingIndex > -1) {
            await editKurikulum(dataToSave, dataToSave.id);
            setJadwalPelajaran((prev) => {
              const updated = [...prev];
              updated[existingIndex] = dataToSave;
              return updated;
            });
            successRecords.push(dataToSave);
          } else {
            await addKurikulum(dataToSave);
            setJadwalPelajaran((prev) => [...prev, dataToSave]);
            successRecords.push(dataToSave);
          }
        } catch (error) {
          errorCount++;
          console.error("Gagal menyimpan data:", error);
        }
      }

      if (errorCount === 0) {
        message.success(`${successRecords.length} data berhasil disimpan.`);
      } else {
        message.warning(
          `${successRecords.length} data berhasil disimpan. ${errorCount} data gagal disimpan.`
        );
      }

      if (successRecords.length > 0) {
        fetchKurikulum();
      }
    } catch (error) {
      console.error("Gagal memproses data:", error);
      message.error("Terjadi kesalahan saat memproses data");
    } finally {
      setImportedData([]);
      setColumnTitles([]);
      setColumnMapping({});
    }
  };

  const handleUpload = () => {
    if (importedData.length === 0) {
      message.error("Tidak ada data untuk diimpor");
      return;
    }

    setUploading(true);
    saveImportedData()
      .then(() => {
        setUploading(false);
        setImportModalVisible(false);
      })
      .catch((error) => {
        console.error("Gagal mengunggah data:", error);
        setUploading(false);
        message.error("Gagal mengunggah data, harap coba lagi.");
      });
  };

  useEffect(() => {
    fetchKurikulum();
  }, []);

  const title = (
    <Row gutter={[16, 16]} justify="start" style={{ paddingLeft: 9 }}>
      <Col xs={24} sm={12} md={8} lg={6} xl={6}>
        <Button type="primary" onClick={handleAddKurikulum}>
          Tambahkan Jadwal Pelajaran
        </Button>
      </Col>
      <Col xs={24} sm={12} md={8} lg={6} xl={6}>
        <Button onClick={() => setImportModalVisible(true)}>Import File</Button>
      </Col>
    </Row>
  );

  const cardContent = `Di sini, Anda dapat mengelola jadwal pelajaran di sistem, seperti menambahkan jadwal pelajaran baru, atau mengubah jadwal pelajaran yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Jadwal Pelajaran" source={cardContent} />
      <br />
      <Card title={title}>
        <Table
          variant
          rowKey="id"
          dataSource={jadwalPelajaran}
          pagination={{ pageSize: 10 }}
        >
          <Column
            title="Guru Pengajar"
            dataIndex="lecture.name"
            key="lecture.name"
            align="center"
          />
          <Column
            title="Jabatan"
            dataIndex="jabatan"
            key="jabatan"
            align="center"
          />
          <Column
            title="Mata Pelajaran"
            dataIndex="mapel.name"
            key="mapel.name"
            align="center"
          />
          <Column
            title="Jam Pelajaran"
            dataIndex="jmlJam"
            key="jmlJam"
            align="center"
          />
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(text, row) => (
              <span>
                <Button
                  type="primary"
                  shape="circle"
                  icon="edit"
                  title="mengedit"
                  onClick={() => handleEditKurikulum(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteKurikulum(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditKurikulumForm
        currentRowData={currentRowData}
        wrappedComponentRef={editKurikulumFormRef}
        visible={editKurikulumModalVisible}
        confirmLoading={editKurikulumModalLoading}
        onCancel={handleCancel}
        onOk={handleEditKurikulumOk}
      />

      <AddKurikulumForm
        wrappedComponentRef={addKurikulumFormRef}
        visible={addKurikulumModalVisible}
        confirmLoading={addKurikulumModalLoading}
        onCancel={handleCancel}
        onOk={handleAddKurikulumOk}
      />

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
            onClick={handleUpload}
          >
            Upload
          </Button>,
        ]}
      >
        <Upload beforeUpload={handleFileImport} accept=".xlsx,.xls">
          <Button>Pilih File</Button>
        </Upload>
      </Modal>
    </div>
  );
};

export default Kurikulum;
