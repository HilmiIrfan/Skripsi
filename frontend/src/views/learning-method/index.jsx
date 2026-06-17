/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getLearningMethods,
  deleteLearningMethod,
  editLearningMethod,
  addLearningMethod,
} from "@/api/learningMethod";
import TypingCard from "@/components/TypingCard";
import EditLearningMethodForm from "./forms/edit-learningMethod-form";
import AddLearningMethodForm from "./forms/add-learningMethod-form";

const { Column } = Table;

const LearningMethod = () => {
  const [learningMethods, setLearningMethods] = useState([]);
  const [editLearningMethodModalVisible, setEditLearningMethodModalVisible] =
    useState(false);
  const [editLearningMethodModalLoading, setEditLearningMethodModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addLearningMethodModalVisible, setAddLearningMethodModalVisible] =
    useState(false);
  const [addLearningMethodModalLoading, setAddLearningMethodModalLoading] =
    useState(false);

  const editLearningMethodFormRef = useRef();
  const addLearningMethodFormRef = useRef();

  const fetchLearningMethods = async () => {
    try {
      const result = await getLearningMethods();
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setLearningMethods(content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditLearningMethod = (row) => {
    setCurrentRowData({ ...row });
    setEditLearningMethodModalVisible(true);
  };

  const handleDeleteLearningMethod = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteLearningMethod({ id });
      message.success("Berhasil dihapus");
      fetchLearningMethods();
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleEditLearningMethodOk = () => {
    const form = editLearningMethodFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditLearningMethodModalLoading(true);
      editLearningMethod(values, values.id)
        .then((response) => {
          form.resetFields();
          setEditLearningMethodModalVisible(false);
          setEditLearningMethodModalLoading(false);
          message.success("Berhasil diubah!");
          fetchLearningMethods();
        })
        .catch((error) => {
          setEditLearningMethodModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  const handleCancel = () => {
    setEditLearningMethodModalVisible(false);
    setAddLearningMethodModalVisible(false);
  };

  const handleAddLearningMethod = () => {
    setAddLearningMethodModalVisible(true);
  };

  const handleAddLearningMethodOk = () => {
    const form = addLearningMethodFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddLearningMethodModalLoading(true);
      addLearningMethod(values)
        .then((response) => {
          form.resetFields();
          setAddLearningMethodModalVisible(false);
          setAddLearningMethodModalLoading(false);
          message.success("Berhasil ditambahkan!");
          fetchLearningMethods();
        })
        .catch((error) => {
          setAddLearningMethodModalLoading(false);
          message.error("Gagal menambahkan: " + error.message);
        });
    });
  };

  useEffect(() => {
    fetchLearningMethods();
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddLearningMethod}>
        Tambahkan metode pembelajaran
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola metode pembelajaran di sistem, seperti menambahkan metode pembelajaran baru, atau mengubah metode pembelajaran yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Metode Pembelajaran" source={cardContent} />
      <br />
      <Card title={title}>
        <Table
          variant
          rowKey="id"
          dataSource={learningMethods}
          pagination={false}
        >
          <Column
            title="ID Metode Pembelajaran"
            dataIndex="id"
            key="id"
            align="center"
          />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Deskripsi Metode Pembelajaran"
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
                  onClick={() => handleEditLearningMethod(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteLearningMethod(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditLearningMethodForm
        currentRowData={currentRowData}
        wrappedComponentRef={editLearningMethodFormRef}
        visible={editLearningMethodModalVisible}
        confirmLoading={editLearningMethodModalLoading}
        onCancel={handleCancel}
        onOk={handleEditLearningMethodOk}
      />

      <AddLearningMethodForm
        wrappedComponentRef={addLearningMethodFormRef}
        visible={addLearningMethodModalVisible}
        confirmLoading={addLearningMethodModalLoading}
        onCancel={handleCancel}
        onOk={handleAddLearningMethodOk}
      />
    </div>
  );
};

export default LearningMethod;
