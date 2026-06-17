/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
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
import { getBidangKeahlian } from "@/api/bidangKeahlian";
import { getProgramByBidang } from "@/api/programKeahlian";
import { getKonsentrasiByProgram } from "@/api/konsentrasiKeahlian";

const { TextArea } = Input;
const { Option } = Select;
const { Column } = Table;
const { TabPane } = Tabs;

registerAllModules();

const AddSeasonForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();

  const [tahunList, setTahunList] = useState([]);
  const [bidangList, setBidangList] = useState([]);
  const [filteredProgramList, setFilteredProgramList] = useState([]);
  const [filteredKonsentrasiList, setFilteredKonsentrasiList] = useState([]);
  const [kelasList, setKelasList] = useState([]);
  const [siswaList, setSiswaList] = useState([]);
  const [jadwalPelajaranList, setJadwalPelajaranList] = useState([]);
  const [guruList, setGuruList] = useState([]);
  const [activeTab, setActiveTab] = useState("siswa");
  const [siswaData, setSiswaData] = useState([
    { nisn: "", nama: "", alamat: "", jurusan: "" },
  ]);
  const [jadwalPelajaranData, setJadwalPelajaranData] = useState([
    { guru: "", jabatan: "", mapel: "", jmlJam: "" },
  ]);

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
      title="Tambah Kelas Ajaran"
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
      width={900}
    >
      <Form {...formItemLayout} form={form} name="AddSeasonForm">
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
          <Select placeholder="Pilih Kelas">
            {kelasList.map((kelas) => (
              <Option key={kelas.idKelas} value={kelas.idKelas}>
                {kelas.namaKelas}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="Semester:"
          name="semester"
          rules={[{ required: true, message: "Semester wajib diisi" }]}
        >
          <Select style={{ width: 120 }} placeholder="Semester">
            <Option value="Ganjil">Ganjil</Option>
            <Option value="Genap">Genap</Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Wali Kelas:"
          name="lecture_id"
          rules={[{ required: true, message: "Silahkan isi kelas" }]}
        >
          <Select placeholder="Pilih Kelas">
            {guruList.map((guru) => (
              <Option key={guru.id} value={guru.id}>
                {guru.name}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Tabs defaultActiveKey="siswa" onChange={handleTabChange}>
          <TabPane tab="Siswa" key="siswa">
            <Form.Item
              name="student_id"
              initialValue={siswaData
                .slice(0, -1)
                .filter((siswa) => siswa.id !== null)
                .map((siswa) => siswa.id)}
            >
              <HotTable
                data={siswaData}
                colHeaders={["NISN", "Nama", "Alamat", "Jurusan"]}
                columns={[
                  {
                    data: "nisn",
                    type: "dropdown",
                    source: siswaList.map((siswa) => siswa.nisn),
                    allowInvalid: false,
                  },
                  { data: "nama", readOnly: true },
                  { data: "alamat", readOnly: true },
                  { data: "konsentrasi", readOnly: true },
                ]}
                afterChange={(changes) => {
                  if (!changes) return;

                  changes.forEach(([row, prop, oldValue, newValue]) => {
                    if (prop === "nisn" && oldValue !== newValue) {
                      const selectedSiswa = siswaList.find(
                        (siswa) => siswa.nisn === newValue
                      );
                      if (selectedSiswa) {
                        const updatedTableData = [...siswaData];
                        updatedTableData[row] = {
                          ...updatedTableData[row],
                          id: selectedSiswa.id,
                          nisn: selectedSiswa.nisn,
                          nama: selectedSiswa.name,
                          alamat: selectedSiswa.address,
                          konsentrasi: selectedSiswa.konsentrasi,
                        };
                        if (row === updatedTableData.length - 1) {
                          updatedTableData.push({
                            nisn: "",
                            nama: "",
                            alamat: "",
                            konsentrasi: "",
                          });
                        }
                        setSiswaData(updatedTableData);
                        form.setFieldsValue({
                          siswaData: updatedTableData,
                        });
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

          <TabPane tab="Jadwal Pelajaran" key="jadwalPelajaran">
            <Form.Item
              name="jadwalPelajaran_id"
              initialValue={jadwalPelajaranData
                .slice(0, -1)
                .filter((jadwal) => jadwal.idJadwal !== null)
                .map((jadwal) => jadwal.idJadwal)}
            >
              <HotTable
                data={jadwalPelajaranData}
                colHeaders={[
                  "Guru Pengajar",
                  "Jabatan",
                  "Mata Pelajaran",
                  "Jumlah Jam",
                ]}
                columns={[
                  {
                    data: "guru",
                    type: "dropdown",
                    source: jadwalPelajaranList.map((jadwal) => jadwal.guru),
                    allowInvalid: false,
                  },
                  { data: "jabatan", readOnly: true },
                  { data: "mapel", readOnly: true },
                  { data: "jmlJam", readOnly: true },
                ]}
                afterChange={(changes) => {
                  if (!changes) return;

                  changes.forEach(([row, prop, oldValue, newValue]) => {
                    if (prop === "guru" && oldValue !== newValue) {
                      const selectedJadwal = jadwalPelajaranList.find(
                        (jadwal) => jadwal.guru === newValue
                      );
                      if (selectedJadwal) {
                        const updatedTableData = [...jadwalPelajaranData];
                        updatedTableData[row] = {
                          ...updatedTableData[row],
                          idJadwal: selectedJadwal.idJadwal,
                          guru: selectedJadwal.guru,
                          jabatan: selectedJadwal.jabatan,
                          mapel: selectedJadwal.mapel,
                          jmlJam: selectedJadwal.jmlJam,
                        };
                        if (row === updatedTableData.length - 1) {
                          updatedTableData.push({
                            guru: "",
                            jabatan: "",
                            mapel: "",
                            jmlJam: "",
                          });
                        }
                        setJadwalPelajaranData(updatedTableData);
                        form.setFieldsValue({
                          jadwalPelajaranData: updatedTableData,
                        });
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
