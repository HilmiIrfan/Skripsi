/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, InputNumber, Modal, Select } from "antd";
import TextArea from "antd/lib/input/TextArea";

const AddRPSForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  formLearnings,
  learningMethods,
  assessmentCriterias,
  appraisalForms,
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

  const handleSubmit = () => {
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
      width={1000}
      title="Tambah RPS"
      visible={visible}
      onCancel={onCancel}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="Minggu Ke:"
          name="week"
          rules={[{ required: true, message: "Minggu wajib diisi" }]}
        >
          <InputNumber placeholder="Minggu ke" min={1} style={{ width: 300 }} />
        </Form.Item>

        <Form.Item
          label="Sub CP MK:"
          name="sub_cp_mk"
          rules={[{ required: true, message: "Sub CP MK wajib diisi" }]}
        >
          <TextArea placeholder="Sub CP MK" style={{ width: 300 }} />
        </Form.Item>

        <Form.Item
          label="Materi Pembelajaran :"
          name="learning_materials"
          rules={[
            { required: true, message: "Silahkan Isikan Materi Pembelajaran" },
          ]}
        >
          <Select
            mode="tags"
            style={{ width: 300 }}
            placeholder="Isikan Materi Pembelajaran"
          />
        </Form.Item>

        <Form.Item
          label="Bentuk Pembelajaran:"
          name="form_learning_id"
          rules={[
            { required: true, message: "Silahkan pilih Bentuk Pembelajaran" },
          ]}
        >
          <Select
            style={{ width: 300 }}
            placeholder="Pilih Bentuk Pembelajaran"
          >
            {formLearnings.map((arr, key) => (
              <Select.Option value={arr.id} key={`form-learning-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Metode Pembelajaran:"
          name="learning_methods"
          rules={[
            { required: true, message: "Silahkan pilih Metode Pembelajaran" },
          ]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Metode Pembelajaran"
          >
            {learningMethods.map((arr, key) => (
              <Select.Option value={arr.id} key={`learning-method-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Penugasan:"
          name="assignments"
          rules={[{ required: true, message: "Silahkan isikan Penugasan" }]}
        >
          <Select
            mode="tags"
            style={{ width: 300 }}
            placeholder="Isikan Penugasan"
          />
        </Form.Item>

        <Form.Item
          label="Estimasi Waktu:"
          name="estimated_times"
          rules={[{ required: true, message: "Silahkan isi estimasi waktu" }]}
        >
          <Select
            mode="tags"
            style={{ width: 300 }}
            placeholder="Isikan Estimasi Waktu"
          />
        </Form.Item>

        <Form.Item
          label="Pengalaman Belajar Mahasiswa:"
          name="student_learning_experiences"
          rules={[
            {
              required: true,
              message: "Silahkan isi pengalaman belajar mahasiswa",
            },
          ]}
        >
          <Select
            mode="tags"
            style={{ width: 300 }}
            placeholder="Isikan Pengalaman Belajar Mahasiswa"
          />
        </Form.Item>

        <Form.Item
          label="Kriteria Penilaian :"
          name="assessment_criterias"
          rules={[
            { required: true, message: "Silahkan pilih kriteria penilaian" },
          ]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Kriteria Penilaian"
          >
            {assessmentCriterias.map((arr, key) => (
              <Select.Option value={arr.id} key={`assessment-criteria-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Bentuk Penilaian:"
          name="appraisal_forms"
          rules={[
            { required: true, message: "Silahkan pilih bentuk penilaian" },
          ]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Bentuk Penilaian"
          >
            {appraisalForms.map((arr, key) => (
              <Select.Option value={arr.id} key={`subject-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Indikator Penilaian:"
          name="assessment_indicators"
          rules={[
            { required: true, message: "Silahkan isi indikator penilaian" },
          ]}
        >
          <Select
            mode="tags"
            style={{ width: 300 }}
            placeholder="Isikan Indikator Penilaian"
          />
        </Form.Item>

        <Form.Item
          label="Bobot:"
          name="weight"
          rules={[{ required: true, message: "Bobot wajib diisi" }]}
        >
          <InputNumber placeholder="Bobot" min={1} style={{ width: 300 }} />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddRPSForm;
