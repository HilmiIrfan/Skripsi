/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Table, Modal } from "antd";
import { getExam } from "@/api/exam";
import { getQuestions, getQuestionsByRPS } from "@/api/question";
import { getRPS } from "@/api/rps";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import TypingCard from "@/components/TypingCard";
import { getAttemptExamByUserID } from "../../api/attemptExam";
import { getUserInfo } from "../../store/actions/user";
import moment from "moment";

const { Column } = Table;

const StudentExam = () => {
  const [exam, setExam] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [rps, setRps] = useState([]);

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const idUser = useSelector((state) => state.user.idUser);

  const getExamData = async () => {
    const result = await getExam();
    const { content, statusCode } = result.data;

    if (statusCode === 200) {
      const attemptExamResult = await getAttemptExamByUserID(idUser);
      const { content: attemptExamContent, statusCode: attemptExamStatusCode } =
        attemptExamResult.data;

      if (attemptExamStatusCode === 200) {
        const examWithStatus = content.map((exam) => {
          const attemptExam = attemptExamContent.find(
            (attempt) => attempt.exam.id === exam.id
          );

          if (attemptExam) {
            return {
              ...exam,
              status: "sudah",
              grade: attemptExam.grade,
              state: attemptExam.state,
            };
          }

          return {
            ...exam,
            status: "Belum dikerjakan",
          };
        });

        setExam(examWithStatus);
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

  const handleDoExam = (row) => {
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin memulai ujian?",
      okText: "OK",
      cancelText: "Batal",
      onOk: () => {
        navigate.push(`/exam/do/${row.id}`);
      },
    });
  };

  useEffect(() => {
    getExamData();
    getQuestionsData();
    getRpsData();
  }, []);

  const title = <span></span>;
  const cardContent = `Di sini, Anda dapat mengelola Exam sesuai dengan mata kuliah yang diampu. Di bawah ini dapat menampilkan list Exam yang ada.`;

  return (
    <div className="app-container">
      <TypingCard title="Ujian" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={exam} pagination={false}>
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
                  <Button
                    type="primary"
                    shape="circle"
                    icon="container"
                    title="Ujian Sudah Dikerjakan"
                    disabled
                  />
                ) : (
                  <Button
                    type="primary"
                    shape="circle"
                    icon="container"
                    title="Ujian Sekarang"
                    onClick={() => handleDoExam(row)}
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

export default StudentExam;
