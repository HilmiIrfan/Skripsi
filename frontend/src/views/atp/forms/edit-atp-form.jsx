/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import {
  Form,
  Input,
  Modal,
  Select,
  Table,
  Tabs,
  Row,
  Col,
  Button,
  message,
} from "antd";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";
import { getKelas } from "@/api/kelas";
import { getTahunAjaran } from "@/api/tahun-ajaran";
import { getSemester } from "@/api/semester";
import { getMapel } from "@/api/mapel";
import { getKonsentrasiSekolah } from "@/api/konsentrasiKeahlianSekolah";
import { getElemen } from "@/api/elemen";
import { getACP } from "@/api/acp";
import { getATP } from "@/api/atp";
import { useFormFilterEditAcp } from "@/helper/acp/formFilterAcpHelperEdit";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;

const EditATPForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [atp, setATP] = useState([]);
  const [form] = Form.useForm();

  const [userSchoolId, setUserSchoolId] = useState([]); // State untuk menyimpan ID sekolah user
  const [schoolList, setSchoolList] = useState([]);
  const [konsentrasiKeahlianSekolahList, setKonsentrasiKeahlianSekolahList] =
    useState([]);
  const [tableLoading, setTableLoading] = useState(false);

  const [initialData, setInitialData] = useState(null);

  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo(); // Ambil data user dari API
      setUserSchoolId(response.data.school_id); // Simpan ID sekolah user ke state
      console.log("User School ID: ", response.data.school_id);
    } catch (error) {
      message.error("Gagal mengambil informasi pengguna");
    }
  };

  const fetchSchoolList = async () => {
    try {
      const result = await getSchool();
      if (result.data.statusCode === 200) {
        setSchoolList(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const fetchATP = async () => {
    setTableLoading(true);
    try {
      const result = await getATP();
      if (result.data.statusCode === 200) {
        setATP(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setTableLoading(false);
    }
  };

  const fetchKonsentrasiKeahlianSekolahList = async () => {
    try {
      const result = await getKonsentrasiSekolah();
      if (result.data.statusCode === 200) {
        setKonsentrasiKeahlianSekolahList(result.data.content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const {
    renderTahunAjaranSelect,
    renderSemesterSelect,
    renderKelasSelect,
    renderMapelSelect,
    renderElemenSelect,
    renderAcpSelect,
  } = useFormFilterEditAcp(currentRowData, initialData);

  // Fetch data awal
  useEffect(() => {
    const fetchData = async () => {
      try {
        setTableLoading(true);
        const [
          tahunAjaranRes,
          semesterRes,
          kelasRes,
          mapelRes,
          elemenRes,
          acpRes,
        ] = await Promise.all([
          getTahunAjaran(),
          getSemester(),
          getKelas(),
          getMapel(),
          getElemen(),
          getACP(),
        ]);

        setInitialData({
          tahunAjaranList: tahunAjaranRes.data.content || [],
          semesterList: semesterRes.data.content || [],
          kelasList: kelasRes.data.content || [],
          mapelList: mapelRes.data.content || [],
          elemenList: elemenRes.data.content || [],
          acpList: acpRes.data.content || [],
        });
      } catch (error) {
        message.error("Gagal memuat data");
      } finally {
        setTableLoading(false);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    fetchUserInfo();
    fetchSchoolList();
    fetchKonsentrasiKeahlianSekolahList();
    fetchATP();

    if (currentRowData) {
      form.setFieldsValue({
        idAtp: currentRowData.idAtp,
        namaAtp: currentRowData.namaAtp,
        jumlahJpl: currentRowData.jumlahJpl,
        idKelas: currentRowData.kelas?.idKelas,
        idTahun: currentRowData.tahunAjaran?.idTahun,
        idSemester: currentRowData.semester?.idSemester,
        idMapel: currentRowData.mapel?.idMapel,
        idKonsentrasiSekolah:
          currentRowData.konsentrasiKeahlianSekolah?.idKonsentrasiSekolah,
        idElemen: currentRowData.elemen?.idElemen,
        idAcp: currentRowData.acp?.idAcp,
        idSchool: currentRowData.school?.idSchool,
      });
    }
  }, [currentRowData, form]);

  useEffect(() => {
    if (userSchoolId) {
      form.setFieldsValue({ idSchool: userSchoolId });
    }
  }, [userSchoolId, form]);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      onOk(values);
    } catch (error) {
      console.error("Validation failed:", error);
    }
  };

  return (
    <Modal
      title="Edit Analisa Tujuan Pembelajaran"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
      okText="Simpan"
      width={1000} // Mengatur lebar modal agar lebih luas
    >
      <Form form={form} layout="vertical">
        <Row gutter={16}>
          <Col xs={24} sm={24} md={24}>
            <Form.Item
              label="Sekolah:"
              name="idSchool"
              style={{ display: "none" }}
              rules={[{ required: true, message: "Silahkan pilih Sekolah" }]}
            >
              <Select defaultValue={userSchoolId} disabled>
                {schoolList
                  .filter(({ idSchool }) => idSchool === userSchoolId) // Hanya menampilkan sekolah user
                  .map(({ idSchool, nameSchool }) => (
                    <Option key={idSchool} value={idSchool}>
                      {nameSchool}
                    </Option>
                  ))}
              </Select>
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12}>
            <Form.Item
              label="Konsentrasi Keahlian Sekolah:"
              name="idKonsentrasiSekolah"
              rules={[
                {
                  required: true,
                  message: "Silahkan pilih Konsentrasi Keahlian Sekolah",
                },
              ]}
            >
              <Select
                placeholder="Pilih Konsentrasi Keahlian Sekolah"
                showSearch
                optionFilterProp="children"
                filterOption={(input, option) =>
                  option.children.toLowerCase().includes(input.toLowerCase())
                }
              >
                {konsentrasiKeahlianSekolahList.map(
                  ({ idKonsentrasiSekolah, namaKonsentrasiSekolah }) => (
                    <Option
                      key={idKonsentrasiSekolah}
                      value={idKonsentrasiSekolah}
                    >
                      {namaKonsentrasiSekolah}
                    </Option>
                  )
                )}
              </Select>
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderTahunAjaranSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderSemesterSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderKelasSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderMapelSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={12}>
            {renderElemenSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={24}>
            {renderAcpSelect(form)}
          </Col>
          <Col xs={24} sm={24} md={24}>
            <Form.Item
              label="Jumlah JPL:"
              name="jumlahJpl"
              rules={[
                {
                  required: true,
                  message: "Silahkan isi Jumlah JPL",
                },
              ]}
            >
              <Input placeholder="Masukkan Jumlah JPL" />
            </Form.Item>
          </Col>
          <Col xs={24} sm={24} md={24}>
            <Form.Item
              label="Nama Tujuan Pembelajaran:"
              name="namaAtp"
              rules={[
                {
                  required: true,
                  message: "Silahkan isi Nama Tujuan Pembelajaran",
                },
              ]}
            >
              <TextArea rows={2} placeholder="Masukkan Nama ATP" />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default EditATPForm;
