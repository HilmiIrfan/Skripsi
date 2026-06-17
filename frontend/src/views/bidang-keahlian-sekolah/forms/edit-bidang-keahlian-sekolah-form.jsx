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

import { getBidangSekolah } from "@/api/bidangKeahlianSekolah";
import { getSchool } from "@/api/school";
import { getBidangKeahlian } from "@/api/bidangKeahlian";
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
    title: "Bidang Keahlian",
    dataIndex: ["bidangKeahlian", "bidang"],
    key: "bidang",
    align: "center",
  },
  {
    title: "Bidang Keahlian Sekolah",
    dataIndex: "namaBidangSekolah",
    key: "namaBidangSekolah",
    align: "center",
  },
];

const EditBidangSekolahForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [bidangSekolah, setBidangSekolah] = useState([]);
  const [form] = Form.useForm();

  const [schoolList, setSchoolList] = useState([]);
  const [bidangKeahlianList, setBidangKeahlianList] = useState([]);
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

  const fetchBidangSekolah = async () => {
    setTableLoading(true);
    try {
      const result = await getBidangSekolah();
      if (result.data.statusCode === 200) {
        setBidangSekolah(result.data.content);
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

  const fetchBidangKeahlianList = async () => {
    try {
      const result = await getBidangKeahlian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setBidangKeahlianList(content);
      } else {
        console.log("Error: ", result.data.message);
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  useEffect(() => {
    fetchBidangSekolah();
    fetchSchoolList();
    fetchBidangKeahlianList();
    fetchUserInfo();

    if (currentRowData) {
      form.setFieldsValue({
        idBidangSekolah: currentRowData.idBidangSekolah,
        namaBidangSekolah: currentRowData.namaBidangSekolah,
        idSchool: currentRowData.school?.idSchool,
        id: currentRowData.bidangKeahlian?.id,
      });
    }
  }, [currentRowData, form]);

  useEffect(() => {
    if (userSchoolId) {
      form.setFieldsValue({ idSchool: userSchoolId });
    }
  }, [userSchoolId, form]);

  const handleBidangChange = (selectedId) => {
    const selectedBidang = bidangKeahlianList.find(
      (item) => item.id === selectedId
    );
    if (selectedBidang) {
      form.setFieldsValue({ namaBidangSekolah: selectedBidang.bidang });
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
      title="Edit Analisa Bidang Keahlian Sekolah"
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
              label="Bidang Keahlian:"
              name="id"
              rules={[
                { required: true, message: "Silahkan pilih Bidang Keahlian" },
              ]}
            >
              <Select
                placeholder="Pilih Bidang Keahlian"
                showSearch
                optionFilterProp="children"
                onChange={handleBidangChange}
                filterOption={(input, option) =>
                  option.children.toLowerCase().includes(input.toLowerCase())
                }
              >
                {bidangKeahlianList.map(({ id, bidang }) => (
                  <Option key={id} value={id}>
                    {bidang}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12} style={{ display: "none" }}>
            <Form.Item
              label="Nama Bidang Keahlian Sekolah:"
              name="namaBidangSekolah"
              rules={[
                {
                  required: true,
                  message: "Silahkan isi Nama Bidang Keahlian Sekolah",
                },
              ]}
            >
              <Input
                readOnly
                placeholder="Masukkan Nama Bidang Keahlian Sekolah"
              />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default EditBidangSekolahForm;
