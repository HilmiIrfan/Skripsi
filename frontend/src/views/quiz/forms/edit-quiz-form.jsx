/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Select, Modal, InputNumber, DatePicker } from "antd";
import moment from "moment";

const { TextArea } = Input;

const EditQuizForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
  rpsAll,
  questions,
  handleUpdateQuestion,
}) => {
  const [form] = Form.useForm();
  const { id, name, description, min_grade, duration, date_start, date_end } =
    currentRowData;

  const formItemLayout = {
    labelCol: {
      sm: { span: 4 },
    },
    wrapperCol: {
      sm: { span: 16 },
    },
  };

  return (
    <Modal
      width={1000}
      title="Edit Quiz"
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
        initialValues={{
          id,
          name,
          description,
          min_grade,
          duration,
          date_start: moment(date_start),
          date_end: moment(date_end),
        }}
      >
        <Form.Item label="ID Kuis:" name="id">
          <Input disabled />
        </Form.Item>

        <Form.Item
          label="Nama Kuis:"
          name="name"
          rules={[{ required: true, message: "Nama wajib diisi" }]}
        >
          <Input placeholder="请输入Nama" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Kuis:"
          name="description"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi kuis" },
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
          label="Durasi Kuis:"
          name="duration"
          rules={[{ required: true, message: "Durasi kuis wajib diisi" }]}
        >
          <InputNumber
            placeholder="Durasi kuis (menit)"
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
            {rpsAll.map((arr, key) => (
              <Select.Option value={arr.id} key={`rps-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Pertanyaan:"
          name="questions"
          rules={[{ required: true, message: "Silahkan pilih pertanyaan" }]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Pertanyaan"
          >
            {questions.map((arr, key) => (
              <Select.Option value={arr.id} key={`question-${key}`}>
                {arr.title}
              </Select.Option>
            ))}
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

export default EditQuizForm;
