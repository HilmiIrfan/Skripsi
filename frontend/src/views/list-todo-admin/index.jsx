/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Select, Card, Table } from "antd";
import TypingCard from "@/components/TypingCard";
import { getQuiz } from "@/api/quiz";
import { getRPS } from "@/api/rps";
import { reqUserInfo } from "@/api/user";

const { Column } = Table;

const ListTodoAdmin = () => {
  const [rps, setRps] = useState([]);
  const [quiz, setQuiz] = useState([]);
  const [userInfo, setUserInfo] = useState([]);
  const [quizMessages, setQuizMessages] = useState([]);
  const [lecturerColumns, setLecturerColumns] = useState([]);
  const [selectedQuiz, setSelectedQuiz] = useState("");

  const fetchQuiz = async () => {
    try {
      const result = await getQuiz();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setQuiz(content);
      }
    } catch (error) {
      console.error("Error fetching quiz:", error);
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
      console.error("Error fetching RPS:", error);
    }
  };

  const fetchUserInfo = async () => {
    try {
      const result = await reqUserInfo();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setUserInfo(content);
      }
    } catch (error) {
      console.error("Error fetching user info:", error);
    }
  };

  const fetchData = async () => {
    try {
      const [quizResponse, userInfoResponse, rpsResponse] = await Promise.all([
        getQuiz(),
        reqUserInfo(),
        getRPS(),
      ]);

      const { content: quizContent, statusCode: quizStatusCode } =
        quizResponse.data;
      const { content: rpsContent, statusCode: rpsStatusCode } =
        rpsResponse.data;

      if (quizStatusCode === 200) {
        const maxLecturers = Math.max(
          ...rpsContent.map((rps) =>
            rps.dev_lecturers ? rps.dev_lecturers.length : 0
          )
        );

        const newLecturerColumns = Array.from(
          { length: maxLecturers },
          (_, i) => ({
            title: `Lecturer ${i + 1}`,
            dataIndex: `lecturer${i + 1}`,
            key: `lecturer${i + 1}`,
            align: "center",
          })
        );

        setLecturerColumns(newLecturerColumns);

        const messages = quizContent
          .filter(
            (item) =>
              item.name === selectedQuiz &&
              rpsContent.some((rps) => rps.id === item.rps.id)
          )
          .map((item) => {
            const rps = rpsContent.find((rps) => rps.id === item.rps.id);
            const message = {
              message: item.message,
              quiz: item.name,
              rpsName: rps ? rps.name : "",
            };

            rps.dev_lecturers.forEach((lecturer, i) => {
              message[`lecturer${i + 1}`] = lecturer.name;
            });

            return message;
          });

        setQuizMessages(messages);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchQuiz();
    fetchRps();
    fetchUserInfo();
  }, []);

  useEffect(() => {
    if (selectedQuiz) {
      fetchData();
    }
  }, [selectedQuiz]);

  const handleQuizSelect = (quizName) => {
    setSelectedQuiz(quizName);
  };

  const title = <span>Monitor List Todo Dosen untuk Admin</span>;
  const cardContent = `Di sini, Anda sebagai admin dapat mengetahui progres yang dilakukan dosen untuk persiapan membuat ujian`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Kuis" source={cardContent} />
      <br />
      <Card title={title}>
        <Select
          onChange={handleQuizSelect}
          style={{ width: 200, marginBottom: 20 }}
        >
          {quiz.map((quizItem, index) => (
            <Select.Option key={index} value={quizItem.name}>
              {quizItem.name}
            </Select.Option>
          ))}
        </Select>

        <Table
          variant
          rowKey={(record, index) => index}
          dataSource={quizMessages}
          pagination={false}
        >
          {lecturerColumns.map((column) => (
            <Column
              key={column.key}
              title={column.title}
              dataIndex={column.dataIndex}
              align="center"
              render={(text) => (
                <div>
                  {text}
                  <div style={{ color: "red" }}>Not Completed</div>
                </div>
              )}
            />
          ))}

          <Column
            title="Quiz Name"
            dataIndex="quiz"
            key="rpsName"
            align="center"
          />

          <Column
            title="status"
            key="status"
            align="center"
            render={() => <div style={{ color: "red" }}>Not Completed</div>}
          />
        </Table>
      </Card>
    </div>
  );
};

export default ListTodoAdmin;
