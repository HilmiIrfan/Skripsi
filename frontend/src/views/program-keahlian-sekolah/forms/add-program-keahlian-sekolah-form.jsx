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
import { getProgramSekolah } from "@/api/programKeahlianSekolah";
import { getSchool } from "@/api/school";
import { getProgramKeahlian } from "@/api/programKeahlian";
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
    title: "Program Keahlian",
    dataIndex: ["programKeahlian", "program"],
    key: "program",
    align: "center",
  },
  {
    title: "Program Keahlian Sekolah",
    dataIndex: "namaProgramSekolah",
    key: "namaProgramSekolah",
    align: "center",
  },
];

const AddProgramSekolahForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [programSekolah, setProgramSekolah] = useState([]);
  const [form] = Form.useForm();

  const [schoolList, setSchoolList] = useState([]);
  const [programKeahlianList, setProgramKeahlianList] = useState([]);
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

  const fetchProgramSekolah = async () => {
    setTableLoading(true);
    try {
      const result = await getProgramSekolah();
      if (result.data.statusCode === 200) {
        setProgramSekolah(result.data.content);
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

  const fetchProgramKeahlianList = async () => {
    try {
      const result = await getProgramKeahlian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setProgramKeahlianList(content);
      } else {
        console.log("Error: ", result.data.message);
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  useEffect(() => {
    fetchUserInfo();
    fetchProgramSekolah();
    fetchSchoolList();
    fetchProgramKeahlianList();
  }, []);

  useEffect(() => {
    if (userSchoolId) {
      form.setFieldsValue({ idSchool: userSchoolId });
    }
  }, [userSchoolId, form]);

  const handleProgramChange = (selectedId) => {
    const selectedProgram = programKeahlianList.find(
      (item) => item.id === selectedId
    );
    if (selectedProgram) {
      form.setFieldsValue({
        namaProgramSekolah: selectedProgram.program,
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
      title="Tambah Kelas Analisa Program Keahlian Sekolah"
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
              label="Program Keahlian:"
              name="id"
              rules={[
                {
                  required: true,
                  message: "Silahkan pilih Program Keahlian",
                },
              ]}
            >
              <Select
                showSearch
                placeholder="Pilih Program Keahlian"
                optionFilterProp="children"
                onChange={handleProgramChange}
                filterOption={(input, option) =>
                  option.children.toLowerCase().includes(input.toLowerCase())
                }
              >
                {programKeahlianList.map(({ id, program }) => (
                  <Option key={id} value={id}>
                    {program}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12} style={{ display: "none" }}>
            <Form.Item
              label="Nama Program Keahlian Sekolah:"
              name="namaProgramSekolah"
              rules={[
                {
                  required: true,
                  message: "Silahkan isi Nama Program Keahlian Sekolah",
                },
              ]}
            >
              <Input
                readOnly
                placeholder="Masukkan Nama Program Keahlian Sekolah"
              />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default AddProgramSekolahForm;
