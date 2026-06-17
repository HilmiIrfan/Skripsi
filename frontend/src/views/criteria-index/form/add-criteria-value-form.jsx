/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from "react";
import { reqUserInfo } from "@/api/user";
import { Form, Input, InputNumber, Modal, Select } from "antd";

const AddTeamTeachingForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  questionID,
  userID,
  linguisticValues,
}) => {
  const [form] = Form.useForm();

  const criteriaNames = [
    "Evaluation",
    "Synthesis",
    "Comprehension",
    "Analysis",
    "Difficulty",
    "Reliability",
    "Discrimination",
    "Application",
    "Knowledge",
  ];

  const formItemLayout = {
    labelCol: {
      xs: { span: 10 },
      sm: { span: 9 },
    },
    wrapperCol: {
      xs: { span: 15 },
      sm: { span: 10 },
    },
  };

  return (
    <Modal
      visible={visible}
      title="Berikan Nilai Untuk Soal"
      okText="Add"
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
          label="Question ID"
          name="questionId"
          initialValue={questionID}
          rules={[{ required: true, message: "Please input the Question ID!" }]}
        >
          <Input disabled />
        </Form.Item>

        <Form.Item
          label="User ID"
          name="user_id"
          initialValue={userID}
          rules={[{ required: true, message: "Please input the Question ID!" }]}
          style={{ display: "none" }}
        >
          <Input disabled />
        </Form.Item>

        {criteriaNames.map((name, index) => (
          <Form.Item
            key={name}
            label={`Kriteria ${index + 1}: ${name}`}
            name={`value${index + 1}`}
            rules={[
              {
                required: true,
                message: `Silahkan pilih nilai kriteria ${index + 1}`,
              },
            ]}
          >
            <Select
              style={{ width: 300 }}
              placeholder={"Pilih Nilai Linguistik "}
            >
              {linguisticValues &&
                Array.isArray(linguisticValues) &&
                linguisticValues.map((arr, key) => (
                  <Select.Option
                    value={arr.id}
                    key={`value${index + 1}-${key}`}
                  >
                    {arr.name}
                  </Select.Option>
                ))}
            </Select>
          </Form.Item>
        ))}
      </Form>
    </Modal>
  );
};

export default AddTeamTeachingForm;
