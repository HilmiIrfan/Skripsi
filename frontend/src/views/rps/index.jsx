/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import { getRPS, deleteRPS, editRPS, addRPS, importRPS } from "@/api/rps";
import { importRPSDetail } from "@/api/rpsDetail";
import { getSubjects } from "@/api/subject";
import { getStudyPrograms } from "@/api/studyProgram";
import { getLectures } from "@/api/lecture";
import { Link } from "react-router-dom";
import {
  getLearningMediasSoftware,
  getLearningMediasHardware,
} from "@/api/learningMedia";
import * as XLSX from "xlsx";
import TypingCard from "@/components/TypingCard";
import EditRPSForm from "./forms/edit-rps-form";
import AddRPSForm from "./forms/add-rps-form";

const { Column } = Table;

const RPS = () => {
  const [rps, setRps] = useState([]);
  const [learningMediaSoftwares, setLearningMediaSoftwares] = useState([]);
  const [learningMediaHardwares, setLearningMediaHardwares] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [studyPrograms, setStudyPrograms] = useState([]);
  const [lectures, setLectures] = useState([]);
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
      const rpsResult = await getRPS();
      if (rpsResult.data.statusCode === 200) {
        setRps(rpsResult.data.content);
      }

      const softwareResult = await getLearningMediasSoftware();
      if (softwareResult.data.statusCode === 200) {
        setLearningMediaSoftwares(softwareResult.data.content);
      }

      const hardwareResult = await getLearningMediasHardware();
      if (hardwareResult.data.statusCode === 200) {
        setLearningMediaHardwares(hardwareResult.data.content);
      }

      const subjectsResult = await getSubjects();
      if (subjectsResult.data.statusCode === 200) {
        setSubjects(subjectsResult.data.content);
      }

      const studyProgramResult = await getStudyPrograms();
      if (studyProgramResult.data.statusCode === 200) {
        setStudyPrograms(studyProgramResult.data.content);
      }

      const lecturesResult = await getLectures();
      if (lecturesResult.data.statusCode === 200) {
        setLectures(lecturesResult.data.content);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleEditRPS = (row) => {
    setCurrentRowData({ ...row });
    setModalState((prev) => ({ ...prev, editVisible: true }));
  };

  const handleDeleteRPS = async (row) => {
    if (row.id === "admin") {
      message.error("Berhasil Dibuat");
      return;
    }

    try {
      await deleteRPS({ id: row.id });
      message.success("berhasil dihapus");
      fetchData();
    } catch (error) {
      message.error("Gagal menghapus");
    }
  };

  const handleEditRPSOk = () => {
    const form = editFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, editLoading: true }));
      try {
        await editRPS(values);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          editVisible: false,
          editLoading: false,
        }));
        message.success("Berhasil!");
        fetchData();
      } catch (error) {
        message.error("Gagal mengubah");
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

  const handleAddRPS = () => {
    setModalState((prev) => ({ ...prev, addVisible: true }));
  };

  const handleAddRPSOk = () => {
    const form = addFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, addLoading: true }));
      try {
        await addRPS(values);
        form.resetFields();
        setModalState((prev) => ({
          ...prev,
          addVisible: false,
          addLoading: false,
        }));
        message.success("Berhasil!");
        fetchData();
      } catch (error) {
        message.error("Gagal menambahkan, coba lagi!");
        setModalState((prev) => ({ ...prev, addLoading: false }));
      }
    });
  };

  const handleFileUpload = async (event) => {
    const file = event.target.files[0];

    const reader = new FileReader();
    reader.onload = async (e) => {
      try {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: "array" });
        const firstSheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[firstSheetName];
        const jsonData = XLSX.utils.sheet_to_json(worksheet);
        setRps(jsonData);

        const [responseRPS, responseRPSDetail] = await Promise.all([
          importRPS(file),
          importRPSDetail(file),
        ]);

        if (responseRPS.status === 200 && responseRPSDetail.status === 200) {
          window.location.reload();
        }
      } catch (error) {
        console.error("Error during file upload:", error);
        message.error("Gagal mengupload file");
      }
    };

    reader.readAsArrayBuffer(file);
  };

  const cardContent = `Di sini, Anda dapat mengelola RPS sesuai dengan mata kuliah yang diampu. Di bawah ini dapat menampilkan list RPS yang ada.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen RPS" source={cardContent} />
      <div className="file-upload-container">
        <label htmlFor="fileUpload" className="file-upload-label">
          Upload File RPS:
        </label>
        <input
          type="file"
          id="fileUpload"
          className="file-upload-input"
          onChange={handleFileUpload}
        />
      </div>
      <br />
      <Card
        title={
          <Button type="primary" onClick={handleAddRPS}>
            Tambahkan RPS
          </Button>
        }
      >
        <Table variant rowKey="id" dataSource={rps} pagination={false}>
          <Column title="ID RPS" dataIndex="id" key="id" align="center" />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Semester"
            dataIndex="semester"
            key="semester"
            align="center"
          />
          <Column title="SKS" dataIndex="sks" key="sks" align="center" />
          <Column
            title="Mata Kuliah"
            dataIndex="subject.name"
            key="subject.name"
            align="center"
          />
          <Column
            title="Dosen Pengembang"
            dataIndex="dev_lecturers"
            key="dev_lecturers"
            align="center"
            render={(dev_lecturers) =>
              dev_lecturers?.map((l) => l.name).join(", ") || ""
            }
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
                  onClick={() => handleEditRPS(row)}
                />
                <Divider type="vertical" />
                <Link to={`/rps/${row.id}`}>
                  <Button
                    type="primary"
                    shape="circle"
                    icon="diff"
                    title="menghapus"
                  />
                </Link>
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteRPS(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditRPSForm
        wrappedComponentRef={editFormRef}
        currentRowData={currentRowData}
        visible={modalState.editVisible}
        confirmLoading={modalState.editLoading}
        onCancel={handleCancel}
        onOk={handleEditRPSOk}
        learningMediaSoftwares={learningMediaSoftwares}
        learningMediaHardwares={learningMediaHardwares}
        studyPrograms={studyPrograms}
        subjects={subjects}
        lectures={lectures}
      />

      <AddRPSForm
        wrappedComponentRef={addFormRef}
        visible={modalState.addVisible}
        confirmLoading={modalState.addLoading}
        onCancel={handleCancel}
        onOk={handleAddRPSOk}
        learningMediaSoftwares={learningMediaSoftwares}
        learningMediaHardwares={learningMediaHardwares}
        studyPrograms={studyPrograms}
        subjects={subjects}
        lectures={lectures}
      />
    </div>
  );
};

export default RPS;
