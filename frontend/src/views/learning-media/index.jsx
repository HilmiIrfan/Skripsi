/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getLearningMedias,
  deleteLearningMedia,
  editLearningMedia,
  addLearningMedia,
} from "@/api/learningMedia";
import TypingCard from "@/components/TypingCard";
import EditLearningMediaForm from "./forms/edit-learningMedia-form";
import AddLearningMediaForm from "./forms/add-learningMedia-form";

const { Column } = Table;

const LearningMedia = () => {
  const [learningMedias, setLearningMedias] = useState([]);
  const [editLearningMediaModalVisible, setEditLearningMediaModalVisible] =
    useState(false);
  const [editLearningMediaModalLoading, setEditLearningMediaModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addLearningMediaModalVisible, setAddLearningMediaModalVisible] =
    useState(false);
  const [addLearningMediaModalLoading, setAddLearningMediaModalLoading] =
    useState(false);

  const editLearningMediaFormRef = useRef();
  const addLearningMediaFormRef = useRef();

  const fetchLearningMedias = async () => {
    try {
      const result = await getLearningMedias();
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setLearningMedias(content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditLearningMedia = (row) => {
    setCurrentRowData({ ...row });
    setEditLearningMediaModalVisible(true);
  };

  const handleDeleteLearningMedia = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteLearningMedia({ id });
      message.success("Berhasil dihapus");
      fetchLearningMedias();
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleEditLearningMediaOk = () => {
    const form = editLearningMediaFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditLearningMediaModalLoading(true);
      editLearningMedia(values, values.id)
        .then((response) => {
          form.resetFields();
          setEditLearningMediaModalVisible(false);
          setEditLearningMediaModalLoading(false);
          message.success("Berhasil diubah!");
          fetchLearningMedias();
        })
        .catch((error) => {
          setEditLearningMediaModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  const handleCancel = () => {
    setEditLearningMediaModalVisible(false);
    setAddLearningMediaModalVisible(false);
  };

  const handleAddLearningMedia = () => {
    setAddLearningMediaModalVisible(true);
  };

  const handleAddLearningMediaOk = () => {
    const form = addLearningMediaFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddLearningMediaModalLoading(true);
      addLearningMedia(values)
        .then((response) => {
          form.resetFields();
          setAddLearningMediaModalVisible(false);
          setAddLearningMediaModalLoading(false);
          message.success("Berhasil ditambahkan!");
          fetchLearningMedias();
        })
        .catch((error) => {
          setAddLearningMediaModalLoading(false);
          message.error("Gagal menambahkan: " + error.message);
        });
    });
  };

  useEffect(() => {
    fetchLearningMedias();
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddLearningMedia}>
        Tambahkan media pembelajaran
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola media pembelajaran di sistem, seperti menambahkan media pembelajaran baru, atau mengubah media pembelajaran yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Media Pembelajaran" source={cardContent} />
      <br />
      <Card title={title}>
        <Table
          variant
          rowKey="id"
          dataSource={learningMedias}
          pagination={false}
        >
          <Column
            title="ID Media Pembelajaran"
            dataIndex="id"
            key="id"
            align="center"
          />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Deskripsi Media Pembelajaran"
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
                  onClick={() => handleEditLearningMedia(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteLearningMedia(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditLearningMediaForm
        currentRowData={currentRowData}
        wrappedComponentRef={editLearningMediaFormRef}
        visible={editLearningMediaModalVisible}
        confirmLoading={editLearningMediaModalLoading}
        onCancel={handleCancel}
        onOk={handleEditLearningMediaOk}
      />

      <AddLearningMediaForm
        wrappedComponentRef={addLearningMediaFormRef}
        visible={addLearningMediaModalVisible}
        confirmLoading={addLearningMediaModalLoading}
        onCancel={handleCancel}
        onOk={handleAddLearningMediaOk}
      />
    </div>
  );
};

export default LearningMedia;
