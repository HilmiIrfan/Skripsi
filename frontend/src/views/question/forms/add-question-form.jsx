/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState } from "react";
import { Form, Input, Modal, Select, Upload, Checkbox } from "antd";
import { InboxOutlined } from "@ant-design/icons";

const { TextArea } = Input;

const AddQuestionForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState([]);

  const handleBeforeUpload = (file) => {
    if (file) {
      setFileList((prevFileList) => [...prevFileList, file]);
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
      title="Tambah Pertanyaan"
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
        encType="multipart/form-data"
        initialValues={{
          question_type: "NORMAL",
          answer_type: "MULTIPLE_CHOICE",
          description: "Default value in this form",
          explanation: "Default value in this form",
          examType: [],
          examType2: [],
          examType3: [],
        }}
      >
        <Form.Item
          label="Pertanyaan :"
          name="title"
          rules={[{ required: true, message: "Silahkan isikan pertanyaan" }]}
        >
          <Input placeholder="Pertanyaan" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Pertanyaan:"
          name="description"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi pertanyaan" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi pertanyaan" />
        </Form.Item>

        <Form.Item
          label="Penjelasan:"
          name="explanation"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi penjelasan" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi penjelasan" />
        </Form.Item>

        <Form.Item
          label="Tipe Pertanyaan:"
          name="question_type"
          rules={[
            { required: true, message: "Silahkan pilih tipe pertanyaan" },
          ]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih tipe pertanyaan">
            <Select.Option value="IMAGE">Gambar</Select.Option>
            <Select.Option value="AUDIO">Musik / Audio</Select.Option>
            <Select.Option value="VIDEO">Video</Select.Option>
            <Select.Option value="NORMAL">Normal</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Tipe Jawaban:"
          name="answer_type"
          rules={[{ required: true, message: "Silahkan pilih tipe jawaban" }]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih tipe jawaban">
            <Select.Option value="MULTIPLE_CHOICE">Pilihan Ganda</Select.Option>
            <Select.Option value="BOOLEAN">Benar / Salah</Select.Option>
            <Select.Option value="COMPLETION">
              Menyelesaikan kalimat rumpang
            </Select.Option>
          </Select>
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

        <Form.Item label="untuk latihan soal:" name="examType">
          <Checkbox.Group>
            <Checkbox value="EXERCISE">Exercise</Checkbox>
          </Checkbox.Group>
        </Form.Item>

        <Form.Item label="untuk quiz 1 atau quiz 2:" name="examType2">
          <Checkbox.Group>
            <Checkbox value="QUIZ">Quiz</Checkbox>
          </Checkbox.Group>
        </Form.Item>

        <Form.Item label="untuk UTS atau UAS:" name="examType3">
          <Checkbox.Group>
            <Checkbox value="EXAM">Exam</Checkbox>
          </Checkbox.Group>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddQuestionForm;
