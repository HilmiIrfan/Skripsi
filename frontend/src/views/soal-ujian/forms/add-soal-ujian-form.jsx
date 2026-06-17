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
  InputNumber,
  Space,
  Radio,
  Checkbox,
  Divider,
} from "antd";
import { PlusOutlined, MinusCircleOutlined } from "@ant-design/icons";
import { getSoalUjian } from "@/api/soalUjian";
import { getSchool } from "@/api/school";
import { reqUserInfo } from "@/api/user";
import { getKonsentrasiSekolah } from "@/api/konsentrasiKeahlianSekolah";
import { getTaksonomi } from "@/api/taksonomi";

const { TextArea } = Input;
const { Option } = Select;
const { TabPane } = Tabs;

// Define available colors for matching
const MATCH_COLORS = [
  { name: "blue", bg: "#BAD7F2", key: "blue" },
  { name: "green", bg: "#C2E0C9", key: "green" },
  { name: "orange", bg: "#F2C4B3", key: "orange" },
  { name: "purple", bg: "#D4C1EC", key: "purple" },
  { name: "yellow", bg: "#F2E2B3", key: "yellow" },
  { name: "pink", bg: "#F2B3D6", key: "pink" },
  { name: "teal", bg: "#B3F2E2", key: "teal" },
  { name: "brown", bg: "#D9C4B1", key: "brown" },
];

const AddSoalUjianForm = ({ visible, onCancel, onOk, confirmLoading }) => {
  const [form] = Form.useForm();
  const [soalUjian, setSoalUjian] = useState([]);
  const [jenisSoal, setJenisSoal] = useState("PG");
  const [tableLoading, setTableLoading] = useState(false);
  const [userSchoolId, setUserSchoolId] = useState(null);
  const [userId, setUserId] = useState(null);
  const [schoolList, setSchoolList] = useState([]);
  const [taksonomiList, setTaksonomiList] = useState([]);
  const [konsentrasiKeahlianSekolahList, setKonsentrasiKeahlianSekolahList] =
    useState([]);
  const [options, setOptions] = useState(["A", "B"]); // Default to 2 options: A and B

  // New state for managing color matching
  const [kiriItems, setKiriItems] = useState(["", "", ""]);
  const [kananItems, setKananItems] = useState(["", "", ""]);
  const [kiriColors, setKiriColors] = useState(["", "", ""]);
  const [kananColors, setKananColors] = useState(["", "", ""]);

  const addOption = () => {
    if (options.length < 5) {
      const nextOption = String.fromCharCode(65 + options.length); // 65 is ASCII for 'A'
      setOptions([...options, nextOption]);
    }
  };

  const removeOption = () => {
    if (options.length > 2) {
      // Check if removed option is selected as correct answer
      const currentAnswer = form.getFieldValue("jawabanBenar");
      const optionToRemove = options[options.length - 1];

      // If removing the selected answer, reset the answer
      if (currentAnswer === optionToRemove) {
        form.setFieldsValue({ jawabanBenar: undefined });
      }

      // Remove the option
      setOptions(options.slice(0, -1));
    }
  };

  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo(); // Ambil data user dari API
      setUserSchoolId(response.data.school_id); // Simpan ID sekolah user ke state
      setUserId(response.data.id); // Simpan ID user ke state
      console.log("User School ID: ", response.data.school_id);
      console.log("User ID: ", response.data.id);
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
      message.error("Gagal mengambil data sekolah");
    }
  };

  const fetchTaksonomiList = async () => {
    try {
      const result = await getTaksonomi();
      if (result.data.statusCode === 200) {
        setTaksonomiList(result.data.content);
      }
    } catch (error) {
      message.error("Gagal mengambil data taksonomi");
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

  const fetchSoalUjian = async () => {
    setTableLoading(true);
    try {
      const result = await getSoalUjian();
      if (result.data.statusCode === 200) {
        setSoalUjian(result.data.content);
      }
    } catch (error) {
      message.error("Gagal mengambil data soal ujian");
    } finally {
      setTableLoading(false);
    }
  };

  useEffect(() => {
    fetchUserInfo();
    fetchSchoolList();
    fetchTaksonomiList();
    fetchKonsentrasiKeahlianSekolahList();
    fetchSoalUjian();
  }, []);

  useEffect(() => {
    if (userSchoolId) {
      form.setFieldsValue({
        idSchool: userSchoolId,
        jenisSoal: "PG",
      });
    }
  }, [userSchoolId, form]);

  // Handler for changing question type
  const handleJenisSoalChange = (value) => {
    setJenisSoal(value);

    // Reset options to default when changing to PG or MULTI
    if (value === "PG" || value === "MULTI") {
      setOptions(["A", "B"]);
    }

    // Reset color matching states when switching to COCOK
    if (value === "COCOK") {
      setKiriItems(["", "", ""]);
      setKananItems(["", "", ""]);
      setKiriColors(["", "", ""]);
      setKananColors(["", "", ""]);
    }

    // Reset answer fields when changing question type
    form.setFieldsValue({
      opsiA: undefined,
      opsiB: undefined,
      opsiC: undefined,
      opsiD: undefined,
      opsiE: undefined,
      jawabanBenar: undefined,
      jawabanBenarMulti: undefined,
      pasanganKiri: undefined,
      pasanganKanan: undefined,
      pasanganJawaban: undefined,
      jawabanIsian: undefined,
      toleransiTypo: undefined,
    });
  };

  // Add item to kiri side
  const addKiriItem = () => {
    setKiriItems([...kiriItems, ""]);
    setKiriColors([...kiriColors, ""]);
  };

  // Remove item from kiri side
  const removeKiriItem = (index) => {
    if (kiriItems.length > 2) {
      const newItems = [...kiriItems];
      const newColors = [...kiriColors];
      newItems.splice(index, 1);
      newColors.splice(index, 1);
      setKiriItems(newItems);
      setKiriColors(newColors);
    }
  };

  // Add item to kanan side
  const addKananItem = () => {
    setKananItems([...kananItems, ""]);
    setKananColors([...kananColors, ""]);
  };

  // Remove item from kanan side
  const removeKananItem = (index) => {
    if (kananItems.length > 2) {
      const newItems = [...kananItems];
      const newColors = [...kananColors];
      newItems.splice(index, 1);
      newColors.splice(index, 1);
      setKananItems(newItems);
      setKananColors(newColors);
    }
  };

  // Update kiri item text
  const updateKiriItem = (index, value) => {
    const newItems = [...kiriItems];
    newItems[index] = value;
    setKiriItems(newItems);
  };

  // Update kanan item text
  const updateKananItem = (index, value) => {
    const newItems = [...kananItems];
    newItems[index] = value;
    setKananItems(newItems);
  };

  // Update kiri item color
  const updateKiriColor = (index, color) => {
    const newColors = [...kiriColors];
    newColors[index] = color;
    setKiriColors(newColors);
  };

  // Update kanan item color
  const updateKananColor = (index, color) => {
    const newColors = [...kananColors];
    newColors[index] = color;
    setKananColors(newColors);
  };

  // Generate color pairs data from selections
  const generateColorPairs = () => {
    const positionPairs = [];
    const contentPairs = [];

    // Create a map to store items by color
    const colorMap = {};

    // Add kiri items to the map
    kiriItems.forEach((item, index) => {
      const color = kiriColors[index];
      if (color && item) {
        if (!colorMap[color]) colorMap[color] = { kiri: [], kanan: [] };
        colorMap[color].kiri.push({ index, text: item });
      }
    });

    // Add kanan items to the map
    kananItems.forEach((item, index) => {
      const color = kananColors[index];
      if (color && item) {
        if (!colorMap[color]) colorMap[color] = { kiri: [], kanan: [] };
        colorMap[color].kanan.push({ index, text: item });
      }
    });

    // Generate pairs from the map
    Object.values(colorMap).forEach(({ kiri, kanan }) => {
      kiri.forEach((kiriItem) => {
        kanan.forEach((kananItem) => {
          positionPairs.push(`${kiriItem.index}-${kananItem.index}`);
          contentPairs.push(`${kiriItem.text}=${kananItem.text}`);
        });
      });
    });

    return { positionPairs, contentPairs };
  };

  // Submit handler
  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      // Basic payload structure
      const payload = {
        idSoalUjian: values.idSoalUjian || null,
        namaUjian: values.namaUjian,
        pertanyaan: values.pertanyaan,
        bobot: values.bobot?.toString(),
        jenisSoal: values.jenisSoal,
        idUser: userId,
        idTaksonomi: values.idTaksonomi,
        idKonsentrasiSekolah: values.idKonsentrasiSekolah,
        idSchool: values.idSchool,
      };

      // Build payload based on question type
      switch (values.jenisSoal) {
        case "PG": {
          const opsi = {};
          options.forEach((option) => {
            opsi[option] = values[`opsi${option}`];
          });
          payload.opsi = opsi;
          payload.jawabanBenar = [values.jawabanBenar];
          break;
        }

        case "MULTI": {
          const opsi = {};
          options.forEach((option) => {
            opsi[option] = values[`opsi${option}`];
          });
          payload.opsi = opsi;
          payload.jawabanBenar = values.jawabanBenarMulti;
          break;
        }

        case "COCOK": {
          // Process left side items
          const kiri = {};
          kiriItems.forEach((item, index) => {
            if (item.trim() !== "") {
              kiri[`${index + 1}_kiri`] = item;
            }
          });

          // Process right side items
          const kanan = {};
          kananItems.forEach((item, index) => {
            if (item.trim() !== "") {
              kanan[`${index + kiriItems.length + 1}_kanan`] = item;
            }
          });

          payload.pasangan = { ...kiri, ...kanan };

          // Generate pairs based on matching colors
          const { contentPairs } = generateColorPairs();

          // Using content pairs (actual text values) instead of position references
          payload.jawabanBenar = contentPairs;
          break;
        }

        case "ISIAN":
          payload.jawabanBenar = [values.jawabanIsian];
          payload.toleransiTypo = values.toleransiTypo?.toString() || "0";
          break;
      }

      console.log("Payload:", payload);
      onOk(payload);
    } catch (error) {
      console.error("Validation failed:", error);
    }
  };

  // Render form based on question type
  const renderQuestionForm = () => {
    switch (jenisSoal) {
      case "PG":
        return (
          <>
            <Row gutter={16}>
              {options.map((option) => (
                <Col span={24} key={option}>
                  <Form.Item
                    label={`Pilihan ${option}`}
                    name={`opsi${option}`}
                    rules={[
                      {
                        required: true,
                        message: `Pilihan ${option} wajib diisi`,
                      },
                    ]}
                  >
                    <Input placeholder={`Pilihan ${option}`} />
                  </Form.Item>
                </Col>
              ))}
            </Row>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                marginBottom: 16,
              }}
            >
              <Button
                type="primary"
                icon={<PlusOutlined />}
                onClick={addOption}
                disabled={options.length >= 5}
              >
                Tambah Pilihan
              </Button>
              <Button
                danger
                icon={<MinusCircleOutlined />}
                onClick={removeOption}
                disabled={options.length <= 2}
              >
                Hapus Pilihan
              </Button>
            </div>
            <Divider orientation="left">Jawaban</Divider>
            <Form.Item
              name="jawabanBenar"
              label="Jawaban Benar"
              rules={[{ required: true, message: "Pilih jawaban yang benar" }]}
            >
              <Radio.Group>
                {options.map((option) => (
                  <Radio key={option} value={option}>
                    {option}
                  </Radio>
                ))}
              </Radio.Group>
            </Form.Item>
          </>
        );

      case "MULTI":
        return (
          <>
            <Row gutter={16}>
              {options.map((option) => (
                <Col span={12} key={option}>
                  <Form.Item
                    label={`Pilihan ${option}`}
                    name={`opsi${option}`}
                    rules={[
                      {
                        required: true,
                        message: `Pilihan ${option} wajib diisi`,
                      },
                    ]}
                  >
                    <Input placeholder={`Pilihan ${option}`} />
                  </Form.Item>
                </Col>
              ))}
            </Row>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                marginBottom: 16,
              }}
            >
              <Button
                type="primary"
                icon={<PlusOutlined />}
                onClick={addOption}
                disabled={options.length >= 5}
              >
                Tambah Pilihan
              </Button>
              <Button
                danger
                icon={<MinusCircleOutlined />}
                onClick={removeOption}
                disabled={options.length <= 2}
              >
                Hapus Pilihan
              </Button>
            </div>
            <Divider orientation="left">Jawaban</Divider>
            <Form.Item
              name="jawabanBenarMulti"
              label="Jawaban Benar (Pilih satu atau lebih)"
              rules={[
                {
                  required: true,
                  message: "Pilih minimal satu jawaban yang benar",
                },
              ]}
            >
              <Checkbox.Group>
                <Space direction="vertical">
                  {options.map((option) => (
                    <Checkbox key={option} value={option}>
                      {option}
                    </Checkbox>
                  ))}
                </Space>
              </Checkbox.Group>
            </Form.Item>
          </>
        );

      case "COCOK":
        return (
          <>
            <Row gutter={24}>
              {/* Left column */}
              <Col span={12}>
                <Divider orientation="left">Sisi Kiri</Divider>

                {kiriItems.map((item, index) => (
                  <div
                    key={`kiri-${index}`}
                    style={{
                      marginBottom: 16,
                      display: "flex",
                      alignItems: "center",
                    }}
                  >
                    <Select
                      value={kiriColors[index] || undefined}
                      onChange={(color) => updateKiriColor(index, color)}
                      placeholder="Pilih warna"
                      style={{ width: 120, marginRight: 8 }}
                      dropdownRender={(menu) => <div>{menu}</div>}
                    >
                      {MATCH_COLORS.map((color) => (
                        <Option key={color.key} value={color.key}>
                          <div
                            style={{
                              backgroundColor: color.bg,
                              width: 80,
                              height: 20,
                              borderRadius: 4,
                            }}
                          ></div>
                        </Option>
                      ))}
                    </Select>

                    <Input
                      value={item}
                      onChange={(e) => updateKiriItem(index, e.target.value)}
                      placeholder={`Item ${index + 1}`}
                      style={{
                        flex: 1,
                        backgroundColor: kiriColors[index]
                          ? MATCH_COLORS.find(
                              (c) => c.key === kiriColors[index]
                            )?.bg
                          : "white",
                      }}
                    />

                    {kiriItems.length > 2 && (
                      <Button
                        type="text"
                        icon={<MinusCircleOutlined />}
                        onClick={() => removeKiriItem(index)}
                        style={{ marginLeft: 8 }}
                      />
                    )}
                  </div>
                ))}

                <Button
                  type="dashed"
                  onClick={addKiriItem}
                  icon={<PlusOutlined />}
                  style={{ width: "100%" }}
                >
                  Tambah Item Kiri
                </Button>
              </Col>

              {/* Right column */}
              <Col span={12}>
                <Divider orientation="left">Sisi Kanan</Divider>

                {kananItems.map((item, index) => (
                  <div
                    key={`kanan-${index}`}
                    style={{
                      marginBottom: 16,
                      display: "flex",
                      alignItems: "center",
                    }}
                  >
                    <Select
                      value={kananColors[index] || undefined}
                      onChange={(color) => updateKananColor(index, color)}
                      placeholder="Pilih warna"
                      style={{ width: 120, marginRight: 8 }}
                      dropdownRender={(menu) => <div>{menu}</div>}
                    >
                      {MATCH_COLORS.map((color) => (
                        <Option key={color.key} value={color.key}>
                          <div
                            style={{
                              backgroundColor: color.bg,
                              width: 80,
                              height: 20,
                              borderRadius: 4,
                            }}
                          ></div>
                        </Option>
                      ))}
                    </Select>

                    <Input
                      value={item}
                      onChange={(e) => updateKananItem(index, e.target.value)}
                      placeholder={`Item ${index + 1}`}
                      style={{
                        flex: 1,
                        backgroundColor: kananColors[index]
                          ? MATCH_COLORS.find(
                              (c) => c.key === kananColors[index]
                            )?.bg
                          : "white",
                      }}
                    />

                    {kananItems.length > 2 && (
                      <Button
                        type="text"
                        icon={<MinusCircleOutlined />}
                        onClick={() => removeKananItem(index)}
                        style={{ marginLeft: 8 }}
                      />
                    )}
                  </div>
                ))}

                <Button
                  type="dashed"
                  onClick={addKananItem}
                  icon={<PlusOutlined />}
                  style={{ width: "100%" }}
                >
                  Tambah Item Kanan
                </Button>
              </Col>
            </Row>

            <Divider>
              Petunjuk: Item dengan warna yang sama akan dipasangkan secara
              otomatis
            </Divider>
          </>
        );

      case "ISIAN":
        return (
          <>
            <Form.Item
              name="jawabanIsian"
              label="Jawaban Benar"
              rules={[{ required: true, message: "Jawaban harus diisi" }]}
            >
              <Input placeholder="Jawaban yang benar" />
            </Form.Item>
            <Form.Item
              name="toleransiTypo"
              label="Toleransi Typo"
              tooltip="Berapa banyak kesalahan ketik yang diperbolehkan (0 berarti harus persis sama)"
              initialValue={0}
            >
              <InputNumber min={0} max={10} />
            </Form.Item>
          </>
        );

      default:
        return null;
    }
  };

  return (
    <Modal
      title="Tambah Soal Ujian"
      open={visible}
      onCancel={() => {
        form.resetFields();
        setOptions(["A", "B"]); // Reset options when closing modal
        onCancel();
      }}
      onOk={handleSubmit}
      confirmLoading={confirmLoading}
      okText="Simpan"
      width={700}
    >
      <Form form={form} layout="vertical">
        <Tabs defaultActiveKey="1">
          <TabPane tab="Informasi Soal" key="1">
            <Row gutter={16}>
              <Col xs={24} sm={24} md={24}>
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
                      option.children
                        .toLowerCase()
                        .includes(input.toLowerCase())
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
              <Col span={24}>
                <Form.Item
                  label="Jenis Soal"
                  name="jenisSoal"
                  rules={[
                    { required: true, message: "Jenis soal wajib dipilih" },
                  ]}
                  initialValue="PG"
                >
                  <Select onChange={handleJenisSoalChange}>
                    <Option value="PG">Pilihan Ganda</Option>
                    <Option value="MULTI">Multi Jawaban</Option>
                    <Option value="COCOK">Mencocokkan</Option>
                    <Option value="ISIAN">Isian</Option>
                  </Select>
                </Form.Item>
              </Col>
              <Col span={24}>
                <Form.Item
                  label="Nama Ujian"
                  name="namaUjian"
                  rules={[
                    { required: true, message: "Nama ujian wajib diisi" },
                  ]}
                >
                  <Input placeholder="Nama Ujian" />
                </Form.Item>
              </Col>
              <Col span={24}>
                <Form.Item
                  label="Pertanyaan"
                  name="pertanyaan"
                  rules={[
                    { required: true, message: "Pertanyaan wajib diisi" },
                  ]}
                >
                  <TextArea rows={4} placeholder="Tulis pertanyaan di sini" />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  label="Bobot"
                  name="bobot"
                  rules={[{ required: true, message: "Bobot wajib diisi" }]}
                  initialValue={5}
                >
                  <InputNumber min={1} max={100} style={{ width: "100%" }} />
                </Form.Item>
              </Col>

              <Col span={12}>
                <Form.Item
                  label="Taksonomi"
                  name="idTaksonomi"
                  rules={[{ required: true, message: "Taksonomi wajib diisi" }]}
                >
                  <Select placeholder="Pilih Taksonomi">
                    {taksonomiList.map(({ idTaksonomi, namaTaksonomi }) => (
                      <Option key={idTaksonomi} value={idTaksonomi}>
                        {namaTaksonomi}
                      </Option>
                    ))}
                  </Select>
                </Form.Item>
              </Col>
              <Col span={24}>
                <Form.Item
                  label="Sekolah:"
                  name="idSchool"
                  style={{ display: "none" }}
                  rules={[{ required: true, message: "Silahkan pilih Kelas" }]}
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
            </Row>
          </TabPane>
          <TabPane tab="Pilihan & Jawaban" key="2">
            {renderQuestionForm()}
          </TabPane>
        </Tabs>
      </Form>
    </Modal>
  );
};

export default AddSoalUjianForm;
