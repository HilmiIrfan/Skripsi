/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import { getUsers, deleteUser, editUser, addUser } from "@/api/user";
import TypingCard from "@/components/TypingCard";
import EditUserForm from "./forms/edit-grade-form";
import AddUserForm from "./forms/add-grade-form";

const { Column } = Table;

const Grade = () => {
  const [users, setUsers] = useState([]);
  const [editUserModalVisible, setEditUserModalVisible] = useState(false);
  const [editUserModalLoading, setEditUserModalLoading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addUserModalVisible, setAddUserModalVisible] = useState(false);
  const [addUserModalLoading, setAddUserModalLoading] = useState(false);

  const editUserFormRef = useRef();
  const addUserFormRef = useRef();

  const fetchUsers = async () => {
    try {
      const result = await getUsers();
      const { users, status } = result.data;
      if (status === 0) {
        setUsers(users);
      } else {
        message.error("Gagal mengambil data pengguna");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditUser = (row) => {
    setCurrentRowData({ ...row });
    setEditUserModalVisible(true);
  };

  const handleDeleteUser = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteUser({ id });
      message.success("Berhasil dihapus");
      fetchUsers();
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleEditUserOk = () => {
    const form = editUserFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditUserModalLoading(true);
      editUser(values)
        .then(() => {
          form.resetFields();
          setEditUserModalVisible(false);
          setEditUserModalLoading(false);
          message.success("Berhasil diubah!");
          fetchUsers();
        })
        .catch((error) => {
          setEditUserModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  const handleCancel = () => {
    setEditUserModalVisible(false);
    setAddUserModalVisible(false);
  };

  const handleAddUser = () => {
    setAddUserModalVisible(true);
  };

  const handleAddUserOk = () => {
    const form = addUserFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddUserModalLoading(true);
      addUser(values)
        .then(() => {
          form.resetFields();
          setAddUserModalVisible(false);
          setAddUserModalLoading(false);
          message.success("Berhasil ditambahkan!");
          fetchUsers();
        })
        .catch((error) => {
          setAddUserModalLoading(false);
          message.error("Gagal menambahkan: " + error.message);
        });
    });
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddUser}>
        Tambahkan pengguna
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola pengguna di sistem, seperti menambahkan pengguna baru, atau mengubah pengguna yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Pengguna" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={users} pagination={false}>
          <Column title="ID Pengguna" dataIndex="id" key="id" align="center" />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column title="Peran" dataIndex="role" key="role" align="center" />
          <Column
            title="Deskripsi Pengguna"
            dataIndex="description"
            key="description"
            align="center"
          />
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(text, row) => (
              <span>
                <Button
                  type="primary"
                  shape="circle"
                  icon="edit"
                  title="mengedit"
                  onClick={() => handleEditUser(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteUser(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditUserForm
        currentRowData={currentRowData}
        wrappedComponentRef={editUserFormRef}
        visible={editUserModalVisible}
        confirmLoading={editUserModalLoading}
        onCancel={handleCancel}
        onOk={handleEditUserOk}
      />

      <AddUserForm
        wrappedComponentRef={addUserFormRef}
        visible={addUserModalVisible}
        confirmLoading={addUserModalLoading}
        onCancel={handleCancel}
        onOk={handleAddUserOk}
      />
    </div>
  );
};

export default Grade;
