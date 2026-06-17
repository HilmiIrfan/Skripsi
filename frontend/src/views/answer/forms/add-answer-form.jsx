/* eslint-disable react/prop-types */
import { Form, Input, Modal, Select, Upload, Switch } from "antd";
import { InboxOutlined } from "@ant-design/icons";

const { TextArea } = Input;

const AddAnswerForm = ({ visible, onCancel, onOk, confirmLoading }) => {
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
      title="Tambah Jawaban"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout} encType="multipart/form-data">
        <Form.Item
          name="title"
          label="Jawaban"
          rules={[{ required: true, message: "Silahkan isikan pertanyaan" }]}
        >
          <Input placeholder="Jawaban" />
        </Form.Item>

        <Form.Item
          name="description"
          label="Deskripsi Jawaban"
          rules={[
            { required: true, message: "Silahkan isikan deskripsi pertanyaan" },
          ]}
          initialValue="This is the default value"
        >
          <TextArea rows={4} placeholder="Deskripsi pertanyaan" />
        </Form.Item>

        <Form.Item
          name="is_right"
          label="Jawaban Benar / Salah"
          rules={[{ required: true, message: "Silahkan isikan benar / salah" }]}
          initialValue={false}
        >
          <Switch
            checkedChildren="Benar"
            unCheckedChildren="Salah"
            defaultChecked
          />
        </Form.Item>

        <Form.Item
          name="type"
          label="Tipe Jawaban"
          rules={[
            { required: true, message: "Silahkan pilih tipe pertanyaan" },
          ]}
          initialValue="NORMAL"
        >
          <Select style={{ width: 300 }} placeholder="Pilih tipe pertanyaan">
            <Select.Option value="IMAGE">Gambar</Select.Option>
            <Select.Option value="AUDIO">Musik / Audio</Select.Option>
            <Select.Option value="VIDEO">Video</Select.Option>
            <Select.Option value="NORMAL">Normal</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item name="file" label="File">
          <Upload.Dragger name="file" beforeUpload={() => false} maxCount={1}>
            <p className="ant-upload-drag-icon">
              <InboxOutlined />
            </p>
            <p className="ant-upload-text">Klik atau Seret file ke sini</p>
            <p className="ant-upload-hint">support semua file</p>
          </Upload.Dragger>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddAnswerForm;
