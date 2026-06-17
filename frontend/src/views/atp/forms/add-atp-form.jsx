/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import {
  Form,
  Input,
  Modal,
  Select,
  Table,
  Tabs,
  Row,
  Col,
  Button,
  message,
} from "antd";

import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";
import { getKelas } from "@/api/kelas";
import { getTahunAjaran } from "@/api/tahun-ajaran";
import { getSemester } from "@/api/semester";
import { getMapel } from "@/api/mapel";
import { getKonsentrasiSekolah } from "@/api/konsentrasiKeahlianSekolah";
import { getElemen } from "@/api/elemen";
import { getACP } from "@/api/acp";
import { getATP } from "@/api/atp";
import { useFormFilterACP } from "@/helper/acp/formFilterAcpHelperAdd";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;

const AddATPForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [atp, setATP] = useState([]);
  const [form] = Form.useForm();

  // State untuk menyimpan daftar ATP yang ditambahkan
  const [atpItems, setAtpItems] = useState([
    { id: 1, namaAtp: "", jumlahJpl: "" },
  ]);

  const [userSchoolId, setUserSchoolId] = useState([]); // State untuk menyimpan ID sekolah user
  const [schoolList, setSchoolList] = useState([]);
  const [konsentrasiKeahlianSekolahList, setKonsentrasiKeahlianSekolahList] =
    useState([]);
  const [tableLoading, setTableLoading] = useState(false);

  const [initialData, setInitialData] = useState(null);

  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo(); // Ambil data user dari API
      setUserSchoolId(response.data.school_id); // Simpan ID sekolah user ke state
      console.log("User School ID: ", response.data.school_id);
    } catch (error) {
      message.error("Gagal mengambil informasi pengguna");
    }
  };

  const fetchSchoolList = async () => {
    try {
      const result = await getSchool();
      if (result.data.statusCode === 200) {
        setSchoolList(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const fetchATP = async () => {
    setTableLoading(true);
    try {
      const result = await getATP();
      if (result.data.statusCode === 200) {
        setATP(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
    }
  };

  const fetchKonsentrasiKeahlianSekolahList = async () => {
    try {
      const result = await getKonsentrasiSekolah();
      if (result.data.statusCode === 200) {
        setKonsentrasiKeahlianSekolahList(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const {
    renderTahunAjaranSelect,
    renderSemesterSelect,
    renderKelasSelect,
    renderMapelSelect,
    renderElemenSelect,
    renderAcpSelect,
  } = useFormFilterACP(initialData);

  // Fetch data awal
  useEffect(() => {
    const fetchData = async () => {
      try {
        setTableLoading(true);
        const [
          tahunAjaranRes,
          semesterRes,
          kelasRes,
          mapelRes,
          elemenRes,
          acpRes,
        ] = await Promise.all([
          getTahunAjaran(),
          getSemester(),
          getKelas(),
          getMapel(),
          getElemen(),
          getACP(),
        ]);

        setInitialData({
          tahunAjaranList: tahunAjaranRes.data.content || [],
          semesterList: semesterRes.data.content || [],
          kelasList: kelasRes.data.content || [],
          mapelList: mapelRes.data.content || [],
          elemenList: elemenRes.data.content || [],
          acpList: acpRes.data.content || [],
        });
      } catch (error) {
        message.error("Gagal memuat data");
      } finally {
        setTableLoading(false);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    fetchUserInfo();
    fetchSchoolList();
    fetchKonsentrasiKeahlianSekolahList();
    fetchATP();
  }, []);

  useEffect(() => {
    if (userSchoolId) {
      form.setFieldsValue({ idSchool: userSchoolId });
    }
  }, [userSchoolId, form]);

  // Fungsi untuk menambah baris ATP baru
  const addAtpItem = () => {
    const newId =
      atpItems.length > 0
        ? Math.max(...atpItems.map((item) => item.id)) + 1
        : 1;
    setAtpItems([...atpItems, { id: newId, namaAtp: "", jumlahJpl: "" }]);
  };

  // Fungsi untuk menghapus baris ATP
  const removeAtpItem = (id) => {
    if (atpItems.length > 1) {
      setAtpItems(atpItems.filter((item) => item.id !== id));
    } else {
      message.warning("Minimal harus ada satu Tujuan Pembelajaran");
    }
  };

  // Fungsi untuk update nilai ATP
  const updateAtpItem = (id, field, value) => {
    setAtpItems(
      atpItems.map((item) =>
        item.id === id ? { ...item, [field]: value } : item
      )
    );
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      // Validasi input ATP
      const invalidAtp = atpItems.some(
        (item) => !item.namaAtp || !item.jumlahJpl
      );
      if (invalidAtp) {
        message.error("Mohon lengkapi semua Nama ATP dan Jumlah JPL");
        return;
      }

      // Jika kita hanya mengirim data ATP pertama untuk kompatibilitas dengan kode lama
      // (untuk menghindari undefined)
      if (atpItems.length > 0) {
        values.namaAtp = atpItems[0].namaAtp;
        values.jumlahJpl = atpItems[0].jumlahJpl;
      }

      // Gabungkan data form dengan data ATP lengkap
      const completeValues = {
        ...values,
        atpList: atpItems,
      };

      onOk(completeValues);
    } catch (error) {
      console.error("Validation failed:", error);
    }
  };

  const atpColumns = [
    {
      title: "No.",
      key: "index",
      width: "50px",
      render: (_, __, index) => index + 1,
    },
    {
      title: "Nama Tujuan Pembelajaran",
      key: "namaAtp",
      render: (_, record) => (
        <Input
          placeholder="Masukkan Nama ATP"
          value={record.namaAtp}
          onChange={(e) => updateAtpItem(record.id, "namaAtp", e.target.value)}
        />
      ),
    },
    {
      title: "Jumlah Jam Pelajaran",
      key: "jumlahJpl",
      width: "200px",
      render: (_, record) => (
        <Input
          placeholder="Masukkan JPL"
          value={record.jumlahJpl}
          onChange={(e) =>
            updateAtpItem(record.id, "jumlahJpl", e.target.value)
          }
        />
      ),
    },
    {
      title: "Aksi",
      key: "action",
      width: "70px",
      render: (_, record) => (
        <Button
          type="text"
          danger
          icon={<DeleteOutlined />}
          onClick={() => removeAtpItem(record.id)}
        />
      ),
    },
  ];

  return (
    <Modal
      title="Tambah Kelas Analisa Tujuan Pembelajaran"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
      okText="Simpan"
      width={1000} // Mengatur lebar modal agar lebih luas
    >
      <Form form={form} layout="vertical">
        <Row gutter={16}>
          <Col xs={24} sm={24} md={24}>
            <Form.Item
              label="Sekolah:"
              name="idSchool"
              style={{ display: "none" }}
              rules={[{ required: true, message: "Silahkan pilih Kelas" }]}
            >
              <Select defaultValue={userSchoolId} disabled>
                {schoolList
                  .filter(({ idSchool }) => idSchool === userSchoolId) // Hanya menampilkan sekolah user
                  .map(({ idSchool, nameSchool }) => (
                    <Option key={idSchool} value={idSchool}>
                      {nameSchool}
                    </Option>
                  ))}
              </Select>
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Konsentrasi Keahlian Sekolah:"
              name="idKonsentrasiSekolah"
              rules={[
                {
                  required: true,
                  message: "Silahkan pilih Konsentrasi Keahlian Sekolah",
                },
              ]}
            >
              <Select
                placeholder="Pilih Konsentrasi Keahlian Sekolah"
                showSearch
                optionFilterProp="children"
                filterOption={(input, option) =>
                  option.children.toLowerCase().includes(input.toLowerCase())
                }
              >
                {konsentrasiKeahlianSekolahList.map(
                  ({ idKonsentrasiSekolah, namaKonsentrasiSekolah }) => (
                    <Option
                      key={idKonsentrasiSekolah}
                      value={idKonsentrasiSekolah}
                    >
                      {namaKonsentrasiSekolah}
                    </Option>
                  )
                )}
              </Select>
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderTahunAjaranSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderSemesterSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderKelasSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderMapelSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderElemenSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={24}>
            {renderAcpSelect(form)}
          </Col>
          {/* Tabel ATP */}
          <Col xs={24} sm={24} md={24} style={{ marginTop: "20px" }}>
            <div style={{ marginBottom: "10px" }}>
              <span style={{ fontSize: "16px", fontWeight: "500" }}>
                Tujuan Pembelajaran:
              </span>
            </div>
            <Table
              dataSource={atpItems}
              columns={atpColumns}
              pagination={false}
              rowKey="id"
              size="middle"
              bordered
            />
            <Button
              type="dashed"
              onClick={addAtpItem}
              style={{ width: "100%", marginTop: "10px" }}
              icon={<PlusOutlined />}
            >
              Tambah Tujuan Pembelajaran
            </Button>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default AddATPForm;
