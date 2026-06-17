/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const EditSubjectForm = ({
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

  return (
    <Modal
      title="Edit Mata Kuliah"
      visible={visible}
      onCancel={onCancel}
      onOk={() => {
        form
          .validateFields()
          .then((values) => {
            onOk(values);
          })
          .catch((error) => {
            console.log("Validation failed:", error);
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
        }}
      >
        <Form.Item label="ID Mata Kuliah:" name="id">
          <Input disabled />
        </Form.Item>

        <Form.Item
          label="Nama Mata Kuliah:"
          name="name"
          rules={[
            { required: true, message: "Silahkan isikan nama mata kuliah" },
          ]}
        >
          <Input placeholder="Nama Mata Kuliah" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Mata Kuliah:"
          name="description"
          rules={[
            {
              required: true,
              message: "Silahkan isikan deskripsi mata kuliah",
            },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi Mata Kuliah" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditSubjectForm;
