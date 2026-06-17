/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getExercise,
  deleteExercise,
  editExercise,
  addExercise,
  getQuestionsByRPS,
} from "@/api/exercise";
import { getQuestions } from "@/api/question";
import { getRPS } from "@/api/rps";
import { getRPSDetail } from "@/api/rpsDetail";
import { Link } from "react-router-dom";
import TypingCard from "@/components/TypingCard";
import EditExerciseForm from "./forms/edit-exercise-form";
import AddExerciseForm from "./forms/add-exercise-form";
import moment from "moment";

const { Column } = Table;

const Exercise = () => {
  const [exercise, setExercise] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [rps, setRps] = useState([]);
  const [rpsDetail, setRpsDetail] = useState([]);
  const [editExerciseModalVisible, setEditExerciseModalVisible] =
    useState(false);
  const [editExerciseModalLoading, setEditExerciseModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addExerciseModalVisible, setAddExerciseModalVisible] = useState(false);
  const [addExerciseModalLoading, setAddExerciseModalLoading] = useState(false);
  const [rpsId, setRpsId] = useState(null);
  const [typeExercise, setTypeExercise] = useState(null);

  const editExerciseFormRef = useRef();
  const addExerciseFormRef = useRef();

  const handleRPSChange = (value) => {
    setRpsId(value);
    updateQuestion(value, typeExercise);
  };

  const handleExerciseTypeChange = (value) => {
    setTypeExercise(value);
    updateQuestion(rpsId, value);
  };

  const fetchExercise = async () => {
    try {
      const result = await getExercise();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setExercise(content);
      }
    } catch (error) {
      message.error("Gagal mengambil data latihan");
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

  const updateQuestion = async (rpsId, type) => {
    if (rpsId && type) {
      try {
        const result = await getQuestionsByRPS(rpsId, type);
        const { content, statusCode } = result.data;
        if (statusCode === 200) {
          const filteredQuestions = content.filter(
            (question) => question.examType === "EXERCISE"
          );
          setQuestions(filteredQuestions);
        }
      } catch (error) {
        message.error("Gagal memperbarui pertanyaan");
      }
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

  const handleEditExercise = (row) => {
    setCurrentRowData({ ...row });
    setEditExerciseModalVisible(true);
  };

  const handleDeleteExercise = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteExercise({ id });
      message.success("Berhasil dihapus");
      fetchExercise();
    } catch (error) {
      message.error("Gagal menghapus data");
    }
  };

  const handleEditExerciseOk = () => {
    const form = editExerciseFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditExerciseModalLoading(true);
      editExercise(values, values.id)
        .then((response) => {
          form.resetFields();
          setEditExerciseModalVisible(false);
          setEditExerciseModalLoading(false);
          message.success("Berhasil diubah!");
          fetchExercise();
        })
        .catch((error) => {
          setEditExerciseModalLoading(false);
          message.error("Gagal mengubah data");
        });
    });
  };

  const handleCancel = () => {
    setEditExerciseModalVisible(false);
    setAddExerciseModalVisible(false);
  };

  const handleAddExercise = () => {
    setAddExerciseModalVisible(true);
  };

  const handleAddExerciseOk = () => {
    const form = addExerciseFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddExerciseModalLoading(true);
      addExercise(values)
        .then((response) => {
          form.resetFields();
          setAddExerciseModalVisible(false);
          setAddExerciseModalLoading(false);
          message.success("Berhasil ditambahkan!");
          fetchExercise();
        })
        .catch((error) => {
          setAddExerciseModalLoading(false);
          message.error("Gagal menambahkan data");
        });
    });
  };

  useEffect(() => {
    fetchExercise();
    fetchQuestions();
    fetchRPSDetail();
    fetchRps();
  }, []);

  useEffect(() => {
    if (rpsId && typeExercise) {
      updateQuestion(rpsId, typeExercise);
    }
  }, [rpsId, typeExercise]);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddExercise}>
        Tambahkan Latihan
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola Exercise sesuai dengan mata kuliah yang diampu. Di bawah ini dapat menampilkan list Exercise yang ada.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Latihan" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={exercise} pagination={false}>
          <Column title="ID Exercise" dataIndex="id" key="id" align="center" />
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
                  title="Edit Latihan"
                  onClick={() => handleEditExercise(row)}
                />
                <Divider type="vertical" />
                <Link to={`/setting-exercise/result/${row.id}`}>
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
                  onClick={() => handleDeleteExercise(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditExerciseForm
        currentRowData={currentRowData}
        wrappedComponentRef={editExerciseFormRef}
        visible={editExerciseModalVisible}
        confirmLoading={editExerciseModalLoading}
        onCancel={handleCancel}
        onOk={handleEditExerciseOk}
        handleUpdateQuestion={updateQuestion}
        questions={questions}
        rpsAll={rps}
      />

      <AddExerciseForm
        wrappedComponentRef={addExerciseFormRef}
        visible={addExerciseModalVisible}
        handleRPSChange={handleRPSChange}
        handleExerciseTypeChange={handleExerciseTypeChange}
        confirmLoading={addExerciseModalLoading}
        onCancel={handleCancel}
        onOk={handleAddExerciseOk}
        handleUpdateQuestion={updateQuestion}
        questions={questions}
        rps={rps}
      />
    </div>
  );
};

export default Exercise;
