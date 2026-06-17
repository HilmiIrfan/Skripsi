/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const AddLearningMethodForm = ({ visible, onCancel, onOk, confirmLoading }) => {
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

  return (
    <Modal
      title="Tambah Metode Pembelajaran"
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
      <Form {...formItemLayout} form={form} name="AddLearningMethodForm">
        <Form.Item
          label="Nama Metode Pembelajaran:"
          name="name"
          rules={[
            {
              required: true,
              message: "Silahkan isikan nama metode pembelajaran",
            },
          ]}
        >
          <Input placeholder="Nama Metode Pembelajaran" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Metode Pembelajaran:"
          name="description"
          rules={[
            {
              required: true,
              message: "Silahkan isikan deskripsi metode pembelajaran",
            },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi Pengguna" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddLearningMethodForm;
