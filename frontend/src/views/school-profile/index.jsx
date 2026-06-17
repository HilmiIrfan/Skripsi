/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import {
  Card,
  Button,
  Table,
  message,
  Upload,
  Row,
  Col,
  Divider,
  Modal,
  Input,
} from "antd";
import {
  getSchoolProfiles,
  deleteSchoolProfile,
  editSchoolProfile,
  addSchoolProfile,
} from "@/api/schoolProfile";
import TypingCard from "@/components/TypingCard";
import EditSchoolProfileForm from "./forms/edit-schoolProfile-form";
import AddSchoolProfileForm from "./forms/add-schoolProfile-form";

const { Column } = Table;

const SchoolProfile = () => {
  const [schoolProfiles, setSchoolProfiles] = useState([]);
  const [modalState, setModalState] = useState({
    editVisible: false,
    editLoading: false,
    addVisible: false,
    addLoading: false,
  });
  const [currentRowData, setCurrentRowData] = useState({});

  const editFormRef = useRef();
  const addFormRef = useRef();

  const fetchSchoolProfiles = async () => {
    try {
      const result = await getSchoolProfiles();
      if (result.data.statusCode === 200) {
        setSchoolProfiles(result.data.content);
      }
    } catch (error) {
      console.error("Error fetching school profiles:", error);
    }
  };

  useEffect(() => {
    fetchSchoolProfiles();
  }, []);

  const handleEditSchoolProfile = (row) => {
    setCurrentRowData({ ...row });
    setModalState((prev) => ({ ...prev, editVisible: true }));
  };

  const handleDeleteSchoolProfile = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteSchoolProfile({ id });
      message.success("Berhasil dihapus");
      fetchSchoolProfiles();
    } catch (error) {
      message.error("Gagal menghapus");
    }
  };

  const handleEditSchoolProfileOk = () => {
    const form = editFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, editLoading: true }));
      try {
        await editSchoolProfile(values, values.id);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          editVisible: false,
          editLoading: false,
        }));
        message.success("Berhasil memperbarui!");
        fetchSchoolProfiles();
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

  const handleAddSchoolProfile = () => {
    setModalState((prev) => ({ ...prev, addVisible: true }));
  };

  const handleAddSchoolProfileOk = () => {
    const form = addFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, addLoading: true }));
      try {
        await addSchoolProfile(values);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          addVisible: false,
          addLoading: false,
        }));
        message.success("Berhasil menambahkan!");
        fetchSchoolProfiles();
      } catch (error) {
        console.error(error.response?.data);
        message.error("Gagal menambahkan, silakan coba lagi!");
        setModalState((prev) => ({ ...prev, addLoading: false }));
      }
    });
  };

  const cardContent = `Di sini, Anda dapat mengelola sekolah di sistem, seperti menambahkan sekolah baru, atau mengubah sekolah yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Sekolah" source={cardContent} />
      <br />
      <Card
        title={
          <Button type="primary" onClick={handleAddSchoolProfile}>
            Tambahkan Profil Sekolah
          </Button>
        }
        style={{ overflowX: "scroll" }}
      >
        <Table
          variant
          rowKey="id"
          dataSource={schoolProfiles}
          pagination={false}
        >
          <Column title="NPSN" dataIndex="npsn" key="npsn" align="center" />
          <Column
            title="Sekolah"
            dataIndex="school.name"
            key="school.name"
            align="center"
          />
          <Column
            title="Status"
            dataIndex="status"
            key="status"
            align="center"
          />
          <Column
            title="Bentuk Kependidikan"
            dataIndex="bentukKependidikan"
            key="bentukKependidikan"
            align="center"
          />
          <Column
            title="Status Kepemilikan"
            dataIndex="kepemilikan"
            key="kepemilikan"
            align="center"
          />
          <Column
            title="SK Pendirian Sekolah"
            dataIndex="skpendirianSekolah"
            key="skpendirianSekolah"
            align="center"
          />
          <Column
            title="Tanggal SK Pendirian"
            dataIndex="tglSKPendirian"
            key="tglSKPendirian"
            align="center"
          />
          <Column
            title="SK Izin Operasional"
            dataIndex="skizinOperasional"
            key="skizinOperasional"
            align="center"
          />
          <Column
            title="Tanggal SK Izin Operasional"
            dataIndex="tglSKOperasional"
            key="tglSKOperasional"
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
                  onClick={() => handleEditSchoolProfile(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteSchoolProfile(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditSchoolProfileForm
        wrappedComponentRef={editFormRef}
        currentRowData={currentRowData}
        visible={modalState.editVisible}
        confirmLoading={modalState.editLoading}
        onCancel={handleCancel}
        onOk={handleEditSchoolProfileOk}
      />

      <AddSchoolProfileForm
        wrappedComponentRef={addFormRef}
        visible={modalState.addVisible}
        confirmLoading={modalState.addLoading}
        onCancel={handleCancel}
        onOk={handleAddSchoolProfileOk}
      />
    </div>
  );
};

export default SchoolProfile;
