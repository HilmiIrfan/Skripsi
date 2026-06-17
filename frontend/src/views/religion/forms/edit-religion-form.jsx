/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Modal } from "antd";
const { TextArea } = Input;

const EditReligionForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();

  React.useEffect(() => {
    if (currentRowData) {
      form.setFieldsValue(currentRowData);
    }
  }, [currentRowData, form]);

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

  const handleOk = () => {
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
      title="Edit Agama"
      visible={visible}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
    >
      <Form {...formItemLayout} form={form} initialValues={currentRowData}>
        <Form.Item label="ID Agama" name="id">
          <Input disabled />
        </Form.Item>
        <Form.Item
          label="Nama Agama"
          name="name"
          rules={[{ required: true, message: "Silahkan isikan nama agama" }]}
        >
          <Input placeholder="Nama Agama" />
        </Form.Item>
        <Form.Item
          label="Deskripsi Agama"
          name="description"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi agama" },
          ]}
        >
          <TextArea rows={4} placeholder="Deskripsi Agama" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditReligionForm;
