/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Table, Tabs, message } from "antd";
import TypingCard from "@/components/TypingCard";
import { getQuestionsByRPSQuiz1, getQuiz } from "@/api/quiz";
import { getAllCriteriaValueByQuestion } from "@/api/criteriaValue";
import { getRPS } from "@/api/rps";
import { reqUserInfo } from "@/api/user";

const { Column } = Table;
const { TabPane } = Tabs;

const QuizGenerate = () => {
  // State declarations
  const [rps, setRps] = useState([]);
  const [quiz, setQuiz] = useState([]);
  const [userInfo, setUserInfo] = useState([]);
  const [questionsWithCriteria, setQuestionsWithCriteria] = useState([]);
  const [selectedLecturer, setSelectedLecturer] = useState("");
  const [quizId, setQuizId] = useState("");
  const [devLecturers, setDevLecturers] = useState([]);
  const [matchingRPS, setMatchingRPS] = useState(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  // Navigation handlers
  const handleNextPage = (quizId) => {
    if (!quizId) {
      message.error("Quiz ID tidak ditemukan");
      return;
    }
    navigate(`/setting-quiz/generate-quiz-step2/${quizId}`);
  };

  const handlePreviousPage = () => {
    navigate("/setting-quiz/");
  };

  // Main data fetching function
  const fetchData = async () => {
    setLoading(true);
    try {
      // Fetch all required data in parallel
      const [quizResponse, userInfoResponse, rpsResponse] = await Promise.all([
        getQuiz(),
        reqUserInfo(),
        getRPS(),
      ]);

      const { content: quizContent, statusCode: quizStatusCode } =
        quizResponse.data;
      const { content: rpsContent, statusCode: rpsStatusCode } =
        rpsResponse.data;
      const { content: userContent, statusCode: userStatusCode } =
        userInfoResponse.data;

      if (
        quizStatusCode !== 200 ||
        rpsStatusCode !== 200 ||
        userStatusCode !== 200
      ) {
        throw new Error("Failed to fetch required data");
      }

      let newDevLecturers = [];
      const processedRPSIds = new Set();

      // Process quiz content
      for (const quiz of quizContent) {
        const matchingRPS = rpsContent.find((rps) => rps.id === quiz.rps.id);

        if (matchingRPS && !processedRPSIds.has(matchingRPS.id)) {
          newDevLecturers.push(...matchingRPS.dev_lecturers);
          processedRPSIds.add(matchingRPS.id);

          setQuizId(quiz.id);
          setMatchingRPS(matchingRPS);

          try {
            // Fetch questions for this RPS
            const questionsResult = await getQuestionsByRPSQuiz1(
              matchingRPS.id
            );
            if (questionsResult.data.statusCode === 200) {
              const quizQuestions = questionsResult.data.content.filter(
                (q) => q.examType2 === "QUIZ"
              );

              // Get criteria values for each question
              const questionsWithCriteriaValues = await Promise.all(
                quizQuestions.map(async (question) => {
                  try {
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
                  } catch (error) {
                    console.error(
                      `Error fetching criteria for question ${question.id}:`,
                      error
                    );
                    return {
                      ...question,
                      criteriaValues: [],
                    };
                  }
                })
              );

              setQuestionsWithCriteria(questionsWithCriteriaValues);
            }
          } catch (error) {
            console.error("Error processing RPS questions:", error);
            message.error(
              `Gagal memuat pertanyaan untuk RPS ${matchingRPS.id}`
            );
          }
        }
      }

      setDevLecturers(newDevLecturers);
      setUserInfo(userContent);
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

  // Tab change handler
  const selectLecture = (lecturerId) => {
    const lecturer = devLecturers.find((l) => l.id === lecturerId);
    if (lecturer) {
      setSelectedLecturer(lecturer.name);
    }
  };

  // Criteria configuration
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

  // Generate table columns
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
            {criteriaValue ? criteriaValue[`value${index + 1}`]?.name : "N/A"}
          </span>
        );
      }}
    />
  ));

  // Additional columns for question details
  const questionColumns = [
    <Column title="ID" dataIndex="id" key="id" align="center" />,
    <Column title="Pertanyaan" dataIndex="title" key="title" align="center" />,
    ...columns,
  ];

  return (
    <div className="quiz-generate-container">
      <TypingCard source="Daftar Nilai Quiz Berdasarkan Dosen Yang Menilai" />

      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          marginTop: 20,
        }}
      >
        <Button type="primary" onClick={handlePreviousPage} disabled={loading}>
          Previous Page
        </Button>
        <Button
          type="primary"
          onClick={() => handleNextPage(quizId)}
          disabled={loading || !quizId}
        >
          Next Page
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
              columns={questionColumns}
              pagination={false}
              rowKey="id"
              loading={loading}
              variant
              scroll={{ x: true }}
            />
          </TabPane>
        ))}
      </Tabs>
    </div>
  );
};

export default QuizGenerate;
