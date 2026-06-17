/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import { Form, Input, Modal, Select } from "antd";
import { getBidangKeahlian } from "@/api/bidangKeahlian";

const { Option } = Select;

const AddProgramKeahlianForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
}) => {
  const [form] = Form.useForm();
  const [bidangList, setBidangList] = useState([]);

  const fetchBidangKeahlianList = async () => {
    try {
      const result = await getBidangKeahlian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const formattedBidangList = content.map((bidang) => ({
          id: bidang.id,
          bidang: bidang.bidang,
        }));
        setBidangList(formattedBidangList);
      }
    } catch (error) {
      console.error("Error fetching bidang data: ", error);
    }
  };

  useEffect(() => {
    fetchBidangKeahlianList();
  }, []);

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
      title="Tambah Program Keahlian"
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
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="ID Program Keahlian"
          name="id"
          rules={[
            { required: true, message: "Silahkan isikan ID Program Keahlian" },
          ]}
        >
          <Input placeholder="ID Program Keahlian" />
        </Form.Item>

        <Form.Item
          label="Program Keahlian"
          name="program"
          rules={[
            { required: true, message: "Silahkan isikan Program Keahlian" },
          ]}
        >
          <Input placeholder="Program Keahlian" />
        </Form.Item>

        <Form.Item
          label="Bidang Keahlian"
          name="bidangKeahlian_id"
          rules={[{ required: true, message: "Silahkan isi bidang keahlian" }]}
        >
          <Select placeholder="Pilih Bidang Keahlian">
            {bidangList.map((bidang) => (
              <Option key={bidang.id} value={bidang.id}>
                {bidang.bidang}
              </Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddProgramKeahlianForm;
