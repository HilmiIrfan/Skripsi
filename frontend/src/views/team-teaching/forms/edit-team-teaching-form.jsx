/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useEffect } from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const EditTeamTeachingForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (currentRowData) {
      form.setFieldsValue({
        id: currentRowData.id,
        name: currentRowData.name,
        description: currentRowData.description,
        lecture_id: currentRowData.lecture_id,
      });
    }
  }, [currentRowData, form]);

  return (
    <Modal
      title="Edit Team Teaching"
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
      <Form form={form} layout="vertical">
        <Form.Item label="ID Team Teaching:" name="id">
          <Input disabled />
        </Form.Item>
        <Form.Item
          label="Nama Team Teaching:"
          name="name"
          rules={[
            { required: true, message: "Silahkan isi nama team teaching" },
          ]}
        >
          <Input placeholder="Nama team teaching" />
        </Form.Item>
        <Form.Item
          label="Deskripsi Team Teaching:"
          name="description"
          rules={[
            { required: true, message: "Silahkan isi deskripsi team teaching" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi team teaching" />
        </Form.Item>
        <Form.Item
          label="ID Lecture:"
          name="lecture_id"
          rules={[{ required: true, message: "Silahkan isi id lecture" }]}
        >
          <Input placeholder="ID lecture" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditTeamTeachingForm;
