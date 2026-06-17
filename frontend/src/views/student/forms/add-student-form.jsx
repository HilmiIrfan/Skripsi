/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import { Form, Input, Modal, DatePicker, Select } from "antd";
import { getBidangKeahlian } from "@/api/bidangKeahlian";
import { getProgramByBidang } from "@/api/programKeahlian";
import { getKonsentrasiByProgram } from "@/api/konsentrasiKeahlian";

const { TextArea } = Input;
const { Option } = Select;

const AddStudentForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  religion,
}) => {
  const [form] = Form.useForm();
  const [bidangList, setBidangList] = useState([]);
  const [filteredProgramList, setFilteredProgramList] = useState([]);
  const [filteredKonsentrasiList, setFilteredKonsentrasiList] = useState([]);

  const fetchBidangKeahlianList = async () => {
    try {
      const result = await getBidangKeahlian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const bidangList = content.map((bidang) => ({
          id: bidang.id,
          bidang: bidang.bidang,
        }));
        setBidangList(bidangList);
      }
    } catch (error) {
      console.error("Error fetching bidang data: ", error);
    }
  };

  const handleBidangChange = async (value) => {
    try {
      const result = await getProgramByBidang(value);
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setFilteredProgramList(content);
        setFilteredKonsentrasiList([]);
      } else {
        setFilteredProgramList([]);
        setFilteredKonsentrasiList([]);
      }

      form.setFieldsValue({
        programKeahlian_id: undefined,
        konsentrasiKeahlian_id: undefined,
      });
    } catch (error) {
      console.error("Error fetching program data: ", error);
    }
  };

  const handleProgramChange = async (value) => {
    try {
      const result = await getKonsentrasiByProgram(value);
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setFilteredKonsentrasiList(content);
      } else {
        setFilteredKonsentrasiList([]);
      }

      form.setFieldsValue({
        konsentrasiKeahlian_id: undefined,
      });
    } catch (error) {
      console.error("Error fetching konsentrasi data: ", error);
    }
  };

  const handleSubmit = () => {
    form
      .validateFields()
      .then((values) => {
        onOk(values);
      })
      .catch((info) => {
        console.log("Validate Failed:", info);
      });
  };

  useEffect(() => {
    fetchBidangKeahlianList();
  }, []);

  const formItemLayout = {
    labelCol: {
      xs: { span: 24 },
      sm: { span: 8 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 16 },
    },
  };

  return (
    <Modal
      title="Tambah Siswa"
      visible={visible}
      onCancel={onCancel}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="ID Siswa:"
          name="id"
          rules={[{ required: true, message: "Silahkan isikan id siswa" }]}
        >
          <Input placeholder="ID Siswa" />
        </Form.Item>

        <Form.Item
          label="NISN:"
          name="nisn"
          rules={[{ required: true, message: "NISN wajib diisi" }]}
        >
          <Input placeholder="NISN" />
        </Form.Item>

        <Form.Item
          label="Nama Lengkap:"
          name="name"
          rules={[
            { required: true, message: "Nama lengkap mahasiswa wajib diisi" },
          ]}
        >
          <Input placeholder="Nama Lengkap Mahasiswa" />
        </Form.Item>

        <Form.Item
          label="Tempat Lahir:"
          name="place_born"
          rules={[{ required: true, message: "Tempat Lahir wajib diisi" }]}
        >
          <Input placeholder="Tempat Lahir" />
        </Form.Item>

        <Form.Item
          label="Tanggal Lahir:"
          name="birth_date"
          rules={[{ required: true, message: "Tanggal Lahir wajib diisi" }]}
        >
          <DatePicker placeholder="Tanggal Lahir" />
        </Form.Item>

        <Form.Item
          label="Gender:"
          name="gender"
          rules={[{ required: true, message: "Gender wajib diisi" }]}
        >
          <Select style={{ width: 120 }} placeholder="Gender">
            <Select.Option value="L">Laki-laki</Select.Option>
            <Select.Option value="P">Perempuan</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Nomor Telepon:"
          name="phone"
          rules={[{ required: true, message: "Nomor telefon wajib diisi" }]}
        >
          <Input type="number" placeholder="Nomor Telefon (62)" />
        </Form.Item>

        <Form.Item
          label="Alamat:"
          name="address"
          rules={[{ required: true, message: "Alamat wajib diisi" }]}
        >
          <TextArea placeholder="Alamat" />
        </Form.Item>

        <Form.Item
          label="Agama:"
          name="religion_id"
          rules={[{ required: true, message: "Silahkan pilih agama" }]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih Agama">
            {religion.map((arr, key) => (
              <Select.Option value={arr.id} key={`religion-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Bidang Keahlian:"
          name="bidangKeahlian_id"
          rules={[
            { required: true, message: "Silahkan isi konsentrasi keahlian" },
          ]}
        >
          <Select
            placeholder="Pilih Bidang Keahlian"
            onChange={handleBidangChange}
          >
            {bidangList.map((bidang) => (
              <Option key={bidang.id} value={bidang.id}>
                {bidang.bidang}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Program Keahlian:"
          name="programKeahlian_id"
          rules={[
            { required: true, message: "Silahkan isi konsentrasi keahlian" },
          ]}
        >
          <Select
            placeholder="Pilih Program Keahlian"
            onChange={handleProgramChange}
          >
            {filteredProgramList.map((program) => (
              <Option key={program.id} value={program.id}>
                {program.program}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Konsentrasi Keahlian:"
          name="konsentrasiKeahlian_id"
          rules={[
            { required: true, message: "Silahkan isi konsentrasi keahlian" },
          ]}
        >
          <Select placeholder="Pilih Konsentrasi Keahlian">
            {filteredKonsentrasiList.map((konsentrasi) => (
              <Option key={konsentrasi.id} value={konsentrasi.id}>
                {konsentrasi.konsentrasi}
              </Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddStudentForm;
