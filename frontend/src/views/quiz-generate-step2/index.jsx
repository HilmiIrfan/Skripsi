/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Table, Tabs, message } from "antd";
import TypingCard from "@/components/TypingCard";
import { getQuestionsByRPSQuiz1 } from "@/api/quiz";
import { getAllCriteriaValueByQuestion } from "@/api/criteriaValue";
import { getQuiz } from "@/api/quiz";
import { getRPS } from "@/api/rps";
import { reqUserInfo } from "@/api/user";

const { Column } = Table;
const { TabPane } = Tabs;

const QuizGenerate = () => {
  const [rps, setRps] = useState([]);
  const [quiz, setQuiz] = useState([]);
  const [quizId, setQuizId] = useState("");
  const [userInfo, setUserInfo] = useState([]);
  const [questionsWithCriteria, setQuestionsWithCriteria] = useState([]);
  const [selectedLecturer, setSelectedLecturer] = useState("");
  const [devLecturers, setDevLecturers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [matchingRPS, setMatchingRPS] = useState(null);

  const navigate = useNavigate();

  const handleNextPage = (quizId) => {
    navigate(`/setting-quiz/generate-quiz-step3/${quizId}`);
  };

  const handlePreviousPage = (quizId) => {
    navigate(`/setting-quiz/generate-quiz/${quizId}`);
  };

  const fetchData = async () => {
    setLoading(true);
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

      if (quizStatusCode === 200 && rpsStatusCode === 200) {
        const processedRPSIds = new Set();
        let newDevLecturers = [];

        for (const quiz of quizContent) {
          const matchingRPS = rpsContent.find((rps) => rps.id === quiz.rps.id);
          if (matchingRPS) {
            if (!processedRPSIds.has(matchingRPS.id)) {
              newDevLecturers.push(...matchingRPS.dev_lecturers);
              processedRPSIds.add(matchingRPS.id);
            }

            setQuizId(quiz.id);
            setMatchingRPS(matchingRPS);

            try {
              const result = await getQuestionsByRPSQuiz1(matchingRPS.id);
              if (result.data.statusCode === 200) {
                const quizQuestions = result.data.content.filter(
                  (question) => question.examType2 === "QUIZ"
                );

                const questionsWithCriteriaData = await Promise.all(
                  quizQuestions.map(async (question) => {
                    const criteriaResult = await getAllCriteriaValueByQuestion(
                      question.id
                    );
                    return {
                      ...question,
                      criteriaValues:
                        criteriaResult.data.statusCode === 200
                          ? criteriaResult.data.content
                          : [],
                    };
                  })
                );

                setQuestionsWithCriteria(questionsWithCriteriaData);
              }
            } catch (error) {
              console.error("Error fetching question data:", error);
              message.error("Gagal memuat data pertanyaan");
            }
          }
        }

        setDevLecturers(newDevLecturers);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
      message.error("Gagal memuat data");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const selectLecture = (lecturerId) => {
    const lecturer = devLecturers.find((l) => l.id === lecturerId);
    if (lecturer) {
      setSelectedLecturer(lecturer.name);
    }
  };

  const criteriaNames = [
    "Evaluation",
    "Synthesis",
    "Comprehension",
    "Analysis",
    "Difficulty",
    "Reliability",
    "Discrimination",
    "Application",
    "Knowledge",
  ];

  const columns = Array.from({ length: 9 }, (_, index) => (
    <Column
      title={criteriaNames[index]}
      key={`value${index + 1}Name`}
      render={(text, record) => {
        const criteriaValue = record.criteriaValues?.find(
          (cv) => cv.user === selectedLecturer
        );
        return (
          <span>
            {criteriaValue
              ? criteriaValue[`value${index + 1}`]?.average
              : "N/A"}
          </span>
        );
      }}
    />
  ));

  return (
    <div className="quiz-generate-container">
      <TypingCard source="Daftar Nilai Quiz Berdasarkan Dosen Yang Menilai Dalam bentuk numerik" />

      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          marginTop: 20,
        }}
      >
        <Button
          type="primary"
          onClick={() => handlePreviousPage(quizId)}
          disabled={loading}
        >
          Tahap 1
        </Button>
        <Button
          type="primary"
          onClick={() => handleNextPage(quizId)}
          disabled={loading}
        >
          Tahap 3
        </Button>
      </div>

      <br />
      <br />

      <Tabs
        onChange={selectLecture}
        style={{ marginBottom: 20 }}
        destroyInactiveTabPane
      >
        {devLecturers.map((lecturer) => (
          <TabPane tab={lecturer.name} key={lecturer.id}>
            <Table
              dataSource={questionsWithCriteria}
              pagination={false}
              rowKey="id"
              loading={loading}
              variant
              scroll={{ x: true }}
            >
              <Column
                title="ID"
                dataIndex="id"
                key="id"
                align="center"
                fixed="left"
              />
              <Column
                title="Pertanyaan"
                dataIndex="title"
                key="title"
                align="center"
                fixed="left"
              />
              {columns}
            </Table>
          </TabPane>
        ))}
      </Tabs>
    </div>
  );
};

export default QuizGenerate;
