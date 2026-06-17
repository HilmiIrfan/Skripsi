/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Row, Col, Button, Card, Table, Select } from "antd";
import { useNavigate } from "react-router-dom";
import TypingCard from "@/components/TypingCard";
import { getQuestionsByRPSQuiz1 } from "@/api/quiz";
import { getAllCriteriaValueByQuestion } from "@/api/criteriaValue";
import { getQuiz } from "@/api/quiz";
import { getRPS } from "@/api/rps";
import { reqUserInfo } from "@/api/user";

const QuizGenerate = () => {
  const navigate = useNavigate();
  const [rps, setRps] = useState([]);
  const [quiz, setQuiz] = useState([]);
  const [userInfo, setUserInfo] = useState([]);
  const [quizId, setQuizId] = useState("");
  const [questionsWithCriteria, setQuestionsWithCriteria] = useState([]);
  const [selectedLecturer, setSelectedLecturer] = useState("");
  const [devLecturers, setDevLecturers] = useState([]);
  const [isMounted, setIsMounted] = useState(true);

  const handleNextPage = (quizId) => {
    navigate(`/setting-quiz/generate-quiz-step5/${quizId}`);
  };

  const handlePreviousPage = (quizId) => {
    navigate(`/setting-quiz/generate-quiz-step3/${quizId}`);
  };

  const calculateMinMax = (questionsWithCriteria) => {
    const calculateTop3MinMax = (array) => {
      const top3 = array.slice(0, 3);
      return {
        min: Math.min(...top3),
        max: Math.max(...top3),
      };
    };

    const valueTypes = Array.from({ length: 9 }, (_, i) => i + 1);
    const result = {};

    valueTypes.forEach((valueType) => {
      const averagesArray = questionsWithCriteria.map(
        (question) => question[`avgOfAvgValue${valueType}`]
      );
      const { min, max } = calculateTop3MinMax(averagesArray);
      result[`minValue${valueType}`] = min;
      result[`maxValue${valueType}`] = max;
    });

    return result;
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

      if (quizStatusCode === 200 && rpsStatusCode === 200) {
        const tempDevLecturers = [];

        for (const quiz of quizContent) {
          const matchingRPS = rpsContent.find((rps) => rps.id === quiz.rps.id);
          if (matchingRPS) {
            tempDevLecturers.push(...matchingRPS.dev_lecturers);
            setQuizId(quiz.id);

            const result = await getQuestionsByRPSQuiz1(matchingRPS.id);
            if (result.data.statusCode === 200) {
              const quizQuestions = result.data.content.filter(
                (question) => question.examType2 === "QUIZ"
              );

              const questionsWithCriteria = await Promise.all(
                quizQuestions.map(async (question) => {
                  const criteriaResult = await getAllCriteriaValueByQuestion(
                    question.id
                  );

                  if (criteriaResult.data.statusCode === 200) {
                    question.criteriaValues = criteriaResult.data.content;

                    // Calculate averages for all 9 values
                    for (let i = 1; i <= 9; i++) {
                      const totalAvg = question.criteriaValues.reduce(
                        (sum, response) =>
                          sum + parseFloat(response[`value${i}`].average),
                        0
                      );
                      question[`avgOfAvgValue${i}`] =
                        totalAvg / question.criteriaValues.length;
                    }
                  } else {
                    question.criteriaValues = [];
                  }
                  return question;
                })
              );

              if (isMounted) {
                setQuestionsWithCriteria(questionsWithCriteria);
                setDevLecturers(tempDevLecturers);
              }
            }
          }
        }
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    setIsMounted(true);
    fetchData();
    return () => setIsMounted(false);
  }, []);

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

  const columns = [
    {
      title: "Pertanyaan",
      dataIndex: "title",
      key: "title",
    },
    ...criteriaNames.map((name, index) => ({
      title: name,
      key: `avgOfAvgValue${index + 1}`,
      render: (text, record) => {
        if (record.criteriaValues?.length > 0) {
          const valueKey = `value${index + 1}`;
          const tempAveragesArray = record.criteriaValues.map((response) =>
            parseFloat(response[valueKey].average)
          );

          const totalAvg = tempAveragesArray.reduce((sum, avg) => sum + avg, 0);
          const avgOfAvgValue = totalAvg / tempAveragesArray.length;

          const minMaxValues = calculateMinMax(questionsWithCriteria);
          const result =
            (0.11 * (minMaxValues[`maxValue${index + 1}`] - avgOfAvgValue)) /
            (minMaxValues[`maxValue${index + 1}`] -
              minMaxValues[`minValue${index + 1}`]);

          return <div>Calculated Value: {result.toFixed(3)}</div>;
        }
        return "N/A";
      },
    })),
  ];

  return (
    <div>
      <TypingCard source="Tahap 4 Menormalisasikan matrix fuzzy yang di dapat" />
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          marginTop: 20,
        }}
      >
        <div>
          <Button type="primary" onClick={() => handlePreviousPage(quizId)}>
            Tahap 3
          </Button>
        </div>
        <div>
          <Button type="primary" onClick={() => handleNextPage(quizId)}>
            Tahap 5
          </Button>
        </div>
      </div>
      <br />
      <br />
      <Table
        dataSource={questionsWithCriteria}
        columns={columns}
        pagination={false}
        rowKey="id"
      />
    </div>
  );
};

export default QuizGenerate;
