/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getLectures,
  deleteLecture,
  editLecture,
  addLecture,
} from "@/api/lecture";
import { getReligions } from "@/api/religion";
import { getUsersNotUsedInLectures } from "@/api/user";
import { getStudyPrograms } from "@/api/studyProgram";
import TypingCard from "@/components/TypingCard";
import EditLectureForm from "./forms/edit-lecture-form";
import AddLectureForm from "./forms/add-lecture-form";

const { Column } = Table;

const Lecture = () => {
  const [lectures, setLectures] = useState([]);
  const [religions, setReligions] = useState([]);
  const [users, setUsers] = useState([]);
  const [studyPrograms, setStudyPrograms] = useState([]);
  const [editLectureModalVisible, setEditLectureModalVisible] = useState(false);
  const [editLectureModalLoading, setEditLectureModalLoading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addLectureModalVisible, setAddLectureModalVisible] = useState(false);
  const [addLectureModalLoading, setAddLectureModalLoading] = useState(false);

  const editLectureFormRef = useRef();
  const addLectureFormRef = useRef();

  const fetchLectures = async () => {
    try {
      const result = await getLectures();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setLectures(content);
      } else {
        message.error("Gagal mengambil data guru");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const fetchReligions = async () => {
    try {
      const result = await getReligions();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setReligions(content);
      } else {
        message.error("Gagal mengambil data agama");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const fetchUsers = async () => {
    try {
      const result = await getUsersNotUsedInLectures();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setUsers(content);
      } else {
        message.error("Gagal mengambil data pengguna");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const fetchStudyPrograms = async () => {
    try {
      const result = await getStudyPrograms();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setStudyPrograms(content);
      } else {
        message.error("Gagal mengambil data program studi");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditLecture = (row) => {
    setCurrentRowData({ ...row });
    setEditLectureModalVisible(true);
  };

  const handleDeleteLecture = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteLecture({ id });
      message.success("Berhasil dihapus");
      await Promise.all([fetchLectures(), fetchUsers()]);
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleEditLectureOk = () => {
    const form = editLectureFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditLectureModalLoading(true);
      editLecture(values)
        .then((response) => {
          form.resetFields();
          setEditLectureModalVisible(false);
          setEditLectureModalLoading(false);
          message.success("Berhasil diubah!");
          Promise.all([fetchLectures(), fetchUsers()]);
        })
        .catch((error) => {
          setEditLectureModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  const handleCancel = () => {
    setEditLectureModalVisible(false);
    setAddLectureModalVisible(false);
  };

  const handleAddLecture = () => {
    setAddLectureModalVisible(true);
  };

  const handleAddLectureOk = () => {
    const form = addLectureFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddLectureModalLoading(true);
      addLecture(values)
        .then((response) => {
          form.resetFields();
          setAddLectureModalVisible(false);
          setAddLectureModalLoading(false);
          message.success("Berhasil ditambahkan!");
          Promise.all([fetchLectures(), fetchUsers()]);
        })
        .catch((error) => {
          console.error(error.response?.data);
          setAddLectureModalLoading(false);
          message.error(
            "Gagal menambahkan: " +
              (error.response?.data?.message || error.message)
          );
        });
    });
  };

  useEffect(() => {
    Promise.all([
      fetchLectures(),
      fetchReligions(),
      fetchUsers(),
      fetchStudyPrograms(),
    ]);
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddLecture}>
        Tambahkan guru
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola guru di sistem, seperti menambahkan guru baru, atau mengubah guru yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Guru" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={lectures} pagination={false}>
          <Column title="NIDN" dataIndex="nidn" key="nidn" align="center" />
          <Column
            title="Nama Depan"
            dataIndex="name"
            key="name"
            align="center"
          />
          <Column
            title="Tempat Lahir"
            dataIndex="place_born"
            key="place_born"
            align="center"
          />
          <Column
            title="Agama"
            dataIndex="religion.name"
            key="religion.name"
            align="center"
          />
          <Column
            title="Telepon"
            dataIndex="phone"
            key="phone"
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
                  onClick={() => handleEditLecture(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteLecture(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditLectureForm
        currentRowData={currentRowData}
        wrappedComponentRef={editLectureFormRef}
        visible={editLectureModalVisible}
        confirmLoading={editLectureModalLoading}
        onCancel={handleCancel}
        onOk={handleEditLectureOk}
      />

      <AddLectureForm
        wrappedComponentRef={addLectureFormRef}
        visible={addLectureModalVisible}
        confirmLoading={addLectureModalLoading}
        onCancel={handleCancel}
        onOk={handleAddLectureOk}
        religion={religions}
        user={users}
        studyProgram={studyPrograms}
      />
    </div>
  );
};

export default Lecture;
