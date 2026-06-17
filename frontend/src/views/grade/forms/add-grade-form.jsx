/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Select, Modal } from "antd";
import { reqValidatUserID } from "@/api/user";

const { TextArea } = Input;

const AddGradeForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();

  const validateUserID = async (_, value) => {
    if (value) {
      if (!/^[a-zA-Z0-9]{1,6}$/.test(value)) {
        throw new Error("ID Pengguna必须为1-6位数字或字母组合");
      }
      let res = await reqValidatUserID(value);
      const { status } = res.data;
      if (status) {
        throw new Error("该ID Pengguna已存在");
      }
    } else {
      throw new Error("请输入ID Pengguna");
    }
  };

  const formItemLayout = {
    labelCol: {
      sm: { span: 4 },
    },
    wrapperCol: {
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
      title="mengedit"
      visible={visible}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form
        form={form}
        {...formItemLayout}
        initialValues={{
          role: "admin",
        }}
      >
        <Form.Item
          label="ID Pengguna:"
          name="id"
          rules={[
            {
              required: true,
              validator: validateUserID,
            },
          ]}
        >
          <Input placeholder="请输入ID Pengguna" />
        </Form.Item>

        <Form.Item
          label="Nama:"
          name="name"
          rules={[{ required: true, message: "请输入Nama!" }]}
        >
          <Input placeholder="请输入Nama" />
        </Form.Item>

        <Form.Item label="Peran:" name="role">
          <Select style={{ width: 120 }}>
            <Select.Option value="admin">admin</Select.Option>
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

export default AddGradeForm;
