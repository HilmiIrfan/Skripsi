/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Row, Col, Button, Table, Select } from "antd";
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
    navigate(`/setting-quiz/generate-quiz-step4/${quizId}`);
  };

  const handlePreviousPage = (quizId) => {
    navigate(`/setting-quiz/generate-quiz-step2/${quizId}`);
  };

  const fetchData = async () => {
    try {
      const quizResponse = await getQuiz();
      const { content: quizContent, statusCode: quizStatusCode } =
        quizResponse.data;

      const userInfoResponse = await reqUserInfo();
      const { content: userInfoContent, statusCode: userInfoStatusCode } =
        userInfoResponse.data;

      const rpsResponse = await getRPS();
      const { content: rpsContent, statusCode: rpsStatusCode } =
        rpsResponse.data;

      let tempDevLecturers = [];

      if (quizStatusCode === 200 && rpsStatusCode === 200) {
        quizContent.forEach((quiz) => {
          const matchingRPS = rpsContent.find((rps) => rps.id === quiz.rps.id);
          if (matchingRPS) {
            tempDevLecturers.push(...matchingRPS.dev_lecturers);
          }
          setQuizId(quiz.id);
        });
      }

      if (isMounted) {
        setDevLecturers(tempDevLecturers);
      }

      if (quizStatusCode === 200 && rpsStatusCode === 200) {
        for (const quiz of quizContent) {
          const matchingRPS = rpsContent.find((rps) => rps.id === quiz.rps.id);
          if (matchingRPS) {
            const rpsID = matchingRPS.id;
            const result = await getQuestionsByRPSQuiz1(rpsID);
            const { content, statusCode } = result.data;

            if (statusCode === 200) {
              const quizQuestions = content.filter(
                (question) => question.examType2 === "QUIZ"
              );

              const questionsWithCriteria = await Promise.all(
                quizQuestions.map(async (question) => {
                  const criteriaResult = await getAllCriteriaValueByQuestion(
                    question.id
                  );
                  if (criteriaResult.data.statusCode === 200) {
                    question.criteriaValues = criteriaResult.data.content;
                  } else {
                    question.criteriaValues = [];
                  }
                  return question;
                })
              );

              if (isMounted) {
                setQuestionsWithCriteria(questionsWithCriteria);
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

  const selectLecture = (lecturerName) => {
    setSelectedLecturer(lecturerName);
    setIsMounted(false);
    fetchData();
  };

  const calculateAverage = (values) => {
    let sum = 0;
    let count = 0;

    for (let i = 0; i < values.length; i++) {
      if (values[i]?.value1?.average !== undefined) {
        sum += parseFloat(values[i].value1.average);
        count++;
      }
    }

    return count > 0 ? (sum / count).toFixed(2) : "N/A";
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

  const calculateAverageOfAverages = (criteriaValues) => {
    const averages = criteriaValues.reduce((acc, curr) => {
      criteriaNames.forEach((name, index) => {
        const key = `value${index + 1}`;
        if (curr[key] && "average" in curr[key]) {
          if (!acc[name]) {
            acc[name] = {
              sum: 0,
              count: 0,
            };
          }
          acc[name].sum += parseFloat(curr[key].average);
          acc[name].count += 1;
        }
      });
      return acc;
    }, {});

    criteriaNames.forEach((name) => {
      if (averages[name]) {
        averages[name] = (averages[name].sum / averages[name].count).toFixed(4);
      }
    });

    return averages;
  };

  const columns = [
    {
      title: "Pertanyaan",
      dataIndex: "title",
      key: "title",
    },
    ...criteriaNames.map((name, index) => ({
      title: name,
      key: `value${index + 1}Name`,
      render: (text, record) => {
        const averages = calculateAverageOfAverages(record.criteriaValues);
        return <span>{averages[name]}</span>;
      },
    })),
  ];

  const filteredData = questionsWithCriteria
    .filter((item) => item.user === selectedLecturer)
    .map((item) => ({
      id: item.id,
      title: item.question.title,
      user: item.user,
    }));

  return (
    <div>
      <TypingCard source="Tahap 3 yaitu mengubah numerik menjadi matrix fuzzy" />
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          marginTop: 20,
        }}
      >
        <div>
          <Button type="primary" onClick={() => handlePreviousPage(quizId)}>
            Tahap 2
          </Button>
        </div>
        <div>
          <Button type="primary" onClick={() => handleNextPage(quizId)}>
            Tahap 4
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
