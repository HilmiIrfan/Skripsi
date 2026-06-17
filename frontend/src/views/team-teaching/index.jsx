/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getTeamTeachings,
  deleteTeamTeaching,
  editTeamTeaching,
  addTeamTeaching,
} from "@/api/teamTeaching";
import { getLectures } from "@/api/lecture";
import { EditOutlined, DeleteOutlined } from "@ant-design/icons";
import TypingCard from "@/components/TypingCard";
import EditTeamTeachingForm from "./forms/edit-team-teaching-form";
import AddTeamTeachingForm from "./forms/add-team-teaching-form";

const { Column } = Table;

const TeamTeaching = () => {
  const [teamTeachings, setTeamTeachings] = useState([]);
  const [lectures, setLectures] = useState([]);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [editModalLoading, setEditModalLoading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addModalVisible, setAddModalVisible] = useState(false);
  const [addModalLoading, setAddModalLoading] = useState(false);

  const fetchTeamTeachings = async () => {
    try {
      const result = await getTeamTeachings();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setTeamTeachings(content);
      }
    } catch (error) {
      message.error("Gagal mengambil data team teaching");
    }
  };

  const fetchLectures = async () => {
    try {
      const result = await getLectures();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setLectures(content);
      }
    } catch (error) {
      message.error("Gagal mengambil data dosen");
    }
  };

  useEffect(() => {
    fetchTeamTeachings();
    fetchLectures();
  }, []);

  const handleEditTeamTeaching = (row) => {
    setCurrentRowData({ ...row });
    setEditModalVisible(true);
  };

  const handleDeleteTeamTeaching = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }
    try {
      await deleteTeamTeaching({ id });
      message.success("Berhasil dihapus");
      fetchTeamTeachings();
    } catch (error) {
      message.error("Gagal menghapus team teaching");
    }
  };

  const handleEditTeamTeachingOk = async (values) => {
    try {
      setEditModalLoading(true);
      await editTeamTeaching(values);
      setEditModalVisible(false);
      message.success("Berhasil mengedit Team Teaching!");
      fetchTeamTeachings();
    } catch (error) {
      message.error("Gagal mengedit team teaching");
    } finally {
      setEditModalLoading(false);
    }
  };

  const handleAddTeamTeachingOk = async (values) => {
    try {
      setAddModalLoading(true);
      await addTeamTeaching(values);
      setAddModalVisible(false);
      message.success("Berhasil menambahkan Team Teaching!");
      fetchTeamTeachings();
    } catch (error) {
      message.error("Gagal menambahkan Team Teaching!");
    } finally {
      setAddModalLoading(false);
    }
  };

  const handleCancel = () => {
    setEditModalVisible(false);
    setAddModalVisible(false);
  };

  const title = (
    <span>
      <Button type="primary" onClick={() => setAddModalVisible(true)}>
        Tambahkan Team Teaching
      </Button>
    </span>
  );

  const cardContent =
    "Di sini, Anda dapat mengelola Team teachhing untuk menilai soal , Di bawah ini daftar team teaching yang ada.";

  return (
    <div className="app-container">
      <TypingCard title="Team Teaching" source={cardContent} />
      <Card title={title}>
        <Table
          variant
          rowKey="id"
          dataSource={teamTeachings}
          pagination={false}
        >
          <Column
            title="Nama Team Teaching"
            dataIndex="name"
            key="name"
            align="center"
          />
          <Column
            title="Deskripsi tim teaching"
            dataIndex="description"
            key="description"
            align="center"
          />
          <Column
            title="Nama Dosen 1"
            dataIndex={["lecture", "name"]}
            key="lecture.name"
            align="center"
          />
          <Column
            title="Nama Dosen 2"
            dataIndex={["lecture2", "name"]}
            key="lecture2.name"
            align="center"
          />
          <Column
            title="Nama Dosen 3"
            dataIndex={["lecture3", "name"]}
            key="lecture3.name"
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
                  icon={<EditOutlined />}
                  shape="circle"
                  title="mengedit"
                  onClick={() => handleEditTeamTeaching(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  icon={<DeleteOutlined />}
                  shape="circle"
                  title="menghapus"
                  onClick={() => handleDeleteTeamTeaching(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditTeamTeachingForm
        currentRowData={currentRowData}
        visible={editModalVisible}
        confirmLoading={editModalLoading}
        onCancel={handleCancel}
        onOk={handleEditTeamTeachingOk}
      />

      <AddTeamTeachingForm
        visible={addModalVisible}
        confirmLoading={addModalLoading}
        onCancel={handleCancel}
        onOk={handleAddTeamTeachingOk}
        lecture={lectures}
      />
    </div>
  );
};

export default TeamTeaching;
