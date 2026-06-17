/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Form, Input, Modal, Select, Tabs, Row, Col, message } from "antd";
import { getTahunAjaran } from "@/api/tahun-ajaran";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";
import { use } from "react";

const { TextArea } = Input;
const { Option } = Select;

const AddTahunAjaranForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();
  const [tahunAjaran, setTahunAjaran] = useState([]);

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

  const fetchTahunAjaran = async () => {
    setTableLoading(true);
    try {
      const result = await getTahunAjaran();
      if (result.data.statusCode === 200) {
        setTahunAjaran(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
    setTableLoading(false);
  };

  useEffect(() => {
    fetchUserInfo();
    fetchSchoolList();
    fetchTahunAjaran();
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
      message.error("Gagal menambahkan data");
    }
  };

  return (
    <Modal
      title="Tambah Tahun Ajaran"
      open={visible}
      onCancel={() => {
        onCancel();
        form.resetFields();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
      onText="Simpan"
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
              label="Tahun Ajaran:"
              name="tahunAjaran"
              rules={[
                { required: true, message: "Silahkan isikan Tahun Ajaran" },
              ]}
            >
              <Input placeholder="Tahun Ajaran" />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default AddTahunAjaranForm;
