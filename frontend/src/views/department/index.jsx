/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getDepartments,
  deleteDepartment,
  editDepartment,
  addDepartment,
} from "@/api/department";
import TypingCard from "@/components/TypingCard";
import EditDepartmentForm from "./forms/edit-department-form";
import AddDepartmentForm from "./forms/add-department-form";

const { Column } = Table;

const Department = () => {
  const [departments, setDepartments] = useState([]);
  const [editDepartmentModalVisible, setEditDepartmentModalVisible] =
    useState(false);
  const [editDepartmentModalLoading, setEditDepartmentModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addDepartmentModalVisible, setAddDepartmentModalVisible] =
    useState(false);
  const [addDepartmentModalLoading, setAddDepartmentModalLoading] =
    useState(false);

  const editDepartmentFormRef = useRef();
  const addDepartmentFormRef = useRef();

  const fetchDepartments = async () => {
    try {
      const result = await getDepartments();
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setDepartments(content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditDepartment = (row) => {
    setCurrentRowData({ ...row });
    setEditDepartmentModalVisible(true);
  };

  const handleDeleteDepartment = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteDepartment({ id });
      message.success("Berhasil dihapus");
      fetchDepartments();
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleEditDepartmentOk = () => {
    const form = editDepartmentFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditDepartmentModalLoading(true);
      editDepartment(values, values.id)
        .then((response) => {
          if (response.data.statusCode === 200) {
            form.resetFields();
            setEditDepartmentModalVisible(false);
            setEditDepartmentModalLoading(false);
            message.success("Berhasil diubah!");
            fetchDepartments();
          } else {
            throw new Error(response.data.message);
          }
        })
        .catch((error) => {
          setEditDepartmentModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  const handleCancel = () => {
    setEditDepartmentModalVisible(false);
    setAddDepartmentModalVisible(false);
  };

  const handleAddDepartment = () => {
    setAddDepartmentModalVisible(true);
  };

  const handleAddDepartmentOk = () => {
    const form = addDepartmentFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddDepartmentModalLoading(true);
      addDepartment(values)
        .then((response) => {
          if (response.data.statusCode === 200) {
            form.resetFields();
            setAddDepartmentModalVisible(false);
            setAddDepartmentModalLoading(false);
            message.success("Berhasil ditambahkan!");
            fetchDepartments();
          } else {
            throw new Error(response.data.message);
          }
        })
        .catch((error) => {
          setAddDepartmentModalLoading(false);
          message.error("Gagal menambahkan: " + error.message);
        });
    });
  };

  useEffect(() => {
    fetchDepartments();
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddDepartment}>
        Tambahkan jurusan
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola jurusan di sistem, seperti menambahkan jurusan baru, atau mengubah jurusan yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Jurusan" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={departments} pagination={false}>
          <Column title="ID Jurusan" dataIndex="id" key="id" align="center" />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Deskripsi Jurusan"
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
                  onClick={() => handleEditDepartment(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteDepartment(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditDepartmentForm
        currentRowData={currentRowData}
        wrappedComponentRef={editDepartmentFormRef}
        visible={editDepartmentModalVisible}
        confirmLoading={editDepartmentModalLoading}
        onCancel={handleCancel}
        onOk={handleEditDepartmentOk}
      />

      <AddDepartmentForm
        wrappedComponentRef={addDepartmentFormRef}
        visible={addDepartmentModalVisible}
        confirmLoading={addDepartmentModalLoading}
        onCancel={handleCancel}
        onOk={handleAddDepartmentOk}
      />
    </div>
  );
};

export default Department;
