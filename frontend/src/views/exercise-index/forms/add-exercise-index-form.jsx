/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import { DatePicker, Form, Input, InputNumber, Modal, Select } from "antd";
import moment from "moment";

const { TextArea } = Input;

const AddExerciseForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  rps,
  questions,
  exercise,
}) => {
  const [form] = Form.useForm();
  const [selectedOption, setSelectedOption] = useState(null);
  const [randomQuestions, setRandomQuestions] = useState([]);

  useEffect(() => {
    if (exercise && exercise.rps) {
      const { id, name, description, min_grade, duration, rps, date_end } =
        exercise;

      form.setFieldsValue({
        id,
        name,
        description,
        min_grade,
        duration,
        rps_id: rps.id,
        date_end: moment(date_end),
      });
    }
  }, [exercise, form]);

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
    const questionsCopy = [...questions];

    for (let i = questionsCopy.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [questionsCopy[i], questionsCopy[j]] = [
        questionsCopy[j],
        questionsCopy[i],
      ];
    }

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
        <Form.Item label="ID" name="id">
          <Input />
        </Form.Item>

        <Form.Item label="Name" name="name">
          <Input />
        </Form.Item>

        <Form.Item label="Description" name="description">
          <Input />
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
          <Select style={{ width: 300 }} placeholder="Pilih RPS">
            {rps.map((arr, key) => (
              <Select.Option value={arr.id} key={`rps-${key}`}>
                {arr.name}
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
