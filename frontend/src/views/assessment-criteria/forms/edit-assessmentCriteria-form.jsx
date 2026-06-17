/* eslint-disable react/prop-types */
import { useEffect } from "react";
import { Form, Input, Modal } from "antd";

const { TextArea } = Input;

const EditAssessmentCriteriaForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const { id, name, description } = currentRowData;

  useEffect(() => {
    form.setFieldsValue({
      id,
      name,
      description,
    });
  }, [form, id, name, description]);

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
      title="Edit Penilaian"
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
        <Form.Item label="ID Penilaian:" name="id">
          <Input disabled />
        </Form.Item>

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
          <TextArea rows={4} placeholder="Deskripsi Penilaian" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditAssessmentCriteriaForm;
