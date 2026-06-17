/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const EditDepartmentForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const { id, name, description } = currentRowData;

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
      title="Edit Jurusan"
      visible={visible}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form
        form={form}
        {...formItemLayout}
        initialValues={{
          id,
          name,
          description,
        }}
      >
        <Form.Item label="ID Jurusan:" name="id">
          <Input disabled />
        </Form.Item>
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
          <TextArea rows={4} placeholder="Deskripsi Jurusan" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditDepartmentForm;
