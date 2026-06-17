/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const EditLearningMediaForm = ({
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
      title="Edit Media Pembelajaran"
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
        name="EditLearningMediaForm"
        initialValues={{
          id,
          name,
          description,
        }}
      >
        <Form.Item label="ID Media Pembelajaran:" name="id">
          <Input disabled />
        </Form.Item>

        <Form.Item
          label="Nama Media Pembelajaran:"
          name="name"
          rules={[
            {
              required: true,
              message: "Silahkan isikan nama media pembelajaran",
            },
          ]}
        >
          <Input placeholder="Nama Media Pembelajaran" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Media Pembelajaran:"
          name="description"
          rules={[
            {
              required: true,
              message: "Silahkan isikan deskripsi media pembelajaran",
            },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi Media Pembelajaran" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditLearningMediaForm;
