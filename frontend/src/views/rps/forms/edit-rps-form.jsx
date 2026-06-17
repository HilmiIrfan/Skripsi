/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Select, Modal, InputNumber } from "antd";

const EditRPSForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const {
    id,
    name,
    sks,
    semester,
    cpl_prodi,
    cpl_mk,
    learningMediaSoftwares,
    learningMediaHardwares,
    subjects,
    lectures,
    studyPrograms,
  } = currentRowData;

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
      title="mengedit"
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
          sks,
          semester,
          cpl_prodi,
          cpl_mk,
          learningMediaSoftwares:
            currentRowData.learning_media_softwares?.map(
              (software) => software.id
            ) || [],
          learningMediaHardwares:
            currentRowData.learning_media_hardwares?.map(
              (hardware) => hardware.id
            ) || [],
          subjects: currentRowData.subject?.id,
          dev_lecturers:
            currentRowData.dev_lecturers?.map((lecturer) => lecturer.name) ||
            [],
          study_program_id: currentRowData.study_program_id,
        }}
      >
        <Form.Item label="ID Pengguna:" name="id">
          <Input disabled />
        </Form.Item>

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
          label="Learning Media Softwares"
          name="learningMediaSoftwares"
          rules={[
            { required: true, message: "Learning Media Softwares is required" },
          ]}
        >
          <Select
            mode="multiple"
            style={{ width: 300 }}
            placeholder="Select Learning Media Softwares"
          >
            {learningMediaSoftwares?.map((software, key) => (
              <Select.Option value={software.id} key={`software-${key}`}>
                {software.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Hardware Media Pembelajaran:"
          name="learningMediaHardwares"
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
            {learningMediaHardwares?.map((hardware, key) => (
              <Select.Option value={hardware.id} key={`hardware-${key}`}>
                {hardware.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Subject ID"
          name="subjects"
          rules={[{ required: true, message: "Subject ID is required" }]}
        >
          <Select style={{ width: 300 }} placeholder="Subject ID">
            {subjects?.map((subject, key) => (
              <Select.Option value={subject.id} key={`subject-${key}`}>
                {subject.name}
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
            {lectures?.map((arr, key) => (
              <Select.Option value={arr.id} key={`dev-lecture-${key}`}>
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
            {studyPrograms?.map((arr, key) => (
              <Select.Option value={arr.id} key={`study-program-${key}`}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditRPSForm;
