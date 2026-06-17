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
import { getUsers } from "@/api/user";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;

const AdduserForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();
  const [user, setuser] = useState([]);

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

  const fetchuser = async () => {
    setTableLoading(true);
    try {
      const result = await getUsers();
      if (result.data.statusCode === 200) {
        setuser(result.data.content);
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
    fetchuser();
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
      title="Tambah user"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
      okText="Simpan"
      width={1000}
    >
      <Form form={form} layout="vertical">
        <Row gutter={16}>
          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Sekolah:"
              name="schoolId"
              rules={[{ required: true, message: "Silahkan pilih Sekolah" }]}
            >
              <Select
                placeholder="Pilih Sekolah"
                showSearch
                optionFilterProp="children"
                filterOption={(input, option) =>
                  option.children.toLowerCase().includes(input.toLowerCase())
                }
              >
                {schoolList.map(({ idSchool, nameSchool }) => (
                  <Option key={idSchool} value={idSchool}>
                    {nameSchool}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Username: "
              name="username"
              rules={[{ required: true, message: "Silahkan isikan username" }]}
            >
              <Input placeholder="username" />
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Nama Lengkap: "
              name="name"
              rules={[
                { required: true, message: "Silahkan isikan nama lengkap" },
              ]}
            >
              <Input placeholder="Nama Lengkap" />
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Email: "
              name="email"
              rules={[{ required: true, message: "Silahkan isikan email" }]}
            >
              <Input placeholder="Email" />
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Password: "
              name="password"
              rules={[{ required: true, message: "Silahkan isikan password" }]}
            >
              <Input.Password placeholder="Password" />
            </Form.Item>
          </Col>

          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Roles: "
              name="roles"
              rules={[{ required: true, message: "Silahkan pilih roles" }]}
            >
              <Select placeholder="Pilih Roles">
                <Option value="2">Admin Sekolah</Option>
                <Option value="3">Guru</Option>
                <Option value="5">Siswa</Option>
              </Select>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default AdduserForm;
