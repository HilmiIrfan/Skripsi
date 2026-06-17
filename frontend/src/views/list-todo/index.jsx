/* eslint-disable no-unused-vars */
import { useState, useEffect } from "react";
import { Card, Table } from "antd";
import TypingCard from "@/components/TypingCard";
import { getQuiz } from "@/api/quiz";
import { getRPS } from "@/api/rps";
import { reqUserInfo } from "@/api/user";

const { Column } = Table;

const ListTodo = () => {
  const [rps, setRps] = useState([]);
  const [quiz, setQuiz] = useState([]);
  const [userInfo, setUserInfo] = useState([]);
  const [quizMessages, setQuizMessages] = useState([]);

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
      const { content: userInfoContent, statusCode: userInfoStatusCode } =
        userInfoResponse.data;
      const { content: rpsContent, statusCode: rpsStatusCode } =
        rpsResponse.data;

      let devLecturerIds = [];

      if (quizStatusCode === 200 && rpsStatusCode === 200) {
        quizContent.forEach((quiz) => {
          const matchingRPS = rpsContent.find((rps) => rps.id === quiz.rps.id);
          if (matchingRPS) {
            devLecturerIds = matchingRPS.dev_lecturers.map(
              (lecturer) => lecturer.id
            );
          }
        });
      }

      const { id: userId } = userInfoResponse.data;

      if (quizStatusCode === 200 && devLecturerIds.includes(userId)) {
        const messages = quizContent
          .filter((item) => rpsContent.some((rps) => rps.id === item.rps.id))
          .map((item) => ({
            message: item.message,
            quiz: item.name,
            rpsName: item.rps.name,
            rpsId: item.rps.id,
            type_quiz: item.type_quiz,
            devLecturer: rpsContent.find((rps) => rps.id === item.rps.id)
              .dev_lecturers,
          }));

        setQuizMessages(messages);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    Promise.all([fetchQuiz(), fetchRps(), fetchUserInfo(), fetchData()]);
  }, []);

  const title = <span>List Todo</span>;
  const cardContent = `Di sini, Anda dapat mengetahui list yang harus dikerjakan oleh Anda.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Kuis" source={cardContent} />
      <br />
      <Card title={title}>
        <Table
          variant
          rowKey={(record, index) => index}
          dataSource={quizMessages}
          pagination={false}
        >
          <Column
            title="Quiz Messages"
            dataIndex="message"
            key="message"
            align="center"
          />
          <Column
            title="Quiz Name"
            dataIndex="quiz"
            key="rpsName"
            align="center"
          />
          <Column
            title="status"
            dataIndex=""
            key=""
            align="center"
            render={() => <div style={{ color: "red" }}>Not Completed</div>}
          />
          <Column
            title="Link Penilaian"
            key="action"
            align="center"
            render={(text, record) => (
              <a
                href={`http://localhost:5173/#/index/question/${record.type_quiz}/${record.rpsId}`}
              >
                Link Penilaian
              </a>
            )}
          />
        </Table>
      </Card>
    </div>
  );
};

export default ListTodo;
