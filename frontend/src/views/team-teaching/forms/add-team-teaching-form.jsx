/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Modal, Input, Select } from "antd";
const { TextArea } = Input;

const AddTeamTeachingForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  lecture,
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

  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        onOk(values);
      })
      .catch((info) => {
        console.log("Validate Failed:", info);
      });
  };

  return (
    <Modal
      title="Tambah Team Teaching"
      open={visible}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form {...formItemLayout} form={form}>
        <Form.Item
          label="Nama"
          name="name"
          rules={[
            { required: true, message: "Nama Team teaching wajib diisi" },
          ]}
        >
          <Input placeholder="Nama" />
        </Form.Item>

        <Form.Item
          label="Deskripsi"
          name="description"
          rules={[
            { required: true, message: "Deskripsi Team teaching wajib diisi" },
          ]}
        >
          <TextArea placeholder="Deskripsi Team teaching" />
        </Form.Item>

        <Form.Item
          label="Dosen 1"
          name="lecture"
          rules={[{ required: true, message: "Silahkan pilih dosen" }]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih Dosen">
            {lecture &&
              Array.isArray(lecture) &&
              lecture.map((arr, key) => (
                <Select.Option value={arr.id} key={`lecture-${key}`}>
                  {arr.name}
                </Select.Option>
              ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Dosen 2"
          name="lecture2"
          rules={[{ required: true, message: "Silahkan pilih dosen" }]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih Dosen">
            {lecture &&
              Array.isArray(lecture) &&
              lecture.map((arr, key) => (
                <Select.Option value={arr.id} key={`lecture2-${key}`}>
                  {arr.name}
                </Select.Option>
              ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Dosen 3"
          name="lecture3"
          rules={[{ required: true, message: "Silahkan pilih dosen" }]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih Dosen">
            {lecture &&
              Array.isArray(lecture) &&
              lecture.map((arr, key) => (
                <Select.Option value={arr.id} key={`lecture3-${key}`}>
                  {arr.name}
                </Select.Option>
              ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddTeamTeachingForm;
