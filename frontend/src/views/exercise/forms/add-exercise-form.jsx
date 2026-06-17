/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState } from "react";
import { DatePicker, Form, Input, InputNumber, Modal, Select } from "antd";

const { TextArea } = Input;

const AddExerciseForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  questions,
  rps,
  handleUpdateQuestion,
  rpsDetail,
  handleRPSChange,
  handleExerciseTypeChange,
}) => {
  const [form] = Form.useForm();
  const [selectedOption, setSelectedOption] = useState(null);
  const [randomQuestions, setRandomQuestions] = useState([]);

  const handleSelectChange = (value) => {
    setSelectedOption(value);
    const newRandomQuestions = generateRandomQuestions(value);
    setRandomQuestions(newRandomQuestions);
    const selectedQuestionIds = newRandomQuestions.map(
      (question) => question.id
    );
    form.setFieldsValue({ questions: selectedQuestionIds });
  };

  const generateRandomQuestions = (numQuestions) => {
    // Create a copy of the questions array
    const questionsCopy = [...questions];

    // Shuffle the questions
    for (let i = questionsCopy.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [questionsCopy[i], questionsCopy[j]] = [
        questionsCopy[j],
        questionsCopy[i],
      ];
    }

    // Get the first 'numQuestions' questions
    return questionsCopy.slice(0, numQuestions);
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
      title="Tambah Exercise"
      visible={visible}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="Nama Latihan:"
          name="name"
          rules={[{ required: true, message: "Nama wajib diisi" }]}
        >
          <Input placeholder="Nama" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Latihan:"
          name="description"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi latihan" },
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
          label="Durasi Latihan:"
          name="duration"
          rules={[{ required: true, message: "Durasi latihan wajib diisi" }]}
        >
          <InputNumber
            placeholder="Durasi latihan (menit)"
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
            onChange={handleRPSChange}
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
            placeholder="Pilih tipe latihan ujian"
            onChange={handleExerciseTypeChange}
          >
            <Select.Option value="Latihan quiz 1">
              Latihan quiz 1 (Weeks 1-4)
            </Select.Option>
            <Select.Option value="Latihan quiz 2">
              Latihan quiz 2 (Weeks 9-13)
            </Select.Option>
            <Select.Option value="Latihan UTS">
              Latihan UTS (Weeks 1-8)
            </Select.Option>
            <Select.Option value="Latihan UAS">
              Latihan UAS (Weeks 1-18)
            </Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Pilih Pertanyaan"
          name="questions"
          rules={[{ required: true, message: "Silahkan pilih pertanyaan" }]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Pertanyaan"
          >
            {randomQuestions.map((arr, key) => (
              <Select.Option value={arr.id} key={`question-${key}`}>
                {arr.title}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label="pilih jumlah soal:">
          <Select
            style={{ width: 300 }}
            placeholder="pilih jumlah soal yang akan di ujikan"
            onChange={handleSelectChange}
          >
            {[10, 20, 30, 40, 50].map((value, key) => (
              <Select.Option value={value} key={`option-${key}`}>
                {value}
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

export default AddExerciseForm;
