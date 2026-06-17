/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getAppraisalForms,
  deleteAppraisalForm,
  editAppraisalForm,
  addAppraisalForm,
} from "@/api/appraisalForm";
import TypingCard from "@/components/TypingCard";
import EditAppraisalForm from "./forms/edit-appraisal-form";
import AddAppraisalForm from "./forms/add-appraisal-form";

const { Column } = Table;

const AppraisalForm = () => {
  const [appraisalForms, setAppraisalForms] = useState([]);
  const [editAppraisalFormModalVisible, setEditAppraisalFormModalVisible] =
    useState(false);
  const [editAppraisalFormModalLoading, setEditAppraisalFormModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addAppraisalFormModalVisible, setAddAppraisalFormModalVisible] =
    useState(false);
  const [addAppraisalFormModalLoading, setAddAppraisalFormModalLoading] =
    useState(false);

  const editAppraisalFormRef = useRef();
  const addAppraisalFormRef = useRef();

  const fetchAppraisalForms = async () => {
    try {
      const result = await getAppraisalForms();
      const { content, statusCode } = result.data;

      if (statusCode === 200) {
        setAppraisalForms(content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditAppraisalForm = (row) => {
    setCurrentRowData({ ...row });
    setEditAppraisalFormModalVisible(true);
  };

  const handleDeleteAppraisalForm = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteAppraisalForm({ id });
      message.success("Berhasil dihapus");
      fetchAppraisalForms();
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleEditAppraisalFormOk = () => {
    const form = editAppraisalFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditAppraisalFormModalLoading(true);
      editAppraisalForm(values, values.id)
        .then((response) => {
          if (response.data.statusCode === 200) {
            form.resetFields();
            setEditAppraisalFormModalVisible(false);
            setEditAppraisalFormModalLoading(false);
            message.success("Berhasil diubah!");
            fetchAppraisalForms();
          } else {
            throw new Error(response.data.message);
          }
        })
        .catch((error) => {
          setEditAppraisalFormModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  const handleCancel = () => {
    setEditAppraisalFormModalVisible(false);
    setAddAppraisalFormModalVisible(false);
  };

  const handleAddAppraisalForm = () => {
    setAddAppraisalFormModalVisible(true);
  };

  const handleAddAppraisalFormOk = () => {
    const form = addAppraisalFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddAppraisalFormModalLoading(true);
      addAppraisalForm(values)
        .then((response) => {
          if (response.data.statusCode === 200) {
            form.resetFields();
            setAddAppraisalFormModalVisible(false);
            setAddAppraisalFormModalLoading(false);
            message.success("Berhasil ditambahkan!");
            fetchAppraisalForms();
          } else {
            throw new Error(response.data.message);
          }
        })
        .catch((error) => {
          setAddAppraisalFormModalLoading(false);
          message.error("Gagal menambahkan: " + error.message);
        });
    });
  };

  useEffect(() => {
    fetchAppraisalForms();
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddAppraisalForm}>
        Tambahkan formulir penilaian
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola formulir penilaian di sistem, seperti menambahkan formulir penilaian baru, atau mengubah formulir penilaian yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Formulir Penilaian" source={cardContent} />
      <br />
      <Card title={title}>
        <Table
          variant
          rowKey="id"
          dataSource={appraisalForms}
          pagination={false}
        >
          <Column
            title="ID Formulir Penilaian"
            dataIndex="id"
            key="id"
            align="center"
          />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Deskripsi Formulir Penilaian"
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
                  onClick={() => handleEditAppraisalForm(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteAppraisalForm(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditAppraisalForm
        currentRowData={currentRowData}
        wrappedComponentRef={editAppraisalFormRef}
        visible={editAppraisalFormModalVisible}
        confirmLoading={editAppraisalFormModalLoading}
        onCancel={handleCancel}
        onOk={handleEditAppraisalFormOk}
      />

      <AddAppraisalForm
        wrappedComponentRef={addAppraisalFormRef}
        visible={addAppraisalFormModalVisible}
        confirmLoading={addAppraisalFormModalLoading}
        onCancel={handleCancel}
        onOk={handleAddAppraisalFormOk}
      />
    </div>
  );
};

export default AppraisalForm;
