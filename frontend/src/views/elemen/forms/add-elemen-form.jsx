/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import { Form, Input, Modal, Select, Row, Col, message, Tabs } from "antd";
import { getElemen } from "@/api/elemen";
import { getKelas } from "@/api/kelas";
import { getTahunAjaran } from "@/api/tahun-ajaran";
import { getSemester } from "@/api/semester";
import { getMapel } from "@/api/mapel";
import { getKonsentrasiSekolah } from "@/api/konsentrasiKeahlianSekolah";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";
import { useFormFilter } from "@/helper/formFilterMapelHelperAdd";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;

const AddElemen = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();
  const [elemen, setElemen] = useState([]);

  const [tableLoading, setTableLoading] = useState(false);
  const [kelasList, setKelasList] = useState([]);
  const [tahunAjaranList, setTahunAjaranList] = useState([]);
  const [semesterList, setSemesterList] = useState([]);
  const [mapelList, setMapelList] = useState([]);
  const [konsentrasiKeahlianSekolahList, setKonsentrasiKeahlianSekolahList] =
    useState([]);
  const [userSchoolId, setUserSchoolId] = useState([]); // State untuk menyimpan ID sekolah user
  const [schoolList, setSchoolList] = useState([]);

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

  const fetchElemen = async () => {
    setTableLoading(true);
    try {
      const result = await getElemen();
      if (result.data.statusCode === 200) {
        setElemen(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
    }
  };

  const fetchKelasList = async () => {
    try {
      const result = await getKelas();
      if (result.data.statusCode === 200) {
        setKelasList(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
    }
  };

  const fetchTahunAjaranList = async () => {
    try {
      const result = await getTahunAjaran();
      if (result.data.statusCode === 200) {
        setTahunAjaranList(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
    }
  };

  const fetchSemesterList = async () => {
    try {
      const result = await getSemester();
      if (result.data.statusCode === 200) {
        setSemesterList(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
    }
  };

  const fetchMapelList = async () => {
    try {
      const result = await getMapel();
      if (result.data.statusCode === 200) {
        setMapelList(result.data.content);
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
    } finally {
      setTableLoading(false);
    }
  };

  const {
    renderTahunAjaranSelect,
    renderSemesterSelect,
    renderKelasSelect,
    renderMapelSelect,
  } = useFormFilter(initialData);

  // Fetch data awal
  useEffect(() => {
    const fetchData = async () => {
      try {
        setTableLoading(true);
        const [tahunAjaranRes, semesterRes, kelasRes, mapelRes] =
          await Promise.all([
            getTahunAjaran(),
            getSemester(),
            getKelas(),
            getMapel(),
          ]);

        setInitialData({
          tahunAjaranList: tahunAjaranRes.data.content || [],
          semesterList: semesterRes.data.content || [],
          kelasList: kelasRes.data.content || [],
          mapelList: mapelRes.data.content || [],
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
    fetchElemen();
    fetchSchoolList();
    fetchKelasList();
    fetchTahunAjaranList();
    fetchSemesterList();
    fetchMapelList();
    fetchKonsentrasiKeahlianSekolahList();
  }, []);

  useEffect(() => {
    if (userSchoolId) {
      form.setFieldsValue({ idSchool: userSchoolId });
    }
  }, [userSchoolId, form]);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      onOk(values);
    } catch (error) {
      console.error("Validation failed:", error);
    }
  };

  return (
    <Modal
      title="Tambah Elemen"
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
                placeholder="Pilih Konsetrasi Keahlian Sekolah"
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
            <Form.Item
              label="Nama Elemen:"
              name="namaElemen"
              rules={[{ required: true, message: "Silahkan isi Nama Elemen" }]}
            >
              <Input placeholder="Masukkan Nama Elemen" />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default AddElemen;
