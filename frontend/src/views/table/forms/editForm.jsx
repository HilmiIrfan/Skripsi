/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { Form, Input, DatePicker, Select, Rate, Modal } from "antd";
import moment from "moment";
import "moment/locale/zh-cn";
moment.locale("zh-cn");

const EditForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();
  const { id, author, date, readings, star, status, title } = currentRowData;

  const formItemLayout = {
    labelCol: {
      sm: { span: 4 },
    },
    wrapperCol: {
      sm: { span: 16 },
    },
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

  return (
    <Modal
      title="mengedit"
      visible={visible}
      onCancel={onCancel}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
    >
      <Form
        {...formItemLayout}
        form={form}
        initialValues={{
          id,
          title,
          author,
          readings,
          star: star.length,
          status,
          date: moment(date || "YYYY-MM-DD HH:mm:ss"),
        }}
      >
        <Form.Item label="序号:" name="id">
          <Input disabled />
        </Form.Item>

        <Form.Item
          label="标题:"
          name="title"
          rules={[{ required: true, message: "请输入标题!" }]}
        >
          <Input placeholder="标题" />
        </Form.Item>

        <Form.Item label="作者:" name="author">
          <Input disabled />
        </Form.Item>

        <Form.Item label="阅读量:" name="readings">
          <Input disabled />
        </Form.Item>

        <Form.Item label="推荐指数:" name="star">
          <Rate count={3} />
        </Form.Item>

        <Form.Item label="状态:" name="status">
          <Select style={{ width: 120 }}>
            <Select.Option value="published">published</Select.Option>
            <Select.Option value="draft">draft</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="时间:"
          name="date"
          rules={[{ type: "object", required: true, message: "请选择时间!" }]}
        >
          <DatePicker showTime format="YYYY-MM-DD HH:mm:ss" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditForm;
