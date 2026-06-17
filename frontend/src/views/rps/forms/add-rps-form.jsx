/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, InputNumber, Modal, Select } from "antd";

const AddRPSForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  studyPrograms,
  learningMediaSoftwares,
  learningMediaHardwares,
  subjects,
  lectures,
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

  return (
    <Modal
      width={1000}
      title="Tambah RPS"
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
          label="Nama:"
          name="name"
          rules={[{ required: true, message: "Nama wajib diisi" }]}
        >
          <Input placeholder="Nama" />
        </Form.Item>

        <Form.Item
          label="SKS:"
          name="sks"
          rules={[{ required: true, message: "SKS wajib diisi" }]}
        >
          <InputNumber placeholder="SKS RPS" min={1} style={{ width: 300 }} />
        </Form.Item>

        <Form.Item
          label="Semester:"
          name="semester"
          rules={[{ required: true, message: "Semester wajib diisi" }]}
        >
          <InputNumber placeholder="Semester" min={1} style={{ width: 300 }} />
        </Form.Item>

        <Form.Item
          label="CPL Prodi:"
          name="cpl_prodi"
          rules={[{ required: true, message: "CPL Prodi wajib diisi" }]}
        >
          <Input placeholder="CPL Prodi" />
        </Form.Item>

        <Form.Item
          label="CPL Mata Kuliah:"
          name="cpl_mk"
          rules={[{ required: true, message: "CPL Mata Kuliah wajib diisi" }]}
        >
          <Input placeholder="CPL Mata Kuliah" />
        </Form.Item>

        <Form.Item
          label="Software Media Pembelajaran:"
          name="learning_media_softwares"
          rules={[
            {
              required: true,
              message: "Silahkan pilih Software Media Pembelajaran",
            },
          ]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Software Media Pembelajaran"
          >
            {learningMediaSoftwares.map((arr, key) => (
              <Select.Option
                value={arr.id}
                key={`learning-media-software-${key}`}
              >
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Hardware Media Pembelajaran:"
          name="learning_media_hardwares"
          rules={[
            {
              required: true,
              message: "Silahkan pilih Hardware Media Pembelajaran",
            },
          ]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Hardware Media Pembelajaran"
          >
            {learningMediaHardwares.map((arr, key) => (
              <Select.Option
                value={arr.id}
                key={`learning-media-hardware-${key}`}
              >
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Program Study (Prodi):"
          name="study_program_id"
          rules={[{ required: true, message: "Silahkan pilih prodi" }]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih Prodi">
            {studyPrograms.map((arr, key) => (
              <Select.Option value={arr.id} key={`study-program-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Untuk Mata Kuliah:"
          name="subject_id"
          rules={[{ required: true, message: "Silahkan pilih matkul" }]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih Matkul">
            {subjects.map((arr, key) => (
              <Select.Option value={arr.id} key={`subject-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Mata Kuliah Wajib:"
          name="requirement_subjects"
          rules={[{ required: true, message: "Silahkan pilih matkul wajib" }]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Matkul Wajib"
          >
            {subjects.map((arr, key) => (
              <Select.Option value={arr.id} key={`subject-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Dosen Pengembang:"
          name="dev_lecturers"
          rules={[
            { required: true, message: "Silahkan pilih dosen pengembang" },
          ]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Dosen Pengembang"
          >
            {lectures.map((arr, key) => (
              <Select.Option value={arr.id} key={`dev-lecture-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Dosen Pengampu:"
          name="teaching_lecturers"
          rules={[{ required: true, message: "Silahkan pilih dosen pengampu" }]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Dosen Pengampu"
          >
            {lectures.map((arr, key) => (
              <Select.Option value={arr.id} key={`teaching-lecture-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Dosen Koordinator:"
          name="coordinator_lecturers"
          rules={[
            { required: true, message: "Silahkan pilih dosen koordinator" },
          ]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Pilih Dosen Koordinator"
          >
            {lectures.map((arr, key) => (
              <Select.Option value={arr.id} key={`coordinator-lecture-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Ka Prodi:"
          name="ka_study_program"
          rules={[{ required: true, message: "Silahkan pilih Ka Prodi" }]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih Ka Prodi">
            {lectures.map((arr, key) => (
              <Select.Option value={arr.id} key={`ka-prodi-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddRPSForm;
