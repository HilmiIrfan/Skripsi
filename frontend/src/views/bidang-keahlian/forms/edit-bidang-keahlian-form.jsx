/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useEffect } from "react";
import { Form, Input, Select, Modal } from "antd";

const { TextArea } = Input;

const EditBidangKeahlianForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue(currentRowData);
  }, [currentRowData, form]);

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
      title="Edit Bidang Keahlian"
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
          label="ID Bidang Keahlian:"
          name="id"
          rules={[
            { required: true, message: "Silahkan isikan id bidang keahlian" },
          ]}
        >
          <Input placeholder="ID Bidang Keahlian" />
        </Form.Item>

        <Form.Item
          label="Nama Bidang Keahlian:"
          name="bidang"
          rules={[
            { required: true, message: "Silahkan isikan Bidang Keahlian" },
          ]}
        >
          <Input placeholder="ID Bidang Keahlian" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditBidangKeahlianForm;
