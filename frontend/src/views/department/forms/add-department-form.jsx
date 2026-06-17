/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const AddDepartmentForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();

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

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      onOk(values);
    } catch (error) {
      console.log("Validation failed:", error);
    }
  };

  return (
    <Modal
      title="Tambah Jurusan"
      visible={visible}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="Nama Jurusan:"
          name="name"
          rules={[{ required: true, message: "Silahkan isikan nama jurusan" }]}
        >
          <Input placeholder="Nama Jurusan" />
        </Form.Item>
        <Form.Item
          label="Deskripsi Jurusan:"
          name="description"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi jurusan" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi Pengguna" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddDepartmentForm;
