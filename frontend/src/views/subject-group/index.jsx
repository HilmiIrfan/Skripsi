/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getSubjectGroups,
  deleteSubjectGroup,
  editSubjectGroup,
  addSubjectGroup,
} from "@/api/subjectGroup";
import TypingCard from "@/components/TypingCard";
import EditSubjectGroupForm from "./forms/edit-subject-group-form";
import AddSubjectGroupForm from "./forms/add-subject-group-form";

const { Column } = Table;

const SubjectGroup = () => {
  const [subjectGroups, setSubjectGroups] = useState([]);
  const [modalState, setModalState] = useState({
    editVisible: false,
    editLoading: false,
    addVisible: false,
    addLoading: false,
  });
  const [currentRowData, setCurrentRowData] = useState({});

  const editFormRef = useRef();
  const addFormRef = useRef();

  const fetchSubjectGroups = async () => {
    try {
      const result = await getSubjectGroups();
      if (result.data.statusCode === 200) {
        setSubjectGroups(result.data.content);
      }
    } catch (error) {
      console.error("Error fetching subject groups:", error);
    }
  };

  useEffect(() => {
    fetchSubjectGroups();
  }, []);

  const handleEditSubjectGroup = (row) => {
    setCurrentRowData({ ...row });
    setModalState((prev) => ({ ...prev, editVisible: true }));
  };

  const handleDeleteSubjectGroup = async (row) => {
    if (row.id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteSubjectGroup({ id: row.id });
      message.success("Berhasil dihapus");
      fetchSubjectGroups();
    } catch (error) {
      message.error("Gagal menghapus");
    }
  };

  const handleEditSubjectGroupOk = () => {
    const form = editFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, editLoading: true }));
      try {
        await editSubjectGroup(values, values.id);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          editVisible: false,
          editLoading: false,
        }));
        message.success("Berhasil diperbarui!");
        fetchSubjectGroups();
      } catch (error) {
        message.error("Gagal memperbarui");
        setModalState((prev) => ({ ...prev, editLoading: false }));
      }
    });
  };

  const handleCancel = () => {
    setModalState((prev) => ({
      ...prev,
      editVisible: false,
      addVisible: false,
    }));
  };

  const handleAddSubjectGroup = () => {
    setModalState((prev) => ({ ...prev, addVisible: true }));
  };

  const handleAddSubjectGroupOk = () => {
    const form = addFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, addLoading: true }));
      try {
        await addSubjectGroup(values);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          addVisible: false,
          addLoading: false,
        }));
        message.success("Berhasil ditambahkan!");
        fetchSubjectGroups();
      } catch (error) {
        message.error("Gagal menambahkan, coba lagi!");
        setModalState((prev) => ({ ...prev, addLoading: false }));
      }
    });
  };

  const cardContent = `Di sini, Anda dapat mengelola rumpun mata kuliah di sistem, seperti menambahkan rumpun mata kuliah baru, atau mengubah rumpun mata kuliah yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Rumpun Mata Kuliah" source={cardContent} />
      <br />
      <Card
        title={
          <Button type="primary" onClick={handleAddSubjectGroup}>
            Tambahkan rumpun mata kuliah
          </Button>
        }
      >
        <Table
          variant
          rowKey="id"
          dataSource={subjectGroups}
          pagination={false}
        >
          <Column title="ID Rumpun Mata Kuliah" dataIndex="id" align="center" />
          <Column title="Nama" dataIndex="name" align="center" />
          <Column
            title="Deskripsi Rumpun Mata Kuliah"
            dataIndex="description"
            align="center"
          />
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(_, row) => (
              <span>
                <Button
                  type="primary"
                  shape="circle"
                  icon="edit"
                  title="mengedit"
                  onClick={() => handleEditSubjectGroup(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteSubjectGroup(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditSubjectGroupForm
        wrappedComponentRef={editFormRef}
        currentRowData={currentRowData}
        visible={modalState.editVisible}
        confirmLoading={modalState.editLoading}
        onCancel={handleCancel}
        onOk={handleEditSubjectGroupOk}
      />

      <AddSubjectGroupForm
        wrappedComponentRef={addFormRef}
        visible={modalState.addVisible}
        confirmLoading={modalState.addLoading}
        onCancel={handleCancel}
        onOk={handleAddSubjectGroupOk}
      />
    </div>
  );
};

export default SubjectGroup;
