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
  message,
} from "antd";

import { getKonsentrasiSekolah } from "@/api/konsentrasiKeahlianSekolah";
import { getSchool } from "@/api/school";
import { getKonsentrasiKeahlian } from "@/api/konsentrasiKeahlian";
import { reqUserInfo } from "@/api/user";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;

const renderColumns = () => [
  {
    title: "No.",
    dataIndex: "index",
    key: "index",
    align: "center",
    render: (_, __, index) => index + 1,
  },
  {
    title: "Sekolah",
    dataIndex: ["school", "nameSchool"],
    key: "nameSchool",
    align: "center",
  },
  {
    title: "Konsentrasi Keahlian",
    dataIndex: ["konsentrasiKeahlian", "konsentrasi"],
    key: "konsentrasi",
    align: "center",
  },
  {
    title: "Konsentrasi Keahlian Sekolah",
    dataIndex: "namaKonsentrasiSekolah",
    key: "namaKonsentrasiSekolah",
    align: "center",
  },
];

const EditKonsentrasiSekolahForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [konsentrasiSekolah, setKonsentrasiSekolah] = useState([]);
  const [form] = Form.useForm();

  const [schoolList, setSchoolList] = useState([]);
  const [konsentrasiKeahlianList, setKonsentrasiKeahlianList] = useState([]);
  const [tableLoading, setTableLoading] = useState(false);
  const [userSchoolId, setUserSchoolId] = useState([]); // State untuk menyimpan ID sekolah user

  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo(); // Ambil data user dari API
      setUserSchoolId(response.data.school_id); // Simpan ID sekolah user ke state
      console.log("User School ID: ", response.data.school_id);
    } catch (error) {
      message.error("Gagal mengambil informasi pengguna");
    }
  };

  const fetchKonsentrasiSekolah = async () => {
    setTableLoading(true);
    try {
      const result = await getKonsentrasiSekolah();
      if (result.data.statusCode === 200) {
        setKonsentrasiSekolah(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
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

  const fetchKonsentrasiKeahlianList = async () => {
    try {
      const result = await getKonsentrasiKeahlian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setKonsentrasiKeahlianList(content);
      } else {
        console.log("Error: ", result.data.message);
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  useEffect(() => {
    fetchKonsentrasiSekolah();
    fetchSchoolList();
    fetchKonsentrasiKeahlianList();
    fetchUserInfo();

    if (currentRowData) {
      form.setFieldsValue({
        idKonsentrasiSekolah: currentRowData.idKonsentrasiSekolah,
        namaKonsentrasiSekolah: currentRowData.namaKonsentrasiSekolah,
        idSchool: currentRowData.school?.idSchool,
        id: currentRowData.konsentrasiKeahlian?.id,
      });
    }
  }, [currentRowData, form]);

  useEffect(() => {
    if (userSchoolId) {
      form.setFieldsValue({ idSchool: userSchoolId });
    }
  }, [userSchoolId]);

  const handleKonsentrasiChange = (selectedId) => {
    const selectedKonsentrasi = konsentrasiKeahlianList.find(
      (item) => item.id === selectedId
    );
    if (selectedKonsentrasi) {
      form.setFieldsValue({
        namaKonsentrasiSekolah: selectedKonsentrasi.konsentrasi,
      });
    }
  };

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
      title="Edit Analisa Konsentrasi Keahlian Sekolah"
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
          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Sekolah:"
              name="idSchool"
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
              label="Konsentrasi Keahlian:"
              name="id"
              rules={[
                {
                  required: true,
                  message: "Silahkan pilih Konsentrasi Keahlian",
                },
              ]}
            >
              <Select
                placeholder="Pilih Konsentrasi Keahlian"
                showSearch
                optionFilterProp="children"
                onChange={handleKonsentrasiChange}
                filterOption={(input, option) =>
                  option.children.toLowerCase().includes(input.toLowerCase())
                }
              >
                {konsentrasiKeahlianList.map(({ id, konsentrasi }) => (
                  <Option key={id} value={id}>
                    {konsentrasi}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12} style={{ display: "none" }}>
            <Form.Item
              label="Nama Konsentrasi Keahlian Sekolah:"
              name="namaKonsentrasiSekolah"
              rules={[
                {
                  required: true,
                  message: "Silahkan isi Nama Konsentrasi Keahlian Sekolah",
                },
              ]}
            >
              <Input
                readOnly
                placeholder="Masukkan Nama Konsentrasi Keahlian Sekolah"
              />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default EditKonsentrasiSekolahForm;
