/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import { Form, Input, Modal, Select, Upload } from "antd";
import { InboxOutlined } from "@ant-design/icons";

const AddLinguisticValueForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
}) => {
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState([]);
  const [selectedExamTypes, setSelectedExamTypes] = useState([]);

  const handleBeforeUpload = (file) => {
    if (file) {
      setFileList((prevList) => [...prevList, file]);
    }
    return false;
  };

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
      title="Tambah Nilai Linguistik"
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
        name="AddLinguisticValueForm"
        encType="multipart/form-data"
      >
        <Form.Item
          label="Level index:"
          name="name"
          rules={[{ required: true, message: "Silahkan isikan nama" }]}
        >
          <Input placeholder="Name" />
        </Form.Item>

        <Form.Item
          label="Value 1:"
          name="value1"
          rules={[{ required: true, message: "Silahkan isikan value 1" }]}
        >
          <Input placeholder="Value 1" />
        </Form.Item>

        <Form.Item
          label="Value 2:"
          name="value2"
          rules={[{ required: true, message: "Silahkan isikan value 2" }]}
        >
          <Input placeholder="Value 2" />
        </Form.Item>

        <Form.Item
          label="Value 3:"
          name="value3"
          rules={[{ required: true, message: "Silahkan isikan value 3" }]}
        >
          <Input placeholder="Value 3" />
        </Form.Item>

        <Form.Item
          label="Value 4:"
          name="value4"
          rules={[{ required: true, message: "Silahkan isikan value 4" }]}
        >
          <Input placeholder="Value 4" />
        </Form.Item>

        <Form.Item label="File" name="file">
          <Upload.Dragger
            name="file"
            beforeUpload={handleBeforeUpload}
            maxCount={1}
          >
            <p className="ant-upload-drag-icon">
              <InboxOutlined />
            </p>
            <p className="ant-upload-text">
              Click or drag file to this area to upload
            </p>
            <p className="ant-upload-hint">
              Support for a single or bulk upload.
            </p>
          </Upload.Dragger>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddLinguisticValueForm;
