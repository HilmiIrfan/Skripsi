/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getStudents,
  deleteStudent,
  editStudent,
  addStudent,
} from "@/api/student";
import { getReligions } from "@/api/religion";
import { getUsersNotUsedInLectures } from "@/api/user";
import { getStudyPrograms } from "@/api/studyProgram";
import TypingCard from "@/components/TypingCard";
import EditStudentForm from "./forms/edit-student-form";
import AddStudentForm from "./forms/add-student-form";

const { Column } = Table;

const Student = () => {
  const [students, setStudents] = useState([]);
  const [religions, setReligions] = useState([]);
  const [users, setUsers] = useState([]);
  const [studyPrograms, setStudyPrograms] = useState([]);
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
      const [studentsRes, religionsRes, usersRes, programsRes] =
        await Promise.all([
          getStudents(),
          getReligions(),
          getUsersNotUsedInLectures(),
          getStudyPrograms(),
        ]);

      if (studentsRes.data.statusCode === 200) {
        setStudents(studentsRes.data.content);
      }
      if (religionsRes.data.statusCode === 200) {
        setReligions(religionsRes.data.content);
      }
      if (usersRes.data.statusCode === 200) {
        setUsers(usersRes.data.content);
      }
      if (programsRes.data.statusCode === 200) {
        setStudyPrograms(programsRes.data.content);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleEditStudent = (row) => {
    setCurrentRowData({ ...row });
    setModalState((prev) => ({ ...prev, editVisible: true }));
  };

  const handleDeleteStudent = async (row) => {
    if (row.id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteStudent({ id: row.id });
      message.success("Berhasil dihapus");
      fetchData();
    } catch (error) {
      message.error("Gagal menghapus");
    }
  };

  const handleEditStudentOk = () => {
    const form = editFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, editLoading: true }));
      try {
        await editStudent(values);
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

  const handleAddStudent = () => {
    setModalState((prev) => ({ ...prev, addVisible: true }));
  };

  const handleAddStudentOk = () => {
    const form = addFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, addLoading: true }));
      try {
        await addStudent(values);
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

  const cardContent = `Di sini, Anda dapat mengelola siswa di sistem, seperti menambahkan siswa baru, atau mengubah siswa yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Siswa" source={cardContent} />
      <br />
      <Card
        title={
          <Button type="primary" onClick={handleAddStudent}>
            Tambahkan siswa
          </Button>
        }
      >
        <Table variant rowKey="id" dataSource={students} pagination={false}>
          <Column title="NISN" dataIndex="nisn" key="nisn" align="center" />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Konsentrasi Keahlian"
            dataIndex="konsentrasiKeahlian.konsentrasi"
            key="konsentrasiKeahlian.konsentrasi"
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
                  onClick={() => handleEditStudent(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteStudent(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditStudentForm
        wrappedComponentRef={editFormRef}
        currentRowData={currentRowData}
        visible={modalState.editVisible}
        confirmLoading={modalState.editLoading}
        onCancel={handleCancel}
        onOk={handleEditStudentOk}
      />

      <AddStudentForm
        wrappedComponentRef={addFormRef}
        visible={modalState.addVisible}
        confirmLoading={modalState.addLoading}
        onCancel={handleCancel}
        onOk={handleAddStudentOk}
        religion={religions}
        user={users}
        studyProgram={studyPrograms}
      />
    </div>
  );
};

export default Student;
