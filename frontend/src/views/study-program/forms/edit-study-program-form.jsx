/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Modal, Select } from "antd";
const { TextArea } = Input;

const EditStudyProgramForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const { id, name, role, description } = currentRowData;

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

  return (
    <Modal
      title="Edit Program Studi"
      visible={visible}
      onCancel={onCancel}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form
        {...formItemLayout}
        form={form}
        initialValues={{
          id,
          name,
          role,
          description,
        }}
      >
        <Form.Item label="ID Program Studi:" name="id">
          <Input disabled />
        </Form.Item>

        <Form.Item label="Peran:" name="role">
          <Select style={{ width: 120 }} disabled={id === "admin"}>
            <Select.Option value="admin">
              Jurusan Teknologi Informasi
            </Select.Option>
            <Select.Option value="lecture">Jurusan Sipil</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Nama Prodi:"
          name="name"
          rules={[
            { required: true, message: "Silahkan isi nama program studi" },
          ]}
        >
          <Input placeholder="Nama program studi" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Prodi:"
          name="description"
          rules={[
            { required: true, message: "Silahkan isi deskripsi program studi" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi program studi" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditStudyProgramForm;
