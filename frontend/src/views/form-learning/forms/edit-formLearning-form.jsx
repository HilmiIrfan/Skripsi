/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const EditFormLearningForm = ({
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
      title="Edit Bentuk Pembelajaran"
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
        <Form.Item label="ID Bentuk Pembelajaran:" name="id">
          <Input disabled />
        </Form.Item>

        <Form.Item
          label="Nama Bentuk Pembelajaran:"
          name="name"
          rules={[
            {
              required: true,
              message: "Silahkan isikan nama bentuk pembelajaran",
            },
          ]}
        >
          <Input placeholder="Nama Bentuk Pembelajaran" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Bentuk Pembelajaran:"
          name="description"
          rules={[
            {
              required: true,
              message: "Silahkan isikan deskripsi bentuk pembelajaran",
            },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi Bentuk Pembelajaran" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditFormLearningForm;
