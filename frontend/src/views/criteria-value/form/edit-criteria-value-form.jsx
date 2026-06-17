/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useEffect } from "react";
import { Form, Input, Select, Modal } from "antd";

const { TextArea } = Input;

const EditCriteriaValueForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const { id, name, role, description } = currentRowData;

  useEffect(() => {
    form.setFieldsValue(currentRowData);
  }, [currentRowData, form]);

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
      <Form {...formItemLayout} form={form}>
        <Form.Item label="ID Pengguna:" name="id">
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

        <Form.Item label="Deskripsi Pengguna:" name="description">
          <TextArea rows={4} placeholder="请输入Deskripsi Pengguna" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditCriteriaValueForm;
