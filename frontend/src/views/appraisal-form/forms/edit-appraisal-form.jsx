/* eslint-disable react/prop-types */
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const EditAppraisalForm = ({
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
      title="Edit Formulir Penilaian"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item name="id" label="ID Formulir Penilaian" initialValue={id}>
          <Input disabled />
        </Form.Item>

        <Form.Item
          name="name"
          label="Nama Formulir Penilaian"
          rules={[
            {
              required: true,
              message: "Silahkan isikan nama formulir penilaian",
            },
          ]}
          initialValue={name}
        >
          <Input placeholder="Nama Formulir Penilaian" />
        </Form.Item>

        <Form.Item
          name="description"
          label="Deskripsi Formulir Penilaian"
          rules={[
            {
              required: true,
              message: "Silahkan isikan deskripsi formulir penilaian",
            },
          ]}
          initialValue={description}
        >
          <TextArea rows={4} placeholder="Deskripsi Formulir Penilaian" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditAppraisalForm;
