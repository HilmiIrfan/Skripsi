/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import { getQuiz, deleteQuiz, editQuiz, addQuiz } from "@/api/quiz";
import { getQuestions, getQuestionsByRPS } from "@/api/question";
import { getRPS } from "@/api/rps";
import { Link } from "react-router-dom";
import TypingCard from "@/components/TypingCard";
import EditQuizForm from "./forms/edit-quiz-form";
import AddQuizForm from "./forms/add-quiz-form";
import moment from "moment";

const { Column } = Table;

const Quiz = () => {
  const [quiz, setQuiz] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [rps, setRps] = useState([]);
  const [editQuizModalVisible, setEditQuizModalVisible] = useState(false);
  const [editQuizModalLoading, setEditQuizModalLoading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addQuizModalVisible, setAddQuizModalVisible] = useState(false);
  const [addQuizModalLoading, setAddQuizModalLoading] = useState(false);

  const editQuizFormRef = useRef();
  const addQuizFormRef = useRef();

  const fetchQuiz = async () => {
    try {
      const result = await getQuiz();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setQuiz(content);
      }
    } catch (error) {
      message.error("Gagal mengambil data kuis");
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

  const updateQuestion = async (id) => {
    try {
      const result = await getQuestionsByRPS(id);
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        const filteredQuestions = content.filter(
          (question) => question.examType2 === "QUIZ"
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
      }
    } catch (error) {
      message.error("Gagal mengambil data RPS");
    }
  };

  const handleEditQuiz = (row) => {
    setCurrentRowData({ ...row });
    setEditQuizModalVisible(true);
  };

  const handleDeleteQuiz = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteQuiz({ id });
      message.success("Berhasil dihapus");
      fetchQuiz();
    } catch (error) {
      message.error("Gagal menghapus kuis");
    }
  };

  const handleEditQuizOk = () => {
    const form = editQuizFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditQuizModalLoading(true);
      editQuiz(values, values.id)
        .then(() => {
          form.resetFields();
          setEditQuizModalVisible(false);
          setEditQuizModalLoading(false);
          message.success("Berhasil diubah!");
          fetchQuiz();
        })
        .catch((error) => {
          setEditQuizModalLoading(false);
          message.error("Gagal mengubah kuis");
        });
    });
  };

  const handleCancel = () => {
    setEditQuizModalVisible(false);
    setAddQuizModalVisible(false);
  };

  const handleAddQuiz = () => {
    setAddQuizModalVisible(true);
  };

  const handleAddQuizOk = () => {
    const form = addQuizFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddQuizModalLoading(true);
      addQuiz(values)
        .then(() => {
          form.resetFields();
          setAddQuizModalVisible(false);
          setAddQuizModalLoading(false);
          message.success("Berhasil ditambahkan!");
          fetchQuiz();
        })
        .catch((error) => {
          setAddQuizModalLoading(false);
          message.error("Gagal menambahkan kuis");
        });
    });
  };

  useEffect(() => {
    fetchQuiz();
    fetchQuestions();
    fetchRps();
  }, []);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddQuiz}>
        Tambahkan Kuis
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola Quiz sesuai dengan mata kuliah yang diampu. Di bawah ini dapat menampilkan list Quiz yang ada.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Kuis" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={quiz} pagination={false}>
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
            title="Tipe Kuis"
            dataIndex="type_quiz"
            key="type_quiz"
            align="center"
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
                  title="Edit Kuis"
                  onClick={() => handleEditQuiz(row)}
                />
                <Divider type="vertical" />
                <Link to={`/setting-quiz/result/${row.id}`}>
                  <Button
                    type="primary"
                    shape="circle"
                    icon="diff"
                    title="Detail Hasil"
                  />
                </Link>
                <Divider type="vertical" />
                <Link to={`/setting-quiz/generate-quiz/${row.id}`}>
                  <Button
                    type="primary"
                    shape="circle"
                    icon="diff"
                    title="Detail Generate Quiz"
                  />
                </Link>
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="Hapus Data"
                  onClick={() => handleDeleteQuiz(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditQuizForm
        currentRowData={currentRowData}
        wrappedComponentRef={editQuizFormRef}
        visible={editQuizModalVisible}
        confirmLoading={editQuizModalLoading}
        onCancel={handleCancel}
        onOk={handleEditQuizOk}
        handleUpdateQuestion={updateQuestion}
        questions={questions}
        rpsAll={rps}
      />

      <AddQuizForm
        wrappedComponentRef={addQuizFormRef}
        visible={addQuizModalVisible}
        confirmLoading={addQuizModalLoading}
        onCancel={handleCancel}
        onOk={handleAddQuizOk}
        handleUpdateQuestion={updateQuestion}
        questions={questions}
        rps={rps}
      />
    </div>
  );
};

export default Quiz;
