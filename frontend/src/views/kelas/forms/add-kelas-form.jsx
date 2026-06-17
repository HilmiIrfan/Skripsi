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
import { getKelas } from "@/api/kelas";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;

const AddKelasForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();
  const [kelas, setKelas] = useState([]);

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

  const fetchKelas = async () => {
    setTableLoading(true);
    try {
      const result = await getKelas();
      if (result.data.statusCode === 200) {
        setKelas(result.data.content);
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
    fetchKelas();
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
      title="Tambah Kelas"
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
          <Col xs={24} sm={24} md={24}>
            <Form.Item
              label="Kelas :"
              name="namaKelas"
              rules={[{ required: true, message: "Silahkan isikan Kelas" }]}
            >
              <Input placeholder="Kelas " />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default AddKelasForm;
