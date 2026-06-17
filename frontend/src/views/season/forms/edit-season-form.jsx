/* eslint-disable react/prop-types */
import { useState, useEffect } from "react";
import { Form, Input, Modal, Select } from "antd";
import { getProgramKeahlian } from "@/api/programKeahlian";

const { Option } = Select;

const EditSeasonForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const [programList, setProgramList] = useState([]);

  const fetchProgramKeahlianList = async () => {
    try {
      const result = await getProgramKeahlian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const formattedProgramList = content.map((program) => ({
          id: program.id,
          program: program.program,
        }));
        setProgramList(formattedProgramList);
      }
    } catch (error) {
      console.error("Error fetching program data: ", error);
    }
  };

  useEffect(() => {
    fetchProgramKeahlianList();
  }, []);

  useEffect(() => {
    if (currentRowData) {
      form.setFieldsValue({
        id: currentRowData.id,
        konsentrasi: currentRowData.name,
        programkeahlian_id: currentRowData.description,
      });
    }
  }, [currentRowData, form]);

  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        onOk(values);
      })
      .catch((error) => {
        console.log("Validation failed:", error);
      });
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
      title="Edit Konsentrasi Keahlian"
      open={visible}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          name="id"
          label="ID Konsentrasi Keahlian"
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
          name="konsentrasi"
          label="Konsentrasi Keahlian"
          rules={[
            { required: true, message: "Silahkan isikan Konsentrasi Keahlian" },
          ]}
        >
          <Input placeholder="Konsentrasi Keahlian" />
        </Form.Item>

        <Form.Item
          name="programkeahlian_id"
          label="Program Keahlian"
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

export default EditSeasonForm;
