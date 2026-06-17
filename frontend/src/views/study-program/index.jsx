/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getStudyPrograms,
  deleteStudyProgram,
  editStudyProgram,
  addStudyProgram,
} from "@/api/studyProgram";
import { getDepartments } from "@/api/department";
import TypingCard from "@/components/TypingCard";
import EditStudyProgramForm from "./forms/edit-study-program-form";
import AddStudyProgramForm from "./forms/add-study-program-form";

const { Column } = Table;

const StudyProgram = () => {
  const [studyPrograms, setStudyPrograms] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [modalState, setModalState] = useState({
    editVisible: false,
    editLoading: false,
    addVisible: false,
    addLoading: false,
  });
  const [currentRowData, setCurrentRowData] = useState({});

  const editFormRef = useRef();
  const addFormRef = useRef();

  const fetchData = async () => {
    try {
      const [programsRes, departmentsRes] = await Promise.all([
        getStudyPrograms(),
        getDepartments(),
      ]);

      if (programsRes.data.statusCode === 200) {
        setStudyPrograms(programsRes.data.content);
      }
      if (departmentsRes.data.statusCode === 200) {
        setDepartments(departmentsRes.data.content);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleEditStudyProgram = (row) => {
    setCurrentRowData({ ...row });
    setModalState((prev) => ({ ...prev, editVisible: true }));
  };

  const handleDeleteStudyProgram = async (row) => {
    if (row.id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteStudyProgram({ id: row.id });
      message.success("Berhasil dihapus");
      fetchData();
    } catch (error) {
      message.error("Gagal menghapus");
    }
  };

  const handleEditStudyProgramOk = () => {
    const form = editFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, editLoading: true }));
      try {
        await editStudyProgram(values);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          editVisible: false,
          editLoading: false,
        }));
        message.success("Berhasil diperbarui!");
        fetchData();
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

  const handleAddStudyProgram = () => {
    setModalState((prev) => ({ ...prev, addVisible: true }));
  };

  const handleAddStudyProgramOk = () => {
    const form = addFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, addLoading: true }));
      try {
        await addStudyProgram(values);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          addVisible: false,
          addLoading: false,
        }));
        message.success("Berhasil ditambahkan!");
        fetchData();
      } catch (error) {
        message.error("Gagal menambahkan, coba lagi!");
        setModalState((prev) => ({ ...prev, addLoading: false }));
      }
    });
  };

  const cardContent = `Di sini, Anda dapat mengelola program studi di sistem, seperti menambahkan program studi baru, atau mengubah program studi yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Program Studi" source={cardContent} />
      <br />
      <Card
        title={
          <Button type="primary" onClick={handleAddStudyProgram}>
            Tambahkan program studi
          </Button>
        }
      >
        <Table
          variant
          rowKey="id"
          dataSource={studyPrograms}
          pagination={false}
        >
          <Column title="ID Program Studi" dataIndex="id" align="center" />
          <Column title="Jurusan" dataIndex="department.name" align="center" />
          <Column title="Nama" dataIndex="name" align="center" />
          <Column
            title="Deskripsi Program Studi"
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
                  onClick={() => handleEditStudyProgram(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteStudyProgram(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditStudyProgramForm
        wrappedComponentRef={editFormRef}
        currentRowData={currentRowData}
        visible={modalState.editVisible}
        confirmLoading={modalState.editLoading}
        onCancel={handleCancel}
        onOk={handleEditStudyProgramOk}
      />

      <AddStudyProgramForm
        wrappedComponentRef={addFormRef}
        visible={modalState.addVisible}
        confirmLoading={modalState.addLoading}
        onCancel={handleCancel}
        onOk={handleAddStudyProgramOk}
        departments={departments}
      />
    </div>
  );
};

export default StudyProgram;
