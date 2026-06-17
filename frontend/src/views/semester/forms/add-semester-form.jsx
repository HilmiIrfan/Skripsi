/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
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
import { getSemester } from "@/api/semester";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;

const AddSemesterForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();
  const [semester, setSemester] = useState([]);

  const [tableLoading, setTableLoading] = useState(false);
  const [userSchoolId, setUserSchoolId] = useState([]); // State untuk menyimpan ID sekolah user
  const [schoolList, setSchoolList] = useState([]);

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

  const fetchSemester = async () => {
    setTableLoading(true);
    try {
      const result = await getSemester();
      if (result.data.statusCode === 200) {
        setSemester(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
    }
  };

  useEffect(() => {
    fetchUserInfo();
    fetchSchoolList();
    fetchSemester();
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
      title="Tambah Semester"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
      okText="Simpan"
      width={500}
    >
      <Form form={form} layout="vertical">
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
        <Col xs={24} sm={24} md={24}>
          <Form.Item
            label="Nama Semester:"
            name="namaSemester"
            rules={[{ required: true, message: "Silahkan isikan Semester" }]}
          >
            <Input placeholder="Nama Semester" />
          </Form.Item>
        </Col>
      </Form>
    </Modal>
  );
};

export default AddSemesterForm;
