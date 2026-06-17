/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import { Form, Input, Modal, Select } from "antd";
import { getProgramKeahlian } from "@/api/programKeahlian";

const { Option } = Select;

const EditKonsentrasiKeahlianForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const [programList, setProgramList] = useState([]);
  const { id, name, description } = currentRowData;

  const fetchProgramKeahlianList = async () => {
    try {
      const result = await getProgramKeahlian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const programList = content.map((program) => ({
          id: program.id,
          program: program.program,
        }));
        setProgramList(programList);
      }
    } catch (error) {
      console.error("Error fetching program data: ", error);
    }
  };

  useEffect(() => {
    fetchProgramKeahlianList();
  }, []);

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
      title="Edit Konsentrasi Keahlian"
      visible={visible}
      onCancel={onCancel}
      onOk={() => {
        form
          .validateFields()
          .then((values) => {
            onOk(values);
          })
          .catch((error) => {
            console.log("Validation failed:", error);
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
        }}
      >
        <Form.Item
          label="ID Konsentrasi Keahlian:"
          name="id"
          rules={[
            {
              required: true,
              message: "Silahkan isikan ID Konsentrasi Keahlian",
            },
          ]}
        >
          <Input placeholder="ID Konsentrasi Keahlian" />
        </Form.Item>

        <Form.Item
          label="Konsentrasi Keahlian:"
          name="konsentrasi"
          rules={[
            { required: true, message: "Silahkan isikan Konsentrasi Keahlian" },
          ]}
        >
          <Input placeholder="Konsentrasi Keahlian" />
        </Form.Item>

        <Form.Item
          label="Program Keahlian:"
          name="programkeahlian_id"
          rules={[{ required: true, message: "Silahkan isi program keahlian" }]}
        >
          <Select placeholder="Pilih Program Keahlian">
            {programList.map((program) => (
              <Option key={program.id} value={program.id}>
                {program.program}
              </Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditKonsentrasiKeahlianForm;
