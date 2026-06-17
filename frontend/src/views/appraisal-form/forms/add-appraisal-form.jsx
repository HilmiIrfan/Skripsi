/* eslint-disable react/prop-types */
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const AddAppraisalForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();

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
      title="Tambah Formulir Penilaian"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          name="name"
          label="Nama Formulir Penilaian"
          rules={[
            {
              required: true,
              message: "Silahkan isikan nama formulir penilaian",
            },
          ]}
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
        >
          <TextArea rows={4} placeholder="Deskripsi Pengguna" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddAppraisalForm;
