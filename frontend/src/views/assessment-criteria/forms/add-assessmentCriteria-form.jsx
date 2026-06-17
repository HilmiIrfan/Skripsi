/* eslint-disable react/prop-types */
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const AddAssessmentCriteriaForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
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
      title="Tambah Penilaian"
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
          label="Nama Penilaian:"
          name="name"
          rules={[
            { required: true, message: "Silahkan isikan nama penilaian" },
          ]}
        >
          <Input placeholder="Nama Penilaian" />
        </Form.Item>

        <Form.Item
          label="Deskripsi Penilaian:"
          name="description"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi penilaian" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi Pengguna" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddAssessmentCriteriaForm;
