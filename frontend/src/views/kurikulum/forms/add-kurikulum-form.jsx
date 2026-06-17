/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Form, Input, Modal, Select, Table, Tabs } from "antd";
import ReactSelect from "react-select";
import { HotTable } from "@handsontable/react";
import { registerAllModules } from "handsontable/registry";
import "handsontable/dist/handsontable.full.min.css";
import { getTahunAjaran } from "@/api/tahun-ajaran";
import { getKelas } from "@/api/kelas";
import { getStudents } from "@/api/student";
import { getJadwalPelajaran } from "@/api/jadwalPelajaran";
import { getLectures } from "@/api/lecture";
import { getMapel } from "@/api/mapel";
import { getBidangKeahlian } from "@/api/bidangKeahlian";
import { getProgramByBidang } from "@/api/programKeahlian";
import { getKonsentrasiByProgram } from "@/api/konsentrasiKeahlian";

const { Option } = Select;
const { TabPane } = Tabs;
registerAllModules();

const AddSeasonForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();

  const [mapelList, setMapelList] = useState([]);
  const [tahunList, setTahunList] = useState([]);
  const [bidangList, setBidangList] = useState([]);
  const [filteredProgramList, setFilteredProgramList] = useState([]);
  const [filteredKonsentrasiList, setFilteredKonsentrasiList] = useState([]);
  const [kelasList, setKelasList] = useState([]);
  const [siswaList, setSiswaList] = useState([]);
  const [jadwalPelajaranList, setJadwalPelajaranList] = useState([]);
  const [guruList, setGuruList] = useState([]);
  const [mapelData, setMapelData] = useState([{ mapel: "" }]);
  const [activeTab, setActiveTab] = useState("siswa");
  const [selectedStudents, setSelectedStudents] = useState([]);
  const [selectedJadwalPelajarans, setSelectedJadwalPelajarans] = useState([]);
  const [jadwalPelajaranData, setJadwalPelajaranData] = useState([
    {
      guru: "",
      jabatan: "",
      mapel: "",
      jmlJam: "",
    },
  ]);

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

  const handleTabChange = (activeKey) => {
    setActiveTab(activeKey);
  };

  const fetchTahunAjaranList = async () => {
    try {
      const result = await getTahunAjaran();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const tahunList = content.map((tahun) => ({
          idTahun: tahun.idTahun,
          tahunAjaran: tahun.tahunAjaran,
        }));
        setTahunList(tahunList);
      }
    } catch (error) {
      console.error("Error fetching tahun data: ", error);
    }
  };

  const fetchBidangKeahlianList = async () => {
    try {
      const result = await getBidangKeahlian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const bidangList = content.map((bidang) => ({
          id: bidang.id,
          bidang: bidang.bidang,
        }));
        setBidangList(bidangList);
      }
    } catch (error) {
      console.error("Error fetching bidang data: ", error);
    }
  };

  const handleBidangChange = async (value) => {
    try {
      const result = await getProgramByBidang(value);
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setFilteredProgramList(content);
        setFilteredKonsentrasiList([]);
      } else {
        setFilteredProgramList([]);
        setFilteredKonsentrasiList([]);
      }

      form.setFieldsValue({
        programkeahlian_id: undefined,
        konsentrasikeahlian_id: undefined,
      });
    } catch (error) {
      console.error("Error fetching program data: ", error);
    }
  };

  const handleProgramChange = async (value) => {
    try {
      const result = await getKonsentrasiByProgram(value);
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setFilteredKonsentrasiList(content);
      } else {
        setFilteredKonsentrasiList([]);
      }

      form.setFieldsValue({
        konsentrasikeahlian_id: undefined,
      });
    } catch (error) {
      console.error("Error fetching konsentrasi data: ", error);
    }
  };

  const fetchKelasList = async () => {
    try {
      const result = await getKelas();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const kelasList = content.map((kelas) => ({
          idKelas: kelas.idKelas,
          namaKelas: kelas.namaKelas,
        }));
        setKelasList(kelasList);
      }
    } catch (error) {
      console.error("Error fetching kelas data: ", error);
    }
  };

  const fetchSiswaList = async () => {
    try {
      const result = await getStudents();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const siswaList = content.map((student) => ({
          id: student.id,
          name: student.name,
          nisn: student.nisn,
          address: student.address,
          konsentrasi: student.konsentrasiKeahlian.konsentrasi,
        }));
        setSiswaList(siswaList);
      }
    } catch (error) {
      console.error("Error fetching siswa data: ", error);
    }
  };

  const fetchJadwalPelajaranList = async () => {
    try {
      const result = await getJadwalPelajaran();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const jadwalPelajaranList = content.map((jadwalPelajaran) => ({
          idJadwal: jadwalPelajaran.idJadwal,
          guru: jadwalPelajaran.lecture.name,
          jabatan: jadwalPelajaran.jabatan,
          mapel: jadwalPelajaran.mapel.name,
          jmlJam: jadwalPelajaran.jmlJam,
        }));
        setJadwalPelajaranList(jadwalPelajaranList);
      }
    } catch (error) {
      console.error("Error fetching jadwalPelajaran data: ", error);
    }
  };

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

  useEffect(() => {
    fetchTahunAjaranList();
    fetchBidangKeahlianList();
    fetchKelasList();
    fetchSiswaList();
    fetchJadwalPelajaranList();
    fetchGuruList();
    fetchMapelList();
  }, []);

  const formItemLayout = {
    labelCol: {
      xs: { span: 24 },
      sm: { span: 6 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 18 },
    },
  };

  return (
    <Modal
      title="Tambah Kurikulum"
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
      width={900}
    >
      <Form form={form} {...formItemLayout}>
        <Form.Item
          label="Tahun Ajaran:"
          name="tahunAjaran_id"
          rules={[{ required: true, message: "Silahkan isi tahun ajaran" }]}
        >
          <Select placeholder="Pilih Tahun Ajaran">
            {tahunList.map((tahun) => (
              <Option key={tahun.idTahun} value={tahun.idTahun}>
                {tahun.tahunAjaran}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Bidang Keahlian:"
          name="bidangKeahlian_id"
          rules={[
            { required: true, message: "Silahkan isi konsentrasi keahlian" },
          ]}
        >
          <Select
            placeholder="Pilih Bidang Keahlian"
            onChange={handleBidangChange}
          >
            {bidangList.map((bidang) => (
              <Option key={bidang.id} value={bidang.id}>
                {bidang.bidang}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Program Keahlian:"
          name="programKeahlian_id"
          rules={[
            { required: true, message: "Silahkan isi konsentrasi keahlian" },
          ]}
        >
          <Select
            placeholder="Pilih Program Keahlian"
            onChange={handleProgramChange}
          >
            {filteredProgramList.map((program) => (
              <Option key={program.id} value={program.id}>
                {program.program}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Konsentrasi Keahlian:"
          name="konsentrasiKeahlian_id"
          rules={[
            { required: true, message: "Silahkan isi konsentrasi keahlian" },
          ]}
        >
          <Select placeholder="Pilih Konsentrasi Keahlian">
            {filteredKonsentrasiList.map((konsentrasi) => (
              <Option key={konsentrasi.id} value={konsentrasi.id}>
                {konsentrasi.konsentrasi}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Kelas:"
          name="kelas_id"
          rules={[{ required: true, message: "Silahkan isi kelas" }]}
        >
          <Select style={{ width: 120 }} placeholder="Kelas">
            <Option value="10">10</Option>
            <Option value="11">11</Option>
            <Option value="12">12</Option>
          </Select>
        </Form.Item>

        <Tabs defaultActiveKey="siswa" onChange={handleTabChange}>
          <TabPane tab="Mata Pelajaran" key="siswa">
            <Form.Item
              name="mapel_id"
              initialValue={mapelData
                .slice(0, -1)
                .filter((mapel) => mapel.id !== null)
                .map((mapel) => mapel.id)}
            >
              <HotTable
                data={mapelData}
                colHeaders={["Mata Pelajaran"]}
                columns={[
                  {
                    data: "mapel",
                    type: "dropdown",
                    source: mapelList.map((mapel) => mapel.name),
                    allowInvalid: false,
                  },
                ]}
                afterChange={(changes) => {
                  if (!changes) return;

                  changes.forEach(([row, prop, oldValue, newValue]) => {
                    if (prop === "mapel" && oldValue !== newValue) {
                      const selectedMapel = mapelList.find(
                        (mapel) => mapel.name === newValue
                      );
                      if (selectedMapel) {
                        const updatedTableData = [...mapelData];
                        updatedTableData[row] = {
                          ...updatedTableData[row],
                          id: selectedMapel.id,
                          mapel: selectedMapel.name,
                        };
                        if (row === updatedTableData.length - 1) {
                          updatedTableData.push({
                            mapel: "",
                          });
                        }
                        form.setFieldsValue({
                          mapelData: updatedTableData,
                        });
                        setMapelData(updatedTableData);
                      }
                    }
                  });
                }}
                stretchH="all"
                rowHeaders={true}
                manualColumnResize={true}
                height="300"
                licenseKey="non-commercial-and-evaluation"
              />
            </Form.Item>
          </TabPane>
        </Tabs>
      </Form>
    </Modal>
  );
};

export default AddSeasonForm;
