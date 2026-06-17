/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, Select, Modal } from "antd";
import { reqValidatUserID } from "@/api/user";
const { TextArea } = Input;

const AddStudyProgramForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  departments,
}) => {
  const [form] = Form.useForm();

  const validatUserID = async (_, value) => {
    if (value) {
      if (!/^[a-zA-Z0-9]{1,6}$/.test(value)) {
        return Promise.reject("ID Pengguna必须为1-6位数字或字母组合");
      }
      const res = await reqValidatUserID(value);
      const { status } = res.data;
      if (status) {
        return Promise.reject("该ID Pengguna已存在");
      }
      return Promise.resolve();
    }
    return Promise.reject("请输入ID Pengguna");
  };

  const handleSubmit = () => {
    form
      .validateFields()
      .then((values) => {
        onOk(values);
      })
      .catch((info) => {
        console.log("Validate Failed:", info);
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
      title="Tambah Program Studi"
      visible={visible}
      onCancel={onCancel}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="Jurusan:"
          name="department_id"
          rules={[
            {
              required: true,
              message: "Silahkan isi jurusan program studi",
            },
          ]}
        >
          <Select style={{ width: 300 }} placeholder="Pilih Jurusan">
            {departments.map((arr, key) => (
              <Select.Option value={arr.id} key={key}>
                {arr.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Nama Prodi:"
          name="name"
          rules={[
            { required: true, message: "Silahkan isi nama program studi" },
          ]}
        >
          <Input placeholder="Nama program studi" />
        </Form.Item>

        <Form.Item label="Deskripsi Prodi:" name="description">
          <TextArea
            rows={4}
            placeholder="Silahkan isi deskripsi program studi"
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddStudyProgramForm;
