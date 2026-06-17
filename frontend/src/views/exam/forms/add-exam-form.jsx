/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { DatePicker, Form, Input, InputNumber, Modal, Select } from "antd";

const { TextArea } = Input;

const AddExamForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  questions,
  rps,
  rpsDetail,
  handleGetRPSDetail,
  handleUpdateQuestion,
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
      width={1000}
      title="Tambah Exam"
      visible={visible}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="Nama Ujian:"
          name="name"
          rules={[{ required: true, message: "Nama wajib diisi" }]}
        >
          <Input placeholder="Nama" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Ujian:"
          name="description"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi ujian" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi pertanyaan" />
        </Form.Item>

        <Form.Item
          label="Nilai Minimum:"
          name="min_grade"
          rules={[{ required: true, message: "Nilai minimum wajib diisi" }]}
        >
          <InputNumber
            placeholder="Nilai minimum"
            min={1}
            style={{ width: 300 }}
          />
        </Form.Item>

        <Form.Item
          label="Durasi Ujian:"
          name="duration"
          rules={[{ required: true, message: "Durasi ujian wajib diisi" }]}
        >
          <InputNumber
            placeholder="Durasi ujian (menit)"
            min={1}
            style={{ width: 300 }}
          />
        </Form.Item>

        <Form.Item
          label="RPS:"
          name="rps_id"
          rules={[{ required: true, message: "Silahkan pilih RPS" }]}
        >
          <Select
            style={{ width: 300 }}
            placeholder="Pilih RPS"
            onChange={handleUpdateQuestion}
          >
            {rps.map((arr, key) => (
              <Select.Option value={arr.id} key={`rps-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Pilih ujian:"
          name="type_exercise"
          rules={[{ required: true, message: "Silahkan pilih RPS Detail" }]}
        >
          <Select
            style={{ width: 300 }}
            placeholder="Pilih RPS Detail"
            onChange={handleGetRPSDetail}
          >
            <Select.Option value="1-8">
              UTS (Weeks 1-8):{" "}
              {rpsDetail
                .filter((arr) => arr.week >= 1 && arr.week <= 8)
                .map((arr) => arr.week)
                .join(", ")}
            </Select.Option>
            <Select.Option value="1-18">
              UAS (Weeks 1-16):{" "}
              {rpsDetail
                .filter((arr) => arr.week >= 1 && arr.week <= 16)
                .map((arr) => arr.week)
                .join(", ")}
            </Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Tanggal Mulai:"
          name="date_start"
          rules={[{ required: true, message: "Tanggal Mulai wajib diisi" }]}
        >
          <DatePicker
            showTime
            format="YYYY-MM-DD HH:mm:ss"
            placeholder="Pilih tanggal"
          />
        </Form.Item>

        <Form.Item
          label="Tanggal Selesai:"
          name="date_end"
          rules={[{ required: true, message: "Tanggal Selesai wajib diisi" }]}
        >
          <DatePicker
            showTime
            format="YYYY-MM-DD HH:mm:ss"
            placeholder="Pilih tanggal"
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddExamForm;
