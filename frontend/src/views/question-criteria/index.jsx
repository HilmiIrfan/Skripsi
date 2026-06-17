/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getQuestionCriterias,
  deleteQuestionCriteria,
  editQuestionCriteria,
  addQuestionCriteria,
} from "@/api/questionCriteria";
import TypingCard from "@/components/TypingCard";
import EditQuestionCriteriaForm from "./forms/edit-question-criteria-form";
import AddQuestionCriteriaForm from "./forms/add-question-criteria-form";

const { Column } = Table;

const QuestionCriteria = () => {
  const [questionCriterias, setQuestionCriterias] = useState([]);
  const [
    editQuestionCriteriaModalVisible,
    setEditQuestionCriteriaModalVisible,
  ] = useState(false);
  const [
    editQuestionCriteriaModalLoading,
    setEditQuestionCriteriaModalLoading,
  ] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addQuestionCriteriaModalVisible, setAddQuestionCriteriaModalVisible] =
    useState(false);
  const [addQuestionCriteriaModalLoading, setAddQuestionCriteriaModalLoading] =
    useState(false);

  const editQuestionCriteriaFormRef = useRef();
  const addQuestionCriteriaFormRef = useRef();

  const fetchQuestionCriterias = async () => {
    try {
      const result = await getQuestionCriterias();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setQuestionCriterias(content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditQuestionCriteria = (row) => {
    setCurrentRowData({ ...row });
    setEditQuestionCriteriaModalVisible(true);
  };

  const handleDeleteQuestionCriteria = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteQuestionCriteria({ id });
      message.success("Berhasil dihapus");
      fetchQuestionCriterias();
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleCancel = () => {
    setAddQuestionCriteriaModalVisible(false);
    setEditQuestionCriteriaModalVisible(false);
  };

  const handleAddQuestionCriteria = () => {
    setAddQuestionCriteriaModalVisible(true);
  };

  const handleAddQuestionCriteriaOk = () => {
    const form = addQuestionCriteriaFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddQuestionCriteriaModalLoading(true);
      addQuestionCriteria(values)
        .then(() => {
          message.success("Berhasil ditambahkan!");
          setAddQuestionCriteriaModalLoading(false);
          setAddQuestionCriteriaModalVisible(false);
          fetchQuestionCriterias();
        })
        .catch((error) => {
          setAddQuestionCriteriaModalLoading(false);
          message.error("Gagal menambahkan: " + error.message);
        });
    });
  };

  const handleEditQuestionCriteriaOk = () => {
    const form = editQuestionCriteriaFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditQuestionCriteriaModalLoading(true);
      editQuestionCriteria(values, currentRowData.id)
        .then(() => {
          message.success("Berhasil diubah!");
          setEditQuestionCriteriaModalLoading(false);
          setEditQuestionCriteriaModalVisible(false);
          fetchQuestionCriterias();
        })
        .catch((error) => {
          setEditQuestionCriteriaModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  useEffect(() => {
    fetchQuestionCriterias();
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddQuestionCriteria}>
        Tambah Kriteria Pertanyaan
      </Button>
    </span>
  );

  return (
    <div className="app-container">
      <TypingCard title="Kriteria Pertanyaan" source="Kriteria Pertanyaan" />
      <br />
      <Card title={title}>
        <Table
          variant
          rowKey="id"
          dataSource={questionCriterias}
          pagination={false}
        >
          <Column
            title="ID"
            key="id"
            align="center"
            render={(value, record, index) => index + 1}
          />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Deskripsi Kriteria Pertanyaan"
            dataIndex="description"
            key="description"
            align="center"
          />
          <Column
            title="Kategori"
            dataIndex="category"
            key="category"
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
                  onClick={() => handleEditQuestionCriteria(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteQuestionCriteria(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditQuestionCriteriaForm
        wrappedComponentRef={editQuestionCriteriaFormRef}
        visible={editQuestionCriteriaModalVisible}
        confirmLoading={editQuestionCriteriaModalLoading}
        onCancel={handleCancel}
        onOk={handleEditQuestionCriteriaOk}
        currentRowData={currentRowData}
      />

      <AddQuestionCriteriaForm
        wrappedComponentRef={addQuestionCriteriaFormRef}
        visible={addQuestionCriteriaModalVisible}
        confirmLoading={addQuestionCriteriaModalLoading}
        onCancel={handleCancel}
        onOk={handleAddQuestionCriteriaOk}
      />
    </div>
  );
};

export default QuestionCriteria;
