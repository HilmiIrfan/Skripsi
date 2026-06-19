/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Form, Input, Modal, Select, Row, Col, message, InputNumber, Tooltip } from "antd";
import { InfoCircleOutlined } from "@ant-design/icons";
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
import { getSoalUjian } from "@/api/soalUjian";
import { useFormFilterEditAtp } from "@/helper/atp/formFilterAtpHelperEdit";

const { Option } = Select;

const EditBankSoalForm = ({
  visible,
  onCancel,
  onOk,
  confirmLoading,
  currentRowData,
}) => {
  const [form] = Form.useForm();

  const [userSchoolId, setUserSchoolId] = useState([]);
  const [schoolList, setSchoolList] = useState([]);
  const [konsentrasiKeahlianSekolahList, setKonsentrasiKeahlianSekolahList] =
    useState([]);
  const [initialData, setInitialData] = useState(null);
  const [creatorInfo, setCreatorInfo] = useState(null);

  const {
    renderTahunAjaranSelect,
    renderSemesterSelect,
    renderKelasSelect,
    renderMapelSelect,
    renderElemenSelect,
    renderAcpSelect,
    renderAtpSelect,
  } = useFormFilterEditAtp(currentRowData, initialData);

  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo();
      setUserSchoolId(response.data.school_id);
    } catch (error) {
      message.error("Gagal mengambil informasi pengguna");
    }
  };

  const fetchSchoolList = async () => {
    try {
      const result = await getSchool();
      if (result.data.statusCode === 200) {
        setSchoolList(result.data.content);
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const fetchKonsentrasiKeahlianSekolahList = async () => {
    try {
      const result = await getKonsentrasiSekolah();
      if (result.data.statusCode === 200) {
        setKonsentrasiKeahlianSekolahList(result.data.content);
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        await fetchUserInfo();
        await fetchSchoolList();
        await fetchKonsentrasiKeahlianSekolahList();

        const [
          tahunAjaranRes,
          semesterRes,
          kelasRes,
          mapelRes,
          elemenRes,
          acpRes,
          atpRes,
        ] = await Promise.all([
          getTahunAjaran(),
          getSemester(),
          getKelas(),
          getMapel(),
          getElemen(),
          getACP(),
          getATP(),
        ]);

        setInitialData({
          tahunAjaranList: tahunAjaranRes.data.content || [],
          semesterList: semesterRes.data.content || [],
          kelasList: kelasRes.data.content || [],
          mapelList: mapelRes.data.content || [],
          elemenList: elemenRes.data.content || [],
          acpList: acpRes.data.content || [],
          atpList: atpRes.data.content || [],
        });
      } catch (error) {
        message.error("Gagal memuat data");
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    const fetchCreatorInfo = async () => {
      if (currentRowData?.idSoalUjian) {
        try {
          const result = await getSoalUjian();
          if (result.data.statusCode === 200) {
            const soalUjian = result.data.content.find(
              (item) => item.idSoalUjian === currentRowData.idSoalUjian
            );
            if (soalUjian && soalUjian.createdBy) {
              setCreatorInfo(soalUjian.createdBy);
            }
          }
        } catch (error) {
          console.error("Gagal mengambil info pembuat:", error);
        }
      }
    };

    fetchCreatorInfo();
  }, [currentRowData?.idSoalUjian]);

  useEffect(() => {
    if (currentRowData) {
      form.setFieldsValue({
        idBankSoal: currentRowData.idBankSoal,
        idKelas: currentRowData.kelas?.idKelas,
        idTahun: currentRowData.tahunAjaran?.idTahun,
        idSemester: currentRowData.semester?.idSemester,
        idMapel: currentRowData.mapel?.idMapel,
        idKonsentrasiSekolah:
          currentRowData.konsentrasiKeahlianSekolah?.idKonsentrasiSekolah,
        idElemen: currentRowData.elemen?.idElemen,
        idAcp: currentRowData.acp?.idAcp,
        idAtp: currentRowData.atp?.idAtp,
        idSchool: currentRowData.school?.idSchool,
        // IRT parameters - pre-fill from existing data
        irtDiscrimination: currentRowData.irtDiscrimination || null,
        irtDifficulty: currentRowData.irtDifficulty || null,
        irtGuessing: currentRowData.irtGuessing || null,
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

      const payload = {
        idBankSoal: values.idBankSoal,
        idSoalUjian: currentRowData?.idSoalUjian,
        namaUjian: currentRowData?.namaUjian,
        pertanyaan: currentRowData?.pertanyaan,
        bobot: currentRowData?.bobot?.toString(),
        jenisSoal: currentRowData?.jenisSoal,
        idKelas: values.idKelas,
        idTahun: values.idTahun,
        idSemester: values.idSemester,
        idMapel: values.idMapel,
        idKonsentrasiSekolah: values.idKonsentrasiSekolah,
        idElemen: values.idElemen,
        idAcp: values.idAcp,
        idAtp: values.idAtp,
        idTaksonomi: currentRowData?.taksonomi?.idTaksonomi,
        idSchool: values.idSchool,
        // IRT 3PL parameters
        irtDiscrimination: values.irtDiscrimination || null,
        irtDifficulty: values.irtDifficulty || null,
        irtGuessing: values.irtGuessing || null,
      };

      const jenisSoal = currentRowData?.jenisSoal;
      switch (jenisSoal) {
        case "PG":
          payload.opsi = currentRowData?.opsi || {};
          payload.jawabanBenar = currentRowData?.jawabanBenar || [];
          break;
        case "MULTI":
          payload.opsi = currentRowData?.opsi || {};
          payload.jawabanBenar = currentRowData?.jawabanBenar || [];
          break;
        case "COCOK":
          payload.pasangan = currentRowData?.pasangan || {};
          payload.jawabanBenar = currentRowData?.jawabanBenar || {};
          break;
        case "ISIAN":
          payload.jawabanBenar = currentRowData?.jawabanBenar || [""];
          payload.toleransiTypo = currentRowData?.toleransiTypo || null;
          break;
      }

      onOk(payload);
    } catch (error) {
      console.error("Validation failed:", error);
    }
  };

  return (
    <Modal
      title="Edit Informasi ATP Bank Soal"
      open={visible}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
      okText="Simpan"
      width={1000}
    >
      <Form form={form} layout="vertical">
        <Row gutter={16}>
          <Col xs={24} sm={24} md={24}>
            <Form.Item name="idBankSoal" style={{ display: "none" }}>
              <Input type="hidden" />
            </Form.Item>

            <Form.Item
              label="Sekolah:"
              name="idSchool"
              style={{ display: "none" }}
              rules={[{ required: true, message: "Silahkan pilih Sekolah" }]}
            >
              <Select defaultValue={userSchoolId} disabled>
                {schoolList
                  .filter(({ idSchool }) => idSchool === userSchoolId)
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
            {renderAtpSelect(form)}
          </Col>
          {creatorInfo && (
            <Col xs={24} sm={24} md={24}>
              <Form.Item label="Dibuat oleh:">
                <Input value={creatorInfo} disabled style={{ color: "#666" }} />
              </Form.Item>
            </Col>
          )}

          {/* IRT 3PL Parameters */}
          <Col xs={24} sm={24} md={24}>
            <div style={{
              marginTop: "16px",
              padding: "16px",
              background: "#e6f7ff",
              borderRadius: "6px",
              border: "1px solid #91d5ff",
              marginBottom: "8px"
            }}>
              <p style={{ margin: "0 0 12px 0", fontWeight: 600, color: "#0050b3" }}>
                <InfoCircleOutlined style={{ marginRight: 6 }} />
                Parameter IRT 3PL (Opsional)
              </p>
              <Row gutter={16}>
                <Col xs={24} sm={24} md={8}>
                  <Form.Item
                    label={<span>Parameter <strong>a</strong> — Daya Pembeda <Tooltip title="0.5–2.5. Semakin tinggi = lebih diskriminatif."><InfoCircleOutlined style={{ color: "#1890ff" }} /></Tooltip></span>}
                    name="irtDiscrimination"
                  >
                    <InputNumber min={0} max={3} step={0.1} precision={3} placeholder="mis. 1.2" style={{ width: "100%" }} />
                  </Form.Item>
                </Col>
                <Col xs={24} sm={24} md={8}>
                  <Form.Item
                    label={<span>Parameter <strong>b</strong> — Tingkat Kesulitan <Tooltip title="-3 hingga 3. Negatif=mudah, positif=sulit."><InfoCircleOutlined style={{ color: "#1890ff" }} /></Tooltip></span>}
                    name="irtDifficulty"
                  >
                    <InputNumber min={-4} max={4} step={0.1} precision={3} placeholder="mis. 0.0" style={{ width: "100%" }} />
                  </Form.Item>
                </Col>
                <Col xs={24} sm={24} md={8}>
                  <Form.Item
                    label={<span>Parameter <strong>c</strong> — Peluang Menebak <Tooltip title="0–0.35. PG 4 opsi ≈ 0.25."><InfoCircleOutlined style={{ color: "#1890ff" }} /></Tooltip></span>}
                    name="irtGuessing"
                  >
                    <InputNumber min={0} max={0.5} step={0.01} precision={3} placeholder="mis. 0.25" style={{ width: "100%" }} />
                  </Form.Item>
                </Col>
              </Row>
            </div>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default EditBankSoalForm;
