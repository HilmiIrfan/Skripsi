/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState } from "react";
import { DatePicker, Form, Input, InputNumber, Modal, Select } from "antd";

const { TextArea } = Input;

const AddQuizForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  list_questions,
  rps,
  handleUpdateQuestion,
  quizName,
  quizDesc,
  quizMinGrade,
  quizDuration,
  quizRpsId,
  quizType,
}) => {
  const [form] = Form.useForm();
  const [selectedQuestionCount, setSelectedQuestionCount] = useState(5);

  const handleSelectChange = (value) => {
    setSelectedQuestionCount(value);
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
      width={1000}
      title="Tambah Soal Dalam Quiz"
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
          name: quizName,
          description: quizDesc,
          min_grade: quizMinGrade,
          duration: quizDuration,
          rps_id: quizRpsId,
          type_quiz: quizType,
        }}
      >
        <Form.Item
          label="Nama Kuis:"
          name="name"
          style={{ display: "none" }}
          rules={[{ required: true, message: "Nama wajib diisi" }]}
        >
          <Input placeholder="Nama" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Kuis:"
          name="description"
          style={{ display: "none" }}
          rules={[
            { required: true, message: "Silahkan isikan deskripsi kuis" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi pertanyaan" />
        </Form.Item>

        <Form.Item
          label="Nilai Minimum:"
          name="min_grade"
          style={{ display: "none" }}
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
          style={{ display: "none" }}
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
          style={{ display: "none" }}
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

        <Form.Item label="Pilih jumlah soal:">
          <Select
            style={{ width: 300 }}
            placeholder="Pilih jumlah soal yang akan di ujikan"
            onChange={handleSelectChange}
          >
            {[5, 10, 20, 30, 40, 50].map((value, key) => (
              <Select.Option value={value} key={`option-${key}`}>
                {value}
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
            {list_questions.slice(0, selectedQuestionCount).map((arr, key) => (
              <Select.Option value={arr.rank} key={`list_questions-${key}`}>
                {arr.title} (Rank: {arr.rank})
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Tipe Kuis:"
          name="type_quiz"
          style={{ display: "none" }}
          rules={[{ required: true, message: "Tipe Kuis Wajib diisi" }]}
        >
          <Select style={{ width: 120 }} placeholder="Tipe Kuis">
            <Select.Option value="quiz1">Kuis 1</Select.Option>
            <Select.Option value="quiz2">Kuis 2</Select.Option>
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

export default AddQuizForm;
