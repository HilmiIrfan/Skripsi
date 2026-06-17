/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import { getExam, deleteExam, editExam, addExam } from "@/api/exam";
import { getQuestions, getQuestionsByRPS } from "@/api/question";
import { getRPS } from "@/api/rps";
import { getRPSDetail } from "@/api/rpsDetail";
import { Link } from "react-router-dom";
import TypingCard from "@/components/TypingCard";
import EditExamForm from "./forms/edit-exam-form";
import AddExamForm from "./forms/add-exam-form";
import moment from "moment";

const { Column } = Table;

const Exam = () => {
  const [exam, setExam] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [rps, setRps] = useState([]);
  const [rpsDetail, setRpsDetail] = useState([]);
  const [editExamModalVisible, setEditExamModalVisible] = useState(false);
  const [editExamModalLoading, setEditExamModalLoading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addExamModalVisible, setAddExamModalVisible] = useState(false);
  const [addExamModalLoading, setAddExamModalLoading] = useState(false);
  const [rpsId, setRpsId] = useState([]);

  const editExamFormRef = useRef();
  const addExamFormRef = useRef();

  const fetchExam = async () => {
    try {
      const result = await getExam();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setExam(content);
      }
    } catch (error) {
      message.error("Gagal mengambil data ujian");
    }
  };

  const fetchQuestions = async () => {
    try {
      const result = await getQuestions();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setQuestions(content);
      }
    } catch (error) {
      message.error("Gagal mengambil data pertanyaan");
    }
  };

  const fetchRPSDetail = async () => {
    try {
      const result = await getRPSDetail(rpsId);
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setRpsDetail(content);
      }
    } catch (error) {
      message.error("Gagal mengambil detail RPS");
    }
  };

  const updateQuestion = async (id) => {
    try {
      const result = await getQuestionsByRPS(id);
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const filteredQuestions = content.filter(
          (question) => question.examType3 === "EXAM"
        );
        setQuestions(filteredQuestions);
      }
    } catch (error) {
      message.error("Gagal memperbarui pertanyaan");
    }
  };

  const fetchRps = async () => {
    try {
      const result = await getRPS();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setRps(content);
        setRpsId(content.id);
      }
    } catch (error) {
      message.error("Gagal mengambil data RPS");
    }
  };

  const handleEditExam = (row) => {
    setCurrentRowData({ ...row });
    setEditExamModalVisible(true);
  };

  const handleDeleteExam = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteExam({ id });
      message.success("Berhasil dihapus");
      fetchExam();
    } catch (error) {
      message.error("Gagal menghapus data");
    }
  };

  const handleEditExamOk = () => {
    const form = editExamFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditExamModalLoading(true);
      editExam(values, values.id)
        .then((response) => {
          form.resetFields();
          setEditExamModalVisible(false);
          setEditExamModalLoading(false);
          message.success("Berhasil diubah!");
          fetchExam();
        })
        .catch((error) => {
          setEditExamModalLoading(false);
          message.error("Gagal mengubah data");
        });
    });
  };

  const handleCancel = () => {
    setEditExamModalVisible(false);
    setAddExamModalVisible(false);
  };

  const handleAddExam = () => {
    setAddExamModalVisible(true);
  };

  const handleAddExamOk = () => {
    const form = addExamFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddExamModalLoading(true);
      addExam(values)
        .then((response) => {
          form.resetFields();
          setAddExamModalVisible(false);
          setAddExamModalLoading(false);
          message.success("Berhasil ditambahkan!");
          fetchExam();
        })
        .catch((error) => {
          setAddExamModalLoading(false);
          message.error("Gagal menambahkan data");
        });
    });
  };

  useEffect(() => {
    fetchExam();
    fetchQuestions();
    fetchRps();
    fetchRPSDetail();
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddExam}>
        Tambahkan Ujian
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola Exam sesuai dengan mata kuliah yang diampu. Di bawah ini dapat menampilkan list Exam yang ada.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Ujian" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={exam} pagination={false}>
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="RPS"
            dataIndex="rps.name"
            key="rps.name"
            align="center"
          />
          <Column
            title="Nilai Minimal"
            dataIndex="min_grade"
            key="min_grade"
            align="center"
          />
          <Column
            title="Pilihan ujian"
            dataIndex="type_exercise"
            key="type_exercise"
            align="center"
            render={(text) => {
              if (text === "1-8") return "UTS";
              if (text === "1-18") return "UAS";
              return text;
            }}
          />
          <Column
            title="Tanggal Mulai"
            dataIndex="date_start"
            key="date_start"
            align="center"
            render={(text) => moment(text).format("DD MMMM YYYY, HH:mm:ss")}
          />
          <Column
            title="Tanggal Selesai"
            dataIndex="date_end"
            key="date_end"
            align="center"
            render={(text) => moment(text).format("DD MMMM YYYY, HH:mm:ss")}
          />
          <Column
            title="Durasi"
            dataIndex="duration"
            key="duration"
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
                  title="Edit Ujian"
                  onClick={() => handleEditExam(row)}
                />
                <Divider type="vertical" />
                <Link to={`/setting-exam/result/${row.id}`}>
                  <Button
                    type="primary"
                    shape="circle"
                    icon="diff"
                    title="Detail Hasil"
                  />
                </Link>
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="Hapus Data"
                  onClick={() => handleDeleteExam(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditExamForm
        currentRowData={currentRowData}
        wrappedComponentRef={editExamFormRef}
        visible={editExamModalVisible}
        confirmLoading={editExamModalLoading}
        onCancel={handleCancel}
        onOk={handleEditExamOk}
        handleUpdateQuestion={updateQuestion}
        questions={questions}
        rpsAll={rps}
      />

      <AddExamForm
        wrappedComponentRef={addExamFormRef}
        visible={addExamModalVisible}
        confirmLoading={addExamModalLoading}
        onCancel={handleCancel}
        onOk={handleAddExamOk}
        handleGetRPSDetail={fetchRPSDetail}
        handleUpdateQuestion={updateQuestion}
        rpsDetail={rpsDetail}
        questions={questions}
        rps={rps}
      />
    </div>
  );
};

export default Exam;
