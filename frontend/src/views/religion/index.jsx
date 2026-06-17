/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getReligions,
  deleteReligion,
  editReligion,
  addReligion,
} from "@/api/religion";
import TypingCard from "@/components/TypingCard";
import EditReligionForm from "./forms/edit-religion-form";
import AddReligionForm from "./forms/add-religion-form";
import { EditOutlined, DeleteOutlined, PlusOutlined } from "@ant-design/icons";

const { Column } = Table;

const Religion = () => {
  const [religions, setReligions] = useState([]);
  const [editReligionModalVisible, setEditReligionModalVisible] =
    useState(false);
  const [editReligionModalLoading, setEditReligionModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addReligionModalVisible, setAddReligionModalVisible] = useState(false);
  const [addReligionModalLoading, setAddReligionModalLoading] = useState(false);

  const fetchReligions = async () => {
    try {
      const result = await getReligions();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setReligions(content);
      }
    } catch (error) {
      message.error("Gagal mengambil data");
    }
  };

  useEffect(() => {
    fetchReligions();
  }, []);

  const handleEditReligion = (row) => {
    setCurrentRowData({ ...row });
    setEditReligionModalVisible(true);
  };

  const handleDeleteReligion = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }
    try {
      await deleteReligion({ id });
      message.success("Berhasil dihapus");
      fetchReligions();
    } catch (error) {
      message.error("Gagal menghapus");
    }
  };

  const handleEditReligionOk = async (values) => {
    try {
      setEditReligionModalLoading(true);
      await editReligion(values, values.id);
      setEditReligionModalVisible(false);
      message.success("Berhasil diubah!");
      fetchReligions();
    } catch (error) {
      message.error("Gagal mengubah");
    } finally {
      setEditReligionModalLoading(false);
    }
  };

  const handleCancel = () => {
    setEditReligionModalVisible(false);
    setAddReligionModalVisible(false);
  };

  const handleAddReligion = () => {
    setAddReligionModalVisible(true);
  };

  const handleAddReligionOk = async (values) => {
    try {
      setAddReligionModalLoading(true);
      await addReligion(values);
      setAddReligionModalVisible(false);
      message.success("Berhasil ditambahkan!");
      fetchReligions();
    } catch (error) {
      message.error("Gagal menambahkan, coba lagi!");
    } finally {
      setAddReligionModalLoading(false);
    }
  };

  const title = (
    <span>
      <Button
        type="primary"
        onClick={handleAddReligion}
        icon={<PlusOutlined />}
      >
        Tambahkan agama
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola agama di sistem, seperti menambahkan agama baru, atau mengubah agama yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Agama" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={religions} pagination={false}>
          <Column title="ID Agama" dataIndex="id" key="id" align="center" />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Deskripsi Agama"
            dataIndex="description"
            key="description"
            align="center"
          />
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(_, record) => (
              <span>
                <Button
                  type="primary"
                  icon={<EditOutlined />}
                  onClick={() => handleEditReligion(record)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  danger
                  icon={<DeleteOutlined />}
                  onClick={() => handleDeleteReligion(record)}
                />
              </span>
            )}
          />
        </Table>
      </Card>
      <EditReligionForm
        currentRowData={currentRowData}
        visible={editReligionModalVisible}
        confirmLoading={editReligionModalLoading}
        onCancel={handleCancel}
        onOk={handleEditReligionOk}
      />
      <AddReligionForm
        visible={addReligionModalVisible}
        confirmLoading={addReligionModalLoading}
        onCancel={handleCancel}
        onOk={handleAddReligionOk}
      />
    </div>
  );
};

export default Religion;
