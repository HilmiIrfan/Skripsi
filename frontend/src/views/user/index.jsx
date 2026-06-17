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
} from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  UploadOutlined,
  SearchOutlined,
} from "@ant-design/icons";
import { getUsers, deleteUser, addUser, editUser } from "@/api/user";
import { Skeleton } from "antd";
import Highlighter from "react-highlight-words";
import TypingCard from "@/components/TypingCard";
import AddUserForm from "./forms/add-user-form";
import EditUserForm from "./forms/edit-user-form";
import { useTableSearch } from "@/helper/tableSearchHelper.jsx";
import { reqUserInfo, getUserById } from "@/api/user";
import { set } from "nprogress";
import * as XLSX from "xlsx";

const User = () => {
  const [users, setUsers] = useState([]);
  const [addUserModalVisible, setAddUserModalVisible] = useState(false);
  const [addUserModalLoading, setAddUserModalLoading] = useState(false);
  const [editUserModalVisible, setEditUserModalVisible] = useState(false);
  const [editUserModalLoading, setEditUserModalLoading] = useState(false);
  const [importModalVisible, setImportModalVisible] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [searchKeyword, setSearchKeyword] = useState("");
  const [userIdJson, setUserIdJson] = useState("");
  const [loading, setLoading] = useState(true);
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [currentUserRole, setCurrentUserRole] = useState("");
  const [user, setUser] = useState(null);
  const [importFile, setImportFile] = useState(null);

  // Fungsi Helper Table Search
  const { getColumnSearchProps } = useTableSearch();

  const editUserFormRef = useRef();
  const addUserFormRef = useRef();

  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo();
      if (response && response.data) {
        setUser(response.data);
      } else {
        setUser(null);
      }
    } catch (error) {
      console.error("Error fetching user info:", error);
      setUser(null);
    }
  };

  const fetchUsers = useCallback(async () => {
    setLoading(true);
    try {
      const result = await getUsers();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        let filteredUsers = content;
        // ✅ Ubah ID role menjadi nama
        const transformedUsers = filteredUsers.map((user) => ({
          ...user,
          roles: mapRoleToName(user.roles), // ubah ID jadi nama
        }));

        setUsers(transformedUsers);
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
    fetchUsers();
    fetchUserInfo();
  }, [fetchUsers]);

  const handleDeleteUser = (row) => {
    const { id } = row;
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin menghapus data ini?",
      okText: "Ya",
      okType: "danger",
      cancelText: "Tidak",
      onOk: async () => {
        try {
          console.log("ID User yang akan dihapus:", id);
          await deleteUser({ id });
          message.success("Berhasil dihapus");
          fetchUsers();
        } catch (error) {
          message.error("Gagal menghapus: " + error.message);
        }
      },
    });
  };

  const handleImportFile = (file) => {
    const reader = new FileReader();
    reader.onload = async (e) => {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: "array" });
      const sheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[sheetName];
      const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

      // Ambil header dan data
      const [header, ...rows] = jsonData;
      // Index kolom sesuai header
      const idx = (col) => header.indexOf(col);

      let successCount = 0;
      let errorCount = 0;
      let errorMessages = [];

      // Loop dan masukkan satu-satu
      for (const row of rows) {
        if (!row[idx("username")]) continue;

        // Fungsi untuk membersihkan data dengan lebih hati-hati
        const cleanText = (val) => {
          if (val === null || val === undefined) return "";
          return val.toString().trim(); // Hanya trim whitespace di awal/akhir
        };

        // Fungsi khusus untuk username dan password (tidak boleh ada spasi)
        const cleanCredential = (val) => {
          if (val === null || val === undefined) return "";
          return val.toString().replace(/\s/g, "").trim(); // Hapus semua spasi untuk credentials
        };

        // Fungsi untuk role (case-insensitive, standardize)
        const cleanRole = (val) => {
          if (val === null || val === undefined) return "";
          const roleStr = val.toString().trim().toLowerCase();

          // Map common role variations
          const roleMap = {
            administrator: "1",
            admin: "1",
            operator: "2",
            guru: "3",
            teacher: "3",
            siswa: "5",
            student: "5",
            1: "1",
            2: "2",
            3: "3",
            4: "4",
            5: "5",
          };

          return roleMap[roleStr] || "4"; // Default ke "Tidak Diketahui"
        };

        let currentUsername = "Unknown";
        try {
          const userData = {
            name: cleanText(row[idx("name")]), // Preserve spaces in name
            username: cleanCredential(row[idx("username")]), // No spaces in username
            email:
              row[idx("email")] === "-" ? "" : cleanText(row[idx("email")]), // Preserve email format
            password: cleanCredential(row[idx("password")]), // No spaces in password
            roles: cleanRole(row[idx("roles")]), // Standardize role
            schoolId: user?.school_id, // Ambil ID sekolah dari user yang login
          };

          currentUsername = userData.username || "Unknown";

          console.log(`🔄 Processing user: "${userData.username}"`);
          console.log(`📋 User data:`, {
            name: userData.name,
            username: userData.username,
            email: userData.email,
            roles: userData.roles,
            schoolId: userData.schoolId,
          });

          // Validate required fields
          if (!userData.username || !userData.name) {
            throw new Error("Username dan nama wajib diisi");
          }

          await handleAddUserOk(userData);
          successCount++;
          console.log(`✅ User "${userData.username}" berhasil diimport`);
        } catch (error) {
          errorCount++;
          const errorMsg = `User "${currentUsername}" gagal diimport: ${error.message}`;
          errorMessages.push(errorMsg);
          console.error(errorMsg);
        }
      }

      // Tampilkan hasil import
      if (successCount > 0) {
        message.success(
          `Import berhasil! ${successCount} user berhasil ditambahkan.`
        );
      }

      if (errorCount > 0) {
        // Tampilkan beberapa error pertama saja
        const displayErrors = errorMessages.slice(0, 3);
        const moreErrors =
          errorMessages.length > 3
            ? `\n... dan ${errorMessages.length - 3} error lainnya`
            : "";

        message.error({
          content: `${errorCount} user gagal diimport:\n${displayErrors.join(
            "\n"
          )}${moreErrors}`,
          duration: 8,
        });
      }

      setImportModalVisible(false);
      fetchUsers();
    };
    reader.readAsArrayBuffer(file);
    return false; // Supaya Upload tidak auto-upload ke server
  };

  const handleEditUser = (row) => {
    setCurrentRowData({ ...row });
    setEditUserModalVisible(true);
  };

  const handleAddUserOk = async (values) => {
    setAddUserModalLoading(true);
    try {
      const updatedData = {
        idUser: null,
        name: values.name,
        username: values.username,
        email: values.email,
        password: values.password,
        roles: values.roles,
        schoolId: values.schoolId,
      };
      // console.log("Updated Data:", updatedData);
      await addUser(updatedData);
      setAddUserModalVisible(false);
      message.success("Berhasil menambahkan");
      fetchUsers();
    } catch (error) {
      setAddUserModalVisible(false);
      message.error("Gagal menambahkan: " + error.message);
    } finally {
      setAddUserModalLoading(false);
    }
  };

  const handleEditUserOk = async (values) => {
    setEditUserModalLoading(true);
    try {
      const updatedData = {
        idUser: values.id || currentRowData.id,
        name: values.name,
        username: values.username,
        email: values.email,
        roles: values.roles,
        schoolId: values.idSchool,
      };

      // Tambahkan password jika ada
      if (values.password && values.password.trim() !== "") {
        updatedData.password = values.password;
      }

      console.log("Updated Data:", updatedData);
      await editUser(updatedData, updatedData.idUser);

      setEditUserModalVisible(false);
      message.success("Berhasil mengedit");
      fetchUsers();
    } catch (error) {
      setEditUserModalVisible(false);
      message.error("Gagal mengedit: " + error.message);
    } finally {
      setEditUserModalLoading(false);
    }
  };

  const handleCancel = () => {
    setAddUserModalVisible(false);
    setEditUserModalVisible(false);
  };

  const handleSearch = (keyword) => {
    setSearchKeyword(keyword);
    getUsers();
  };

  const mapRoleToName = (roleId) => {
    switch (roleId) {
      case "1":
        return "Administrator";
      case "2":
        return "Operator";
      case "3":
        return "Guru";
      case "4":
        return "Tidak Diketahui";
      case "5":
        return "Siswa";
      default:
        return "Tidak Diketahui";
    }
  };

  const renderColumns = () => [
    {
      title: "No",
      dataIndex: "index",
      key: "index",
      align: "center",
      render: (_, __, index) => index + 1,
    },
    ...(user?.roles === "ROLE_ADMINISTRATOR"
      ? [
          {
            title: "Sekolah",
            dataIndex: ["school", "nameSchool"],
            key: "nameSchool",
            align: "center",
            ...getColumnSearchProps("school", "school.nameSchool"),
            sorter: (a, b) =>
              a.school.nameSchool.localeCompare(b.school.nameSchool),
          },
        ]
      : []),
    {
      title: "Nama",
      dataIndex: "name",
      key: "name",
      align: "center",
      ...getColumnSearchProps("name"),
      sorter: (a, b) => a.name.localeCompare(b.name),
    },
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
      align: "center",
      ...getColumnSearchProps("username"),
      sorter: (a, b) => a.username.localeCompare(b.username),
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      align: "center",
      ...getColumnSearchProps("email"),
      sorter: (a, b) => a.email.localeCompare(b.email),
    },
    {
      title: "Roles",
      dataIndex: "roles",
      key: "roles",
      align: "center",
      ...getColumnSearchProps("roles"),
      sorter: (a, b) => a.roles.localeCompare(b.roles),
    },
    {
      title: "Operasi",
      key: "action",
      align: "center",
      render: (_, row) => (
        <span>
          <Button
            type="primary"
            shape="circle"
            icon={<EditOutlined />}
            onClick={() => handleEditUser(row)}
          />
          <Divider type="vertical" />
          <Button
            type="primary"
            danger
            shape="circle"
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteUser(row)}
          />
        </span>
      ),
    },
  ];

  const renderTable = () => (
    <Table
      rowKey="id"
      dataSource={users}
      columns={renderColumns()}
      pagination={{ pageSize: 10 }}
    />
  );

  const renderButtons = () => (
    <Row gutter={[16, 16]} justify="start">
      <Col>
        <Button type="primary" onClick={() => setAddUserModalVisible(true)}>
          Tambahkan User
        </Button>
      </Col>
      <Col>
        <Button
          icon={<UploadOutlined />}
          onClick={() => setImportModalVisible(true)}
        >
          Import File
        </Button>
      </Col>
    </Row>
  );

  return (
    <div className="app-container">
      <TypingCard
        title="Manajemen User"
        source="Di sini, Anda dapat mengelola user di sistem."
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
                placeholder="Cari user..."
                allowClear
                enterButton
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ width: 300 }}
              />
            </Col>
          </Row>

          {/* Tabel */}
          {renderTable()}

          <AddUserForm
            wrappedComponentRef={addUserFormRef}
            visible={addUserModalVisible}
            confirmLoading={addUserModalLoading}
            onCancel={handleCancel}
            onOk={handleAddUserOk}
          />

          <EditUserForm
            wrappedComponentRef={editUserFormRef}
            currentRowData={currentRowData}
            visible={editUserModalVisible}
            confirmLoading={editUserModalLoading}
            onCancel={handleCancel}
            onOk={handleEditUserOk}
          />
        </Card>
      )}

      <Modal
        title="Import User"
        open={importModalVisible}
        onCancel={() => setImportModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setImportModalVisible(false)}>
            Cancel
          </Button>,
          <Button
            key="upload"
            type="primary"
            loading={uploading}
            onClick={() => {
              if (importFile) {
                setUploading(true);
                handleImportFile(importFile);
                setUploading(false);
              } else {
                message.warning("Pilih file terlebih dahulu!");
              }
            }}
          >
            Import
          </Button>,
        ]}
        width={600}
      >
        <div style={{ marginBottom: 16 }}>
          <label
            style={{ display: "block", marginBottom: 8, fontWeight: "bold" }}
          >
            Upload File CSV/Excel:
          </label>
          <Upload
            beforeUpload={(file) => {
              setImportFile(file);
              return false; // Jangan auto-upload
            }}
            accept=".csv,.xlsx,.xls"
            showUploadList={false}
            maxCount={1}
          >
            <Button icon={<UploadOutlined />}>Pilih File</Button>
          </Upload>
          {importFile && (
            <div
              style={{
                marginTop: 8,
                padding: 8,
                backgroundColor: "#f0f2f5",
                borderRadius: 4,
              }}
            >
              <strong>File terpilih:</strong> {importFile.name}
            </div>
          )}
        </div>

        <div
          style={{
            padding: 12,
            backgroundColor: "#e6f7ff",
            borderRadius: 4,
            fontSize: "12px",
          }}
        >
          <strong>
            💡 Disarankan menggunakan format CSV untuk menghindari file corrupt!
          </strong>
          <br />
          <br />
          <strong>Format CSV/Excel yang diperlukan:</strong>
          <br />
          <strong>Kolom Wajib:</strong> name, username, email, password, roles
          <br />
          <br />
          <strong>🔧 Perbaikan Import:</strong>
          <br />• <strong>Preserve Spaces:</strong> Spasi dalam nama akan
          dipertahankan (contoh: &quot;John Doe&quot;)
          <br />• <strong>Clean Credentials:</strong> Username dan password akan
          dibersihkan dari spasi
          <br />• <strong>Case Insensitive Roles:</strong> Role dapat ditulis
          dalam format apapun
          <br />• <strong>Smart Role Mapping:</strong> Otomatis convert nama
          role ke ID yang benar
          <br />
          <br />
          <strong>Role Mapping:</strong>
          <br />• <strong>Administrator/Admin:</strong> ID = 1
          <br />• <strong>Operator:</strong> ID = 2
          <br />• <strong>Guru/Teacher:</strong> ID = 3
          <br />• <strong>Siswa/Student:</strong> ID = 5
          <br />• <strong>Lainnya:</strong> ID = 4 (Tidak Diketahui)
          <br />
          <br />
          <strong>Contoh data yang benar:</strong>
          <br />• <strong>name:</strong> &quot;John Doe&quot;, &quot;Maria
          Santos&quot; (spasi dipertahankan)
          <br />• <strong>username:</strong> &quot;johndoe&quot;,
          &quot;maria123&quot; (tanpa spasi)
          <br />• <strong>email:</strong> &quot;john@example.com&quot; atau
          &quot;-&quot; untuk kosong
          <br />• <strong>password:</strong> &quot;password123&quot; (tanpa
          spasi)
          <br />• <strong>roles:</strong> &quot;Administrator&quot;,
          &quot;guru&quot;, &quot;SISWA&quot;, &quot;3&quot; (fleksibel format)
          <br />
          <br />
          <strong>Error Handling:</strong>
          <br />
          • Username dan nama wajib diisi
          <br />
          • User yang gagal akan di-skip dengan pesan error
          <br />
          • Import berlanjut ke data berikutnya
          <br />
          • Summary hasil import akan ditampilkan
          <br />
          <br />
          <strong>Download Template:</strong>
          <br />
          <div style={{ marginTop: 8 }}>
            <a
              href="/templates/import-template-user.csv"
              download
              style={{ color: "#1890ff" }}
            >
              📄 Template User (CSV)
            </a>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default User;
