/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Select, Modal } from "antd";

const { TextArea } = Input;

const EditLectureForm = ({
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
        name="EditLectureForm"
        initialValues={{
          id,
          name,
          role,
          description,
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

        <Form.Item label="Peran:" name="role">
          <Select style={{ width: 120 }} disabled={id === "admin"}>
            <Select.Option value="admin">admin</Select.Option>
            <Select.Option value="lecture">editor</Select.Option>
            <Select.Option value="student">guest</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item label="Deskripsi Dosen:" name="description">
          <TextArea rows={4} placeholder="请输入Deskripsi Dosen" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditLectureForm;
