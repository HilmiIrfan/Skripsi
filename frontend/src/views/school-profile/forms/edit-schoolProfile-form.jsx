/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { DatePicker, Form, Input, Select, Modal, Upload } from "antd";
import { InboxOutlined } from "@ant-design/icons";

const { TextArea } = Input;

const EditSchoolProfileForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();

  const {
    npsn,
    status,
    bentukKependidikan,
    kepemilikan,
    SKPendirianSekolah,
    SKIzinOperasional,
  } = currentRowData;

  const formItemLayout = {
    labelCol: {
      sm: { span: 4 },
    },
    wrapperCol: {
      sm: { span: 16 },
    },
  };

  return (
    <Modal
      title="mengedit"
      visible={visible}
      onCancel={onCancel}
      onOk={() => {
        form
          .validateFields()
          .then((values) => {
            onOk(values);
          })
          .catch((info) => {
            console.log("Validate Failed:", info);
          });
      }}
      confirmLoading={confirmLoading}
    >
      <Form
        {...formItemLayout}
        form={form}
        name="EditSchoolProfileForm"
        initialValues={{
          npsn,
          status,
          bentukKependidikan,
          kepemilikan,
          SKPendirianSekolah,
          SKIzinOperasional,
          school_id: "RWK001",
        }}
      >
        <Form.Item
          label="NPSN:"
          name="npsn"
          rules={[{ required: true, message: "Silahkan isi NPSN Sekolah!" }]}
        >
          <Input placeholder="NPSN" />
        </Form.Item>

        <Form.Item label="Status:" name="status">
          <Input placeholder="Status" />
        </Form.Item>

        <Form.Item label="Bentuk Kependidikan:" name="bentukKependidikan">
          <Input placeholder="Bentuk Kependidikan" />
        </Form.Item>

        <Form.Item label="Status Kepemilikan:" name="kepemilikan">
          <Input placeholder="Status Kepemilikan" />
        </Form.Item>

        <Form.Item label="SK Pendirian Sekolah:" name="SKPendirianSekolah">
          <Input placeholder="SK Pendirian Sekolah" />
        </Form.Item>

        <Form.Item label="Tanggal SK Pendirian :" name="tglSKPendirian">
          <DatePicker
            showTime
            format="YYYY-MM-DD"
            placeholder="Pilih tanggal"
          />
        </Form.Item>

        <Form.Item label="SK Izin Operasional:" name="SKIzinOperasional">
          <Input placeholder="SK Izin Operasional" />
        </Form.Item>

        <Form.Item label="Tanggal SK Izin Operasional:" name="tglSKOperasional">
          <DatePicker
            showTime
            format="YYYY-MM-DD"
            placeholder="Pilih tanggal"
          />
        </Form.Item>

        <Form.Item label="Sekolah:" name="school_id">
          <Select style={{ width: 240 }}>
            <Select.Option value="RWK001">
              SMK Negeri Rowokangkung
            </Select.Option>
            <Select.Option value="TMP001">SMK Negeri Tempeh</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item label="Foto Profil Sekolah" name="file">
          <Upload.Dragger beforeUpload={() => false} listType="picture">
            <p className="ant-upload-drag-icon">
              <InboxOutlined />
            </p>
            <p className="ant-upload-text">
              Click or drag file to this area to upload
            </p>
            <p className="ant-upload-hint">
              Support for a single or bulk upload.
            </p>
          </Upload.Dragger>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditSchoolProfileForm;
