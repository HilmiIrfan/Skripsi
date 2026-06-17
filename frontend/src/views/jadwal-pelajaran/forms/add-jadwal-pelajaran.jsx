/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import { Form, Input, Modal, Select } from "antd";
import { getLectures } from "@/api/lecture";
import { getMapel } from "@/api/mapel";

const { Option } = Select;

const AddJadwalPelajaranForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
}) => {
  const [form] = Form.useForm();
  const [guruList, setGuruList] = useState([]);
  const [mapelList, setMapelList] = useState([]);

  const fetchGuruList = async () => {
    try {
      const result = await getLectures();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const guruList = content.map((guru) => ({
          id: guru.id,
          name: guru.name,
          nidn: guru.nidn,
        }));
        setGuruList(guruList);
      }
    } catch (error) {
      console.error("Error fetching guru data: ", error);
    }
  };

  const fetchMapelList = async () => {
    try {
      const result = await getMapel();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const mapelList = content.map((mapel) => ({
          idMapel: mapel.idMapel,
          name: mapel.name,
        }));
        setMapelList(mapelList);
      }
    } catch (error) {
      console.error("Error fetching mapel data: ", error);
    }
  };

  useEffect(() => {
    fetchGuruList();
    fetchMapelList();
  }, []);

  const formItemLayout = {
    labelCol: { xs: { span: 24 }, sm: { span: 8 } },
    wrapperCol: { xs: { span: 24 }, sm: { span: 16 } },
  };

  return (
    <Modal
      title="Tambah Jadwal Pelajaran"
      visible={visible}
      onCancel={onCancel}
      onOk={() => {
        form
          .validateFields()
          .then((values) => {
            onOk(values);
          })
          .catch((error) => {
            console.log("Validation failed:", error);
          });
      }}
      confirmLoading={confirmLoading}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="ID Jadwal:"
          name="idJadwal"
          rules={[
            { required: true, message: "Silahkan isikan id jadwal pelajaran" },
          ]}
        >
          <Input placeholder="ID Jadwal" />
        </Form.Item>

        <Form.Item
          label="Guru Pengajar:"
          name="lecture_id"
          rules={[{ required: true, message: "Silahkan isi guru" }]}
        >
          <Select placeholder="Pilih Kelas">
            {guruList.map((guru) => (
              <Option key={guru.id} value={guru.id}>
                {guru.name}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Jabatan:"
          name="jabatan"
          rules={[{ required: true, message: "Silahkan isikan jabatan" }]}
        >
          <Input placeholder="Jabatan" />
        </Form.Item>

        <Form.Item
          label="Mata Pelajaran:"
          name="mapel_id"
          rules={[{ required: true, message: "Silahkan isi mapel" }]}
        >
          <Select placeholder="Pilih Mata Pelajaran">
            {mapelList.map((mapel) => (
              <Option key={mapel.idMapel} value={mapel.idMapel}>
                {mapel.name}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Jam Pelajaran:"
          name="jmlJam"
          rules={[{ required: true, message: "Silahkan isikan jam pelajaran" }]}
        >
          <Input placeholder="Jumlah Jam Pelajaran" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddJadwalPelajaranForm;
