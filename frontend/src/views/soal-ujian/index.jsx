/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef, useCallback } from "react";
import {
  Card,
  Button,
  Table,
  message,
  Upload,
  Row,
  Col,
  Divider,
  Modal,
  Input,
  Space,
  Tag,
  Tooltip,
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
  SearchOutlined,
  EyeOutlined,
} from "@ant-design/icons";
import {
  getSoalUjian,
  deleteSoalUjian,
  addSoalUjian,
  editSoalUjian,
} from "@/api/soalUjian";
import { deleteBankSoal, getBankSoal } from "@/api/bankSoal";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import TypingCard from "@/components/TypingCard";
import AddSoalUjianForm from "./forms/add-soal-ujian-form";
import EditSoalUjianForm from "./forms/edit-soal-ujian-form";
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { reqUserInfo, getUserById } from "@/api/user";

const SoalUjian = () => {
  const [soalUjians, setSoalUjians] = useState([]);
  const [addSoalUjianModalVisible, setAddSoalUjianModalVisible] =
    useState(false);
  const [addSoalUjianModalLoading, setAddSoalUjianModalLoading] =
    useState(false);
  const [editSoalUjianModalVisible, setEditSoalUjianModalVisible] =
    useState(false);
  const [editSoalUjianModalLoading, setEditSoalUjianModalLoading] =
    useState(false);
  const [previewModalVisible, setPreviewModalVisible] = useState(false);
  const [previewData, setPreviewData] = useState(null);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [searchQuery, setSearchQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [filteredData, setFilteredData] = useState([]);

  // Fungsi Helper Table Search
  const { getColumnSearchProps } = useTableSearch();

  const editSoalUjianFormRef = useRef();

  // Filter soal ujian berdasarkan search query
  useEffect(() => {
    if (searchQuery?.trim()) {
      const filtered = soalUjians.filter(
        (item) =>
          item?.namaUjian?.toLowerCase().includes(searchQuery.toLowerCase()) ||
          item?.pertanyaan?.toLowerCase().includes(searchQuery.toLowerCase()) ||
          item?.jenisSoal?.toLowerCase().includes(searchQuery.toLowerCase())
      );
      setFilteredData(filtered);
    } else {
      setFilteredData(soalUjians);
    }
  }, [searchQuery, soalUjians]);

  const fetchSoalUjians = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getSoalUjian();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        // Basic logging for debugging
        console.log("Soal Ujian data received:", content?.length, "items");

        // Handle empty or null content
        const safeContent = Array.isArray(content) ? content : [];

        // Log question types for debugging
        if (safeContent.length > 0) {
          const typeDistribution = safeContent.reduce((acc, item) => {
            const type = item?.jenisSoal || "UNKNOWN";
            acc[type] = (acc[type] || 0) + 1;
            return acc;
          }, {});
          console.log("Question types distribution:", typeDistribution);
        }

        setSoalUjians(safeContent);
        setFilteredData(safeContent);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchSoalUjians();
  }, [fetchSoalUjians]);

  const handleDeleteSoalUjian = (row) => {
    const { idSoalUjian } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content:
        "Apakah Anda yakin ingin menghapus data ini? Data terkait di Bank Soal juga akan dihapus.",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          // Hapus dari Soal Ujian terlebih dahulu
          await deleteSoalUjian({ idSoalUjian });

          // Cari dan hapus data terkait di Bank Soal
          try {
            const bankSoalResult = await getBankSoal();
            if (bankSoalResult.data && bankSoalResult.data.content) {
              const relatedBankSoal = bankSoalResult.data.content.find(
                (item) =>
                  item.soalUjian && item.soalUjian.idSoalUjian === idSoalUjian
              );

              if (relatedBankSoal) {
                await deleteBankSoal({
                  idBankSoal: relatedBankSoal.idBankSoal,
                });
                console.log(
                  `Bank Soal ${relatedBankSoal.idBankSoal} berhasil dihapus`
                );
              }
            }
          } catch (bankSoalError) {
            console.warn(
              "Gagal menghapus dari Bank Soal:",
              bankSoalError.message
            );
            // Tidak perlu menampilkan error karena mungkin sudah dihapus sebelumnya
          }

          message.success("Berhasil dihapus dari Soal Ujian dan Bank Soal");
          fetchSoalUjians();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handlePreviewSoalUjian = (row) => {
    setPreviewData(row);
    setPreviewModalVisible(true);
  };

  const handleEditSoalUjian = (row) => {
    setCurrentRowData({ ...row });
    setEditSoalUjianModalVisible(true);
  };

  const handleAddSoalUjianOk = async (values) => {
    setAddSoalUjianModalLoading(true);
    try {
      // Gunakan values langsung dari form AddSoalUjianForm
      await addSoalUjian(values);
      setAddSoalUjianModalVisible(false);
      message.success("Berhasil menambahkan soal ujian");
      fetchSoalUjians();
    } catch (error) {
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddSoalUjianModalLoading(false);
    }
  };

  const handleEditSoalUjianOk = async (values) => {
    setEditSoalUjianModalLoading(true);
    try {
      const idSoalUjian = values.idSoalUjian || currentRowData.idSoalUjian;

      if (!idSoalUjian) {
        throw new Error(
          "ID Soal Ujian tidak ditemukan. Tidak dapat melakukan update."
        );
      }

      // Structure the data properly like the working forms
      const updatedValues = {
        idSoalUjian: idSoalUjian,
        namaUjian: values.namaUjian,
        pertanyaan: values.pertanyaan,
        bobot: values.bobot?.toString(), // Keep as string like the working example
        jenisSoal: values.jenisSoal,
        idUser: values.idUser,
        idTaksonomi: values.idTaksonomi,
        idKonsentrasiSekolah: values.idKonsentrasiSekolah,
        idSchool: values.idSchool,
        // Include question-specific fields based on jenisSoal
        ...(values.opsi && { opsi: values.opsi }),
        ...(values.pasangan && { pasangan: values.pasangan }),
        ...(values.jawabanBenar && { jawabanBenar: values.jawabanBenar }),
        // Only include toleransiTypo for ISIAN questions, just like add form
        ...(values.jenisSoal === "ISIAN" &&
          values.toleransiTypo !== undefined && {
            toleransiTypo: values.toleransiTypo?.toString(),
          }),
      };

      console.log("Updated values for edit:", updatedValues);
      await editSoalUjian(updatedValues, idSoalUjian);
      setEditSoalUjianModalVisible(false);
      message.success("Berhasil mengedit");
      fetchSoalUjians();
    } catch (error) {
      message.error("Gagal mengedit: " + error.message);
    } finally {
      setEditSoalUjianModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddSoalUjianModalVisible(false);
    setEditSoalUjianModalVisible(false);
    setPreviewModalVisible(false);
  };

  // Render tag untuk jenis soal dengan warna yang berbeda
  const renderJenisSoalTag = (jenisSoal) => {
    let color = "blue";
    let text = "Pilihan Ganda";

    switch (jenisSoal) {
      case "PG":
        color = "blue";
        text = "Pilihan Ganda";
        break;
      case "MULTI":
        color = "green";
        text = "Multi Jawaban";
        break;
      case "COCOK":
        color = "orange";
        text = "Mencocokkan";
        break;
      case "ISIAN":
        color = "purple";
        text = "Isian";
        break;
      default:
        color = "default";
        text = jenisSoal || "Unknown";
    }

    return <Tag color={color}>{text}</Tag>;
  };

  const renderColumns = () => [
    {
      title: "No",
      dataIndex: "index",
      key: "index",
      align: "center",
      width: 70,
      render: (_, __, index) => index + 1,
    },
    {
      title: "Nama Ujian",
      dataIndex: "namaUjian",
      key: "namaUjian",
      align: "left",
      ...getColumnSearchProps("namaUjian"),
      sorter: (a, b) => (a.namaUjian || "").localeCompare(b.namaUjian || ""),
      render: (text) => (
        <Tooltip title={text}>
          <span>
            {text?.length > 30 ? `${text.substring(0, 30)}...` : text}
          </span>
        </Tooltip>
      ),
    },
    {
      title: "Pertanyaan",
      dataIndex: "pertanyaan",
      key: "pertanyaan",
      align: "left",
      ...getColumnSearchProps("pertanyaan"),
      render: (text) => (
        <Tooltip title={text}>
          <span>
            {text?.length > 40 ? `${text.substring(0, 40)}...` : text}
          </span>
        </Tooltip>
      ),
    },
    {
      title: "Jenis Soal",
      dataIndex: "jenisSoal",
      key: "jenisSoal",
      align: "center",
      width: 140,
      filters: [
        { text: "Pilihan Ganda", value: "PG" },
        { text: "Multi Jawaban", value: "MULTI" },
        { text: "Mencocokkan", value: "COCOK" },
        { text: "Isian", value: "ISIAN" },
      ],
      onFilter: (value, record) => record.jenisSoal === value,
      render: (jenisSoal) => renderJenisSoalTag(jenisSoal),
    },
    {
      title: "Bobot",
      dataIndex: "bobot",
      key: "bobot",
      align: "center",
      width: 90,
      sorter: (a, b) => Number(a.bobot) - Number(b.bobot),
    },
    {
      title: "Operasi",
      key: "action",
      align: "center",
      width: 150,
      render: (_, row) => (
        <Space>
          <Button
            type="primary"
            shape="circle"
            icon={<EyeOutlined />}
            onClick={() => handlePreviewSoalUjian(row)}
          />
          <Button
            type="primary"
            shape="circle"
            icon={<EditOutlined />}
            onClick={() => handleEditSoalUjian(row)}
          />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteSoalUjian(row)}
          />
        </Space>
      ),
    },
  ];

  const renderTable = () => {
    return (
      <Table
        rowKey="idSoalUjian"
        dataSource={filteredData || []}
        columns={renderColumns()}
        pagination={{
          pageSize: 10,
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (total, range) =>
            `${range[0]}-${range[1]} dari ${total} soal`,
          showLessItems: true,
        }}
        loading={loading}
        locale={{
          emptyText:
            soalUjians?.length === 0
              ? "Belum ada data soal ujian"
              : "Tidak ada data yang sesuai dengan pencarian",
        }}
      />
    );
  };

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button
          type="primary"
          onClick={() => setAddSoalUjianModalVisible(true)}
        >
          Tambah Soal Ujian
        </Button>
      </Col>
    </Row>
  );

  // Fungsi untuk merender detail soal dalam preview
  const renderPreviewContent = () => {
    if (!previewData) return null;

    return (
      <div>
        <h3>{previewData.namaUjian}</h3>
        <p>
          <strong>Pertanyaan:</strong> {previewData.pertanyaan}
        </p>
        <p>
          <strong>Jenis Soal:</strong>{" "}
          {renderJenisSoalTag(previewData.jenisSoal)}
        </p>
        <p>
          <strong>Bobot:</strong> {previewData.bobot}
        </p>

        {/* Render options based on question type */}
        {(previewData.jenisSoal === "PG" ||
          previewData.jenisSoal === "MULTI") &&
          previewData.opsi && (
            <div>
              <p>
                <strong>Pilihan:</strong>
              </p>
              <ul>
                {Object.entries(previewData.opsi).map(([key, value]) => (
                  <li key={key}>
                    {key}: {value}
                    {previewData.jawabanBenar &&
                      previewData.jawabanBenar.includes(key) && (
                        <Tag color="green" style={{ marginLeft: 8 }}>
                          Jawaban Benar
                        </Tag>
                      )}
                  </li>
                ))}
              </ul>
            </div>
          )}

        {previewData.jenisSoal === "COCOK" && previewData.pasangan && (
          <div>
            <Row gutter={[16, 16]}>
              <Col span={12}>
                <p>
                  <strong>Sisi Kiri:</strong>
                </p>
                <ul>
                  {Object.entries(previewData.pasangan)
                    .filter(([key]) => key.includes("_kiri"))
                    .map(([key, value]) => (
                      <li key={key}>{value}</li>
                    ))}
                </ul>
              </Col>
              <Col span={12}>
                <p>
                  <strong>Sisi Kanan:</strong>
                </p>
                <ul>
                  {Object.entries(previewData.pasangan)
                    .filter(([key]) => key.includes("_kanan"))
                    .map(([key, value]) => (
                      <li key={key}>{value}</li>
                    ))}
                </ul>
              </Col>
            </Row>
            <Divider />
            <p>
              <strong>Pasangan Benar:</strong>
            </p>
            <ul>
              {previewData.jawabanBenar &&
                previewData.jawabanBenar.map((item, index) => (
                  <li key={index}>
                    {item}
                    {previewData.jawabanBenar &&
                      previewData.jawabanBenar.includes(item) && (
                        <Tag color="green" style={{ marginLeft: 8 }}>
                          Jawaban Benar
                        </Tag>
                      )}
                  </li>
                ))}
            </ul>
          </div>
        )}

        {previewData.jenisSoal === "ISIAN" && (
          <div>
            <p>
              <strong>Jawaban Benar:</strong>{" "}
              {previewData.jawabanBenar && previewData.jawabanBenar.join(", ")}
            </p>
            {previewData.toleransiTypo !== undefined && (
              <p>
                <strong>Toleransi Typo:</strong> {previewData.toleransiTypo}
              </p>
            )}
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="app-container">
      <TypingCard
        title="Manajemen Soal Ujian"
        source="Di sini, Anda dapat mengelola soal ujian di sistem. Tambahkan, edit, atau hapus soal ujian sesuai kebutuhan."
      />
      <br />
      {loading ? (
        <Card>
          <Skeleton active paragraph={{ rows: 10 }} />
        </Card>
      ) : (
        <Card style={{ overflowX: "scroll" }}>
          {/* Baris untuk tombol dan pencarian */}
          <Row
            justify="space-between"
            align="middle"
            style={{ marginBottom: 16 }}
          >
            {/* Tombol Tambah & Import */}
            {renderButtons()}

            {/* Kolom Pencarian */}
            <Col>
              <Input.Search
                key="search"
                placeholder="Cari soal ujian..."
                allowClear
                enterButton
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ width: 300 }}
              />
            </Col>
          </Row>

          {/* Status Information */}
          {soalUjians?.length > 0 && (
            <Card
              size="small"
              style={{
                marginBottom: 16,
                backgroundColor: "#f6ffed",
                borderColor: "#b7eb8f",
              }}
            >
              <Row gutter={[16, 8]}>
                <Col span={6}>
                  <strong>Total Soal:</strong> {soalUjians?.length || 0}
                </Col>
                <Col span={6}>
                  <strong>Ditampilkan:</strong> {filteredData?.length || 0}
                </Col>
                <Col span={12}>
                  <strong>Jenis Soal:</strong>{" "}
                  {Object.entries(
                    soalUjians?.reduce((acc, item) => {
                      const type = item?.jenisSoal || "UNKNOWN";
                      acc[type] = (acc[type] || 0) + 1;
                      return acc;
                    }, {})
                  ).map(([type, count]) => (
                    <Tag
                      key={type}
                      color={
                        type === "PG"
                          ? "blue"
                          : type === "MULTI"
                          ? "green"
                          : type === "COCOK"
                          ? "orange"
                          : type === "ISIAN"
                          ? "purple"
                          : "default"
                      }
                      style={{ marginLeft: 4 }}
                    >
                      {type}: {count}
                    </Tag>
                  ))}
                </Col>
              </Row>
            </Card>
          )}

          {/* Tabel */}
          {renderTable()}

          {/* Modal untuk tambah soal ujian */}
          <AddSoalUjianForm
            visible={addSoalUjianModalVisible}
            confirmLoading={addSoalUjianModalLoading}
            onCancel={handleCancel}
            onOk={handleAddSoalUjianOk}
          />

          {/* Modal untuk edit soal ujian */}
          <EditSoalUjianForm
            wrappedComponentRef={editSoalUjianFormRef}
            currentRowData={currentRowData}
            visible={editSoalUjianModalVisible}
            confirmLoading={editSoalUjianModalLoading}
            onCancel={handleCancel}
            onOk={handleEditSoalUjianOk}
          />

          {/* Modal untuk preview soal ujian */}
          <Modal
            title="Detail Soal Ujian"
            open={previewModalVisible}
            onCancel={handleCancel}
            footer={[
              <Button key="close" onClick={handleCancel}>
                Tutup
              </Button>,
            ]}
            width={700}
          >
            {renderPreviewContent()}
          </Modal>
        </Card>
      )}
    </div>
  );
};

export default SoalUjian;
