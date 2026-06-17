/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Modal, Select } from "antd";

const { TextArea } = Input;

const AddQuestionCriteriaForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
}) => {
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
      title="Tambah Kriteria Pertanyaan"
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
        <Form.Item
          label="Kriteria Pertanyaan:"
          name="name"
          rules={[
            { required: true, message: "Kriteria Pertanyaan wajib diisi" },
          ]}
        >
          <Input placeholder="Kriteria Pertanyaan" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Kriteria Pertanyaan:"
          name="description"
          rules={[
            {
              required: true,
              message: "Deskripsi Kriteria Pertanyaan wajib diisi",
            },
          ]}
        >
          <TextArea placeholder="Deskripsi Kriteria Pertanyaan" />
        </Form.Item>

        <Form.Item
          label="Kategori:"
          name="category"
          rules={[{ required: true, message: "Kategori wajib diisi" }]}
        >
          <Select style={{ width: 120 }} placeholder="Kategori">
            <Select.Option value="Cognitive">Cognitive</Select.Option>
            <Select.Option value="Non Cognitive">Non Cognitive</Select.Option>
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddQuestionCriteriaForm;
