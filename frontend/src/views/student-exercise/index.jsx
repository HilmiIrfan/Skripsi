/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Table, Modal } from "antd";
import { getExercise } from "@/api/exercise";
import { getQuestions, getQuestionsByRPS } from "@/api/question";
import { getRPS } from "@/api/rps";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import TypingCard from "@/components/TypingCard";
import { getAttemptExerciseByUserID } from "../../api/attemptExercise";
import { getUserInfo } from "../../store/actions/user";
import moment from "moment";

const { Column } = Table;

const StudentExercise = () => {
  const [exercise, setExercise] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [rps, setRps] = useState([]);

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const idUser = useSelector((state) => state.user.idUser);

  const getExerciseData = async () => {
    const result = await getExercise();
    const { content, statusCode } = result.data;

    if (statusCode === 200) {
      const attemptExerciseResult = await getAttemptExerciseByUserID(idUser);
      const {
        content: attemptExerciseContent,
        statusCode: attemptExerciseStatusCode,
      } = attemptExerciseResult.data;

      if (attemptExerciseStatusCode === 200) {
        const exerciseWithStatus = content.map((exercise) => {
          const attemptExercise = attemptExerciseContent.find(
            (attempt) => attempt.exercise && attempt.exercise.id === exercise.id
          );

          if (attemptExercise) {
            return {
              ...exercise,
              status: "sudah",
              grade: attemptExercise.grade,
              total_right: attemptExercise.total_right,
              state: attemptExercise.state,
              total_wrong:
                attemptExercise.student_answers.length -
                attemptExercise.total_right,
              attemptExerciseId: attemptExercise.id,
            };
          }

          return {
            ...exercise,
            status: "Belum dikerjakan",
          };
        });

        setExercise(exerciseWithStatus);
      }
    }
  };

  const getQuestionsData = async () => {
    const result = await getQuestions();
    const { content, statusCode } = result.data;

    if (statusCode === 200) {
      setQuestions(content);
    }
  };

  const updateQuestion = async (id) => {
    const result = await getQuestionsByRPS(id);
    const { content, statusCode } = result.data;

    if (statusCode === 200) {
      setQuestions(content);
    }
  };

  const getRpsData = async () => {
    const result = await getRPS();
    const { content, statusCode } = result.data;

    if (statusCode === 200) {
      setRps(content);
    }
  };

  const handleDoExercise = (row) => {
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin memulai ujian?",
      okText: "OK",
      cancelText: "Batal",
      onOk: () => {
        navigate.push(`/exercise/do/${row.id}`);
      },
    });
  };

  const handleDoExerciseReview = (row) => {
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin melihat review ujian anda?",
      okText: "OK",
      cancelText: "Batal",
      onOk: () => {
        navigate.push(`/exercise-review/${row.attemptExerciseId}`);
      },
    });
  };

  useEffect(() => {
    getExerciseData();
    getQuestionsData();
    getRpsData();
  }, []);

  const title = <span></span>;
  const cardContent = `Di sini, Anda dapat mengelola Exercise sesuai dengan mata kuliah yang diampu. Di bawah ini dapat menampilkan list Exercise yang ada.`;

  return (
    <div className="app-container">
      <TypingCard title="Ujian" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={exercise} pagination={false}>
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
            render={(text) => `${text} minute`}
          />
          <Column
            title="Nilai"
            dataIndex="grade"
            key="grade"
            align="center"
            render={(grade, row) => (
              <>{row.status === "sudah" ? <span>{grade}</span> : null}</>
            )}
          />
          <Column
            title="Jumlah benar"
            dataIndex="total_right"
            key="total_right"
            align="center"
          />
          <Column
            title="Jumlah salah"
            dataIndex="total_wrong"
            key="total_wrong"
            align="center"
          />
          <Column
            title="Status"
            dataIndex="state"
            key="state"
            align="center"
            render={(state, row) => (
              <>{row.status === "sudah" ? <span>{state}</span> : null}</>
            )}
          />
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(text, row) => (
              <span>
                {row.status === "sudah" ? (
                  <>
                    <Button
                      type="primary"
                      shape="circle"
                      icon="container"
                      title="Ujian Sudah Dikerjakan"
                      disabled
                    />
                    <Button
                      type="primary"
                      shape="circle"
                      icon="container"
                      title="Review Sekarang"
                      onClick={() => handleDoExerciseReview(row)}
                    />
                  </>
                ) : (
                  <Button
                    type="primary"
                    shape="circle"
                    icon="container"
                    title="Ujian Sekarang"
                    onClick={() => handleDoExercise(row)}
                  />
                )}
              </span>
            )}
          />
        </Table>
      </Card>
    </div>
  );
};

export default StudentExercise;
