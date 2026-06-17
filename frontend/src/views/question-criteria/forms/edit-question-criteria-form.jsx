/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Select, Modal } from "antd";

const EditQuestionCriteriaForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const { id, name, description, category } = currentRowData;

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
        initialValues={{
          id,
          name,
          description,
          category,
        }}
      >
        <Form.Item label="ID Dosen:" name="id">
          <Input disabled />
        </Form.Item>

        <Form.Item
          label="Nama:"
          name="name"
          rules={[{ required: true, message: "请输入Nama!" }]}
        >
          <Input placeholder="请输入Nama" />
        </Form.Item>

        <Form.Item
          label="Deskripsi:"
          name="description"
          rules={[{ required: true, message: "请输入Deskripsi!" }]}
        >
          <Input placeholder="请输入Deskripsi" />
        </Form.Item>

        <Form.Item
          label="Kategori:"
          name="category"
          rules={[{ required: true, message: "请选择Kategori!" }]}
        >
          <Select placeholder="请选择Kategori">
            <Select.Option value="Cognitive">Cognitive</Select.Option>
            <Select.Option value="Non Cognitive">Non Cognitive</Select.Option>
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditQuestionCriteriaForm;
