/* eslint-disable react/prop-types */

import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const EditAnswerForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const { id, name, description } = currentRowData;

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      onOk(values);
    } catch (error) {
      console.error("Validation failed:", error);
    }
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
      title="Edit Jurusan"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item name="id" label="ID Jurusan" initialValue={id}>
          <Input disabled />
        </Form.Item>

        <Form.Item
          name="name"
          label="Nama Jurusan"
          rules={[{ required: true, message: "Silahkan isikan nama jurusan" }]}
          initialValue={name}
        >
          <Input placeholder="Nama Jurusan" />
        </Form.Item>

        <Form.Item
          name="description"
          label="Deskripsi Jurusan"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi jurusan" },
          ]}
          initialValue={description}
        >
          <TextArea rows={4} placeholder="Deskripsi Jurusan" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditAnswerForm;
