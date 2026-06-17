/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const AddReligionForm = ({ visible, onCancel, onOk, confirmLoading }) => {
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

  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        form.resetFields();
        onOk(values);
      })
      .catch((info) => {
        console.log("Validate Failed:", info);
      });
  };

  const handleCancel = () => {
    form.resetFields();
    onCancel();
  };

  return (
    <Modal
      title="Tambah Agama"
      visible={visible}
      onCancel={handleCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form {...formItemLayout} form={form}>
        <Form.Item
          label="Nama Agama"
          name="name"
          rules={[{ required: true, message: "Silahkan isikan nama agama" }]}
        >
          <Input placeholder="Nama Agama" />
        </Form.Item>
        <Form.Item
          label="Deskripsi Agama"
          name="description"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi agama" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi Agama" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddReligionForm;
